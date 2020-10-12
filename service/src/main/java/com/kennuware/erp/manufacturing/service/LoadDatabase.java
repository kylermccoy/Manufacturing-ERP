package com.kennuware.erp.manufacturing.service;

import com.kennuware.erp.manufacturing.service.model.Item;
import com.kennuware.erp.manufacturing.service.model.Product;
import com.kennuware.erp.manufacturing.service.model.Recipe;
import com.kennuware.erp.manufacturing.service.model.RecipeComponent;
import com.kennuware.erp.manufacturing.service.model.repository.ProductRepository;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;

@Slf4j
@Configuration
public class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(ProductRepository repository) {
    return args -> {
      Product p = new Product();
      p.setName("Table");
      Recipe r = new Recipe();
      r.setName("Table Recipe");
      Item tabletop = new Item("Tabletop");
      Item leg = new Item("Leg");
      Item screw = new Item("Screw");
      RecipeComponent tabletopComponent = new RecipeComponent(tabletop, 1);
      RecipeComponent legComponent = new RecipeComponent(leg, 4);
      RecipeComponent screwComponent = new RecipeComponent(screw, 4);
      List<RecipeComponent> components = Arrays.asList(tabletopComponent, legComponent, screwComponent);
      r.setComponents(components);
      p.setRecipe(r);
      if (!repository.existsByName(p.getName())) {
        log.info("Preloading " + repository.save(p));
      } else {
        log.info("Skipping preloading, already present");
      }
    };
  }

}
