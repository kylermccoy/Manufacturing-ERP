package com.kennuware.erp.manufacturing.service.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Queue {

  @Id
  private String name;

  @OneToMany
  private List<Request> requestsInQueue;

  private boolean running;

  private String timeLeft;

}
