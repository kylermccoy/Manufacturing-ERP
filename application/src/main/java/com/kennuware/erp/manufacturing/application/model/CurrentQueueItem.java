package com.kennuware.erp.manufacturing.application.model;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CurrentQueueItem {

  private Request request;
  private LocalDateTime startTime;
  private LocalDateTime finishTime;

  public CurrentQueueItem(Request request) {
    this.request = request;
    this.startTime = LocalDateTime.now();
    calculateFinishTime();
  }

  private void calculateFinishTime() {
    Product prod = this.request.getProduct();
    int timeToBuild = prod.getRecipe().getBuildTime();
    long quantity = this.request.getQuantity();

    this.finishTime = startTime.plusMinutes(quantity * timeToBuild);
  }


}
