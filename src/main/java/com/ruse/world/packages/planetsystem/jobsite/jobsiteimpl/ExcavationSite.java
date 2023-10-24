package com.ruse.world.packages.planetsystem.jobsite.jobsiteimpl;

import com.ruse.engine.task.Task;
import com.ruse.model.Item;
import com.ruse.world.packages.planetsystem.jobsite.AbstractJobSite;

public class ExcavationSite extends AbstractJobSite {

    public ExcavationSite(String name, int resourceRecoveryInterval, int workerLength, Item... resources) {
        super(name, resourceRecoveryInterval, workerLength, resources);
    }

    @Override
    public Task sequence() {
        return super.sequence();
    }
}
