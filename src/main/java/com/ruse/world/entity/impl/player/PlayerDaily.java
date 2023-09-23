package com.ruse.world.entity.impl.player;

import com.ruse.model.Item;
import com.ruse.world.WorldCalendar;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PlayerDaily {

    private final Map<String, Integer> claimed = new ConcurrentHashMap<>();

    public void load(Map<String, Integer> claimed){
        this.claimed.putAll(claimed);
    }

    public boolean claim(String content){
        if(claimed.containsKey(content))
            if(claimed.get(content) != WorldCalendar.getInstance().getDay()){
                claimed.replace(content, WorldCalendar.getInstance().getDay());
                return true;
            } else {
                return false;
            }
        else {
            claimed.put(content, WorldCalendar.getInstance().getDay());
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
}
