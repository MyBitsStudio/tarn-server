package com.ruse.net.packet.impl;

import com.ruse.GameSettings;
import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.engine.task.impl.WalkToTask;
import com.ruse.model.*;
import com.ruse.model.Locations.Location;
import com.ruse.model.container.impl.Equipment;
import com.ruse.model.definitions.GameObjectDefinition;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.input.impl.EnterAmountOfLogsToAdd;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.util.Misc;
import com.ruse.util.RandomUtility;
import com.ruse.world.World;
import com.ruse.world.clip.region.RegionClipping;
import com.ruse.world.content.*;
import com.ruse.world.content.combat.magic.Autocasting;
import com.ruse.world.packages.combat.prayer.CurseHandler;
import com.ruse.world.packages.combat.prayer.PrayerHandler;
import com.ruse.world.content.combat.range.DwarfMultiCannon;
import com.ruse.world.content.combat.weapon.CombatSpecial;
import com.ruse.world.content.grandexchange.GrandExchange;
import com.ruse.world.content.holidayevents.christmas2016;
import com.ruse.world.content.holidayevents.easter2017data;
import com.ruse.world.content.tbdminigame.Lobby;
import com.ruse.world.packages.chests.ChestHandler;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.dialogue.impl.tower.NextLevel;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.content.minigames.impl.*;
import com.ruse.world.content.minigames.impl.Dueling.DuelRule;
import com.ruse.world.content.minigames.impl.dungeoneering.DungeoneeringParty;
import com.ruse.world.content.portal.portal;
import com.ruse.world.content.skill.impl.construction.Construction;
import com.ruse.world.content.skill.impl.construction.ConstructionActions;
import com.ruse.world.content.skill.impl.crafting.Flax;
import com.ruse.world.content.skill.impl.crafting.Jewelry;
import com.ruse.world.content.skill.impl.fishing.Fishing;
import com.ruse.world.content.skill.impl.fishing.Fishing.Spot;
import com.ruse.world.content.skill.impl.hunter.Hunter;
import com.ruse.world.content.skill.impl.hunter.PuroPuro;
import com.ruse.world.content.skill.impl.old_dungeoneering.Dungeoneering;
import com.ruse.world.content.skill.impl.smithing.EquipmentMaking;
import com.ruse.world.content.skill.impl.smithing.Smelting;
import com.ruse.world.content.skill.impl.thieving.Stalls;
import com.ruse.world.content.skill.impl.woodcutting.WoodcuttingData;
import com.ruse.world.content.skill.impl.woodcutting.WoodcuttingData.Hatchet;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportLocations;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.impl.GroupIronman;
import com.ruse.world.packages.skills.S_Skills;
import com.ruse.world.packages.skills.mining.Miner;
import com.ruse.world.packages.skills.mining.MiningProps;
import com.ruse.world.packages.tower.TarnTower;
import org.jetbrains.annotations.NotNull;

import static com.ruse.world.packages.combat.prayer.PrayerHandler.startDrain;

/**
 * This packet listener is called when a player clicked on a game object.
 *
 * @author relex lawl
 */

public class ObjectActionPacketListener implements PacketListener {

    public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70, FOURTH_CLICK = 234,
            FIFTH_CLICK = 228;

