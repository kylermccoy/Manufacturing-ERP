package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.model.Order;
import com.kennuware.erp.manufacturing.service.model.repository.OrderRepository;
import java.util.List;
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
@RequestMapping(path = "/orders")
public class OrderController {

  private final OrderRepository orderRepository;
  private final ObjectMapper mapper;
  
  OrderController(OrderRepository orderRepository, ObjectMapper mapper) {
    this.orderRepository = orderRepository;
    this.mapper = mapper;
  }
  
  @GetMapping
  List<Order> listOrders() {
    return orderRepository.findAll();
  }
  
  @GetMapping(path = "/{id}")
  Order getOrder(@PathVariable long id) {
    return orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  @PostMapping
  Order addOrder(@RequestBody Order order) {
    return orderRepository.save(order);
  }

  @DeleteMapping(path = "/{id}")
  ObjectNode deleteOrder(@PathVariable long id) {
    final ObjectNode response = mapper.createObjectNode();
    final String message;
    final boolean success;
    // does the order exist in the first place
    if (orderRepository.existsById(id)) {
      orderRepository.deleteById(id);
      success = !orderRepository.existsById(id);
      // did it actually get deleted
      if (!success) {
        message = "Order was not deleted!";
        log.error("Order " + id + " was not successfully deleted");
      } else {
        message = "Order was successfully deleted";
      }
    } else {
      success = false;
      message = "Order does not exist";
    }
    response.put("success", success);
    response.put("message", message);
    return response;
  }
  
}
