package com.kennuware.erp.manufacturing.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class LoginController {
    @RequestMapping(path = "/sign_in")
    public RedirectView redirectToProcoess(@RequestParam String user, @RequestParam String pass){
        // INSERT SIGN IN CHECKING HERE!!!
        if (user.isEmpty() || pass.isEmpty()) {
            return new RedirectView("");
        }else if (user.isBlank() || pass.isBlank()) {
            return new RedirectView("");
        }
        return new RedirectView("/process");
    }

}