package com.ruse.net.packet.impl;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.engine.task.impl.WalkToTask;
import com.ruse.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.ruse.model.*;
import com.ruse.model.Locations.Location;
import com.ruse.model.container.impl.Equipment;
import com.ruse.model.definitions.GameObjectDefinition;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.model.input.impl.DonateToWellListener;
import com.ruse.model.input.impl.EnterAmountOfBarsToSmelt;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.clip.region.RegionClipping;
import com.ruse.world.content.CrystalChest;
import com.ruse.world.content.ItemForging;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.content.cluescrolls.OLD_ClueScrolls;
import com.ruse.world.content.holidayevents.christmas2016;
import com.ruse.world.content.minigames.impl.TreasureHunter;
import com.ruse.world.content.minigames.impl.VaultOfWar;
import com.ruse.world.content.minigames.impl.WarriorsGuild;
import com.ruse.world.content.skill.impl.cooking.Cooking;
import com.ruse.world.content.skill.impl.cooking.CookingData;
import com.ruse.world.content.skill.impl.crafting.Flax;
import com.ruse.world.content.skill.impl.crafting.Gems;
import com.ruse.world.content.skill.impl.crafting.Jewelry;
import com.ruse.world.content.skill.impl.crafting.LeatherMaking;
import com.ruse.world.content.skill.impl.firemaking.Firelighter;
import com.ruse.world.content.skill.impl.firemaking.Firemaking;
import com.ruse.world.content.skill.impl.firemaking.Logdata.logData;
import com.ruse.world.content.skill.impl.fletching.BoltData;
import com.ruse.world.content.skill.impl.fletching.Fletching;
import com.ruse.world.content.skill.impl.herblore.Crushing;
import com.ruse.world.content.skill.impl.herblore.Herblore;
import com.ruse.world.content.skill.impl.herblore.PotionCombinating;
import com.ruse.world.content.skill.impl.herblore.WeaponPoison;
import com.ruse.world.content.skill.impl.prayer.BonesOnAltar;
import com.ruse.world.content.skill.impl.prayer.Prayer;
import com.ruse.world.content.skill.impl.smithing.EquipmentMaking;
import com.ruse.world.content.skill.impl.smithing.Smelting;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.misc.CurrPouch;
import com.ruse.world.packages.mode.impl.UltimateIronman;
import com.ruse.world.packages.plus.PlusUpgrade;
import com.ruse.world.packages.plus.PlusUpgrades;

/**
 * This packet listener is called when a player 'uses' an item on another
 * entity.
 *
 * @author relex lawl
 */

public class UseItemPacketListener implements PacketListener {

    public final static int USE_ITEM = 174;
    public final static int ITEM_ON_NPC = 57;
    public final static int ITEM_ON_ITEM = 53;
    public final static int ITEM_ON_OBJECT = 192;
    public final static int ITEM_ON_GROUND_ITEM = 25;
    public static final int ITEM_ON_PLAYER = 14;

    /**
     * The PacketListener logger to debug sendInformation and print out errors.
     */
    // private final static Logger logger =
    // Logger.getLogger(UseItemPacketListener.class);
    private static void useItem(Player player, Packet packet) {
        if (player.isTeleporting() || player.getConstitution() <= 0)
            return;
        int interfaceId = packet.readLEShortA();
        int slot = packet.readShortA();
        int id = packet.readLEShort();
    }

