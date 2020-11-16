package com.kennuware.erp.manufacturing.service.util;

import com.kennuware.erp.manufacturing.service.model.Request;
import com.kennuware.erp.manufacturing.service.model.RequestType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestSorter {

  public static List<Request> getRequestsOfType(Request[] requests, RequestType type) {
    return Arrays.stream(requests).filter(r -> r.getType() == type).collect(Collectors.toList());
  }

}
