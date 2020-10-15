package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.controller.util.RequestSorter;
import com.kennuware.erp.manufacturing.application.model.Request;
import com.kennuware.erp.manufacturing.application.model.RequestType;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Controller
public class OrdersController {

    @GetMapping(path = "/orders")
    public String getOrders(Model model){
        RestTemplate rt = new RestTemplate();
        Request[] ordersArr = rt.getForObject("http://localhost:8080/manufacturing/api/requests", Request[].class);
        List<Request> orders = RequestSorter.getRequestsOfType(ordersArr, RequestType.ORDER);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping(path = "/orders/{id}")
    public String getOrder(@PathVariable Long id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        Request order = restTemplate.getForObject("http://localhost:8080/manufacturing/api/requests/" + id, Request.class);
        String orderStatus;
        if (order.isCompleted()){
            orderStatus = "Complete";
        }else{
            orderStatus = "Not Complete";
        }
        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("order", order);
        return "order";
    }
}