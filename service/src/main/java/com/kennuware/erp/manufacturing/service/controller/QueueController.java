package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.model.Request;
import com.kennuware.erp.manufacturing.service.model.Queue;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import com.kennuware.erp.manufacturing.service.model.repository.RequestRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/queue")
public class QueueController {

  public static final String QUEUE_NAME = "queue";

  //TODO: probably refactor a lot of duplicate code
  //TODO: make better use of Optional.orElse
  //TODO: Create a json error response object

  private final QueueRepository queueRepository;
  private final RequestRepository requestRepository;
  private final ObjectMapper mapper;

  QueueController(QueueRepository queueRepository, RequestRepository requestRepository, ObjectMapper mapper) {
    this.queueRepository = queueRepository;
    this.requestRepository = requestRepository;
    this.mapper = mapper;
  }

  @GetMapping
  Queue getQueue() {
    return queueRepository.findByName(QUEUE_NAME).orElseThrow(EntityNotFoundException::new);
  }

  @GetMapping("/start")
  ObjectNode startQueue() {
    ObjectNode node = mapper.createObjectNode();
    boolean success = false;
    String message = "";
    Optional<Queue> queue = queueRepository.findByName(QUEUE_NAME);
    if (queue.isPresent()) {
      Queue q = queue.get();
      if (q.isRunning()) {
        success = false;
        message = "Queue is already running";
      } else {
        q.setRunning(true);
        success = true;
      }
      queueRepository.save(q);
    } else {
      message = "Queue does not exist";
    }
    node.put("success", success);
    node.put("message", message);
    return node;
  }

  @GetMapping("/stop")
  ObjectNode stopQueue() {
    ObjectNode node = mapper.createObjectNode();
    boolean success = false;
    String message = "";
    Optional<Queue> queue = queueRepository.findByName(QUEUE_NAME);
    if (queue.isPresent()) {
      Queue q = queue.get();
      if (!q.isRunning()) {
        success = false;
        message = "Queue is already stopped";
      } else {
        q.setRunning(false);
        success = true;
      }
      queueRepository.save(q);
    } else {
      message = "Queue does not exist";
    }
    node.put("success", success);
    node.put("message", message);
    return node;
  }

  @GetMapping("/requests")
  List<Request> getRequestsInQueue() {
    Optional<Queue> queue = queueRepository.findByName(QUEUE_NAME);
    if (queue.isPresent()) {
      return queue.get().getRequestsInQueue();
    } else {
      return Collections.emptyList();
    }
  }

  @PostMapping("/requests")
  ObjectNode addRequestToQueue(@RequestBody Request request) {
    ObjectNode node = mapper.createObjectNode();
    boolean success = false;
    String message = "";
    if (request.isCompleted()) {
      message = "You can not add a completed request to the queue";
    } else {
      Optional<Queue> queue = queueRepository.findByName(QUEUE_NAME);
      if (queue.isPresent()) {
        Queue q = queue.get();
        q.getRequestsInQueue().add(request);
        queueRepository.save(q);
        success = true;
      } else {
        message = "Queue does not exist";
      }
    }
    node.put("success", success);
    node.put("message", message);
    return node;
  }

  @DeleteMapping("/{id}")
  ObjectNode removeRequestFromQueue(@PathVariable long id) {
    ObjectNode node = mapper.createObjectNode();
    boolean success = false;
    String message = "";
    Optional<Queue> queue = queueRepository.findByName(QUEUE_NAME);
    if (queue.isPresent()) {
      Queue q = queue.get();
      List<Request> requestsInQueue = q.getRequestsInQueue();
      // see if the request is in the queue
      if (requestsInQueue.stream().anyMatch(o -> o.getId() == id)) {
        requestsInQueue.removeIf(o -> o.getId() == id);
        success = true;
      } else {
        message = "Request " + id + " was not in queue";
      }
      // save the updated list
      queueRepository.save(q);
    } else {
      message = "Queue does not exist";
    }
    node.put("success", success);
    node.put("message", message);
    return node;
  }

  @GetMapping({"/skip", "/completeRequest"})
  ObjectNode skipCurrentRequest() {
    ObjectNode node = mapper.createObjectNode();
    boolean success = false;
    String message = "";
    Optional<Queue> queue = queueRepository.findByName(QUEUE_NAME);
    if (queue.isPresent()) {
      Queue q = queue.get();
      List<Request> requestsInQueue = q.getRequestsInQueue();
      if (requestsInQueue.isEmpty()) {
        message = "No items in queue.";
      } else {
        Request skippedRequest = requestsInQueue.remove(0);
        skippedRequest.setCompleted(true);
        requestRepository.save(skippedRequest);
        queueRepository.save(q);
        success = true;
      }
    } else {
      message = "Queue does not exist";
    }

    /*
     * Complete a product, deliver to inventory
     */

    node.put("success", success);
    node.put("message", message);
    return node;
  }

}
