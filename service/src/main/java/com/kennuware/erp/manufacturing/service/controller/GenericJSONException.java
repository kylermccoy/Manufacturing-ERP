package com.kennuware.erp.manufacturing.service.controller;

public class GenericJSONException extends RuntimeException{


  public GenericJSONException() {
    super("There was an error processing your request, please try again or contact the administrator.");
  }

  public GenericJSONException(String message) {
    super(message);
  }

}
