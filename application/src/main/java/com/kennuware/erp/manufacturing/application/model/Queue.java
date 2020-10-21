package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

import java.util.List;

@Data
public class Queue {
    private String name;    // Name of Queue

    private Request[] requestsInQueue;  // Contents of the queue

    private boolean running;    // Signifies if queue is running

}