    private static void itemOnItem(Player player, Packet packet) {
        int usedWithSlot = packet.readUnsignedShort();
        int itemUsedSlot = packet.readUnsignedShortA();
        if (usedWithSlot < 0 || itemUsedSlot < 0 || itemUsedSlot > player.getInventory().capacity()
                || usedWithSlot > player.getInventory().capacity())
            return;
        Item usedWith = player.getInventory().getItems()[usedWithSlot];
        Item itemUsedWith = player.getInventory().getItems()[itemUsedSlot];
        if (player.getRank().isDeveloper()) {
            player.getPacketSender()
                    .sendMessage("ItemOnItem - <shad=000000><col=ffffff>[<col=ff774a>"
                            + ItemDefinition.forId(itemUsedWith.getId()).getName() + ":" + itemUsedWith.getId() + ":"
                            + itemUsedWith.getAmount() + " <col=ffffff>was used on <col=4AD2FF>"
                            + ItemDefinition.forId(usedWith.getId()).getName() + ":" + usedWith.getId() + ":"
                            + usedWith.getAmount() + "<col=ffffff>]");
        }

        if (!player.getControllerManager().canUseItemOnItem(usedWith, itemUsedWith)) {
            return;
        }

        if((PlusUpgrade.getPlusUpgrade().isUpgradeable(itemUsedWith.getId()) &&
                PlusUpgrades.isMaterial(usedWith.getId())) ||
                (PlusUpgrade.getPlusUpgrade().isUpgradeable(usedWith.getId()) &&
                        PlusUpgrades.isMaterial(itemUsedWith.getId()))) {
            if(PlusUpgrades.isMaterial(itemUsedWith.getId())){
                if(PlusUpgrade.getPlusUpgrade().upgradeGear(player, usedWith)){
                    player.sendMessage("You successfully upgrade your gear.");
                }
            } else if (PlusUpgrade.getPlusUpgrade().upgradeGear(player, itemUsedWith)) {
                player.sendMessage("You successfully upgrade your gear.");
            }
            return;
        }

        switch (itemUsedWith.getId()) {
            case 22108 -> {
                if(player.getPouch().handleItemOnItem(itemUsedWith)){
                    return;
                }
                return;
            }
            case 9003 -> {
                if (itemUsedWith.getId() == 989) {
                    CrystalChest.sendRewardInterface(player);
                }
                return;
            }
        }

        switch (usedWith.getId()) {
            case 22108 -> {
                if(player.getPouch().handleItemOnItem(itemUsedWith)){
                    return;
                }
            }
            case 9003 -> {
                if (itemUsedWith.getId() == 989) {
                    CrystalChest.sendRewardInterface(player);
                }
                return;
            }
        }

//        if (usedWith.getId() == TreasureHunter.KEY_1 || usedWith.getId() == TreasureHunter.KEY_2 | usedWith.getId() == TreasureHunter.KEY_3 || usedWith.getId() == TreasureHunter.KEY_4) {
//            TreasureHunter.combineKeys(player, usedWith, itemUsedWith);
//            return;
//        }
//
//        if (usedWith.getId() == 22106) {
//            Item[] requirements = new Item[]{new Item(22106, 1),
//                    new Item(19000, 10_000), new Item(5022, 10_000_000),
//                    new Item(ItemDefinition.TOKEN_ID, 10_000_000)};
//            if (player.getInventory().containsAll(requirements) && itemUsedWith.getId() == 19_000) {
//                player.getInventory().deleteItemSet(requirements);
//                if (Misc.exclusiveRandom(1, 100) <= 25) {
//                    player.getInventory().add(22107, 1);
//                    player.sendMessage("@red@Congratulations, you've made Lucifer's pet!");
//                    String msg = "@blu@<img=5>[CREATION]<img=5>@red@ " + player.getUsername() + " has created Lucifer's pet!";
//                    World.sendMessage(msg);
//                }
//                return;
//            } else if (player.getInventory().containsAll(requirements = new Item[]{new Item(22106, 2), new Item(18885, 1)})
//                    && itemUsedWith.getId() == requirements[1].getId()) {
//                player.getInventory().deleteItemSet(requirements);
//                player.getInventory().add(22104, 1);
//                player.sendMessage("@red@Congratulations, you've made Lucifer's gloves!");
//                String msg = "@blu@<img=5>[CREATION]<img=5>@red@ " + player.getUsername() + " has created Lucifer's gloves!";
//                World.sendMessage(msg);
//                return;
//            } else if (player.getInventory().containsAll(requirements = new Item[]{new Item(22106, 2), new Item(18887, 1),})
//                    && itemUsedWith.getId() == requirements[1].getId()) {
//                player.getInventory().deleteItemSet(requirements);
//                player.getInventory().add(22103, 1);
//                player.sendMessage("@red@Congratulations, you've made Lucifer's boots!");
//                String msg = "@blu@<img=5>[CREATION]<img=5>@red@ " + player.getUsername() + " has created Lucifer's boots!";
//                World.sendMessage(msg);
//                return;
//            } else if (player.getInventory().containsAll(requirements = new Item[]{new Item(22106, 3), new Item(4684, 1),})
//                    && itemUsedWith.getId() == requirements[1].getId()) {
//                player.getInventory().deleteItemSet(requirements);
//                player.getInventory().add(22100, 1);
//                player.sendMessage("@red@Congratulations, you've made Lucifer's head!");
//                String msg = "@blu@<img=5>[CREATION]<img=5>@red@ " + player.getUsername() + " has created Lucifer's head!";
//                World.sendMessage(msg);
//                return;
//            } else if (player.getInventory().containsAll(requirements = new Item[]{new Item(22106, 3), new Item(4685, 1),})
//                    && itemUsedWith.getId() == requirements[1].getId()) {
//                player.getInventory().deleteItemSet(requirements);
//                player.getInventory().add(22101, 1);
//                player.sendMessage("@red@Congratulations, you've made Lucifer's body!");
//                String msg = "@blu@<img=5>[CREATION]<img=5>@red@ " + player.getUsername() + " has created Lucifer's body!";
//                World.sendMessage(msg);
//                return;
//            } else if (player.getInventory().containsAll(requirements = new Item[]{new Item(22106, 3), new Item(4686, 1),})
//                    && itemUsedWith.getId() == requirements[1].getId()) {
//                player.getInventory().deleteItemSet(requirements);
//                player.getInventory().add(22102, 1);
//                player.sendMessage("@red@Congratulations, you've made Lucifer's legs!");
//                String msg = "@blu@<img=5>[CREATION]<img=5>@red@ " + player.getUsername() + " has created Lucifer's legs!";
//                World.sendMessage(msg);
//                return;
//            } else if (player.getInventory().containsAll(requirements = new Item[]{new Item(22106, 3), new Item(20400, 1),})
//                    && itemUsedWith.getId() == requirements[1].getId()) {
//                player.getInventory().deleteItemSet(requirements);
//                player.getInventory().add(22105, 1);
//                player.sendMessage("@red@Congratulations, you've made Lucifer's wings!");
//                String msg = "@blu@<img=5>[CREATION]<img=5>@red@ " + player.getUsername() + " has created Lucifer's wings!";
//                World.sendMessage(msg);
//                return;
//            }
//        }
//
//        if ((usedWith.getId() == 23004 || usedWith.getId() == 23005 || usedWith.getId() == 23006 || usedWith.getId() == 23007)
//                && (itemUsedWith.getId() == 23004 || itemUsedWith.getId() == 23005 || itemUsedWith.getId() == 23006 || itemUsedWith.getId() == 23007)) {
//            Item[] requirements = new Item[]{new Item(23004, 100),
//                    new Item(23005, 100), new Item(23006, 100),
//                    new Item(23007, 100)};
//            if (player.getInventory().containsAll(requirements)) {
//                player.getInventory().deleteItemSet(requirements);
//                player.getInventory().add(23018, 1);
//                player.sendMessage("@red@Congratulations, you've made an Armoured Bunny pet!");
//                String msg = "@blu@<img=5>[CREATION]<img=5>@red@ " + player.getUsername() + " has created a Armoured Bunny Pet!";
//                World.sendMessage(msg);
//                return;
//            }
//        }
//
//
//        if (usedWith.getId() == 28 && itemUsedWith.getId() == 29 || usedWith.getId() == 29 && itemUsedWith.getId() == 28) {
//            int rewards2[][] = {
//                    {14733, 14732, 14734, 19111, 11137, 3907, 18351, 3140, 18353, 18355, 18357, 15501, 15272, 2503,
//                            10499, 3805, 6326, 861, 1163, 1201, 6111, 544, 542, 5574, 5575, 5576, 1215, 3105, 13734,
//                            7400, 11118}, // Common, 0
//                    {18686, 13996, 13913, 13919, 18799, 18834, 18801, 18800, 5095, 19140, 19139, 19138, 4411, 19887,
//                            22078, 19123, 11617, 3909, 3318, 15501, 11133, 15126, 16043, 6500, 10828, 3751, 3753, 10589,
//                            10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528, 7158, 4153, 6, 8, 10, 12, 4675, 6914,
//                            6889}, // Uncommon, 1
//                    {6739, 11728, 6585, 15259, 15332, 2579, 6920, 7630, 6922, 13879, 13883, 15241, 15243, 3321, 3391,
//                            3322, 3320, 3318, 18360} // Rare, 2
//            };
//            double numGen = Math.random();
//            /**
//             * Chances 50% chance of Common Items - cheap gear, high-end consumables 40%
//             * chance of Uncommon Items - various high-end coin-bought gear 10% chance of
//             * Rare Items - Highest-end coin-bought gear, some voting-point/pk-point
//             * equipment
//             */
//
//            int rewardGrade = numGen >= 0.5 ? 0 : numGen >= 0.20 ? 1 : 2;
//            int rewardPos = Misc.getRandom(rewards2[rewardGrade].length - 1);
//            player.getInventory().delete(28, 1);
//            player.getInventory().delete(29, 1);
//            player.getInventory().add(rewards2[rewardGrade][rewardPos], 1).refreshItems();
//            player.getPacketSender().sendMessage("You unlocked the slayer chest");
//
//
//            return;
//
//        }
//
//        /* Clue Handler */
//        if (itemUsedWith.getId() == 9003
//                && (usedWith.getId() == 2724 || ItemDefinition.forId(usedWith.getId()).getName().contains("Clue"))) {
//            OLD_ClueScrolls.sendDropTableInterface(player);
//        }
//        for (int i = 0; i < Firelighter.values().length; i++) {
//            if (usedWith.getId() == Firelighter.values()[i].getLighterId()
//                    || itemUsedWith.getId() == Firelighter.values()[i].getLighterId()) {
//                Firelighter.handleFirelighter(player, i);
//                break;
//            }
//        }
//
//        for (int i = 0; i < Crushing.values().length; i++) {
//            if (usedWith.getId() == Crushing.values()[i].getInput()
//                    || itemUsedWith.getId() == Crushing.values()[i].getInput()) {
//                Crushing.handleCrushing(player, i);
//                break;
//            }
//        }
//
//        for (int i = 0; i < BoltData.values().length; i++) {
//            if (usedWith.getId() == BoltData.values()[i].getTip()
//                    || itemUsedWith.getId() == BoltData.values()[i].getTip()) {
//                Fletching.tipBolt(player, BoltData.values()[i].getTip());
//                break;
//            }
//        }
//
//        WeaponPoison.execute(player, itemUsedWith.getId(), usedWith.getId());
//        if (itemUsedWith.getId() == 590 || usedWith.getId() == 590)
//            Firemaking.lightFire(player, itemUsedWith.getId() == 590 ? usedWith.getId() : itemUsedWith.getId(), false,
//                    1);
//        if (itemUsedWith.getDefinition().getName().contains("(") && usedWith.getDefinition().getName().contains("("))
//            PotionCombinating.combinePotion(player, usedWith.getId(), itemUsedWith.getId());
//        if (usedWith.getId() == Herblore.VIAL || itemUsedWith.getId() == Herblore.VIAL) {
//            if (Herblore.makeUnfinishedPotion(player, usedWith.getId())
//                    || Herblore.makeUnfinishedPotion(player, itemUsedWith.getId()))
//                return;
//        }
//        if (Herblore.finishPotion(player, usedWith.getId(), itemUsedWith.getId())
//                || Herblore.finishPotion(player, itemUsedWith.getId(), usedWith.getId()))
//            return;
//        if (usedWith.getId() == 12934 || itemUsedWith.getId() == 12926 || usedWith.getId() == 12926
//                || itemUsedWith.getId() == 12934) {
//            ToxicBlowpipe.loadPipe(player);
//        }
//        if (usedWith.getId() == 5012 && itemUsedWith.getId() == 10949 && itemUsedWith.getAmount() >= 3)
//            World.sendMessage("<img=16> <shad=1>[" + player.getUsername() + "] @cya@Turned his Supreme twisted bow into a Light Twisted Bow!");
//
//        if (usedWith.getId() == 10949 && itemUsedWith.getId() == 5012 && usedWith.getAmount() >= 3)
//            World.sendMessage("<img=16> <shad=1>[" + player.getUsername() + "] @cya@Turned his Supreme twisted bow into a Light Twisted Bow!");
//
//        if (usedWith.getId() == 946 || itemUsedWith.getId() == 946)
//            Fletching.openSelection(player, usedWith.getId() == 946 ? itemUsedWith.getId() : usedWith.getId());
//        if (usedWith.getId() == 1777 || itemUsedWith.getId() == 1777)
//            Fletching.openBowStringSelection(player,
//                    usedWith.getId() == 1777 ? itemUsedWith.getId() : usedWith.getId());
//        if (usedWith.getId() == 53 || itemUsedWith.getId() == 53 || usedWith.getId() == 52
//                || itemUsedWith.getId() == 52)
//            Fletching.makeArrows(player, usedWith.getId(), itemUsedWith.getId());
//        if (itemUsedWith.getId() == 1755 || usedWith.getId() == 1755)
//            Gems.selectionInterface(player, usedWith.getId() == 1755 ? itemUsedWith.getId() : usedWith.getId());
//        if (itemUsedWith.getId() == 1755 || usedWith.getId() == 1755)
//            Fletching.openGemCrushingInterface(player,
//                    usedWith.getId() == 1755 ? itemUsedWith.getId() : usedWith.getId());
//
//        if (usedWith.getId() == 1733 || itemUsedWith.getId() == 1733)
//            LeatherMaking.craftLeatherDialogue(player, usedWith.getId(), itemUsedWith.getId());
//        Herblore.handleSpecialPotion(player, itemUsedWith.getId(), usedWith.getId());
//        if (itemUsedWith.getId() == 1759 || usedWith.getId() == 1759) {
//            Jewelry.stringAmulet(player, itemUsedWith.getId(), usedWith.getId());
//        }
//        ItemForging.forgeItem(player, itemUsedWith.getId(), usedWith.getId());
    }

