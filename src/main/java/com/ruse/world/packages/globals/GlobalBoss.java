package com.ruse.world.packages.globals;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.model.definitions.NPCDrops;
import com.ruse.world.content.achievement.Achievements;
import com.ruse.world.content.dailytasks_new.DailyTask;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

public abstract class GlobalBoss extends NPC {
    public GlobalBoss(int id, Position position) {
        super(id, position, false);
        this.getDefinition().setAggressive(true);
        this.getDefinition().setMulti(true);
        this.getDefinition().setRespawnTime(-1);
        setLocation(Locations.Location.getLocation(this));
    }

    public abstract String message();
    public abstract String dropMessage();

    public void handleDrop(){
        for(Player player : getClosePlayers(25)){
            if(player == null || !player.isRegistered())
                continue;
            player.getPacketSender().sendMessage(dropMessage());
            Achievements.doProgress(player, Achievements.Achievement.KILL_45_GLOBAL_BOSSES);
            DailyTask.GLOBAL_BOSSES.tryProgress(player);

            NPCDrops.handleDrops(player, this);
        }
    }

    @Override
    public void onDeath(){
        handleDrop();
    }
}
