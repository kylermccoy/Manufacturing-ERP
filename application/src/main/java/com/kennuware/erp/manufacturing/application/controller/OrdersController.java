package com.kennuware.erp.manufacturing.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class OrdersController {
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String GetOrder(){
        return "order";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String DeleteOrder(){
        // INSERT ORDER DELETION HERE
        return "orders";
    }
}