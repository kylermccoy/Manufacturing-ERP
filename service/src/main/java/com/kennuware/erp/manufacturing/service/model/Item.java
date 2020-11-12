package com.kennuware.erp.manufacturing.service.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Item {

  @Id
  private Long id; // Unique Item ID

  private String name; // Item name


  /**
   * Creates a new instance of an Item
   * @param name Item name
   */
  public Item(String name) {
    this.name = name;
  }

}