    /**
     * The PacketListener logger to debug sendInformation and print out errors.
     */
    // private final static Logger logger =
    // Logger.getLogger(ObjectActionPacketListener.class);
    private static void firstClick(final Player player, Packet packet) {
        final int x = packet.readLEShortA();
        final int id = packet.readUnsignedShort();
        final int y = packet.readUnsignedShortA();
        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (id > 0 && id != 6 && !RegionClipping.objectExists(gameObject)) {
            if (player.getRank().isDeveloper()) {
                player.getPacketSender().sendMessage("A interaction error occured. Error code: " + id);
            } else {
                player.getPacketSender().sendMessage("Nothing interesting happens.");
            }
            return;
        }
        int distanceX = (player.getPosition().getX() - position.getX());
        int distanceY = (player.getPosition().getY() - position.getY());
        if (distanceX < 0)
            distanceX = -(distanceX);
        if (distanceY < 0)
            distanceY = -(distanceY);
        int size = distanceX > distanceY ? GameObjectDefinition.forId(id).getSizeX()
                : GameObjectDefinition.forId(id).getSizeY();
        if (size <= 0)
            size = 1;
        gameObject.setSize(size);
        if (player.getMovementQueue().isLockMovement())
            return;
        if (player.getRank().isDeveloper())
            player.getPacketSender()
                    .sendMessage("First click object id; [id, position] : [" + id + ", " + position.toString() + "]");
        player.setInteractingObject(gameObject)
                .setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), () -> {
                    player.setPositionToFace(gameObject.getPosition());

                    if (MiningProps.Rocks.forId(gameObject.getId()) != null) {
                        Miner.startMining(player, gameObject);
                        return;
                    }

                    if (!player.getControllerManager().processObjectClick1(gameObject)) {
                        return;
                    }

                    if(player.getInstance() != null){
                        if(player.getInstance().handleObjectClick(player, gameObject, 1)){
                            return;
                        }
                    }

                    if(ChestHandler.openChest(player, gameObject)){
                        return;
                    }

                    if(World.handler.handleObjectClick(player, gameObject.getId(), 1)){
                        return;
                    }

//                    if (player.getFarming().click(player, x, y, 1))
//                        return;
//                    if (Runecrafting.runecraftingAltar(player, gameObject.getId())) {
//                        RunecraftingData.RuneData rune = RunecraftingData.RuneData.forId(gameObject.getId());
//                        if (rune == null)
//                            return;
//                        Runecrafting.craftRunes(player, rune);
//                        return;
//                    }
//                    if (Agility.handleObject(player, gameObject)) {
//                        return;
//                    }
//                    if (player.getPosition().getRegionId() == 7758) {//vod
//                        player.vod.handleObject(gameObject);
//                        return;
//                    }
//                    if (gameObject.getId() == HallsOfValor.CHEST_ID) {
//                        HallsOfValor.handleObject(player);
//                    }
//                    if (Barrows.handleObject(player, gameObject)) {
//                        return;
//                    }
//                    if (player.getLocation() != null && player.getLocation() == Location.WILDERNESS
//                            && WildernessObelisks.handleObelisk(gameObject.getId())) {
//                        return;
//                    }
                    if (ConstructionActions.handleFirstObjectClick(player, gameObject)) {
                        return;
                    }

                    if (gameObject.getDefinition() != null && gameObject.getDefinition().getName() != null) {
                        if (gameObject.getDefinition().getName().toLowerCase().contains("bank")) {
                            if (player.getMode() instanceof GroupIronman
                                    && player.getIronmanGroup() != null) {
//                                DialogueManager.start(player, 8002);
//                                player.setDialogueActionId(8002);
                            } else {
                                player.getBank(player.getCurrentBankTab()).open();
                            }
                            return;
                        }
                    }
                    switch(id){
                        case 2912, 2913 ->{
                            handleDoor(player, gameObject);
                            return;
                        }

                        case 63003 -> {
                            player.sendMessage("VIP Boss : "+GlobalBossManager.getInstance().getProgress("VIP")+"/50");
                            return;
                        }

                        case 16116 -> {
                            TarnTower.sendInterface(player);
                            return;
                        }
                        case 16686 -> {
                            TarnTower.leave(player);
                            return;
                        }
                    }
                    switch (id) {
                        case 4469:
                            Lobby.getInstance().barrierClick(player);
                            break;
                        case 621:

                            break;

                        case 13291:
                        case 20040:
                            player.sendMessage("This is being reworked! Check back soon!");
                            //player.loadUpgradeInterface().open();
                            break;
                        case 26791:
                            //player.sendMessage("This is being reworked! Check back soon!");
                            player.getCrafting().open();
                            break;
                        case 41205:
                            player.sendMessage("Coming soon...");
                            ///player.getRaidsInterface().openInterface(RaidsInterface.Raids.FURY_RAIDS);
                            break;
                        case 12260:

                            CurseHandler.deactivateAll(player);

//                            if (player.getLocation() == Location.ZOMBIE_LOBBY) {
//                                if (player.getZombieParty() != null) {
//                                    if (player.getZombieParty().getOwner().equals(player)) {
//                                        player.setDialogueActionId(2012);
//                                        DialogueManager.start(player, 2012);
//                                    } else {
//                                        player.sendMessage("Only the party leader can start the Raids [1]");
//                                    }
//                                } else {
//                                    player.sendMessage("You must be in a party to start the Raids [1]");
//                                }
//                            }
                            break;
                        case 10251:

                            CurseHandler.deactivateAll(player);

//                            if (player.getLocation() == Location.AURA_LOBBY) {
//                                if (player.getAuraParty() != null) {
//                                    if (player.getAuraParty().getOwner().equals(player)) {
//                                        AuraRaids.start(player.getAuraParty());
//                                        //player.setDialogueActionId(2012);
//                                       // DialogueManager.start(player, 2012);
//                                    } else {
//                                        player.sendMessage("Only the party leader can start the Raids [2]");
//                                    }
//                                } else {
//                                    player.sendMessage("You must be in a party to start the Raids [2]");
//                                }
//                            }
                            break;
                        case 10014:
                            DungeoneeringParty party1 = player.getMinigameAttributes().getDungeoneeringAttributes().getParty();
//                            if (party1 != null) {
//                                if (!party1.getOwner().equals(player)) {
//                                    player.sendMessage("Only the party leader can start the dungeon.");
//                                } else {
//                                    if (com.ruse.world.content.minigames.impl.dungeoneering.Dungeoneering.Companion.ready(party1)) {
//                                        com.ruse.world.content.minigames.impl.dungeoneering.Dungeoneering dung = new com.ruse.world.content.minigames.impl.dungeoneering.Dungeoneering(party1);
//                                        dung.startDungeon();
//                                    } else {
//                                        party1.sendMessage("Your party is not ready.");
//                                    }
//                                }
//                            } else {
//                                player.sendMessage("Please join a party before entering a dungeon.");
//                            }
                            break;

                        case 5222:
                            if (player.getLocation() == Location.KEEPERS_OF_LIGHT_LOBBY) {
                                if (player.getPosition().getY() < 5030) {
                                    KeepersOfLight.insertWaiting(player);
                                } else {
                                    KeepersOfLight.removeWaiting(player, true);
                                }
                            }
                            break;


                        case 31424:
//                            TeleportHandler.teleportPlayer(player, new Position(2654, 2796),
//                                    player.getSpellbook().getTeleportType());
                            player.sendMessage("This is being changed! Check back soon!");
                            break;

                        case 4388:
                            if(player.getInstance() != null){
                                if(!player.getInstance().canLeave()){
                                    return;
                                }
                                player.getInstance().remove(player);
                            }
                            break;
                        case 2469:
                            TeleportHandler.teleportPlayer(player, player.getPosition().setZ(player.getPosition().getZ() + 4), TeleportType.NORMAL);
                            break;


                        case 16958:
                            CurseHandler.deactivateAll(player);
                            break;
                       /* case 52601:
                            Stalls.stealFromAFKStall(player, id, 1);
                            break;
                        case 53654:
                            Stalls.stealFromAFKStall(player, id, 2);
                            break;
                        case 30035:
                            Stalls.stealFromAFKStall(player, id, 3);
                            break;*/
                        case 16047:
                            player.getPacketSender().sendMessage(
                                    "In order to unlock cradle of cursed you must use a @blu@Cursed@bla@ key on it.");
                            break;
                        case 16135:
                            player.getPacketSender().sendMessage(
                                    "In order to unlock gift of betrayed you must use a @blu@Betrayed@bla@ key on it.");
                            break;
                        case 16077:
                            player.getPacketSender().sendMessage(
                                    "In order to unlock grain of damned you must use a @blu@Damned@bla@ key on it.");
                            break;
                        case 16118:
                            player.getPacketSender().sendMessage(
                                    "In order to unlock box of hidden you must use a @blu@Hidden@bla@ key on it.");
                            break;
                        case 16150:
                            DialogueManager.sendDialogue(player, new NextLevel(player), -1);
                            break;

                        // RAID CHEST REWARDS
                        // ARLO
                       /* case 59731:
                            if (player.getInventory().getFreeSlots() < 4) {
                                player.getPacketSender()
                                        .sendMessage("You don't have enough inventory spaces. You need 4 spaces.");
                                return;
                            }
                            Item[] common = Raids1.common;
                            Item[] uncommon = Raids1.uncommon;
                            Item[] rare = Raids1.rare;
                            if (player.getInventory().contains(13591, 1)) {
                                player.getInventory().delete(13591, 1);
                                int chance = RandomUtility.inclusiveRandom(0, 1000);
                                String rarity = "";
                                Item reward = null;
                                if (chance < 600) {
                                    reward = common[RandomUtility.exclusiveRandom(0, common.length)];
                                    rarity = "Common";
                                } else if (chance < 985) {
                                    reward = uncommon[RandomUtility.exclusiveRandom(0, uncommon.length)];
                                    rarity = "Uncommon";
                                } else {
                                    reward = rare[RandomUtility.exclusiveRandom(0, rare.length)];
                                    rarity = "Very Rare";
                                }
                                player.getPacketSender().sendInterface(29130);
                                player.getPacketSender().sendItemOnInterface(29132, reward.getId(), 0,
                                        reward.getAmount());
                                int[] Items1 = new int[]{ItemDefinition.MILL_ID, 5022};
                                int[] Items2 = new int[]{200, 202, 204, 206, 208, 210, 212, 214, 216, 218, 220, 2486,
                                        3052, 1624, 1622, 1620, 1618, 1632, 1516, 1514, 454, 448, 450, 452, 378, 372,
                                        7945, 384, 390, 15271, 533, 535, 537, 18830, 556, 558, 555, 554, 557, 559, 564,
                                        562, 566, 9075, 563, 561, 560, 565, 888, 890, 892, 11212, 9142, 9143, 9144,
                                        9341, 9244, 866, 867, 868, 2, 10589, 10564, 6809, 4132, 15126, 4153, 1704, 1149,
                                        4709, 4711, 4713, 4715, 4717, 4719, 4721, 4723, 4725, 4727, 4729, 4731, 4733,
                                        4735, 4737, 4739, 4746, 4748, 4750, 4752, 4754, 4756, 4758, 4760};
                                int[] Items3 = new int[]{14733, 14732, 14734, 19111, 11137, 3907, 18351, 3140, 18353,
                                        18355, 18357, 15501, 15272, 2503, 10499, 3805, 6326, 861, 1163, 1201, 6111, 544,
                                        542, 5574, 5575, 5576, 1215, 3105, 13734, 7400, 11118, 4708, 4710, 4712, 4714,
                                        4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745,
                                        4747, 4749, 4751, 4753, 4755, 4757, 4759, 4708, 4710, 4712, 4714, 4716, 4718,
                                        4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749,
                                        4751, 4753, 4755, 4757, 4759, 4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722,
                                        4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753,
                                        4755, 4757, 4759, 6199, 290};
                                int[] LowItems = new int[]{50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 2, 4, 6,
                                        8, 10, 12, 14, 16, 20, 30, 40, 50, 60, 70, 80, 90, 100, 20, 30, 40, 50, 60, 70,
                                        80, 90, 100, 1000, 2000, 3000, 4000, 5000};
                                int[] MedItems = new int[]{5, 10, 10, 6, 8, 4, 5, 3, 5, 7, 8, 10, 2, 3, 5, 10, 2, 3, 9, 11};
                                int[] HighItems = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2};
                                int Orbs = Items1[RandomUtility.exclusiveRandom(0, Items1.length)];
                                int Skill = Items2[RandomUtility.exclusiveRandom(0, Items2.length)];
                                int Keys = Items3[RandomUtility.exclusiveRandom(0, Items3.length)];

                                int pickedAmt = LowItems[RandomUtility.exclusiveRandom(0, LowItems.length)];
                                int pickedAmt2 = MedItems[RandomUtility.exclusiveRandom(0, MedItems.length)];
                                int pickedAmt3 = HighItems[RandomUtility.exclusiveRandom(0, HighItems.length)];
                                player.getInventory().add(Orbs, pickedAmt);
                                player.getInventory().add(Skill, pickedAmt2);
                                player.getInventory().add(Keys, pickedAmt3);
                                player.getPacketSender().sendItemOnInterface(29134, Orbs, 0, pickedAmt);
                                player.getPacketSender().sendItemOnInterface(29135, Skill, 0, pickedAmt2);
                                player.getPacketSender().sendItemOnInterface(29136, Keys, 0, pickedAmt3);
                                player.getInventory().add(reward);
                                if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                                    DungeoneeringParty party = player.getMinigameAttributes()
                                            .getDungeoneeringAttributes().getParty();
                                    party.sendMessage("<shad=1>[" + rarity + " Reward]" + "@blu@ "
                                            + player.getUsername() + " " + "opened the raids chest and got a @red@X"
                                            + reward.getAmount() + " " + reward.getDefinition().getName());
                                }
                            } else {
                                player.sendMessage("@red@You need a raid key to open the raid chest");
                            }
                            player.sendMessage("Clicked on the raid chest");
                            break;*/

                        case 16148:
                            player.moveTo(new Position(1863, 5239, 0));
                            break;
                        case 16149:
                            player.moveTo(new Position(2042, 5245, 0));
                            break;
                        case 16082:
                            player.moveTo(new Position(2017, 5211, 0));
                            break;
                        case 16114:

                            player.moveTo(new Position(2358, 5218, 0));
                            break;

                        case 16115:
                            player.moveTo(new Position(2358, 5215, 0));
                            break;
                        case 16080:
                            player.moveTo(new Position(1863, 5239, 0));
                            break;
                        case 16078:
                            player.moveTo(new Position(1902, 5221, 0));
                            break;
                        case 16049:
                            player.moveTo(new Position(2120, 5257, 0));
                            break;
                        case 16081:
                            player.moveTo(new Position(2122, 5251, 0));
                            break;
                        case 16050:
                            player.moveTo(new Position(2350, 5214, 0));
                            break;
                        case 16112:
                            player.moveTo(new Position(2026, 5217, 0));
                            break;
                        case 16048:
                            player.moveTo(new Position(2147, 5284, 0));
                            break;
                        case 2305:
                            if (player.getLocation() != null) {
                                player.moveTo(new Position(3003, 10354, player.getPosition().getZ()));
                                player.getPacketSender().sendMessage("You escape from the spikes.");
                            }
                            break;
//                        case 4387://
//                            player.setDialogueActionId(214);
//                            DialogueManager.start(player, 214);
//                            break;
                        case 4408:

                            /*
                             * if(player.getPointsHandler().getTotalPrestiges() <= 49) {
                             * player.getPacketSender().
                             * sendMessage("You need 50 Total Prestiges. You only have "+
                             * player.getPointsHandler().getTotalPrestiges()+"@bla@.");
                             *
                             *
                             *
                             * } else {
                             * player.getPacketSender().sendMessage("You teleport to prestige portals area."
                             * );
                             *
                             * player.moveTo(new Position(3191, 9825, player.getPosition().getZ())); }
                             */
                            Position position1 = new Position(3168, 3490, 2);
                            player.getPacketSender().sendMessage("@red@This portal is disabled, please type ::Train!");
                            break;
                        case 589: // varrock ball
                            if (Misc.easter(2017)) {
                                if (player.getInventory().isFull()) {
                                    player.getPacketSender()
                                            .sendMessage("My inventory is too full, I should make room first.");
                                    return;
                                }
                                if (player.getEaster2017() == easter2017data.forObjectId(id).getRequiredProgress()) {
                                    player.getPacketSender()
                                            .sendMessage(easter2017data.forObjectId(id).getSearchMessage());
                                    player.setEaster2017(easter2017data.forObjectId(id).getRequiredProgress() + 1);
                                    player.getInventory().add(1961, 1);
                                }
                            } else {
                                player.getPacketSender().sendMessage("Just a wise old woman's ball.");
                            }
                            break;
                        case 11678:
                            if (Misc.easter(2017)) {
                                if (player.getInventory().isFull()) {
                                    player.getPacketSender()
                                            .sendMessage("My inventory is too full, I should make room first.");
                                    return;
                                }
                                if (player.getEaster2017() == easter2017data.forObjectId(id).getRequiredProgress()) {
                                    player.getPacketSender()
                                            .sendMessage(easter2017data.forObjectId(id).getSearchMessage());
                                    player.setEaster2017(easter2017data.forObjectId(id).getRequiredProgress() + 1);
                                    player.getInventory().add(1961, 1);
                                }
                            } else {
                                player.getPacketSender().sendMessage("Nope. Nothing special to it.");
                            }
                            break;
                        case 5595:
                            if (Misc.easter(2017)) {
                                if (player.getInventory().isFull()) {
                                    player.getPacketSender()
                                            .sendMessage("My inventory is too full, I should make room first.");
                                    return;
                                }
                                if (player.getEaster2017() == easter2017data.forObjectId(id).getRequiredProgress()) {
                                    player.getPacketSender()
                                            .sendMessage(easter2017data.forObjectId(id).getSearchMessage());
                                    player.setEaster2017(easter2017data.forObjectId(id).getRequiredProgress() + 1);
                                    player.getInventory().add(1961, 1);
                                }
                            } else {
                                player.getPacketSender().sendMessage("Just some toys.");
                            }
                            break;
                        case 2725:
                            if (Misc.easter(2017)) {
                                if (player.getInventory().isFull()) {
                                    player.getPacketSender()
                                            .sendMessage("My inventory is too full, I should make room first.");
                                    return;
                                }
                                if (player.getEaster2017() == easter2017data.forObjectId(id).getRequiredProgress()) {
                                    player.getPacketSender()
                                            .sendMessage(easter2017data.forObjectId(id).getSearchMessage());
                                    player.setEaster2017(easter2017data.forObjectId(id).getRequiredProgress() + 1);
                                    player.getInventory().add(1961, 1);
                                }
                            } else {
                                player.getPacketSender().sendMessage("Just regular fireplace things.");
                            }
                            break;
                        case 423:
                            if (Misc.easter(2017)) {
                                if (player.getInventory().isFull()) {
                                    player.getPacketSender()
                                            .sendMessage("My inventory is too full, I should make room first.");
                                    return;
                                }
                                if (player.getEaster2017() == easter2017data.forObjectId(id).getRequiredProgress()) {
                                    player.getPacketSender()
                                            .sendMessage(easter2017data.forObjectId(id).getSearchMessage());
                                    player.setEaster2017(easter2017data.forObjectId(id).getRequiredProgress() + 1);
                                    player.getInventory().add(1961, 1);
                                }
                            } else {
                                player.getPacketSender().sendMessage("I don't want to mess around with someone's bed.");
                            }
                            break;
                        case 11339:
                            if (Misc.easter(2017)) {
                                if (player.getInventory().isFull()) {
                                    player.getPacketSender()
                                            .sendMessage("My inventory is too full, I should make room first.");
                                    return;
                                }
                                if (player.getEaster2017() == easter2017data.forObjectId(id).getRequiredProgress()) {
                                    player.getPacketSender()
                                            .sendMessage(easter2017data.forObjectId(id).getSearchMessage());
                                    player.setEaster2017(easter2017data.forObjectId(id).getRequiredProgress() + 1);
                                    player.getInventory().add(1961, 1);
                                }
                            } else {
                                player.getPacketSender().sendMessage("Just some gold, I can get enough on my own.");
                            }
                            break;
                        case 17953:
                            break;
                        case 28295:
                            if (christmas2016.isChristmas()) {
                                player.getPacketSender().sendMessage("Welcome to the Christmas 2016 event!");
                                player.moveTo(christmas2016.eventStart);
                            }
                            break;
                        case 28296:
                            if (!player.getClickDelay().elapsed(1250)) {
                                // player.getPacketSender().sendMessage("Your hands are getting cold, slow
                                // down!");
                                return;
                            }
                            player.getClickDelay().reset();
                            if (!player.getInventory().isFull() || (player.getInventory().getFreeSlots() == 0
                                    && player.getInventory().contains(10501))) {
                                player.performAnimation(new Animation(827));
                                player.getInventory().add(10501, Misc.getRandom(20));
                                player.getPacketSender().sendMessage("You pack some of the snow together...");
                            } else {
                                player.getPacketSender().sendMessage("You'll need some inventory space first!");
                            }
                            break;
                        case 7475:
                            if (!player.getClickDelay().elapsed(1250)) {
                                // player.getPacketSender().sendMessage("Your hands are getting cold, slow
                                // down!");
                                return;
                            }
                            player.getClickDelay().reset();
                            if (player.getInventory().contains(20104) && player.getInventory().contains(20105)) {
                                player.performAnimation(new Animation(810));
                                player.getInventory().delete(20104, 1);
                                player.getInventory().delete(20105, 1);
                                player.moveTo(new Position(2584, 2575, player.getPosition().getZ()));

                                player.getPacketSender()
                                        .sendMessage("<img=100>@cya@You make it to the next room! keep going :)");
                            } else {
                                player.getPacketSender().sendMessage("you need Key 1 and key 2 to enter!");
                            }
                            break;
                        case 3378:
                            int[] commonsuper = new int[]{11846, 11848, 11850, 11852, 11854, 11856, 18686, 18799, 5095, 13996, 13913, 13919};
                            int[] uncommonsuper = new int[]{6927, 6928, 6929, 6930, 6931, 6932, 6933, 6935, 6936, 22077, 19136, 6936};
                            int[] raresuper = new int[]{13640, 19468, 19166, 19165, 19812, 20554, 19115, 20488,
                                    10946, 6769, 15288, 15290};
                            player.getMysteryBoxOpener().display(11795, "Custom chest key", commonsuper, uncommonsuper,
                                    raresuper);
                            break;
                        case 7476:
                            if (!player.getClickDelay().elapsed(1250)) {
                                // player.getPacketSender().sendMessage("Your hands are getting cold, slow
                                // down!");
                                return;
                            }
                            player.getClickDelay().reset();
                            if (player.getInventory().contains(20106) && player.getInventory().contains(20107)) {
                                player.performAnimation(new Animation(810));
                                player.getInventory().delete(20106, 1);
                                player.getInventory().delete(20107, 1);
                                player.moveTo(new Position(2583, 2584, player.getPosition().getZ()));

                                player.getPacketSender()
                                        .sendMessage("<img=100>@cya@You make it to the next room! keep going :)");
                            } else {
                                player.getPacketSender().sendMessage("you need Key 3 and 4 to enter!");
                            }
                            break;
                        case 7477:
                            if (!player.getClickDelay().elapsed(1250)) {
                                // player.getPacketSender().sendMessage("Your hands are getting cold, slow
                                // down!");
                                return;
                            }
                            player.getClickDelay().reset();
                            if (player.getInventory().contains(20108) && player.getInventory().contains(20109)) {
                                player.performAnimation(new Animation(810));
                                player.getInventory().delete(20108, 1);
                                player.getInventory().delete(20109, 1);
                                player.moveTo(new Position(2581, 2592, player.getPosition().getZ()));

                                player.getPacketSender()
                                        .sendMessage("<img=100>@cya@You make it to the next room! keep going :)");
                            } else {
                                player.getPacketSender().sendMessage("you need Key 5 and Key 6 to enter!");
                            }
                            break;
                        case 7478:
                            if (!player.getClickDelay().elapsed(1250)) {
                                // player.getPacketSender().sendMessage("Your hands are getting cold, slow
                                // down!");
                                return;
                            }
                            player.getClickDelay().reset();// 20104
                            if (player.getInventory().contains(20103)) {
                                player.performAnimation(new Animation(810));
                                player.getInventory().delete(20103, 1);

                                player.moveTo(new Position(2583, 2607, player.getPosition().getZ()));

                                Doom.spawnWave1(player);

                                player.getPacketSender()
                                        .sendMessage("<img=100>@cya@You make it to the next room! keep going :)");
                                player.getPacketSender().sendMessage("@red@Kill the boss for a Chest key!");
                            } else {
                                player.getPacketSender().sendMessage("you need boss room key to enter!");
                            }
                            break;
                        case 7479:
                            player.moveTo(new Position(2583, 2565, player.getPosition().getZ()));

                            player.getPacketSender().sendMessage("@red@Kill stormtrooper to get the key 1 and 2.");
                            break;

//                        case 9975:
//                        case 2804:
//                        case 41900:
//                            DialogueManager.start(player, 22);
//                            player.setDialogueActionId(14);
//                            break;
                        case 2112:
                            if (!player.getDonator().isClericPlus()) {
                                player.getPacketSender().sendMessage("You must be a member to access this area.");
                                return;
                            }
                            TaskManager.submit(new Task(0, player, true) {
                                int tick = 0;

                                @Override
                                public void execute() {
                                    tick++;
                                    if (player.getPosition().getX() == 3046 && player.getPosition().getY() == 9757) {
                                        player.getMovementQueue().walkStep(0, -1);
                                        player.getPacketSender()
                                                .sendMessage("As a member, you can pass through the door.");
                                    } else if (player.getPosition().getX() == 3046
                                            && player.getPosition().getY() == 9756) {
                                        player.getMovementQueue().walkStep(0, 1);
                                        player.getPacketSender()
                                                .sendMessage("As a member, you can pass through the door.");
                                    } else {
                                        player.getPacketSender().sendMessage("You must be in front of the door first.");
                                    }
                                    if (tick == 1)
                                        stop();
                                }

                                @Override
                                public void stop() {
                                    setEventRunning(false);
                                    // player.setCrossingObstacle(false);
                                }
                            });
                            break;
                        case 2882:
                        case 2883:
                            TaskManager.submit(new Task(0, player, true) {
                                int tick = 0;

                                @Override
                                public void execute() {
                                    tick++;
                                    if (player.getPosition().getX() == 3268 && player.getPosition().getY() > 3226
                                            && player.getPosition().getY() < 3229) {
                                        player.getMovementQueue().walkStep(-1, 0);
                                        player.getPacketSender().sendMessage("You pass through the gate.");
                                    } else if (player.getPosition().getX() == 3267 && player.getPosition().getY() > 3226
                                            && player.getPosition().getY() < 3229) {
                                        player.getMovementQueue().walkStep(1, 0);
                                        player.getPacketSender().sendMessage("You pass through the gate.");
                                    } else {
                                        player.getPacketSender().sendMessage("You must be in front of the gate first.");
                                    }
                                    if (tick == 1)
                                        stop();
                                }

                                @Override
                                public void stop() {
                                    setEventRunning(false);
                                    // player.setCrossingObstacle(false);
                                }
                            });
                            break;
                        case 5262:

                            break;

                        case 2273:

                            break;
//                        case 5259:
//                            if (player.getPosition().getX() >= 3653) { // :)
//                                if (player.getPosition().getY() != 3485 && player.getPosition().getY() != 3486) {
//                                    player.getPacketSender()
//                                            .sendMessage("You need to stand infront of the barrier to pass through.");
//                                    return;
//                                }
//                                player.moveTo(new Position(3651, player.getPosition().getY()));
//                            } else {
//                                player.setDialogueActionId(73);
//                                DialogueManager.start(player, 115);
//                            }
//                            break;
                        case 4470:
                            if (player.getPosition().getX() == 2459 && player.getPosition().getY() == 2862
                                    || player.getPosition().getY() == 2861) { // :)
                                player.moveTo(new Position(2462, player.getPosition().getY()));
                            }
                            if (player.getPosition().getX() == 2462 && player.getPosition().getY() == 2862
                                    || player.getPosition().getY() == 2861) { // :)
                                player.moveTo(new Position(2459, player.getPosition().getY()));
                            } else {
                                player.getPacketSender()
                                        .sendMessage("You need to stand infront of the barrier to pass through.");
                            }
                            break;
                        case 10805:
                        case 10806:
                            GrandExchange.open(player);
                            break;
                        case 38700:
                            if (gameObject.getPosition().getX() == 3668 && gameObject.getPosition().getY() == 2976) {
                                player.getPacketSender().sendMessage(
                                        "<img=5> @blu@Welcome to the free-for-all arena! You will not lose any items on death here.");
                                player.moveTo(new Position(2815, 5511));
                            } else if (player.getLocation() == Location.FREE_FOR_ALL_WAIT) {
                                player.moveTo(GameSettings.DEFAULT_POSITION.copy());
                            } else if (gameObject.getPosition().getX() == 2849
                                    && gameObject.getPosition().getY() == 3353) {
                                player.getPacketSender().sendMessage(
                                        "<img=5> @blu@Welcome to the free-for-all arena! You will not lose any items on death here.");
                                player.moveTo(new Position(2815, 5511));
                            }
                            break;
                        case 2465:
                            break;
//                        case 45803:
//                        case 1767:
//                            DialogueManager.start(player, 114);
//                            player.setDialogueActionId(72);
//                            break;
                        case 7352:
                            if (Dungeoneering.doingOldDungeoneering(player) && player.getMinigameAttributes()
                                    .getDungeoneeringAttributes().getParty().getGatestonePosition() != null) {
                                player.moveTo(player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                        .getGatestonePosition());
                                player.setEntityInteraction(null);
                                player.getPacketSender().sendMessage("You are teleported to your party's gatestone.");
                                player.performGraphic(new Graphic(1310));
                            } else
                                player.getPacketSender().sendMessage(
                                        "Your party must drop a Gatestone somewhere in the dungeon to use this portal.");
                            break;
                        case 7353:
                            player.moveTo(new Position(2439, 4956, player.getPosition().getZ()));
                            break;
                        case 7321:
                            player.moveTo(new Position(2452, 4944, player.getPosition().getZ()));
                            break;
                        case 7322:
                            player.moveTo(new Position(2455, 4964, player.getPosition().getZ()));
                            break;
                        case 7315:
                            player.moveTo(new Position(2447, 4956, player.getPosition().getZ()));
                            break;
                        case 7316:
                            player.moveTo(new Position(2471, 4956, player.getPosition().getZ()));
                            break;
                        case 7318:
                            player.moveTo(new Position(2464, 4963, player.getPosition().getZ()));
                            break;
                        // case 7319:
                        // player.moveTo(new Position(2467, 4940, player.getPosition().getZ()));
                        // break;
                        case 7324:
                            player.moveTo(new Position(2481, 4956, player.getPosition().getZ()));
                            break;

                        case 7319:
                            if (gameObject.getPosition().getX() == 2481 && gameObject.getPosition().getY() == 4956)
                                player.moveTo(new Position(2467, 4940, player.getPosition().getZ()));
                            break;

                        case 11356:
                            if (gameObject.getPosition().getX() == 1764 && gameObject.getPosition().getY() == 5331) {
                                VaultOfWar.enterEasternDungeon(player);
                            } else {
                                player.moveTo(new Position(2860, 9741));
                                player.getPacketSender().sendMessage("You step through the portal..");
                            }
                            break;
                        case 47180:
                            if (!player.getDonator().isClericPlus()) {
                                player.getPacketSender().sendMessage("You must be a Member to use this.");
                                return;
                            }
                            player.getPacketSender().sendMessage("You activate the device..");
                            player.moveTo(new Position(2586, 3912));
                            break;
                        case 10091:
                        case 8702:
                            if (gameObject.getId() == 8702) {
                                if (!player.getDonator().isClericPlus()) {
                                    player.getPacketSender().sendMessage("You must be a Member to use this.");
                                    return;
                                }
                            }
                            Fishing.setupFishing(player, Spot.ROCKTAIL);
                            break;
                        case 2470:
                            if (player.getTeleblockTimer() > 0) {
                                player.getPacketSender().sendMessage("You are teleblocked, don't die, noob.");
                                return;
                            }
                            if (gameObject.getPosition().getX() == 2464 && gameObject.getPosition().getY() == 4782) {
                                player.moveTo(GameSettings.DEFAULT_POSITION.copy());
                                player.getPacketSender().sendMessage("The portal teleports you home.");
                                return;
                            }
                            if (gameObject.getPosition().getX() == 3674 && gameObject.getPosition().getY() == 2981
                                    && GameSettings.Halloween) {
                                player.moveTo(new Position(3108, 3352, 4));
                                player.getPacketSender().sendMessage("<img=5> You teleport to the event!");
                                return;
                            }
                            break;
                        case 2274:
                            if (player.getTeleblockTimer() > 0) {
                                player.getPacketSender().sendMessage("You are teleblocked, don't die, noob.");
                                return;
                            }
                            if (gameObject.getPosition().getX() == 2912 && gameObject.getPosition().getY() == 5300) {
                                player.moveTo(new Position(2914, 5300, 1));
                            } else if (gameObject.getPosition().getX() == 2914
                                    && gameObject.getPosition().getY() == 5300) {
                                player.moveTo(new Position(2912, 5300, 2));
                            } else if (gameObject.getPosition().getX() == 3553
                                    && gameObject.getPosition().getY() == 9695) {
                                player.moveTo(new Position(3565, 3313, 0));
                            } else if (gameObject.getPosition().getX() == 2919
                                    && gameObject.getPosition().getY() == 5276) {
                                player.moveTo(new Position(2918, 5274));
                            } else if (gameObject.getPosition().getX() == 2918
                                    && gameObject.getPosition().getY() == 5274) {
                                player.moveTo(new Position(2919, 5276, 1));
                            } else if (gameObject.getPosition().getX() == 3001
                                    && gameObject.getPosition().getY() == 3931
                                    || gameObject.getPosition().getX() == 3652
                                    && gameObject.getPosition().getY() == 3488) {
                                player.moveTo(GameSettings.DEFAULT_POSITION.copy());
                                player.getPacketSender().sendMessage("The portal teleports you home.");
                                // } else if(gameObject.getPosition().getX() == 2914 &&
                                // gameObject.getPosition().getY() == 5300 && (player.getAmountDonated() >= 5 ||
                                // player.getSkillManager().getCurrentLevel(Skill.AGILITY) == 99)) {
                                // player.getPacketSender().sendMessage("You would have access to the
                                // shortcut.");
                            }
                            break;
                        case 28779:
                            if (player.getTeleblockTimer() > 0) {
                                player.getPacketSender()
                                        .sendMessage("You are teleblocked, and cannot navigate the chaos tunnels.");
                                return;
                            }
                            Position des = new Position(-1, -1);
                            for (int i = 0; i < portal.values().length; i++) {
                                if (portal.values()[i].getLocation().getX() == gameObject.getPosition().getX()
                                        && portal.values()[i].getLocation().getY() == gameObject.getPosition().getY()) {
                                    des = new Position(portal.values()[i].getDestination().getX(),
                                            portal.values()[i].getDestination().getY(), player.getPosition().getZ());
                                    // // System.out.println("Matched on portal index "+i);
                                    break;
                                }
                            }
                            if (des.getX() != -1 && des.getY() != -1) {
                                player.moveTo(des);
                            } else {
                                player.getPacketSender().sendMessage("ERROR 13754, no internals. Report on forums!");
                            }
                            /*
                             * if(gameObject.getPosition().getX() == 3186 && gameObject.getPosition().getY()
                             * == 5472) { player.moveTo(new Position(3192, 5471, 0)); } else
                             * if(gameObject.getPosition().getX() == 3192 && gameObject.getPosition().getY()
                             * == 5472) { player.moveTo(new Position(3185, 5472, 0)); } else
                             * if(gameObject.getPosition().getX() == 3197 && gameObject.getPosition().getY()
                             * == 5448) { player.moveTo(new Position(3205, 5445, 0)); } else
                             * if(gameObject.getPosition().getX() == 3204 && gameObject.getPosition().getY()
                             * == 5445) { player.moveTo(new Position(3196, 5448, 0)); } else
                             * if(gameObject.getPosition().getX() == 3189 && gameObject.getPosition().getY()
                             * == 5444) { player.moveTo(new Position(3187, 5459, 0)); } else
                             * if(gameObject.getPosition().getX() == 3187 && gameObject.getPosition().getY()
                             * == 5460) { player.moveTo(new Position(3190, 5444, 0)); } else
                             * if(gameObject.getPosition().getX() == 3178 && gameObject.getPosition().getY()
                             * == 5460) { player.moveTo(new Position(3168, 5457, 0)); } else
                             * if(gameObject.getPosition().getX() == 3168 && gameObject.getPosition().getY()
                             * == 5456) { player.moveTo(new Position(3178, 5459, 0)); } else
                             * if(gameObject.getPosition().getX() == 3167 && gameObject.getPosition().getY()
                             * == 5471) { player.moveTo(new Position(3172, 5473, 0)); } else
                             * if(gameObject.getPosition().getX() == 3171 && gameObject.getPosition().getY()
                             * == 5473) { player.moveTo(new Position(3167, 5470, 0)); } else
                             * if(gameObject.getPosition().getX() == 3171 && gameObject.getPosition().getY()
                             * == 5478) { player.moveTo(new Position(3166, 5478, 0)); } else
                             * if(gameObject.getPosition().getX() == 3167 && gameObject.getPosition().getY()
                             * == 5478) { player.moveTo(new Position(3172, 5478, 0)); // }
                             */
                            break;
                        case 5960: // Levers
                        case 5959:
                            if (player.getLocation() == Location.MAGEBANK_SAFE) {
                                TeleportHandler.teleportPlayer(player, TeleportLocations.MAGEBANK_WILDY.getPos(),
                                        TeleportType.LEVER);
                            } else {
                                player.getPacketSender()
                                        .sendMessage("ERROR: 00512, P: [" + player.getPosition().getX() + ","
                                                + player.getPosition().getY() + "," + player.getPosition().getZ()
                                                + "] - please report this bug!");
                            }
                            break;
                        // player.setDirection(Direction.WEST);
                        // TeleportHandler.teleportPlayer(player, new Position(3090, 3475),
                        // TeleportType.LEVER);
                        // break;
                        case 5096:
                            if (gameObject.getPosition().getX() == 2644 && gameObject.getPosition().getY() == 9593)
                                player.moveTo(new Position(2649, 9591));
                            break;

                        case 5094:
                            if (gameObject.getPosition().getX() == 2648 && gameObject.getPosition().getY() == 9592)
                                player.moveTo(new Position(2643, 9594, 2));
                            break;

                        case 5098:
                            if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9511)
                                player.moveTo(new Position(2637, 9517));
                            break;

                        case 5097:
                            if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9514)
                                player.moveTo(new Position(2636, 9510, 2));
                            break;
                        case 26426:
                            // Saradomin Room
                            if (player.getPosition().getX() == 2533 && gameObject.getPosition().getX() == 2534)
                                player.moveTo(new Position(2534, 2652, 0));
                            else if (player.getPosition().getX() == 2534 && gameObject.getPosition().getX() == 2534)
                                player.moveTo(new Position(2533, 2652, 0));
                                // Zamorak Room
                            else if (player.getPosition().getX() == 2521 && gameObject.getPosition().getX() == 2520)
                                player.moveTo(new Position(2520, 2652, 0));
                            else if (player.getPosition().getX() == 2520 && gameObject.getPosition().getX() == 2520)
                                player.moveTo(new Position(2521, 2652, 0));
                                // Armadyl Room
                            else if (player.getPosition().getY() == 2643 && gameObject.getPosition().getY() == 2643)
                                player.moveTo(new Position(2527, 2644, 0));
                            else if (player.getPosition().getY() == 2644 && gameObject.getPosition().getY() == 2643)
                                player.moveTo(new Position(2527, 2643, 0));
                                // Bandos Room
                            else if (player.getPosition().getY() == 2661 && gameObject.getPosition().getY() == 2661)
                                player.moveTo(new Position(2527, 2660, 0));
                            else if (player.getPosition().getY() == 2660 && gameObject.getPosition().getY() == 2661)
                                player.moveTo(new Position(2527, 2661, 0));

