package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

@Data
public class Recipe {
    Long id;
    String name;
    RecipeComponent[] components;
    String buildInstructions; // Instructions of how to build product
    int buildTime;  // Number of minutes to manufacture product
}
