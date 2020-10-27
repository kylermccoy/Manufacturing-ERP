package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

import java.util.List;

@Data
public class Recipe {

    Long id;    // Unique ID of Recipe
    String name;    // Name of Recipe
    RecipeComponent[] components;   // Contents of Recipe
    List<String> buildInstructions; // Instructions of how to build product
    int buildTime;  // Number of minutes to manufacture product
}
