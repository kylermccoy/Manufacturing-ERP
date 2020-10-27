package com.kennuware.erp.manufacturing.service.model;

import java.util.List;
import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class Recipe {

  @Id
  private Long id; // Unique recipe ID

  private String name; // Recipe name

  @OneToMany(cascade = CascadeType.ALL)
  private List<RecipeComponent> components; // List of recipe components

  @ElementCollection
  private List<String> buildInstructions; // Instructions of how to manufacture product

  private int buildTime;  // How many minutes manufacturing of product will take

}
