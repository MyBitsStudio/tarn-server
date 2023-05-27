package com.ruse.world.content.bosses;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.content.bosses.multi.MultiBoss;
import com.ruse.world.content.instances.Instance;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultiBossInstance extends Instance {

    private final Player owner;
    @Getter
    private final int npcId;
    @Getter
    private final int cap;
    @Getter
    private AtomicInteger spawned = new AtomicInteger(0);
    @Getter
    private final MultiBoss[] bosses;

    public MultiBossInstance(Player p, int npcId,
                             int amount, int cap) {
        super(Locations.Location.NORMAL_INSTANCE);
        this.owner = p;
        this.npcId = npcId;
        this.bosses = new MultiBoss[amount];
        this.cap = cap;
    }

    public Player getOwner() {
        return owner;
    }

    public void spawnAll(Position[] pos){
        for(int i = 0; i < bosses.length; i++){
            if(bosses[i] == null){
                bosses[i] = new MultiBoss(npcId, pos[i].setZ(getOwner().getIndex() * 4), true, getOwner());
                bosses[i].setInstance(this);
            }
            bosses[i].setSpawnedFor(getOwner());
            add(bosses[i]);
        }
    }

    @Override
    public void signalSpawn(NPC n){
        if(spawned.getAndIncrement() >= cap)
            return;
        if(n.getId() == getNpcId()){
            MultiBoss boss = new MultiBoss(npcId, n.getPosition().setZ(getOwner().getIndex() * 4), true, getOwner());
            boss.setSpawnedFor(getOwner());
            add(boss);
        }
    }
}
