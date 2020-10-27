package com.kennuware.erp.manufacturing.application.controller.util;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RequestSender {

  public static <T> ResponseEntity<T> getForObject(String url, Class<T> responseType, HttpSession session) {
    RestTemplate rt = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Cookie", "SESSION=" + session.getAttribute("session"));
    HttpEntity entity = new HttpEntity(headers);
    return rt.exchange(url, HttpMethod.GET, entity, responseType);
  }

  public static <T> ResponseEntity<T> postForObject(String url, Object data, Class<T> responseType, HttpSession session ) {
    RestTemplate rt = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Cookie", "SESSION=" + session.getAttribute("session"));
    HttpEntity entity = new HttpEntity(data, headers);
    return rt.exchange(url, HttpMethod.POST, entity, responseType);
  }
}