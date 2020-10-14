package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

@Data
public class Recall {

    private Long id;

    private Product product;

    private long quantity;

    private boolean completed;

}