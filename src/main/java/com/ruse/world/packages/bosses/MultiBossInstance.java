package com.ruse.world.packages.bosses;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.packages.bosses.multi.MultiBoss;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultiBossInstance extends Instance {

    private final Player owner;
    @Getter
    private final int npcId;
    @Getter
    private final long times;
    @Getter
    private AtomicInteger spawned = new AtomicInteger(0);
    @Getter
    private final MultiBoss[] bosses;

    public MultiBossInstance(Player p, int npcId,
                             int amount, long time) {
        super(Locations.Location.NORMAL_INSTANCE);
        this.owner = p;
        this.npcId = npcId;
        this.bosses = new MultiBoss[amount];
        this.times = time;
    }

    @Override
    public void process(){
        super.process();
        long time = System.currentTimeMillis() - started;
        long timea = times - time;
        String timeStamp = ""+ timea / (1000 * 60);
        playerList.stream()
              .filter(Objects::nonNull)
              .forEach(p -> {
                     p.getPacketSender().sendWalkableInterface(63000, true);
                     p.getPacketSender().sendString(63004, timeStamp+"M");
              });
    }

    public Player getOwner() {
        return owner;
    }

    public void spawnAll(Position[] pos){
        if(pos == null || pos.length != bosses.length){
            throw new IllegalArgumentException("Positions must be the same length as the bosses array.");
        }
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
        if(System.currentTimeMillis() >= (started + times)){
            getOwner().sendMessage("Your instance has expired.");
            return;
        }
        if(n.getId() == getNpcId() && n.getInstanceId().equals(getOwner().getInstanceId())){
            MultiBoss boss = new MultiBoss(npcId, n.getPosition().setZ(getOwner().getIndex() * 4), true, getOwner());
            boss.setSpawnedFor(getOwner());
            add(boss);
        }
    }
}
