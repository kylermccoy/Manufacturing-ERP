package com.kennuware.erp.manufacturing.service;

import com.kennuware.erp.manufacturing.service.controller.QueueController;
import com.kennuware.erp.manufacturing.service.model.Employee;
import com.kennuware.erp.manufacturing.service.model.Queue;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(QueueRepository repository) {
    return args -> {
      Queue q = new Queue();
      q.setName(QueueController.QUEUE_NAME);
      q.setRunning(false);
      q.setRequestsInQueue(Collections.emptyList());
      if (!repository.existsByName(QueueController.QUEUE_NAME)) {
        log.info("Preloading " + repository.save(q));
      }
    };
  }

}
