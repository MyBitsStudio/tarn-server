package com.ruse.world.content.bosses;

import com.ruse.GameSettings;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.model.RegionInstance;
import com.ruse.world.content.instances.Instance;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.List;

public abstract class BossInstance extends Instance {

    private final Boss boss;
    private final Player owner;

    public BossInstance(Player p, Locations.Location loc, Boss boss) {
        super(loc);
        this.boss = boss;
        this.owner = p;
    }

    public Boss getBoss() {
        return boss;
    }
    public Player getOwner() {
        return owner;
    }


    public void start(){

    }

    public void dispose(){
        if (getOwner().getInstance() != null)
            getOwner().getInstance().destroy();

        getOwner().setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
        cancelCurrentActions(getOwner());
        getOwner().moveTo(GameSettings.DEFAULT_POSITION.copy()).setPosition(GameSettings.DEFAULT_POSITION.copy());
        getOwner().getMovementQueue().setLockMovement(false).reset();

        getOwner().getPacketSender().sendInterfaceRemoval();
    }

    public void moveTo(Position pos){
        getOwner().setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
        cancelCurrentActions(getOwner());
        getOwner().setLocation(location);
        getOwner().moveTo(pos).setPosition(pos);
        getOwner().getMovementQueue().setLockMovement(false).reset();

        getOwner().getPacketSender().sendInterfaceRemoval();
    }

    public static void cancelCurrentActions(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        player.setTeleporting(false);
        player.setWalkToTask(null);
        player.setInputHandling(null);
        player.getSkillManager().stopSkilling();
        player.setEntityInteraction(null);
        player.getMovementQueue().setFollowCharacter(null);
        player.getCombatBuilder().cooldown(false);
        player.setResting(false);
    }

}
