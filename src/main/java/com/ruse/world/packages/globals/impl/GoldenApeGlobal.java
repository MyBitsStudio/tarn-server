package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class GoldenApeGlobal extends GlobalBoss {

    public GoldenApeGlobal() {
        super(9908, new Position(2143, 5016,0));
    }

    @Override
    public String message() {
        return "Whoever dares to challenge the Golden Great Ape, face him now at ::golden";
    }

    @Override
    public String dropMessage() {
        return "@yel@Congratulations, you have received a reward for killing Golden Ape!";
    }
}
