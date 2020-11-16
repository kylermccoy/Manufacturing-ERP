package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.model.*;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import com.kennuware.erp.manufacturing.service.model.repository.RequestRepository;
import com.kennuware.erp.manufacturing.service.util.QueueManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.kennuware.erp.manufacturing.service.util.RequestSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class RequestController {

  private final RequestRepository requestRepository; // Repository of requests
  private final QueueRepository queueRepository; // Queue repository
  private final ObjectMapper mapper; // Provides functionality for reading and writing JSON
  private final QueueManager queueManager;


  /**
   * Creates a new instance of RequestController
   *
   * @param requestRepository Repository of requests
   * @param queueRepository Queue repository
   * @param mapper Mapper
   */
  RequestController(RequestRepository requestRepository, QueueRepository queueRepository,
      ObjectMapper mapper, QueueManager queueManager) {
    this.requestRepository = requestRepository;
    this.queueRepository = queueRepository;
    this.mapper = mapper;
    this.queueManager = queueManager;
  }


  /**
   * Lists the requests in the queue
   * @return List of requests
   */
  @GetMapping
  List<Request> listRequests() {
    return requestRepository.findAll();
  }


  /**
   * Gathers data on specific request
   * @param id Unique request ID
   * @return Specific request
   */
  @GetMapping(path = "/{id}")
  Request getRequest(@PathVariable long id) {
    return requestRepository.findById(id).orElseThrow(() -> new GenericJSONException("Request [" + id + "] was not found"));
  }


  /**
   * Adds a request to the repository
   * @param request Request to be added
   * @return JSON success or failure message
   */
  @PostMapping
  @Operation(summary = "Adds a request to the repository")
  Request addRequest(@Parameter(description = "Request to be added") @RequestBody Request request) {
    Request newRequest = requestRepository.save(request);
    Queue q = queueRepository.findByName(QueueController.QUEUE_NAME).orElseThrow(() -> new GenericJSONException("Queue does not exist"));
    q.getRequestsInQueue().add(newRequest);
    queueRepository.save(q);
    queueManager.addNextRequest(queueManager.getNextRequest());

    RestTemplate rt = new RestTemplate();

    if (request.getType() == RequestType.RECALL) {
      try {
        String[] skus_recall = new String[1];
        int[] quantities_recall = new int[1];
        skus_recall[0] = request.getProduct().getId().toString();
        quantities_recall[0] = (int)request.getQuantity();
        JsonNode response = rt.postForObject("http://demo-1602622154660.azurewebsites.net/api/transfer/products/out?sku="
                        + skus_recall[0] + "&quantity="
                        + quantities_recall[0] + "&location=MANUFACTURING",
                null, JsonNode.class);
      }
      catch (NullPointerException ignored) {

      }
    }
    else if (request.getType() == RequestType.ORDER) {
      try {
        List<RecipeComponent> recipeComponents = request.getProduct().getRecipe().getComponents();
        ArrayList<String> skus = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        for (RecipeComponent component : recipeComponents) {
          skus.add(Long.toString(component.getItem().getId()));
          quantities.add((int)component.getQuantity());
        }
        JsonNode response = rt.postForObject("http://demo-1602622154660.azurewebsites.net/api/transfer/parts/out?sku="
                        + Arrays.toString(skus.toArray()) + "&quantity="
                        + Arrays.toString(quantities.toArray()) + "&location=MANUFACTURING",
                null, JsonNode.class);
      }
      catch (NullPointerException ignored) {

      }
    }

    return newRequest;
  }


  /**
   * Deletes a request from queue
   * @param id Request ID
   * @return JSON success or failure
   */
  @DeleteMapping(path = "/{id}")
  @Operation(summary = "Deletes a request from queue")
  ObjectNode deleteRequest(@Parameter(description = "Request ID") @PathVariable long id) {
    final ObjectNode response = mapper.createObjectNode();
    final String message;
    final boolean success;
    // does the request exist in the first place
    if (requestRepository.existsById(id)) {
      Optional<Queue> queue = queueRepository.findByName(QueueController.QUEUE_NAME);
      if (queue.isPresent()) {
        Queue q = queue.get();
        List<Request> reqInQueue = q.getRequestsInQueue();
        boolean contains = reqInQueue.removeIf(r -> r.getId() == id);
        queueRepository.save(q);
      }
      requestRepository.deleteById(id);
      success = !requestRepository.existsById(id);
      // did it actually get deleted
      if (!success) {
        message = "Request was not deleted!";
        log.error("Request " + id + " was not successfully deleted");
      } else {
        message = "Request was successfully deleted";
      }
    } else {
      success = false;
      message = "Request does not exist";
    }
    response.put("success", success);
    response.put("message", message);
    return response;
  }

}
