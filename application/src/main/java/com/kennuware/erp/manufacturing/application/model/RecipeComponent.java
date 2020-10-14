package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

@Data
public class RecipeComponent {
    Long id;
    Item item;
    Long quantity;
}
