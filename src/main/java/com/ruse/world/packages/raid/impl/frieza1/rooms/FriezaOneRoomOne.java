package com.ruse.world.packages.raid.impl.frieza1.rooms;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.packages.raid.room.RaidRoom;

public class FriezaOneRoomOne extends RaidRoom {

    public FriezaOneRoomOne(Locations.Location location) {
        super(location);
    }

    @Override
    public Position startPosition() {
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
    public void onRoomEndHook() {

    }
}