                            break;
                        case 26428:
                        case 26425:
                        case 26427:
                            String bossRoom = "Armadyl";
                            boolean leaveRoom = player.getPosition().getY() > 5295;
                            int index = 0;
                            Position movePos = new Position(2839, !leaveRoom ? 5296 : 5295, 2);
                            if (id == 26425) {
                                bossRoom = "Bandos";
                                leaveRoom = player.getPosition().getX() > 2863;
                                index = 1;
                                movePos = new Position(!leaveRoom ? 2864 : 2863, 5354, 2);
                            } else if (id == 26427) {
                                bossRoom = "Saradomin";
                                leaveRoom = player.getPosition().getX() < 2908;
                                index = 2;
                                movePos = new Position(leaveRoom ? 2908 : 2907, 5265);
                            } else if (id == 26428) {
                                bossRoom = "Zamorak";
                                leaveRoom = player.getPosition().getY() <= 5331;
                                index = 3;
                                movePos = new Position(2925, leaveRoom ? 5332 : 5331, 2);
                            }
                            if (!leaveRoom && (!player.getDonator().isClericPlus() && player.getMinigameAttributes()
                                    .getGodwarsDungeonAttributes().getKillcount()[index] < 20)) {
                                if (player.getInventory().contains(22053)) {
                                    player.getInventory().delete(22053, 1);
                                    player.getPacketSender().sendMessage(
                                            "Your ecumenical key is consumed, and you pass through the door.");
                                } else {
                                    player.getPacketSender().sendMessage("You need " + Misc.anOrA(bossRoom) + " "
                                            + bossRoom + " killcount of at least 20 to enter this room.");
                                    return;
                                }
                            }
                            if (player.getDonator().isClericPlus()) {
                                player.getPacketSender()
                                        .sendMessage("@red@As a member, you don't need to worry about kill count.");
                                player.performGraphic(new Graphic(6, GraphicHeight.LOW));
                            }
                            player.moveTo(movePos);
                            player.getMinigameAttributes().getGodwarsDungeonAttributes()
                                    .setHasEnteredRoom(leaveRoom ? false : true);
                            player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index] = 0;
                            player.getPacketSender().sendString(16216 + index, "0");
                            break;
                        case 26439:
