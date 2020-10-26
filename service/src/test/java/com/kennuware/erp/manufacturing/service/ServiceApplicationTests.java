package com.kennuware.erp.manufacturing.service;

import com.kennuware.erp.manufacturing.service.auth.AuthManager;
import com.kennuware.erp.manufacturing.service.controller.QueueController;
import com.kennuware.erp.manufacturing.service.model.Employee;
import com.kennuware.erp.manufacturing.service.model.Product;
import com.kennuware.erp.manufacturing.service.model.Queue;
import com.kennuware.erp.manufacturing.service.model.repository.EmployeeRepository;
import com.kennuware.erp.manufacturing.service.model.repository.ProductRepository;
import com.kennuware.erp.manufacturing.service.model.repository.QueueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@DataJpaTest
class ServiceApplicationTests {

	@Autowired ProductRepository productRepository;
	@Autowired QueueRepository repository;
	@Autowired EmployeeRepository employeeRepository;


	@Test
	void contextLoads() {
	}

	@Test
	void injectionComponentsAreNotNull() {
		Assertions.assertNotNull(productRepository);
		Assertions.assertNotNull(repository);
		Assertions.assertNotNull(employeeRepository);
	}

	@Test
	void addProductToProductRepo() {
		Product product = new Product();
		product.setName("Product");
		product.setId(Integer.toUnsignedLong(1));
		Assertions.assertEquals(productRepository.count(), 0);
		productRepository.save(product);
		Assertions.assertTrue(productRepository.existsByName("Product"));
	}

	@Test
	void addEmployeeToEmployeeRepo() {
		Employee employee = new Employee();
		employee.setUsername("admin");
		employee.setPassword("admin");
		employeeRepository.save(employee);
		Assertions.assertEquals(employeeRepository.findByUsername("admin"), employee);
	}

	//THIS ONE IS STUPID
	@Test
	void authenticateUser() {
		Employee employee = new Employee();
		employee.setUsername("admin");
		employee.setPassword("admin");
		employeeRepository.save(employee);
		Employee employee2 = employeeRepository.findByUsername("admin");
		Assertions.assertEquals(employee2.getPassword(), "admin");
	}






}
