package com.kennuware.erp.manufacturing.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class LoginController {
    @RequestMapping(value = "/sign_in", method = RequestMethod.GET)
    public String GetLogin(){
        // INSERT SIGN IN CHECKING HERE!!!
        return "process";
    }
}