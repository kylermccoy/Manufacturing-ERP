package com.kennuware.erp.manufacturing.service.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

public class RequestSender {

  public static <T> ResponseEntity<T> getForObject(String url, Class<T> responseType, HttpSession session) {
    RestTemplate rt = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Cookie", "SESSION=" + session.getAttribute("session"));
    HttpEntity entity = new HttpEntity(headers);
    return rt.exchange(url, HttpMethod.GET, entity, responseType);
  }

  public static <T> ResponseEntity<T> postForObject(String url, Object data, Class<T> responseType, HttpSession session ) throws NullPointerException{
    RestTemplate rt = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Cookie", "SESSION=" + session.getAttribute("session"));
    HttpEntity entity = new HttpEntity(data, headers);
    return rt.exchange(url, HttpMethod.POST, entity, responseType);
  }
}
