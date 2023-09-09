package com.ruse.world.packages.bosses.single.agthomoth;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.SingleBossSinglePlayerInstance;

public class AgthomothInstance extends SingleBossSinglePlayerInstance {

    public AgthomothInstance(Player p, long time) {
        super(p, Locations.Location.SINGLE_INSTANCE, new AgthomothBoss(p.getIndex() * 4),
                time);
    }

    @Override
    public Position getStartLocation() {
        return new Position(3025, 5231);
    }

    @Override
    public void startAnew() {
        removeNPC(getBoss());
        setBoss(null);
        setBoss(new AgthomothBoss(getOwner().getIndex() * 4));
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
