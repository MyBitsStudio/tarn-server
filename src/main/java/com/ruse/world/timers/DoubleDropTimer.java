package com.ruse.world.timers;

import com.ruse.model.Timer;
import com.ruse.world.World;

public class DoubleDropTimer extends Timer {

    public DoubleDropTimer(long length) {
        super(length);
    }

    @Override
    public void onExecute() {
        World.attributes.setSetting("double-drops", true);
        World.sendNewsMessage("@red@[EVENT]@whi@ Double drops event is active!");
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onEnd() {
        World.attributes.setSetting("double-drops", false);
    }

    @Override
    public String getName() {
        return "DoubleDrops";
    }
}
