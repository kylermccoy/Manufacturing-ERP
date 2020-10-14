package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class HeaderController {
    @GetMapping(path = "/process")
    public String GetProcess(){
        return "process";
    }

    @GetMapping(path = "/orders")
    public String GetOrders(){
        return "orders";
    }

    @GetMapping(path = "/recalls")
    public String GetRecalls(){
        return "recalls";
    }

    @GetMapping(path = "/products")
    public String GetProducts(Model model){
        RestTemplate rt = new RestTemplate();
        Product[] products = rt.getForObject("http://localhost:8080/manufacturing/api/products/list", Product[].class);
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
}