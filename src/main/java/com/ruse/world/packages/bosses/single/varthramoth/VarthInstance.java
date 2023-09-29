package com.ruse.world.packages.bosses.single.varthramoth;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.SingleBossSinglePlayerInstance;
import com.ruse.world.packages.bosses.single.zernath.ZernathBoss;

public class VarthInstance extends SingleBossSinglePlayerInstance {
    public VarthInstance(Player p, long time) {
        super(p, Locations.Location.SINGLE_INSTANCE, new VarthBoss(p.getIndex() * 4),
                time);

    }

    @Override
    public Position getStartLocation() {
        return new Position(2014, 4518);
    }

    @Override
    public void startAnew() {
        removeNPC(getBoss());
        setBoss(null);
        setBoss(new VarthBoss(getOwner().getIndex() * 4));
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
