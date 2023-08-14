package com.ruse.world.packages.bosses.single.agthomoth;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.SingleBossSinglePlayerInstance;

public class AgthomothInstance extends SingleBossSinglePlayerInstance {

    public AgthomothInstance(Player p) {
        super(p, Locations.Location.SINGLE_INSTANCE, new AgthomothBoss(p.getIndex() * 4));
    }

    @Override
    public Position getStartLocation() {
        return new Position(3025, 5231);
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
        return player.getRank().isDeveloper();
    }

    @Override
    public String failedEntry(){return "You are not staff! Please stay tuned!";}
}
