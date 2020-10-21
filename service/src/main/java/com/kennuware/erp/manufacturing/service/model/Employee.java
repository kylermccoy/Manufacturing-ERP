package com.kennuware.erp.manufacturing.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; // Unique Employee ID

  private String username; // Employee username

  // users dont need to see this
  @JsonIgnore
  private String password; // Employee password

  private long hoursWorked; // Hours worked by employee

}
