package com.ruse.world.packages.globals;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.content.dailytasks_new.DailyTask;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.drops.DropManager;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import org.jetbrains.annotations.NotNull;

public abstract class GlobalBoss extends NPC {

    public GlobalBoss(int id, Position position) {
        super(id, position, false);
        this.getDefinition().setAggressive(true);
        this.getDefinition().setMulti(true);
        this.getDefinition().setRespawn(-1);
        setLocation(Locations.Location.getLocation(this));
    }

    public abstract String message();
    public abstract String dropMessage();

    public void handleDrop(){
        for(Player player : World.getPlayers()){
            if(player == null || !player.isRegistered())
                continue;
            if(player.getBossPlugin() == null)
                continue;
            if(player.getBossPlugin().getDamage(this.getDefinition().getName()) < 1000)
                continue;
            player.getPacketSender().sendMessage(dropMessage());

            AchievementHandler.progress(player, 1, 14);
            AchievementHandler.progress(player, 1, 24);
            AchievementHandler.progress(player, 1, 41);
            AchievementHandler.progress(player, 1, 42);
            AchievementHandler.progress(player, 1, 65);
            AchievementHandler.progress(player, 1, 66);
            AchievementHandler.progress(player, 1, 86);
            AchievementHandler.progress(player, 1, 87);

            player.getBossPlugin().setDamage(this.getDefinition().getName(), 0L);
            DropManager.getManager().sendDrop(this, player, 1.0);
        }
    }

    @Override
    public void onDeath(Player player){
        handleDrop();
    }

    @Override
    public void onDamage(@NotNull Player player, long damage){
        player.getBossPlugin().addDamage(this.getDefinition().getName(), damage);
    }
}
