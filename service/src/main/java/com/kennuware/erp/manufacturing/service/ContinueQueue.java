package com.kennuware.erp.manufacturing.service;

import com.kennuware.erp.manufacturing.service.controller.QueueController;
import com.kennuware.erp.manufacturing.service.model.CurrentQueueItem;
import com.kennuware.erp.manufacturing.service.model.Employee;
import com.kennuware.erp.manufacturing.service.model.Item;
import com.kennuware.erp.manufacturing.service.model.Product;
import com.kennuware.erp.manufacturing.service.model.Queue;
import com.kennuware.erp.manufacturing.service.model.Recipe;
import com.kennuware.erp.manufacturing.service.model.RecipeComponent;
import com.kennuware.erp.manufacturing.service.model.repository.CurrentQueueItemRepository;
import com.kennuware.erp.manufacturing.service.model.repository.EmployeeRepository;
import com.kennuware.erp.manufacturing.service.model.repository.ItemRepository;
import com.kennuware.erp.manufacturing.service.model.repository.ProductRepository;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import com.kennuware.erp.manufacturing.service.model.repository.RecipeRepository;
import com.kennuware.erp.manufacturing.service.model.repository.RequestRepository;
import com.kennuware.erp.manufacturing.service.util.QueueManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class ContinueQueue {

  @Bean
  CommandLineRunner initQueue(QueueManager queueManager, QueueRepository queueRepository,
      CurrentQueueItemRepository currentQueueItemRepository) {

    return args -> {
      Optional<Queue> queue = queueRepository.findByName(QueueController.QUEUE_NAME);
      if (queue.isEmpty()) {
        return;
      }
      Queue realQueue = queue.get();
      if (realQueue.isRunning()) {
        Optional<CurrentQueueItem> currentQueueItemOptional = currentQueueItemRepository
            .findDistinctFirstByOrderByIdDesc();
        if (currentQueueItemOptional.isEmpty()) {
          return;
        }
        CurrentQueueItem currentQueueItem = currentQueueItemOptional.get();
        queueManager.resumeFromBoot(currentQueueItem.getId());
      }
    };
  }

}
