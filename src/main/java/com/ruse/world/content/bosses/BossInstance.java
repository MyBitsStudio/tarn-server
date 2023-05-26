package com.ruse.world.content.bosses;

import com.ruse.GameSettings;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.model.RegionInstance;
import com.ruse.world.content.instances.Instance;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.List;

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
