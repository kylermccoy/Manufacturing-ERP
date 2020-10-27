package com.kennuware.erp.manufacturing.service.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@NoArgsConstructor
@Data
@Entity
public class Request {

  @NonNull
  private RequestType type; // Request type

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;  // Unique Request ID

  @ManyToOne
  private Product product; // Product

  private long quantity;  // Product quantity

  private boolean completed;  // Specifies whether or not the request has been complete

}
