package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.service.auth.AuthManager;
import com.kennuware.erp.manufacturing.service.model.Employee;
import com.kennuware.erp.manufacturing.service.model.repository.EmployeeRepository;
import javax.servlet.http.HttpSession;
import org.springframework.lang.UsesSunMisc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

  EmployeeRepository employeeRepository;
  ObjectMapper mapper;
  PasswordEncoder encoder;

  EmployeeController(EmployeeRepository repository, PasswordEncoder encoder, ObjectMapper mapper) {
    this.employeeRepository = repository;
    this.encoder = encoder;
    this.mapper = mapper;
  }

  @PostMapping
  Employee createEmployee(@RequestBody JsonNode json, HttpSession session) {
    String user = json.get("user").textValue();
    String pass = json.get("pass").textValue();
    if (!user.isBlank() || !pass.isBlank()) {
      // todo: some error
    }
    if (employeeRepository.existsByUsername(user)) {
      // TODO: idk something else here
      return null;
    }
    Employee employee = new Employee();
    employee.setUsername(user);
    employee.setPassword(encoder.encode(pass));
    employee.setHoursWorked(0L);
    session.setAttribute(AuthManager.AUTH, true);
    session.setAttribute(AuthManager.USER, user);
    return employeeRepository.save(employee);
  }

  @GetMapping("/authenticate")
  boolean authenticateEmployee(@RequestBody JsonNode json, HttpSession session) {
    String user = json.get("user").textValue();
    String pass = json.get("pass").textValue();
    Employee employee = employeeRepository.findByUsername(user);
    boolean success = encoder.matches(pass, employee.getPassword());
    session.setAttribute(AuthManager.AUTH, success);
    session.setAttribute(AuthManager.USER, user);
    return success;
  }

  @PostMapping("/updateHours")
  Object updateHours(@RequestParam String user, @RequestParam long hoursWorked, HttpSession session) {
    if (session != null) {
      if (user.equals((session.getAttribute(AuthManager.USER)))) {
        Employee employee = employeeRepository.findByUsername(user);
        employee.setHoursWorked(hoursWorked);
        employeeRepository.save(employee);
      }
    }
    ObjectNode node = mapper.createObjectNode();
    node.put("success", false);
    node.put("message", "You are not authenticated to update this user's hours");
    return node;
  }

  @GetMapping("/getHours")
  Object updateHours(@RequestParam String user, HttpSession session) {
    ObjectNode node = mapper.createObjectNode();
    if (session != null) {
      if (user.equals((session.getAttribute(AuthManager.USER)))) {
        Employee employee = employeeRepository.findByUsername(user);
        node.put("name", user);
        node.put("hours", employee.getHoursWorked());
        return node;
      }
    }
    node.put("success", false);
    node.put("message", "You are not authenticated to get this user's hours");
    return node;
  }
}
