package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.model.Order;
import com.kennuware.erp.manufacturing.application.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Controller
public class OrdersController {
//
//    @GetMapping("/orders")
//    public String GetOrders(Model model){
//        RestTemplate rt = new RestTemplate();
//        Order[] orders = rt.getForObject("http://localhost:8080/manufacturing/api/orders", Order[].class);
//        model.addAttribute("orders", orders);
//        return "orders";
//    }

    @GetMapping(path = "/orders/{id}")
    String getOrder(@PathVariable Long id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        Order order = restTemplate.getForObject("http://localhost:8080/manufacturing/api/orders/" + id, Order.class);
        model.addAttribute("order", order);
        return "order";
    }


}
