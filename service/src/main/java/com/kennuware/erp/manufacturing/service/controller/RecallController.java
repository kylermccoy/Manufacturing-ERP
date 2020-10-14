package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.model.Recall;
import com.kennuware.erp.manufacturing.service.model.repository.RecallRepository;
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
@RequestMapping(path = "/recalls")
public class RecallController {

  private final RecallRepository recallRepository;
  private final ObjectMapper mapper;

  RecallController(RecallRepository recallRepository, ObjectMapper mapper) {
    this.recallRepository = recallRepository;
    this.mapper = mapper;
  }

  @GetMapping
  List<Recall> listRecalls() {
    return recallRepository.findAll();
  }

  @GetMapping(path = "/{id}")
  Recall getRecall(@PathVariable long id) {
    return recallRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  @PostMapping
  Recall addRecall(@RequestBody Recall recall) {
    return recallRepository.save(recall);
  }

  @DeleteMapping(path = "/{id}")
  ObjectNode deleteRecall(@PathVariable long id) {
    final ObjectNode response = mapper.createObjectNode();
    final String message;
    final boolean success;
    // does the recall exist in the first place
    if (recallRepository.existsById(id)) {
      recallRepository.deleteById(id);
      success = !recallRepository.existsById(id);
      // did it actually get deleted
      if (!success) {
        message = "Recall was not deleted!";
        log.error("Recall " + id + " was not successfully deleted");
      } else {
        message = "Recall was successfully deleted";
      }
    } else {
      success = false;
      message = "Recall does not exist";
    }
    response.put("success", success);
    response.put("message", message);
    return response;
  }

}

