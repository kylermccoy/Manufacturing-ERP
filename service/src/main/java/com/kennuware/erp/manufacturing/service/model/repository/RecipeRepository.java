package com.kennuware.erp.manufacturing.service.model.repository;

import com.kennuware.erp.manufacturing.service.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

  boolean existsByName(String name);

}
