package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class VeigarGlobal extends GlobalBoss {
    public VeigarGlobal() {
        super(9906, new Position(3032, 5232,0));
    }

    @Override
    public String message() {
        return "@red@ The Godly Veigar has been awakened. Battle if you dare.. ::veigar";
    }

    @Override
    public String dropMessage() {
        return "@yel@You are a god! You have managed to make it through Veigar.";
    }

    @Override
    public String discordImage() {
        return "veigar.png";
    }
}