//                            if (player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) <= 700
//                                    && !(player.getDonator().isClericPlus())) {
//                                player.getPacketSender().sendMessage(
//                                        "You need a Constitution level of at least 70 to swim across, or be a member.");
//                                return;
//                            }
//                            if (player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) <= 700) {
//                                player.performGraphic(new Graphic(6, GraphicHeight.LOW));
//                                player.getPacketSender().sendMessage(
//                                        "@red@You don't have 70 Constitution, but as a member you can cross anyway.");
//                            }
//                            if (!player.getClickDelay().elapsed(1000))
//                                return;
//                            if (player.isCrossingObstacle())
//                                return;
//                            final String startMessage = "You jump into the icy cold water..";
//                            final String endMessage = "You climb out of the water safely.";
//                            final int jumpGFX = 68;
//                            final int jumpAnimation = 772;
//                            player.setSkillAnimation(773);
//                            player.setCrossingObstacle(true);
//                            player.getUpdateFlag().flag(Flag.APPEARANCE);
//                            player.performAnimation(new Animation(3067));
//                            final boolean goBack2 = player.getPosition().getY() >= 5344;
//                            player.getPacketSender().sendMessage(startMessage);
//                            player.moveTo(new Position(2885, !goBack2 ? 5335 : 5342, 2));
//                            player.setDirection(goBack2 ? Direction.SOUTH : Direction.NORTH);
//                            player.performGraphic(new Graphic(jumpGFX));
//                            player.performAnimation(new Animation(jumpAnimation));
//                            TaskManager.submit(new Task(1, player, false) {
//                                int ticks = 0;
//
//                                @Override
//                                public void execute() {
//                                    ticks++;
//                                    player.getMovementQueue().walkStep(0, goBack2 ? -1 : 1);
//                                    if (ticks >= 10)
//                                        stop();
//                                }
//
//                                @Override
//                                public void stop() {
//                                    player.setSkillAnimation(-1);
//                                    player.setCrossingObstacle(false);
//                                    player.getUpdateFlag().flag(Flag.APPEARANCE);
//                                    player.getPacketSender().sendMessage(endMessage);
//                                    player.moveTo(
//                                            new Position(2885, player.getPosition().getY() < 5340 ? 5333 : 5345, 2));
//                                    setEventRunning(false);
//                                }
//                            });
//                            player.getClickDelay().reset((System.currentTimeMillis() + 9000));
                            break;
                        case 26384:
                            if (player.isCrossingObstacle())
                                return;
                            if (!player.getInventory().contains(2347) && !(player.getDonator().isClericPlus())) {
                                player.getPacketSender()
                                        .sendMessage("You need to have a hammer to bang on the door with.");
                                return;
                            }
                            if (!player.getInventory().contains(2347) && (player.getDonator().isClericPlus())) {
                                player.getPacketSender().sendMessage(
                                        "@red@You don't have a hammer, but as a member you can enter anyway.");
                                player.performGraphic(new Graphic(6, GraphicHeight.LOW));
                            }
                            if (player.getDonator().isClericPlus())
                                player.setCrossingObstacle(true);
                            final boolean goBack = player.getPosition().getX() <= 2850;
                            player.performAnimation(new Animation(377));
                            TaskManager.submit(new Task(2, player, false) {
                                @Override
                                public void execute() {
                                    player.moveTo(new Position(goBack ? 2851 : 2850, 5333, 2));
                                    player.setCrossingObstacle(false);
                                    stop();
                                }
                            });
                            break;
                        case 57211:
                            player.getPacketSender().sendMessage(
                                    "@red@Nobody is home. Please use the teleport under Modern Bosses to get to Nex.");
                            break;
