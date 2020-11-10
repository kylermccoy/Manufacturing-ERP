package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.util.RequestSender;
import com.kennuware.erp.manufacturing.application.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class ProductsController {

    /**
     * Requests a list of all products from backend
     * @param model Model
     * @return Redirection to products page
     */
    @GetMapping("/products")
    public String getProducts(Model model, HttpSession session) {
        ResponseEntity<Product[]> response1 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/products", Product[].class, session);
        Product[] products = response1.getBody();
        model.addAttribute("products", products);
        return "products";
    }


    /**
     * Requests specific product data from backend
     * @param id Unique Product ID
     * @param model Model
     * @return Redirection to specific product page
     */
    @GetMapping(path = "/products/{id}")
    String getProduct(@PathVariable Long id, Model model, HttpSession session) {
        ResponseEntity<Product> response1 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/products/" + id, Product.class, session);
        Product product = response1.getBody();
        model.addAttribute("product", product);
        return "product";
    }

    /*
     * Deliver products to inventory
     */
    @PostMapping(path = "/products/deliver")
    String deliverProducts(Product product, int quantity) {
        return null;
    }

    /*
     * Request products from inventory
     */
    @PostMapping(path = "products/request")
    String requestProducts(Long productID, int quantity) {
        return null;
    }

}