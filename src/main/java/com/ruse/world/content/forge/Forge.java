package com.ruse.world.content.forge;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class Forge {

    public static final int INTERFACE_ID = 49510;
    private static final int PERCENTAGE_BAR_ID = 49529;
    private static final int MAX_LEVEL = ForgeTierType.VALUES.size();
    private static final int FRAGMENT_ITEM_ID = 18653;

    private final Player player;
    private int tier;
    private int progress;
    private int progressToAdd;
    private final HashMap<Item, Integer> addedItemMap = new HashMap<>();

    public void showInterface() {
        reset();
        updateInterface();
        player.getPacketSender().sendItemContainer(player.getInventory(), 49541);
        player.getPacketSender().sendInterfaceSet(INTERFACE_ID, 49540);
    }

    private void reset() {
        progressToAdd = 0;
        addedItemMap.clear();
    }

    public void updateInterface() {
        player.getPacketSender().sendItemContainer(new ArrayList<>(), 49539)
                .updateProgressSpriteBar(PERCENTAGE_BAR_ID, progress, getValueForNextTier().orElseGet(() -> progress))
                .sendItemOnInterface(49523, 18653, 0)
                .sendString(49527, String.valueOf(0))
                .setScrollMax(49538, 220)
                .sendString(49531, "Tier: " + tier)
                .sendString(49526, String.valueOf(tier));
    }

    public void addItem(Item item) {
        if(addedItemMap.entrySet()
                .stream()
                .anyMatch(it -> it.getKey().getId() == item.getId())) {
            player.getPacketSender().sendMessage("@red@You have already added a " + item.getDefinition().getName());
            return;
        }
        if(!player.getInventory().contains(item.getId(), item.getEffect(), item.getBonus(), item.getRarity())) {
            player.getPacketSender().sendMessage("@red@You do not have this item.");
            return;
        }
        int itemId = item.getId();
        int value = SacrificeData.getValue(itemId);
        if(value == 0) {
            player.getPacketSender().sendMessage("@red@This item has no value on the forge.");
            return;
        }
        if(addedItemMap.size() >= 90) {
            player.getPacketSender().sendMessage("@red@Forge is full.");
            return;
        }
        int progressNeeded = 0;
        if(tier != MAX_LEVEL) {
            Optional<Integer> optionalInteger = getValueForNextTier();
            if (optionalInteger.isPresent()) {
                progressNeeded = optionalInteger.get();
                if (progressToAdd >= progressNeeded) {
                    player.getPacketSender().sendMessage("@red@The forge will not accept anymore items.");
                    return;
                }
            }
        }
        progressToAdd += value;
        addedItemMap.put(item, 1);
        if(progressNeeded != 0) {
            player.getPacketSender().updateProgressSpriteBar(PERCENTAGE_BAR_ID, progressToAdd + progress, progressNeeded);
        }
        player.getPacketSender().sendItemContainer(getItemList(), 49539);
        player.getPacketSender().sendItemOnInterface(49523, 18653, progressToAdd);
        player.getPacketSender().sendString(49527, String.valueOf(progressToAdd));
        player.getPacketSender().setScrollMax(49538, Math.max(220, (addedItemMap.size()/6)*43));
    }

    public void removeItem(int itemId) {
        Item toRemove = null;
        for(Map.Entry<Item, Integer> entry : addedItemMap.entrySet()) {
            if(entry.getKey().getId() == itemId) {
                toRemove = entry.getKey();
                break;
            }
        }
        if(toRemove != null) {
            addedItemMap.remove(toRemove);
            player.getPacketSender().sendItemContainer(getItemList(), 49539);
            progressToAdd -= SacrificeData.getValue(itemId);
            player.getPacketSender().sendItemOnInterface(49523, 18653, progressToAdd - SacrificeData.getValue(itemId));
            player.getPacketSender().sendString(49527, String.valueOf(progressToAdd));
            player.getPacketSender().updateProgressSpriteBar(PERCENTAGE_BAR_ID, progressToAdd + progress, getValueForNextTier().orElse(0));
        }
    }

    private void startForge() {
        List<Item> itemList = getItemList();
        if(itemList.size() == 0) {
            player.getPacketSender().sendMessage("@red@Try adding items to the forge before starting.");
            return;
        }
        for(Item item : itemList) {
            if(player.getInventory().getAmount(item.getId(), item.getEffect(), item.getRarity(), item.getBonus()) < item.getAmount()) {
                player.getPacketSender().sendMessage("@red@Please try again.");
                return;
            }
        }
        for(Item item : itemList) {
            player.getInventory().delete(item.getId(), item.getAmount(), item.getRarity(), item.getBonus(), item.getEffect());
        }
        if(tier != MAX_LEVEL) {
            Optional<Integer> optionalInteger = getValueForNextTier();
            if(optionalInteger.isPresent()) {
                progress += progressToAdd;
                int neededValue = optionalInteger.get();
                if(progress >= neededValue) {
                    levelUp();
                }
            }
        }
        player.getInventory().add(FRAGMENT_ITEM_ID, progressToAdd);
        player.getPacketSender().sendItemContainer(player.getInventory(), 49541);
        reset();
        updateInterface();
    }

    public boolean handleButtonClick(int btnId) {
        if(btnId == -16016) {
            startForge();
            return true;
        } else if(btnId == -15988) {
            showInterface();
            return true;
        }
        return false;
    }

    public void onInterfaceClose() {
        reset();
    }

    private void levelUp() {
        tier++;
        if(tier != MAX_LEVEL) {
            progress = 0;
        } else {
            player.getPacketSender().sendMessage("@red@You have reached max level!");
        }
        player.getPacketSender().sendMessage("@red@You have increased your forge level to " + tier + "!");
    }

    private Optional<Integer> getValueForNextTier() {
        return ForgeTierType.VALUES
                .stream()
                .filter(forgeTierType -> forgeTierType.getTier() == (tier + 1))
                .map(ForgeTierType::getRequiredValue)
                .findFirst();
    }

    private List<Item> getItemList() {
        return addedItemMap.size() == 0 ? new ArrayList<>()
                : addedItemMap.entrySet()
                .stream()
                .map(item -> item.getKey().setAmount(item.getValue()))
                .collect(toList());
    }
}
