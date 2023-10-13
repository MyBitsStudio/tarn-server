package com.ruse.world.entity.impl.player;

import com.ruse.model.Item;
import com.ruse.world.World;
import com.ruse.world.WorldCalendar;
import com.ruse.world.packages.instances.InstanceManager;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PlayerDaily {

    private final Map<String, Integer> claimed = new ConcurrentHashMap<>();

    public void load(Map<String, Integer> claimed){
        this.claimed.putAll(claimed);
    }

    public boolean claim(String content){
        if(claimed.containsKey(content))
            if(LocalDate.now(ZoneOffset.UTC).getDayOfMonth() != claimed.get(content)){
                claimed.replace(content, LocalDate.now(ZoneOffset.UTC).getDayOfMonth());
                return true;
            } else {
                return false;
            }
        else {
            claimed.put(content, LocalDate.now(ZoneOffset.UTC).getDayOfMonth());
            return true;
        }
    }

    public void onLogin(Player player){
        if(claim("coin-ticket")){
            player.getInventory().add(new Item(23165, 1));
            player.sendMessage(("@red@[DAILY]@whi@ You have claimed your daily Coin Ticket!"));
        }

        if(player.getVip().getRank() > 0){
            if(claim("vip-tickets")){
                int tickets = player.getVip().getRank();
                player.getInventory().addDropIfFull(23003, tickets);
                player.getPacketSender().sendMessage("@red@[DAILY]@whi@You have gained " + tickets + " VIP tickets.");
            }
            if(player.getVip().getRank() >= 5){
                if(claim("daily-crystal")){
                    player.getItems().addCharge("crystal-monic", 1);
                    player.getPacketSender().sendMessage("@red@[DAILY]@whi@You have gained your daily Crystal Monic charge.");
                }
            }
        }

        if(claim("daily-monic")){
            player.getItems().addCharge("ancient-monic", 1);
            player.getPacketSender().sendMessage("@red@[DAILY]@whi@You have gained your daily Ancient Monic charge.");
        }

    }

    public void enterDonatorMaterialZone(@NotNull Player player){
        if(player.getVip().getRank() >= 5){
            if(player.getInstance() != null) {
                player.getInstance().destroy();
                player.setInstance(null);
                player.sendMessage("You have left your previous instance.");
                return;
            }

            if(!Objects.equals(player.getInstanceId(), "")){
                InstanceManager.getManager().removeInstance(player.getInstanceId());
                player.setInstanceId("");
            }

            if(claim("daily-donator-materials")){
                InstanceManager.getManager().startDonatorDailyMaterial(player);
            } else {
                player.sendMessage("@red@You have already claimed your daily Donator Materials Instance.");
            }
        } else {
            player.sendMessage("@red@You must be a VIP 5 to enter this area.");
        }
    }

    public void enterTreasureHunterInstance(@NotNull Player player){
        if(player.getLoyalty().getLevel() >= 3){
            if(player.getInstance() != null) {
                player.getInstance().destroy();
                player.setInstance(null);
                player.sendMessage("You have left your previous instance.");
                return;
            }

            if(!Objects.equals(player.getInstanceId(), "")){
                InstanceManager.getManager().removeInstance(player.getInstanceId());
                player.setInstanceId("");
            }

            if(claim("daily-treasure-hunter")){
                InstanceManager.getManager().startTreasureHunterInstance(player);
            } else {
                player.sendMessage("@red@You have already claimed your daily Treasure Hunter Instance.");
            }
        } else {
            player.sendMessage("@red@You must be a Loyalty 3 to enter this area.");
        }
    }

    public void enterDailyCasketInstance(@NotNull Player player){
        if(World.handler.eventActive("halloween")){
            if(player.getInstance() != null) {
                player.getInstance().destroy();
                player.setInstance(null);
                player.sendMessage("You have left your previous instance.");
                return;
            }

            if(!Objects.equals(player.getInstanceId(), "")){
                InstanceManager.getManager().removeInstance(player.getInstanceId());
                player.setInstanceId("");
            }

            if(claim("daily-casket")){
                InstanceManager.getManager().startDailyCasket(player);
            } else {
                player.sendMessage("@red@You have already claimed your daily Casket Raid Instance.");
            }
        } else {
            player.sendMessage("@red@Halloween Event must be active to enter.");
        }
    }
}