    @SuppressWarnings("unused")
    private static void itemOnObject(Player player, Packet packet) {
        @SuppressWarnings("unused")
        int interfaceType = packet.readShort();
        final int objectId = packet.readUnsignedShort();
        final int objectY = packet.readLEShortA();
        final int itemSlot = packet.readLEShort();
        final int objectX = packet.readLEShortA();
        final int itemId = packet.readShort();

        if (itemSlot < 0 || itemSlot > player.getInventory().capacity())
            return;
        final Item item = player.getInventory().getItems()[itemSlot];
        if (item == null)
            return;
        final GameObject gameObject = new GameObject(objectId,
                new Position(objectX, objectY, player.getPosition().getZ()));
        if (objectId > 0 && objectId != 6 && !RegionClipping.objectExists(gameObject)) {
            // player.getPacketSender().sendMessage("An error occured. Error code:
            // "+id).sendMessage("Please report the error to a staff member.");
            return;
        }
        player.setInteractingObject(gameObject);
        if (player.getRank().isDeveloper()) {
            if (GameObjectDefinition.forId(gameObject.getId()) == null || GameObjectDefinition.forId(gameObject.getId()).getName() == null) {
                player.getPacketSender()
                        .sendMessage("ItemOnObject - <shad=000000><col=ffffff>[<col=ff774a>"
                                + ItemDefinition.forId(itemId).getName() + ":" + itemId
                                + " <col=ffffff>was used on <col=4AD2FF>" + gameObject.getId()
                                + "<col=ffffff>] @red@(null obj. def)");
            } else {
                player.getPacketSender()
                        .sendMessage("ItemOnObject - <shad=000000><col=ffffff>[<col=ff774a>"
                                + ItemDefinition.forId(itemId).getName() + ":" + itemId
                                + " <col=ffffff>was used on <col=4AD2FF>"
                                + GameObjectDefinition.forId(gameObject.getId()).getName() + ":" + gameObject.getId()
                                + "<col=ffffff>]");
            }
        }
        player.setWalkToTask(new WalkToTask(player, gameObject.getPosition().copy(), gameObject.getSize(),
                () -> {


                    if (!player.getControllerManager().handleItemOnObject(gameObject, item)) {
                        return;
                    }

                    if (Prayer.isBone(itemId) && (objectId == 409 || objectId == 24343 || objectId == 13192)) {
                        BonesOnAltar.openInterface(player, itemId);
                        return;
                    }

                    if(objectId == 63003){
                        player.setInputHandling(new DonateToWellListener(player, item.getId()));
                        player.getPacketSender().sendEnterAmountPrompt(
                                "How many " + ItemDefinition.forId(item.getId()).getName() + "s would you like to donate?");
                        return;
                    }


                    if (player.getMode() instanceof UltimateIronman) { // UIM can use any noted item on a bank
                        // booth to instantly unnote it to
                        // fill all your free inventory
                        // spaces
                        if (GameObjectDefinition.forId(objectId) != null) {
                            GameObjectDefinition def = GameObjectDefinition.forId(objectId);
                            if (def.name != null && def.name.toLowerCase().contains("bank") && def.actions != null
                                    && def.actions[0] != null && def.actions[0].toLowerCase().contains("use")) {
                                ItemDefinition def1 = ItemDefinition.forId(itemId);
                                ItemDefinition def2;
                                int newId = def1.isNoted() ? itemId - 1 : itemId + 1;
                                def2 = ItemDefinition.forId(newId);
                                if (def2 != null && def1.getName().equals(def2.getName())) {
                                    int amt = player.getInventory().getAmount(itemId);
                                    if (!def2.isNoted()) {
                                        if (amt > player.getInventory().getFreeSlots())
                                            amt = player.getInventory().getFreeSlots();
                                    }
                                    if (amt == 0) {
                                        player.getPacketSender().sendMessage(
                                                "You do not have enough space in your inventory to do that.");
                                        return;
                                    }
                                    player.getInventory().delete(itemId, amt).add(newId, amt);

                                } else {
                                    player.getPacketSender().sendMessage("You cannot do this with that item.");
                                }
                                return;
                            }
                        }
                    }
                    switch (objectId) {


                    }
                }));
    }

