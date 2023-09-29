package com.ruse.world.packages.bosses;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.packages.bosses.multi.MultiBoss;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

@Getter
public abstract class SingleBossSinglePlayerInstance extends Instance {

    private Boss boss;
    private final Player owner;
    private final long times;

    public SingleBossSinglePlayerInstance(Player p, Locations.Location loc, Boss boss, long time) {
        super(loc);
        this.boss = boss;
        this.owner = p;
        this.times = time;
    }

    public abstract Position getStartLocation();

    public void setBoss(Boss boss){
        this.boss = boss;
    }

    @Override
    public void start(){
        moveTo(getOwner(), getStartLocation());
        add(getOwner());

        getBoss().setSpawnedFor(getOwner());
        add(getBoss());

        getOwner().getPacketSender().sendMessage("@blu@[INSTANCE] Your instance has started.");
    }

    public abstract void startAnew();
    @Override
    public void signalSpawn(NPC n){
        if(started + times <= System.currentTimeMillis()){
            getOwner().sendMessage("Your instance has expired.");
            return;
        }
        startAnew();
    }


}
