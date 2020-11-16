package com.kennuware.erp.manufacturing.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.controller.QueueController;
import com.kennuware.erp.manufacturing.service.model.CurrentQueueItem;
import com.kennuware.erp.manufacturing.service.model.Product;
import com.kennuware.erp.manufacturing.service.model.Queue;
import com.kennuware.erp.manufacturing.service.model.Request;
import com.kennuware.erp.manufacturing.service.model.repository.CurrentQueueItemRepository;
import com.kennuware.erp.manufacturing.service.model.repository.ProductRepository;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import com.kennuware.erp.manufacturing.service.model.repository.RequestRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class QueueManager {

  private Long currentID = -1L;
  private final ScheduledExecutorService executor;
  private ScheduledFuture<?> task;
  private final QueueRepository queueRepository;
  private final RequestRepository requestRepository;
  private final ProductRepository productRepository;
  private final CurrentQueueItemRepository currentItemRepository;
  private final ObjectMapper mapper;

  QueueManager(QueueRepository queueRepository, CurrentQueueItemRepository currentQueueItemRepository,
      RequestRepository requestRepository, ProductRepository productRepository, ObjectMapper mapper) {
    this.executor = Executors.newScheduledThreadPool(1);
    this.queueRepository = queueRepository;
    this.currentItemRepository = currentQueueItemRepository;
    this.requestRepository = requestRepository;
    this.productRepository = productRepository;
    this.mapper = mapper;
  }

  public synchronized void resumeFromBoot(long id) {
    this.currentID = id;
    startTimerForTask();
  }

  public synchronized void addNextRequest(Request request) {
    // api probably returned nothing - queue empty
    if (request == null) {
      return;
    }
    // do we have a current item?
    if (!currentItemRepository.existsById(currentID)) {
      Product p = productRepository.getOne(request.getProduct().getId());
      currentID = currentItemRepository.saveAndFlush(new CurrentQueueItem(request, p)).getId();
      startTimerForTask();
    }
  }

  public synchronized void startTimerForTask() {
    Optional<Queue> queue = queueRepository.findByName(QueueController.QUEUE_NAME);
    if (queue.isEmpty() || !queue.get().isRunning()) {
      // queue not running, don't start
      return;
    }

    Optional<CurrentQueueItem> current = currentItemRepository.findById(currentID);
    if (current.isEmpty()) {
      return;
    }
    task = executor.scheduleAtFixedRate(this::tickDown, 1, 1, TimeUnit.MINUTES);
  }

  // this is unbelievably inefficient
  // better idea would probably be to store in memory but wouldn't handle crashes
  private synchronized void tickDown() {
    Optional<CurrentQueueItem> currentOptional = currentItemRepository.findById(currentID);
    if (currentOptional.isEmpty()) {
      return;
    }
    CurrentQueueItem current = currentOptional.get();
    long remainingTime = current.getTimeRemaining();
    if (remainingTime <= 1) {
      current.setTimeRemaining(0);
      completeTask();
    } else {
      current.setTimeRemaining(remainingTime - 1);
    }
    currentItemRepository.save(current);
  }

  public synchronized void stopCurrent() {
    if(currentID < 0) {
      return;
    }
    task.cancel(true);
  }

  public ObjectNode getRemainingTimeMinutes() {
    ObjectNode resp = mapper.createObjectNode();
    if (currentID < 0) {
      // no task running
      resp.put("remaining", -1);
      resp.put("requestID", -1);
      return resp;
    }
    Optional<CurrentQueueItem> currentOptional = currentItemRepository.findById(currentID);
    final long remainingTime;
    final long requestID;
    if (currentOptional.isEmpty()) {
      remainingTime = -1;
      requestID = -1;
    } else {
      CurrentQueueItem current = currentOptional.get();
      remainingTime = current.getTimeRemaining();
      requestID = current.getRequest().getId();
    }
    resp.put("remaining", remainingTime);
    resp.put("requestID", requestID);
    return resp;
  }

  private synchronized void completeTask() {
    Optional<CurrentQueueItem> current = currentItemRepository.findById(currentID);
    if (current.isEmpty()) {
      return;
    }
    Request req = current.get().getRequest();
    req.setCompleted(true);
    requestRepository.save(req);
    currentItemRepository.deleteById(currentID);
    currentID = -1L;
    task.cancel(true);
    addNextRequest(getNextRequest());
    //todo: notify others?
  }

  public synchronized Request getNextRequest() {
    // already have one | resume?
    if (currentID > 0) {
      Optional<CurrentQueueItem> next = currentItemRepository.findById(currentID);
      return next.map(CurrentQueueItem::getRequest).orElse(null);
    }
    Optional<Queue> queue = queueRepository.findByName(QueueController.QUEUE_NAME);
    if (queue.isPresent() && queue.get().isRunning()) {
      List<Request> reqs = queue.get().getRequestsInQueue();
      Request r = reqs.size() > 0 ? queue.get().getRequestsInQueue().remove(0) : null;
      queueRepository.save(queue.get());
      return r;
    }
    return null;
  }

  /**
   * Skips a certain amount of minutes, possibly skipping multiple items
   * @param minutes the amount of time to skip
   * @return the requests that were completed due to the skip
   */
  public List<Request> skipTime(long minutes) {
    List<Request> res = new ArrayList<>();
    stopCurrent();
    long skipTime = minutes;
    while (skipTime > 0) {
      Optional<CurrentQueueItem> currentQueueItemOptional = currentItemRepository.findById(currentID);
      if (currentQueueItemOptional.isEmpty()) {
        return res;
      }
      CurrentQueueItem current = currentQueueItemOptional.get();
      long itemRemainingTime = current.getTimeRemaining();
      if (skipTime >= itemRemainingTime) { // we have to do more than one item
        skipTime -= itemRemainingTime;
        current.setTimeRemaining(0);
        res.add(current.getRequest());
        completeTask();
      } else { // only have to do one
        current.setTimeRemaining(itemRemainingTime - skipTime);
        skipTime = 0;
        currentItemRepository.save(current);
      }
    }
    startTimerForTask();
    return res;
  }
}