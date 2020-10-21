package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

@Data
public class RecipeComponent {
    Long id;    // Unique ID of Component
    Item item;  // Item in the Recipe
    Long quantity;  // Quantity of the Item
}
