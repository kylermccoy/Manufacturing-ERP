package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.model.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class LoginController {
    @RequestMapping(path = "/sign_in")
    public Object redirectToProcess(@RequestParam String user, @RequestParam String pass, Model model){
        // INSERT SIGN IN CHECKING HERE!!!
        if (user.isBlank() || pass.isBlank()) {
            model.addAttribute("failure", true);
            return "login";
        }
        RestTemplate rt = new RestTemplate();
        Order[] queue = rt.getForObject("http://localhost:8080/manufacturing/api/queue/?queueName=Main Queue", Order[].class);
        model.addAttribute("queue", queue);
        return new RedirectView("/process");
    }

}