package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

@Data
public class Order {

    private Long id;

    private Product product;

    private long quantity;

    private boolean completed;

}
