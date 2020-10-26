package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.model.Queue;
import com.kennuware.erp.manufacturing.application.model.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;


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
    public String getQueue(Model model){
        RestTemplate rt = new RestTemplate();
        Queue queue = rt.getForObject("http://localhost:8080/manufacturing/api/queue", Queue.class);
        String queueStatus;
        if (queue.isRunning()){
            queueStatus = "Running";
        }else {
            queueStatus = "Stopped";
        }
        model.addAttribute("queueStatus", queueStatus);
        Request[] requests = rt.getForObject("http://localhost:8080/manufacturing/api/queue/requests", Request[].class);
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