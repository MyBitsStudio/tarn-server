package com.ruse.world.packages.bosses;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.entity.impl.player.Player;

public abstract class SingleBossSinglePlayerInstance extends Instance {

    private final Boss boss;
    private final Player owner;

    public SingleBossSinglePlayerInstance(Player p, Locations.Location loc, Boss boss) {
        super(loc);
        this.boss = boss;
        this.owner = p;
    }

    public abstract Position getStartLocation();

    public Boss getBoss() {
        return boss;
    }
    public Player getOwner() {
        return owner;
    }

    @Override
    public void start(){
        moveTo(getOwner(), getStartLocation());
        add(getOwner());

        getBoss().setSpawnedFor(getOwner());
        add(getBoss());

        getOwner().getPacketSender().sendMessage("@blu@[INSTANCE] Your instance has started.");
    }


}
