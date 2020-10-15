package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.model.Queue;
import com.kennuware.erp.manufacturing.service.model.Request;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import com.kennuware.erp.manufacturing.service.model.repository.RequestRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class RequestController {

  private final RequestRepository requestRepository;
  private final QueueRepository queueRepository;
  private final ObjectMapper mapper;
  
  RequestController(RequestRepository requestRepository, QueueRepository queueRepository, ObjectMapper mapper) {
    this.requestRepository = requestRepository;
    this.queueRepository = queueRepository;
    this.mapper = mapper;
  }
  
  @GetMapping
  List<Request> listRequests() {
    return requestRepository.findAll();
  }
  
  @GetMapping(path = "/{id}")
  Request getRequest(@PathVariable long id) {
    return requestRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  @PostMapping
  Request addRequest(@RequestBody Request request) {
    return requestRepository.save(request);
  }

  @DeleteMapping(path = "/{id}")
  ObjectNode deleteRequest(@PathVariable long id) {
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
