package com.ruse.world.content.kingdomsystem;

public class Worker {

    private final WorkerType workerType;
    private int hungerLevel;

    public Worker(WorkerType workerType) {
        this.workerType = workerType;
        this.hungerLevel = workerType.getMaxHungerLevel();
    }

    public WorkerType getWorkerType() {
        return workerType;
    }

    public int getHungerLevel() {
        return hungerLevel;
    }
}
