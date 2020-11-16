package com.kennuware.erp.manufacturing.service;

import com.kennuware.erp.manufacturing.service.model.*;
import com.kennuware.erp.manufacturing.service.model.repository.*;
import java.util.List;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
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
	RecipeRepository recipeRepository;

	@Autowired
	RecipeComponentRepository recipeComponentRepository;

	@Autowired
	EmployeeRepository employeeRepo;

	Item item = new Item();

	Product product = new Product();

	Request request = new Request();

	Employee employee = new Employee();

	Recipe recipe = new Recipe();

	RecipeComponent recipeComponent = new RecipeComponent();

	Queue queue = new Queue();


	@BeforeEach
	void init() {
		// Item initialization
		item.setId(0L);
		item.setName("Paper");

		// RecipeComponent initialization
		recipeComponent.setItem(item);
		recipeComponent.setQuantity(Integer.toUnsignedLong(20));

		// Recipe initialization
		recipe.setName("Book Recipe");
		recipe.setId(Integer.toUnsignedLong(0));
		List<RecipeComponent> components = Stream.of(recipeComponent).collect(Collectors.toList());
		recipe.setComponents(components);
		recipe.setBuildTime(20);
		recipe.setBuildInstructions(Stream.of("Print pages", "Stitch pages").collect(Collectors.toList()));

		// Product initialization
		product.setId(Integer.toUnsignedLong(0));
		product.setName("Book");
		product.setRecipe(recipe);

		// Request initialization
		request.setType(RequestType.ORDER);
		//request.setProduct(product);
		request.setQuantity(3);

		// Employee initialization
		employee.setUsername("admin");
		employee.setPassword("admin");
		employee.setHoursWorked(30);

		// Queue initialization
		queue.setName("QUEUE");
	}


	////////////////////////////////////////////////////////////
	/////////////////////  ITEM TESTS  /////////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding an Item to the Item Repository
	 */
	@Test
	void addItemTest() {
		itemRepo.saveAndFlush(item);
		assertTrue(itemRepo.existsByName(item.getName()));
	}


	/**
	 * Tests getting an Item from the Item Repository
	 */
	@Test
	void getItemTest() {
		itemRepo.saveAndFlush(item);
		assertEquals(itemRepo.getOne(item.getId()), item);
	}


	/**
	 * Tests the ability to get an Item's name from the repository given an ID
	 */
	@Test
	void getItemNameTest() {
		itemRepo.saveAndFlush(item);
		assertTrue(itemRepo.existsByName(item.getName()));
		Assertions.assertEquals(itemRepo.getOne(item.getId()).getName(), "Paper");
	}


	////////////////////////////////////////////////////////////
	/////////////////  REC. COMPONENT TESTS  ///////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding a RecipeComponent to the RecipeComponent Repository
	 */
	@Test
	void addRecipeComponentTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		assertTrue(recipeComponentRepository.existsById(recipeComponent.getId()));
	}


	/**
	 * Tests getting a RecipeComponent from the RecipeComponent Repository
	 */
	@Test
	void getRecipeComponentTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		assertEquals(recipeComponentRepository.getOne(recipeComponent.getId()), recipeComponent);
	}


	/**
	 * Tests getting a RecipeComponent quantity from the RecipeComponent Repository
	 */
	@Test
	void getRecipeComponentQuantityTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		assertEquals(recipeComponentRepository.getOne(recipeComponent.getId()).getQuantity(), recipeComponent.getQuantity());
	}


	/**
	 * Tests getting a RecipeComponent's Item from the RecipeComponent Repository
	 */
	@Test
	void getRecipeComponentItemTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		assertEquals(recipeComponentRepository.getOne(recipeComponent.getId()).getItem(), recipeComponent.getItem());
	}

	////////////////////////////////////////////////////////////
	/////////////////////  RECIPE TESTS  ///////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding a Recipe to the Recipe Repository
	 */
	@Test
	void addRecipeTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		assertTrue(recipeRepository.existsByName(recipe.getName()));
	}


	/**
	 * Tests the ability to get a Recipe from the repository given an ID
	 */
	@Test
	void getRecipeTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		assertEquals(recipeRepository.getOne(recipe.getId()).toString(), recipe.toString());
	}


	/**
	 * Tests the ability to get a Recipe's name from the repository given an ID
	 */
	@Test
	void getRecipeNameTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		assertEquals(recipeRepository.getOne(recipe.getId()).getName(), recipe.getName());
	}


	/**
	 * Tests the ability to get a Recipe's build time from the repository given an ID
	 */
	@Test
	void getRecipeBuildTimeTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		assertEquals(recipeRepository.getOne(recipe.getId()).getBuildTime(), recipe.getBuildTime());
	}


	/**
	 * Tests the ability to get a Recipe's build time from the repository given an ID
	 */
	@Test
	void getRecipeBuildInstructionsTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		assertEquals(recipeRepository.getOne(recipe.getId()).getBuildInstructions().toString(), recipe.getBuildInstructions().toString());
	}


	/**
	 * Tests the ability to get a Recipe's components from the repository given an ID
	 */
	@Test
	void getRecipeComponentsFromRecipeTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		assertEquals(recipeRepository.getOne(recipe.getId()).getComponents().toString(), recipe.getComponents().toString());
	}


	////////////////////////////////////////////////////////////
	////////////////////  PRODUCT TESTS  ///////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding a Product to the Product Repository
	 */
	@Test
	void addProductTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		productRepo.saveAndFlush(product);
		assertTrue(productRepo.existsByName(product.getName()));
	}


	/**
	 * Tests the ability to get a Product from the repository given an ID
	 */
	@Test
	void getProductTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		productRepo.saveAndFlush(product);
		assertEquals(productRepo.getOne(product.getId()).toString(), product.toString());
	}


	/**
	 * Tests the ability to get a Product's name from the repository given an ID
	 */
	@Test
	void getProductNameTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		productRepo.saveAndFlush(product);
		assertEquals(productRepo.getOne(product.getId()).getName(), "Book");
	}


	/**
	 * Tests the ability to get a Product's recipe from the repository given an ID
	 */
	@Test
	void getProductRecipeTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		productRepo.saveAndFlush(product);
		assertEquals(productRepo.getOne(product.getId()).getRecipe().toString(), product.getRecipe().toString());
	}


	////////////////////////////////////////////////////////////
	////////////////////  REQUEST TESTS  ///////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding a Request to the Request Repository
	 */
	@Test
	void addRequestTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		productRepo.saveAndFlush(product);
		requestRepo.saveAndFlush(request);
		assertTrue(requestRepo.existsById(request.getId()));
	}


	/**
	 * Tests getting a Request to the Request Repository
	 */
	@Test
	void getRequestTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		productRepo.saveAndFlush(product);
		requestRepo.saveAndFlush(request);
		assertEquals(requestRepo.getOne(request.getId()), request);
	}


	/**
	 * Tests getting the Request's type from the Request Repository
	 */
	@Test
	void getRequestType() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		productRepo.saveAndFlush(product);
		requestRepo.saveAndFlush(request);
		assertEquals(requestRepo.getOne(request.getId()).getType(), request.getType());
	}


	/**
	 * Tests getting the Request's product from the Request Repository
	 */
	@Test
	void getRequestProduct() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		productRepo.saveAndFlush(product);
		requestRepo.saveAndFlush(request);
		assertEquals(requestRepo.getOne(request.getId()).getProduct(), request.getProduct());
	}


	/**
	 * Tests getting the Request's type from the Request Repository
	 */
	@Test
	void getRequestQuantity() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		productRepo.saveAndFlush(product);
		requestRepo.saveAndFlush(request);
		assertEquals(requestRepo.getOne(request.getId()).getQuantity(), request.getQuantity());
	}


	////////////////////////////////////////////////////////////
	/////////////////////  QUEUE TESTS  ////////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding a Queue to the Queue Repository through the Queue Repository
	 */
	@Test
	void addQueueTest() {
		queueRepo.saveAndFlush(queue);
		assertTrue(queueRepo.existsByName(queue.getName()));
	}


	/**
	 * Tests getting a Queue from the Queue Repository through the Queue Repository
	 */
	@Test
	void getQueueTest() {
		queueRepo.saveAndFlush(queue);
		assertEquals(queueRepo.getOne(queue.getName()), queue);
	}


	/**
	 * Tests getting a Queue's name from the Queue Repository through the Queue Repository
	 */
	@Test
	void getQueueNameTest() {
		queueRepo.saveAndFlush(queue);
		assertEquals(queueRepo.getOne(queue.getName()).getName(), queue.getName());
	}


	/**
	 * Tests adding a Request to the Queue through the Queue Repository
	 */
	@Test
	void addRequestToQueueTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		productRepo.saveAndFlush(product);
		requestRepo.saveAndFlush(request);
		queueRepo.saveAndFlush(queue);
		queueRepo.findByName(queue.getName()).get().setRequestsInQueue(Stream.of(requestRepo.getOne(request.getId())).collect(Collectors.toList()));
		assertEquals(queueRepo.findByName(queue.getName()).get().getRequestsInQueue().get(0).getId(), request.getId());
	}


	/**
	 * Tests removing a Request from the Queue through the Queue Repository
	 */
	@Test
	void removeRequestFromQueueTest() {
		itemRepo.saveAndFlush(item);
		recipeComponentRepository.saveAndFlush(recipeComponent);
		recipeRepository.saveAndFlush(recipe);
		productRepo.saveAndFlush(product);
		requestRepo.saveAndFlush(request);
		queueRepo.saveAndFlush(queue);
		queueRepo.findByName(queue.getName()).get().setRequestsInQueue(Stream.of(requestRepo.getOne(request.getId())).collect(Collectors.toList()));
		queueRepo.findByName(queue.getName()).get().getRequestsInQueue().remove(request);
		assertFalse(queueRepo.findByName(queue.getName()).get().getRequestsInQueue().contains(request));
	}


	////////////////////////////////////////////////////////////
	///////////////////  EMPLOYEE TESTS  ///////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding an Employee to the Employee Repository
	 */
	@Test
	void addEmployeeTest() {
		employeeRepo.saveAndFlush(employee);
		assertTrue(employeeRepo.existsById(employee.getId()));
	}


	/**
	 * Tests getting the hours worked by an Employee from the Employee Repository
	 */
	@Test
	void getHoursWorkedTest() {
		employeeRepo.saveAndFlush(employee);
		assertEquals(employeeRepo.getOne(employee.getId()).getHoursWorked(), employee.getHoursWorked());
	}


	/**
	 * Tests getting the username of an Employee from the Employee Repository
	 */
	@Test
	void getEmployeeUsernameTest() {
		employeeRepo.saveAndFlush(employee);
		assertEquals(employeeRepo.getOne(employee.getId()).getUsername(), employee.getUsername());
	}


	/**
	 * Tests getting the password of an Employee from the Employee Repository
	 */
	@Test
	void getEmployeePasswordTest() {
		employeeRepo.saveAndFlush(employee);
		assertEquals(employeeRepo.getOne(employee.getId()).getPassword(), employee.getPassword());
	}

}