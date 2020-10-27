package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

@Data
public class Request {

    private RequestType type;   // Type of Request

    private Long id;    // Unique ID of Request

    private Product product;    // Product associated with Request

    private long quantity;  // Quantity of product

    private boolean completed;  // Signifies whether or not the Request has been completed

}
