package com.kennuware.erp.manufacturing.service.model;

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
  private Long id;

  private String username;

  private String password;

  private byte[] hashSalt;

  private long hoursWorked;

}
