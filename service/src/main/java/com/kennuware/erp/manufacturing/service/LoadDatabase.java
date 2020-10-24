package com.kennuware.erp.manufacturing.service;

import com.kennuware.erp.manufacturing.service.controller.QueueController;
import com.kennuware.erp.manufacturing.service.model.Employee;
import com.kennuware.erp.manufacturing.service.model.Queue;
import com.kennuware.erp.manufacturing.service.model.repository.EmployeeRepository;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(QueueRepository repository, EmployeeRepository employeeRepository, PasswordEncoder encoder) {
    return args -> {
      Employee e = new Employee();
      e.setUsername("admin");
      e.setPassword(encoder.encode("admin"));
      if (!employeeRepository.existsByUsername("admin")) {
        log.info("Preloading " + employeeRepository.save(e));
      }
      Queue q = new Queue();
      q.setName(QueueController.QUEUE_NAME);
      q.setRunning(false);
      q.setRequestsInQueue(Collections.emptyList());
      if (!repository.existsByName(QueueController.QUEUE_NAME)) {
        log.info("Preloading " + repository.save(q));
      }

      System.out.println("some change here");
    };
  }

}
