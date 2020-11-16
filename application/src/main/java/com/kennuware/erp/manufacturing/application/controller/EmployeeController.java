package com.kennuware.erp.manufacturing.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.application.util.RequestSender;
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

    public String user; // Employee user
    ObjectMapper mapper;    // Provides functionality for reading & writing JSON


    /**
     * Creates a new EmployeeController
     * @param mapper mapper
     */
    EmployeeController(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    /**
     * User login process with database authentication
     * @param user Username
     * @param pass Password
     * @param model Model
     * @param session Current Session
     * @return Redirect page
     */
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
        ResponseEntity<Boolean> response1 = RequestSender.postForObject("http://ec2-184-73-13-89.compute-1.amazonaws.com:8080/api/v1/hr/login?username="
                + user + "&password="
                + pass + "&departmentType=MANUFACTURING", null, boolean.class, session);
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

    @GetMapping(path = "/logout")
    public String redirectToLogin(HttpSession session){
        try {
            RequestSender.postForObject("http://ec2-184-73-13-89.compute-1.amazonaws.com:8080/api/v1/hr/logout?username="
                    + user, null, boolean.class, session);
            this.user = "";
        }
        catch (NullPointerException e){
            return "error";
        }
        return "login";
    }

    /**
     * Gathers data from the user's Timesheet page
     * @param model Model
     * @return Where to redirect the user
     */
    @GetMapping(path = "/timesheet")
    public String getTimesheet(Model model){
        model.addAttribute("success", false);
        model.addAttribute("failure", false);
        return "timesheet";
    }


    /**
     * Updates the employee's timesheet based on the data inputted
     * @param hours Hours worked
     * @param model Model
     * @param session Current session
     * @return Where to redirect the user
     */
    @RequestMapping(path = "/update_timesheet")
    public String updateTimesheet(@RequestParam String hours, Model model, HttpSession session){
        try {
            int time = Integer.parseInt(hours);
            ResponseEntity<Boolean> response = RequestSender.postForObject("http://ec2-184-73-13-89.compute-1.amazonaws.com:8080/api/v1/hr/timeSheet?username="
                    + user + "&hours="
                    + time, null, boolean.class, session);
            model.addAttribute("success", response.getBody());
            model.addAttribute("failure", false);
            return "timesheet";
        }catch (NumberFormatException ex){
            model.addAttribute("success", false);
            model.addAttribute("failure", true);
            return "timesheet";
        }
        catch (NullPointerException e){
            model.addAttribute("success", false);
            model.addAttribute("failure", true);
            return "timesheet";
        }
    }
}