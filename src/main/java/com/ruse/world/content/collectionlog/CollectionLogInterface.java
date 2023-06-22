package com.ruse.world.content.collectionlog;

import com.ruse.model.Item;
import com.ruse.model.definitions.NPCDrops;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.util.Misc;
import com.ruse.world.content.KillsTracker;
import com.ruse.world.content.TeleportInterface;
import com.ruse.world.entity.impl.player.Player;
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
    private List<Integer> currentlyViewing = new ArrayList<>();
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
        currentlyViewing.forEach(entry -> player.getPA().sendString(startingLine[0]++, "" + NpcDefinition.forId(entry).getName()));
    }

    private void initialiseCurrentlyViewing() {
        currentlyViewing.clear();

		for (TeleportInterface.Bosses data : TeleportInterface.Bosses.values())
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
        NPCDrops.NpcDropItem[] npcDropItems;
        NPCDrops drops = NPCDrops.forId(npcId);
        if(drops != null) {
            npcDropItems = drops.getDropList();
            for (NPCDrops.NpcDropItem npcDrop : npcDropItems) {
                if (npcDrop.getChance() <= 1) {
                    continue;
                }
                if (!hasObtainedItem(npcId, npcDrop.getId())) {
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
        player.getPacketSender().sendString(30368, "" + definition.getName() + "");
        player.getPacketSender().sendString(30369, "Killcount: " +
                Misc.insertCommasToNumber(String.valueOf(KillsTracker.getTotalKillsForNpc(definition.getId(), player))));
        var drops = NPCDrops.forId(definition.getId());
        NPCDrops.NpcDropItem[] npcDropItems;
        if(drops != null && (npcDropItems = drops.getDropList()) != null) {
            List<Item> items = new ArrayList<>();
            for (NPCDrops.NpcDropItem npcDrop : npcDropItems) {
                if (npcDrop.getChance() <= 1) {
                    continue;
                }
                if (hasObtainedItem(definition.getId(), npcDrop.getId())) {
                    var item = player.getCollectionLogData().stream().filter(data -> data.getNpcId() == definition.getId() && data.getItem() == npcDrop.getId()).findFirst().get();
                    items.add(new Item(item.getItem(), item.getAmount()));
                    received++;
                } else {
                    items.add(new Item(npcDrop.getId(), 0));
                }
                total++;
            }
            player.getPacketSender().sendItemContainer(items.toArray(new Item[0]), 30375);
        }

        Item[] rewards = RewardClaim.REWARDS.get(currentlyViewing.get(index));
        if(rewards != null) {
            for (int i = 0; i < 8; i++) {
                if(rewards.length > i) {
                    player.getPacketSender().sendItemOnInterface(30711, rewards[i].getId(), i, rewards[i].getAmount(), -1);
                } else {
                    player.getPacketSender().sendItemOnInterface(30711, -1, i, 0,-1);
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
                    } else {
                        if(player.getInventory().getFreeSlots() >= rewards.length) {
                            NPCDrops drops = NPCDrops.forId(npcId);
                            for(NPCDrops.NpcDropItem drop : drops.getDropList()) {
                                if (drop.getChance() <= 1) {
                                    continue;
                                }
                                if(!hasObtainedItem(npcId, drop.getId())) {
                                    return true;
                                }
                            }
                            rewardClaim.hasClaimed = true;
                            player.getInventory().addItemSet(rewards);
                        } else {
                            player.getPacketSender().sendMessage("@red@You need at least " + rewards.length + " free slots to claim this.");
                        }
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
            //MOBS
            REWARDS.put(TeleportInterface.Bosses.ELITE_DRAGON.getNpcId(), new Item[]{new Item(19119,1), new Item(10835,5000)});
            REWARDS.put(TeleportInterface.Bosses.ETERNAL_DRAGON.getNpcId(), new Item[]{new Item(2736,3), new Item(10835,10000)});
            REWARDS.put(TeleportInterface.Bosses.SCARLET_FALCON.getNpcId(), new Item[]{new Item(2736,3), new Item(10835,10000)});
            REWARDS.put(TeleportInterface.Bosses.CRYSTAL_QUEEN.getNpcId(), new Item[]{new Item(2736,3), new Item(10835,10000)});
            REWARDS.put(TeleportInterface.Bosses.LUCIFER.getNpcId(),  new Item[]{new Item(19886,1), new Item(10835,10000)});
            REWARDS.put(TeleportInterface.Bosses.MEGA_AVATAR.getNpcId(), new Item[]{new Item(2736,3), new Item(10835,10000)});
            REWARDS.put(TeleportInterface.Bosses.CRAZY_WITCH.getNpcId(), new Item[]{new Item(2736,3), new Item(15584,1)});
            REWARDS.put(TeleportInterface.Bosses.LIGHT_SUPREME.getNpcId(), new Item[]{new Item(2736,3), new Item(10835,10000)});
            REWARDS.put(TeleportInterface.Bosses.DARK_SUPREME.getNpcId(), new Item[]{new Item(2736,3), new Item(10835,10000)});
            REWARDS.put(TeleportInterface.Bosses.FRACTITE_DEMON.getNpcId(), new Item[]{new Item(2736,3), new Item(10835,10000)});
            REWARDS.put(TeleportInterface.Bosses.INFERNAL_DEMON.getNpcId(), new Item[]{new Item(2736,3), new Item(10835,10000)});
            REWARDS.put(TeleportInterface.Bosses.PERFECT_CELL.getNpcId(), new Item[]{new Item(2736,3), new Item(10835,10000)});
            REWARDS.put(TeleportInterface.Bosses.SUPER_BUU.getNpcId(), new Item[]{new Item(10946,3), new Item(21815,2)});
            REWARDS.put(TeleportInterface.Bosses.FRIEZA.getNpcId(), new Item[]{new Item(10946,3), new Item(21815,2)});
            REWARDS.put(TeleportInterface.Bosses.GOKU.getNpcId(), new Item[]{new Item(10946,3), new Item(21815,2)});
            REWARDS.put(TeleportInterface.Bosses.GROUDON.getNpcId(), new Item[]{new Item(10946,3), new Item(21815,2)});
            REWARDS.put(TeleportInterface.Bosses.EZKEL.getNpcId(), new Item[]{new Item(10946,3), new Item(21815,2)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX.getNpcId(), new Item[]{new Item(10946,5), new Item(21815,5)});
            REWARDS.put(TeleportInterface.Bosses.EXTRA1.getNpcId(), new Item[]{new Item(2736,5), new Item(15585,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX2.getNpcId(), new Item[]{new Item(20488,1), new Item(10835,20000)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX3.getNpcId(), new Item[]{new Item(20488,1), new Item(10835,20000)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX4.getNpcId(), new Item[]{new Item(10946,5), new Item(21814,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX5.getNpcId(), new Item[]{new Item(10946,5), new Item(21815,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX6.getNpcId(), new Item[]{new Item(10946,5), new Item(21816,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX7.getNpcId(), new Item[]{new Item(10946,5), new Item(21814,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX8.getNpcId(), new Item[]{new Item(10946,5), new Item(21815,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX9.getNpcId(), new Item[]{new Item(10946,5), new Item(21816,5)});
            //BOSSES
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX10.getNpcId(), new Item[]{new Item(15003,1), new Item(15586,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX11.getNpcId(), new Item[]{new Item(20488,1), new Item(15589,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX12.getNpcId(), new Item[]{new Item(20488,1), new Item(10946,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX13.getNpcId(), new Item[]{new Item(2736,10), new Item(21816,15)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX14.getNpcId(), new Item[]{new Item(15003,1), new Item(15587,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX15.getNpcId(), new Item[]{new Item(20488,1), new Item(15588,1)});

            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX16.getNpcId(), new Item[]{new Item(20488,1), new Item(21815,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX17.getNpcId(), new Item[]{new Item(20488,1), new Item(21814,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX18.getNpcId(), new Item[]{new Item(20488,1), new Item(21816,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX19.getNpcId(), new Item[]{new Item(15004,1), new Item(3578,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX20.getNpcId(), new Item[]{new Item(20488,1), new Item(8788,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX21.getNpcId(), new Item[]{new Item(3578,1), new Item(8788,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX22.getNpcId(), new Item[]{new Item(20488,1), new Item(8788,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX23.getNpcId(), new Item[]{new Item(20488,1), new Item(21815,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX24.getNpcId(), new Item[]{new Item(20488,1), new Item(21815,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX25.getNpcId(), new Item[]{new Item(3578,1), new Item(21815,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX26.getNpcId(), new Item[]{new Item(20488,1), new Item(8788,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX27.getNpcId(), new Item[]{new Item(20488,1), new Item(21816,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX28.getNpcId(), new Item[]{new Item(13019,1), new Item(8788,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX29.getNpcId(), new Item[]{new Item(20488,1), new Item(21814,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX30.getNpcId(), new Item[]{new Item(3578,1), new Item(8788,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX31.getNpcId(), new Item[]{new Item(20488,1), new Item(21814,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX32.getNpcId(), new Item[]{new Item(20488,1), new Item(8788,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX33.getNpcId(), new Item[]{new Item(13019,1), new Item(21814,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX34.getNpcId(), new Item[]{new Item(20488,1), new Item(8788,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX35.getNpcId(), new Item[]{new Item(20488,1), new Item(21815,5)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX36.getNpcId(), new Item[]{new Item(20488,1), new Item(8788,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX37.getNpcId(), new Item[]{new Item(20491,1), new Item(20488,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX38.getNpcId(), new Item[]{new Item(20488,1), new Item(8788,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX39.getNpcId(), new Item[]{new Item(13019,1), new Item(3578,1)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX40.getNpcId(), new Item[]{new Item(20491,1), new Item(13650,1000)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX41.getNpcId(), new Item[]{new Item(20491,1), new Item(13650,1000)});
            REWARDS.put(TeleportInterface.Bosses.SUPREMENEX42.getNpcId(), new Item[]{new Item(20491,1), new Item(13650,5000)});
            REWARDS.put(595, new Item[]{new Item(20491,1), new Item(13650,5000)});
            REWARDS.put(440, new Item[]{new Item(20491,1), new Item(13650,5000)});
            REWARDS.put(438, new Item[]{new Item(20491,1), new Item(13650,5000)});
            //GLOBALS
            REWARDS.put(TeleportInterface.Bosses.VEIGAR.getNpcId(), new Item[]{new Item(20490,1), new Item(23059,1)});
            REWARDS.put(TeleportInterface.Bosses.NINETAILS.getNpcId(), new Item[]{new Item(20490,1), new Item(23059,1)});
            REWARDS.put(TeleportInterface.Bosses.MERUEM.getNpcId(), new Item[]{new Item(20490,1), new Item(23059,1)});
            REWARDS.put(TeleportInterface.Bosses.GOLDEN.getNpcId(), new Item[]{new Item(20490,1), new Item(23059,1)});
        }
    }

}