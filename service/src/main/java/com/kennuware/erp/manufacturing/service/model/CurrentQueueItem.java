package com.kennuware.erp.manufacturing.service.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
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
public class CurrentQueueItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  private Request request;
  private long timeRemaining;

  public CurrentQueueItem(Request request) {
    this.request = request;
    this.timeRemaining = request.getQuantity() * request.getProduct().getRecipe().getBuildTime();
  }

}
