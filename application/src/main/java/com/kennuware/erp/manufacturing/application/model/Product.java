package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

@Data
public class Product {
    Long id;    // Unique ID of product
    String name;    // Name of product
    private Recipe recipe;  // Product recipe
}