//                        case 26303:
//                            if (!player.getClickDelay().elapsed(1200))
//                                return;
//                            if (player.getSkillManager().getCurrentLevel(Skill.RANGED) < 70
//                                    && !(player.getDonator().isClericPlus()))
//                                player.getPacketSender()
//                                        .sendMessage("You need a Ranged level of at least 70 to swing across here.")
//                                        .sendMessage(
//                                                "Or, you can get membership for $10 and pass without the requirement.");
//                            else if (!player.getInventory().contains(9418) && !(player.getDonator().isClericPlus())) {
//                                player.getPacketSender().sendMessage(
//                                        "You need a Mithril grapple to swing across here. Explorer Jack might have one.")
//                                        .sendMessage(
//                                                "Or, you can get membership for $10 and pass without the requirement.");
//                                return;
//                            } else {
//                                if (player.getSkillManager().getCurrentLevel(Skill.RANGED) < 70) {
//                                    player.getPacketSender().sendMessage(
//                                            "@red@You don't have 70 Ranged, but as a member you can enter anyway.");
//                                    player.performGraphic(new Graphic(6, GraphicHeight.LOW));
//                                }
//                                if (!(player.getInventory().contains(9418))) {
//                                    player.performGraphic(new Graphic(6, GraphicHeight.LOW));
//                                    player.getPacketSender().sendMessage(
//                                            "@red@You don't have a Mith grapple, but as a member you can enter anyway.");
//                                }
//                                player.performAnimation(new Animation(789));
//                                TaskManager.submit(new Task(2, player, false) {
//                                    @Override
//                                    public void execute() {
//                                        player.getPacketSender().sendMessage(
//                                                "You throw your Mithril grapple over the pillar and move across.");
//                                        player.moveTo(new Position(2871,
//                                                player.getPosition().getY() <= 5270 ? 5279 : 5269, 2));
//                                        stop();
//                                    }
//                                });
//                                player.getClickDelay().reset();
//                            }
//                            break;
                        case 4493:
                            if (player.getPosition().getX() >= 3432) {
                                player.moveTo(new Position(3433, 3538, 1));
                            }
                            break;
                        case 4494:
                            player.moveTo(new Position(3438, 3538, 0));
                            break;
                        case 4495:
                            player.moveTo(new Position(3417, 3541, 2));
                            break;
                        case 4496:
                            player.moveTo(new Position(3412, 3541, 1));
                            break;
