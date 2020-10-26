package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.model.Product;
import com.kennuware.erp.manufacturing.service.model.repository.ProductRepository;
import java.util.List;
import javax.persistence.EntityNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

  ProductRepository productRepository; // Repository of products
  ObjectMapper mapper; // Provides functionality for reading and writing JSON


  /**
   * Creates a new instance of ProductController
   * @param productRepository repository of products
   * @param mapper // Provides functionality for reading and writing JSON
   */
  ProductController(ProductRepository productRepository, ObjectMapper mapper) {
    this.productRepository = productRepository;
    this.mapper = mapper;
  }


  /**
   * Lists all products in repository
   * @return list of all products
   */
  @GetMapping
  @Operation(summary = "Lists all products in repository")
  List<Product> listProducts() {
    return productRepository.findAll();
  }


  /**
   * Gathers data of a specific product from repository
   * @param id Unique Product ID
   * @return Specified product
   */
  @GetMapping(path = "/{id}")
  @Operation(summary = "Gathers data of a specific product from repository")
  Product getProduct(@Parameter(description = "Unique Product ID") @PathVariable long id) {
    return productRepository.findById(id).orElseThrow(() -> new GenericJSONException("Product [" + id + "] was not found"));
  }


  /**
   * Adds a product to the repository
   * @param product Product to be added
   * @return
   */
  @PostMapping
  @Operation(summary = "Adds a product to the repository")
  Product addProduct(@Parameter(description = "Product to be added") @RequestBody Product product) {
    return productRepository.save(product);
  }


  /**
   * Deletes a product from the repository
   * @param id Unique Product ID
   * @return Success or failure message for deletion of product
   */
  @DeleteMapping(path = "/{id}")
  @Operation(summary = "Deletes a product from the repository")
  ObjectNode deleteProduct(@Parameter(description = "Unique Product ID") @PathVariable long id) {
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
