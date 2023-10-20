package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class CherubimonGlobal extends GlobalBoss {
    public CherubimonGlobal() {
        super(14378, new Position(3032, 5232,4));
    }

    @Override
    public String message() {
        return "@red@ The Godly Cherubimon has been awakened. Battle if you dare.. ::cherub";
    }

    @Override
    public String dropMessage() {
        return "@yel@You are a god! You have managed to make it through Cherubimon.";
    }

    @Override
    public String discordImage() {
        return "cherub.png";
    }
}
