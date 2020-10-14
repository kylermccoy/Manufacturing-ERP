package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.model.Order;
import com.kennuware.erp.manufacturing.application.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;


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

    @GetMapping(path = "/timesheet")
    public String GetTimesheet(){
        return "timesheet";
    }

    @GetMapping(path = "/update_timesheet")
    public String UpdateTimesheet(@RequestParam String hours, Model model){
        //ADD TIMESHEET FUNCTIONALITY
        model.addAttribute("hours",hours);
        return "timesheet";
    }

    @GetMapping(path = "/log")
    public String GetLog(){
        return "log";
    }

    @GetMapping(path = "/process")
    public String GetQueue(Model model){
        RestTemplate rt = new RestTemplate();
        Order[] queue = rt.getForObject("http://localhost:8080/manufacturing/api/queue/?queueName=Main Queue", Order[].class);
        model.addAttribute("queue", queue);
        return "process";
    }
}