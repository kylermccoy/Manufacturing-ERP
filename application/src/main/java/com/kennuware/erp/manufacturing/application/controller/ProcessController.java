package com.kennuware.erp.manufacturing.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class ProcessController {
    @RequestMapping(value = "/process", method = RequestMethod.GET)
    public String GetProcess(){
        return "process";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String GetOrders(){
        return "orders";
    }

    @RequestMapping(value = "/recalls", method = RequestMethod.GET)
    public String GetRecalls(){
        return "recalls";
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String GetProducts(){
        return "products";
    }

    @RequestMapping(value = "/timesheet", method = RequestMethod.GET)
    public String GetTimesheet(){
        return "timesheet";
    }

    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public String GetLog(){
        return "log";
    }
}