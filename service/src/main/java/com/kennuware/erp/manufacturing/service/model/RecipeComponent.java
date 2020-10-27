package com.kennuware.erp.manufacturing.service.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class RecipeComponent {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; // Unique Recipe component ID

  @ManyToOne
  private Item item; // Recipe Component Item

  private long quantity; // Item quantity


  /**
   * Creates a new instance of RecipeComponent
   * @param item Item
   * @param quantity Item quantity
   */
  public RecipeComponent(Item item, long quantity) {
    this.item = item;
    this.quantity = quantity;
  }

}
