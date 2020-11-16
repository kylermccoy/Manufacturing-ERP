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
  private String name; // Queue name

  @OneToMany
  private List<Request> requestsInQueue; // List of requests in queue

  private boolean running;  // Specifies whether or not the queue is running

  private String timeLeft;

}
