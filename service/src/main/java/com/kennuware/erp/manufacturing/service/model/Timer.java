package com.kennuware.erp.manufacturing.service.model;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {

    private long duration;
    private volatile boolean running = false;
    private static final long interval = 1000;
    private long elapsedTime;
    private Future<?> future = null;
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public Timer(long duration) {
        this.duration = duration;
        this.elapsedTime = 0;
    }

    public Timer(){
        this.duration = 0;
        this.elapsedTime = 0;
    }

    public void start() {
        if (running)
            return;

        running = true;
        future = executorService.scheduleWithFixedDelay(() -> {
            elapsedTime += interval;
            if (duration > 0 && elapsedTime >= duration){
                onFinish();
                future.cancel(false);
            }
        }, 0, interval, TimeUnit.MILLISECONDS);

    }

    public void pause() {
        if (!running)
            return;
        future.cancel(false);
        running = false;
    }


    public void onFinish(){
        future.cancel(false);
        elapsedTime = 0;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = (duration*60000);
    }

    public String getRemainingTime() {
        return "" + ((duration-elapsedTime)/60000);
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
