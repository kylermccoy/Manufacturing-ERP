package com.kennuware.erp.manufacturing.application.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kennuware.erp.manufacturing.application.controller.ProcessController;
import com.kennuware.erp.manufacturing.application.model.CurrentQueueItem;
import com.kennuware.erp.manufacturing.application.model.Queue;
import com.kennuware.erp.manufacturing.application.model.Request;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Timer;
import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class QueueManager {

  private CurrentQueueItem current = null;
  private final Timer timer;
  private StatusTimerTask task;

  QueueManager() {
    this.timer = new Timer();
  }

  public synchronized void addNextRequest(Request request, HttpSession session) {
    // api probably returned nothing - queue empty
    if (request == null) {
      return;
    }
    if (current != null) {
      if (!isRunning()) { // resuming
        startTimerForTask(session);
      } else { // something already running
        return;
      }
    }
    current = new CurrentQueueItem(request);
  }

  public CurrentQueueItem getCurrent() {
    return current;
  }

  public boolean isRunning() {
    return task != null && task.isRunning();
  }

  public synchronized void startTimerForTask(HttpSession session) {
    Queue queue = RequestSender.getForObject("http://localhost:8080/manufacturing/api/queue/", Queue.class, session).getBody();
    if (queue == null || !queue.isRunning()) {
      // queue not running, don't start
      return;
    }
    task = new StatusTimerTask() {
      @Override
      void doWork() {
        completeTask(session);
      }
    };
    timer.schedule(task, Date.from(current.getFinishTime().toInstant(ZoneOffset.UTC)));
  }

  public synchronized void stopCurrent() {
    if(current == null) {
      return;
    }
    timer.cancel();
    current = null;
    //todo: get remaining time and replace current
  }

  public long getRemainingTimeMinutes() {
    if (current == null) {
      // no task running
      return -1;
    }
    return LocalDateTime.now().until(current.getFinishTime(), ChronoUnit.MINUTES);
  }

  private void completeTask(HttpSession session) {
    Request req = current.getRequest();
    req.setCompleted(true);
    // update the object in the db
    ResponseEntity<ObjectNode> resp = RequestSender.postForObject("http://localhost:8080/manufacturing/api/requests", req,
        ObjectNode.class, session);
    addNextRequest(ProcessController.getNextRequest(session), session);
    //todo: notify others?
  }

}
