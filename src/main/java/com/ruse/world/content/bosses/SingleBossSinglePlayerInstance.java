package com.ruse.world.content.bosses;

import com.ruse.GameSettings;
import com.ruse.model.GameObject;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.content.instances.Instance;
import com.ruse.world.entity.Entity;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

public abstract class SingleBossSinglePlayerInstance extends Instance {

    private final Boss boss;
    private final Player owner;

    public SingleBossSinglePlayerInstance(Player p, Locations.Location loc, Boss boss) {
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


    public void start(){

    }

    @Override
    public void destroy(){
        for(GameObject object : objects){
            World.deregister(object);
        }
    }

    @Override
    public void add(Entity en){
    }

    @Override
    public void remove(Entity en){
    }

}
