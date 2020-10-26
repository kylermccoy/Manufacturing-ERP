package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ExceptionController {

  private ObjectMapper mapper;

  ExceptionController(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @ExceptionHandler(GenericJSONException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public JsonNode handleGenericError(GenericJSONException e, WebRequest request) {
    ObjectNode node = mapper.createObjectNode();
    node.put("success", false);
    node.put("message", e.getLocalizedMessage());
    return node;
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public JsonNode handleRouteNotFound(Exception e) {
    ObjectNode node = mapper.createObjectNode();
    node.put("success", false);
    node.put("message", e.getLocalizedMessage());
    return node;
  }

}
