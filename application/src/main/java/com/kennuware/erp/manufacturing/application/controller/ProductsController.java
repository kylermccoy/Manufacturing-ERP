package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.controller.util.RequestSender;
import com.kennuware.erp.manufacturing.application.model.Product;
import com.kennuware.erp.manufacturing.application.model.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

@Controller
public class ProductsController {

    @GetMapping("/products")
    public String getProducts(Model model, HttpSession session) {
        ResponseEntity<Product[]> response1 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/products", Product[].class, session);
        Product[] products = response1.getBody();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping(path = "/products/{id}")
    String getProduct(@PathVariable Long id, Model model, HttpSession session) {
        ResponseEntity<Product> response1 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/products/" + id, Product.class, session);
        Product product = response1.getBody();
        model.addAttribute("product", product);
        return "product";
    }

}