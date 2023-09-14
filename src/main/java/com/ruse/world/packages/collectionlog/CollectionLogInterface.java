package com.ruse.world.packages.collectionlog;

import com.ruse.model.Item;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.KillsTracker;
import com.ruse.world.content.teleport.TeleInterfaceData;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.drops.Drop;
import com.ruse.world.packages.combat.drops.DropManager;
import com.ruse.world.packages.combat.drops.NPCDrops;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;

//import com.sun.scenario.effect.DropShadow;

@RequiredArgsConstructor
public class CollectionLogInterface {
    public static final int INTERFACE_ID = 30360;
    private final int[] NPC_LIST = new int[]{
            //starters
            9837, 9027, 9835, 9911, 9922, 8014, 8003, 811, 9817, 92, 3313, 1906, 9836, 1742,
            1743, 1744, 1745, 1738, 1739, 1740, 1741, 9025, 9026, 8008, 2342, 9839,
            9806, 4972, 1746, 3305, 3021, 125,

            //globals
            8010, 3308, 9904, 8013, 9005, 587,

            //instances
            9915, 9024, 8002, 8000, 9919, 9913, 3020, 1313, 1311, 9914, 185, 188,
            3117, //multi

            3013, 9017, 3016, 12239, // single

            9818, 591, 593, //special

            3010, 3014, 595 //masters


    };
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

		for (int entry : NPC_LIST) {
            if(currentlyViewing.stream().noneMatch(x -> x == entry))
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

        int npcId = currentlyViewing.get(currentIndex);
        if(RewardClaim.REWARDS.get(npcId) == null) return;
        RewardClaim rewardClaim = rewardsClaims.computeIfAbsent(npcId, x -> new RewardClaim());
        if(rewardClaim.canClaim) {
            if (rewardClaim.hasClaimed) {
                player.getPacketSender().sendString(30712, "Claimed");
            } else {
                player.getPacketSender().sendString(30712, "Claim Now!");
            }
        } else {
            player.getPacketSender().sendString(30712, "Cannot Claim");
        }
    }

