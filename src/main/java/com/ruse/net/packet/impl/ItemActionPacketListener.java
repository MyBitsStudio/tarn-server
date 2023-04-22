package com.ruse.net.packet.impl;

import com.ruse.engine.task.TaskManager;
import com.ruse.engine.task.impl.*;
import com.ruse.model.*;
import com.ruse.model.Locations.Location;
import com.ruse.model.container.impl.Bank;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.*;
import com.ruse.world.content.Sounds.Sound;
import com.ruse.world.content.StarterTasks.StarterTaskData;
import com.ruse.world.content.achievement.Achievements;
import com.ruse.world.content.casketopening.Box;
import com.ruse.world.content.casketopening.BoxLoot;
import com.ruse.world.content.casketopening.CasketOpening;
import com.ruse.world.content.casketopening.impl.EliteSlayerCasket;
import com.ruse.world.content.casketopening.impl.SlayerCasket;
import com.ruse.world.content.cluescrolls.ClueScroll;
import com.ruse.world.content.cluescrolls.ClueScrollReward;
import com.ruse.world.content.cluescrolls.OLD_ClueScrolls;
import com.ruse.world.content.combat.prayer.PrayerHandler;
import com.ruse.world.content.dialogue.DialogueManager;
import com.ruse.world.content.holidayevents.easter2017;
import com.ruse.world.content.skill.impl.herblore.Herblore;
import com.ruse.world.content.skill.impl.herblore.ingredientsBook;
import com.ruse.world.content.skill.impl.hunter.*;
import com.ruse.world.content.skill.impl.hunter.Trap.TrapState;
import com.ruse.world.content.skill.impl.old_dungeoneering.ItemBinding;
import com.ruse.world.content.skill.impl.prayer.Prayer;
import com.ruse.world.content.skill.impl.runecrafting.RunecraftingPouches;
import com.ruse.world.content.skill.impl.runecrafting.RunecraftingPouches.RunecraftingPouch;
import com.ruse.world.content.skill.impl.slayer.SlayerDialogues;
import com.ruse.world.content.skill.impl.slayer.SlayerTasks;
import com.ruse.world.content.skill.impl.summoning.CharmingImp;
import com.ruse.world.content.skill.impl.summoning.SummoningData;
import com.ruse.world.content.skill.impl.woodcutting.BirdNests;
import com.ruse.world.content.transportation.*;
import com.ruse.world.entity.impl.player.Player;

import java.text.NumberFormat;
import java.util.Locale;

public class ItemActionPacketListener implements PacketListener {

    public static final int THIRD_ITEM_ACTION_OPCODE = 75;
    public static final int FIRST_ITEM_ACTION_OPCODE = 122;
    public static final int SECOND_ITEM_ACTION_OPCODE = 16;
    private static final String[] ROCK_CAKE = {"Oww!", "Ouch!", "Owwwy!", "I nearly broke a tooth!", "My teeth!",
            "Who would eat this?", "*grunt*", ":'("};
    public static int count = 0;

