package com.ruse.model;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public abstract class Timer {

    public static long SECONDS = 1000, MINUTES = SECONDS * 60, HOURS = MINUTES * 60, DAYS = HOURS * 24;

    private final long start = System.currentTimeMillis();
    private long length;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public Timer(long length){
        this.length = length;
    }

    public void start(){
        onExecute();
    }

    public boolean tick(){
        if(!running.get()){
            return false;
        }
        onTick();
        if(System.currentTimeMillis() - start >= length){
            stop();
            onEnd();
            return true;
        }
        return true;
    }

    public void setLength(long length) {
    	this.length = length;
    }

    public abstract void onExecute();
    public abstract void onTick();
    public abstract void onEnd();

    public abstract String getName();

    public void stop(){
        running.set(false);
    }

    public void set(){
        running.set(true);
    }

    public long returnLeft(){
        return length - (System.currentTimeMillis() - start);
    }
}
