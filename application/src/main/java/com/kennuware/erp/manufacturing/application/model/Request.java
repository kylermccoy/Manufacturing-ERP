package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

@Data
public class Request {

    private RequestType type;

    private Long id;

    private Product product;

    private long quantity;

    private boolean completed;

}
