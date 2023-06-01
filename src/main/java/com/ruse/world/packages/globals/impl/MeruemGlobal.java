package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class MeruemGlobal extends GlobalBoss {

    public MeruemGlobal() {
        super(9907, new Position(2143, 5016,4));
    }

    @Override
    public String message() {
        return "Meruem The King has spawned, fight him at ::meruem";
    }

    @Override
    public String dropMessage() {
        return "@yel@Congratulations, you have received a reward for killing Meruem!";
    }
}
