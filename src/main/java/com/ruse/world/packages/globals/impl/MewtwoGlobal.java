package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class MewtwoGlobal extends GlobalBoss {
    public MewtwoGlobal() {
        super(9005, new Position(2143, 5016,12));
    }

    @Override
    public String message() {
        return "@blu@The ancients have created a portal to Mewtwo! ::mewtwo";
    }

    @Override
    public String dropMessage() {
        return "@yel@Congratulations, you have received a reward for killing Mewtwo!";
    }

    @Override
    public String discordImage() {
        return "mewtwo.png";
    }
}
