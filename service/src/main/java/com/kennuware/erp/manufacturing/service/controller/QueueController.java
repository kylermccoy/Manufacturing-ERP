package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.model.Request;
import com.kennuware.erp.manufacturing.service.model.Queue;
import com.kennuware.erp.manufacturing.service.model.Timer;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import com.kennuware.erp.manufacturing.service.model.repository.RequestRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path = "/queue")
public class QueueController {

  public static final String QUEUE_NAME = "queue"; // Queue name

  //TODO: probably refactor a lot of duplicate code
  //TODO: make better use of Optional.orElse

  private final QueueRepository queueRepository; // Repository of queues
  private final RequestRepository requestRepository; // Repository of requests
  private final ObjectMapper mapper; // Provides functionality for reading and writing JSON
  private final Timer timer;


  /**
   * Creates a new instance of QueueController
   * @param queueRepository Repository of queue
   * @param requestRepository Repository of requests
   * @param mapper Mapper
   */
  QueueController(QueueRepository queueRepository, RequestRepository requestRepository, ObjectMapper mapper) {
    this.queueRepository = queueRepository;
    this.requestRepository = requestRepository;
    this.mapper = mapper;
    this.timer = new Timer();
  }


  /**
   * Obtains the Queue from the repository
   * @return Queue
   */
  @GetMapping
  @Operation(summary = "Obtains the Queue from the repository")
  Queue getQueue() {
    return queueRepository.findByName(QUEUE_NAME).orElseThrow(() -> new GenericJSONException("The queue does not exist"));
  }


  /**
   * Starts the manufacturing process
   * @return JSON success or failure message
   */
  @GetMapping("/start")
  @Operation(summary = "Starts the manufacturing process")
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

      List<Request> requestsInQueue = q.getRequestsInQueue();
      if (!requestsInQueue.isEmpty()){
        int duration = requestsInQueue.get(0).getProduct().getRecipe().getBuildTime();
        int quantity = (int)requestsInQueue.get(0).getQuantity();
        timer.setDuration(duration*quantity);
        timer.start();
        q.setTimeLeft(timer.getRemainingTime());

      }
    } else {
      message = "Queue does not exist";
    }
    node.put("success", success);
    node.put("message", message);
    return node;
  }


  /**
   * Stops the manufacturing process
   * @return JSON success or failure message
   */
  @GetMapping("/stop")
  @Operation(summary = "Stops the manufacturing process")
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

      timer.pause();
      q.setTimeLeft(timer.getRemainingTime());

    } else {
      message = "Queue does not exist";
    }
    node.put("success", success);
    node.put("message", message);
    return node;
  }


  /**
   * Requests a list of all requests in the queue
   * @return List of requests
   */
  @GetMapping("/requests")
  @Operation(summary = "Requests a list of all requests in the queue")
  List<Request> getRequestsInQueue() {
    Optional<Queue> queue = queueRepository.findByName(QUEUE_NAME);
    if (queue.isPresent()) {
      return queue.get().getRequestsInQueue();
    } else {
      return Collections.emptyList();
    }
  }

  /**
   * Requests the first (next up) request in the queue
   * @return List of requests
   */
  @GetMapping("/requests/next")
  @Operation(summary = "Gets the next up item in the queue")
  Request getNextRequest() {
    Optional<Queue> queue = queueRepository.findByName(QUEUE_NAME);
    if (queue.isPresent()) {
      List<Request> reqs = queue.get().getRequestsInQueue();
      return reqs.size() > 0 ? queue.get().getRequestsInQueue().remove(0) : null;
    }
    return null;
  }
  

  /**
   * Adds a request to the queue
   * @param request Request to be added
   * @return JSON success or failure message
   */
  @PostMapping("/requests")
  @Operation(summary = "Adds a request to the queue")
  ObjectNode addRequestToQueue(@Parameter(description = "Request to be added") @RequestBody Request request) {
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


  /**
   * Deletes a request from the queue
   * @param id Unique ID of Request being removed
   * @return JSON success or failure message
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "Deletes a request from the queue")
  ObjectNode removeRequestFromQueue(@Parameter(description = "Unique ID of Request being removed") @PathVariable long id) {
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


  /**
   * Skip current request in the manufacturing process
   * @return JSON success or failure message
   */
  @GetMapping({"/skip", "/completeRequest"})
  @Operation(summary = "Skip current request in the manufacturing process")
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

        int duration = requestsInQueue.get(0).getProduct().getRecipe().getBuildTime();
        timer.setDuration(duration);
        timer.start();
        q.setTimeLeft(timer.getRemainingTime());
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

  @GetMapping("/timeRemaining")
  public ObjectNode getTimeRemaining() {
    // this is super backwards and fucked to call the front end and then just return that
    RestTemplate rt = new RestTemplate();
    return rt.getForObject("http://localhost:8081/process/getRemainingTime", ObjectNode.class);
  }

}
