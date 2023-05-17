package com.ruse.world.content.bosses;

import com.ruse.GameSettings;
import com.ruse.model.RegionInstance;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BossInstance extends RegionInstance {

    private final Boss boss;
    private final Player owner;

    private final List<NPC> npcs = new ArrayList<>();
    public BossInstance(Player p, RegionInstanceType type, Boss boss) {
        super(p, type);
        this.boss = boss;
        this.owner = p;
        setOwner(p);
    }

    public Boss getBoss() {
        return boss;
    }
    public Player getOwner() {
        return owner;
    }

    public void process(){

    }

    public void start(){
        add(getOwner());
    }

    public void dispose(){
        if (getOwner().getRegionInstance() != null)
            getOwner().getRegionInstance().destruct();

        TeleportHandler.teleportPlayer(getOwner(), GameSettings.DEFAULT_POSITION.copy(),
                getOwner().getSpellbook().getTeleportType());
        getOwner().getPacketSender().sendInterfaceRemoval();
    }

}
