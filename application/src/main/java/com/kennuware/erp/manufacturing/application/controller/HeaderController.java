package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.controller.util.RequestSender;
import com.kennuware.erp.manufacturing.application.model.Queue;
import com.kennuware.erp.manufacturing.application.model.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;


@Controller
public class HeaderController {

    /**
     * Redirects to the Log page
     * @return Redirection to the log page
     */
    @GetMapping(path = "/log")
    public String getLog(){
        return "log";
    }


    /**
     * Redirects to the Process page
     * @param model Model
     * @return Redirection to the process page
     */
    @GetMapping(path = "/process")
    public String getQueue(Model model, HttpSession session){
        ResponseEntity<Queue> response1 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/queue", Queue.class, session);
        Queue queue = response1.getBody();
        String queueStatus;
        if (queue.isRunning()){
            queueStatus = "Running";
        }else {
            queueStatus = "Stopped";
        }
        model.addAttribute("queueStatus", queueStatus);
        ResponseEntity<Request[]> response2 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/queue/requests", Request[].class, session);
        Request[] requests = response2.getBody();
        model.addAttribute("requests", requests);
        return "process";
    }


    /**
     * Alerts user of an error
     * @param model Model
     * @return Error
     */
    @GetMapping(path = "/error")
    public String getError(Model model){
        return "error";
    }


}