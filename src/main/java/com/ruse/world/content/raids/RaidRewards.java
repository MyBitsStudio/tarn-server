package com.ruse.world.content.raids;

import com.ruse.model.Item;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;

import java.util.*;

public abstract class RaidRewards {

    protected final Raid raid;
    protected List<Player> claimed = new ArrayList<>();
    protected Map<Player, List<Item>> rewards = new HashMap<>();
    private final int KEY_1 = 23200, KEY_2 = 23201, KEY_3 = 23202;

    public RaidRewards(Raid raid){
        this.raid = raid;
    }

    public abstract void claim(Player player);

    public void claimRewards(Player player){
        if(raid.isFinished()){
            if(claimed.contains(player)){
                player.sendMessage("You have already claimed your reward!");
                return;
            }
            claimed.add(player);

            int chance = Misc.inclusiveRandom(0, 326);

            Item key = chance >= 311 ? new Item(KEY_3, 1) : chance >= 282 ? new Item(KEY_2, 1) : new Item(KEY_1, 1);

            if(raid.getParty().getKeyWithHighestValue().equals(player.getUsername())){
                if(Misc.random(5) == 0){
                    key.setAmount(2);
                }
            }

            if(rewards.containsKey(player)){
                rewards.get(player).add(key);
            } else {
                List<Item> items = new ArrayList<>();
                items.add(key);
                rewards.put(player, items);
            }

            claim(player);

//            if(player.getInventory().getFreeSlots() < rewards.get(player).size()){
//                for(Item items : Objects.requireNonNull(rewards.get(player))){
//                    if(items.getId() == KEY_1 || items.getId() == KEY_2 || items.getId() == KEY_3){
//                        if(player.getInventory().contains(items.getId())){
//                            player.getInventory().add(items);
//                        } else if(player.getInventory().getFreeSlots() <= 0) {
//                            player.getBank(player.getCurrentBankTab()).add(items);
//                        } else {
//                            player.getInventory().add(items);
//                        }
//                    } else if (player.getInventory().contains(items.getId()) && items.getDefinition().isStackable()) {
//                        player.getInventory().add(items);
//                    } else if (player.getInventory().getFreeSlots() <= 0) {
//                        player.getBank(player.getCurrentBankTab()).add(items);
//                    } else {
//                        player.getInventory().add(items);
//                    }
//                    player.sendMessage("@red@ You have received a "+items.getDefinition().getName()+" from completing the raid");
//                }
//            } else {
//
//            }
            sendInterface(player);

        }


    }

    private void sendInterface(Player player){

    }
}
