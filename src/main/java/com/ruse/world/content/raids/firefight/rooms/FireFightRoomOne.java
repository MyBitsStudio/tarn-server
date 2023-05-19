package com.ruse.world.content.raids.firefight.rooms;

import com.ruse.model.Position;
import com.ruse.world.content.raids.Raid;
import com.ruse.world.content.raids.RaidRoom;
import com.ruse.world.content.raids.firefight.npc.one.FireFightRoomOneBoss;
import com.ruse.world.content.raids.firefight.npc.one.FireFightRoomOneMinion;
import com.ruse.world.content.raids.testRaid.rooms.one.boss.RoomOneBoss;
import com.ruse.world.instance.DestroyMode;
import lombok.NonNull;

public class FireFightRoomOne extends RaidRoom {

    private FireFightRoomOneMinion[] minions;

    private final Position[] minionPositions = {
            new Position(3057, 2874), new Position(2067, 2866),
            new Position(3057, 2855)
    };

    public FireFightRoomOne(Raid raid) {
        super(raid, DestroyMode.GROUP, 3057, 2865, 8, 8);
    }

    @Override
    public Position playerSpawn() {
        return new Position(3017, 2867);
    }

    @Override
    public Position deathPosition() {
        return new Position(3032, 2877);
    }

    @Override
    public void onRoomStartHook() {
        setRaidBoss(new FireFightRoomOneBoss(getRaid(), this));
        getRaid().getParty().getPlayers()
                .forEach(otherPlr -> {
                    otherPlr.sendMessage("Prepare for the raid!");
                });

        addNpc(getBoss());
//        minions = new FireFightRoomOneMinion[3];
//        for(int i = 0; i < minions.length; i++){
//            minions[i] = new FireFightRoomOneMinion(getRaid(), this, minionPositions[i]);
//        }
//
//        for(FireFightRoomOneMinion minion : minions){
//            addNpc(minion);
//        }
    }

    @Override
    public void onRoomFinishHook() {

    }

    @Override
    public RaidRoom nextRoom() {
        return null;
    }

    @Override
    public void createNpcs() {

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
}
