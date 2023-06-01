package com.ruse.world.packages.globals.impl;

import com.ruse.model.Position;
import com.ruse.world.packages.globals.GlobalBoss;

public class VeigarGlobal extends GlobalBoss {

    public VeigarGlobal() {
        super(9906, new Position(2143, 5016,8));
    }

    @Override
    public String message() {
        return "The Final Boss Veigar has spawned at ::veigar";
    }

    @Override
    public String dropMessage() {
        return "@yel@Congratulations, you have received a reward for killing Veigar!";
    }
}
