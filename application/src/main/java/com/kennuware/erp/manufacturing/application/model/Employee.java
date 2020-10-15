package com.kennuware.erp.manufacturing.application.model;

import lombok.Data;

@Data
public class Employee {
    int hoursWorked;
    String user;

    public Employee(String user, int hoursWorked){
        this.hoursWorked= hoursWorked;
        this.user = user;
    }
}
