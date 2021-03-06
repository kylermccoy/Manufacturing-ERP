package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class ProductsController {

    @GetMapping("/products")
    public String getProducts(Model model) {
        RestTemplate rt = new RestTemplate();
        Product[] products = rt.getForObject("http://localhost:8080/manufacturing/api/products", Product[].class);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping(path = "/products/{id}")
    String getProduct(@PathVariable Long id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        Product product = restTemplate.getForObject("http://localhost:8080/manufacturing/api/products/" + id, Product.class);
        model.addAttribute("product", product);
        return "product";
    }

}