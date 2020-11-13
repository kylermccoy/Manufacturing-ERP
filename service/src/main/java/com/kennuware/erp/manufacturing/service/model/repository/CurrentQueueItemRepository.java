package com.kennuware.erp.manufacturing.service.model.repository;

import com.kennuware.erp.manufacturing.service.model.CurrentQueueItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentQueueItemRepository extends JpaRepository<CurrentQueueItem, Long> {

  Optional<CurrentQueueItem> findDistinctFirstByOrderByIdDesc();

}
