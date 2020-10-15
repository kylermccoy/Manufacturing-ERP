package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.model.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Controller
public class OrdersController {

    @GetMapping(path = "/orders")
    public String GetOrders(Model model){
        RestTemplate rt = new RestTemplate();
        Request[] orders = rt.getForObject("http://localhost:8080/manufacturing/api/orders", Request[].class);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping(path = "/orders/{id}")
    String getProduct(@PathVariable Long id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        Request order = restTemplate.getForObject("http://localhost:8080/manufacturing/api/orders/" + id, Request.class);
        model.addAttribute("order", order);
        return "order";
    }



}