//                        case 2491:
//                            player.setDialogueActionId(48);
//                            DialogueManager.start(player, 87);
//                            break;
                        case 25339:
                        case 25340:
                            player.moveTo(new Position(1778, 5346, player.getPosition().getZ() == 0 ? 1 : 0));
                            break;
                        case 10229:
                        case 10230:
                            boolean up = id == 10229;
                            player.performAnimation(new Animation(up ? 828 : 827));
                            player.getPacketSender().sendMessage("You climb " + (up ? "up" : "down") + " the ladder..");
                            TaskManager.submit(new Task(1, player, false) {
                                @Override
                                protected void execute() {
                                    player.moveTo(up ? new Position(1912, 4367) : new Position(2900, 4449));
                                    stop();
                                }
                            });
                            break;
                        case 1568:
                            player.moveTo(new Position(3097, 9868));
                            break;

//                        case 57225:
//                            if (!player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
//                                player.setDialogueActionId(44);
//                                DialogueManager.start(player, 79);
//                            } else {
//                                player.moveTo(new Position(2906, 5204));
//                                player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(false);
//                            }
//                            break;
                        // open dialogueopti
                        // Trying to make this object open a dialouge.
                        // and when clicking on the fi

                        case 1755:
                            player.performAnimation(new Animation(828));
                            player.getPacketSender().sendMessage("You climb the ladder..");
                            TaskManager.submit(new Task(1, player, false) {
                                @Override
                                protected void execute() {
                                    if (gameObject.getPosition().getX() == 2547
                                            && gameObject.getPosition().getY() == 9951) {
                                        player.moveTo(new Position(2548, 3551));
                                    } else if (gameObject.getPosition().getX() == 3005
                                            && gameObject.getPosition().getY() == 10363) {
                                        player.moveTo(new Position(3005, 3962));
                                    } else if (gameObject.getPosition().getX() == 3084
                                            && gameObject.getPosition().getY() == 9672) {
                                        player.moveTo(new Position(3117, 3244));
                                    } else if (gameObject.getPosition().getX() == 3097
                                            && gameObject.getPosition().getY() == 9867) {
                                        player.moveTo(new Position(3094, 3480));
                                    } else if (gameObject.getPosition().getX() == 3209
                                            && gameObject.getPosition().getY() == 9616) {
                                        player.moveTo(new Position(3210, 3216));
                                    }
                                    stop();
                                }
                            });
                            break;
                        case 28742:
                            player.performAnimation(new Animation(827));
                            player.getPacketSender().sendMessage("You enter the trapdoor..");
                            TaskManager.submit(new Task(1, player, false) {
                                @Override
                                protected void execute() {
                                    player.moveTo(new Position(3209, 9617));
                                    stop();
                                }
                            });
                            break;
                        case 5110:
                            player.moveTo(new Position(2647, 9557));
                            player.getPacketSender().sendMessage("You pass the stones..");
                            break;
                        case 5111:
                            player.moveTo(new Position(2649, 9562));
                            player.getPacketSender().sendMessage("You pass the stones..");
                            break;
                        case 6434:
                            player.performAnimation(new Animation(827));
                            player.getPacketSender().sendMessage("You enter the trapdoor..");
                            TaskManager.submit(new Task(1, player, false) {
                                @Override
                                protected void execute() {
                                    player.moveTo(new Position(3085, 9672));
                                    stop();
                                }
                            });
                            break;
                        case 19187:
                        case 19175:
                            Hunter.dismantle(player, gameObject);
                            break;
                        case 25029:
                            PuroPuro.goThroughWheat(player, gameObject);
                            break;
                        case 47976:
                            Nomad.endFight(player, false);
                            break;
                        case 2182:
                            if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
                                player.getPacketSender()
                                        .sendMessage("You have no business with this chest. Talk to the Gypsy first!");
                                return;
                            }
                            RecipeForDisaster.openRFDShop(player);
                            break;
                        case 12356:
                            if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
                                player.getPacketSender()
                                        .sendMessage("You have no business with this portal. Talk to the Gypsy first!");
                                return;
                            }
                            if (player.getPosition().getZ() > 0) {
                                RecipeForDisaster.leave(player);
                            } else {
                                player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(1,
                                        true);
                                RecipeForDisaster.enter(player);
                            }
                            break;
                        case 9369:
                            if (player.getPosition().getY() > 5175) {
                                FightPit.addPlayer(player);
                            } else {
                                FightPit.removePlayer(player, "leave room");
                            }
                            break;
                        case 9368:
                            if (player.getPosition().getY() < 5169) {
                                FightPit.removePlayer(player, "leave game");
                            }
                            break;
                        case 9356:
                            FightCave.enterCave(player);
                            break;
                        case 6704:
                            player.moveTo(new Position(3577, 3282, 0));
                            break;
                        case 6706:
                            player.moveTo(new Position(3554, 3283, 0));
                            break;
                        case 6705:
                            player.moveTo(new Position(3566, 3275, 0));
                            break;
                        case 6702:
                            player.moveTo(new Position(3564, 3289, 0));
                            break;
                        case 6703:
                            player.moveTo(new Position(3574, 3298, 0));
                            break;
                        case 6707:
                            player.moveTo(new Position(3556, 3298, 0));
                            break;
                        case 3203:
                            if (player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
                                if (Dueling.checkRule(player, DuelRule.NO_FORFEIT)) {
                                    player.getPacketSender().sendMessage("Forfeiting has been disabled in this duel.");
                                    return;
                                }
                                player.getCombatBuilder().reset(true);
                                if (player.getDueling().duelingWith > -1) {
                                    Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
                                    if (duelEnemy == null)
                                        return;
                                    duelEnemy.getCombatBuilder().reset(true);
                                    duelEnemy.getMovementQueue().reset();
                                    duelEnemy.getDueling().duelVictory();
                                }
                                player.moveTo(new Position(3368 + Misc.getRandom(5), 3267 + Misc.getRandom(3), 0));
                                player.getDueling().reset();
                                player.getCombatBuilder().reset(true);
                                player.restart();
                            }
                            break;
                        case 14315:
                            PestControl.boardBoat(player);
                            break;
                        case 14314:
                            if (player.getLocation() == Location.PEST_CONTROL_BOAT) {
                                player.getLocation().leave(player);
                            }
                            break;
                        case 2145:
                            player.getPacketSender().sendMessage("There's no good reason to disturb that.");
                            break;
                        case 1738:
                            if (gameObject.getPosition().getX() == 3204 && gameObject.getPosition().getY() == 3207
                                    && player.getPosition().getZ() == 0) {
                                player.moveTo(
                                        new Position(player.getPosition().getX(), player.getPosition().getY(), 1));
                            } else if (player.getLocation() == Location.WARRIORS_GUILD) {
                                player.moveTo(new Position(2840, 3539, 2));
                            }
                            break;
                        case 1739:
                            break;
                        case 15638:
                            if (player.getLocation() == Location.WARRIORS_GUILD) {
                                player.moveTo(new Position(2840, 3539, 0));
                            }
                            break;
                        case 1740:

                            break;
                        case 15644:
                        case 15641:
                            switch (player.getPosition().getZ()) {
                                case 0 ->
                                        player.moveTo(new Position(2855, player.getPosition().getY() >= 3546 ? 3545 : 3546));
//                                case 2 -> {
//                                    if (player.getPosition().getX() == 2846) {
//                                        if (player.getInventory().getAmount(8851) < 70) {
//                                            player.getPacketSender()
//                                                    .sendMessage("You need at least 70 tokens to enter this area.");
//                                            return;
//                                        }
//                                        DialogueManager.start(player, WarriorsGuild.warriorsGuildDialogue(player));
//                                        player.moveTo(new Position(2847, player.getPosition().getY(), 2));
//                                        WarriorsGuild.handleTokenRemoval(player);
//                                    } else if (player.getPosition().getX() == 2847) {
//                                        WarriorsGuild.resetCyclopsCombat(player);
//                                        player.moveTo(new Position(2846, player.getPosition().getY(), 2));
//                                        player.getMinigameAttributes().getWarriorsGuildAttributes()
//                                                .setEnteredTokenRoom(false);
//                                    }
//                                }
                            }
                            break;
                        case 28714:
                            player.performAnimation(new Animation(828));
                            player.delayedMoveTo(new Position(2655, 4017), 2);
                            break;
                        case 26933:
                            player.performAnimation(new Animation(827));
                            player.delayedMoveTo(new Position(3096, 9867), 2);
                            break;
                        case 1746:
                            player.performAnimation(new Animation(827));
                            player.delayedMoveTo(new Position(2209, 5348), 2);
                            break;
                        case 19191:
                        case 19189:
                        case 19180:
                        case 19184:
                        case 19182:
                        case 19178:
                            Hunter.lootTrap(player, gameObject);
                            break;
                        case 13493:
                            if (!player.getDonator().isClericPlus()) {
                                player.getPacketSender().sendMessage("You must be a Member to use this.");
                                return;
                            }
                            double c = Math.random() * 100;
                            int reward = c >= 70 ? 13003
                                    : c >= 45 ? 4131
                                    : c >= 35 ? 1113
                                    : c >= 25 ? 1147
                                    : c >= 18 ? 1163 : c >= 12 ? 1079 : c >= 5 ? 1201 : 1127;
                            Stalls.stealFromStall(player, gameObject, 95, 121, new Item(reward), "You stole some rune equipment.");
                            break;
                        case 22772:
                            Stalls.stealFromStall(player, gameObject, 1, 50, new Item(ItemDefinition.COIN_ID, 1000 + Misc.getRandom(4000)), "You stole some coins.");
                            break;
                        case 22774:
                            Stalls.stealFromStall(player, gameObject, 90, 250, new Item(ItemDefinition.COIN_ID, 3000 + Misc.getRandom(7000)), "You stole some coins.");
                            break;
