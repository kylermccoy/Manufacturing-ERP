package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

import java.util.List;

@Data
public class Queue {
    private String name;

    private Request[] requestsInQueue;

    private boolean running;

    private String timeLeft;

}
