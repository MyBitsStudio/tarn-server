package com.ruse.world.packages.bosses.single.zeidan;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.SingleBossSinglePlayerInstance;
import com.ruse.world.packages.bosses.single.varthramoth.VarthBoss;
import org.jetbrains.annotations.NotNull;

public class ZeidanInstance extends SingleBossSinglePlayerInstance {
    public ZeidanInstance(Player p, long time) {
        super(p, Locations.Location.ZEIDAN, new ZeidanBoss(p.getIndex() * 4),
                time);

    }

    @Override
    public Position getStartLocation() {
        return new Position(2579, 4500);
    }

    @Override
    public void startAnew() {
        removeNPC(getBoss());
        setBoss(null);
        setBoss(new ZeidanBoss(getOwner().getIndex() * 4));
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
    public boolean canEnter(@NotNull Player player){
        return player.getRank().isStaff();
    }

    @Override
    public String failedEntry(){return "You are not staff! Please stay tuned!";}
}