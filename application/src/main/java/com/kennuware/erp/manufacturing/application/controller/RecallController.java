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
public class RecallController {

    /**
     * Requests a list of recall orders from the backend
     * @param model Model
     * @return Redirection to the recalls page
     */
    @GetMapping(path = "/recalls")
    public String getRecalls(Model model){
        RestTemplate rt = new RestTemplate();
        Request[] recallsArr = rt.getForObject("http://localhost:8080/manufacturing/api/requests", Request[].class);
        List<Request> recalls = RequestSorter.getRequestsOfType(recallsArr, RequestType.RECALL);
        model.addAttribute("recalls", recalls);
        return "recalls";
    }


    /**
     * Requests specific recall order data from backend
     * @param id Unique Recall Order ID
     * @param model Model
     * @return Redirection to specific recall page
     */
    @GetMapping(path = "/recalls/{id}")
    String getProduct(@PathVariable Long id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        Request recall = restTemplate.getForObject("http://localhost:8080/manufacturing/api/requests/" + id, Request.class);
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