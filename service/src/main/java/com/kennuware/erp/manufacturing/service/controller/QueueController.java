package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.model.Order;
import com.kennuware.erp.manufacturing.service.model.Queue;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/queue")
public class QueueController {
  //TODO: probably refactor a lot of duplicate code
  //TODO: make better use of Optional.orElse
  //TODO: Create a json response object for entity not existing

  final private QueueRepository queueRepository;
  final private ObjectMapper mapper;

  QueueController(QueueRepository queueRepository, ObjectMapper mapper) {
    this.queueRepository = queueRepository;
    this.mapper = mapper;
  }

  @GetMapping
  List<Order> getOrdersInQueue(@RequestParam String queueName) {
    Optional<Queue> queue = queueRepository.findByName(queueName);
    if (queue.isPresent()) {
      return queue.get().getOrdersInQueue();
    } else {
      return Collections.emptyList();
    }
  }

  @GetMapping("/start")
  ObjectNode startQueue(@RequestParam String queueName) {
    ObjectNode node = mapper.createObjectNode();
    boolean success = false;
    String message = "";
    Optional<Queue> queue = queueRepository.findByName(queueName);
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
      message = "Queue " + queueName + " does not exist";
    }
    node.put("success", success);
    node.put("message", message);
    return node;
  }

  @GetMapping("/stop")
  ObjectNode stopQueue(@RequestParam String queueName) {
    ObjectNode node = mapper.createObjectNode();
    boolean success = false;
    String message = "";
    Optional<Queue> queue = queueRepository.findByName(queueName);
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
      message = "Queue " + queueName + " does not exist";
    }
    node.put("success", success);
    node.put("message", message);
    return node;
  }

  @PostMapping("/add")
  ObjectNode addOrderToQueue(@RequestParam String queueName, @RequestBody Order order) {
    ObjectNode node = mapper.createObjectNode();
    boolean success = false;
    String message = "";
    Optional<Queue> queue = queueRepository.findByName(queueName);
    if (queue.isPresent()) {
      Queue q = queue.get();
      q.getOrdersInQueue().add(order);
      queueRepository.save(q);
      success = true;
    } else {
      message = "Queue " + queueName + " does not exist";
    }
    node.put("success", success);
    node.put("message", message);
    return node;
  }

  @DeleteMapping("/{id}")
  ObjectNode removeOrderFromQueue(@PathVariable long id, @RequestParam String queueName) {
    ObjectNode node = mapper.createObjectNode();
    boolean success = false;
    String message = "";
    Optional<Queue> queue = queueRepository.findByName(queueName);
    if (queue.isPresent()) {
      Queue q = queue.get();
      List<Order> ordersInQueue = q.getOrdersInQueue();
      // see if the order is in the queue
      if (ordersInQueue.stream().anyMatch(o -> o.getId() == id)) {
        ordersInQueue.removeIf(o -> o.getId() == id);
        success = true;
      } else {
        message = "Order " + id + " was not in queue " + queueName;
      }
      // save the updated list
      queueRepository.save(q);
    } else {
      message = "Queue " + queueName + " does not exist";
    }
    node.put("success", success);
    node.put("message", message);
    return node;
  }

  @PostMapping("/create")
  Queue createQueue(@RequestParam String queueName) {
    Queue q = new Queue();
    q.setName(queueName);
    q.setOrdersInQueue(Collections.emptyList());q.setRunning(false);
    return queueRepository.save(q);
  }

}
