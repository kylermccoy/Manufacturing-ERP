package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.model.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class OrdersController {

    @GetMapping(path = "/orders")
    public String GetOrders(Model model){
        RestTemplate rt = new RestTemplate();
        Order[] orders = rt.getForObject("http://localhost:8080/manufacturing/api/orders", Order[].class);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping(path = "/orders/{id}")
    String getProduct(@PathVariable Long id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        Order order = restTemplate.getForObject("http://localhost:8080/manufacturing/api/orders/" + id, Order.class);
        model.addAttribute("order", order);
        return "order";
    }



}