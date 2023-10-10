package com.ruse.world.packages.misc;

import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.impl.UltimateIronman;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CurrPouch {

    private final int[] allowed = {995, 10835, 5020, 5022, 19000, 19002, 23003, 5023, 19639};
    @Getter
    private final Map<Integer, Long> currency = new HashMap<>();
    private final Player player;

    public void load(Map<Integer, Long> currency){
        this.currency.putAll(currency);
    }

    public CurrPouch(Player player){
        this.player = player;
    }

    public int checkBalance(int itemId){
        return currency.getOrDefault(itemId, 0L).intValue();
    }

    public void printBalances(){
        currency.forEach((k, v) -> player.getPacketSender().sendMessage("[POUCH] "+ItemDefinition.forId(k).getName() + " : " + v));
    }

    public boolean withdraw(int item, int amount){
        int balance = checkBalance(item);
        if(balance <= 0){
            player.getPacketSender().sendMessage("You have no " + ItemDefinition.forId(item).getName() + " in your currency pouch.");
            return false;
        }
        if(balance < amount){
            amount = balance;
        }

        if(player.getInventory().getFreeSlots() < 1){
            player.getPacketSender().sendMessage("You do not have enough inventory space to withdraw that many.");
            return false;
        }

        if(player.getInventory().getAmount(item) + amount > Integer.MAX_VALUE){
            player.getPacketSender().sendMessage("You cannot withdraw that many.");
            return false;
        }

        player.getInventory().add(new Item(item, amount));
        currency.put(item, (long) (balance - amount));


        return true;
    }

    public boolean deposit(int item, int amount){

        if(player.getMode() instanceof UltimateIronman){
            if(Arrays.stream(allowed).noneMatch(i -> i == item)){
                player.getPacketSender().sendMessage("You cannot deposit that item into your currency pouch.");
                return false;
            }

            if(currency.containsKey(item)){
                if(currency.get(item) >= Integer.MAX_VALUE){
                    player.getPacketSender().sendMessage("Your currency pouch cannot hold any more of that item.");
                    return false;
                }
            }

            if(player.getInventory().getAmount(item) < amount){
                amount = player.getInventory().getAmount(item);
            }

            if(currency.containsKey(item)) {
                if (currency.get(item) + amount > Integer.MAX_VALUE) {
                    amount = Integer.MAX_VALUE - currency.get(item).intValue();
                }
            }

            player.getInventory().delete(new Item(item, amount));
            currency.put(item, currency.getOrDefault(item, 0L) + amount);

            return true;
        }

        return false;
    }

    public boolean handleItemOnItem(Item usedWith){
        if(Arrays.stream(allowed).anyMatch(i -> i == usedWith.getId())){
            deposit(usedWith.getId(), usedWith.getAmount());
            return true;
        }
        return false;
    }
}
