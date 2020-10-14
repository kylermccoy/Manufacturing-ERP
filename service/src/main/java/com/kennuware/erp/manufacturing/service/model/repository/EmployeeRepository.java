package com.kennuware.erp.manufacturing.service.model.repository;

import com.kennuware.erp.manufacturing.service.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Employee findByUsername(String username);

  boolean existsByUsername(String username);

}
