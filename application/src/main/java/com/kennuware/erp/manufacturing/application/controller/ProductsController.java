package com.kennuware.erp.manufacturing.application.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.kennuware.erp.manufacturing.application.controller.util.RequestSender;
import com.kennuware.erp.manufacturing.application.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
@Controller
public class ProductsController {

    /**
     * Requests a list of all products from backend
     * @param model Model
     * @return Redirection to products page
     */
    @GetMapping("/products")
    public String getProducts(Model model, HttpSession session) {
        ResponseEntity<Product[]> response1 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/products", Product[].class, session);
        Product[] products = response1.getBody();
        model.addAttribute("products", products);
        return "products";
    }


    /**
     * Requests specific product data from backend
     * @param id Unique Product ID
     * @param model Model
     * @return Redirection to specific product page
     */
    @GetMapping(path = "/products/{id}")
    String getProduct(@PathVariable Long id, Model model, HttpSession session) {
        ResponseEntity<Product> response1 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/products/" + id, Product.class, session);
        Product product = response1.getBody();
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping(path="/products/create")
    String createProduct(Model model, HttpSession session){
        return "createProduct";
    }

    @RequestMapping(path = "/products/creating")
    Object creatingProduct(@RequestParam String name, @RequestParam int buildTime, @RequestParam String instructions, @RequestParam int item0, @RequestParam int item1, @RequestParam int item2, @RequestParam int item3, @RequestParam int item4, @RequestParam int item5, @RequestParam int item6, @RequestParam int item7, @RequestParam int item8, @RequestParam int item9, @RequestParam int item10, @RequestParam int item11, @RequestParam int item12, @RequestParam int item13, @RequestParam int item14, @RequestParam int item15, @RequestParam int item16, @RequestParam int item17, @RequestParam int item18, @RequestParam int item19, @RequestParam int item20, @RequestParam int item21, @RequestParam int item22, @RequestParam int item23, @RequestParam int item24, @RequestParam int item25, @RequestParam int item26, Model model, HttpSession session){
        ResponseEntity<Item[]> response1 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/items", Item[].class, session);
        Item[] items = response1.getBody();
        ArrayList<RecipeComponent> recipeComponents = new ArrayList<>();

        if (item0 > 0) {
            RecipeComponent temp0 = new RecipeComponent();
            temp0.setItem(items[0]);
            temp0.setQuantity((long) item0);
            recipeComponents.add(temp0);
        }
        if (item1 > 0) {
            RecipeComponent temp1 = new RecipeComponent();
            temp1.setItem(items[1]);
            temp1.setQuantity((long) item1);
            recipeComponents.add(temp1);
        }
        if (item2 > 0) {
            RecipeComponent temp2 = new RecipeComponent();
            temp2.setItem(items[2]);
            temp2.setQuantity((long) item2);
            recipeComponents.add(temp2);
        }
        if (item3 > 0) {
            RecipeComponent temp3 = new RecipeComponent();
            temp3.setItem(items[3]);
            temp3.setQuantity((long) item3);
            recipeComponents.add(temp3);
        }
        if (item4 > 0) {
            RecipeComponent temp4 = new RecipeComponent();
            temp4.setItem(items[4]);
            temp4.setQuantity((long) item4);
            recipeComponents.add(temp4);
        }
        if (item5 > 0) {
            RecipeComponent temp5 = new RecipeComponent();
            temp5.setItem(items[5]);
            temp5.setQuantity((long) item5);
            recipeComponents.add(temp5);
        }
        if (item6 > 0) {
            RecipeComponent temp6 = new RecipeComponent();
            temp6.setItem(items[6]);
            temp6.setQuantity((long) item6);
            recipeComponents.add(temp6);
        }
        if (item7 > 0) {
            RecipeComponent temp7 = new RecipeComponent();
            temp7.setItem(items[7]);
            temp7.setQuantity((long) item7);
            recipeComponents.add(temp7);
        }
        if (item8 > 0) {
            RecipeComponent temp8 = new RecipeComponent();
            temp8.setItem(items[8]);
            temp8.setQuantity((long) item8);
            recipeComponents.add(temp8);
        }
        if (item9 > 0) {
            RecipeComponent temp9 = new RecipeComponent();
            temp9.setItem(items[9]);
            temp9.setQuantity((long) item9);
            recipeComponents.add(temp9);
        }
        if (item10 > 0) {
            RecipeComponent temp10 = new RecipeComponent();
            temp10.setItem(items[10]);
            temp10.setQuantity((long) item10);
            recipeComponents.add(temp10);
        }
        if (item11 > 0) {
            RecipeComponent temp11 = new RecipeComponent();
            temp11.setItem(items[11]);
            temp11.setQuantity((long) item11);
            recipeComponents.add(temp11);
        }
        if (item12 > 0) {
            RecipeComponent temp12 = new RecipeComponent();
            temp12.setItem(items[12]);
            temp12.setQuantity((long) item12);
            recipeComponents.add(temp12);
        }
        if (item13 > 0) {
            RecipeComponent temp13 = new RecipeComponent();
            temp13.setItem(items[13]);
            temp13.setQuantity((long) item13);
            recipeComponents.add(temp13);
        }
        if (item14 > 0) {
            RecipeComponent temp14 = new RecipeComponent();
            temp14.setItem(items[14]);
            temp14.setQuantity((long) item14);
            recipeComponents.add(temp14);
        }
        if (item15 > 0) {
            RecipeComponent temp15 = new RecipeComponent();
            temp15.setItem(items[15]);
            temp15.setQuantity((long) item15);
            recipeComponents.add(temp15);
        }
        if (item16 > 0) {
            RecipeComponent temp16 = new RecipeComponent();
            temp16.setItem(items[16]);
            temp16.setQuantity((long) item16);
            recipeComponents.add(temp16);
        }
        if (item17 > 0) {
            RecipeComponent temp17 = new RecipeComponent();
            temp17.setItem(items[17]);
            temp17.setQuantity((long) item17);
            recipeComponents.add(temp17);
        }
        if (item18 > 0) {
            RecipeComponent temp18 = new RecipeComponent();
            temp18.setItem(items[18]);
            temp18.setQuantity((long) item18);
            recipeComponents.add(temp18);
        }
        if (item19 > 0) {
            RecipeComponent temp19 = new RecipeComponent();
            temp19.setItem(items[19]);
            temp19.setQuantity((long) item19);
            recipeComponents.add(temp19);
        }
        if (item20 > 0) {
            RecipeComponent temp20 = new RecipeComponent();
            temp20.setItem(items[20]);
            temp20.setQuantity((long) item20);
            recipeComponents.add(temp20);
        }
        if (item21 > 0) {
            RecipeComponent temp21 = new RecipeComponent();
            temp21.setItem(items[21]);
            temp21.setQuantity((long) item21);
            recipeComponents.add(temp21);
        }
        if (item22 > 0) {
            RecipeComponent temp22 = new RecipeComponent();
            temp22.setItem(items[22]);
            temp22.setQuantity((long) item22);
            recipeComponents.add(temp22);
        }
        if (item23 > 0) {
            RecipeComponent temp23 = new RecipeComponent();
            temp23.setItem(items[23]);
            temp23.setQuantity((long) item23);
            recipeComponents.add(temp23);
        }
        if (item24 > 0) {
            RecipeComponent temp24 = new RecipeComponent();
            temp24.setItem(items[24]);
            temp24.setQuantity((long) item24);
            recipeComponents.add(temp24);
        }
        if (item25 > 0) {
            RecipeComponent temp25 = new RecipeComponent();
            temp25.setItem(items[25]);
            temp25.setQuantity((long) item25);
            recipeComponents.add(temp25);
        }
        if (item26 > 0) {
            RecipeComponent temp26 = new RecipeComponent();
            temp26.setItem(items[26]);
            temp26.setQuantity((long) item26);
            recipeComponents.add(temp26);
        }

        ResponseEntity<Product[]> response2 = RequestSender.getForObject("http://localhost:8080/manufacturing/api/products", Product[].class, session);
        Product[] products = response2.getBody();

        Recipe recipe = new Recipe();
        recipe.setName(name + " Recipe");
        recipe.setBuildInstructions(Arrays.asList(instructions.split("\n")));
        recipe.setBuildTime(buildTime);
        recipe.setComponents(recipeComponents.toArray(new RecipeComponent[recipeComponents.size()]));
        recipe.setId((long) (products.length));

        Product product = new Product();
        product.setName(name);
        product.setRecipe(recipe);
        product.setId((long) (products.length));

        ResponseEntity<JsonNode> response3 = RequestSender.postForObject("http://localhost:8080/manufacturing/api/products/", product, JsonNode.class, session);
        boolean success = response3.getStatusCode().is2xxSuccessful();
        if (!success) {
            model.addAttribute("failure", true);
            return "createProduct";
        }

        return new RedirectView("/products");
    }

    /*
     * Deliver products to inventory
     */
    @PostMapping(path = "/products/deliver")
    String deliverProducts(Product product, int quantity) {
        return null;
    }

    /*
     * Request products from inventory
     */
    @PostMapping(path = "products/request")
    String requestProducts(Long productID, int quantity) {
        return null;
    }

}