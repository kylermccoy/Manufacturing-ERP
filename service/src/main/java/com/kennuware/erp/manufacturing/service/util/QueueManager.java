package com.kennuware.erp.manufacturing.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.controller.QueueController;
import com.kennuware.erp.manufacturing.service.model.CurrentQueueItem;
import com.kennuware.erp.manufacturing.service.model.Queue;
import com.kennuware.erp.manufacturing.service.model.Request;
import com.kennuware.erp.manufacturing.service.model.repository.CurrentQueueItemRepository;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import com.kennuware.erp.manufacturing.service.model.repository.RequestRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import org.springframework.stereotype.Component;

@Component
public class QueueManager {

  private Long currentID = -1L;
  private final Timer timer;
  private StatusTimerTask task;
  private final QueueRepository queueRepository;
  private final RequestRepository requestRepository;
  private final CurrentQueueItemRepository currentItemRepository;
  private final ObjectMapper mapper;

  QueueManager(QueueRepository queueRepository, CurrentQueueItemRepository currentQueueItemRepository,
      RequestRepository requestRepository, ObjectMapper mapper) {
    this.timer = new Timer();
    this.queueRepository = queueRepository;
    this.currentItemRepository = currentQueueItemRepository;
    this.requestRepository = requestRepository;
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
    // do we got a current item?
    if (!currentItemRepository.existsById(currentID)) {
      if (!isRunning()) { // resuming
        currentID = currentItemRepository.saveAndFlush(new CurrentQueueItem(request)).getId();
        startTimerForTask();
      }
    }
  }

  public boolean isRunning() {
    return task != null;
  }

  public synchronized void startTimerForTask() {
    Optional<Queue> queue = queueRepository.findByName(QueueController.QUEUE_NAME);
    if (queue.isEmpty() || !queue.get().isRunning()) {
      // queue not running, don't start
      return;
    }
    task = new StatusTimerTask() {
      @Override
      void doWork() {
        tickDown();
      }
    };

    Optional<CurrentQueueItem> current = currentItemRepository.findById(currentID);
    if (current.isEmpty()) {
      return;
    }
    timer.scheduleAtFixedRate(task, 0, 60000); // every 60 seconds tick down
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
      completeTask();
    } else {
      current.setTimeRemaining(remainingTime - 1);
      currentItemRepository.save(current);
    }
  }

  public synchronized void stopCurrent() {
    if(currentID < 0) {
      return;
    }
    timer.cancel();
    task = null;
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
    currentID = -1L;
    task = null;
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
    if (queue.isPresent()) {
      List<Request> reqs = queue.get().getRequestsInQueue();
      Request r = reqs.size() > 0 ? queue.get().getRequestsInQueue().remove(0) : null;
      queueRepository.save(queue.get());
      return r;
    }
    return null;
  }

}
