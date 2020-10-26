package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.model.Item;
import com.kennuware.erp.manufacturing.service.model.repository.ItemRepository;
import java.util.List;

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
@RequestMapping("/items")
public class ItemController {

  private final ItemRepository itemRepository; // Repository of items
  private final ObjectMapper mapper; // Provides functionality for reading and writing JSON

  /**
   * Creates a new instance of ItemController
   * @param itemRepository Repository of items
   * @param mapper Mapper
   */
  ItemController(ItemRepository itemRepository, ObjectMapper mapper) {
    this.itemRepository = itemRepository; // Repository of items
    this.mapper = mapper; // Provides functionality for reading and writing JSON
  }


  /**
   * Lists all items in repository
   * @return list of all items
   */
  @GetMapping
  @Operation(summary = "Lists all items in repository")
  List<Item> listItems() {
    return itemRepository.findAll();
  }


  /**
   * Gathers data of a specific item
   * @param id Unique Item ID
   * @return item
   */
  @GetMapping(path = "/{id}")
  @Operation(summary = "Gathers data of a specific item")
  Item getItem(@Parameter(description = "Unique Item ID") @PathVariable long id) {
    return itemRepository.findById(id).orElseThrow(() -> new GenericJSONException("Item [" + id + "] not found"));
  }


  /**
   * Adds an item to the repository
   * @param item Item to be added
   * @return Saves item to repository
   */
  @PostMapping
  @Operation(summary = "Adds an item to the repository")
  Item addItem(@Parameter(description = "Item to be added") @RequestBody Item item) {
    return itemRepository.save(item);
  }


  /**
   * Deletes an item from the repository
   * @param id Unique Item ID
   * @return JSON success or failure message
   */
  @DeleteMapping(path = "/{id}")
  @Operation(summary = "Deletes an item from the repository")
  ObjectNode deleteItem(@Parameter(description = "Unique Item ID") @PathVariable long id) {
    final ObjectNode response = mapper.createObjectNode();
    final String message;
    final boolean success;
    // does the item exist in the first place
    if (itemRepository.existsById(id)) {
      itemRepository.deleteById(id);
      success = !itemRepository.existsById(id);
      // did it actually get deleted
      if (!success) {
        message = "Item was not deleted!";
        log.error("Item " + id + " was not successfully deleted");
      } else {
        message = "Item was successfully deleted";
      }
    } else {
      success = false;
      message = "Item does not exist";
    }
    response.put("success", success);
    response.put("message", message);
    return response;
  }

}