    private static void itemOnNpc(final Player player, Packet packet) {
        final int id = packet.readShortA();
        final int index = packet.readShortA();
        final int slot = packet.readLEShort();
        if (index < 0 || index > World.getNpcs().capacity()) {
            return;
        }
        if (slot < 0 || slot > player.getInventory().getItems().length) {
            return;
        }
        NPC npc = World.getNpcs().get(index);
        if (npc == null) {
            return;
        }
        Item usedItem = player.getInventory().forSlot(slot);
        if (usedItem == null) {
            return;
        }
        if (player.getInventory().getItems()[slot].getId() != id) {
            return;
        }
        if (player.getRank().isDeveloper()) {
            player.getPacketSender()
                    .sendMessage("ItemOnNPC - <shad=000000><col=ffffff>[<col=ff774a>"
                            + ItemDefinition.forId(id).getName() + ":" + id + " <col=ffffff>was used on <col=4AD2FF>"
                            + npc.getDefinition().getName() + ":" + npc.getId() + "<col=ffffff>]");
        }
        if (!player.getControllerManager().processItemOnNPC(npc, usedItem)) {
            return;
        }

        if (player.getMode() instanceof UltimateIronman) { // UIM can use any noted item on a bank booth to
            // instantly unnote it to fill all your free
            // inventory spaces
            if (npc.getDefinition() != null) {
                NpcDefinition def = npc.getDefinition();
                if (def.getName() != null && def.getName().toLowerCase().contains("banker")) {
                    ItemDefinition def1 = ItemDefinition.forId(id);
                    ItemDefinition def2;
                    int newId = def1.isNoted() ? id - 1 : id + 1;
                    def2 = ItemDefinition.forId(newId);
                    if (def2 != null && def1.getName().equals(def2.getName())) {
                        int amt = player.getInventory().getAmount(id);
                        if (!def2.isNoted()) {
                            if (amt > player.getInventory().getFreeSlots())
                                amt = player.getInventory().getFreeSlots();
                        }
                        if (amt == 0) {
                            player.getPacketSender()
                                    .sendMessage("You do not have enough space in your inventory to do that.");
                            return;
                        }
                        player.getInventory().delete(id, amt).add(newId, amt);

                    } else {
                        player.getPacketSender().sendMessage("You cannot do this with that item.");
                    }
                    return;
                }
            }
        }
        switch (npc.getId()) {
            case VaultOfWar.GLOVES_NPC:
                VaultOfWar.useGlovesOnNPC(player, new Item(id));
                break;


        }
        switch (id) {


            case 9003:
                if (player.getLastTomed() != npc.getId()) {
                    player.getPacketSender()
                            .sendMessage(NpcDefinition.forId(npc.getId()).getName() + " has been scanned to your tome.");
                    if (npc.getId() == 1158) {
                        player.setLastTomed(1160);
                        break;
                    }
                }
                player.setLastTomed(npc.getId());
                break;
            case 1907:
                if (npc.getId() == 1552 && christmas2016.isChristmas()) {
                    if (player.getChristmas2016() < 5) {
                        player.getPacketSender().sendMessage("I should do this after giving the Reindeer their runes.");
                        return;
                    } else if (player.getInventory().getAmount(1907) >= 1) {
                        player.setPositionToFace(npc.getPosition());
                        player.performAnimation(new Animation(4540));
                        player.getInventory().delete(1907, 100);
                        player.setchristmas2016(6);
                        player.getPacketSender().sendMessage("You give Santa the " + ItemDefinition.forId(1907).getName()
                                + ". I should speak with him.");
                    }

                }
                break;
            case 3550:// clue
                boolean clue = OLD_ClueScrolls.handleNpcUse(player, npc.getId());
                if (clue) {
                    player.getPacketSender().sendMessage("You manage to continue your clue..");
                } else {
                    player.getPacketSender().sendMessage("Nothing interesting happens.");
                }
                break;
            case 4837:
                if (NpcDefinition.forId(npc.getId()).getName().contains("ark wizar")) {
                    TaskManager.submit(new Task(1, player, true) {
                        int tick = 0;

                        @Override
                        public void execute() {
                            if (tick >= 8) {
                                stop();
                            }
                            switch (tick) {
                                case 0:
                                    player.getInventory().delete(new Item(4837, 1));
                                    break;
                                case 1:
                                    player.forceChat("Can I have your autograph?");
                                    break;
                                case 3:
                                    npc.forceChat("Yea noob lol here u go");
                                    break;
                                case 4:
                                    npc.performAnimation(new Animation(1249));
                                    break;
                                case 7:
                                    player.getInventory().add(new Item(22040, 1));
                                    player.getPacketSender()
                                            .sendMessage("The Dark Wizard signs your Necromancy Book, transforming it.");
                                    break;
                            }
                            tick++;
                        }
                    });
                }
                break;
        }
        for (int i = 0; i < logData.values().length; i++) {
            if (logData.values()[i].getLogId() == id) {
                if (npc.getId() == 7377) {
                    if (player.getSummoning().getFamiliar() == null) {
                        player.getPacketSender().sendMessage("That isn't your familiar!");
                        return;
                    }
                    if (player.getSummoning().getFamiliar().getSummonNpc().getId() != 7377) {
                        player.getPacketSender().sendMessage("You must have your own Pyrefiend to use that effect.");
                        return;
                    }
                    Firemaking.lightFire(player, id, false, 1);
                }
                break;
            }
        }
    }

