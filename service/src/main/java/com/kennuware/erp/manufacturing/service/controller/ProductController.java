package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.model.Product;
import com.kennuware.erp.manufacturing.service.model.repository.ProductRepository;
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
@RequestMapping(path = "/products")
public class ProductController {

  ProductRepository productRepository;
  ObjectMapper mapper;

  ProductController(ProductRepository productRepository, ObjectMapper mapper) {
    this.productRepository = productRepository;
    this.mapper = mapper;
  }

  @GetMapping(path = "/list")
  List<Product> listProducts() {
    return productRepository.findAll();
  }

  @GetMapping(path = "/{id}")
  Product getProduct(@PathVariable long id) {
    return productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  @PostMapping
  Product addProduct(@RequestBody Product product) {
    return productRepository.save(product);
  }

  @DeleteMapping(path = "/{id}")
  ObjectNode deleteProduct(@PathVariable long id) {
    final ObjectNode response = mapper.createObjectNode();
    final String message;
    final boolean success;
    // does the product exist in the first place
    if (productRepository.existsById(id)) {
      productRepository.deleteById(id);
      success = !productRepository.existsById(id);
      // did it actually get deleted
      if (!success) {
        message = "Product was not deleted!";
        log.error("Product " + id + " was not successfully deleted");
      } else {
        message = "Product was successfully deleted";
      }
    } else {
      success = false;
      message = "Product does not exist";
    }
    response.put("success", success);
    response.put("message", message);
    return response;
  }

}
