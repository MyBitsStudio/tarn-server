package com.ruse.world.content.donation;

import com.google.gson.Gson;
import com.ruse.model.Item;
import com.ruse.model.ItemRarity;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.projectile.ItemEffect;
import com.ruse.security.save.impl.FlashDealLoad;
import com.ruse.world.content.discordbot.AdminCord;
import com.ruse.world.entity.impl.player.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class FlashDeals {

    private static FlashDeals flash;

    public static FlashDeals getDeals() {
        if (flash == null) {
            flash = new FlashDeals();
        }
        return flash;
    }

    private final AtomicBoolean isActive = new AtomicBoolean(false);
    private final AtomicBoolean specialActive = new AtomicBoolean(false);
    private final List<Integer> doubledItems = new CopyOnWriteArrayList<>();
    private final Map<Integer, Map<Integer, Integer>> deals = new ConcurrentHashMap<>();
    private final Map<Integer, Map<Integer, String>> specialDeals = new ConcurrentHashMap<>();

    public FlashDeals(){
        load();
    }

    public void setIsActive(boolean active){
        isActive.set(active);
    }
    public void setSpecialActive(boolean active){
        specialActive.set(active);
    }

    public boolean isActive(){
        return isActive.get();
    }

    public void setDoubledItems(List<Integer> items){
        doubledItems.clear();
        doubledItems.addAll(items);
    }

    public void setDeals(Map<Integer, Map<Integer, Integer>> deals){
        this.deals.clear();
        this.deals.putAll(deals);
    }

    public void setSpecialDeals(Map<Integer, Map<Integer, String>> deals){
        this.specialDeals.clear();
        this.specialDeals.putAll(deals);
    }

    public boolean handleFlashDeals(Player player, int amount, int[] items){
        if(!isActive.get()){
            player.getPacketSender().sendMessage("There are no active flash deals at the moment.");
            return false;
        }

        try{
            isDoubled(player, items);
            handleAmountRewards(player, amount);
            handleSpecialDeals(player, amount);

            if(specialActive.get())
                handleSpecialActive(player);

            return true;
        } catch(Exception e){
            player.getPacketSender().sendMessage("There was an error when collecting your flash deal.");
            return false;
        }

    }

    private void handleSpecialActive(Player player){

    }

    private void isDoubled(Player player, int[] items){
        for (int item : items) {
            if (doubledItems.contains(item)) {
                player.getInventory().add(item, 1);
                player.getPacketSender().sendMessage("You have collected a bonus and had your item doubled!");
                AdminCord.sendMessage(1109343574534934538L, player.getUsername()+" has collected doubled item "+ ItemDefinition.forId(item).getName());
            }
        }
    }

    private void handleAmountRewards(Player player, int amount){
        for(Map.Entry<Integer, Map<Integer, Integer>> deal : deals.entrySet()){
            int amounts = deal.getKey();
            if(amount >= amounts){
                int multiple = Math.floorDiv(amount , amounts);
                Map<Integer, Integer> rewards = deal.getValue();
                for(Map.Entry<Integer, Integer> reward : rewards.entrySet()){
                    int item = reward.getKey();
                    int quantity = reward.getValue();
                    player.getInventory().add(item, quantity * multiple);
                    player.sendMessage("You have collected a bonus for spending " + amounts + " credits!");
                    AdminCord.sendMessage(1109343574534934538L, player.getUsername()+" has collected deal item "+ ItemDefinition.forId(item).getName()+" x"+quantity);
                }
            }
        }
    }

    private void handleSpecialDeals(Player player, int amount){
        for(Map.Entry<Integer, Map<Integer, String>> deal : specialDeals.entrySet()){
            int amounts = deal.getKey();
            if(amount >= amounts){
                int multiple = Math.floorDiv(amount , amounts);
                Map<Integer, String> rewards = deal.getValue();
                for(Map.Entry<Integer, String> reward : rewards.entrySet()){
                    int item = reward.getKey();
                    String[] name = reward.getValue().split("--");
                    ItemEffect effect = ItemEffect.getEffectForName(name[0]);
                    ItemRarity rarity = ItemEffect.getRarityForName(name[0]);
                    int bonus = Integer.parseInt(name[1]);
                    Item items = new Item(item, multiple, effect, bonus, rarity);
                    player.getInventory().add(items);
                    player.sendMessage("You have collected a special bonus for spending " + amounts + " credits!");
                    AdminCord.sendMessage(1109343574534934538L, player.getUsername()+" has collected special item "+ item);
                }
            }
        }
    }

    private void load(){
        new FlashDealLoad(this).loadJSON("./data/flash.json").run();
    }

    public void reload(){
        new FlashDealLoad(this).loadJSON("./data/flash.json").run();
        System.out.println("Active Deals \n"+deals+"\nSpecial Deals \n"+specialDeals+"\nDoubled Items \n"+doubledItems);
    }

}
