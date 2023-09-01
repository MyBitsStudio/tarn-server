package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class GroundonGlobal extends GlobalBoss {

    public GroundonGlobal() {
        super(9904, new Position(2143, 5016,8));
    }

    @Override
    public String message() {
        return "The Ancient Groundon has just spawned, he can be found at ::groundon";
    }

    @Override
    public String dropMessage() {
        return "@yel@Congratulations, you have received a reward for killing Groundon!";
    }
}