    @SuppressWarnings("unused")
    private static void itemOnGroundItem(Player player) {
        player.getPacketSender().sendMessage("Nothing interesting happens.");
        // // System.out.println("itemongrounditem");
    }

    @SuppressWarnings("unused")
    private static void itemOnPlayer(Player player, Packet packet) {
        int interfaceId = packet.readUnsignedShortA();
        int targetIndex = packet.readUnsignedShort();
        int itemId = packet.readUnsignedShort();
        int slot = packet.readLEShort();
        if (slot < 0 || slot > player.getInventory().capacity() || targetIndex > World.getPlayers().capacity())
            return;
        Player target = World.getPlayers().get(targetIndex);
        if (target == null)
            return;

        if (!player.getControllerManager().processItemOnPlayer(target, itemId, slot)) {
            return;
        }

        switch (itemId) {
//            case 23057, 23058, 23059, 23060, 3686 -> {
//                //   System.out.println("1");
//                boolean ironman = player.getGameMode().isIronman();
//                boolean targetIronman = target.getGameMode().isIronman();
//                if (target.getGameMode() == GameMode.GROUP_IRONMAN) {
//                    player.sendMessage("You cannot give to group ironmen players.");
//                    return;
//                }
//                if (ironman) {
//                    player.sendMessage("Ironman can't give these away.");
//                    return;
//                }
//                //  System.out.println("2");
//               /* if (!ironman && !targetIronman) { //if same ip
//                    if (!player.getHostAddress().equals(target.getHostAddress())) {
//                        player.sendMessage("You cannot give bonds to other mains unless its your own!");
//                        return;
//                    }
//                }*/
//                // System.out.println("3");
//                //if (ironman && !targetIronman || !ironman && targetIronman) {
//                //    System.out.println("f");
//                if (target.getInventory().isFull()) {
//                    player.sendMessage("This player's inventory is full.");
//                } else if (player.getInventory().contains(itemId)) {
//                    player.getInventory().delete(itemId, 1);
//                    target.getInventory().add(itemId, 1);
//                    player.sendMessage("You gave " + target.getUsername() + " a " + ItemDefinition.forId(itemId).getName());
//                    target.sendMessage("You received a " + ItemDefinition.forId(itemId).getName() + " from " + target.getUsername());
//                } else {
//                    player.sendMessage("You need a bond to perform this action!");
//                }
//            }
            //}
            case 962 -> {
                if (!player.getInventory().contains(962) || player.getRank().isDeveloper())
                    return;
                player.setPositionToFace(target.getPosition());
                player.performGraphic(new Graphic(1006));
                player.performAnimation(new Animation(451));
                player.getPacketSender().sendMessage("You pull the Christmas cracker...");
                target.getPacketSender().sendMessage("" + player.getUsername() + " pulls a Christmas cracker on you..");
                player.getInventory().delete(962, 1);
                player.getPacketSender().sendMessage("The cracker explodes and you receive a Party hat!");
                int phat = 1038 + Misc.getRandom(10);
                player.getInventory().add(phat, 1);
                target.getPacketSender().sendMessage("" + player.getUsername() + " has received a Party hat!");
                PlayerLogs.log(player.getUsername(), "Opened a cracker containing a " + ItemDefinition.forId(phat).getName()
                        + " on " + target.getUsername());
            }
//            case 15707 -> {
//                Player partyDung = World.getPlayers().get(targetIndex);
//                if (player.getLocation() != Location.DUNGEONEERING || player.isTeleporting()) {
//                    player.getPacketSender().sendMessage("You're not in Daemonheim");
//                    return;
//                }
//                if (partyDung.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
//                    player.getPacketSender().sendMessage("That player is already in a party.");
//                    return;
//                }
//                if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() == null) {
//                    player.getPacketSender().sendMessage("You're not in a party!");
//                    return;
//                }
//                if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner() != player) {
//                    player.getPacketSender().sendMessage("Only the party leader can invite other players.");
//                    return;
//                }
//                if (player.busy()) {
//                    player.getPacketSender().sendMessage("You're busy and can't invite anyone.");
//                    return;
//                }
//                if (partyDung.busy()) {
//                    player.getPacketSender().sendMessage(partyDung.getUsername() + " is too busy to get your invite.");
//                    return;
//                }
//                DialogueManager.start(partyDung, new DungPartyInvitation(player, partyDung));
//                player.getPacketSender().sendMessage("An invitation has been sent to " + partyDung.getUsername());
//            }
//            case 4566 -> player.performAnimation(new Animation(451));
            case 4155 -> player.getSlayer().handleSlayerTask(player, 1);
        }
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getConstitution() <= 0)
            return;
        switch (packet.getOpcode()) {
            case ITEM_ON_ITEM -> itemOnItem(player, packet);
            case USE_ITEM -> useItem(player, packet);
            case ITEM_ON_OBJECT -> itemOnObject(player, packet);
            case ITEM_ON_GROUND_ITEM -> itemOnGroundItem(player);
            case ITEM_ON_NPC -> itemOnNpc(player, packet);
            case ITEM_ON_PLAYER -> itemOnPlayer(player, packet);
        }
    }
}
