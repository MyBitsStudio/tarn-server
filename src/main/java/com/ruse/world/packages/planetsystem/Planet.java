package com.ruse.world.packages.planetsystem;

import com.ruse.world.packages.planetsystem.jobsite.AbstractJobSite;

public class Planet {

    public static final int MAX_WORKERS = 10;

    private final String name;

    private final AbstractJobSite jobSite;

    public Planet(String name, AbstractJobSite jobSite) {
        this.name = name;
        this.jobSite = jobSite;
    }

    public String getName() {
        return name;
    }

    public AbstractJobSite getJobSite() {
        return jobSite;
    }

    public void startJobSite() {
        jobSite.start();
    }
}
