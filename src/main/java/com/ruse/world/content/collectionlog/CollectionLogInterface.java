package com.ruse.world.content.collectionlog;

import com.ruse.model.Item;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.util.Misc;
import com.ruse.world.content.KillsTracker;
import com.ruse.world.content.TeleportInterface;
import com.ruse.world.content.teleport.TeleInterfaceData;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.drops.Drop;
import com.ruse.world.packages.combat.drops.DropManager;
import com.ruse.world.packages.combat.drops.NPCDrops;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

//import com.sun.scenario.effect.DropShadow;

@RequiredArgsConstructor
public class CollectionLogInterface {
    public static final int INTERFACE_ID = 30360;
    private final int[] NPC_LIST = new int[]{};
    private final Player player;
    private final List<Integer> currentlyViewing = new ArrayList<>();
    private int currentIndex;
    @Getter @Setter public HashMap<Integer, RewardClaim> rewardsClaims = new HashMap<>();

    public void open() {
        currentIndex = 0;
        initialiseCurrentlyViewing();
        sendBossNames();
        sendNpcData(currentIndex);
        player.getPA().sendInterface(INTERFACE_ID);
    }

    private void sendBossNames() {
        int[] startingLine = new int[]{30560};
        currentlyViewing.forEach(entry -> player.getPA().sendString(startingLine[0]++, NpcDefinition.forId(entry).getName()));
    }

    private void initialiseCurrentlyViewing() {
        currentlyViewing.clear();

		for (TeleInterfaceData data : TeleInterfaceData.values())
			currentlyViewing.add(data.getNpcId());

		for (int entry : NPC_LIST) {
            currentlyViewing.add(entry);
        }
    }

    public void search(String name) {
        initialiseCurrentlyViewing();
        var tempList = new ArrayList<Integer>();
        for (int data : currentlyViewing) {
            if (Objects.nonNull(NpcDefinition.forId(data))) {
                if (!NpcDefinition.forId(data).getName().toLowerCase().contains(name.toLowerCase()))
                    tempList.add(data);
            } else {
                tempList.add(data);
            }
        }
        currentlyViewing.removeAll(tempList);

        for (int i = 0; i < 100; i++) {
            player.getPacketSender().sendString(30560 + i, "");
        }
        sendBossNames();
    }

    private boolean hasObtainedItem(int npc, int item) {
        return player.getCollectionLogData().stream().anyMatch(data -> data.getNpcId() == npc && data.getItem() == item);
    }

    public void checkForObtainedAll(int npcId) {
        RewardClaim rewardClaim = rewardsClaims.computeIfAbsent(npcId, x -> new RewardClaim());
        if(rewardClaim.hasClaimed || rewardClaim.canClaim) return;
        NPCDrops drops = DropManager.getManager().forId(npcId);
        if(drops != null) {
           Drop[] npcDropItems = drops.customTable().drops();
            for (Drop npcDrop : npcDropItems) {
                if (npcDrop.modifier() <= 1) {
                    continue;
                }
                if (!hasObtainedItem(npcId, npcDrop.id())) {
                    return;
                }
            }
            rewardClaim.canClaim = true;
        }
    }

    private void sendNpcData(int index) {
        int received = 0;
        int total = 0;
        var definition = NpcDefinition.forId(currentlyViewing.get(index));
        player.getPacketSender().sendString(30368, definition.getName());
        player.getPacketSender().sendString(30369, "Killcount: " +
                Misc.insertCommasToNumber(String.valueOf(KillsTracker.getTotalKillsForNpc(definition.getId(), player))));
        var drops = DropManager.getManager().forId(definition.getId());
        Drop[] npcDropItems;
        if(drops != null && (npcDropItems = drops.customTable().drops()) != null) {
            List<Item> items = new ArrayList<>();
            for (Drop npcDrop : npcDropItems) {
                if (npcDrop.modifier() <= 1) {
                    continue;
                }
                if (hasObtainedItem(definition.getId(), npcDrop.id())) {
                    var item = player.getCollectionLogData().stream().filter(data -> data.getNpcId() == definition.getId() && data.getItem() == npcDrop.id()).findFirst().get();
                    items.add(new Item(item.getItem(), item.getAmount()));
                    received++;
                } else {
                    items.add(new Item(npcDrop.id(), 0));
                }
                total++;
            }
            player.getPacketSender().sendItemContainer(items.toArray(new Item[0]), 30375);
        }

        Item[] rewards = RewardClaim.REWARDS.get(currentlyViewing.get(index));
        if(rewards != null) {
            for (int i = 0; i < 8; i++) {
                if(rewards.length > i) {
                    player.getPacketSender().sendItemOnInterface(30711, rewards[i].getId(), i, rewards[i].getAmount());
                } else {
                    player.getPacketSender().sendItemOnInterface(30711, -1, i, 0);
                }
            }
        }
        int lines = total / 6;
        if (total % 6 > 0)
            lines++;
        player.getPacketSender().setScrollMax(30385, lines * 40);
        player.getPacketSender().sendString(30367, "Obtained: @gre@" + received + "/" + total + "");
    }

    public boolean handleButton(int buttonId) {
        if(player.getInterfaceId() == INTERFACE_ID) {
            if(buttonId == 30712) {
                int npcId = currentlyViewing.get(currentIndex);
                if(RewardClaim.REWARDS.get(npcId) == null) return true;
                RewardClaim rewardClaim = rewardsClaims.computeIfAbsent(npcId, x -> new RewardClaim());
                Item[] rewards = RewardClaim.REWARDS.get(npcId);
                if(rewardClaim.canClaim) {
                    if(rewardClaim.hasClaimed) {
                        player.getPacketSender().sendMessage("@red@You already claimed this reward.");
                    } else if (player.getInventory().getFreeSlots() >= rewards.length) {
                        NPCDrops drops = DropManager.getManager().forId(npcId);
                        for (Drop drop : drops.customTable().drops()) {
                            if (drop.modifier() <= 1) {
                                continue;
                            }
                            if (!hasObtainedItem(npcId, drop.id())) {
                                return true;
                            }
                        }
                        rewardClaim.hasClaimed = true;
                        player.getInventory().addItemSet(rewards);
                    } else {
                        player.getPacketSender().sendMessage("@red@You need at least " + rewards.length + " free slots to claim this.");
                    }
                } else {
                    player.getPacketSender().sendMessage("@red@You cannot claim this reward yet.");
                }
                return true;
            }
            if (!(buttonId >= 30560 && buttonId <= 30760)) {
                return false;
            }
            int index = -30560 + buttonId;
            if (currentlyViewing.size() > index) { //i do though, has to be as clean as possible ; here are some free lines
                currentIndex = index;
                sendNpcData(index);
            }
            return true;
        }
        return false;
    }

    @RequiredArgsConstructor
    @Getter
    @Setter
    public static class RewardClaim {
        boolean canClaim;
        boolean hasClaimed;

        public static HashMap<Integer, Item[]> REWARDS = new HashMap<>();

        static {
            REWARDS.put(9837, new Item[]{new Item(995,1000), new Item(23200,5)});
        }
    }

}