package com.ruse.world.content.bosses;

import com.ruse.model.RegionInstance;
import com.ruse.world.entity.impl.player.Player;

public class BossInstance extends RegionInstance {

    private final Boss boss;
    public BossInstance(Player p, RegionInstanceType type, Boss boss) {
        super(p, type);
        this.boss = boss;
    }





}
