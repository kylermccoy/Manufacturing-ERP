package com.kennuware.erp.manufacturing.application.controller;

import com.kennuware.erp.manufacturing.application.model.Employee;
import com.kennuware.erp.manufacturing.application.model.Queue;
import com.kennuware.erp.manufacturing.application.model.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class EmployeeController {

    public String user;

    @RequestMapping(path = "/sign_in")
    public Object redirectToProcess(@RequestParam String user, @RequestParam String pass, Model model){
        // INSERT SIGN IN CHECKING HERE!!!
        if (user.isBlank() || pass.isBlank()) {
            model.addAttribute("failure", true);
            return "login";
        }
        this.user = user;
        RestTemplate rt = new RestTemplate();
        Request[] queue = rt.getForObject("http://localhost:8080/manufacturing/api/queue/requests?queueName=Main Queue", Request[].class);
        model.addAttribute("queue", queue);
        return new RedirectView("/process");
    }

    @GetMapping(path = "/timesheet")
    public String getTimesheet(){
        return "timesheet";
    }

    @RequestMapping(path = "/update_timesheet")
    public String updateTimesheet(@RequestParam String hours, Model model){
        //ADD TIMESHEET FUNCTIONALITY
        if (hours.isBlank()){
            model.addAttribute("success", false);
            model.addAttribute("failure", true);
            return "timesheet";
        }
        try {
            int temp = Integer.parseInt(hours);
            RestTemplate rt = new RestTemplate();
            Employee employee = new Employee(user,temp);
            Employee result = rt.postForObject("http://localhost:8080/manufacturing/api/employees/updateHours?user=" + this.user + "&hoursWorked=" + hours, employee, Employee.class);
            model.addAttribute("success", true);
            model.addAttribute("failure", false);
            return "timesheet";
        }catch (NumberFormatException ex){
            model.addAttribute("success", false);
            model.addAttribute("failure", true);
            return "timesheet";
        }
    }
}