package com.ruse.world.packages.bosses;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.packages.bosses.multi.MultiBoss;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

public abstract class SpecialBossInstance extends Instance {

    private final Player owner;
    @Getter
    private final int npcId, cap;
    @Getter
    private final MultiBoss[] bosses;

    public SpecialBossInstance(Player p, int npcId,
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
                bosses[i] = new MultiBoss(npcId, pos[i].setZ(owner.getIndex() * 4), true, getOwner());
                bosses[i].setInstance(this);
            }
            bosses[i].setSpawnedFor(getOwner());
            add(bosses[i]);
        }
    }

    @Override
    public void signalSpawn(NPC n){
        if(started + cap <= System.currentTimeMillis()){
            getOwner().sendMessage("Your instance has expired.");
            return;
        }
        if(n.getId() == getNpcId()){
            MultiBoss boss = new MultiBoss(npcId, n.getPosition().setZ(owner.getIndex() * 4), true, owner);
            boss.setInstance(this);
            boss.setSpawnedFor(owner);
            add(boss);
        }
    }

}
