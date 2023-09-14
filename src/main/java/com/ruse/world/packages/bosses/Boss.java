package com.ruse.world.packages.bosses;

import com.ruse.model.Position;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;

public abstract class Boss extends NPC {

    public Boss(int id, Position position, boolean respawns) {
        super(id, position, respawns);
        onSpawn();
    }

    public void scale(){}
    public void onSpawn(){}

    @Override
    public void onDeath(Player player){
        AchievementHandler.progress(player, 1, 26);
        AchievementHandler.progress(player, 1, 27);
        AchievementHandler.progress(player, 1, 43);
        AchievementHandler.progress(player, 1, 44);
        AchievementHandler.progress(player, 1, 67);
        AchievementHandler.progress(player, 1, 68);
        AchievementHandler.progress(player, 1, 88);
        AchievementHandler.progress(player, 1, 89);
    }
}
