package com.ruse.world.content.bosses;

import com.ruse.GameSettings;
import com.ruse.model.RegionInstance;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BossInstance extends RegionInstance {

    private final Boss boss;
    private final Player owner;

    private final List<NPC> npcs = new ArrayList<>();
    public BossInstance(Player p, RegionInstanceType type, Boss boss) {
        super(p, type);
        this.boss = boss;
        this.owner = p;
        setOwner(p);
    }

    public Boss getBoss() {
        return boss;
    }
    public Player getOwner() {
        return owner;
    }

    public void process(){

    }

    public void start(){
        add(getOwner());
    }

    public void dispose(){
        if (getOwner().getRegionInstance() != null)
            getOwner().getRegionInstance().destruct();

        getOwner().setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
        cancelCurrentActions(getOwner());
        getOwner().moveTo(GameSettings.HOME_CORDS).setPosition(GameSettings.HOME_CORDS);
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
