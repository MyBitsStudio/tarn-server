package com.ruse.world.packages.tower;

import com.ruse.model.GameObject;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.content.CustomObjects;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.packages.tower.npcs.TowerBoss;
import com.ruse.world.packages.tower.npcs.TowerMinions;
import com.ruse.world.packages.tower.props.Tower;
import org.jetbrains.annotations.NotNull;

public class TowerLevel extends Instance {

    private int amount;
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
        spawnObjects(player);
    }

    private void spawnObjects(@NotNull Player player){
        World.register(new GameObject(16686, new Position(3016, 2864, player.getIndex() * 4)));
    }

    private void spawnNPCs(@NotNull Tower tower, @NotNull Player player){
        amount = tower.getNpcIds().length;
        TowerBoss boss = new TowerBoss(tower.getNpcIds()[0], tower.getNpcPositions()[0].setZ(player.getIndex() * 4));
        boss.buff(tower.getBuffs());
        boss.setSpawnedFor(player);
        boss.setInstance(this);
        add(boss);
        if(tower.getNpcIds().length > 1){
            for(int i = 1; i < tower.getNpcIds().length; i++){
                TowerMinions minion = new TowerMinions(tower.getNpcIds()[i], tower.getNpcPositions()[i].setZ(player.getIndex() * 4));
                minion.buff(tower.getBuffs());
                minion.setSpawnedFor(player);
                minion.setInstance(this);
                add(minion);
            }
        }
    }

    public void check(Player player){
        amount--;
        if(amount <= 0){
            player.getTower().progress();
        }
    }

}
