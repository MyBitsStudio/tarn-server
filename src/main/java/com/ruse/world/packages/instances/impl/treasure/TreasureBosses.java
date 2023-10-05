package com.ruse.world.packages.instances.impl.treasure;

import com.ruse.model.Position;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import org.jetbrains.annotations.NotNull;

public class TreasureBosses extends NPC {

    public TreasureBosses(int id, Position position) {
        super(id, position, false);
        onSpawn();
    }

    public void scale(){

    }
    public void onSpawn(){

    }

    @Override
    public void onDeath(@NotNull Player player){
        if(player.getInstance() == null){
            return;
        }
        if(player.getInstance() instanceof DailyTreasure treasure){
            treasure.signalDeath();
        }
    }
}
