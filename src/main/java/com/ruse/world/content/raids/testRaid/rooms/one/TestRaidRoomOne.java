package com.ruse.world.content.raids.testRaid.rooms.one;

import com.ruse.model.Position;
import com.ruse.world.content.raids.Raid;
import com.ruse.world.content.raids.RaidBoss;
import com.ruse.world.content.raids.RaidRoom;
import com.ruse.world.instance.DestroyMode;
import lombok.NonNull;

public class TestRaidRoomOne extends RaidRoom {
    public TestRaidRoomOne(Raid raid, RaidBoss boss, @NonNull DestroyMode mode, int chunkX, int chunkY, int width, int height) {
        super(raid, boss, mode, chunkX, chunkY, width, height);
    }

    @Override
    public Position playerSpawn() {
        return null;
    }

    @Override
    public Position deathPosition() {
        return null;
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
}
