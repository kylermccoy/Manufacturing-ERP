package com.kennuware.erp.manufacturing.service.model;

import java.util.List;
import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class Recipe {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @OneToMany(cascade = CascadeType.ALL)
  private List<RecipeComponent> components;

  @ElementCollection
  private List<String> buildInstructions; // Instructions of how to manufacture product

  private int buildTime;  // How many minutes manufacturing of product will take

}
