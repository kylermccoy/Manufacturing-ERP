package com.kennuware.erp.manufacturing.application.util;

import java.util.TimerTask;
import lombok.Getter;

@Getter
public abstract class StatusTimerTask extends TimerTask {

  private boolean isRunning = false;

  abstract void doWork();

  public boolean isRunning() {
    return isRunning;
  }

  @Override
  public void run() {
    this.isRunning = true;
    doWork();
  }
}
