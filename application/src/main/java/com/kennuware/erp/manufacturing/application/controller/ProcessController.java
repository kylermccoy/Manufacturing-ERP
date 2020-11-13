package com.kennuware.erp.manufacturing.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.application.util.RequestSender;
import com.kennuware.erp.manufacturing.application.model.Queue;
import com.kennuware.erp.manufacturing.application.model.Request;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(path = "/process")
public class ProcessController {

  private ObjectMapper mapper;

  ProcessController(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  /**
   * Loads the Process page
   * @param model Model
   * @return the process page
   */
  @GetMapping
  public String getQueue(Model model, HttpSession session){
    ResponseEntity<Queue> response1 = RequestSender
        .getForObject("http://localhost:8080/manufacturing/api/queue", Queue.class, session);
    Queue queue = response1.getBody();
    String queueStatus;
    if (queue.isRunning()){
      queueStatus = "Running";
    }else {
      queueStatus = "Stopped";
    }
    model.addAttribute("queueStatus", queueStatus);

    //model.addAttribute("currentItem", queueManager.getCurrent());

    ResponseEntity<Request[]> response2 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/queue/requests", Request[].class, session);
    Request[] requests = response2.getBody();
    model.addAttribute("requests", requests);

    return "process";
  }

  @GetMapping(value = "/getRemainingTime", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ObjectNode getRemainingTime(HttpSession session) {
    return RequestSender
        .getForObject("http://localhost:8080/manufacturing/api/queue/getRemainingTime",
            ObjectNode.class, session).getBody();
  }

  @GetMapping("/start")
  @ResponseStatus(value = HttpStatus.OK)
  public void startProcess(HttpSession session) {
    ResponseEntity<ObjectNode> queueResponse = RequestSender.getForObject("http://localhost:8080/manufacturing/api/queue/start", ObjectNode.class, session);
    ObjectNode resp = queueResponse.getBody();
    boolean success = resp != null && resp.get("success").asBoolean();
    //todo api call
  }

  @GetMapping("/stop")
  @ResponseStatus(value = HttpStatus.OK)
  public void stopProcess(HttpSession session) {
    ResponseEntity<ObjectNode> queueResponse = RequestSender.getForObject("http://localhost:8080/manufacturing/api/queue/stop", ObjectNode.class, session);
    ObjectNode resp = queueResponse.getBody();
    boolean success = resp != null && resp.get("success").asBoolean();
    //todo api call
  }

  @PostMapping("/skip")
  public void skipTime(@RequestParam long minutes, HttpSession session) {

  }

}
