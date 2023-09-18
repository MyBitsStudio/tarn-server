package com.ruse.world.packages.raid.room;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.packages.instances.Instance;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public abstract class RaidRoom extends Instance {

    @Setter
    protected AtomicBoolean started = new AtomicBoolean(false),
    finished = new AtomicBoolean(false);

    public RaidRoom(Locations.Location location) {
        super(location);
    }

    public abstract Position startPosition();
    public abstract Position deathPosition();

    public abstract void onRoomStartHook();
    public abstract void onRoomEndHook();

    @Override
    public void process(){
        super.process();

        if(started.get()){

        }
    }


}
