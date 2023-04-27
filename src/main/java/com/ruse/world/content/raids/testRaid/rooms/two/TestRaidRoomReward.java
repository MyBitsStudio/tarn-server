package com.ruse.world.content.raids.testRaid.rooms.two;

import com.ruse.model.GameObject;
import com.ruse.model.Position;
import com.ruse.world.content.raids.Raid;
import com.ruse.world.content.raids.RaidBoss;
import com.ruse.world.content.raids.RaidRoom;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.instance.DestroyMode;

public class TestRaidRoomReward extends RaidRoom {
    public TestRaidRoomReward(Raid raid) {
        super(raid, DestroyMode.GROUP, 3358, 3269, 8, 8);
    }

    @Override
    public Position playerSpawn() {
        return new Position(2720, 2647);
    }

    @Override
    public Position deathPosition() {
        return new Position(2720, 2647);
    }

    @Override
    public void onRoomStartHook() {

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

    @Override
    public boolean handRoomExitObject(Player player, GameObject object, int option){
        return false;
    }
}
