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

	private static Item item = new Item();

	private static Product product = new Product();

	private static Request request = new Request();

	private static Employee employee = new Employee();

	private static Recipe recipe = new Recipe();

	private static RecipeComponent recipeComponent = new RecipeComponent();


	@BeforeAll
	public static void init() {
		// Item initialization
		item.setName("Paper");

		// RecipeComponent initialization
		recipeComponent.setItem(item);
		recipeComponent.setQuantity(Integer.toUnsignedLong(20));

		// Recipe initialization
		recipe.setName("Book Recipe");
		List<RecipeComponent> components = Stream.of(recipeComponent).collect(Collectors.toList());
		recipe.setComponents(components);
		recipe.setBuildTime(20);
		recipe.setBuildInstructions(Stream.of("Print pages", "Stitch pages").collect(Collectors.toList()));

		// Product initialization
		product.setName("Book");
		product.setRecipe(recipe);

		// Request initialization
		request.setType(RequestType.ORDER);
		request.setProduct(product);
		request.setQuantity(3);

		// Employee initialization
		employee.setUsername("admin");
		employee.setPassword("admin");
		employee.setHoursWorked(30);
	}


	////////////////////////////////////////////////////////////
	/////////////////////  ITEM TESTS  /////////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding an Item to the Item Repository
	 */
	@Test
	void addItemTest() {
		if (!itemRepo.existsByName(item.getName())) {
			itemRepo.saveAndFlush(item);
			Assertions.assertTrue(itemRepo.existsByName(item.getName()));
		}
	}


	/**
	 * Tests the ability to get an Item's name from the repository given an ID
	 */
   /*@Test
   void getItemNameTest() {
      Assertions.assertEquals(itemRepo.getOne(item.getId()).getName(), "Paper");
   }*/


	/**
	 * Tests deleting an Item from the Item Repository
	 */
   /*@Test
   void deleteItemTest() {

   }*/


	////////////////////////////////////////////////////////////
	/////////////////  REC. COMPONENT TESTS  ///////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding a RecipeComponent to the RecipeComponent Repository
	 */
	@Test
	void addRecipeComponentTest() {
		if (recipeComponent.getId() == null || !recipeComponentRepository.existsById(recipeComponent.getId())) {
			recipeComponentRepository.saveAndFlush(recipeComponent);
			Assertions.assertTrue(recipeComponentRepository.existsById(recipeComponent.getId()));
		}
	}


	////////////////////////////////////////////////////////////
	/////////////////////  RECIPE TESTS  ///////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding a Recipe to the Recipe Repository
	 */
	@Test
	void addRecipeTest() {
		if (recipe.getId() == null || !recipeRepository.existsByName(recipe.getName())) {
			recipeRepository.save(recipe);
			Assertions.assertTrue(recipeRepository.existsByName(recipe.getName()));
		}
	}


	/**
	 * Tests the ability to get a Recipe's name from the repository given an ID
	 */
	@Test
	void getRecipeNameTest() {
		Assertions.assertEquals(recipeRepository.findById(recipe.getId()).get().getName(), "Book Recipe");
	}


	/**
	 * Tests the ability to get a Recipe's build time from the repository given an ID
	 */
	@Test
	void getRecipeBuildTimeTest() {
		Assertions.assertEquals(recipeRepository.findById(recipe.getId()).get().getBuildTime(), recipe.getBuildTime());
	}


	/**
	 * Tests the ability to get a Recipe's build time from the repository given an ID
	 */
	@Test
	void getRecipeBuildInstructionsTest() {
		Assertions.assertEquals(recipeRepository.findById(recipe.getId()).get().getBuildInstructions(), recipe.getBuildInstructions());
	}


	/**
	 * Tests the ability to get a Recipe's components from the repository given an ID
	 */
	@Test
	void getRecipeComponentsTest() {
		Assertions.assertEquals(recipeRepository.findById(recipe.getId()).get().getComponents(), recipe.getComponents());
	}


	/**
	 * Tests deleting a Recipe from the Recipe Repository
	 */
// @Test
// void deleteRecipeTest() {
// }


	/**
	 * Tests deleting a RecipeComponent from the RecipeComponent Repository
	 */
// @Test
// void deleteRecipeComponentTest() {
// }


	////////////////////////////////////////////////////////////
	////////////////////  PRODUCT TESTS  ///////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding a Product to the Product Repository
	 */
	@Test
	void addProductTest() {
		if (product.getId() == null || !productRepo.existsByName(product.getName())) {
			productRepo.save(product);
			Assertions.assertTrue(productRepo.existsByName(product.getName()));
		}
	}


	/**
	 * Tests the ability to get a Product's name from the repository given an ID
	 */
	@Test
	void getProductNameTest() {
		Assertions.assertEquals(productRepo.findById(product.getId()).get().getName(), "Book");
	}


	/**
	 * Tests the ability to get a Product's recipe from the repository given an ID
	 */
	@Test
	void getProductRecipeTest() {
		Assertions.assertEquals(productRepo.findById(product.getId()).get().getRecipe(), product.getRecipe());
	}


	/**
	 * Tests deleting a Product from the Product Repository
	 */
   /*
   @Test
   void deleteProductTest() {
      productRepo.deleteById(Integer.toUnsignedLong(500));
      if (productRepo.existsByName(product.getName())) {
         productRepo.deleteById(product);
         Assertions.assertFalse(productRepo.existsByName(product.getName()));
      }
   }*/


	////////////////////////////////////////////////////////////
	////////////////////  REQUEST TESTS  ///////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding a Request to the Request Repository
	 */
	@Test
	void addRequestTest() {
		if (request.getId() == null || !requestRepo.existsById((request.getId()))) {
			requestRepo.saveAndFlush(request);
			Assertions.assertTrue(requestRepo.existsById(request.getId()));
		}
	}


	/**
	 * Tests deleting a Request from the Request Repository
	 */
// @Test
// void deleteRequestTest() {
// }


	////////////////////////////////////////////////////////////
	///////////////////  EMPLOYEE TESTS  ///////////////////////
	////////////////////////////////////////////////////////////

	/**
	 * Tests adding an Employee to the Employee Repository
	 */
	@Test
	void addEmployeeTest() {
		if (employee.getId() == null || !employeeRepo.existsById(employee.getId())) {
			employeeRepo.saveAndFlush(employee);
			Assertions.assertTrue(employeeRepo.existsByUsername(employee.getUsername()));
		}
	}


	/**
	 * Tests getting the hours worked by an Employee from the Employee Repository
	 */
	@Test
	void getHoursWorkedTest() {
		Assertions.assertEquals(employeeRepo.getOne(employee.getId()).getHoursWorked(), employee.getHoursWorked());
	}


	/**
	 * Tests deleting an Employee from the Employee Repository
	 */
// @Test
// void deleteEmployeeTest() {
// }
}