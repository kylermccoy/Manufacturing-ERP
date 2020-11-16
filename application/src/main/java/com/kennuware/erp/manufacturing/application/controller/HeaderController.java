package com.kennuware.erp.manufacturing.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


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
     * Alerts user of an error
     * @param model Model
     * @return Error
     */
    @GetMapping(path = "/error")
    public String getError(Model model){
        return "error";
    }


}