//                        case 30205:
//                            player.setDialogueActionId(11);
//                            DialogueManager.start(player, 20);
//                            break;
                        case 6:
                            DwarfCannon cannon = player.getCannon();
                            if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
                                player.getPacketSender().sendMessage("This is not your cannon!");
                            } else {
                                DwarfMultiCannon.startFiringCannon(player, cannon);
                            }
                            break;
                        case 2:
                            player.moveTo(new Position(player.getPosition().getX() > 2690 ? 2687 : 2694, 3714));
                            player.getPacketSender().sendMessage("You walk through the entrance..");
                            break;
                        case 2026:
                        case 2028:
                        case 2029:
                        case 2030:
                        case 2031:
                            player.setEntityInteraction(gameObject);
                            Fishing.setupFishing(player, Fishing.forSpot(gameObject.getId(), false));
                            return;
                        case 12692:
                        case 2783:
                        case 2782:
                        case 4306:
                            player.setInteractingObject(gameObject);
                            EquipmentMaking.handleAnvil(player);
                            break;
                        case 2732:
                        case 11404:
                        case 11406:
                        case 11405:
                        case 20000:
                        case 20001:
                            EnterAmountOfLogsToAdd.openInterface(player);
                            break;
                        case 24343:
                        case 409:
                        case 27661:
                        case 2640:
                        case 36972, 13192, 4859:
                            player.performAnimation(new Animation(645));
                            if (player.getNewSkills().getCurrentLevel(S_Skills.PRAYER) < player.getNewSkills()
                                    .getMaxLevel(S_Skills.PRAYER)) {
                                player.getNewSkills().setCurrentLevel(S_Skills.PRAYER,
                                        player.getNewSkills().getMaxLevel(S_Skills.PRAYER), true);
                                player.getPacketSender().sendMessage("You recharge your Prayer points.");
                            }
                            break;
                        case 6552:
                            player.performAnimation(new Animation(645));
                            player.setSpellbook(player.getSpellbook() == MagicSpellbook.ANCIENT ? MagicSpellbook.NORMAL
                                    : MagicSpellbook.ANCIENT);
                            player.getPacketSender()
                                    .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
                                    .sendMessage("Your magic spellbook is changed..");
                            Autocasting.resetAutocast(player, true);
                            break;
