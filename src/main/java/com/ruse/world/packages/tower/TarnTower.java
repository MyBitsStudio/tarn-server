package com.ruse.world.packages.tower;

import com.ruse.model.container.ItemContainer;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.packages.instances.InstanceManager;
import com.ruse.world.packages.tower.props.Tower;
import com.ruse.world.packages.tower.props.TowerLocations;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TarnTower {

    private static final Map<String, Instance> instances = new ConcurrentHashMap<>();
    public static void startTower(@NotNull Player player) {
        InstanceManager.getManager().enterTarnTower(player);
    }

    public static boolean canEnter(Player player){

        return true;
    }

    public static void leave(@NotNull Player player){
        if(player.getInstance() != null) {
            player.getInstance().destroy();
            player.setInstance(null);
            player.setInstanceId("");
            player.getTower().setInstance(null);
            instances.remove(player.getInstanceId());
        }
    }

    public static void sendInterface(Player player){
        if(!World.attributes.getSetting("tower")){
            player.sendMessage("The tower is currently disabled.");
            return;
        }
        reset(player);


        player.getPacketSender().sendInterface(81000);

        player.getPacketSender().sendString(81007, "Tier : "+player.getTower().getTier());
        player.getPacketSender().sendString(81008, "Level : "+player.getTower().getLevel());

        int next = player.getTower().getLevel();
        int tier = player.getTower().getTier();

        if(next >= 20){
            tier++;
            next = 0;
        }

        if(next < 10)
            player.getPacketSender().sendNpcIdToDisplayPacket(Objects.requireNonNull(Tower.get(tier, next)).getNpcIds()[0], 81010);

        int start = 81031;
        ItemContainer container = player.getTower().getRewards();
        for(int i = start; i < start + container.capacity(); i++){
            if(container.get(i - start) != null)
                player.getPacketSender().sendItemOnInterface(i, container.get(i - start).getId(), container.get(i - start).getAmount());
        }

    }

    private static void reset(Player player){
        AtomicInteger start = new AtomicInteger(81031);
        for(int i = start.get(); i < start.get() + 28; i++){
            player.getPacketSender().sendItemOnInterface(i, -1, 0);
        }
    }

    public static boolean handleButton(Player player, int button){
        switch(button){
            case 81013 -> {
                startTower(player);
                return true;
            }
            case 81015 -> {
                player.getTower().collectRewards();
                return true;
            }
        }
        return false;
    }
}
