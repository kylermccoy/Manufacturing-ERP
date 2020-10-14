package com.kennuware.erp.manufacturing.service.model.repository;

import com.kennuware.erp.manufacturing.service.model.Queue;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepository extends JpaRepository<Queue, String> {

  Optional<Queue> findByName(String name);

}
