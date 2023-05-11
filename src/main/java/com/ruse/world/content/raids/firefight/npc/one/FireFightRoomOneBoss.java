package com.ruse.world.content.raids.firefight.npc.one;

import com.ruse.model.Position;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.combat.strategy.impl.SimpleScript;
import com.ruse.world.content.raids.Raid;
import com.ruse.world.content.raids.RaidBoss;
import com.ruse.world.content.raids.RaidRoom;

public class FireFightRoomOneBoss extends RaidBoss {

    private final FireFightRoomOneMinion[] minions = new FireFightRoomOneMinion[3];
    private final Position[] minionPositions = {
            new Position(3057, 2874), new Position(2067, 2866),
            new Position(3057, 2855)
    };

    public FireFightRoomOneBoss(Raid raid, RaidRoom room) {
        super(raid, room, 1, new Position(3056, 2866), false);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new SimpleScript();
    }

    public FireFightRoomOneMinion[] getMinions(){
        return minions;
    }

    public int getAliveMinions(){
        int alive = 0;
        for(FireFightRoomOneMinion minion : minions){
            if(minion.isRegistered()){
                alive++;
            }
        }
        return alive;
    }


    @Override
    public void onSpawn(){
        for(int i = 0; i < minions.length; i++){
            minions[i] = new FireFightRoomOneMinion(getRaid(), getRoom(), minionPositions[i]);
        }

        for(FireFightRoomOneMinion minion : minions){
            getRoom().addNpc(minion);
        }
    }
}
