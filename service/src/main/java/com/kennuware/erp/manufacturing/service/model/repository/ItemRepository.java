package com.kennuware.erp.manufacturing.service.model.repository;

import com.kennuware.erp.manufacturing.service.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
