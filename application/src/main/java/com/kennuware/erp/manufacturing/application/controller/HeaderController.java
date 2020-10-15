package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.model.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;


@Controller
public class HeaderController {

    @GetMapping(path = "/recalls")
    public String GetRecalls(Model model){
        //UNCOMMENT ONCE RECALL IMPLEMENTATION IS GOOD
        /*
        RestTemplate rt = new RestTemplate();
        Recall[] recalls = rt.getForObject("http://localhost:8080/manufacturing/api/recalls", Recall[].class);
        model.addAttribute("recalls", recalls);
        */
        return "recalls";
    }

    @GetMapping(path = "/log")
    public String GetLog(){
        return "log";
    }

    @GetMapping(path = "/process")
    public String GetQueue(Model model){
        RestTemplate rt = new RestTemplate();
        Request[] queue = rt.getForObject("http://localhost:8080/manufacturing/api/queue/requests?queueName=Main Queue", Request[].class);
        model.addAttribute("queue", queue);
        return "process";
    }
}