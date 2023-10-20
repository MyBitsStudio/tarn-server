package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class KhazardGlobal extends GlobalBoss {
    public KhazardGlobal() {
        super(7553, new Position(2143, 5016,16));
    }

    @Override
    public String message() {
        return "@blu@General Khazard has risen from the graveyard! ::khazard";
    }

    @Override
    public String dropMessage() {
        return "@yel@Congratulations, you have received a reward for killing Khazard!";
    }

    @Override
    public String discordImage() {
        return "khazard.png";
    }
}
