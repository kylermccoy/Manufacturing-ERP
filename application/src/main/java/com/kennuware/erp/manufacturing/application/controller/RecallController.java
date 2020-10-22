package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.controller.util.RequestSender;
import com.kennuware.erp.manufacturing.application.controller.util.RequestSorter;
import com.kennuware.erp.manufacturing.application.model.Product;
import com.kennuware.erp.manufacturing.application.model.Request;
import com.kennuware.erp.manufacturing.application.model.RequestType;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

@Controller
public class RecallController {

    @GetMapping(path = "/recalls")
    public String getRecalls(Model model, HttpSession session){
        ResponseEntity<Request[]> response1 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/requests", Request[].class, session);
        Request[] recallsArr = response1.getBody();
        List<Request> recalls = RequestSorter.getRequestsOfType(recallsArr, RequestType.RECALL);
        model.addAttribute("recalls", recalls);
        return "recalls";
    }

    @GetMapping(path = "/recalls/{id}")
    String getProduct(@PathVariable Long id, Model model, HttpSession session) {
        ResponseEntity<Request> response1 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/requests/" + id, Request.class, session);
        Request recall = response1.getBody();
        String recallStatus;
        if (recall.isCompleted()){
            recallStatus = "Complete";
        }else{
            recallStatus = "Not Complete";
        }
        model.addAttribute("recallStatus", recallStatus);
        model.addAttribute("recall", recall);
        return "recall";
    }



}