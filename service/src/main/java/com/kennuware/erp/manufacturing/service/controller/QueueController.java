package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.model.RecipeComponent;
import com.kennuware.erp.manufacturing.service.model.Request;
import com.kennuware.erp.manufacturing.service.model.Queue;
import com.kennuware.erp.manufacturing.service.model.RequestType;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import com.kennuware.erp.manufacturing.service.model.repository.RequestRepository;
import com.kennuware.erp.manufacturing.service.util.QueueManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.kennuware.erp.manufacturing.service.util.RequestSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/queue")
public class QueueController {

  public static final String QUEUE_NAME = "queue"; // Queue name

  //TODO: probably refactor a lot of duplicate code
  //TODO: make better use of Optional.orElse

  private final QueueRepository queueRepository; // Repository of queues
  private final RequestRepository requestRepository; // Repository of requests
  private final ObjectMapper mapper; // Provides functionality for reading and writing JSON
  private final QueueManager queueManager;


  /**
   * Creates a new instance of QueueController
   * @param queueRepository Repository of queue
   * @param requestRepository Repository of requests
   * @param mapper Mapper
   */
  QueueController(QueueRepository queueRepository, RequestRepository requestRepository, ObjectMapper mapper,
      QueueManager queueManager) {
    this.queueRepository = queueRepository;
    this.requestRepository = requestRepository;
    this.mapper = mapper;
    this.queueManager = queueManager;
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
        queueManager.addNextRequest(queueManager.getNextRequest());
      }
      queueRepository.save(q);
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
        queueManager.stopCurrent();
      }
      queueRepository.save(q);

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
        queueManager.addNextRequest(queueManager.getNextRequest());
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
  void skipTime(@RequestParam long minutes) {
    queueManager.skipTime(minutes);
    if (skippedRequest.getType() == RequestType.ORDER) {
      try {
        JSONObject json_order = new JSONObject();
        json_order.put("sku", skippedRequest.getProduct().getId());
        json_order.put("name", skippedRequest.getProduct().getName());
        json_order.put("refurbished", false);
        json_order.put("warehouseId", "33633938-3334-6661-2d31-3734652d3131");
        ResponseEntity<JsonNode> response = RequestSender.postForObject("http://demo-1602622154660.azurewebsites.net/api/transfer/products/in?location=MANUFACTURING",
            json_order, JsonNode.class, session);
      }
      catch (NullPointerException | JSONException e) {

      }
    }
    else if (skippedRequest.getType() == RequestType.RECALL) {
      try {
        List<RecipeComponent> recipeComponents = skippedRequest.getProduct().getRecipe().getComponents();
        for (RecipeComponent component : recipeComponents) {
          JSONObject json = new JSONObject();
          JSONObject stock = new JSONObject();
          json.put("upc", component.getItem().getId());
          stock.put("33633938-3334-6661-2d31-3734652d3131", component.getQuantity());
          json.put("stock", stock);
          ResponseEntity<JsonNode> response = RequestSender.postForObject(
              "http://demo-1602622154660.azurewebsites.net/api/transfer/parts/in?location=MANUFACTURING",
              json, JsonNode.class, session);
        }
      }
      catch (NullPointerException | JSONException e) {

      }
    }
  }


  @GetMapping("/getRemainingTime")
  ObjectNode getRemainingTime() {
    return queueManager.getRemainingTimeMinutes();
  }

}
