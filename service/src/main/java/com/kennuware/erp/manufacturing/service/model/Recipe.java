package com.kennuware.erp.manufacturing.service.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Recipe {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id; // Unique recipe ID

  private String name; // Recipe name

  @OneToMany(cascade = CascadeType.ALL)
  private List<RecipeComponent> components; // List of recipe components

}
