package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class VoteGlobal extends GlobalBoss {

    public VoteGlobal() {
        super(8013, new Position(2529, 2633,0));
    }

    @Override
    public String message() {
        return "The Vote Boss has just spawned, he can be found at ::vboss";
    }

    @Override
    public String dropMessage() {
        return "@yel@Congratulations, you have received a reward for killing the Vote Boss!";
    }

    @Override
    public String discordImage() {
        return "vote-boss.png";
    }

}
