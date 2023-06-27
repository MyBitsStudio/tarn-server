package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class WarriorGlobal extends GlobalBoss {
    public WarriorGlobal() {
        super(125, new Position(2143, 5016,16));
    }

    @Override
    public String message() {
        return "@blu@The Warrior has returned to the battlefield! ::warrior";
    }

    @Override
    public String dropMessage() {
        return "@yel@Congratulations, you have received a reward for killing The Warrior!";
    }
}
