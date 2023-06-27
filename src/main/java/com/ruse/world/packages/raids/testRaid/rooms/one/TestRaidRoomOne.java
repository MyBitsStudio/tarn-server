package com.ruse.world.packages.raids.testRaid.rooms.one;

import com.ruse.model.Position;
import com.ruse.world.packages.raids.Raid;
import com.ruse.world.packages.raids.RaidRoom;
import com.ruse.world.packages.raids.testRaid.rooms.one.boss.RoomOneBoss;
import com.ruse.world.packages.raids.testRaid.rooms.two.TestRaidRoomReward;
import com.ruse.world.instance.DestroyMode;

public class TestRaidRoomOne extends RaidRoom {
    public TestRaidRoomOne(Raid raid) {
        super(raid, DestroyMode.GROUP, 3358, 3269, 8, 8);

    }

    @Override
    public Position playerSpawn() {
        return new Position(2718, 2647);
    }

    @Override
    public Position deathPosition() {
        return new Position(2718, 2647);
    }

    @Override
    public void onRoomStartHook() {
        setRaidBoss(new RoomOneBoss(getRaid(), this));
        getRaid().getParty().getPlayers()
                .forEach(otherPlr -> {
                    otherPlr.sendMessage("Prepare for the raid!");
                });

        addNpc(getBoss());
    }

    @Override
    public boolean isFinalRoom(){ return true;}

    @Override
    public void onRoomFinishHook() {
        getRaid().getParty().getPlayers().forEach(p -> {
            p.sendMessage("Congratulations! You have completed the raid!");
        });

    }

    @Override
    public RaidRoom nextRoom() {
        return new TestRaidRoomReward(getRaid());
    }

    @Override
    public void createNpcs() {

    }
}
