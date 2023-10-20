package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class DonationGlobal extends GlobalBoss {

    public DonationGlobal() {
        super(587, new Position(2529, 2633,4));
    }

    @Override
    public String message() {
        return "The Donation Boss has just spawned, he can be found at ::donboss";
    }

    @Override
    public String dropMessage() {
        return "@yel@Congratulations, you have received a reward for killing the Donation Boss!";
    }

    @Override
    public String discordImage() {
        return "donate.png";
    }
}