    public static boolean drinkSuperOverload(final Player player, int slot, int replacePotion) {
        if (player.getLocation() == Location.WILDERNESS || player.getLocation() == Location.DUEL_ARENA) {
            player.getPacketSender().sendMessage("You cannot use this potion here.");
            return false;
        }
        if (player.getOverloadPotionTimer() > 0 && player.getOverloadPotionTimer() < 750) {
            player.getPacketSender().sendMessage("You already have the effect of an Overload or Super/Infinity Overload potion.");
            return false;
        }
        if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) < 500) {
            player.getPacketSender().sendMessage("You need to have at least 500 Hitpoints to drink this potion.");
            return false;
        }
        player.performAnimation(new Animation(829));
        player.getInventory().getItems()[slot] = new Item(replacePotion, 1);
        player.getInventory().refreshItems();
        player.setOverloadPotionTimer(600);
        player.setPotionUsed("Super Ovl");
        TaskManager.submit(new SuperOverloadPotionTask(player));
        return true;
    }

    public static boolean drinkInfinityRage(final Player player, int slot, int replacePotion) {
        if (player.getLocation() == Location.WILDERNESS || player.getLocation() == Location.DUEL_ARENA) {
            player.getPacketSender().sendMessage("You cannot use this potion here.");
            return false;
        }
        if (player.getOverloadPotionTimer() > 0 && player.getOverloadPotionTimer() < 750) {
            player.getPacketSender().sendMessage("You already have the effect of an Overload or Super/Infinity Overload potion.");
            return false;
        }
        if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) < 500) {
            player.getPacketSender().sendMessage("You need to have at least 500 Hitpoints to drink this potion.");
            return false;
        }
        player.performAnimation(new Animation(829));
        player.getInventory().getItems()[slot] = new Item(replacePotion, 1);
        player.getInventory().refreshItems();
        player.setOverloadPotionTimer(600);
        player.setPotionUsed("Rage");
        TaskManager.submit(new InfinityRagePotionTask(player));
        return true;
    }

    private static void firstAction(final Player player, Packet packet) {
        int interfaceId = packet.readUnsignedShort();
        int slot = packet.readShort();
        int itemId = packet.readShort();
        /*
         * if(interfaceId == 38274) { Construction.handleItemClick(itemId, player);
         * return; }
         */
        if (Misc.checkForOwner()) {
            // System.out.println("Slot: " + slot + ", itemId: " + itemId + ", interface: " + interfaceId);
        }
        if (slot < 0 || slot > player.getInventory().capacity())
            return;
        if (player.getInventory().getItems()[slot].getId() != itemId)
            return;
        Item item = player.getInventory().getItems()[slot];
        player.setInteractingItem(item);
        if (!player.getControllerManager().processItemClick1(slot, item)) {
            return;
        }
        if (Prayer.isBone(itemId)) {
            Prayer.buryBone(player, itemId);
            return;
        }
        if (Consumables.isFood(player, itemId, slot))
            return;
        if (Consumables.isPotion(itemId)) {
            Consumables.handlePotion(player, itemId, slot);
            return;
        }

        if (BirdNests.isNest(itemId)) {
            BirdNests.searchNest(player, itemId);
            return;
        }
        if (Herblore.cleanHerb(player, itemId))
            return;
        if (MemberScrolls.handleScroll(player, itemId, false))
            return;
        if (Effigies.isEffigy(itemId)) {
            Effigies.handleEffigy(player, itemId);
            return;
        }
        if (ExperienceLamps.handleLamp(player, itemId)) {
            return;
        }

        if(SECertificateType.playerConsume(player, itemId))
            return;

        if (player.aonBoxItem > 0) {
            player.sendMessage("Please choose to keep or gamble your item before doing this!");
            return;
        }
        if (player.getInventory().containsAny(11846, 11848, 11850, 11852, 11854, 11856) && !player.getInventory().containsAny(11846, 11848, 11850, 11852, 11854, 11856)) {
            if (!player.getClickDelay().elapsed(250) || !player.getInventory().contains(itemId))
                return;
            if (player.busy()) {
                player.getPacketSender().sendMessage("You cannot open this right now.");
                return;
            }

            int[] items = itemId == 11858 ? new int[]{10350, 10348, 10346, 10352}
                    : itemId == 19580 ? new int[]{19308, 19311, 19314, 19317, 19320}
                    : itemId == 11860 ? new int[]{10334, 10330, 10332, 10336}
                    : itemId == 11862 ? new int[]{10342, 10338, 10340, 10344}
                    : itemId == 11848 ? new int[]{4716, 4720, 4722, 4718}
                    : itemId == 11856 ? new int[]{4753, 4757, 4759, 4755}
                    : itemId == 11850 ? new int[]{4724, 4728, 4730, 4726}
                    : itemId == 11854
                    ? new int[]{4745, 4749, 4751, 4747}
                    : itemId == 11852
                    ? new int[]{4732, 4734, 4736,
                    4738}
                    : itemId == 11846
                    ? new int[]{4708, 4712,
                    4714, 4710}
                    : new int[]{itemId};

            if (player.getInventory().getFreeSlots() < items.length) {
                player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
                return;
            }
            player.getInventory().delete(itemId, 1);
            for (int i : items) {
                player.getInventory().add(i, 1);
            }
            player.getPacketSender().sendMessage("You open the set and find items inside.");
            player.getClickDelay().reset();
            return;
        }


        switch (itemId) {

            case 3253:
                //	player.getInventory().delete(itemId, 1);
                player.getMinimeSystem().spawn();
                break;

            case 7629:
                player.getMiniPManager().spawnMiniPlayer();
                break;
            case 9719:
                player.sendMessage("You must bring this to your slayer master to cancel your task.");
                break;

            case 23177:
                player.getInventory().delete(23177, 1);
                player.getInventory().add(23139, 1);
                player.getInventory().add(23140, 1);
                player.getInventory().add(23141, 1);
                player.getInventory().add(23142, 1);
                player.getInventory().add(23143, 1);
                break;

            case PrayerHandler.HOLY_SCROLL_DESTRUCTION_ITEM:
                if(!player.isHolyPrayerUnlocked(PrayerHandler.HOLY_DESTRUCTION_IDX)) {
                    player.setUnlockedHolyPrayer(PrayerHandler.HOLY_DESTRUCTION_IDX, true);
                    player.getInventory().delete(PrayerHandler.HOLY_SCROLL_DESTRUCTION_ITEM, 1);
                    player.sendMessage("You unlock the Warlock holy prayer.");
                    World.sendMessage("<shad=15536940><img=5> " + player.getUsername()
                            + " has unlocked the " + "<col=ff4f4f>Warlock " + "prayer!");
                } else {
                    player.sendMessage("You have already unlocked this prayer!");
                }
                break;
            case PrayerHandler.HOLY_SCROLL_HUNTERS_EYE_ITEM:
                if(!player.isHolyPrayerUnlocked(PrayerHandler.HOLY_HUNTERS_EYE_IDX)) {
                    player.setUnlockedHolyPrayer(PrayerHandler.HOLY_HUNTERS_EYE_IDX, true);
                    player.getInventory().delete(PrayerHandler.HOLY_SCROLL_HUNTERS_EYE_ITEM, 1);
                    player.sendMessage("You unlock the Knight holy prayer.");
                    World.sendMessage("<shad=15536940><img=5> " + player.getUsername()
                            + " has unlocked the " + "<col=ff4f4f>Knight " + "prayer!");
                } else {
                    player.sendMessage("You have already unlocked this prayer!");
                }
                break;
            case PrayerHandler.HOLY_SCROLL_FORTITUDE_ITEM:
                if(!player.isHolyPrayerUnlocked(PrayerHandler.HOLY_FORTITUDE_IDX)) {
                    player.setUnlockedHolyPrayer(PrayerHandler.HOLY_FORTITUDE_IDX, true);
                    player.getInventory().delete(PrayerHandler.HOLY_SCROLL_FORTITUDE_ITEM, 1);
                    player.sendMessage("You unlock the Marksman holy prayer.");
                    World.sendMessage("<shad=15536940><img=5> " + player.getUsername()
                            + " has unlocked the " + "<col=ff4f4f>Marksman " + "prayer!");
                } else {
                    player.sendMessage("You have already unlocked this prayer!");
                }
                break;
            case PrayerHandler.HOLY_SCROLL_GNOMES_GREED_ITEM:
                if(!player.isHolyPrayerUnlocked(PrayerHandler.HOLY_GNOMES_GREED_IDX)) {
                    player.setUnlockedHolyPrayer(PrayerHandler.HOLY_GNOMES_GREED_IDX, true);
                    player.getInventory().delete(PrayerHandler.HOLY_SCROLL_GNOMES_GREED_ITEM, 1);
                    player.sendMessage("You unlock the Prosperous holy prayer.");
                    World.sendMessage("<shad=15536940><img=5> " + player.getUsername()
                            + " has unlocked the " + "<col=ff4f4f>Prosperous " + "prayer!");
                } else {
                    player.sendMessage("You have already unlocked this prayer!");
                }
                break;
            case PrayerHandler.HOLY_SCROLL_SOUL_LEECH_ITEM:
                if(!player.isHolyPrayerUnlocked(PrayerHandler.HOLY_SOUL_LEECH_IDX)) {
                    player.setUnlockedHolyPrayer(PrayerHandler.HOLY_SOUL_LEECH_IDX, true);
                    player.getInventory().delete(PrayerHandler.HOLY_SCROLL_SOUL_LEECH_ITEM, 1);
                    player.sendMessage("You unlock the Sovereignty holy prayer.");
                    World.sendMessage("<shad=15536940><img=5> " + player.getUsername()
                            + " has unlocked the " + "<col=ff4f4f>Sovereignty " + "prayer!");
                } else {
                    player.sendMessage("You have already unlocked this prayer!");
                }
                break;
            case PrayerHandler.HOLY_SCROLL_FURY_SWIPE_ITEM:
                if(!player.isHolyPrayerUnlocked(PrayerHandler.HOLY_FURY_SWIPE_IDX)) {
                    player.setUnlockedHolyPrayer(PrayerHandler.HOLY_FURY_SWIPE_IDX, true);
                    player.getInventory().delete(PrayerHandler.HOLY_SCROLL_FURY_SWIPE_ITEM, 1);
                    player.sendMessage("You unlock the Trinity holy prayer.");
                    World.sendMessage("<shad=15536940><img=5> " + player.getUsername()
                            + " has unlocked the " + "<col=ff4f4f>Trinity " + "prayer!");
                } else {
                    player.sendMessage("You have already unlocked this prayer!");
                }
                break;

            case 2677:
                if (ClueScroll.readClue(player, ClueScroll.EASY)) {
                    player.getInventory().delete(itemId, 1);
                    ClueScrollReward reward = ClueScrollReward.getWeightedReward(ClueScroll.EASY);
                    player.getInventory().add(reward.getItemId(), Misc.getRandom(reward.getMinAmt(), reward.getMaxAmt()));

                }
                break;
            case 2831:
                if (ClueScroll.readClue(player, ClueScroll.MEDIUM)) {
                    player.getInventory().delete(itemId, 1);
                    ClueScrollReward reward = ClueScrollReward.getWeightedReward(ClueScroll.MEDIUM);
                    player.getInventory().add(reward.getItemId(), Misc.getRandom(reward.getMinAmt(), reward.getMaxAmt()));
                    player.getCurrentClue().setLastTask(player.getCurrentClue().getCurrentTask());
                    player.getCurrentClue().setCurrentTask(SlayerTasks.NO_TASK);

                }
                break;
            case 2773:
                if (ClueScroll.readClue(player, ClueScroll.HARD)) {
                    player.getInventory().delete(itemId, 1);
                    ClueScrollReward reward = ClueScrollReward.getWeightedReward(ClueScroll.HARD);
                    player.getInventory().add(reward.getItemId(), Misc.getRandom(reward.getMinAmt(), reward.getMaxAmt()));
                }
                break;
            case 23002:
                player.getWheelOfFortune().open();
                break;
            case 22108:
                CurrencyPouch.checkBalance(player);
                break;
            case 745:
                if (!player.getClickDelay().elapsed(100)) {
                    player.getPacketSender().sendMessage("Please wait before doing that.");
                    break;
                }

                if (player.getInventory().getFreeSlots() >= 6) {
                    player.getInventory().add(13557, 1);
                    player.getInventory().add(10942, 1);
                    player.getInventory().add(20489, 1);
                    player.getInventory().add(15002, 1);
                    player.getInventory().add(15003, 1);
                    player.getInventory().add(15004, 1);
                    player.getPacketSender().sendMessage("Enjoy your reward!");
                    World.sendMessage("<shad=1>@yel@[<img=18>" + player.getUsername() + "<img=18>] @whi@Killed 10K @red@Valentine's Boxes@whi@ and opened his Heart crystal! Happy Valentine's day!");
                    player.getInventory().delete(745, 1);
                    break;
                } else {
                    player.getPacketSender().sendMessage("You'll need at least 6 free spaces to do that.");
                }

                player.getClickDelay().reset();
                break;
            case 13802:
                if (!player.getClickDelay().elapsed(100)) {
                    player.getPacketSender().sendMessage("Please wait before doing that.");
                    break;
                }

                if (player.getInventory().getFreeSlots() >= 8) {
                    player.getInventory().delete(13802, 1);
                    player.getInventory().add(10942, 1);
                    player.getInventory().add(20489, 1);
                    player.getInventory().add(15002, 1);
                    player.getInventory().add(15003, 2);
                    player.getInventory().add(19114, 10);
                    player.getInventory().add(19115, 10);
                    player.getInventory().add(19116, 10);
                    player.getInventory().add(20488, 5);
                    player.getPacketSender().sendMessage("Enjoy your reward!");
                    World.sendMessage("<shad=1>@yel@[<img=18>" + player.getUsername() + "<img=18>] @whi@Killed 10K @red@St. Patrick Leprechauns@whi@ and opened his St. Pat Box!");
                    break;
                } else {
                    player.getPacketSender().sendMessage("You'll need at least 8 free spaces to do that.");
                }

                player.getClickDelay().reset();
                break;

            case 19000:
                DialogueManager.start(player, PetUpgrading.dialogue(player));
                break;

            case 15355:
                if (player.getDoubleDRTimer() > 0) {
                    player.sendMessage("You already have a double DR scroll active.");
                    return;
                }
                player.getInventory().delete(15355, 1);
                player.setDoubleDRTimer(6000);
                TaskManager.submit(new DoubleDRTask(player));
                break;
            case 15356:
                if (player.getDoubleDDRTimer() > 0) {
                    player.sendMessage("You already have a double DDR scroll active.");
                    return;
                }
                player.getInventory().delete(15356, 1);
                player.setDoubleDDRTimer(6000);
                TaskManager.submit(new DoubleDDRTask(player));
                break;
            case 15357:
                if (player.getDoubleDMGTimer() > 0) {
                    player.sendMessage("You already have a double DMG scroll active.");
                    return;
                }
                player.getInventory().delete(15357, 1);
                player.setDoubleDMGTimer(6000);
                TaskManager.submit(new DoubleDMGTask(player));
                break;
            case 15358:
                if (player.getDoubleDRTimer() > 0) {
                    player.sendMessage("You already have a double DR scroll active.");
                    return;
                }
                player.getInventory().delete(15358, 1);
                player.setDoubleDRTimer(3000);
                TaskManager.submit(new DoubleDRTask(player));
                break;
            case 15359:
                if (player.getDoubleDMGTimer() > 0) {
                    player.sendMessage("You already have a double DMG scroll active.");
                    return;
                }
                player.getInventory().delete(15359, 1);
                player.setDoubleDMGTimer(3000);
                TaskManager.submit(new DoubleDMGTask(player));
                break;


            case 23171:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.RARE_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 14487:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.DEF_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 15003:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.SILVER_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 15002:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.RUBY_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 15004:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.DIAMOND_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 20489:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.PREMIUM_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 20490:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.ZENYTE_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 20491:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.ONYX_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 19624:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.ELITE_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 18404:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.RAIDS_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 20488:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.SUPREME_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 14488:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.OFF_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 14490:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.DEATH_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 14492:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.AFREET_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 19114:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.WEAPON_BOX);
                player.getCasketOpening().openInterface();
                break;
            case 10025:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.PROG_BOX_T1);
                player.getCasketOpening().openInterface();
                break;
            case 10029:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.PROG_BOX_T2);
                player.getCasketOpening().openInterface();
                break;
            case 10027:
                player.getCasketOpening().setCurrentCasket(CasketOpening.Caskets.PROG_BOX_T3);
                player.getCasketOpening().openInterface();
                break;
            case 11858:
            case 11860:
            case 11862:
            case 11848:
            case 11856:
            case 11850:
            case 11854:
            case 11852:
            case 11846:
            case 11930:
            case 11960:
            case 19588:
            case 11938:
            case 14525:
            case 11946:
            case 11942:
            case 11944:
            case 19592:
            case 11926:
            case 14529:
            case 19582:
            case 9666:
            case 19580:
            case 19659:
                if (!player.getClickDelay().elapsed(250) || !player.getInventory().contains(itemId))
                    return;
                if (player.busy()) {
                    player.getPacketSender().sendMessage("You cannot open this right now.");
                    return;
                }

                int[] items =
                        itemId == 11930 ? new int[]{8816, 8817, 8818, 8820, 8819}
                                : itemId == 11960 ? new int[]{8806, 8807, 8808}
                                : itemId == 19588 ? new int[]{14050, 14051, 14052, 1485, 14053, 14055}
                                : itemId == 11938 ? new int[]{11183, 11184, 11179, 11762, 11182, 11181}
                                : itemId == 14525 ? new int[]{15645, 15646, 15647}
                                : itemId == 11946 ? new int[]{4684, 4685, 4686, 9939, 8274, 8273}
                                : itemId == 11942 ? new int[]{17614, 17616, 17618, 17606, 17622, 11195}
                                : itemId == 11944 ? new int[]{22136, 22137, 22138, 22141, 22139, 22142}
                                : itemId == 19592 ? new int[]{22145, 22146, 22147, 22149, 22150}
                                : itemId == 11926 ? new int[]{14190, 14192, 14194, 14200, 14198, 14196, 12608}
                                : itemId == 14529 ? new int[]{22100, 22101, 22102, 22105, 22103, 22104}
                                : itemId == 19582 ? new int[]{14202, 14204, 14206, 14303, 14301}
                                : itemId == 9666 ? new int[]{22152, 22153, 22154, 22158, 22159, 22160}
                                : itemId == 11874 ? new int[]{18599, 18600, 18601, 18603, 18602, 20558}
                                : itemId == 11916 ? new int[]{23127, 23128, 23129, 23131, 23130, 23132}
                                : itemId == 11920 ? new int[]{20060, 20062, 20063, 20073, 19802, 19800, 9929}
                                : itemId == 19659 ? new int[]{22126, 19886, 17596, 22125, 22122, 17686, 22127, 12610, 22123, 15585, 15818, 23094}
                                : new int[]{itemId};

                if (player.getInventory().getFreeSlots() < items.length) {
                    player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
                    return;
                }
                player.getInventory().delete(itemId, 1);
                for (int i : items) {
                    player.getInventory().add(i, 1);
                }
                player.getPacketSender().sendMessage("You open the set and find items inside.");
                player.getClickDelay().reset();
                break;
           /* case 989:
                if (player.getLocation() != null && player.getLocation() == Location.WILDERNESS
                        || player.getLocation() != null && player.getLocation() == Location.CUSTOM_RAIDS) {
                    player.getPacketSender().sendMessage("You cannot do this at the moment.");
                    return;
                }
                Position position = new Position(2644, 4015, 0);
                TeleportHandler.teleportPlayer(player, position, TeleportType.NORMAL);
                break;*/
           /* case 5021:

                MoneyPouch.depositTickets(player, player.getInventory().getAmount(5021));


               *//* int amount1 = player.getInventory().getAmount(5021);
                if (amount1 > 2147 || amount1 + player.getInventory().getAmount(ItemDefinition.COIN_ID) > 2147000000) {
                    long amountLeft;
                    if (!player.getInventory().contains(ItemDefinition.COIN_ID))
                        amountLeft = (long) (((long) amount1 * (long) 1000000) - (long) 2147000000);
                    else
                        amountLeft = ((long) amount1 * (long) 1000000) - (long) (2147000000 - player.getInventory().getAmount(ItemDefinition.COIN_ID));
                    player.getInventory().delete(5021, amount1);
                    player.getInventory().add(ItemDefinition.COIN_ID, 2147000000 - (player.getInventory().getAmount(ItemDefinition.COIN_ID)));
                    player.setMoneyInPouch(player.getMoneyInPouch() + amountLeft);
                    player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                    player.sendMessage("<shad=1>@red@The rest of the cash(" + amountLeft / 1000000
                            + "M) has been added to your @blu@pouch@red@!");
                    return;
                }
                player.getInventory().delete(5021, amount1);
                player.getInventory().add(ItemDefinition.COIN_ID, 1000000 * amount1);*//*

                break;*/
            case 15328:
                PotionHandler.drinkPotion(player, slot, PotionHandler.INFINITY_RAGE);
                if (!drinkInfinityRage(player, slot, 15328))
                    return;
                player.getPacketSender().sendInterfaceRemoval();
                player.getCombatBuilder().incrementAttackTimer(1).cooldown(false);
                player.getCombatBuilder().setDistanceSession(null);
                player.setCastSpell(null);
                player.getFoodTimer().reset();
                player.getPotionTimer().reset();
                player.setOverloadPotionTimer(100000);
                if (player.getOverloadPotionTimer() > 0) { // Prevents decreasing stats
                    Consumables.overloadIncrease(player, Skill.ATTACK, 0.67);
                    Consumables.overloadIncrease(player, Skill.STRENGTH, 0.67);
                    Consumables.overloadIncrease(player, Skill.DEFENCE, 0.67);
                    Consumables.overloadIncrease(player, Skill.RANGED, 0.67);
                    Consumables.overloadIncrease(player, Skill.MAGIC, 0.67);
                }
                Sounds.sendSound(player, Sound.DRINK_POTION);
                break;
            case 15330:
                PotionHandler.drinkPotion(player, slot, PotionHandler.INF_OVERLOAD);
                break;
            case 23124:
                PotionHandler.drinkPotion(player, slot, PotionHandler.T1_INF_OVERLOAD);
                break;
            case 23125:
                PotionHandler.drinkPotion(player, slot, PotionHandler.T2_INF_OVERLOAD);
                break;
            case 23126:
                PotionHandler.drinkPotion(player, slot, PotionHandler.T3_INF_OVERLOAD);
                break;
            case 15331:
                PotionHandler.drinkPotion(player, slot, PotionHandler.SUPER_OVL_1);
                break;
            case 17546:
                if (player.getAggroPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.MORTAL_AGGRO);
                break;
            case 17544:
                if (player.getAggroPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.GODLY_AGGRO);
                break;
            case 17542:
                if (player.getAggroPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.DIVINE_AGGRO);
                break;
            case 1027:
                if (player.getExpPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.MORTAL_EXP);
                break;
            case 1031:
                if (player.getExpPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.GODLY_EXP);
                break;
            case 1033:
                if (player.getExpPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.DIVINE_EXP);
                break;
            case 3084:
                if (player.getDrPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.MORTAL_DR);
                break;
            case 3086:
                if (player.getDrPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.GODLY_DR);
                break;
            case 3088:
                if (player.getDrPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.DIVINE_DR);
                break;
            case 1035:
                if (player.getDdrPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.MORTAL_DDR);
                break;
            case 3080:
                if (player.getDdrPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.GODLY_DDR);
                break;
            case 3082:
                if (player.getDdrPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.DIVINE_DDR);
                break;
            case 3090:
                if (player.getDmgPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.MORTAL_DMG);
                break;
            case 3092:
                if (player.getDmgPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.GODLY_DMG);
                break;
            case 3094:
                if (player.getDmgPotionTimer() > 0) {
                    DialogueManager.sendStatement(player, "You already have the effects of this potion.");
                    return;
                }
                PotionHandler.drinkPotion(player, slot, PotionHandler.DIVINE_DMG);
                break;

            case 21218:
                player.getInventory().delete(21218, 1);
                int num1 = 60000 / Difficulty.getDifficultyModifier(player, Skill.DUNGEONEERING);
                player.getSkillManager().addExperience(Skill.DUNGEONEERING, num1);
                player.getPacketSender().sendMessage("You've Been rewarded with some Dungeoneering XP.");

                break;
            case 21219:
                player.getInventory().delete(21219, 1);
                int num = 90000 / Difficulty.getDifficultyModifier(player, Skill.SLAYER);
                player.getSkillManager().addExperience(Skill.SLAYER, num);
                player.getPacketSender().sendMessage("You've Been rewarded with some slayer XP.");

                break;
        /*    case 15290:
                player.getInventory().delete(15290, 1).add(ItemDefinition.MILL_ID, 5000);
                break;
            case 19001:
                player.getInventory().delete(19001, 1).add(19000, 250);
                break;
            case 15289:
                player.getInventory().delete(15289, 1).add(ItemDefinition.MILL_ID, 25000);
                break;
            case 15288:
                player.getInventory().delete(15288, 1).add(ItemDefinition.MILL_ID, 100000);
                break;*/
            case 14822:
                player.getPacketSender().sendMessage("You are now licenced with VIP slayer");
                player.getPacketSender().sendMessage("Here is your Slayer pass to our exclusive VIP SLAYER ZONE.");
                player.getInventory().delete(14822, 1);
                player.getInventory().add(27, 1);
                break;
            case 27:
                player.getSlayer().handleSlayerRingTP(itemId);
                break;
            case 14819:
                if (player.getSlayer().doubleSlayerXP) {
                    player.getPacketSender().sendMessage("You already have Double Slayer Points.");
                    return;
                }
                player.getInventory().delete(14819, 1);
                // player.getPointsHandler().setSlayerPoints(-300, true);
                player.getSlayer().doubleSlayerXP = true;
                PlayerPanel.refreshPanel(player);
                player.getPacketSender().sendMessage("You will now permanently receive double Slayer experience.");
                break;
            case 19775:
                player.getPointsHandler().incrementGlobalRate(5);

                player.sendMessage("Your Event rate is now: " + player.getPointsHandler().getGlobalRate() + " / 100%");
                player.getInventory().delete(19775, 1);
                player.getInventory().add(8212, 50);
                player.sendMessage("you were rewarded free shards");
                break;
            case 19768:
                player.getPointsHandler().incrementGlobalRate(10);
                player.getInventory().add(8212, 100);

                player.sendMessage("Your Event rate is now: " + player.getPointsHandler().getGlobalRate() + " / 100%");
                player.sendMessage("you were rewarded free shards");

                player.getInventory().delete(19768, 1);
                break;
           /* case 17544:
                player.getInventory().delete(17544, 1);
                player.getInventory().add(ItemDefinition.MILL_ID, 30000);
                player.sendMessage("<shad=1>@yel@You swapped your Supreme potion to 30K Tokens!");
                break;
            case 17546:
                player.getInventory().delete(17546, 1);
                player.getInventory().add(ItemDefinition.MILL_ID, 125000);
                player.sendMessage("<shad=1>@yel@You swapped your God potion to 125K Tokens!");
                break;*/
            case 455:
                player.getScratchCard().open();
                break;
            case 22121:
                if (player.getLocation() != Location.HOME_BANK) {
                    player.sendMessage("<shad=1>@red@You have to be in ::home to scratch this card!");
                    player.sendMessage("<shad=1>@red@You have to be in ::home to scratch this card!");
                    return;
                }
                player.getScratchcard().open();
                player.getInventory().delete(22121, 1);


                break;

            case 2150:
                player.getInventory().delete(2150, 1);
                player.getInventory().add(2152, 1);
                player.getPacketSender().sendMessage("You remove the Toad's legs.");
                break;
            case 15752:
                player.getInventory().delete(15752, 1);
                Position position55 = new Position(2694, 5109, 0);
                TeleportHandler.teleportPlayer(player, position55, TeleportType.NORMAL);
                player.getPacketSender().sendMessage("You will now be facing @red@Darth Vader@bla@!");
                break;

            case 15750:
                player.getInventory().delete(15750, 1);
                Position position1 = new Position(2868, 3590, 0);
                TeleportHandler.teleportPlayer(player, position1, TeleportType.NORMAL);
                player.getPacketSender().sendMessage("You will now be facing @blu@Luke Skywalker@bla@!");
                break;
            case 15751:
                player.getInventory().delete(15751, 1);
                Position position2 = new Position(2912, 3611, 0);
                TeleportHandler.teleportPlayer(player, position2, TeleportType.NORMAL);
                player.getPacketSender().sendMessage("You will now be facing @gre@Yoda@bla@!");
                break;
            case 16:
                player.getInventory().delete(16, 1);
                Position position3 = new Position(2907, 3289, 0);
                TeleportHandler.teleportPlayer(player, position3, TeleportType.NORMAL);
                player.getPacketSender().sendMessage("You Blew on the whistle and the King summons you!");
                break;

            case 6833:
                player.getGoodieBag().boxId = itemId;
                player.getGoodieBag().rewards = new int[]{455, 6199, 19116, 10946, 15290, 16045, 15785, 19331,
                        18686, 15501, 989, 962, 3318, 3907, 11137, 4151, 12790, 15332, 7956};
                player.getGoodieBag().open();
                break;
            case 3578:
                player.getGoodieBag().boxId = itemId;
                player.getGoodieBag().rewards = new int[]{23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 7995, 23058};
                player.getGoodieBag().open();
                break;

            //SPECIAL
            case 13017:
                player.getGoodieBag().boxId = itemId;
                player.getGoodieBag().rewards = new int[]{20489, 20490, 20491, 23058, 23057, 17702, 16249, 7543, 23120, 23123, 23126, 8136, 5011, 17013, 23049, 12630, 18881, 18883, 7995, 23169};
                player.getGoodieBag().open();
                break;
            //ELITE
            case 13021:
                player.getGoodieBag().boxId = itemId;
                player.getGoodieBag().rewards = new int[]{4442, 20490, 20491, 23058, 23057, 8410, 8411, 8412, 23059, 20489, 15004, 7543, 16249, 17702, 4373, 12630, 8831, 13555, 7995, 8812};
                player.getGoodieBag().open();
                break;
            //OWNER JEWELRY
            case 13019:
                player.getGoodieBag().boxId = itemId;
                player.getGoodieBag().rewards = new int[]{23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 8832, 23058, 23058, 23058, 23058, 23058, 23058, 23058, 15834, 23058};
                player.getGoodieBag().open();
                break;

            case 13035:
                player.getGoodieBag().boxId = itemId;
                player.getGoodieBag().rewards = new int[]{21607, 21608, 21609, 12535, 5012, 9942, 8274, 8273, 9939, 17698, 17700, 21018, 23057, 23058, 17644, 22105, 8100, 8101, 8102, 1486};
                player.getGoodieBag().open();
                break;
            case 3576:
                player.getGoodieBag().boxId = itemId;
                player.getGoodieBag().rewards = new int[]{13323, 13324, 13325, 8410, 8411, 8412, 17694, 17696, 18883, 8136, 5011, 17013, 11183, 11184, 11179, 19886, 23046, 23122, 23125, 23059};
                player.getGoodieBag().open();
                break;

            case 7510:
                long plc = player.getConstitution();
                long plcr = 50;

                if (plc <= 50) {
                    plcr = plc - 1;
                }
                if (plc == 1) {
                    plcr = 0;
                }

                if (plcr > 0) {
                    player.performAnimation(new Animation(829));
                    Hit h = new Hit(plcr);
                    player.dealDamage(h);
                    player.forceChat(Misc.randomElement(ROCK_CAKE));
                } else {
                    player.getPacketSender().sendMessage("You'll die if you keep eating this putrid rock!");
                }
                break;
            case 22051:
                if (!player.busy()) {
                    easter2017.openInterface(player);
                } else {
                    player.getPacketSender().sendMessage("You're too busy to do that!");
                }
                break;
            case 9003:
                player.getPacketSender().sendMessage(
                        "<img=5> \"Use\" this Tome on any NPC, then select \"View-drops\" to see it's entire drop table.");
                break;
            case 2946:
                if (!player.getClickDelay().elapsed(1000)) {
                    player.getPacketSender().sendMessage("Please wait 1 second before doing that.");
                    return;
                }
                if (player.getInventory().getFreeSlots() < 4) {
                    player.getPacketSender().sendMessage("You should have at least 4 inventory spaces free.");
                    return;
                }

                player.getInventory().delete(2946, 1);
                player.getInventory().add(7329, 1);
                player.getInventory().add(7330, 1);
                player.getInventory().add(7331, 1);
                player.getInventory().add(10326, 1);
                player.getInventory().add(10327, 1);
                player.getPacketSender().sendMessage("<shad=0>@red@Enjoy your firelighters!");
                player.getClickDelay().reset();
                break;
            case 15367:
                if (!player.getClickDelay().elapsed(1000)) {
                    player.getPacketSender().sendMessage("Please wait 1 second before doing that.");
                    return;
                }
                if (player.getInventory().getFreeSlots() < 11) {
                    player.getPacketSender().sendMessage("You should have at least 11 inventory spaces free.");
                    return;
                }

                player.getInventory().delete(15367, 1);
                player.getInventory().add(2396, 1);
                player.getInventory().add(10593, 1);
                player.getInventory().add(2862, 1);
                player.getInventory().add(19963, 1);
                player.getInventory().add(6851, 5);
                player.getInventory().add(6847, 5);
                player.getInventory().add(6850, 5);
                player.getInventory().add(6849, 5);
                player.getInventory().add(8212, 300);
                player.getInventory().add(8213, 50);
                player.getPacketSender().sendMessage("<shad=0>@red@Enjoy your Christmas pack!");
                World.sendMessage("<img=17>@blu@[Christmas Pack]<img=17>@red@ " + player.getUsername()
                        + " Has just opened a Christmas ingredient pack! ");

                player.getClickDelay().reset();
                break;
            case 3904:
                if (!player.getClickDelay().elapsed(1000)) {
                    player.getPacketSender().sendMessage("Please wait 1 second before doing that.");
                    return;
                }
                if (player.getInventory().getFreeSlots() < 6) {
                    player.getPacketSender().sendMessage("You should have at least 6 inventory spaces free.");
                    return;
                }

                player.getInventory().delete(3904, 1);
                player.getInventory().add(20054, 1);
                player.getInventory().add(20061, 1);
                player.getInventory().add(18365, 1);
                player.getInventory().add(16879, 1);
                player.getInventory().add(13641, 1);
                player.getInventory().add(6199, 1);

                player.getPacketSender().sendMessage("You have claimed your starter gear!");
                player.getClickDelay().reset();
                break;
            case 20061:
                if (!player.getClickDelay().elapsed(5000)) {
                    player.getPacketSender().sendMessage("Please wait 5 seconds before doing that.");
                    return;
                }
                if (player.getInventory().getFreeSlots() < 3) {
                    player.getPacketSender().sendMessage("You should have at least 3 inventory spaces free.");
                    return;
                }

                player.getInventory().add(23020, 1);
                player.getInventory().add(290, 1);
                player.getInventory().add(6198, 1);
                player.getInventory().delete(20061, 1);
                player.getPacketSender().sendMessage("@red@Enjoy a free voting scroll - ::vote for more.");

                player.getClickDelay().reset();
                break;

          /*  case 1561:

                // player.getPacketSender().sendMessage("hi");
                if (!player.getClickDelay().elapsed(10000)) {
                    player.getPacketSender().sendMessage("Please wait 10 seconds before doing that.");
                    return;
                }
                player.getInventory().delete(1561, 1);
                int invSpace = player.getInventory().getFreeSlots();
                int daCount = 0;
                for (int i = 0; i < BossPet.values().length; i++) {
                    // // System.out.println("daCount < invSpace "+(boolean) (daCount < invSpace));
                    // // System.out.println("getBossPet("+i+"),
                    // "+ItemDefinition.forId(BossPet.values()[i].itemId).getName()+" =
                    // "+player.getBossPet(i));
                    if (daCount < invSpace && player.getBossPet(i)) {
                        player.getInventory().add(BossPet.values()[i].itemId, 1);
                        player.getPacketSender().sendMessage("Returned your "
                                + ItemDefinition.forId(BossPet.values()[i].itemId).getName() + " to your inventory.");
                        daCount++;
                    } else if (daCount >= invSpace && player.getBossPet(i) && !player.getBank(0).isFull()) {
                        player.getBank(0).add(BossPet.values()[i].itemId, 1);
                        player.getPacketSender().sendMessage("Returned your "
                                + ItemDefinition.forId(BossPet.values()[i].itemId).getName() + " to your bank.");
                        daCount++;
                    }
                }
                player.getClickDelay().reset();
                break;*/

            case 4837:
                player.getPacketSender()
                        .sendMessage("<col=0><shad=ffffff>This tomb has an unmistakable dark energy to it.");
                player.getPacketSender().sendMessage("The Devil's Notebook - by Anton LaVey, Dark Wizard.");
                break;
            case 2424:
                player.getInventory().delete(new Item(2424, 1));
                player.forceChat("It puts the lotion on it's skin...");
                player.performAnimation(new Animation(860));
                player.getInventory().add(new Item(3057, 1));
                break;
            case 8013:
            case 8012:
            case 8011:
            case 8010:
            case 8009:
            case 8008:
            case 8007:
            case 13599:
            case 13600:
            case 13601:
            case 13602:
            case 13603:
            case 13604:
            case 13605:
            case 13606:
            case 13611:
            case 13607:
            case 13608:
            case 13609:
            case 13610:
                TeleportTabs.teleportTabs(player, itemId);
                break;
            case 2724:
                OLD_ClueScrolls.openCasket(player);
                break;
            case 13663:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
                    return;
                }
                player.setUsableObject(new Object[2]).setUsableObject(0, "reset");
                player.getPacketSender().sendString(38006, "Choose stat to reset!")
                        .sendMessage("@red@Please select a skill you wish to reset and then click on the 'Confim' button.")
                        .sendString(38090, "Which skill would you like to reset?");
                player.getPacketSender().sendInterface(38000);
                break;
            case 23020:
                if (player.busy()) {
                    player.getPacketSender().sendMessage("You can not do this right now.");
                    return;
                }
                if (!player.getInventory().contains(23020) || player.getInventory().getAmount(23020) < 1) {
                    return;
                }
                int amt = 1;
                int minutesEXP = 15 * amt;
                int minutesDR = 5 * amt;
                // int minutesDMG = 2 * amt;

                player.getInventory().delete(23020, amt);
                player.getInventory().add(ItemDefinition.COIN_ID, 500_000 * amt);
                player.getPacketSender()
                        .sendMessage("@blu@You are rewarded " + (amt * 1) + " vote "
                                + (amt > 1 ? "points, " : "point, ") + (500_000 * amt) + " Millions");
                player.getPacketSender()
                        .sendMessage("@blu@You received " + minutesEXP + " minutes of Bonus Xp, " + minutesDR + " minutes of x2 DR");
                player.getPointsHandler().incrementVotingPoints(amt * 1);
                BonusExperienceTask.addBonusXp(player, minutesEXP);
                VotingDRBoostTask.addBonusDR(player, minutesDR);
                //VotingDMGBoostTask.addBonusDMG(player, minutesDMG);
                StarterTasks.finishTask(player, StarterTaskData.REDEEM_A_VOTE_SCROLL);

                Achievements.doProgress(player, Achievements.Achievement.VOTE_10_TIMES, amt);
                Achievements.doProgress(player, Achievements.Achievement.VOTE_50_TIMES, amt);
                Achievements.doProgress(player, Achievements.Achievement.VOTE_100_TIMES, amt);

                player.getClickDelay().reset();
                break;
            case 10138:
                if (player.busy()) {
                    player.getPacketSender().sendMessage("You can not do this right now.");
                    return;
                }
                VoteRewardHandler.AFKFISH(player, false);
                break;
            case 17634:
                if (player.busy()) {
                    player.getPacketSender().sendMessage("You can not do this right now.");
                    return;
                }
                VoteRewardHandler.AFKMINE(player, false);
                break;
            case 9013:
                String[] messages = {"BOO!", "SURPRISE!", "SPOOOKED YA", "Ooooh!", "Spoooooooookyy",
                        "ooooOOoooOOOooOOOOooo", "I'm a ghost!", "I ain't afraid of no ghost",
                        "If there's something strange in your neighborhood...", "Who you gonna call?", "GHOST BUSTERS!",// KEEP
                        // A
                        // COMMA
                        // ON
                        // THE
                        // LAST
                        // ONE!
                };
                int max = messages.length;

                // // System.out.println("Count: " + count + " | Max: " + max);
                player.forceChat(messages[count]);
                player.performAnimation(new Animation(2836));
                player.performGraphic(new Graphic(293));
                count++;

                if (count == max) {
                    // // System.out.println("Resetting count to 0");
                    count = 0;
                }
                break;
            case 20692:
                player.performGraphic(new Graphic(199));
                player.getInventory().delete(20692, 1);
                break;
            case 290:
                player.getInventory().delete(290, 1);
                int[] rewards = {200, 202, 204, 206, 208, 210, 212, 214, 216, 218, 220, 2486, 3052, 1624, 1622, 1620, 1618,
                        1632, 1516, 1514, 454, 448, 450, 452, 378, 372, 7945, 384, 390, 15271, 533, 535, 537, 18830, 556,
                        558, 555, 554, 557, 559, 564, 562, 566, 9075, 563, 561, 560, 565, 888, 890, 892, 11212, 9142, 9143,
                        9144, 9341, 9244, 866, 867, 868, 2, 10589, 10564, 6809, 4131, 15126, 4153, 1704, 1149};
                int[] rewardsAmount = {50, 50, 50, 30, 20, 30, 30, 30, 30, 20, 10, 5, 4, 70, 40, 25, 10, 10, 100, 50, 100,
                        80, 25, 25, 250, 200, 125, 50, 30, 25, 50, 20, 20, 5, 500, 500, 500, 500, 500, 500, 500, 500, 200,
                        200, 200, 200, 200, 200, 1000, 750, 200, 100, 1200, 1200, 120, 50, 20, 1000, 500, 100, 100, 1, 1, 1,
                        1, 1, 1, 1, 1};
                int rewardPos = Misc.getRandom(rewards.length - 1);
                player.getInventory().add(rewards[rewardPos],
                        (int) ((rewardsAmount[rewardPos] * 0.5) + (Misc.getRandom(rewardsAmount[rewardPos]))));
                break;
            case 15387:
                player.getInventory().delete(15387, 1);
                rewards = new int[]{1377, 1149, 7158, 3000, 219, 5016, 6293, 6889, 2205, 3051, 269, 329, 3779, 6371, 2442,
                        347, 247};
                player.getInventory().add(rewards[Misc.getRandom(rewards.length - 1)], 1);
                break;
            case 407:
                player.getInventory().delete(407, 1);
                if (Misc.getRandom(3) < 3) {
                    player.getInventory().add(409, 1);
                } else if (Misc.getRandom(4) < 4) {
                    player.getInventory().add(411, 1);
                } else
                    player.getInventory().add(413, 1);
                break;
            case 405:
                player.getInventory().delete(405, 1);
                if (Misc.getRandom(1) < 1) {
                    int coins = Misc.getRandom(30000);
                    player.getInventory().add(ItemDefinition.COIN_ID, coins);
                    player.getPacketSender().sendMessage(
                            "The casket contained " + NumberFormat.getInstance(Locale.US).format(coins) + " coins!");
                } else
                    player.getPacketSender().sendMessage("The casket was empty.");
                break;
            case 2714:
                int amount = Misc.getRandom(100000);
                player.getInventory().delete(2714, 1);
                player.getInventory().add(ItemDefinition.COIN_ID, amount);
                player.getPacketSender().sendMessage(
                        "Inside the casket you find " + NumberFormat.getInstance(Locale.US).format(amount) + " coins!");
                break;
            case 15084:
                Gambling.rollDice(player);
                break;

            case 15098:
                Gambling.ScamrollDice(player);
                break;
            case 299:
                Gambling.plantSeed(player);
                break;
            case 15103:
                player.getPacketSender().sendMessage("This came from a Goblin. Kill Nex, or Zulrah for another.");
                break;
            case 15104:
                player.getPacketSender().sendMessage("This came from Nex or Zulrah. Kill Monkey Skeletons for another.");
                break;
            case 15105:
                player.getPacketSender().sendMessage("This came from a Monkey Skeleton. Kill the KBD for another.");
                break;
            case 15106:
                player.getPacketSender().sendMessage("This came from the KBD, kill Goblins for another.");
                break;
            /*
             * player.getPacketSender().
             * sendMessage("What lies in the <shad=f999f7>Antiqua Carcere<shad=-1>?");
             * player.getPacketSender().
             * sendMessage("The <shad=b40404>Nbmfgjdvt<shad=-1> may hold next piece.");
             * player.getPacketSender().sendMessage("The..."); player.getPacketSender().
             * sendMessage("<shad=ffffff>01010101 01101110 01100111");
             * player.getPacketSender().
             * sendMessage("<shad=ffffff>01110101 01101001 01110011 ");
             * player.getPacketSender().sendMessage("holds the next piece.");
             * player.getPacketSender().sendMessage("The..."); player.getPacketSender().
             * sendMessage("<shad=000000>53 61 67 69 74 74 61 72 69 69 73");
             * player.getPacketSender().sendMessage("holds the next piece."); break;
             */
            case 4155:
                if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getPacketSender().sendMessage("You do not have a Slayer task.");
                    return;
                }
                DialogueManager.start(player, SlayerDialogues.dialogue(player));
                break;
            case 18719: // potion of flight
                if (player.canFly()) {
                    player.getPacketSender().sendMessage("You already know how to fly.");
                } else {
                    player.getInventory().delete(18719, 1);
                    player.getPacketSender().sendMessage("Your mind is filled with the secrets of flight!")
                            .sendMessage("Use ::fly to toggle flight on and off.");
                    player.setCanFly(true);
                    player.setFlying(true);
                    player.newStance();
                }
                break;
            case 7587:
                if (player.canGhostWalk()) {
                    player.getPacketSender().sendMessage("You already know how to ghost walk.");
                } else {
                    player.getInventory().delete(7587, 1);
                    player.getPacketSender().sendMessage("Your mind is filled with the secrets of death!")
                            .sendMessage("Use ::ghostwalk to toggle it on and off.");
                    player.setCanGhostWalk(true);
                    player.setGhostWalking(true);
                    player.newStance();
                }
                break;
            case 952:
                Digging.dig(player);
                break;
            case 11261:
            case 1748:
            case 1750:
            case 1752:
            case 1754:
            case 228:
                int[] uimint = {1748, 1750, 1752, 1754, 228};
                for (int i = 0; i < uimint.length; i++) {
                    if (uimint[i] == itemId && !player.getGameMode().equals(GameMode.ULTIMATE_IRONMAN)) {
                        player.getPacketSender().sendMessage("Only Ultimate Ironman characters can do that.");
                        return;
                    }
                }
                if (!player.getClickDelay().elapsed(100)) {
                    return;
                }
                if (player.busy()) {
                    player.getPacketSender().sendMessage("You're too busy to un-note that.");
                    break;
                }
                if (player.getInventory().isFull()) {
                    player.getPacketSender().sendMessage("You need a free inventory space to un-note that.");
                    break;
                }
                String orig = ItemDefinition.forId(itemId).getName(), cur = ItemDefinition.forId(itemId - 1).getName();
                if (!orig.equalsIgnoreCase(cur)) {
                    player.getPacketSender().sendMessage("Error 21641: a = " + itemId + " &-1");
                    break;
                }
                player.getClickDelay().reset();
                int b = player.getInventory().getAmount(itemId);
                player.getInventory().delete(itemId, 1);
                if (player.getInventory().getAmount(itemId) == (b - 1)) {
                    player.getInventory().add((itemId - 1), 1);
                } else {
                    player.getPacketSender()
                            .sendMessage("Error 41265: b = " + b + ", c = " + player.getInventory().getAmount(itemId));
                    break;
                }
                break;
            case 10006:
                // Hunter.getInstance().laySnare(client);
                Hunter.layTrap(player, new SnareTrap(new GameObject(19175, new Position(player.getPosition().getX(),
                        player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
                break;
            case 10008:
                Hunter.layTrap(player, new BoxTrap(new GameObject(19187, new Position(player.getPosition().getX(),
                        player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
                break;
            case 5509:
            case 5510:
            case 5512:
            case 5514:
                RunecraftingPouches.fill(player, RunecraftingPouch.forId(itemId));
                break;
            case 292:
                ingredientsBook.readBook(player, 0, false);
                break;
            case 8868:
                player.getInventory().delete(8868, 1);
                player.getInventory().add(19116, 2);
                player.getInventory().add(7956, 5);
                player.getInventory().add(989, 10);
                player.sendMessage(
                        "<shad=1>@blu@You swapped your @red@key@blu@ for @red@X5@blu@ PVM Box, @red@X10@blu@ Ckeys, @red@X2@blu@ Super Mbox");
                break;
            case 14471:
                player.getInventory().delete(14471, 1);
                player.getInventory().add(19116, 2);
                player.getInventory().add(7956, 5);
                player.getInventory().add(989, 10);
                player.sendMessage(
                        "<shad=1>@blu@You swapped your @red@key@blu@ for @red@X5@blu@ PVM Box, @red@X10@blu@ Ckeys, @red@X2@blu@ Super Mbox");
                break;
            case 9662:
                player.getInventory().delete(9662, 1);
                player.getInventory().add(19116, 2);
                player.getInventory().add(7956, 5);
                player.getInventory().add(989, 10);
                player.sendMessage(
                        "<shad=1>@blu@You swapped your @red@key@blu@ for @red@X5@blu@ PVM Box, @red@X10@blu@ Ckeys, @red@X2@blu@ Super Mbox");
                break;
            case 3468:
                player.getInventory().delete(3468, 1);
                player.getInventory().add(19116, 2);
                player.getInventory().add(7956, 5);
                player.getInventory().add(989, 10);
                player.sendMessage(
                        "<shad=1>@blu@You swapped your @red@key@blu@ for @red@X5@blu@ PVM Box, @red@X10@blu@ Ckeys, @red@X2@blu@ Super Mbox");
                break;
            case 2734:
                int amountNeeded = 4;
                if (player.getInventory().contains(995))
                    amountNeeded -= 1;
                if (player.getInventory().contains(5023))
                    amountNeeded -= 1;
                if (player.getInventory().contains(11137))
                    amountNeeded -= 1;
                if (player.getInventory().getFreeSlots() < amountNeeded) {
                    player.sendMessage("You need atleast "+amountNeeded+" inventory slots to do this.");
                    return;
                }
                player.getInventory().delete(2734, 1);
                Box loot = BoxLoot.getLoot(SlayerCasket.loot);
                player.getInventory().add(995, 1000 + Misc.getRandom(19000));
                player.getInventory().add(5023, 10 + Misc.getRandom(40));
                player.getInventory().add(11137, 1 + Misc.getRandom(2));
                player.getInventory().add(loot.getId(), loot.getMin() + Misc.getRandom(loot.getMax() - loot.getMin()));
                break;
            case 22144:
                int amountNeeded5 = 12;
                if (player.getInventory().contains(2023))
                    amountNeeded5 -= 1;
                if (player.getInventory().getFreeSlots() < amountNeeded5) {
                    player.sendMessage("You need atleast "+amountNeeded5+" inventory slots to do this.");
                    return;
                }
                player.getInventory().delete(22144, 1);
                break;
            case 2736:
                int amountNeeded2 = 4;
                if (player.getInventory().contains(995))
                    amountNeeded2 -= 1;
                if (player.getInventory().contains(11137))
                    amountNeeded2 -= 1;
                if (player.getInventory().getFreeSlots() < amountNeeded2) {
                    player.sendMessage("You need atleast "+amountNeeded2+" inventory slots to do this.");
                    return;
                }
                player.getInventory().delete(2736, 1);
                Box loot2 = BoxLoot.getLoot(EliteSlayerCasket.loot);
                player.getInventory().add(995, 1000000 + Misc.getRandom(10000));
                player.getInventory().add(loot2.getId(), loot2.getMin() + Misc.getRandom(loot2.getMax() - loot2.getMin()));
                break;
            case 6199:
                player.getNewSpinner().openBox(6199);
                break;
           /* case 6199:

                player.getMysteryBoxOpener().display(6199, "Mystery Box", MBox.common, MBox.uncommon, MBox.rare);
                break;
            case 18768:
                player.getMysteryBoxOpener().display(18768, "Dragonball Box", DragonballBox.common1, DragonballBox.uncommon1, DragonballBox.rare1);
                break;
            case 10025:
                player.getMysteryBoxOpener().display(10025, "Progressive Box", ProgressiveBox.commonpro, ProgressiveBox.uncommonpro, ProgressiveBox.rarepro);
                break;
            case 7120:
                player.getMysteryBoxOpener().display(7120, "Slayer Box", SlayerBox.commonpro2, SlayerBox.uncommonpro2, SlayerBox.rarepro2);
                break;*/
            /*
             * int progress[][] = { { 671, 16337, 4411, 14415, 14395, 14405, 672, 673,
             * 19887, 681,676,18363,677,678,679,22075,19471,19470,19469,4393, 734, 666,
             * 15424, 674, 22078, 4369, 15877, 16269, 15943, 675, 702, 700, 701, 17708,
             * 17602, 19153, 19142, 19141, 5095, 19140, 19139, 19138, 2572, 15922, 16021,
             * 15933, 18350, 18358,18354,14910,14915,14911,14912,14914,14913}, { 671, 16337,
             * 4411, 14415, 14395, 14405, 672, 673, 19887,
             * 681,676,18363,677,678,679,22075,19471,19470,19469,4393, 734, 666, 15424, 674,
             * 22078, 4369, 15877, 16269, 15943, 675, 702, 700, 701, 17708, 17602, 19153,
             * 19142, 19141, 5095, 19140, 19139, 19138, 2572, 15922, 16021, 15933, 18350,
             * 18358,18354, 18352,18360, 17600, 19944, 703, 704, 705, 19946, 19945, 17712,
             * 17638,17640,15593,16140,2021,12860,12565,10835}, { 671, 16337, 4411, 14415,
             * 14395, 14405, 672, 673, 19887,
             * 681,676,18363,677,678,679,22075,19471,19470,19469,4393, 734, 666, 15424, 674,
             * 22078, 4369, 15877, 16269, 15943, 675, 702, 700, 701, 17708, 17602, 19153,
             * 19142, 19141, 5095, 19140, 19139, 19138, 2572, 15922, 16021, 15933, 18350,
             * 18358,18354, 18352,18360, 17600, 19944, 703, 704, 705, 19946, 19945, 17712,
             * 17638,17640,15593,16140,2021,12860,12565,10835} }; double nugemprofess =
             * Math.random(); /** Chances 50% chance of Common Items - cheap gear, high-end
             * consumables 40% chance of Uncommon Items - various high-end coin-bought gear
             * 10% chance of Rare Items - Highest-end coin-bought gear, some
             * voting-point/pk-point equipment
             *
             * int rewardprogress = nugemprofess >= 0.5 ? 0 : nugemprofess >= 0.20 ? 1 : 2;
             * rewardPos = Misc.getRandom(progress[rewardprogress].length - 1);
             * player.getInventory().delete(10025, 1);
             * player.getInventory().add(progress[rewardprogress][rewardPos],
             * 1).refreshItems(); break;
             */
          /*  case 6198:

                double petNumGen = Math.random();
                *//**
             * Chances 54% chance of Uncommon Items - various high-end coin-bought gear 30%
             * chance of Rare Items - Highest-end coin-bought gear, Some poor
             * voting-point/pk-point equipment 11% chance of Epic Items -Better
             * voting-point/pk-point equipment 5% chance of Legendary Items - Only top-notch
             * voting-point/pk-point equipment
             *//*
                int petRewardGrade = petNumGen >= 0.46 ? 0 : petNumGen >= 0.16 ? 1 : petNumGen >= 0.05 ? 2 : 3;
                int petRewardPos = Misc.getRandom(DRPetBox.petRewards[petRewardGrade].length - 1);
                player.getInventory().delete(6198, 1);
                player.getPacketSender().sendMessage("@blu@Enjoy your pet do ::droprate to check your drop rates.");
                player.getInventory().add(DRPetBox.petRewards[petRewardGrade][petRewardPos], 1).refreshItems();
                break;
            case 19624:


                double bossEventNumGen = Math.random();
                *//**
             * Chances 54% chance of Uncommon Items - various high-end coin-bought gear 30%
             * chance of Rare Items - Highest-end coin-bought gear, Some poor
             * voting-point/pk-point equipment 11% chance of Epic Items -Better
             * voting-point/pk-point equipment 5% chance of Legendary Items - Only top-notch
             * voting-point/pk-point equipment
             *//*
                int bossEventRewardGrade = bossEventNumGen >= 0.46 ? 0
                        : bossEventNumGen >= 0.16 ? 1 : bossEventNumGen >= 0.05 ? 2 : 3;
                int bossEventRewardPos = Misc.getRandom(BossEventBox.bossEventMbox[bossEventRewardGrade].length - 1);
                player.getInventory().delete(19624, 1);
                player.getInventory().add(ItemDefinition.COIN_ID, 20000000);
                player.getPacketSender().sendMessage("<col=3d1c3e>[Boss event box] you have been rewarded!");

                player.getInventory().add(BossEventBox.bossEventMbox[bossEventRewardGrade][bossEventRewardPos], 1).refreshItems();

                break;
            case PVMBox.ITEM_ID:
                player.getMysteryBoxOpener().display(PVMBox.ITEM_ID, "Pvm box", PVMBox.commonpvm, PVMBox.uncommonpvm, PVMBox.rarepvm);
                break;*/

            /*
             * int lootRewards[][] = { { 4716, 4720, 4718, 4722, 4708, 4712, 4714, 4710,
             * 4732, 4736, 4738, 4734, 4753, 4757, 4759, 4755, 4745, 4749, 4751, 4747, 290,
             * 6199 }, // Uncommon, 0 { 18740, 23020, 4153, 1215, 4151, 18684, 18686, 3140,
             * 15332 }, // Rare, 1 { 11852, 11854, 11856, 11846, 11848, 6199, 23020 }, //
             * Epic, 2 { 4882, 4894, 4900, 4888, 20460, 20456, 18747 } // Legendary, 3 };
             * double lootNumGen = Math.random(); /** Chances 54% chance of Uncommon Items -
             * various high-end coin-bought gear 30% chance of Rare Items - Highest-end
             * coin-bought gear, Some poor voting-point/pk-point equipment 11% chance of
             * Epic Items -Better voting-point/pk-point equipment 5% chance of Legendary
             * Items - Only top-notch voting-point/pk-point equipment
             *
             * int lootRewardGrade = lootNumGen >= 0.46 ? 0 : lootNumGen >= 0.16 ? 1 :
             * lootNumGen >= 0.05 ? 2 : 3; int lootRewardPos =
             * Misc.getRandom(lootRewards[lootRewardGrade].length - 1);
             * player.getInventory().delete(7956, 1);
             * player.getPacketSender().sendMessage("looted.");
             * player.getInventory().add(lootRewards[lootRewardGrade][lootRewardPos],
             * 1).refreshItems(); break;
             */
          /*  case 10027:

                double slayerlootnumgen = Math.random();
                *//**
             * Chances 54% chance of Uncommon Items - various high-end coin-bought gear 30%
             * chance of Rare Items - Highest-end coin-bought gear, Some poor
             * voting-point/pk-point equipment 11% chance of Epic Items -Better
             * voting-point/pk-point equipment 5% chance of Legendary Items - Only top-notch
             * voting-point/pk-point equipment
             *//*
                int slayerrewargrad = slayerlootnumgen >= 0.46 ? 0
                        : slayerlootnumgen >= 0.16 ? 1 : slayerlootnumgen >= 0.05 ? 2 : 3;
                int slayerRewardpos = Misc.getRandom(SlayerRewardBox.slayerlootReward[slayerrewargrad].length - 1);
                player.getInventory().delete(10027, 1);


                player.getInventory().add(SlayerRewardBox.slayerlootReward[slayerrewargrad][slayerRewardpos], 1).refreshItems();
                break;
            case 20083:
                int[][] hween = {{22041, 19132, 18405, 18406, 18407}, // Uncommon, 0
                        {22041, 19132, 18405, 18406, 18407}, // Rare, 1
                        {22041, 19132, 18405, 18406, 18407}, // Epic, 2
                        {22041, 19132, 18405, 18406, 18407} // Legendary, 3
                };
                double hweenGen = Math.random();
                *//**
             * Chances 54% chance of Uncommon Items - various high-end coin-bought gear 30%
             * chance of Rare Items - Highest-end coin-bought gear, Some poor
             * voting-point/pk-point equipment 11% chance of Epic Items -Better
             * voting-point/pk-point equipment 5% chance of Legendary Items - Only top-notch
             * voting-point/pk-point equipment
             *//*
                int hweenRewardg = hweenGen >= 0.46 ? 0 : hweenGen >= 0.16 ? 1 : hweenGen >= 0.05 ? 2 : 3;
                int hweenrewardgread = Misc.getRandom(hween[hweenRewardg].length - 1);
                player.getInventory().delete(20083, 1);
                World.sendMessage("<img=5>@blu@[Halloween cracker]<img=5>@red@ " + player.getUsername()
                        + " Has just opened a halloween box! ");

                // player.getPacketSender().sendMessage("looted.");
                player.getInventory().add(hween[hweenRewardg][hweenrewardgread], 1).refreshItems();
                break;
            case 4570:
                int[][] cmascrack = {{1050, 10284, 18411, 18413, 18414, 18412, 18410}, // Uncommon, 0
                        {1050, 10284, 18411, 18413, 18414, 18412, 18410}, // Rare, 1
                        {1050, 10284, 18411, 18413, 18414, 18412, 18410}, // Epic, 2
                        {1050, 10284, 18411, 18413, 18414, 18412, 18410} // Legendary, 3
                };
                double camaksGen2 = Math.random();
                *//**
             * Chances 54% chance of Uncommon Items - various high-end coin-bought gear 30%
             * chance of Rare Items - Highest-end coin-bought gear, Some poor
             * voting-point/pk-point equipment 11% chance of Epic Items -Better
             * voting-point/pk-point equipment 5% chance of Legendary Items - Only top-notch
             * voting-point/pk-point equipment
             *//*
                int cmasrward = camaksGen2 >= 0.46 ? 0 : camaksGen2 >= 0.16 ? 1 : camaksGen2 >= 0.05 ? 2 : 3;
                int refeigenerator = Misc.getRandom(cmascrack[cmasrward].length - 1);
                player.getInventory().delete(4570, 1);
                World.sendMessage("<img=21>@blu@[Chrismas cracker]<img=5>@red@ " + player.getUsername()
                        + " Has just opened a halloween box! ");

                // player.getPacketSender().sendMessage("looted.");
                player.getInventory().add(cmascrack[cmasrward][refeigenerator], 1).refreshItems();
                break;
            case 6183:
                int[][] hween1 = {{8857, 8858, 8859, 16835}, // Uncommon, 0
                        {8857, 8858, 8859, 16835}, // Rare, 1
                        {8857, 8858, 8859, 16835}, // Epic, 2
                        {8857, 8858, 8859, 16835} // Legendary, 3
                };
                double hweenGen1 = Math.random();
                *//**
             * Chances 54% chance of Uncommon Items - various high-end coin-bought gear 30%
             * chance of Rare Items - Highest-end coin-bought gear, Some poor
             * voting-point/pk-point equipment 11% chance of Epic Items -Better
             * voting-point/pk-point equipment 5% chance of Legendary Items - Only top-notch
             * voting-point/pk-point equipment
             *//*
                int hweenRewardg1 = hweenGen1 >= 0.46 ? 0 : hweenGen1 >= 0.16 ? 1 : hweenGen1 >= 0.05 ? 2 : 3;
                int hweenrewardgread1 = Misc.getRandom(hween1[hweenRewardg1].length - 1);
                player.getInventory().delete(6183, 1);
                World.sendMessage("<img=5>@blu@[Halloween Box]<img=5>@red@ " + player.getUsername()
                        + " Has just opened a halloween box! ");

                // player.getPacketSender().sendMessage("looted.");
                player.getInventory().add(hween1[hweenRewardg1][hweenrewardgread1], 1).refreshItems();
                break;
            case 6855:
                int[][] xmaswni = {{13025, 13027, 13023, 22043, 13029, 13031}, // Uncommon, 0
                        {13025, 13027, 13023, 22043, 13029, 13031}, // Rare, 1
                        {13025, 13027, 13023, 22043, 13029, 13031}, // Epic, 2
                        {13025, 13027, 13023, 22043, 13029, 13031} // Legendary, 3
                };
                double xmasgen = Math.random();
                *//**
             * Chances 54% chance of Uncommon Items - various high-end coin-bought gear 30%
             * chance of Rare Items - Highest-end coin-bought gear, Some poor
             * voting-point/pk-point equipment 11% chance of Epic Items -Better
             * voting-point/pk-point equipment 5% chance of Legendary Items - Only top-notch
             * voting-point/pk-point equipment
             *//*
                int xmasreward = xmasgen >= 0.46 ? 0 : xmasgen >= 0.16 ? 1 : xmasgen >= 0.05 ? 2 : 3;
                int xmadretin = Misc.getRandom(xmaswni[xmasreward].length - 1);
                player.getInventory().delete(6855, 1);
                World.sendMessage("<img=17>@blu@[Christmas Mystery Box]<img=17>@red@ " + player.getUsername()
                        + " Has just opened a Christmas box! ");

                // player.getPacketSender().sendMessage("looted.");
                player.getInventory().add(xmaswni[xmasreward][xmadretin], 1).refreshItems();
                break;*/
            /*case 15501:
                int[] commonsuper = new int[]{11946, 11848, 11850, 11852, 11854, 11856, 18686, 18799, 5095, 10025};
                int[] uncommonsuper = new int[]{19116, 21218, 17714, 17686, 15924, 16023, 15935, 15888, 15818, 12994,};
                int[] raresuper = new int[]{8801, 8802, 8803, 8804, 8805, 8806, 8807, 8808, 3907, 10946, 6769, 20489};
                player.getMysteryBoxOpener().display(15501, "Vote Mysterybox", commonsuper, uncommonsuper, raresuper);
                break;
            case 11946:
                player.getInventory().delete(11946, 1);
                player.getInventory().add(6137, 1);
                player.getInventory().add(6139, 1);
                player.getInventory().add(6141, 1);
                player.getInventory().add(6147, 1);
                player.getInventory().add(6153, 1);
                player.sendMessage("You have opened a skeletal set!");
                break;*/
            /*
             * int superiorRewards[][] = { { 11133, 15126, 10828, 3751, 3753, 10589, 10564,
             * 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528, 7158, 4153, 6, 8, 10, 12,
             * 4675, 6914, 6889 }, // Uncommon, 0 { 6739, 15259, 15332, 2579, 6920, 6922,
             * 15241, 11882, 11884, 11906, 20084 }, // Rare, 1 { 6570, 15018, 15019, 15020,
             * 15220, 11730, 18349, 18353, 13896, 18357, 13899, 10551, 3322, 4151, 2577 },
             * // Epic, 2 { 11235, 17273, 14484, 18685, 11696, 11698, 11700, 13262, 15486,
             * 19336, 18358, 19337, 19338, 19339, 19340, 14009, 14010, 14008, 22034, 18799 }
             * // Legendary, 3 }; double superiorNumGen = Math.random(); /** Chances 54%
             * chance of Uncommon Items - various high-end coin-bought gear 30% chance of
             * Rare Items - Highest-end coin-bought gear, Some poor voting-point/pk-point
             * equipment 11% chance of Epic Items -Better voting-point/pk-point equipment 5%
             * chance of Legendary Items - Only top-notch voting-point/pk-point equipment
             *
             * int superiorRewardGrade = superiorNumGen >= 0.46 ? 0 : superiorNumGen >= 0.16
             * ? 1 : superiorNumGen >= 0.05 ? 2 : 3; int superiorRewardPos =
             * Misc.getRandom(superiorRewards[superiorRewardGrade].length - 1);
             * player.getInventory().delete(15501, 1);
             * player.getInventory().add(superiorRewards[superiorRewardGrade][
             * superiorRewardPos], 1).refreshItems(); break;
             */
            case 19116:
                int[] commonsuper1 = new int[]{989, 4888, 18332, 14377, 13902, 13899, 11730};
                int[] uncommonsuper1 = new int[]{13922, 13952, 13940, 13910, 13946, 13934, 13916, 13949, 13937, 14915,
                        14919, 14924};
                int[] raresuper1 = new int[]{16140, 2021, 12860, 12565, 12634, 22077, 19136, 6936, 14008, 14009, 14010,
                        455};
                player.getMysteryBoxOpener().display(19116, "Super Box", commonsuper1, uncommonsuper1, raresuper1);
                break;


            case 19115:
                int[] commonextreme = new int[]{12601, 12603, 12605, 15442, 15443, 15444, 20000, 20001, 20002, 18351,
                        18349, 18353, 6500, 13744, 13738, 13742, 13740, 6570, 19111, 13752, 13746, 13750, 19135, 14484};
                int[] uncommonsuper11 = new int[]{22077, 19136, 6936, 1038, 1040, 1042, 1044, 1046, 1048, 666, 15424,
                        674};
                int[] raresuper11extreme = new int[]{8800, 8803, 8806, 8801, 8804, 8807, 8802, 8805, 8808, 20549, 20173,
                        8809, 10946, 10946, 10946};
                player.getMysteryBoxOpener().display(19115, "Extreme Box", commonextreme, uncommonsuper11,
                        raresuper11extreme);
                break;
            /*
             * int extremeRewards[][] = { { 150195020, 15220, 12601, 12603, 12605, 2572,
             * 15441, 15442, 15443, 15444, 20000, 20001, 20002, 15018, 12931, 18351, 18349,
             * 18353, 18335, 18357, 6500, 18719 }, // Uncommon, 0 { 6739, 13744, 13738,
             * 13742, 13740, 6570, 19111, 11702, 13752, 13746, 13750, 19135, 14484 }, //
             * Rare, // 1 { 11235, 17273, 14484, 11696, 11698, 11700, 13262, 15486, 19336,
             * 19337, 19338, 19339, 19340, 14009, 14010, 14008, 6769 }, // Epic, 2 { 1055,
             * 1053, 1507, 1050, 10284, 962, 1042, 1046, 1044, 1040, 1038, 11858, 11860,
             * 11862, 19580, 10935, 6769 } // Legendary, 3 }; double extremeNumGen =
             * Math.random(); /** Chances 54% chance of Uncommon Items - various high-end
             * coin-bought gear 30% chance of Rare Items - Highest-end coin-bought gear,
             * Some poor voting-point/pk-point equipment 11% chance of Epic Items -Better
             * voting-point/pk-point equipment 5% chance of Legendary Items - Only top-notch
             * voting-point/pk-point equipment
             *
             * int extremeRewardGrade = extremeNumGen >= 0.46 ? 0 : extremeNumGen >= 0.16 ?
             * 1 : extremeNumGen >= 0.05 ? 2 : 3; int extremeRewardPos =
             * Misc.getRandom(extremeRewards[extremeRewardGrade].length - 1);
             * player.getInventory().delete(19115, 1);
             * player.getInventory().add(extremeRewards[extremeRewardGrade][extremeRewardPos
             * ], 1).refreshItems(); int extremeRewardGrade1 = extremeNumGen >= 0.46 ? 0 :
             * extremeNumGen >= 0.16 ? 1 : extremeNumGen >= 0.05 ? 2 : 3; int
             * extremeRewardPos1 = Misc.getRandom(extremeRewards[extremeRewardGrade1].length
             * - 1); player.getInventory().add(extremeRewards[extremeRewardGrade1][
             * extremeRewardPos1], 1).refreshItems(); break;
             */
          /*  case 19114:// grandmbox
                int[] commongrand = new int[]{13736, 13744, 13742, 13740, 6293, 18754, 11694, 11696, 11698, 11700, 1038,
                        1040, 1042, 1044, 1046, 1048};
                int[] uncommongrand = new int[]{20549, 20173, 8809, 8834, 8835, 8860, 8861, 8862, 15830, 3318, 15418};
                int[] raresgrand = new int[]{8326, 8330, 8323, 8327, 8331, 8324, 8328, 8332, 8325, 22084, 22083, 22092,
                        10946, 10942, 6769};
                player.getMysteryBoxOpener().display(19114, "Grand Box", commongrand, uncommongrand, raresgrand);
                break;
            case 20488:// grandmbox
                int[] commonOP = new int[]{8800, 8803, 8806, 8801, 8804, 8807, 8802, 8805, 8808, 20549, 20173, 8809,
                        10946, 10946, 10946};
                int[] uncommonOP = new int[]{8326, 8330, 8323, 8327, 8331, 8324, 8328, 8332, 8325, 22084, 22083, 22092,
                        10946, 10942};
                int[] raresOP = new int[]{18753, 18749, 18631, 18752, 18748, 18637, 18751, 18631, 18623, 18750, 18636,
                        18629, 19886, 4446, 6769, 10942};
                player.getMysteryBoxOpener().display(20488, "OP Chest", commonOP, uncommonOP, raresOP);
                break;
            case 20489:// grandmbox
                int[] commonOP2 = new int[]{8326, 8330, 8323, 8327, 8331, 8324, 8328, 8332, 8325, 22084, 22083, 22092,
                        10946, 10942, 15288};
                int[] uncommonOP2 = new int[]{18753, 18749, 18631, 18752, 18748, 18637, 18751, 18638, 18623, 18750, 18636,
                        18629, 4446, 6769, 10942};
                int[] raresOP2 = new int[]{8253, 19886, 8087, 8088, 8089, 10947, 12608, 10934, 3578};

                player.getMysteryBoxOpener().display(20489, "@mag@Launch Casket", commonOP2, uncommonOP2, raresOP2);
                break;
            case 18404:
                int[] commonraidd = new int[]{8800, 8801, 8802, 8803, 8804, 8805, 8806, 8807, 8808, 22077, 19136, 6936, 15289, 15290};
                int[] uncommonraidd = new int[]{8326, 8327, 8328, 8330, 8331, 8332, 8323, 8324, 8325, 10025, 19116, 19115, 19114, 20488, 15288};
                int[] rareraidd = new int[]{5012, 12535, 17011, 4446, 19886, 1486, 17700, 20488, 20489, 10025, 455, 10946, 18419, 18418, 18416, 15288, 15289, 6500, 18719, 7587, 12608};
                player.getMysteryBoxOpener().display(18404, "@whi@Raids [2] Box", commonraidd, uncommonraidd, rareraidd);// raid
                break;*/
            /*
             * int grandRewards[][] = { { 6739, 13744, 13738, 13742, 13740, 6570, 19111,
             * 11702, 13752, 13746, 13750, 19136, 14484 }, // common // 1 { 11235, 17273,
             * 14484, 11696, 11698, 11700, 13262, 15486, 19336, 19337, 19338, 19339, 19340,
             * 14009, 14010, 14008, 6769 }, // Epic, 2 { 1055, 1053, 1507, 1050, 10284, 962,
             * 1042, 1046, 1044, 1040, 1038, 11858, 11860, 11862, 19580, 10935, 6769 }, //
             * Legendary, 3 { 19886, 5023, 5497, 19812, 19843, 14769, 6199, 6199, 4178,
             * 20534, 19116, 19115, 19114, 19119, 6769, 22078, 4409, 19753, 18349, 15501 }
             * // rare, 4 }; double grandNumGen = Math.random(); /** Chances 54% chance of
             * Uncommon Items - various high-end coin-bought gear 30% chance of Rare Items -
             * Highest-end coin-bought gear, Some poor voting-point/pk-point equipment 11%
             * chance of Epic Items -Better voting-point/pk-point equipment 5% chance of
             * Legendary Items - Only top-notch voting-point/pk-point equipment
             *
             * int grandRewardGrade = grandNumGen >= 0.46 ? 0 : grandNumGen >= 0.16 ? 1 :
             * grandNumGen >= 0.05 ? 2 : 3; int grandRewardPos =
             * Misc.getRandom(grandRewards[grandRewardGrade].length - 1);
             * player.getInventory().delete(19114, 1);
             * player.getInventory().add(grandRewards[grandRewardGrade][grandRewardPos],
             * 1).refreshItems(); int grandRewardGrade1 = grandNumGen >= 0.46 ? 0 :
             * grandNumGen >= 0.16 ? 1 : grandNumGen >= 0.05 ? 2 : 3; int grandRewardPos1 =
             * Misc.getRandom(grandRewards[grandRewardGrade1].length - 1);
             * player.getInventory().add(grandRewards[grandRewardGrade1][grandRewardPos1],
             * 1).refreshItems(); break;
             */

            case 15682:
                if (!player.getInventory().contains(15682)) {
                    return;
                }
                if (!player.getClickDelay().elapsed(1000)) {
                    return;
                }
                player.getClickDelay().reset();
                player.getInventory().delete(15682, 1);
                player.getInventory().add(23020, 3);
                break;
            case 11884:
                player.getInventory().delete(11884, 1);
                player.getInventory().add(2595, 1).refreshItems();
                player.getInventory().add(2591, 1).refreshItems();
                player.getInventory().add(3473, 1).refreshItems();
                player.getInventory().add(2597, 1).refreshItems();
                break;
            case 11882:
                player.getInventory().delete(11882, 1);
                player.getInventory().add(2595, 1).refreshItems();
                player.getInventory().add(2591, 1).refreshItems();
                player.getInventory().add(2593, 1).refreshItems();
                player.getInventory().add(2597, 1).refreshItems();
                break;
            case 11906:
                player.getInventory().delete(11906, 1);
                player.getInventory().add(7394, 1).refreshItems();
                player.getInventory().add(7390, 1).refreshItems();
                player.getInventory().add(7386, 1).refreshItems();
                break;
            case 15262:
                if (!player.getClickDelay().elapsed(1000))
                    return;
                player.getInventory().delete(15262, 1);
                player.getInventory().add(18016, 10000).refreshItems();
                player.getClickDelay().reset();
                break;
            case 6:
                // DwarfMultiCannon.setupCannon(player);
                player.getPacketSender().sendMessage("Cannon is disabled.");
                break;
            case 2722:
            case 2723:
            case 2725:
            case 2727:
            case 2729:
            case 2731:
            case 2733:
            case 2735:
            case 2737:
            case 2739:
            case 2741:
            case 2743:
            case 2745:
            case 2747:
                //case 2773:
            case 2774:
            case 2776:
            case 2778:
            case 2780:
            case 2782:
            case 2783:
            case 2785:
            case 2786:
            case 2788:
            case 2790:
            case 2792:
            case 2793:
            case 2794:
            case 2796:
            case 2797:
            case 2799:
            case 3520:
            case 3522:
            case 3524:
            case 3525:
            case 3526:
            case 3528:
            case 3530:
            case 3532:
            case 3534:
            case 3536:
            case 3538:
            case 3540:
            case 3542:
            case 3544:
            case 3546:
            case 3548:
            case 3550:
                player.getInventory().delete(itemId, 1);
                OLD_ClueScrolls.awardCasket(player);
                /*for (int i = 0; i < CLUESCROLL.values().length; i++) {
                    if (CLUESCROLL.values()[i].getClueId() == itemId) {
                        int steps = player.getPointsHandler().getClueSteps();
                        String s = "steps";
                        if (steps == 1) {
                            s = "step";
                        }
                        player.getPacketSender().sendMessage(CLUESCROLL.values()[i].getHint() + " You've done "
                                + player.getPointsHandler().getClueSteps() + " " + s + ".");
                        break;
                    }
                }*/
        }
    }

    public static void thirdAction(Player player, Packet packet) {
        int interfaceId = packet.readLEShortA();
        int slot = packet.readLEShort();
        int itemId = packet.readShortA();
        if (slot < 0 || slot > player.getInventory().capacity())
            return;
        if (player.getInventory().getItems()[slot].getId() != itemId)
            return;

        player.setInteractingItem(player.getInventory().getItems()[slot]);


        if (SummoningData.isPouch(player, itemId, 2))
            return;


        if (PetUpgrading.upgradeable(player, itemId)) {
            return;
        }

        switch (itemId) {

            case 3253:
                //	player.getInventory().delete(itemId, 1);
                player.getMinimeSystem().despawn();
                break;

            case 13591:
                player.getPacketSender().sendMessage("You rub the enchanted key to teleport to chest area.");
                Position position = new Position(2706, 2737, 0);
                TeleportHandler.teleportPlayer(player, position, TeleportType.NORMAL);
                break;
            case 6500:
                if (player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked()) {
                    player.getPacketSender().sendMessage("You cannot configure this right now.");
                    return;
                }
                player.getPacketSender().sendInterfaceRemoval();
                DialogueManager.start(player, 101);
                player.setDialogueActionId(60);
                break;
            case 1712: // glory start
            case 1710:
            case 1708:
            case 1706: // glory end
            case 11118: // cb brace start
            case 11120:
            case 11122:
            case 11124: // cb brace end
            case 2552: // duel start
            case 2554:
            case 2556:
            case 2558:
            case 2560:
            case 2562:
            case 2564:
            case 2566: // duel end
            case 3853: // games start
            case 3855:
            case 3857:
            case 3859:
            case 3861:
            case 3863:
            case 3865:
            case 3867: // games end
            case 11194: // digsite start
            case 11193:
            case 11192:
            case 11191:
            case 11190: // digsite start
                Position position1 = new Position(2074, 4456, 0);
                TeleportHandler.teleportPlayer(player, position1, TeleportType.NORMAL);
                break;
            case 10362:
                JewelryTeleporting.rub(player, itemId);
                break;
            case 10732:
            case 4566:
                player.performAnimation(new Animation(1835));
                break;
//
            //
            case 13263:
                if (player.getInventory().contains(13263) && player.getInventory().getAmount(5023) >= 1000) {
                    player.getInventory().delete(13263, 1).delete(5023, 1000).add(21075, 1);
                    player.getPacketSender().sendMessage("You have upgraded your slayer helmet");
                } else {
                    player.getPacketSender().sendMessage("You need at least 1K Slayer tickets to upgrade your helmet.");
                }
                break;
            case 21075:
                if (player.getInventory().contains(21075) && player.getInventory().getAmount(5023) >= 3000) {
                    player.getInventory().delete(21075, 1).delete(5023, 3000).add(21076, 1);
                    player.getPacketSender().sendMessage("You have upgraded your slayer helmet");
                } else {
                    player.getPacketSender().sendMessage("You need at least 3K Slayer tickets to upgrade your helmet.");
                }
                break;
            case 21076:
                if (player.getInventory().contains(21076) && player.getInventory().getAmount(5023) >= 6000) {
                    player.getInventory().delete(21076, 1).delete(5023, 6000).add(21077, 1);
                    player.getPacketSender().sendMessage("You have upgraded your slayer helmet");
                } else {
                    player.getPacketSender().sendMessage("You need at least 6K Slayer tickets to upgrade your helmet.");
                }
                break;
            case 21077:
                if (player.getInventory().contains(21077) && player.getInventory().getAmount(5023) >= 10000) {
                    player.getInventory().delete(21077, 1).delete(5023, 10000).add(21078, 1);
                    player.getPacketSender().sendMessage("You have upgraded your slayer helmet");
                } else {
                    player.getPacketSender().sendMessage("You need at least 10K Slayer tickets to upgrade your helmet.");
                }
                break;
            case 21078:
                if (player.getInventory().contains(21077) && player.getInventory().getAmount(5023) >= 20000) {
                    player.getInventory().delete(21077, 1).delete(5023, 20000).add(21079, 1);
                    player.getPacketSender().sendMessage("You have upgraded your slayer helmet");
                } else {
                    player.getPacketSender().sendMessage("You need at least 20K Slayer tickets to upgrade your helmet.");
                }
                break;


            case 11113:
                player.getPacketSender().sendMessage("All skill teleports are available in the skills tab.");
                break;

            case 1704:
                player.getPacketSender().sendMessage("Your amulet has run out of charges.");
                break;
            case 11126:
                player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
                break;
            case 13281:
            case 13282:
            case 13283:
            case 13284:
            case 13285:
            case 13286:
            case 13287:
            case 13288:

                player.getSlayer().handleSlayerRingTP(itemId);
                break;
            case 18819:
                player.getSlayer().handleSlayerRingTP2(itemId);
                break;
            case 5509:
            case 5510:
            case 5512:
            case 5514:
                RunecraftingPouches.check(player, RunecraftingPouch.forId(itemId));
                break;
            case 2550:
                if (!player.getInventory().contains(2550)) {
                    player.getPacketSender().sendMessage("You must have a ring of recoil in your inventory to do this.");
                    return;
                }
                if (ItemDegrading.maxRecoilCharges - player.getRecoilCharges() == ItemDegrading.maxRecoilCharges) {
                    player.getPacketSender().sendMessage("You already have the maximum ring of recoil charges.");
                    return;
                }
                player.getInventory().delete(2550, 1);
                player.setRecoilCharges(0);
                player.getPacketSender().sendMessage("Your ring of recoil turns to dust, and your charges are reset.");
                break;

            case 12926:
                int charges = player.getBlowpipeCharges();
                if (!player.getInventory().contains(12926)) {
                    return;
                }
                if (charges <= 0) {
                    player.getPacketSender().sendMessage("You have no charges!");
                    return;
                }
                if (player.getInventory().contains(12934) || !player.getInventory().isFull()) {
                    player.getInventory().add(12934, charges);
                    player.setBlowpipeCharges(0);
                    player.getPacketSender().sendMessage(
                            "You uncharge your blowpipe and recieve " + Misc.format(charges) + " Zulrah scales");
                } else {
                    player.getPacketSender().sendMessage("You need an inventory space.");
                }
                break;
            case ItemDefinition.COIN_ID:
                ConvertCoins.convertMillCoins(player);
                break;
            case 19131:
                if (player.getInventory().contains(19131) && player.getInventory().getAmount(12657) >= 1000) {
                    player.getInventory().delete(19131, 1).delete(12657, 1000).add(19130, 1);
                    player.getPacketSender().sendMessage("You have upgraded your boots!");
                } else {
                    player.getPacketSender().sendMessage("You need at least 1000 Pebbles to upgrade your boots.");
                }
                break;
            case 19130:
                if (player.getInventory().contains(19130) && player.getInventory().getAmount(12657) >= 2000) {
                    player.getInventory().delete(19130, 1).delete(12657, 2000).add(19129, 1);
                    player.getPacketSender().sendMessage("You have upgraded your boots!");
                } else {
                    player.getPacketSender().sendMessage("You need at least 2000 Pebbles to upgrade your boots.");
                }
                break;
            case 19129:
                if (player.getInventory().contains(19129) && player.getInventory().getAmount(12657) >= 3000) {
                    player.getInventory().delete(19129, 1).delete(12657, 3000).add(19128, 1);
                    player.getPacketSender().sendMessage("You have upgraded your Steel boots!");
                } else {
                    player.getPacketSender().sendMessage("You need at least 3000 Pebbles to upgrade your boots.");
                }
                break;
            case 19128:
                if (player.getInventory().contains(19128) && player.getInventory().getAmount(12657) >= 4000) {
                    player.getInventory().delete(19128, 1).delete(12657, 4000).add(19127, 1);
                    player.getPacketSender().sendMessage("You have upgraded your boots!");
                } else {
                    player.getPacketSender().sendMessage("You need at least 4000 Pebbles to upgrade your boots.");
                }
                break;
            case 19127:
                if (player.getInventory().contains(19127) && player.getInventory().getAmount(12657) >= 5000) {
                    player.getInventory().delete(19127, 1).delete(12657, 5000).add(19126, 1);
                    player.getPacketSender().sendMessage("You have upgraded your boots!");
                } else {
                    player.getPacketSender().sendMessage("You need at least 5000 Pebbles to upgrade your boots.");
                }
                break;
            case 19126:
                if (player.getInventory().contains(19126) && player.getInventory().getAmount(12657) >= 6000) {
                    player.getInventory().delete(19126, 1).delete(12657, 6000).add(19125, 1);
                    player.getPacketSender().sendMessage("You have upgraded your boots!");
                } else {
                    player.getPacketSender().sendMessage("You need at least 6000 Pebbles to upgrade your boots.");
                }
                break;
            case 19125:
                if (player.getInventory().contains(19125) && player.getInventory().getAmount(12657) >= 7000) {
                    player.getInventory().delete(19125, 1).delete(12657, 7000).add(19124, 1);
                    player.getPacketSender().sendMessage("You have upgraded your boots!");
                } else {
                    player.getPacketSender().sendMessage("You need at least 7000 Pebbles to upgrade your boots.");
                }
                break;
            case 19124:
                if (player.getInventory().contains(19124) && player.getInventory().getAmount(12657) >= 8000) {
                    player.getInventory().delete(19124, 1).delete(12657, 8000).add(19123, 1);
                    player.getPacketSender().sendMessage("You have upgraded your boots!");
                } else {
                    player.getPacketSender().sendMessage("You need at least 8000 Pebbles to upgrade your boots.");
                }
                break;

            /*case 1438:
            case 1448:
            case 1440:
            case 1442:
            case 1444:
            case 1446:
            case 1454:
            case 1452:
            case 1462:
            case 1458:
            case 1456:
            case 1450:
                Runecrafting.handleTalisman(player, itemId);
                break;*/
        }
    }

    public void secondAction(Player player, Packet packet) {
        int itemId = packet.readShortA();
        int slot = packet.readLEShortA();
        int interfaceId = packet.readLEShortA();
       // System.out.println(itemId);
        if (slot < 0 || slot > player.getInventory().capacity())
            return;
        if (player.getInventory().getItems()[slot].getId() != itemId)
            return;
        if (JarData.forJar(itemId) != null) {
            PuroPuro.lootJar(player, new Item(itemId, 1), JarData.forJar(itemId));
            return;
        }
        if (SummoningData.isPouch(player, itemId, 3)) {
            return;
        }
        if (ItemBinding.isBindable(itemId)) {
            ItemBinding.bindItem(player, itemId);
            return;
        }


        if (MemberScrolls.handleScroll(player, itemId, true))
            return;

        switch (itemId) {

            case 19984:
            case 19985:
            case 19986:
            case 20400:
            case 19989:
            case 19988:
            case 19992:
            case 19991:
            case 14484:
            case 22174:
            case 22175:
                int amountcoins = 5;
                int dissolvereward = 10835;
                Item toDissolveItem = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem.getId(), toDissolveItem.getEffect(), toDissolveItem.getBonus())) {
                    player.getInventory().delete(toDissolveItem)
                            .add(dissolvereward, amountcoins);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 142);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins + " @or2@tokens.");
                }
                break;

            case 20086:
            case 20087:
            case 20088:
            case 20089:
            case 20091:
            case 20093:
            case 20092:
            case 18750:
            case 18636:
            case 18629:
                int amountcoins2 = 20;
                int dissolvereward2 = 10835;
                Item toDissolveItem1 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem1.getId(), toDissolveItem1.getEffect(), toDissolveItem1.getBonus())) {
                    player.getInventory().delete(toDissolveItem1)
                            .add(dissolvereward2, amountcoins2);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 298);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins2 + " @or2@tokens.");
                }
                break;

            case 18011:
            case 17999:
            case 18001:
            case 18003:
            case 18005:
            case 18009:
                int amountcoins3 = 30;
                int dissolvereward3 = 10835;
                Item toDissolveItem2 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem2.getId(), toDissolveItem2.getEffect(), toDissolveItem2.getBonus())) {
                    player.getInventory().delete(toDissolveItem2)
                            .add(dissolvereward3, amountcoins3);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 352);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins3 + " @or2@tokens.");
                }
                break;

            case 23026:
            case 23027:
            case 23021:
            case 23022:
            case 23023:
            case 23025:
            case 23024:
            case 23033:
            case 23028:
            case 23029:
            case 23030:
            case 23032:
            case 23031:
            case 23039:
            case 23034:
            case 23035:
            case 23036:
            case 23038:
            case 23037:
                int amountcoins4 = 50;
                int dissolvereward4 = 10835;
                Item toDissolveItem3 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem3.getId(), toDissolveItem3.getEffect(), toDissolveItem3.getBonus())) {
                    player.getInventory().delete(toDissolveItem3)
                            .add(dissolvereward4, amountcoins4);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 472);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins4 + " @or2@tokens.");
                }
                break;

            case 23055:
            case 23056:
            case 23050:
            case 23051:
            case 23052:
            case 23054:
            case 19135:
            case 20592:
            case 20593:
            case 20594:
            case 4367:
            case 8334:
            case 11140:
            case 8335:
            case 19892:
            case 20542:
            case 13306:
            case 13300:
            case 13301:
            case 13304:
            case 18683:
            case 13305:
            case 13302:
            case 21055:
            case 21062:
            case 21063:
            case 21064:
            case 21071:
            case 21067:
            case 21066:
            case 21069:
            case 21068:
            case 21048:
            case 21049:
            case 21036:
            case 21037:
            case 21038:
            case 21039:
            case 21041:
            case 21040:
            case 17664:
            case 23134:
            case 23135:
            case 23136:
            case 23138:
            case 23137:
            case 14915:
            case 14910:
            case 14911:
            case 14912:
            case 14914:
            case 14913:
            case 14377:
            case 14733:
            case 14732:
            case 14734:
            case 10865:
            case 12864:
            case 8816:
            case 8817:
            case 8818:
            case 8820:
            case 8819:
            case 23146:
            case 23145:
            case 23144:

                int amountcoins5 = 75;
                int dissolvereward5 = 10835;
                Item toDissolveItem4 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem4.getId(), toDissolveItem4.getEffect(), toDissolveItem4.getBonus())) {
                    player.getInventory().delete(toDissolveItem4)
                            .add(dissolvereward5, amountcoins5);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 3022);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins5 + " @or2@tokens.");
                }
                break;

            case 15922:
            case 16021:
            case 15933:
            case 12614:
            case 17710:
            case 5420:
            case 5422:
            case 5428:
            case 17684:
            case 9940:
            case 21042:
            case 21043:
            case 21044:
            case 21045:
            case 21047:
            case 21046:
            case 8803:
            case 8804:
            case 8805:
            case 8809:
            case 8806:
            case 8807:
            case 8808:
            case 21018:
            case 14050:
            case 14051:
            case 14052:
            case 1485:
            case 14053:
            case 14055:
            case 8088:
            case 11001:
            case 11002:
            case 11003:
            case 7014:
            case 11183:
            case 11184:
            case 11179:
            case 11762:
            case 11182:
            case 11181:
            case 10887:
            case 23092:
            case 23093:
            case 23094:
            case 21028:
            case 21029:
            case 21030:
            case 17391:

                int amountcoins6 = 100;
                int dissolvereward6 = 10835;
                Item toDissolveItem5 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem5.getId(), toDissolveItem5.getEffect(), toDissolveItem5.getBonus())) {
                    player.getInventory().delete(toDissolveItem5)
                            .add(dissolvereward6, amountcoins6);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 5211);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins6 + " @or2@tokens.");
                }
                break;

            case 15888:
            case 15818:
            case 15924:
            case 16023:
            case 15935:
            case 17686:
            case 16272:
            case 12994:
            case 22127:
            case 22126:
            case 17596:
            case 22125:
            case 22122:
            case 22123:
            case 12610:
                int amountcoins7 = 150;
                int dissolvereward7 = 10835;
                Item toDissolveItem6 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem6.getId(), toDissolveItem6.getEffect(), toDissolveItem6.getBonus())) {
                    player.getInventory().delete(toDissolveItem6)
                            .add(dissolvereward7, amountcoins7);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 7902);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins7 + " @or2@tokens.");
                }
                break;

            case 22135:
            case 15645:
            case 15646:
            case 15647:
                int amountcoins8 = 2000;
                int dissolvereward8 = 10835;
                Item toDissolveItem8 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem8.getId(), toDissolveItem8.getEffect(), toDissolveItem8.getBonus())) {
                    player.getInventory().delete(toDissolveItem8)
                            .add(dissolvereward8, amountcoins8);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 10823);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins8 + " @or2@tokens.");
                }
                break;
            case 21023:
            case 21020:
            case 21021:
            case 21022:
            case 21024:
                int amountcoins9 = 3000;
                int dissolvereward9 = 10835;
                Item toDissolveItem9 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem9.getId(), toDissolveItem9.getEffect(), toDissolveItem9.getBonus())) {
                    player.getInventory().delete(toDissolveItem9)
                            .add(dissolvereward9, amountcoins9);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 15987);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins9 + " @or2@tokens.");
                }
                break;
            case 5012:
            case 4684:
            case 4685:
            case 4686:
            case 9939:
            case 8274:
            case 8273:
                int amountcoins10 = 4500;
                int dissolvereward10 = 10835;
                Item toDissolveItem10 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem10.getId(), toDissolveItem10.getEffect(), toDissolveItem10.getBonus())) {
                    player.getInventory().delete(toDissolveItem10)
                            .add(dissolvereward10, amountcoins10);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 17123);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins10 + " @or2@tokens.");
                }
                break;
            case 17698:
            case 17700:
            case 17614:
            case 17616:
            case 17618:
            case 17606:
            case 17622:
            case 11195:
                int amountcoins11 = 7500;
                int dissolvereward11 = 10835;
                Item toDissolveItem11 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem11.getId(), toDissolveItem11.getEffect(), toDissolveItem11.getBonus())) {
                    player.getInventory().delete(toDissolveItem11)
                            .add(dissolvereward11, amountcoins11);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 20423);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins11 + " @or2@tokens.");
                }
                break;
            case 23066:
            case 23067:
            case 23061:
            case 23062:
            case 23063:
            case 23068:
            case 12612:
                int amountcoins12 = 9000;
                int dissolvereward12 = 10835;
                Item toDissolveItem12 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem12.getId(), toDissolveItem12.getEffect(), toDissolveItem12.getBonus())) {
                    player.getInventory().delete(toDissolveItem12)
                            .add(dissolvereward12, amountcoins12);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 29566);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins12 + " @or2@tokens.");
                }
                break;
            case 14018:
            case 19160:
            case 19159:
            case 19158:
                int amountcoins13 = 10000;
                int dissolvereward13 = 10835;
                Item toDissolveItem13 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem13.getId(), toDissolveItem13.getEffect(), toDissolveItem13.getBonus())) {
                    player.getInventory().delete(toDissolveItem13)
                            .add(dissolvereward13, amountcoins13);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 38122);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins13 + " @or2@tokens.");
                }
                break;
            case 20427:
            case 20260:
            case 20095:
                int amountcoins14 = 15000;
                int dissolvereward14 = 10835;
                Item toDissolveItem14 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem14.getId(), toDissolveItem14.getEffect(), toDissolveItem14.getBonus())) {
                    player.getInventory().delete(toDissolveItem14)
                            .add(dissolvereward14, amountcoins14);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 47999);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins14 + " @or2@tokens.");
                }
                break;
            case 8136:
            case 8813:
            case 8814:
            case 8815:
            case 17283:
            case 16194:
            case 1857:
                int amountcoins15 = 17500;
                int dissolvereward15 = 10835;
                Item toDissolveItem15 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem15.getId(), toDissolveItem15.getEffect(), toDissolveItem15.getBonus())) {
                    player.getInventory().delete(toDissolveItem15)
                            .add(dissolvereward15, amountcoins15);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 53012);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins15 + " @or2@tokens.");
                }
                break;
            case 14188:
            case 14184:
            case 14178:
            case 14186:
            case 14180:
            case 14182:
                int amountcoins16 = 20000;
                int dissolvereward16 = 10835;
                Item toDissolveItem16 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem16.getId(), toDissolveItem16.getEffect(), toDissolveItem16.getBonus())) {
                    player.getInventory().delete(toDissolveItem16)
                            .add(dissolvereward16, amountcoins16);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 63465);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins16 + " @or2@tokens.");
                }
                break;
            case 22143:
            case 22136:
            case 22137:
            case 22138:
            case 22141:
            case 22139:
            case 22142:
                int amountcoins17 = 25000;
                int dissolvereward17 = 10835;
                Item toDissolveItem17 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem17.getId(), toDissolveItem17.getEffect(), toDissolveItem17.getBonus())) {
                    player.getInventory().delete(toDissolveItem17)
                            .add(dissolvereward17, amountcoins17);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 68912);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins17 + " @or2@tokens.");
                }
                break;
            case 13640:
            case 13964:
            case 21934:
            case 19918:
            case 19913:
            case 3107:
            case 15448:
                int amountcoins18 = 30000;
                int dissolvereward18 = 10835;
                Item toDissolveItem18 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem18.getId(), toDissolveItem18.getEffect(), toDissolveItem18.getBonus())) {
                    player.getInventory().delete(toDissolveItem18)
                            .add(dissolvereward18, amountcoins18);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 72993);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins18 + " @or2@tokens.");
                }
                break;
            case 22148:
            case 22151:
            case 22145:
            case 22146:
            case 22147:
            case 22149:
            case 22150:
                int amountcoins19 = 40000;
                int dissolvereward19 = 10835;
                Item toDissolveItem19 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem19.getId(), toDissolveItem19.getEffect(), toDissolveItem19.getBonus())) {
                    player.getInventory().delete(toDissolveItem19)
                            .add(dissolvereward19, amountcoins19);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 77123);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins19 + " @or2@tokens.");
                }
                break;
            case 17694:
            case 17696:
            case 14190:
            case 14192:
            case 14194:
            case 14200:
            case 14198:
            case 14196:
            case 12608:
                int amountcoins20 = 45000;
                int dissolvereward20 = 10835;
                Item toDissolveItem20 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem20.getId(), toDissolveItem20.getEffect(), toDissolveItem20.getBonus())) {
                    player.getInventory().delete(toDissolveItem20)
                            .add(dissolvereward20, amountcoins20);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 82913);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins20 + " @or2@tokens.");
                }
                break;
            case 17644:
            case 22100:
            case 22101:
            case 22102:
            case 22105:
            case 22103:
            case 22104:
                int amountcoins21 = 50000;
                int dissolvereward21 = 10835;
                Item toDissolveItem21 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem21.getId(), toDissolveItem21.getEffect(), toDissolveItem21.getBonus())) {
                    player.getInventory().delete(toDissolveItem21)
                            .add(dissolvereward21, amountcoins21);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 88713);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins21 + " @or2@tokens.");
                }
                break;
            case 14305:
            case 14307:
            case 14202:
            case 14204:
            case 14206:
            case 14303:
            case 14301:
                int amountcoins22 = 60000;
                int dissolvereward22 = 10835;
                Item toDissolveItem22 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem22.getId(), toDissolveItem22.getEffect(), toDissolveItem22.getBonus())) {
                    player.getInventory().delete(toDissolveItem22)
                            .add(dissolvereward22, amountcoins22);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 92031);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins22 + " @or2@tokens.");
                }
                break;
            case 22155:
            case 22152:
            case 22153:
            case 22154:
            case 22158:
            case 22159:
            case 22160:
                int amountcoins23 = 75000;
                int dissolvereward23 = 10835;
                Item toDissolveItem23 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem23.getId(), toDissolveItem23.getEffect(), toDissolveItem23.getBonus())) {
                    player.getInventory().delete(toDissolveItem23)
                            .add(dissolvereward23, amountcoins23);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 99813);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins23 + " @or2@tokens.");
                }
                break;
            case 22167:
            case 22163:
            case 22165:
            case 22164:
            case 22166:
            case 22161:
            case 22162:
                int amountcoins24 = 85000;
                int dissolvereward24 = 10835;
                Item toDissolveItem24 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem24.getId(), toDissolveItem24.getEffect(), toDissolveItem24.getBonus())) {
                    player.getInventory().delete(toDissolveItem24)
                            .add(dissolvereward24, amountcoins24);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 101233);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins24 + " @or2@tokens.");
                }
                break;
            case 5730:
            case 23079:
            case 23080:
            case 23075:
            case 23076:
            case 23077:
                int amountcoins25 = 90000;
                int dissolvereward25 = 10835;
                Item toDissolveItem25 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem25.getId(), toDissolveItem25.getEffect(), toDissolveItem25.getBonus())) {
                    player.getInventory().delete(toDissolveItem25)
                            .add(dissolvereward25, amountcoins25);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 104812);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins25 + " @or2@tokens.");
                }
                break;
            case 14319:
            case 14309:
            case 14311:
            case 14313:
            case 14321:
            case 14317:
            case 14315:
                int amountcoins26 = 100000;
                int dissolvereward26 = 10835;
                Item toDissolveItem26 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem26.getId(), toDissolveItem26.getEffect(), toDissolveItem26.getBonus())) {
                    player.getInventory().delete(toDissolveItem26)
                            .add(dissolvereward26, amountcoins26);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 107283);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins26 + " @or2@tokens.");
                }
                break;
            case 22133:
            case 14325:
            case 14327:
            case 14331:
            case 14329:
            case 14323:
                int amountcoins27 = 115000;
                int dissolvereward27 = 10835;
                Item toDissolveItem27 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem27.getId(), toDissolveItem27.getEffect(), toDissolveItem27.getBonus())) {
                    player.getInventory().delete(toDissolveItem27)
                            .add(dissolvereward27, amountcoins27);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 115767);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins27 + " @or2@tokens.");
                }
                break;
            case 14353:
            case 14349:
            case 14359:
            case 14363:
            case 14339:
            case 14347:
            case 14355:
            case 14341:
            case 14345:
            case 14343:
            case 14351:
            case 14361:
            case 14337:
            case 14357:
                int amountcoins28 = 130000;
                int dissolvereward28 = 10835;
                Item toDissolveItem28 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem28.getId(), toDissolveItem28.getEffect(), toDissolveItem28.getBonus())) {
                    player.getInventory().delete(toDissolveItem28)
                            .add(dissolvereward28, amountcoins28);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 134812);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins28 + " @or2@tokens.");
                }
                break;
            case 8828:
            case 8829:
            case 8833:
            case 8830:
            case 8831:
                int amountcoins29 = 145000;
                int dissolvereward29 = 10835;
                Item toDissolveItem29 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem29.getId(), toDissolveItem29.getEffect(), toDissolveItem29.getBonus())) {
                    player.getInventory().delete(toDissolveItem29)
                            .add(dissolvereward29, amountcoins29);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 164772);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins29 + " @or2@tokens.");
                }
                break;
            case 14369:
            case 14373:
            case 14371:
            case 14375:
            case 14365:
            case 14367:
                int amountcoins30 = 160000;
                int dissolvereward30 = 10835;
                Item toDissolveItem30 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem30.getId(), toDissolveItem30.getEffect(), toDissolveItem30.getBonus())) {
                    player.getInventory().delete(toDissolveItem30)
                            .add(dissolvereward30, amountcoins30);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 175684);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins30 + " @or2@tokens.");
                }
                break;
            case 22072:
            case 22036:
            case 22037:
            case 22038:
            case 5594:
            case 6937:
            case 3905:
                int amountcoins31 = 175000;
                int dissolvereward31 = 10835;
                Item toDissolveItem31 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem31.getId(), toDissolveItem31.getEffect(), toDissolveItem31.getBonus())) {
                    player.getInventory().delete(toDissolveItem31)
                            .add(dissolvereward31, amountcoins31);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 187443);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins31 + " @or2@tokens.");
                }
                break;
            case 20552:
            case 15008:
            case 15005:
            case 15006:
            case 15007:
            case 15100:
            case 15201:
            case 15200:
                int amountcoins32 = 190000;
                int dissolvereward32 = 10835;
                Item toDissolveItem32 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem32.getId(), toDissolveItem32.getEffect(), toDissolveItem32.getBonus())) {
                    player.getInventory().delete(toDissolveItem32)
                            .add(dissolvereward32, amountcoins32);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 190772);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins32 + " @or2@tokens.");
                }
                break;
            case 14379:
            case 14381:
            case 14383:
            case 14385:
                int amountcoins33 = 200000;
                int dissolvereward33 = 10835;
                Item toDissolveItem33 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem33.getId(), toDissolveItem33.getEffect(), toDissolveItem33.getBonus())) {
                    player.getInventory().delete(toDissolveItem33)
                            .add(dissolvereward33, amountcoins33);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 201734);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins33 + " @or2@tokens.");
                }
                break;
            case 17702:
            case 11763:
            case 11764:
            case 11765:
            case 11767:
            case 11766:
                int amountcoins34 = 225000;
                int dissolvereward34 = 10835;
                Item toDissolveItem34 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem34.getId(), toDissolveItem34.getEffect(), toDissolveItem34.getBonus())) {
                    player.getInventory().delete(toDissolveItem34)
                            .add(dissolvereward34, amountcoins34);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 217645);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins34 + " @or2@tokens.");
                }
                break;
            case 7543:
            case 7544:
            case 9481:
            case 9482:
            case 9483:
            case 7545:
                int amountcoins35 = 250000;
                int dissolvereward35 = 10835;
                Item toDissolveItem35 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem35.getId(), toDissolveItem35.getEffect(), toDissolveItem35.getBonus())) {
                    player.getInventory().delete(toDissolveItem35)
                            .add(dissolvereward35, amountcoins35);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 267433);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins35 + " @or2@tokens.");
                }
                break;
            case 16249:
            case 15832:
            case 9478:
            case 9479:
            case 9480:
            case 16265:
                int amountcoins36 = 275000;
                int dissolvereward36 = 10835;
                Item toDissolveItem36 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem36.getId(), toDissolveItem36.getEffect(), toDissolveItem36.getBonus())) {
                    player.getInventory().delete(toDissolveItem36)
                            .add(dissolvereward36, amountcoins36);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 287432);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins36 + " @or2@tokens.");
                }
                break;
            case 8410:
            case 8411:
            case 8412:
            case 13323:
            case 13324:
            case 13325:
            case 1486:
            case 13327:
            case 13326:
                int amountcoins37 = 300000;
                int dissolvereward37 = 10835;
                Item toDissolveItem37 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem37.getId(), toDissolveItem37.getEffect(), toDissolveItem37.getBonus())) {
                    player.getInventory().delete(toDissolveItem37)
                            .add(dissolvereward37, amountcoins37);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 309871);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins37 + " @or2@tokens.");
                }
                break;
            case 13333:
            case 13328:
            case 13329:
            case 13330:
            case 4369:
            case 13332:
            case 3318:
                int amountcoins38 = 500000;
                int dissolvereward38 = 10835;
                Item toDissolveItem38 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem38.getId(), toDissolveItem38.getEffect(), toDissolveItem38.getBonus())) {
                    player.getInventory().delete(toDissolveItem38)
                            .add(dissolvereward38, amountcoins38);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 365772);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins38 + " @or2@tokens.");
                }
                break;

            case 2028:
            case 2030:
            case 2032:
            case 2034:
            case 2036:
            case 2038:
            case 2040:
            case 2042:
            case 2044:
            case 2046:
            case 2048:
            case 2050:
            case 2052:
            case 2054:
            case 2056:
            case 2058:
            case 2060:
            case 2062:
            case 2064:
            case 2066:
            case 2068:
            case 2070:
            case 2072:
            case 2074:
            case 2076:
            case 2078:
            case 2080:
            case 2082:
            case 2084:
            case 2086:
            case 2088:
            case 2090:
            case 2092:
            case 2094:
            case 2096:
            case 2098:
            case 2100:
            case 2102:
            case 2104:
            case 2106:
            case 2108:
            case 2110:
            case 2112:
            case 2114:
            case 2116:
            case 2118:
            case 2120:
            case 2122:
            case 2124:
            case 2126:
            case 2128:
            case 2130:
            case 2132:
            case 2134:
            case 2136:
            case 2138:
            case 2140:
            case 2142:
            case 2144:
            case 2146:
            case 2148:
            case 2150:
            case 2152:
            case 2154:
            case 2156:
            case 2158:
            case 2160:
            case 2162:
            case 2164:
            case 2166:
            case 2168:
            case 2170:
            case 2172:
            case 2174:
            case 2176:
            case 2178:
            case 2180:
            case 2182:
            case 2184:
            case 2186:
            case 2188:
            case 2190:
            case 2192:
            case 2194:
            case 2196:
            case 2198:
            case 2200:
            case 2202:
            case 2204:
            case 2206:
            case 2208:
            case 2210:
            case 2212:
            case 2214:
            case 2216:
            case 2218:
            case 2220:
            case 2222:
            case 2224:
            case 2226:
            case 2228:
            case 2230:
            case 2232:
            case 2234:
            case 2236:
            case 2238:
            case 2240:
            case 2242:
            case 2244:
            case 2246:
            case 2248:
            case 2250:
            case 2252:
            case 2254:
            case 2256:
            case 2258:
            case 2260:
            case 2262:
            case 2264:
            case 2266:
            case 2268:
            case 2270:
            case 2272:
            case 2274:
            case 2276:
            case 2278:
            case 2280:
            case 2282:
            case 2284:
            case 2286:
            case 2288:
            case 2290:
            case 2292:
            case 2294:
            case 2296:
            case 2298:
            case 2300:
            case 2302:
            case 2304:
            case 2306:
            case 2308:
            case 2310:
            case 2312:
            case 2314:
            case 2316:
            case 2318:
            case 2320:
            case 2322:
            case 2324:
            case 2326:
            case 2328:
            case 2330:
            case 2332:
            case 2334:
            case 2336:
            case 2338:
            case 2340:
            case 2342:
            case 2344:
            case 2346:
            case 2348:
            case 2350:
            case 2352:
            case 2354:
            case 2356:
            case 2358:
            case 2360:
            case 2362:
            case 2364:
            case 2366:
            case 2368:
            case 2370:
            case 2372:
            case 2374:
            case 2376:
            case 2378:
            case 2384:
            case 2386:
            case 2388:
            case 2390:
            case 2392:
            case 2394:
            case 2396:
            case 2398:
            case 2400:
            case 2402:
            case 2404:
            case 2406:
            case 2408:
            case 2410:
            case 2412:
            case 2414:
            case 2416:
            case 2418:
            case 2420:
            case 2422:
            case 2424:
            case 2426:
            case 2428:
            case 2430:
            case 2432:
            case 2434:
            case 2436:
            case 2438:
            case 2440:
            case 2442:
            case 2444:
            case 2446:
            case 2448:
            case 2450:
            case 2452:
            case 2454:
            case 2456:
            case 2458:
            case 2460:
            case 2462:
            case 2464:
            case 2466:
            case 2468:
            case 2470:
            case 2472:
            case 2474:
            case 2476:
            case 2478:
            case 2480:
            case 2482:
            case 2484:
            case 2486:
            case 2488:
            case 2490:
            case 2492:
            case 2494:
            case 2496:
            case 2498:
            case 2500:
            case 2502:
            case 2504:
            case 2506:
            case 2508:
            case 2510:
            case 2512:
            case 2514:
            case 2516:
            case 2518:
            case 2520:
            case 2522:
            case 2524:
            case 2526:
            case 2528:
            case 2530:
            case 2688:
            case 2532:
            case 2534:
            case 2536:
            case 2538:
            case 2540:
            case 2542:
            case 2544:
            case 2546:
            case 2548:
            case 2550:
            case 2552:
            case 2554:
            case 2556:
            case 2558:
            case 2560:
            case 2562:
            case 2564:
            case 2566:
            case 2568:
            case 2570:
            case 2572:
            case 2574:
            case 2576:
            case 2578:
            case 2580:
            case 2582:
            case 2584:
            case 2586:
            case 2588:
            case 2590:
            case 2592:
            case 2594:
            case 2596:
            case 2598:
            case 2600:
            case 2602:
            case 2604:
            case 2606:
            case 2608:
            case 2610:
            case 2612:
            case 2614:
            case 2616:
            case 2618:
            case 2620:
            case 2622:
            case 2624:
            case 2626:
            case 2628:
            case 2630:
            case 2632:
            case 2634:
            case 2636:
            case 2638:
            case 2640:
            case 2642:
            case 2644:
            case 2646:
            case 2648:
            case 2650:
            case 2652:
            case 2654:
            case 2656:
            case 2658:
            case 2660:
            case 2662:
            case 2664:
            case 2666:
            case 2668:
            case 2670:
            case 2672:
            case 2674:
            case 2676:
            case 2678:
            case 2680:
            case 2682:
            case 2684:
            case 2686:
                int amountcoins39 = 1;
                int dissolvereward39 = 20503;
                Item toDissolveItem39 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem39.getId(), toDissolveItem39.getEffect(), toDissolveItem39.getBonus())) {
                    player.getInventory().delete(toDissolveItem39)
                            .add(dissolvereward39, amountcoins39);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 1000);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(itemId).getName() + "@or2@ for@red@ " + amountcoins39 + " @or2@token.");
                }
                break;


            case 23014/*done*/:
            case 23015:
            case 23016:
            case 23017:
                if (player.getInventory().contains(itemId)) {
                    player.getInventory().delete(itemId, 1);
                    player.getInventory().add(23013, 1);
                    player.sendMessage("You reverted your mask.");
                }
                break;
           /* case 15330:
                if (player.getInventory().contains(15330) &&
                        player.getInventory().getAmount(ItemDefinition.MILL_ID) >= 50_000_000) {
                    player.getInventory().delete(ItemDefinition.MILL_ID, 50_000_000);
                    player.getInventory().delete(15330, 1);
                    player.getInventory().add(15328, 1);
                    player.sendMessage("Congratulations you have upgraded your infinite super overload to a infinity rage potion!");
                    String msg = "@blu@<img=5>[CREATION]<img=5>@red@ " + player.getUsername() + " has created an Infinity Rage potion!";
                    World.sendMessage(msg);
                } else {
                    player.sendMessage("You need 50m Solak tokens to upgrade.");
                }
                break;*/
            case 22108:
                DialogueManager.start(player, 9924);
                player.setDialogueActionId(9924);
                break;
           /* case 19000:
                if (player.getInventory().contains(19000)) {
                    int amount = player.getInventory().getAmount(19000);
                    player.getInventory().delete(19000, amount);
                    player.getInventory().add(ItemDefinition.MILL_ID, amount * 100);
                    player.sendMessage("You have exchanged X" + amount + " Pet fragments for X" + amount * 100 + " Solak tokens!");
                }
                break;*/
           /* case 22000:
                if (player.getInventory().contains(22000) && player.getInventory().getAmount(ItemDefinition.MILL_ID) >= 250000 && player.getInventory().getAmount(5023) >= 250) {
                    player.getInventory().delete(ItemDefinition.MILL_ID, 250000);
                    player.getInventory().delete(5023, 250);
                    player.getInventory().delete(22000, 1);
                    player.getInventory().add(22001, 1);
                    player.sendMessage("Congratulations you have upgraded your helm to t2. ");
                } else {
                    player.sendMessage("You need 250k Tokens and 250 boss slayer tickets to upgrade the helm to t2.");
                }
                break;
            case 22001:
                if (player.getInventory().contains(22001) && player.getInventory().getAmount(ItemDefinition.MILL_ID) >= 500000 && player.getInventory().getAmount(5023) >= 500) {
                    player.getInventory().delete(ItemDefinition.MILL_ID, 500000);
                    player.getInventory().delete(5023, 500);
                    player.getInventory().delete(22001, 1);
                    player.getInventory().add(22002, 1);
                    player.sendMessage("Congratulations you have upgraded your helm to t2. ");
                } else {
                    player.sendMessage("You need 500k Tokens and 500 boss slayer tickets to upgrade the helm to t3.");
                }
                break;
            case 22002:
                if (player.getInventory().contains(22002) && player.getInventory().getAmount(ItemDefinition.MILL_ID) >= 2500000 && player.getInventory().getAmount(5023) >= 2500) {
                    player.getInventory().delete(ItemDefinition.MILL_ID, 2500000);
                    player.getInventory().delete(5023, 2500);
                    player.getInventory().delete(22002, 1);
                    player.getInventory().add(22003, 1);
                    player.sendMessage("Congratulations you have upgraded your helm to t4. ");
                } else {
                    player.sendMessage("You need 2.5m Tokens and 2500 boss slayer tickets to upgrade the helm to t4.");
                }
                break;
            case 22003:
                if (player.getInventory().contains(22003) && player.getInventory().getAmount(ItemDefinition.MILL_ID) >= 5000000 && player.getInventory().getAmount(5023) >= 5000) {
                    player.getInventory().delete(ItemDefinition.MILL_ID, 5000000);
                    player.getInventory().delete(5023, 5000);
                    player.getInventory().delete(22003, 1);
                    player.getInventory().add(22004, 1);
                    player.sendMessage("Congratulations you have upgraded your helm to t5. ");
                } else {
                    player.sendMessage("You need 5m Tokens and 5000 boss slayer tickets to upgrade the helm to t5.");
                }
                break;*/

          /*  case 5021:

                int amount1 = player.getInventory().getAmount(5021);
                if (amount1 > 2147 || amount1 + player.getInventory().getAmount(ItemDefinition.COIN_ID) > 2147000000) {
                    long amountLeft;
                    if (!player.getInventory().contains(ItemDefinition.COIN_ID))
                        amountLeft = (long) (((long) amount1 * (long) 1000000) - (long) 2147000000);
                    else
                        amountLeft = ((long) amount1 * (long) 1000000) - (long) (2147000000 - player.getInventory().getAmount(ItemDefinition.COIN_ID));
                    player.getInventory().delete(5021, amount1);
                    player.getInventory().add(ItemDefinition.COIN_ID, 2147000000 - (player.getInventory().getAmount(ItemDefinition.COIN_ID)));
                    player.setMoneyInPouch(player.getMoneyInPouch() + amountLeft);
                    player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
                    player.sendMessage("<shad=1>@red@The rest of the cash(" + amountLeft / 1000000
                            + "M) has been added to your @blu@pouch@red@!");
                    return;
                }
                player.getInventory().delete(5021, amount1);
                player.getInventory().add(ItemDefinition.COIN_ID, 1000000 * amount1);
                break;*/

            case 11846:
            case 11848:
            case 11850:
            case 11852:
            case 11854:
            case 11856:
                if (!player.getClickDelay().elapsed(250) || !player.getInventory().contains(itemId))
                    return;
                if (player.busy()) {
                    player.getPacketSender().sendMessage("You cannot open this right now.");
                    return;
                }
                int amountToOpen = player.getInventory().getAmount(itemId);
                if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
                    amountToOpen = player.getInventory().getFreeSlots() / 4;
                    if (amountToOpen > player.getInventory().getAmount(itemId))
                        amountToOpen = player.getInventory().getAmount(itemId);
                }
                if (amountToOpen == 0) {
                    player.getPacketSender().sendMessage("You do not have enough inventory space to do this.");
                    return;
                }

                int[] items = itemId == 11858 ? new int[]{10350, 10348, 10346, 10352}
                        : itemId == 19580 ? new int[]{19308, 19311, 19314, 19317, 19320}
                        : itemId == 11860 ? new int[]{10334, 10330, 10332, 10336}
                        : itemId == 11862 ? new int[]{10342, 10338, 10340, 10344}
                        : itemId == 11848 ? new int[]{4716, 4720, 4722, 4718}
                        : itemId == 11856 ? new int[]{4753, 4757, 4759, 4755}
                        : itemId == 11850 ? new int[]{4724, 4728, 4730, 4726}
                        : itemId == 11854 ? new int[]{4745, 4749, 4751, 4747}
                        : itemId == 11852 ? new int[]{4732, 4734, 4736, 4738}
                        : itemId == 11846 ? new int[]{4708, 4712, 4714, 4710}
                        : new int[]{itemId};

                int[][] tabs = new int[items.length][2];

                int index = 0;
                for (int z : items) {
                    tabs[index][0] = z;
                    tabs[index][1] = Bank.getTabForItem(player, z);
                    index++;
                }

                for (int i = 0; i < amountToOpen; i++) {
                    player.getInventory().delete(itemId, 1, false);

                    for (int[] z : tabs) {
                        if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
                            player.getInventory().add(new Item(z[0], 1), false);
                        } else {
                            Item item = new Item(z[0], 1);
                            if (ItemDefinition.forId(item.getId()).isNoted()) {
                                item.setId(Item.getUnNoted(item.getId()));
                            }
                            player.getBank(z[1]).add(item, false);
                            //player.depositItemBank(z[1], new Item(z[0], 1), false);
                        }
                    }
                }
                player.getClickDelay().reset();
                player.getInventory().refreshItems();
                player.getPacketSender().sendMessage("You opened " + amountToOpen + " sets.");
                break;
            case 5500:
                if (player.getPointsHandler().getSlayerRate() >= 6) {
                    player.sendMessage("You can't claim more than X6 Multipliers total!");
                    return;
                }

                player.getPointsHandler().incrementSlayerRate(1);
                player.getPacketSender().sendMessage("You have increased your slayer rate. @blu@"
                        + player.getPointsHandler().getSlayerRate() + "@bla@ is now your total multiplier.");
                PlayerPanel.refreshPanel(player);
                player.getInventory().delete(5500, 1);


                break;
            case ItemDefinition.COIN_ID:
                ConvertCoins.convertMillCoins(player);
                break;
            case ItemDefinition.TOKEN_ID:
                int amount = player.getInventory().getAmount(ItemDefinition.TOKEN_ID);
                if (amount >= 2147483)
                    amount = 2147483;

                int sum = (int) ((double) player.getInventory().getAmount(ItemDefinition.COIN_ID) + (double) (amount * 1_000));

                if (sum >= Integer.MAX_VALUE || sum <= 0)
                    amount = (int) (2147483 - Math.ceil(((double) player.getInventory().getAmount(ItemDefinition.COIN_ID) / (double) 1_000)));

                player.getInventory().delete(ItemDefinition.TOKEN_ID, amount);
                player.getInventory().add(ItemDefinition.COIN_ID, amount * 1_000);
                break;
            case 12845:
                player.getPointsHandler().incrementPengRate(2);
                player.getPacketSender().sendMessage("You have increased your penguin rate. @blu@"
                        + player.getPointsHandler().getPengRate() + "@bla@ is now your total multiplier.");
                PlayerPanel.refreshPanel(player);
                player.getInventory().delete(12845, 1);
                break;
            case 5154:
                player.getPointsHandler().incrementPengRate(100);
                player.getPacketSender().sendMessage("You have increased your penguin rate. @blu@"
                        + player.getPointsHandler().getPengRate() + "@bla@ is now your total multiplier.");
                PlayerPanel.refreshPanel(player);
                player.getInventory().delete(5154, 1);
                break;
            case 5155:
                player.getPointsHandler().incrementPengRate(1000);
                player.getPacketSender().sendMessage("You have increased your penguin rate @blu@"
                        + player.getPointsHandler().getPengRate() + "@bla@ is now your total multiplier.");
                PlayerPanel.refreshPanel(player);
                player.getInventory().delete(5155, 1);
                break;
            case 5156:
                player.getPointsHandler().incrementPengRate(10000);
                player.getPacketSender().sendMessage("You have increased your penguin rate @blu@"
                        + player.getPointsHandler().getPengRate() + "@bla@ is now your total multiplier.");
                PlayerPanel.refreshPanel(player);
                player.getInventory().delete(5156, 1);
                break;
            case 7510:
                long plc = player.getConstitution();
                long plcr = plc - 1;

                if (plc == 1) {
                    plcr = 0;
                }

                if (plcr > 0) {
                    player.performAnimation(new Animation(829));
                    Hit h = new Hit(plcr);
                    player.dealDamage(h);
                    player.forceChat(Misc.randomElement(ROCK_CAKE));
                } else {
                    player.getPacketSender().sendMessage("You'll die if you keep eating this putrid rock!");
                }
                break;
            // case 13738:
            case 13740:
            case 13742:
                // case 13744:
                if (player.isSpiritDebug()) {
                    player.getPacketSender().sendMessage("You toggle your Spirit Shield to not display specific messages.");
                    player.setSpiritDebug(false);
                } else if (player.isSpiritDebug() == false) {
                    player.getPacketSender().sendMessage("You toggle your Spirit Shield to display specific messages.");
                    player.setSpiritDebug(true);
                }
                break;
            case 23020:
                if (player.busy()) {
                    player.getPacketSender().sendMessage("You can not do this right now.");
                    return;
                }
                if (!player.getInventory().contains(23020) || player.getInventory().getAmount(23020) < 1) {
                    return;
                }
                int amt = player.getInventory().getAmount(23020);
                int minutesEXP = 30 * amt;
                int minutesDR = 3 * amt;
                //int minutesDMG = 2 * amt;

                player.getInventory().delete(23020, amt);
                player.getInventory().add(ItemDefinition.COIN_ID, 500000  * amt);
                player.getPacketSender()
                        .sendMessage("@blu@You are rewarded " + (amt * 1) + " vote "
                                + (amt > 1 ? "points, " : "point, ") + (1000 * amt) + " Millions, and " + (1000 * amt) + " PVM Tickets!");
                player.getPacketSender()
                        .sendMessage("@blu@You received " + minutesEXP + " minutes of Bonus Xp, " + minutesDR + " minutes of x2 DR");
                player.getPointsHandler().incrementVotingPoints(amt * 1);
                BonusExperienceTask.addBonusXp(player, minutesEXP);
                VotingDRBoostTask.addBonusDR(player, minutesDR);
                // VotingDMGBoostTask.addBonusDMG(player, minutesDMG);
                StarterTasks.finishTask(player, StarterTaskData.REDEEM_A_VOTE_SCROLL);

                Achievements.doProgress(player, Achievements.Achievement.VOTE_10_TIMES, amt);
                Achievements.doProgress(player, Achievements.Achievement.VOTE_50_TIMES, amt);
                Achievements.doProgress(player, Achievements.Achievement.VOTE_100_TIMES, amt);

                player.getClickDelay().reset();
                break;
            case 10138:
                if (player.busy()) {
                    player.getPacketSender().sendMessage("You can not do this right now.");
                    return;
                }
                VoteRewardHandler.AFKFISH(player, true);
                break;
            case 17634:
                if (player.busy()) {
                    player.getPacketSender().sendMessage("You can not do this right now.");
                    return;
                }
                VoteRewardHandler.AFKMINE(player, true);
                break;
            case 6500:
                CharmingImp.sendConfig(player);
                break;
            case 13281:
            case 13282:
            case 13283:
            case 13284:
            case 13285:
            case 13286:
            case 13287:
            case 13288:
            case 18819:
                player.getPacketSender().sendInterfaceRemoval();
                player.getPacketSender().sendMessage(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK
                        ? ("You do not have a Slayer task.")
                        : ("You're assigned to kill "
                        + player.getSlayer().getSlayerTask().getName()
                        + "s, only " + player.getSlayer().getAmountToSlay() + " more to go."));
                break;
            case 6570:
                if (player.getInventory().contains(6570) && player.getInventory().getAmount(6529) >= 50000) {
                    player.getInventory().delete(6570, 1).delete(6529, 50000).add(19111, 1);
                    player.getPacketSender().sendMessage("You have upgraded your Fire cape into a TokHaar-Kal cape!");
                } else {
                    player.getPacketSender().sendMessage(
                            "You need at least 50.000 Tokkul to upgrade your Fire Cape into a TokHaar-Kal cape.");
                }
                break;
            case 15262:
                if (!player.getClickDelay().elapsed(1300))
                    return;
                amt = player.getInventory().getAmount(15262);
                if (amt > 0)
                    player.getInventory().delete(15262, amt).add(18016, 10000 * amt);
                player.getClickDelay().reset();
                break;
            case 5509:
            case 5510:
            case 5512:
            case 5514:
                RunecraftingPouches.empty(player, RunecraftingPouch.forId(itemId));
                break;
            case 11283: // DFS
                player.getPacketSender()
                        .sendMessage("Your Dragonfire shield has " + player.getDfsCharges() + "/20 dragon-fire charges.");
                break;
        }
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getConstitution() <= 0)
            return;

        switch (packet.getOpcode()) {
            case THIRD_ITEM_ACTION_OPCODE:
                thirdAction(player, packet);
                break;
            case FIRST_ITEM_ACTION_OPCODE:
                firstAction(player, packet);
                break;
            case SECOND_ITEM_ACTION_OPCODE:
                secondAction(player, packet);
                break;
        }
    }

}
