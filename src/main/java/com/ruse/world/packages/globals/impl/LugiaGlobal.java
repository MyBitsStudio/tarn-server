package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class LugiaGlobal extends GlobalBoss {

    public LugiaGlobal() {
        super(3308, new Position(2143, 5016,4));
    }

    @Override
    public String message() {
        return "Lugia has returned! ::lugia";
    }

    @Override
    public String dropMessage() {
        return "@yel@Congratulations, you have received a reward for killing Lugia!";
    }

    @Override
    public String discordImage() {
        return "lugia.png";
    }
}
