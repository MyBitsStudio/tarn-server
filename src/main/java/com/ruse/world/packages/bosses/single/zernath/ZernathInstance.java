package com.ruse.world.packages.bosses.single.zernath;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.SingleBossSinglePlayerInstance;
import com.ruse.world.packages.bosses.single.agthomoth.AgthomothBoss;
import com.ruse.world.packages.bosses.single.sanctum.SanctumBoss;
import lombok.Getter;

@Getter
public class ZernathInstance extends SingleBossSinglePlayerInstance {

    private NPC[] minions = new NPC[3];
    public ZernathInstance(Player p, long time) {
        super(p, Locations.Location.SINGLE_INSTANCE, new ZernathBoss(p.getIndex() * 4),
                time);

    }

    public void spawn(){
        for(int i = 0; i < minions.length; i++){
            if(minions[i] != null)
                continue;
            minions[i] = new NPC(2340, new Position(2018, 4510, getOwner().getIndex() * 4));
            minions[i].setSpawnedFor(getOwner());
            add(minions[i]);
        }
    }

    @Override
    public Position getStartLocation() {
        return new Position(2014, 4518);
    }

    @Override
    public void startAnew() {
        removeNPC(getBoss());
        setBoss(null);
        setBoss(new ZernathBoss(getOwner().getIndex() * 4));
        minions = new NPC[3];
        getBoss().setSpawnedFor(getOwner());
        add(getBoss());
    }

    @Override
    public int cost(){
        return 10;
    }

    @Override
    public int itemId(){
        return 10835;
    }

    @Override
    public boolean canEnter(Player player){
        return player.getPSettings().getBooleanValue("instance-unlock");
    }

    @Override
    public String failedEntry(){return "You are not staff! Please stay tuned!";}
}
