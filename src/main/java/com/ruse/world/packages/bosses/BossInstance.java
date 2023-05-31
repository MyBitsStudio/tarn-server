package com.ruse.world.packages.bosses;

import com.ruse.model.Locations;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.entity.impl.player.Player;

public abstract class BossInstance extends Instance {

    private final Boss boss;
    private final Player owner;

    public BossInstance(Player p, Locations.Location loc, Boss boss) {
        super(loc);
        this.boss = boss;
        this.owner = p;
    }

    public Boss getBoss() {
        return boss;
    }
    public Player getOwner() {
        return owner;
    }



}
