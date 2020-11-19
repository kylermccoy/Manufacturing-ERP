package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.util.RequestSender;
import com.kennuware.erp.manufacturing.application.util.RequestSorter;
import com.kennuware.erp.manufacturing.application.model.Request;
import com.kennuware.erp.manufacturing.application.model.RequestType;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

@Controller
public class OrdersController {

    /**
     * Requests list of all orders from backend
     * @param model Model
     * @return Redirection to orders page
     */
    @GetMapping(path = "/orders")
    public String getOrders(Model model, HttpSession session){
        ResponseEntity<Request[]> response1 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/requests", Request[].class, session);
        Request[] ordersArr = response1.getBody();
        List<Request> orders = RequestSorter.getRequestsOfType(ordersArr, RequestType.ORDER);
        model.addAttribute("orders", orders);
        return "orders";
    }


    /**
     * Requests specific order data from backend
     * @param id Unique Order ID number
     * @param model Model
     * @return Redirection to specific order page
     */
    @GetMapping(path = "/orders/{id}")
    public String getOrder(@PathVariable Long id, Model model, HttpSession session) {
        ResponseEntity<Request> response1 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/requests/" + id, Request.class, session);
        Request order = response1.getBody();
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

    @DeleteMapping("/orders/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    void deleteProduct(@PathVariable long id) {
        RestTemplate rt = new RestTemplate();
        rt.delete("http://localhost:8080/manufacturing/api/requests/" + id);
    }
}