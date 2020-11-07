package com.kennuware.erp.manufacturing.service;

import com.kennuware.erp.manufacturing.service.model.*;
import com.kennuware.erp.manufacturing.service.model.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceApplicationTests {

	@Autowired
	ItemRepository itemRepo;

	@Autowired
	ProductRepository productRepo;

	@Autowired
	RequestRepository requestRepo;

	@Autowired
	QueueRepository queueRepo;

	@Autowired
	EmployeeRepository employeeRepo;


	/**
	 * Tests the add/delete functionality of an Item in the Item Repository
	 */
	@Test
	void addAndDeleteItemTest() {
		long originalCount = itemRepo.count();
		Item item = new Item("Brass");
		itemRepo.save(item);
		Assertions.assertTrue(originalCount < itemRepo.count());
		Assertions.assertTrue(itemRepo.existsByName("Brass"));
		itemRepo.deleteById(item.getId());
		Assertions.assertTrue(originalCount == itemRepo.count());
		Assertions.assertFalse(itemRepo.existsByName("Brass"));
	}


	/**
	 * Tests the add/delete functionality of a Product in the Product Repository
	 */
	@Test
	void addAndDeleteProductTest() {
		long originalCount = productRepo.count();
		Product product = new Product();
		product.setName("Hoverboard");
		product.setId(Integer.toUnsignedLong(100));
		productRepo.save(product);
		Assertions.assertTrue(originalCount < productRepo.count());
		Assertions.assertTrue(productRepo.existsByName("Hoverboard"));
		productRepo.delete(product);
		Assertions.assertTrue(originalCount == productRepo.count());
		Assertions.assertFalse(productRepo.existsByName("Hoverboard"));
	}


	/**
	 * Tests the add/delete functionality of a Request in the Request Repository
	 */
	@Test
	void addAndDeleteRequestTest() {
		long originalCount = requestRepo.count();
		Request request = new Request();
		requestRepo.save(request);
		Assertions.assertTrue(originalCount < requestRepo.count());
		Assertions.assertTrue(requestRepo.existsById(request.getId()));
		requestRepo.delete(request);
		Assertions.assertTrue(originalCount == requestRepo.count());
		Assertions.assertFalse(requestRepo.existsById(request.getId()));
	}


	/**
	 * Tests the add/delete functionality of an Employee in the Employee Repository
	 */
	@Test
	void addAndDeleteEmployeeTest() {
		long originalCount = employeeRepo.count();
		Employee employee = new Employee();
		employeeRepo.save(employee);
		Assertions.assertTrue(originalCount < employeeRepo.count());
		Assertions.assertTrue(employeeRepo.existsById(employee.getId()));
		employeeRepo.delete(employee);
		Assertions.assertTrue(originalCount == employeeRepo.count());
		Assertions.assertFalse(employeeRepo.existsById(employee.getId()));
	}



}
