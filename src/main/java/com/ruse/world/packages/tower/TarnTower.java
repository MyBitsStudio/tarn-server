package com.ruse.world.packages.tower;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.packages.tower.props.TowerLocations;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class TarnTower {

    public static List<Instance> instances = new CopyOnWriteArrayList<>();

    public static void remove(Player player){
        instances.removeIf(instance -> instance.getPlayers().contains(player));
    }
    public static void startTower(@NotNull Player player) {
        if(player.getInstance() != null) {
            player.getInstance().clear();
            player.setInstance(null);
            return;
        }
        if(!Objects.equals(player.getInstanceId(), "")){
            remove(player);
            player.setInstanceId("");
            return;
        }

        TowerProgress progress = new TowerProgress(player);

        TowerLevel level = new TowerLevel(Objects.requireNonNull(TowerLocations.get(progress.getTier())).getLocation());

        instances.add(level);
        level.enter(player);
    }

    public static boolean canEnter(Player player){

        return true;
    }
}