    public boolean handleButton(int buttonId) {
        if(player.getInterfaceId() == INTERFACE_ID) {
            if(buttonId == 30712) {
                if(!World.attributes.getSetting("collection")){
                    player.getPacketSender().sendMessage("The collection log is currently disabled.");
                    return true;
                }
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
                    player.save();
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
            REWARDS.put(9837, new Item[]{new Item(995,15000), new Item(12852,25)});
            REWARDS.put(9027, new Item[]{new Item(995,15000), new Item(12852,25)});
            REWARDS.put(9835, new Item[]{new Item(995,15000), new Item(12852,25)});
            REWARDS.put(9911, new Item[]{new Item(995,15000), new Item(12852,25)});
            REWARDS.put(9922, new Item[]{new Item(995,15000), new Item(12852,25)});
            REWARDS.put(8014, new Item[]{new Item(995,15000), new Item(12852,25)});
            REWARDS.put(8003, new Item[]{new Item(995,15000), new Item(12852,25)});
            REWARDS.put(811, new Item[]{new Item(995,15000), new Item(12852,25)});
            REWARDS.put(9817, new Item[]{new Item(995,15000), new Item(12852,25)});
            REWARDS.put(92, new Item[]{new Item(995,15000), new Item(12852,25)});
            REWARDS.put(3313, new Item[]{new Item(995,15000), new Item(12852,25)});
            REWARDS.put(1906, new Item[]{new Item(995,15000), new Item(12852,25)});
            REWARDS.put(9836, new Item[]{new Item(995,15000), new Item(12852,25)});

            REWARDS.put(1742, new Item[]{new Item(995,20000), new Item(23253,1)});
            REWARDS.put(1743, new Item[]{new Item(995,20000), new Item(23253,1)});
            REWARDS.put(1744, new Item[]{new Item(995,20000), new Item(23253,1)});
            REWARDS.put(1745, new Item[]{new Item(995,20000), new Item(23253,1)});
            REWARDS.put(1738, new Item[]{new Item(995,20000), new Item(23253,1)});
            REWARDS.put(1739, new Item[]{new Item(995,20000), new Item(23253,1)});

            REWARDS.put(1740, new Item[]{new Item(995,25000), new Item(23149,1)});
            REWARDS.put(1741, new Item[]{new Item(995,25000), new Item(23149,1)});

            REWARDS.put(9025, new Item[]{new Item(995,30000), new Item(20500,1)});
            REWARDS.put(9026, new Item[]{new Item(995,30000), new Item(20500,1)});
            REWARDS.put(8008, new Item[]{new Item(995,30000), new Item(20500,1)});
            REWARDS.put(2342, new Item[]{new Item(995,30000), new Item(20500,1)});
            REWARDS.put(9839, new Item[]{new Item(995,30000), new Item(20500,1)});
            REWARDS.put(9806, new Item[]{new Item(995,30000), new Item(20500,1)});

            REWARDS.put(4972, new Item[]{new Item(995,35000), new Item(23149,2)});
            REWARDS.put(1746, new Item[]{new Item(995,35000), new Item(23149,2)});
            REWARDS.put(3305, new Item[]{new Item(995,35000), new Item(23149,2)});
            REWARDS.put(3021, new Item[]{new Item(995,35000), new Item(23149,2)});
            REWARDS.put(125, new Item[]{new Item(995,35000), new Item(23149,2)});

            REWARDS.put(8010, new Item[]{new Item(10835,100), new Item(19001,3)});
            REWARDS.put(3308, new Item[]{new Item(10835,100), new Item(19001,3)});
            REWARDS.put(9904, new Item[]{new Item(10835,100), new Item(19001,3)});
            REWARDS.put(8013, new Item[]{new Item(4001,50), new Item(18768,3)});
            REWARDS.put(9005, new Item[]{new Item(23335,10), new Item(23058,2), new Item(23256,2)});
            REWARDS.put(587, new Item[]{new Item(23335,15), new Item(23257,2)});

            REWARDS.put(9915, new Item[]{new Item(995,50000), new Item(23254,2)});
            REWARDS.put(9024, new Item[]{new Item(995,50000), new Item(23254,2)});
            REWARDS.put(8002, new Item[]{new Item(995,50000), new Item(23254,2)});
            REWARDS.put(8000, new Item[]{new Item(995,50000), new Item(23254,2)});
            REWARDS.put(3013, new Item[]{new Item(995,50000), new Item(23254,2)});
            REWARDS.put(3831, new Item[]{new Item(995,50000), new Item(23254,2)});

            REWARDS.put(9919, new Item[]{new Item(995,65000), new Item(20501,2)});
            REWARDS.put(9913, new Item[]{new Item(995,65000), new Item(20501,2)});
            REWARDS.put(3020, new Item[]{new Item(995,65000), new Item(20501,2)});
            REWARDS.put(9017, new Item[]{new Item(995,65000), new Item(20501,2)});
            REWARDS.put(3016, new Item[]{new Item(995,65000), new Item(20501,2)});

            REWARDS.put(1313, new Item[]{new Item(995,75000), new Item(23148,1)});
            REWARDS.put(1311, new Item[]{new Item(995,75000), new Item(23148,1)});
            REWARDS.put(12239, new Item[]{new Item(995,75000), new Item(23148,1)});

            REWARDS.put(9818, new Item[]{new Item(10835,100), new Item(23058,1)});
            REWARDS.put(591, new Item[]{new Item(10835,250), new Item(23058,2)});
            REWARDS.put(593, new Item[]{new Item(4000,100), new Item(4001,25), new Item(18768,2)});

            REWARDS.put(3010, new Item[]{new Item(10835,250), new Item(9076,10)});
            REWARDS.put(3014, new Item[]{new Item(10835,250), new Item(9076,10)});
            REWARDS.put(595, new Item[]{new Item(10835,250), new Item(9076,10)});
        }
    }

}