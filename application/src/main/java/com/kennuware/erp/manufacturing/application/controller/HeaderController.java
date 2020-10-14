package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.model.Order;
import com.kennuware.erp.manufacturing.application.model.Product;
import com.kennuware.erp.manufacturing.application.model.Recall;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class HeaderController {

    @GetMapping(path = "/orders")
    public String GetOrders(Model model){
        RestTemplate rt = new RestTemplate();
        Order[] orders = rt.getForObject("http://localhost:8080/manufacturing/api/orders", Order[].class);
        model.addAttribute("orders", orders);
        return "orders";
    }

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

    @GetMapping(path = "/products")
    public String GetProducts(Model model){
        RestTemplate rt = new RestTemplate();
        Product[] products = rt.getForObject("http://localhost:8080/manufacturing/api/products", Product[].class);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping(path = "/timesheet")
    public String GetTimesheet(){
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