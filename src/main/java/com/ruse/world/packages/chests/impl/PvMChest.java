package com.ruse.world.packages.chests.impl;

import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.chests.Chest;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PvMChest extends Chest {
    @Override
    public boolean open(@NotNull Player player) {
        if(player.getInventory().getFreeSlots() <= 0){
            player.sendMessage("You need more inventory spaces to continue");
            return false;
        }

        if(player.getInventory().contains(keyId(), 5)){
            player.getInventory().delete(keyId(), 5, true);
        } else if(player.getInventory().contains(23107, 5)){
            player.getInventory().delete(23107, 5, true);
        } else {
            player.sendMessage("Something went wrong here...");
            return false;
        }

        Item reward = rewards().get(Misc.random(rewards().size() - 1));
        player.getInventory().add(reward);
        player.sendMessage("You have successfully retrieved "+ ItemDefinition.forId(reward.getId()).getName()+" from the Slayer Chest!");
        return true;
    }

    @Override
    public int keyId() {
        return 23105;
    }

    @Override
    public int animationId() {
        return -1;
    }

    @Override
    public int graphicId() {
        return -1;
    }

    @Override
    public List<Item> rewards() {
        List<Item> rewards = new ArrayList<>();
        rewards.add(new Item(2736, 1));
        rewards.add(new Item(2736, 3));
        rewards.add(new Item(2736, 5));
        rewards.add(new Item(13727, 100));
        rewards.add(new Item(13727, 250));
        rewards.add(new Item(13727, 500));
        rewards.add(new Item(15358, 1));
        rewards.add(new Item(15359, 1));
        rewards.add(new Item(995, 25000));
        rewards.add(new Item(995, 50000));
        rewards.add(new Item(604, 1));
        rewards.add(new Item(604, 2));
        rewards.add(new Item(604, 3));
        rewards.add(new Item(23148, 1));
        rewards.add(new Item(23148, 2));
        rewards.add(new Item(23148, 3));
        rewards.add(new Item(23147, 1));
        rewards.add(new Item(23147, 2));
        rewards.add(new Item(23147, 3));
        return rewards;
    }
}
