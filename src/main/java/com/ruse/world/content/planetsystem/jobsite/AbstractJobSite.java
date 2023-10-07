package com.ruse.world.content.planetsystem.jobsite;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Item;
import com.ruse.util.Misc;
import com.ruse.world.content.planetsystem.Planet;
import com.ruse.world.content.planetsystem.worker.Worker;
import com.ruse.world.content.planetsystem.worker.WorkerType;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJobSite implements IJobSite {

    private final String name;

    private final List<Item> gatheredResources = new ArrayList<>();

    private final Item[] availableResources;

    protected final SecureRandom random = new SecureRandom();

    private Task jobTask;

    protected final List<Worker> workers = new ArrayList<>();
    
    protected int resourceDepletedLevel = 100;

    protected int ticks = 0;

    protected final int resourceRecoveryInterval;

    /**
     * The base amount of time a worker spends working on this job site
     */
    protected final int workLength;

    protected AbstractJobSite(String name, int resourceRecoveryInterval, int workLength, Item... availableResources) {
        this.name = name;
        this.resourceRecoveryInterval = resourceRecoveryInterval;
        this.workLength = workLength;
        this.availableResources = availableResources;
        for(int i = 0; i < Planet.MAX_WORKERS; i++) {
            workers.add(i, null);
        }
    }

    @Override
    public Task sequence() {
        return new Task() {
            @Override
            protected void execute() {
                for (Worker worker : workers) {
                    WorkerType type = worker.getWorkerType();
                    if (worker.getHungerLevel() == 0 || worker.getTicksLeft() == 0) {
                        continue;
                    }
                    for (int k = 0; k < type.getBaseSpeed(); k++) {
                        if (Misc.random(resourceDepletedLevel) == 0) {
                            Item resource = getRandomResource();
                            resourceDepletedLevel++;
                            if (Misc.random(type.getAppetite()) == 0) {
                                worker.incrementHungerLevel();
                            }
                            addResource(resource);
                            worker.decrementTicksLeft();
                        }
                    }
                    if (ticks == resourceRecoveryInterval) {
                        addMoreResources();
                    }
                }
            }
        };
    }

    public void start() {
        jobTask = sequence();
        TaskManager.submit(jobTask);
    }

    public Item getRandomResource() {
        return availableResources[random.nextInt(availableResources.length)];
    }

    public void stopJob() {
        jobTask.stop();
    }

    public String getName() {
        return name;
    }

    public long getTicks() {
        return ticks;
    }

    public List<Item> getGatheredResources() {
        return gatheredResources;
    }

    protected void addResource(Item item) {
        gatheredResources.add(item);
    }

    protected void addMoreResources() {
        if(resourceDepletedLevel > 100)
            resourceDepletedLevel--;
    }

    public Item[] getAvailableResources() {
        return availableResources;
    }

    public SecureRandom getRandom() {
        return random;
    }

    public Task getJobTask() {
        return jobTask;
    }

    public void setJobTask(Task jobTask) {
        this.jobTask = jobTask;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public int getResourceDepletedLevel() {
        return resourceDepletedLevel;
    }

    public void setResourceDepletedLevel(int resourceDepletedLevel) {
        this.resourceDepletedLevel = resourceDepletedLevel;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public int getResourceRecoveryInterval() {
        return resourceRecoveryInterval;
    }

    public int getWorkLength() {
        return workLength;
    }
}
