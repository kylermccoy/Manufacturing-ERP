package com.kennuware.erp.manufacturing.service;

import com.kennuware.erp.manufacturing.service.model.Employee;
import com.kennuware.erp.manufacturing.service.model.Product;
import com.kennuware.erp.manufacturing.service.model.Request;
import com.kennuware.erp.manufacturing.service.model.RequestType;
import com.kennuware.erp.manufacturing.service.model.repository.EmployeeRepository;
import com.kennuware.erp.manufacturing.service.model.repository.ProductRepository;
import com.kennuware.erp.manufacturing.service.model.repository.RequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class ServiceApplicationTests {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	RequestRepository requestRepository;

	@BeforeEach
	void init() {
		productRepository.deleteAll();
		requestRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void addProductToRepository() {
		Assertions.assertEquals(productRepository.count(),  0);
		productRepository.save(new Product());
		Assertions.assertEquals(productRepository.count(), 1);
	}

	@Test
	void addOrderToRepository() {
		Assertions.assertEquals(requestRepository.count(), 0);
		Request request = new Request();
		request.setId(Integer.toUnsignedLong(1));
		request.setType(RequestType.ORDER);
		requestRepository.save(request);
		Assertions.assertEquals(requestRepository.count(), 1);
	}





}
