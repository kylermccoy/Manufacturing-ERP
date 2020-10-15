package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.model.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;


@Controller
public class HeaderController {

    @GetMapping(path = "/log")
    public String getLog(){
        return "log";
    }

    @GetMapping(path = "/process")
    public String getQueue(Model model){
        RestTemplate rt = new RestTemplate();
        Request[] queue = rt.getForObject("http://localhost:8080/manufacturing/api/queue/requests?queueName=Main Queue", Request[].class);
        model.addAttribute("queue", queue);
        return "process";
    }
}