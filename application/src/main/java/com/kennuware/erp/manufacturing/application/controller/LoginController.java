package com.kennuware.erp.manufacturing.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class LoginController {
    @RequestMapping(path = "/sign_in")
    public RedirectView redirectToProcess(@RequestParam String user, @RequestParam String pass, Model model){
        // INSERT SIGN IN CHECKING HERE!!!
        if (user.isBlank() || pass.isBlank()) {
            model.addAttribute("failure", true);
            return new RedirectView("");
        }
        return new RedirectView("/process");
    }

}