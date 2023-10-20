package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class NineTailsGlobal extends GlobalBoss {

    public NineTailsGlobal() {
        super(9904, new Position(2143, 5016,0));
    }

    @Override
    public String message() {
        return "The Fearless Nine Tails has just spawned, he can be found at ::ninetails";
    }

    @Override
    public String dropMessage() {
        return "@yel@Congratulations, you have received a reward for killing Nine Tails!";
    }

    @Override
    public String discordImage() {
        return "ninetails.png";
    }
}