//                        case 410:
//                            if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
//                                player.getPacketSender()
//                                        .sendMessage("You need a Defence level of at least 40 to use this altar.");
//                                return;
//                            }
//                            player.performAnimation(new Animation(645));
//                            player.setSpellbook(player.getSpellbook() == MagicSpellbook.LUNAR ? MagicSpellbook.NORMAL
//                                    : MagicSpellbook.LUNAR);
//                            player.getPacketSender()
//                                    .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
//                                    .sendMessage("Your magic spellbook is changed..");
//                            ;
//                            Autocasting.resetAutocast(player, true);
//                            break;
                        case 452:
                            player.getPacketSender().sendMessage("There's no ore in that rock.");
                            break;
                       /* case 172:
                            int[] commonpvm = new int[]{13734, 3140, 4087, 2631, 2643, 9470, 2570, 11710, 11712,
                                    11714, 15332, 6918, 6916, 6924, 6922, 6920, 7400, 7399, 7398};
                            int[] uncommonpvm = new int[]{6199, 18350, 18356, 18352, 18354, 7956, 290, 989};
                            int[] rarepvm = new int[]{6571, 11137, 3905, 20535, 17704, 11716, 455, 3321, 3319,
                                    3322, 3320, 3318, 3323, 10946};
                            player.getMysteryBoxOpener().display(989, "Crystal Key", commonpvm, uncommonpvm, rarepvm);
                            break;*/
                        // CrystalChest.handleChest(player, gameObject, false);
                        // break;
                        case 10620:
                            EliteChest.handleChest(player, gameObject, false);
                            break;

                        case 6910:
                        case 4483:
                        case 3193:
                        case 2213:
                        case 11758:
                        case 14367:
                        case 42192:
                        case 75:
                        case 26972:
                        case 11338:
                        case 19230:
                        case 59732:
                        case 25808:
                        case 6084:
                        case 3194:
                            if (player.getMode() instanceof GroupIronman
                                    && player.getIronmanGroup() != null) {
//                                DialogueManager.start(player, 8002);
//                                player.setDialogueActionId(8002);
                            } else {
                                player.getBank(player.getCurrentBankTab()).open();
                            }
                            break;

                        case 11666:
                        case 23963:
                            Smelting.openInterface(player);
                            break;
                    }
                }));
    }

    private static void secondClick(final Player player, Packet packet) {
        final int id = packet.readUnsignedShort();
        final int y = packet.readLEShort();
        final int x = packet.readUnsignedShortA();
        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (id > 0 && id != 6 && !RegionClipping.objectExists(gameObject)) {
            // player.getPacketSender().sendMessage("An error occured. Error code:
            // "+id).sendMessage("Please report the error to a staff member.");
            return;
        }
        player.setPositionToFace(gameObject.getPosition());
        int distanceX = (player.getPosition().getX() - position.getX());
        int distanceY = (player.getPosition().getY() - position.getY());
        if (distanceX < 0)
            distanceX = -(distanceX);
        if (distanceY < 0)
            distanceY = -(distanceY);
        int size = Math.max(distanceX, distanceY);
        gameObject.setSize(size);
        if (player.getRank().isDeveloper())
            player.getPacketSender()
                    .sendMessage("Second click object id; [id, position] : [" + id + ", " + position.toString() + "]");
        player.setInteractingObject(gameObject)
                .setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), () -> {

//                    if (MiningData.forRock(gameObject.getId()) != null) {
//                        Prospecting.prospectOre(player, id);
//                        return;
//                    }
                    if (!player.getControllerManager().processObjectClick2(gameObject)) {
                        return;
                    }
//                    if (player.getFarming().click(player, x, y, 1))
//                        return;
//                    if (player.getPosition().getRegionId() == 7758) {//vod
//                        player.vod.handleObject(gameObject);
//                        return;
//                    }

                    if(player.getInstance() != null){
                        if(player.getInstance().handleObjectClick(player, gameObject, 2)){
                            return;
                        }
                    }

                    if(World.handler.handleObjectClick(player, gameObject.getId(), 2)){
                        return;
                    }

                    switch (gameObject.getId()) {
                        case 2469 -> TeleportHandler.teleportPlayer(player, player.getPosition().setZ(Math.max(player.getPosition().getZ() - 4, 0)), TeleportType.NORMAL);
                        case 12100 -> Smelting.openInterface(player);
                        case 13192 -> {
                            player.performAnimation(new Animation(645));
                            if (player.getPrayerbook() == Prayerbook.CURSES) {
                                if(player.getPSettings().getBooleanValue("holy-unlock")) {
                                    player.getPacketSender().sendMessage("You sense a surge of holiness flow through your body!");
                                    CurseHandler.deactivateAll(player);
                                    CurseHandler.startDrain(player);
                                    player.setPrayerbook(Prayerbook.HOLY);
                                } else {
                                    player.getPacketSender().sendMessage("You need to unlock this prayer book first");
                                    return;
                                }
                            } else if (player.getPrayerbook() == Prayerbook.HOLY) {
                                player.getPacketSender().sendMessage("You sense a surge of power flow through your body!");
                                PrayerHandler.deactivateAll(player);
                                PrayerHandler.startDrain(player);
                                player.setPrayerbook(Prayerbook.CURSES);
                            }
                            PrayerHandler.deactivateAll(player);
                            CurseHandler.deactivateAll(player);
                            player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB,
                                    player.getPrayerbook().getInterfaceId());
                            CurseHandler.startDrain(player);
                            startDrain(player);
                            player.switchedPrayerBooks = true;
                        }
                        /*case 2562:
                            if (player.getAmountDonated() <= 5000) {
                                player.sendMessage("You need $5k+ total claim to use this!");
                                return;
                            }
                            if (player.getAmountDonated() >= 5000) {
                                Stalls.stealFromAFKStall(player, id, 4);
                            }
                            break;*/

                        case 172 -> CrystalChest.sendRewardInterface(player);
//                        case 9975 -> {
//                            DialogueManager.start(player, 22);
//                            player.setDialogueActionId(14);
//                        }
                        case 6910, 4483, 25808, 3193, 2213, 11758, 14367, 42192, 75, 26972, 11338, 19230 ->
                                player.getBank(player.getCurrentBankTab()).open();

//                        case 2152 -> {
//                            player.performAnimation(new Animation(8502));
//                            player.performGraphic(new Graphic(1308));
//                            player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
//                                    player.getSkillManager().getMaxLevel(Skill.SUMMONING));
//                            player.getPacketSender().sendMessage("You renew your Summoning points.");
//                        }
                    }
                }));
    }

    public static void resetInterface(Player player) {
        for (int i = 8145; i < 8196; i++)
            player.getPacketSender().sendString(i, "");
        for (int i = 12174; i < 12224; i++)
            player.getPacketSender().sendString(i, "");
        player.getPacketSender().sendString(8136, "Close window");
    }

    private static void thirdClick(Player player, Packet packet) {
        final int x = packet.readShort();
        final int y = packet.readShort();
        final int id = packet.readLEShortA();

        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (!Construction.buildingHouse(player)) {
            if (id > 0 && !RegionClipping.objectExists(gameObject)) {
                // player.getPacketSender().sendMessage("An error occured. Error code:
                // "+id).sendMessage("Please report the error to a staff member.");
                return;
            }
        }
        player.setPositionToFace(gameObject.getPosition());
        int distanceX = (player.getPosition().getX() - position.getX());
        int distanceY = (player.getPosition().getY() - position.getY());
        if (distanceX < 0)
            distanceX = -(distanceX);
        if (distanceY < 0)
            distanceY = -(distanceY);
        int size = distanceX > distanceY ? distanceX : distanceY;
        gameObject.setSize(size);
        if (player.getRank().isDeveloper()) {
            player.getPacketSender()
                    .sendMessage("Third click object id; [id, position] : [" + id + ", " + position.toString() + "]");
        }
        player.setInteractingObject(gameObject);
        player.setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), () -> {
            if (!player.getControllerManager().processObjectClick3(gameObject)) {
                return;
            }

            if(player.getInstance() != null){
                if(player.getInstance().handleObjectClick(player, gameObject, 3)){
                    return;
                }
            }

            if(World.handler.handleObjectClick(player, gameObject.getId(), 3)){
                return;
            }

            switch (id) {

                case 13192:
                    player.performAnimation(new Animation(645));
                    player.setSpellbook(player.getSpellbook() == MagicSpellbook.ANCIENT ? MagicSpellbook.LUNAR
                            : player.getSpellbook() == MagicSpellbook.LUNAR ? MagicSpellbook.NORMAL
                            : MagicSpellbook.ANCIENT);
                    player.getPacketSender()
                            .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
                            .sendMessage("Your magic spellbook is changed..");
                    Autocasting.resetAutocast(player, true);
                    break;

            }
        }));

    }

    private static void fourthClick(Player player, Packet packet) {
        final int x = packet.readShort();
        final int y = packet.readShort();
        final int id = packet.readLEShortA();

        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        //System.out.println("id:" + id + " x " + x + " y " + y);

        if (gameObject == null) {
            return;
        }
        if (!player.getControllerManager().processObjectClick4(gameObject)) {
            return;
        }
    }

    private static void fifthClick(final Player player, Packet packet) {
        final int id = packet.readUnsignedShortA();
        final int y = packet.readUnsignedShortA();
        final int x = packet.readShort();
        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (!Construction.buildingHouse(player)) {
            if (id > 0 && !RegionClipping.objectExists(gameObject)) {
                // player.getPacketSender().sendMessage("An error occured. Error code:
                // "+id).sendMessage("Please report the error to a staff member.");
                return;
            }
        }
        player.setPositionToFace(gameObject.getPosition());
        int distanceX = (player.getPosition().getX() - position.getX());
        int distanceY = (player.getPosition().getY() - position.getY());
        if (distanceX < 0)
            distanceX = -(distanceX);
        if (distanceY < 0)
            distanceY = -(distanceY);
        int size = distanceX > distanceY ? distanceX : distanceY;
        gameObject.setSize(size);
        if (player.getRank().isDeveloper()) {
            player.getPacketSender()
                    .sendMessage("Third click object id; [id, position] : [" + id + ", " + position.toString() + "]");
        }
        player.setInteractingObject(gameObject);
        player.setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), () -> {

            if (!player.getControllerManager().processObjectClick5(gameObject)) {
                return;
            }

            switch (id) {
            }
            Construction.handleFifthObjectClick(x, y, id, player);
        }));
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.isTeleporting() || (player.isPlayerLocked() || player.isGroupIronmanLocked()) || player.getMovementQueue().isLockMovement())
            return;
        player.getAfk().setAFK(false);
        switch (packet.getOpcode()) {
            case FIRST_CLICK:
                firstClick(player, packet);
                if (player.getRank().isDeveloper()) {
                    player.getPacketSender().sendMessage("1st click obj");
                }
                break;
            case SECOND_CLICK:
                secondClick(player, packet);
                if (player.getRank().isDeveloper()) {
                    player.getPacketSender().sendMessage("2nd click obj");
                }
                break;
            case THIRD_CLICK:
                if (player.getRank().isDeveloper()) {
                    player.getPacketSender().sendMessage("3rd click obj");
                }
                thirdClick(player, packet);
                break;
            case FOURTH_CLICK:
                if (player.getRank().isDeveloper()) {
                    player.getPacketSender().sendMessage("4th click obj. no handler");
                }
                // fourthClick(player, packet);
                break;
            case FIFTH_CLICK:
                fifthClick(player, packet);
                if (player.getRank().isDeveloper()) {
                    player.getPacketSender().sendMessage("5th click obj");
                }
                break;
        }
    }

    private static void handleDoor(Player player, @NotNull GameObject object){
        switch(object.getId()){
            case 2912, 2913 -> {
                if(object.getPosition().equals(new Position(3033, 2876, player.getIndex() * 4))){
                    if(player.getPosition().getX() > object.getPosition().getX()){
                        player.moveTo(new Position(object.getPosition().getX() - 1, object.getPosition().getY(), object.getPosition().getZ()));
                    } else {
                        player.moveTo(new Position(object.getPosition().getX() + 1, object.getPosition().getY(), object.getPosition().getZ()));
                    }
                } else if(object.getPosition().equals(new Position(3033, 2877, player.getIndex() * 4))){
                    if(player.getPosition().getX() > object.getPosition().getX()){
                        player.moveTo(new Position(object.getPosition().getX() - 1, object.getPosition().getY(), object.getPosition().getZ()));
                    } else {
                        player.moveTo(new Position(object.getPosition().getX() + 1, object.getPosition().getY(), object.getPosition().getZ()));
                    }
                } else if(object.getPosition().equals(new Position(3056, 2848, player.getIndex() * 4))){
                    if(player.getPosition().getY() > object.getPosition().getY()){
                        player.moveTo(new Position(object.getPosition().getX(), object.getPosition().getY() - 1, object.getPosition().getZ()));
                    } else {
                        player.moveTo(new Position(object.getPosition().getX(), object.getPosition().getY() + 1, object.getPosition().getZ()));
                    }
                } else if(object.getPosition().equals(new Position(3057, 2848, player.getIndex() * 4))){
                    if(player.getPosition().getY() > object.getPosition().getY()){
                        player.moveTo(new Position(object.getPosition().getX(), object.getPosition().getY() - 1, object.getPosition().getZ()));
                    } else {
                        player.moveTo(new Position(object.getPosition().getX(), object.getPosition().getY() + 1, object.getPosition().getZ()));
                    }
                } else if(object.getPosition().equals(new Position(3024, 2822, player.getIndex() * 4))){
                    if(player.getPosition().getY() > object.getPosition().getY()){
                        player.moveTo(new Position(object.getPosition().getX(), object.getPosition().getY() - 1, object.getPosition().getZ()));
                    } else {
                        player.moveTo(new Position(object.getPosition().getX(), object.getPosition().getY() + 1, object.getPosition().getZ()));
                    }
                } else if(object.getPosition().equals(new Position(3023, 2822, player.getIndex() * 4))){
                    if(player.getPosition().getY() > object.getPosition().getY()){
                        player.moveTo(new Position(object.getPosition().getX(), object.getPosition().getY() - 1, object.getPosition().getZ()));
                    } else {
                        player.moveTo(new Position(object.getPosition().getX(), object.getPosition().getY() + 1, object.getPosition().getZ()));
                    }
                }
            }
        }
    }
}
