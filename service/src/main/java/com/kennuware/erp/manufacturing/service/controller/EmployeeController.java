package com.kennuware.erp.manufacturing.service.controller;

import com.fasterxml.jackson.databind.JsonNode;
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
  PasswordEncoder encoder;

  EmployeeController(EmployeeRepository repository, PasswordEncoder encoder) {
    this.employeeRepository = repository;
    this.encoder = encoder;
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
    return employeeRepository.save(employee);
  }

  @GetMapping("/authenticate")
  boolean authenticateEmployee(@RequestBody JsonNode json, HttpSession session) {
    String user = json.get("user").textValue();
    String pass = json.get("pass").textValue();
    Employee employee = employeeRepository.findByUsername(user);
    boolean success = encoder.matches(pass, employee.getPassword());
    session.setAttribute(AuthManager.AUTH, success);
    return success;
  }



}
