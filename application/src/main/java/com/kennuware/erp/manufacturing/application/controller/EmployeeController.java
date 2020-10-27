package com.kennuware.erp.manufacturing.application.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.application.model.Employee;
import com.kennuware.erp.manufacturing.application.model.Queue;
import com.kennuware.erp.manufacturing.application.model.Request;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;


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
        RestTemplate authenticateTemplate = new RestTemplate();
        ObjectNode json = mapper.createObjectNode();
        json.put("user", user);
        json.put("pass", pass);

        /*
         * Request sign-in authentication from HR here
         */

        ResponseEntity<Boolean> response = authenticateTemplate.postForEntity("http://localhost:8080/manufacturing/api/employees/authenticate", json, boolean.class);

        /*
         * ResponseEntity<Boolean> response = authenticateTemplate.postForEntity("/humanresources/api/employees/authenticate", json, boolean.class);
         */

        HttpHeaders headers = response.getHeaders();
        List<String> cookieList = headers.getOrDefault("Set-Cookie", Collections.emptyList());
        if (!cookieList.isEmpty()) {
            String cookie = cookieList.get(0).split("=")[1];
            session.setAttribute("session", cookie);
        }
        boolean success = response.getBody();
        if (!success) {
            model.addAttribute("failure", true);
            return "login";
        }

        this.user = user;

        RestTemplate rt = new RestTemplate();
        Queue queue = rt.getForObject("http://localhost:8080/manufacturing/api/queue", Queue.class);
        String queueStatus;
        if (queue.isRunning()){
            queueStatus = "Running";
        }else {
            queueStatus = "Stopped";
        }
        model.addAttribute("queueStatus", queueStatus);
        Request[] requests = rt.getForObject("http://localhost:8080/manufacturing/api/queue/requests", Request[].class);
        model.addAttribute("requests", requests);
        return new RedirectView("/process");
    }

    @GetMapping(path = "/timesheet")
    public String getTimesheet(Model model, HttpSession session){
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "SESSION=" + session.getAttribute("session"));
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<JsonNode> response = rt.exchange(
            "http://localhost:8080/manufacturing/api/employees/getHours?user=" + this.user, HttpMethod.GET, entity, JsonNode.class);
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
            RestTemplate rt = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", "SESSION=" + session.getAttribute("session"));
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<JsonNode> response = rt.exchange(
                "http://localhost:8080/manufacturing/api/employees/updateHours?user=" + this.user + "&hoursWorked=" + temp, HttpMethod.POST, entity, JsonNode.class);
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

    @PostMapping(path = "/send_timesheet")
    public String sendTimesheet() {
        return "timesheet";
    }



}