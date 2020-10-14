package com.kennuware.erp.manufacturing.service.controller;

import com.kennuware.erp.manufacturing.service.model.Employee;
import com.kennuware.erp.manufacturing.service.model.repository.EmployeeRepository;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

  EmployeeRepository employeeRepository;

  EmployeeController(EmployeeRepository repository) {
    this.employeeRepository = repository;
  }

  @PostMapping
  Employee createEmployee(@RequestParam String user, @RequestParam String pass) {
    if (!user.isBlank() || !pass.isBlank()) {
      // todo: some error
    }
    try {
      Employee employee = new Employee();
      byte[] passHash = generateHash(generateSalt(employee), pass);
      employee.setUsername(user);
      employee.setPassword(Arrays.toString(passHash));
      employee.setHoursWorked(0L);
      return employeeRepository.save(employee);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
    }
    throw new EntityNotFoundException();
  }

  @GetMapping("/authenticate")
  boolean authenticateEmployee(@RequestParam String user, @RequestParam String pass) {
    return checkHash(user, pass);
  }

  private boolean checkHash(String user, String plaintextPass) {
    Employee e = employeeRepository.findByUsername(user);
    byte[] salt = e.getHashSalt();
    byte[] hash;
    try {
      hash = generateHash(salt, plaintextPass);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException noSuchAlgorithmException) {
      return false;
    }
    return Arrays.toString(hash).equals(e.getPassword());
  }

  private byte[] generateHash(byte[] salt, String plaintextPass)
      throws NoSuchAlgorithmException, InvalidKeySpecException {

    KeySpec spec = new PBEKeySpec(plaintextPass.toCharArray(), salt, 65536, 128);
    SecretKeyFactory factory = null;
    factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    return factory.generateSecret(spec).getEncoded();

  }

  private byte[] generateSalt(Employee e) {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    e.setHashSalt(salt);
    return salt;
  }

}
