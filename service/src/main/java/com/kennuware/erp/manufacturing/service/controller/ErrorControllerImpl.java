package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorControllerImpl implements ErrorController {

  private ObjectMapper mapper;

  ErrorControllerImpl(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @RequestMapping("/error")
  public JsonNode handleError() {
    ObjectNode node = mapper.createObjectNode();
    node.put("success", false);
    node.put("message", "There was an error processing your request, please try again or contact the administrator.");
    return node;
  }

  @Override
  public String getErrorPath() {
    return null;
  }
}
