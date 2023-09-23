package com.ruse.world.packages.raid.room;

import com.ruse.model.GameObject;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.packages.raid.Raid;
import com.ruse.world.packages.raid.props.RaidBoss;
import com.ruse.world.packages.raid.props.RaidNPC;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public abstract class RaidRoom extends Instance {

    @Setter
    protected AtomicBoolean started = new AtomicBoolean(false),
    finished = new AtomicBoolean(false);

    @Setter
    protected Raid raid;

    @Getter@Setter
    protected RaidBoss boss;
    @Getter@Setter
    protected List<RaidNPC> npcs = new CopyOnWriteArrayList<>();

    public RaidRoom(Locations.Location location) {
        super(location);
    }

    public abstract Position startPosition();
    public abstract Position deathPosition();

    public abstract void onRoomStartHook();
    public abstract void onRoomEndHook();

    public boolean isFinal(){
        return false;
    }
    public boolean handleObjectClick(Player player, GameObject object, int option){ return false;}
    public boolean handRoomExitObject(Player player, GameObject object, int option){ return false;}

    @Override
    public void process(){
        super.process();

        if(started.get()){
            if (raid == null) {
                System.out.println("Raid is null");
                getFinished().set(true);
                destroy();
            } else {
                if (raid.getFinished().get()) {
                    System.out.println("Raid is finished");
                    getFinished().set(true);
                }

                if (raid.getCurrent() != this) {
                    System.out.println("Raid current room is not this room");
                    getFinished().set(true);
                    destroy();
                }

                if (raid.getParty() != null) {
                    raid.getParty().refreshInterface();
                }

                if(getBoss().isDying()){
                    System.out.println("Boss is dying");
                    getFinished().set(true);
                }
            }
        }
    }


}
