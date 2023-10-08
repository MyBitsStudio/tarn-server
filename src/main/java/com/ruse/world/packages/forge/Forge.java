package com.ruse.world.packages.forge;

import com.ruse.model.Item;
import com.ruse.model.container.ItemContainer;
import com.ruse.model.container.StackType;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Getter
public class Forge {

    public static final int INTERFACE_ID = 49510;
    private static final int PERCENTAGE_BAR_ID = 49529;
    private static final int MAX_LEVEL = ForgeTierType.VALUES.size();
    public static final int FRAGMENT_ITEM_ID = 18653;

    private final Player player;
    @Setter private int tier;
    @Setter private int progress;
    private int progressToAdd;
    private final ItemContainer container;
    private final HashMap<Item, Integer> addedItemMap = new HashMap<>();


    public Forge(Player player){
        this.player = player;
        container = new ItemContainer(player) {
            @Override
            public int capacity() {
                return 28;
            }

            @Override
            public StackType stackType() {
                return StackType.DEFAULT;
            }

            @Override
            public ItemContainer refreshItems() {
                return this;
            }

            @Override
            public ItemContainer full() {
                return this;
            }
        };
    }

    public void showInterface() {
        if(!World.attributes.getSetting("forge")){
            if(!player.getRank().isStaff()) {
                player.sendMessage("This feature is currently disabled.");
                return;
            }
        }
        reset();
        updateInterface();
        player.getPacketSender().sendItemContainer(player.getInventory(), 49541);
        player.getPacketSender().sendInterfaceSet(INTERFACE_ID, 49540);
    }

    private void reset() {
        progressToAdd = 0;
        container.resetItems();
    }

    public void updateInterface() {
        player.getPacketSender().sendItemContainer(container, 49539)
                .updateProgressSpriteBar(PERCENTAGE_BAR_ID, progress, getValueForNextTier().orElseGet(() -> progress))
                .sendItemOnInterface(49523, 18653, 0)
                .sendString(49527, String.valueOf(0))
                .sendString(49531, "Tier: " + tier)
                .sendString(49526, String.valueOf(tier));
    }

    public void addSilent(Item item){
        if(addedItemMap.entrySet()
                .stream()
                .anyMatch(it -> it.getKey().getId() == item.getId())) {
            return;
        }
        if(!player.getInventory().contains(item.getId())) {
            return;
        }
        int itemId = item.getId();
        int value = SacrificeData.getValue(itemId);
        if(value == 0) {
            return;
        }
        if(addedItemMap.size() >= 90) {
            return;
        }
        int progressNeeded = 0;
        if(tier != MAX_LEVEL) {
            Optional<Integer> optionalInteger = getValueForNextTier();
            if (optionalInteger.isPresent()) {
                progressNeeded = optionalInteger.get();
//                if (progressToAdd >= progressNeeded) {
//                    player.getPacketSender().sendMessage("@red@The forge will not accept anymore items.");
//                    return;
//                }
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

    public void addItem(Item item) {
        if(!World.attributes.getSetting("forge")){
            if(!player.getRank().isStaff()) {
                player.sendMessage("This feature is currently disabled.");
                return;
            }
        }
        if(!player.getInventory().contains(item.getId())) {
            player.getPacketSender().sendMessage("@red@You do not have this item.");
            return;
        }
        int itemId = item.getId();
        int value = SacrificeData.getValue(itemId);
        if(value == 0) {
            player.getPacketSender().sendMessage("@red@This item has no value on the forge.");
            return;
        }
        int progressNeeded = 0;
        if(tier != MAX_LEVEL) {
            Optional<Integer> optionalInteger = getValueForNextTier();
            if (optionalInteger.isPresent()) {
                progressNeeded = optionalInteger.get();
//                if (progressToAdd >= progressNeeded) {
//                    player.getPacketSender().sendMessage("@red@The forge will not accept anymore items.");
//                    return;
//                }
            }
        }
        progressToAdd += value;
        container.add(item.copy(), true);
        player.getInventory().delete(item.copy());
        if(progressNeeded != 0) {
            player.getPacketSender().updateProgressSpriteBar(PERCENTAGE_BAR_ID, progressToAdd + progress, progressNeeded);
        }
        player.getPacketSender().sendItemContainer(player.getInventory(), 49541);
        player.getPacketSender().sendItemContainer(container.getValidItems(), 49539);
        player.getPacketSender().sendItemOnInterface(49523, 18653, progressToAdd);
        player.getPacketSender().sendString(49527, String.valueOf(progressToAdd));
        player.getPacketSender().setScrollMax(49538, Math.max(220, (container.getValidItems().size()/6)*43));
    }

    public void removeItem(int itemId) {
        Item toRemove = container.forSlot(itemId);
        if(toRemove != null) {
            progressToAdd -= SacrificeData.getValue(itemId);
            player.getInventory().add(toRemove.copy(), true);
            container.delete(toRemove.copy());
            container.refreshItems();
            player.getPacketSender().sendItemContainer(player.getInventory(), 49541);
            player.getPacketSender().sendItemContainer(container.getValidItems(), 49539);
            player.getPacketSender().sendItemOnInterface(49523, 18653, progressToAdd);
            player.getPacketSender().sendString(49527, String.valueOf(progressToAdd));
            player.getPacketSender().updateProgressSpriteBar(PERCENTAGE_BAR_ID, progressToAdd + progress, getValueForNextTier().orElse(0));
        }
    }

    private void startForge() {
        int amount = 0;
        if(container.getFreeSlots() == container.capacity()) {
            player.getPacketSender().sendMessage("@red@Try adding items to the forge before starting.");
            return;
        }
        for(Item item : container.getValidItemsArray()) {
            amount += SacrificeData.getValue(item.getId());
            container.delete(item);
        }
        if(tier != MAX_LEVEL) {
            Optional<Integer> optionalInteger = getValueForNextTier();
            if(optionalInteger.isPresent()) {
                progress += amount;
                int neededValue = optionalInteger.get();
                if(progress >= neededValue) {
                    levelUp();
                }
            }
        }
        player.getInventory().add(FRAGMENT_ITEM_ID, amount);
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
        for(Item item : container.getValidItemsArray()){
            player.getInventory().add(item);
        }
        reset();
    }

    private void levelUp() {
        tier++;
        if(tier == MAX_LEVEL) {
            player.getPacketSender().sendMessage("@red@You have reached max level!");
        } else {
            progress = 0;
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

    public void sendDialogue(){
       Arrays.stream(player.getInventory().getItems())
                .filter(Objects::nonNull)
                .filter(item -> SacrificeData.getValue(item.getId()) != 0)
                .forEach(this::addSilent);
    }
}
