package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

@Data
public class Recipe {
    Long id;    // Unique ID of Recipe
    String name;    // Name of Recipe
    RecipeComponent[] components;   // Contents of Recipe
}
