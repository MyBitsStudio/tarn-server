package com.ruse.world.packages.tower;

import com.ruse.model.Locations;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.packages.tower.npcs.TowerBoss;
import com.ruse.world.packages.tower.npcs.TowerMinions;
import com.ruse.world.packages.tower.props.Tower;
import org.jetbrains.annotations.NotNull;

public class TowerLevel extends Instance {
    public TowerLevel(Locations.Location location) {
        super(location);
    }


    public void enter(@NotNull Player player){
        TowerProgress progress = player.getTower();
        if(progress == null)
            return;

        int tier = progress.getTier(), level = progress.getLevel();
        Tower tower = Tower.get(tier, level);
        if(tower == null)
            return;

        moveTo(player, tower.getPosition());
        add(player);
        player.sendMessage("You have entered the tower.");
        spawnNPCs(tower, player);
    }

    private void spawnNPCs(@NotNull Tower tower, @NotNull Player player){
        TowerBoss boss = new TowerBoss(tower.getNpcIds()[0], tower.getNpcPositions()[0].setZ(player.getIndex() * 4));
        boss.buff(tower.getBuffs());
        boss.setSpawnedFor(player);
        add(boss);
        if(tower.getNpcIds().length > 1){
            for(int i = 1; i < tower.getNpcIds().length; i++){
                TowerMinions minion = new TowerMinions(tower.getNpcIds()[i], tower.getNpcPositions()[i].setZ(player.getIndex() * 4));
                minion.buff(tower.getBuffs());
                minion.setSpawnedFor(player);
                add(minion);
            }
        }
    }

}
