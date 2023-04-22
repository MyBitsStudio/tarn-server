package com.ruse.util;

public class Condition {

    private Thread run;

    private Runnable condition;
    private boolean predicate;

    public Condition(Thread run, Runnable condition, boolean predicate) {
        this.run = run;
        this.condition = condition;
        this.predicate = predicate;
    }

    public void run(){
        this.run.start();
        while(this.run.isAlive()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if(this.predicate){
          //  System.out.println("Condition ran");
            this.condition.run();
        }
    }

    public void preRun(){
        if(this.predicate) {
            this.condition.run();
        } else {
            this.run.start();
        }
    }
}
