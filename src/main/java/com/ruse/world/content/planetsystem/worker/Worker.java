package com.ruse.world.content.planetsystem.worker;

public class Worker {

    private final WorkerType workerType;
    private long ticksLeft;
    private int level;
    private int hungerLevel;

    public Worker(WorkerType workerType) {
        this.workerType = workerType;
        this.hungerLevel = workerType.getMaxHungerLevel();
        this.level = 1;
    }

    public WorkerType getWorkerType() {
        return workerType;
    }

    public int getHungerLevel() {
        return hungerLevel;
    }

    public int incrementHungerLevel() {
        return hungerLevel == 0 ? 0 : hungerLevel--;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setHungerLevel(int hungerLevel) {
        this.hungerLevel = hungerLevel;
    }

    public long getTicksLeft() {
        return ticksLeft;
    }

    public void decrementTicksLeft() {
        ticksLeft--;
    }
}
