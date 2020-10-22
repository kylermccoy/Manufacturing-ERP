package com.kennuware.erp.manufacturing.application.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.application.controller.util.RequestSender;
import com.kennuware.erp.manufacturing.application.model.Queue;
import com.kennuware.erp.manufacturing.application.model.Request;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;


@Controller
public class EmployeeController {

    public String user;
    ObjectMapper mapper;

    EmployeeController(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    @RequestMapping(path = "/sign_in")
    public Object redirectToProcess(@RequestParam String user, @RequestParam String pass, Model model, HttpSession session){
        // INSERT SIGN IN CHECKING HERE!!!
        if (user.isBlank() || pass.isBlank()) {
            model.addAttribute("failure", true);
            return "login";
        }
        ObjectNode json = mapper.createObjectNode();
        json.put("user", user);
        json.put("pass", pass);
        ResponseEntity<Boolean> response1 = RequestSender.postForObject("http://localhost:8080/manufacturing/api/employees/authenticate", json, boolean.class, session );
        HttpHeaders headers = response1.getHeaders();
        List<String> cookieList = headers.getOrDefault("Set-Cookie", Collections.emptyList());
        if (!cookieList.isEmpty()) {
            String cookie = cookieList.get(0).split("=")[1];
            session.setAttribute("session", cookie);
        }
        boolean success = response1.getBody();
        if (!success) {
            model.addAttribute("failure", true);
            return "login";
        }

        this.user = user;

        ResponseEntity<Queue> response2 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/queue", Queue.class, session);
        Queue queue = response2.getBody();
        String queueStatus;
        if (queue.isRunning()){
            queueStatus = "Running";
        }else {
            queueStatus = "Stopped";
        }
        model.addAttribute("queueStatus", queueStatus);
        ResponseEntity<Request[]> response3 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/queue/requests", Request[].class, session);
        Request[] requests = response3.getBody();
        model.addAttribute("requests", requests);
        return new RedirectView("/process");
    }

    @GetMapping(path = "/timesheet")
    public String getTimesheet(Model model, HttpSession session){
        ResponseEntity<JsonNode> response = RequestSender.getForObject("http://localhost:8080/manufacturing/api/employees/getHours?user=" + this.user, JsonNode.class, session);
        JsonNode res = response.getBody();
        long hours = res.get("hours").asLong();
        model.addAttribute("hours", hours);
        return "timesheet";
    }

    @RequestMapping(path = "/update_timesheet")
    public String updateTimesheet(@RequestParam String hours, Model model, HttpSession session){
        if (hours.isBlank()){
            model.addAttribute("success", false);
            model.addAttribute("failure", true);
            return "timesheet";
        }
        try {
            int temp = Integer.parseInt(hours);
            ResponseEntity<JsonNode> response = RequestSender.getForObject("http://localhost:8080/manufacturing/api/employees/updateHours?user=" + this.user + "&hoursWorked=" + temp, JsonNode.class, session);
            JsonNode res = response.getBody();
            model.addAttribute("success", res.get("success"));
            model.addAttribute("failure", false);
            model.addAttribute("hours", temp);
            return "timesheet";
        }catch (NumberFormatException ex){
            model.addAttribute("success", false);
            model.addAttribute("failure", true);
            return "timesheet";
        }
    }
}