package com.kennuware.erp.manufacturing.service.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Product {

  @Id
  private Long id; // Unique Product ID

  private String name; // Product name

  @ManyToOne(cascade = CascadeType.ALL)
  private Recipe recipe; // Product recipe

}
