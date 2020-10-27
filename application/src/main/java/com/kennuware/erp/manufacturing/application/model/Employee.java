package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

@Data
public class Employee {
    int hoursWorked;    // Hours worked by employee
    String user;    // Employee

    /**
     * Creates an Employee
     * @param user employee
     * @param hoursWorked hours worked
     */
    public Employee(String user, int hoursWorked){
        this.hoursWorked= hoursWorked;
        this.user = user;
    }
}
