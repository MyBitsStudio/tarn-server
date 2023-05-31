package com.ruse.world.packages.bosses.crucio;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.packages.bosses.SingleBossSinglePlayerInstance;
import com.ruse.world.entity.impl.player.Player;

public class CrucioInstance extends SingleBossSinglePlayerInstance {
    public CrucioInstance(Player p) {
        super(p, Locations.Location.CRUCIO, new CrucioBoss(p.getIndex() * 4));
    }

    @Override
    public Position getStartLocation() {
        return new Position(3025, 5231);
    }

    @Override
    public int cost(){
        return 100000;
    }

    @Override
    public int itemId(){
        return 10835;
    }

    @Override
    public boolean canEnter(Player player){
        return player.getRights().OwnerDeveloperOnly();
    }

    @Override
    public String failedEntry(){return "You are not staff! Please stay tuned!";}
}
