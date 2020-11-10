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
		itemRepo.save(item);
		assertTrue(itemRepo.existsByName(item.getName()));
	}


	/**
	 * Tests the ability to get an Item's name from the repository given an ID
	 */
	@Test
	void getItemNameTest() {
		itemRepo.save(item);
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
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		assertTrue(recipeComponentRepository.existsById(recipeComponent.getId()));
	}


	/**
	 * Tests getting a RecipeComponent from the RecipeComponent Repository
	 */
	@Test
	void getRecipeComponentTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		assertEquals(recipeComponentRepository.getOne(recipeComponent.getId()), recipeComponent);
	}


	/**
	 * Tests getting a RecipeComponent quantity from the RecipeComponent Repository
	 */
	@Test
	void getRecipeComponentQuantityTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		assertEquals(recipeComponentRepository.getOne(recipeComponent.getId()).getQuantity(), recipeComponent.getQuantity());
	}


	/**
	 * Tests getting a RecipeComponent's Item from the RecipeComponent Repository
	 */
	@Test
	void getRecipeComponentItemTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
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
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		assertTrue(recipeRepository.existsByName(recipe.getName()));
	}


	/**
	 * Tests the ability to get a Recipe from the repository given an ID
	 */
	@Test
	void getRecipeTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		assertEquals(recipeRepository.getOne(recipe.getId()), recipe);
	}


	/**
	 * Tests the ability to get a Recipe's name from the repository given an ID
	 */
	@Test
	void getRecipeNameTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		assertEquals(recipeRepository.getOne(recipe.getId()).getName(), recipe.getName());
	}


	/**
	 * Tests the ability to get a Recipe's build time from the repository given an ID
	 */
	@Test
	void getRecipeBuildTimeTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		assertEquals(recipeRepository.getOne(recipe.getId()).getBuildTime(), recipe.getBuildTime());
	}


	/**
	 * Tests the ability to get a Recipe's build time from the repository given an ID
	 */
	@Test
	void getRecipeBuildInstructionsTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		assertEquals(recipeRepository.getOne(recipe.getId()).getBuildInstructions(), recipe.getBuildInstructions());
	}


	/**
	 * Tests the ability to get a Recipe's components from the repository given an ID
	 */
	@Test
	void getRecipeComponentsTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		assertEquals(recipeRepository.getOne(recipe.getId()).getComponents(), recipe.getComponents());
	}


	////////////////////////////////////////////////////////////
	////////////////////  PRODUCT TESTS  ///////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding a Product to the Product Repository
	 */
	@Test
	void addProductTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		productRepo.save(product);
		assertTrue(productRepo.existsByName(product.getName()));
	}


	/**
	 * Tests the ability to get a Product's name from the repository given an ID
	 */
	@Test
	void getProductNameTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		productRepo.save(product);
		assertEquals(productRepo.getOne(product.getId()).getName(), "Book");
	}


	/**
	 * Tests the ability to get a Product's recipe from the repository given an ID
	 */
	@Test
	void getProductRecipeTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		productRepo.save(product);
		assertEquals(productRepo.getOne(product.getId()).getRecipe(), product.getRecipe());
	}


	////////////////////////////////////////////////////////////
	////////////////////  REQUEST TESTS  ///////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding a Request to the Request Repository
	 */
	@Test
	void addRequestTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		productRepo.save(product);
		requestRepo.save(request);
		assertEquals(requestRepo.getOne(request.getId()), request);
	}


	/**
	 * Tests getting the Request's type from the Request Repository
	 */
	@Test
	void getRequestType() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		productRepo.save(product);
		requestRepo.save(request);
		assertEquals(requestRepo.getOne(request.getId()).getType(), request.getType());
	}


	/**
	 * Tests getting the Request's product from the Request Repository
	 */
	@Test
	void getRequestProduct() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		productRepo.save(product);
		requestRepo.save(request);
		assertEquals(requestRepo.getOne(request.getId()).getProduct(), request.getProduct());
	}


	/**
	 * Tests getting the Request's type from the Request Repository
	 */
	@Test
	void getRequestQuantity() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		productRepo.save(product);
		requestRepo.save(request);
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
		queueRepo.save(queue);
		assertTrue(queueRepo.existsByName(queue.getName()));
	}


	/**
	 * Tests adding a Request to the Queue through the Queue Repository
	 */
	@Test
	void addRequestToQueueTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		productRepo.save(product);
		requestRepo.save(request);
		queueRepo.save(queue);
		queueRepo.findByName(queue.getName()).get().setRequestsInQueue(Stream.of(requestRepo.getOne(request.getId())).collect(Collectors.toList()));
		assertEquals(queueRepo.findByName(queue.getName()).get().getRequestsInQueue().get(0).getId(), request.getId());
	}


	/**
	 * Tests removing a Request from the Queue
	 */
	@Test
	void removeRequestFromQueueTest() {
		itemRepo.save(item);
		recipeComponentRepository.save(recipeComponent);
		recipeRepository.save(recipe);
		productRepo.save(product);
		requestRepo.save(request);
		queueRepo.save(queue);
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
		employeeRepo.save(employee);
		assertTrue(employeeRepo.existsById(employee.getId()));
	}


	/**
	 * Tests getting the hours worked by an Employee from the Employee Repository
	 */
	@Test
	void getHoursWorkedTest() {
		employeeRepo.save(employee);
		assertEquals(employeeRepo.getOne(employee.getId()).getHoursWorked(), employee.getHoursWorked());
	}

}