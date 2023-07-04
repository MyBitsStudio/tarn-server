package com.ruse.world.content.dialogue;

import com.ruse.GameSettings;
import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.engine.task.impl.BonusExperienceTask;
import com.ruse.model.*;
import com.ruse.model.Locations.Location;
import com.ruse.model.container.impl.Shop.ShopManager;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.input.impl.*;
import com.ruse.util.Misc;
import com.ruse.util.RandomUtility;
import com.ruse.world.World;
import com.ruse.world.content.AchievementsOLD.AchievementDataOLD;
import com.ruse.world.content.*;
import com.ruse.world.content.Gambling.FlowersData;
import com.ruse.world.content.dailytasks_new.DailyTasks;
import com.ruse.world.content.dialogue.impl.AgilityTicketExchange;
import com.ruse.world.content.dialogue.impl.Mandrith;
import com.ruse.world.content.groupironman.GroupManager;
import com.ruse.world.content.minigames.impl.*;
import com.ruse.world.content.skill.impl.construction.Construction;
import com.ruse.world.content.skill.impl.mining.Mining;
import com.ruse.world.content.skill.impl.old_dungeoneering.Dungeoneering;
import com.ruse.world.content.skill.impl.old_dungeoneering.DungeoneeringFloor;
import com.ruse.world.content.skill.impl.slayer.BossSlayerDialogues;
import com.ruse.world.content.skill.impl.slayer.Slayer;
import com.ruse.world.content.skill.impl.slayer.SlayerDialogues;
import com.ruse.world.content.skill.impl.slayer.SlayerMaster;
import com.ruse.world.content.skill.impl.summoning.CharmingImp;
import com.ruse.world.content.skill.impl.summoning.SummoningTab;
import com.ruse.world.content.tradingpost.dialogues.CancelOptions;
import com.ruse.world.content.tradingpost.dialogues.PurchaseOptions;
import com.ruse.world.content.tradingpost.dialogues.PurchaseStatement;
import com.ruse.world.content.transportation.CityTeleports;
import com.ruse.world.content.transportation.JewelryTeleports;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.content.wellForGlobalBosses.WellForGlobalBossesInterface;
import com.ruse.world.content.zombie.ZombieRaidData;
import com.ruse.world.content.zombie.ZombieRaids;
import com.ruse.world.entity.impl.npc.NpcAggression;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.impl.Normal;

public class DialogueOptions {

    // Last id used = 88

    public static int FIRST_OPTION_OF_FIVE = 2494;
    public static int SECOND_OPTION_OF_FIVE = 2495;
    public static int THIRD_OPTION_OF_FIVE = 2496;
    public static int FOURTH_OPTION_OF_FIVE = 2497;
    public static int FIFTH_OPTION_OF_FIVE = 2498;
    public static int FIRST_OPTION_OF_FOUR = 2482;
    public static int SECOND_OPTION_OF_FOUR = 2483;
    public static int THIRD_OPTION_OF_FOUR = 2484;
    public static int FOURTH_OPTION_OF_FOUR = 2485;
    public static int FIRST_OPTION_OF_THREE = 2471;
    public static int SECOND_OPTION_OF_THREE = 2472;
    public static int THIRD_OPTION_OF_THREE = 2473;
    public static int FIRST_OPTION_OF_TWO = 2461;
    public static int SECOND_OPTION_OF_TWO = 2462;

    public static void handle(Player player, int id) {
        if (player.getRank().isDeveloper()) {
            player.getPacketSender()
                    .sendMessage("Dialogue button id: " + id + ", action id: " + player.getDialogueActionId())
                    .sendConsoleMessage("Dialogue button id: " + id + ", action id: " + player.getDialogueActionId());
        }
        if (Effigies.handleEffigyAction(player, id)) {
            return;
        }
        if (id == FIRST_OPTION_OF_FIVE) {
            switch (player.getDialogueActionId()) {
                case 11050:
                    if(player.getInventory().contains(23210, 350)){
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getInventory().delete(23210, 350);
                        player.getInventory().add(23127, 1);
                        player.sendMessage("@yel@[COMBINE] You have successfully created a Mystical Helmet.");
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("You need 350 Event Crystals to combine this item.");
                    }
                    break;
                case 101:
                    player.sendMessage("POs is currently disabled");
                   // player.getPlayerOwnedShopManager().openMain();
                    break;
                case 9924:
                    player.setInputHandling(new WithdrawCurrencyFromCurrencyPouch(0));
                    player.getPacketSender().sendEnterAmountPrompt("How many Tokens would you like to withdraw?");
                    break;
                case 9925:
                    player.setInputHandling(new WithdrawCurrencyFromCurrencyPouch(4));
                    player.getPacketSender().sendEnterAmountPrompt("How many Vote scrolls would you like to withdraw?");
                    break;
                case 831:
                    if (player.getInventory().contains(ItemDefinition.TOKEN_ID)) {
                        player.getInventory().delete(ItemDefinition.TOKEN_ID, 1);
                        LotterySystem.addUser(player, 1);
                    } else {
                        player.sendMessage("You don't have enough to buy a ticket");
                    }
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 2461:
                    break;
                case 216:
                    break;
                case 44:
                    break;
                case 150:
                    TeleportHandler.teleportPlayer(player, CityTeleports.AL_KHARID.getPos(), TeleportType.NORMAL);
                    player.getPacketSender().sendMessage(CityTeleports.AL_KHARID.getHint());
                    break;
                case 151:
                    TeleportHandler.teleportPlayer(player, CityTeleports.DRAYNOR.getPos(), TeleportType.NORMAL);
                    player.getPacketSender().sendMessage(CityTeleports.DRAYNOR.getHint());
                    break;
                case 153:
                    // GWD Here
                    TeleportHandler.teleportPlayer(player, new Position(2871, 5318, 2), TeleportType.NORMAL);
                    break;
                case 152:
                    TeleportHandler.teleportPlayer(player, CityTeleports.VARROCK.getPos(), TeleportType.NORMAL);
                    player.getPacketSender().sendMessage(CityTeleports.VARROCK.getHint());
                    break;
                case 0:
                    TeleportHandler.teleportPlayer(player, new Position(2679, 3717),
                            player.getSpellbook().getTeleportType());
                    break;

                case 1:
                    TeleportHandler.teleportPlayer(player, new Position(3420, 3510),
                            player.getSpellbook().getTeleportType());
                    break;
                case 2:
                    TeleportHandler.teleportPlayer(player, new Position(3235, 3295),
                            player.getSpellbook().getTeleportType());
                    break;
                case 9:
                    DialogueManager.start(player, 16);
                    break;
                case 10:
                    Artifacts.sellArtifacts(player);
                    break;
                case 11:
                    Scoreboards.open(player, Scoreboards.TOP_KC);
                    break;
                case 12:
                    TeleportHandler.teleportPlayer(player, new Position(3087, 3517),
                            player.getSpellbook().getTeleportType());
                    break;
                case 13:
                    player.setDialogueActionId(78);
                    DialogueManager.start(player, 124);
                    break;
                case 14: // Edgeville
                    TeleportHandler.teleportPlayer(player, new Position(1859 + Misc.getRandom(1), 5237 + Misc.getRandom(1)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 15:
                    player.getPacketSender().sendInterfaceRemoval();
                    int total = player.getSkillManager().getMaxLevel(Skill.ATTACK)
                            + player.getSkillManager().getMaxLevel(Skill.STRENGTH);
                    boolean has99 = player.getSkillManager().getMaxLevel(Skill.ATTACK) >= 99
                            || player.getSkillManager().getMaxLevel(Skill.STRENGTH) >= 99;
                    if (total < 130 && !has99) {
                        player.getPacketSender().sendMessage("");
                        player.getPacketSender().sendMessage("You do not meet the requirements of a Warrior.");
                        player.getPacketSender()
                                .sendMessage("You need to have a total Attack and Strength level of at least 140.");
                        player.getPacketSender().sendMessage("Having level 99 in either is fine aswell.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(2855, 3543),
                            player.getSpellbook().getTeleportType());
                    break;
                case 29:
                    SlayerMaster.changeSlayerMaster(player, SlayerMaster.EASY_SLAYER);
                    break;
                case 36:
                    TeleportHandler.teleportPlayer(player, new Position(2871, 5318, 2),
                            player.getSpellbook().getTeleportType());
                    break;
                case 38:
                    TeleportHandler.teleportPlayer(player, new Position(2273, 4681),
                            player.getSpellbook().getTeleportType());
                    break;
                case 40:
                    TeleportHandler.teleportPlayer(player, new Position(3476, 9502),
                            player.getSpellbook().getTeleportType());
                    break;
                case 48:
                    for (int index = 0; index < JewelryTeleports.values().length; index++) {
                        if (index == JewelryTeleports.jewelIndex(player.getStrippedJewelryName())
                                && player.getClickDelay().elapsed(4500) && !player.getMovementQueue().isLockMovement()) {
                            for (int i = 0; i < JewelryTeleports.values()[index].getItemId().length; i++) {
                                if (player.getSelectedSkillingItem() == JewelryTeleports.values()[index].getItemIdIndex(i)
                                        && player.getSelectedSkillingItemTwo() == JewelryTeleports.values()[index]
                                        .getItemIdIndex(i + 1)) {
                                    player.getInventory().delete(player.getSelectedSkillingItem(), 1);
                                    player.getInventory().add(player.getSelectedSkillingItemTwo(), 1);

                                    TeleportHandler.teleportPlayer(player, new Position(3088, 3506),
                                            TeleportType.RING_TELE);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    player.getClickDelay().reset();
                    break;
                case 195:
                    TeleportHandler.teleportPlayer(player, new Position(3088, 3506), TeleportType.RING_TELE);
                    player.getClickDelay().reset();
                    break;
                case 60:
                case 202:
                    player.setDialogueActionId(61);
                    DialogueManager.start(player, 102);
                    break;
                case 67:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                        if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
                                .equals(player.getUsername())) {
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                    .setDungeoneeringFloor(DungeoneeringFloor.FIRST_FLOOR);
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                    .sendMessage("The party leader has changed floor.");
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
                        }
                    }
                    break;
                case 68:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                        if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
                                .equals(player.getUsername())) {
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setComplexity(1);
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                    .sendMessage("The party leader has changed complexity.");
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
                        }
                    }
                    break;
                case 78:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (!player.getUnlockedLoyaltyTitles()[LoyaltyProgramme.LoyaltyTitles.VETERAN.ordinal()]) {
                        player.getPacketSender().sendMessage(
                                "You must have unlocked the 'Veteran' Loyalty Title in order to buy this cape.");
                        return;
                    }
                    boolean usePouch = false;
                    if (!usePouch && player.getInventory().getAmount(ItemDefinition.COIN_ID) < 75000000) {
                        player.getPacketSender().sendMessage("You do not have enough coins.");
                        return;
                    }
                    if (usePouch) {
                    } else
                        player.getInventory().delete(ItemDefinition.COIN_ID, 75000000);
                    player.getInventory().add(14021, 1);
                    player.getPacketSender().sendMessage("You've purchased a Veteran cape.");
                    DialogueManager.start(player, 122);
                    player.setDialogueActionId(76);
                    break;
                case 88:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getDonator().isClericPlus()) {
                        player.getPacketSender().sendMessage("You need to be a member to teleport to this zone.")
                                .sendMessage("To become a member, use the command ::store and browse our store.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, GameSettings.MEMBER_ZONE,
                            player.getSpellbook().getTeleportType());
                    break;
            }
        } else if (id == SECOND_OPTION_OF_FIVE) {
            switch (player.getDialogueActionId()) {
                case 11050:
                    if(player.getInventory().contains(23210, 500)){
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getInventory().delete(23210, 500);
                        player.getInventory().add(23128, 1);
                        player.sendMessage("@yel@[COMBINE] You have successfully created a Mystical Body.");
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("You need 500 Event Crystals to combine this item.");
                    }
                    break;
                case 9924:
                    player.setInputHandling(new WithdrawCurrencyFromCurrencyPouch(1));
                    player.getPacketSender().sendEnterAmountPrompt("How many Pvm tickets would you like to withdraw?");
                    break;
                case 9925:
                    player.setInputHandling(new WithdrawCurrencyFromCurrencyPouch(5));
                    player.getPacketSender().sendEnterAmountPrompt("How many Pet fragments would you like to withdraw?");
                    break;
                case 831:
                    player.getPacketSender()
                            .sendEnterInputPrompt("How many would you like to buy (1 Lottery ticket costs 1 Upgrade orb)");
                    player.setInputHandling(new EnterLotteryTicketAmount());
                    break;
                case 150:
                    TeleportHandler.teleportPlayer(player, CityTeleports.ARDOUGNE.getPos(), TeleportType.NORMAL);
                    player.getPacketSender().sendMessage(CityTeleports.ARDOUGNE.getHint());
                    break;
                case 151:
                    TeleportHandler.teleportPlayer(player, CityTeleports.EDGEVILLE.getPos(), TeleportType.NORMAL);
                    player.getPacketSender().sendMessage(CityTeleports.EDGEVILLE.getHint());
                    break;

                case 153:
                    // Stykewyrm Cavern here
                    TeleportHandler.teleportPlayer(player, new Position(2731, 5095, 0), TeleportType.NORMAL);
                    break;
                case 152:
                    TeleportHandler.teleportPlayer(player, CityTeleports.KARAMJA.getPos(), TeleportType.NORMAL);
                    player.getPacketSender().sendMessage(CityTeleports.KARAMJA.getHint());
                    break;
                case 0:
                    TeleportHandler.teleportPlayer(player,
                            new Position(3557 + (Misc.getRandom(2)), 9946 + Misc.getRandom(2)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 1:
                    TeleportHandler.teleportPlayer(player, new Position(2933, 9849),
                            player.getSpellbook().getTeleportType());
                    break;
                case 2:
                    TeleportHandler.teleportPlayer(player, new Position(2802, 9148),
                            player.getSpellbook().getTeleportType());
                    break;
                case 9:
                    Lottery.enterLottery(player);
                    break;
                case 10:
                    player.setDialogueActionId(59);
                    DialogueManager.start(player, 100);
                    break;
                case 11:
                    Scoreboards.open(player, Scoreboards.TOP_DONATION);
                    break;
                case 12:
                    TeleportHandler.teleportPlayer(player, new Position(2980 + Misc.getRandom(3), 3596 + Misc.getRandom(3)),
                            player.getSpellbook().getTeleportType());
                    break;

                case 14:
                    TeleportHandler.teleportPlayer(player, new Position(2041, 5242, 0), TeleportType.NORMAL);
                    break;
                case 15:
                    TeleportHandler.teleportPlayer(player, new Position(2663 + Misc.getRandom(1), 2651 + Misc.getRandom(1)),
                            player.getSpellbook().getTeleportType());
                    break;
                case 29:
                    SlayerMaster.changeSlayerMaster(player, SlayerMaster.MEDIUM_SLAYER);
                    // System.out.println("CALLED");
                    break;
                case 30:
                    player.getPacketSender().sendString(36030,
                            "Current Points:   " + player.getPointsHandler().getSlayerPoints());
                    //  player.getPacketSender().sendInterface(36000);
                    break;
                case 36:
                    TeleportHandler.teleportPlayer(player, new Position(1908, 4367),
                            player.getSpellbook().getTeleportType());
                    break;
                case 38:
                    DialogueManager.start(player, 70);
                    player.setDialogueActionId(39);
                    break;
                case 40:
                    TeleportHandler.teleportPlayer(player, new Position(2839, 9557),
                            player.getSpellbook().getTeleportType());
                    break;
                case 48:
                    for (int index = 0; index < JewelryTeleports.values().length; index++) {
                        if (index == JewelryTeleports.jewelIndex(player.getStrippedJewelryName())
                                && player.getClickDelay().elapsed(4500) && !player.getMovementQueue().isLockMovement()) {
                            for (int i = 0; i < JewelryTeleports.values()[index].getItemId().length; i++) {
                                if (player.getSelectedSkillingItem() == JewelryTeleports.values()[index].getItemIdIndex(i)
                                        && player.getSelectedSkillingItemTwo() == JewelryTeleports.values()[index]
                                        .getItemIdIndex(i + 1)) {
                                    player.getInventory().delete(player.getSelectedSkillingItem(), 1);
                                    player.getInventory().add(player.getSelectedSkillingItemTwo(), 1);

                                    TeleportHandler.teleportPlayer(player, new Position(2918, 3176),
                                            TeleportType.RING_TELE);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    player.getClickDelay().reset();

                    // JewelryTeleporting.teleport(player, new Position(3213, 3423));
                    break;
                case 195:
                    TeleportHandler.teleportPlayer(player, new Position(2918, 3176), TeleportType.RING_TELE);
                    player.getClickDelay().reset();
                    break;
                case 78:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (!player.getUnlockedLoyaltyTitles()[LoyaltyProgramme.LoyaltyTitles.MAXED.ordinal()]) {
                        player.getPacketSender()
                                .sendMessage("You must have unlocked the 'Maxed' Loyalty Title in order to buy this cape.");
                        return;
                    }
                    if (player.getInventory().getAmount(ItemDefinition.COIN_ID) < 50000000) {
                        player.getPacketSender().sendMessage("You do not have enough coins.");
                        return;
                    }
                    player.getInventory().delete(ItemDefinition.COIN_ID, 50000000);
                    player.getInventory().add(14019, 1);
                    player.getPacketSender().sendMessage("You've purchased a Max cape.");
                    break;
                case 60:
                case 202:
                    player.setDialogueActionId(62);
                    DialogueManager.start(player, 102);
                    break;

                case 68:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                        if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
                                .equals(player.getUsername())) {
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setComplexity(2);
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                    .sendMessage("The party leader has changed complexity.");
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
                        }
                    }
                    break;
                case 88:
                    ShopManager.getShops().get(80).open(player);
                    break;
            }
        } else if (id == THIRD_OPTION_OF_FIVE) {
            switch (player.getDialogueActionId()) {
                case 11050 -> {
                    if (player.getInventory().contains(23210, 400)) {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getInventory().delete(23210, 400);
                        player.getInventory().add(23129, 1);
                        player.sendMessage("@yel@[COMBINE] You have successfully created a Mystical Legs.");
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("You need 400 Event Crystals to combine this item.");
                    }
                }
                case 9924 -> {
                    player.setInputHandling(new WithdrawCurrencyFromCurrencyPouch(2));
                    player.getPacketSender().sendEnterAmountPrompt("How many Afk tickets would you like to withdraw?");
                }
                case 9925 -> {
                    player.setInputHandling(new WithdrawCurrencyFromCurrencyPouch(6));
                    player.getPacketSender().sendEnterAmountPrompt("How many Instance tokens would you like to withdraw?");
                }
                case 831 -> {
                    LotterySystem.claimReward(player);
                    player.getPacketSender().sendInterfaceRemoval();
                }
                case 150 -> {
                    TeleportHandler.teleportPlayer(player, CityTeleports.CAMELOT.getPos(), TeleportType.NORMAL);
                    player.getPacketSender().sendMessage(CityTeleports.CAMELOT.getHint());
                }
                case 151 -> {
                    TeleportHandler.teleportPlayer(player, CityTeleports.FALADOR.getPos(), TeleportType.NORMAL);
                    player.getPacketSender().sendMessage(CityTeleports.FALADOR.getHint());
                }
                case 153 ->
                    // Ancient Cavern here
                        TeleportHandler.teleportPlayer(player, new Position(1746, 5325, 0), TeleportType.NORMAL);
                case 152 -> {
                    TeleportHandler.teleportPlayer(player, CityTeleports.LUMBRIDGE.getPos(), TeleportType.NORMAL);
                    player.getPacketSender().sendMessage(CityTeleports.LUMBRIDGE.getHint());
                }
                case 0 -> TeleportHandler.teleportPlayer(player,
                        new Position(3204 + (Misc.getRandom(2)), 3263 + Misc.getRandom(2)),
                        player.getSpellbook().getTeleportType());
                case 1 -> TeleportHandler.teleportPlayer(player, new Position(3259, 3228),
                        player.getSpellbook().getTeleportType());
                case 2 -> TeleportHandler.teleportPlayer(player, new Position(2793, 2773),
                        player.getSpellbook().getTeleportType());
                case 9 -> DialogueManager.start(player, Lottery.Dialogues.getCurrentPot(player));
                case 10 -> DialogueManager.start(player, Mandrith.getDialogue(player));
                case 11 -> Scoreboards.open(player, Scoreboards.TOP_TOTAL_EXP);
                case 12 ->
                        TeleportHandler.teleportPlayer(player, new Position(3239 + Misc.getRandom(2), 3619 + Misc.getRandom(2)),
                                player.getSpellbook().getTeleportType());
                case 78 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    for (AchievementDataOLD d : AchievementDataOLD.values()) {
                        if (!player.getAchievementAttributes().getCompletion()[d.ordinal()]) {
                            player.getPacketSender()
                                    .sendMessage("You must have completed all achievements in order to buy this cape.");
                            return;
                        }
                    }
                    if (player.getInventory().getAmount(ItemDefinition.COIN_ID) < 100000000) {
                        player.getPacketSender().sendMessage("You do not have enough coins.");
                        return;
                    }
                    player.getInventory().delete(ItemDefinition.COIN_ID, 100000000);
                    player.getInventory().add(14022, 1);
                    player.getPacketSender().sendMessage("You've purchased a Completionist cape.");
                }
                case 14 -> TeleportHandler.teleportPlayer(player, new Position(2132, 5256),
                        player.getSpellbook().getTeleportType());
                case 15 ->
                        TeleportHandler.teleportPlayer(player, new Position(3368 + Misc.getRandom(5), 3267 + Misc.getRandom(3)),
                                player.getSpellbook().getTeleportType());
                case 29 -> SlayerMaster.changeSlayerMaster(player, SlayerMaster.HARD_SLAYER);
                case 36 -> {
                    player.setDialogueActionId(37);
                    DialogueManager.start(player, 70);
                }
                case 38 -> TeleportHandler.teleportPlayer(player, new Position(2547, 9448),
                        player.getSpellbook().getTeleportType());
                case 40 -> TeleportHandler.teleportPlayer(player, new Position(2368, 4949, 0),
                        player.getSpellbook().getTeleportType());
                case 48 -> {
                    for (int index = 0; index < JewelryTeleports.values().length; index++) {
                        if (index == JewelryTeleports.jewelIndex(player.getStrippedJewelryName())
                                && player.getClickDelay().elapsed(4500) && !player.getMovementQueue().isLockMovement()) {
                            for (int i = 0; i < JewelryTeleports.values()[index].getItemId().length; i++) {
                                if (player.getSelectedSkillingItem() == JewelryTeleports.values()[index].getItemIdIndex(i)
                                        && player.getSelectedSkillingItemTwo() == JewelryTeleports.values()[index]
                                        .getItemIdIndex(i + 1)) {
                                    player.getInventory().delete(player.getSelectedSkillingItem(), 1);
                                    player.getInventory().add(player.getSelectedSkillingItemTwo(), 1);

                                    TeleportHandler.teleportPlayer(player, new Position(3105, 3251),
                                            TeleportType.RING_TELE);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    player.getClickDelay().reset();
                }
                case 195 -> {
                    TeleportHandler.teleportPlayer(player, new Position(3105, 3251), TeleportType.RING_TELE);
                    player.getClickDelay().reset();
                }
                case 60, 202 -> {
                    player.setDialogueActionId(63);
                    DialogueManager.start(player, 102);
                }
                case 68 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                        if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
                                .equals(player.getUsername())) {
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setComplexity(3);
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty()
                                    .sendMessage("The party leader has changed complexity.");
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
                        }
                    }
                }
                case 88 -> ShopManager.getShops().get(25).open(player);
            }
        } else if (id == FOURTH_OPTION_OF_FIVE) {
            switch (player.getDialogueActionId()) {
                case 11050 -> {
                    if (player.getInventory().contains(23210, 300)) {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getInventory().delete(23210, 300);
                        player.getInventory().add(23130, 1);
                        player.sendMessage("@yel@[COMBINE] You have successfully created a Mystical Gloves.");
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("You need 300 Event Crystals to combine this item.");
                    }
                }
                case 9924 -> {
                    player.setInputHandling(new WithdrawCurrencyFromCurrencyPouch(3));
                    player.getPacketSender().sendEnterAmountPrompt("How many Slayer tickets would you like to withdraw?");
                }
                case 9925 -> {
                    player.setInputHandling(new WithdrawCurrencyFromCurrencyPouch(7));
                    player.getPacketSender().sendEnterAmountPrompt("How many Gold charms would you like to withdraw?");
                }
                case 831 -> {
                    player.sendMessage("@bla@There are currently @red@" + LotterySystem.getCurrentTicketAmount()
                            + " @bla@Lottery tickets- Winner pot is@red@: " + LotterySystem.getTotalPrizepool()
                            + "@bla@ Upgrade orbs");
                    player.getPacketSender().sendInterfaceRemoval();
                }
                case 150 -> {
                    TeleportHandler.teleportPlayer(player, CityTeleports.CANIFIS.getPos(), TeleportType.NORMAL);
                    player.getPacketSender().sendMessage(CityTeleports.CANIFIS.getHint());
                }
                case 151 -> {
                    TeleportHandler.teleportPlayer(player, CityTeleports.HOME.getPos(), TeleportType.NORMAL);
                    player.getPacketSender().sendMessage(CityTeleports.HOME.getHint());
                }
                case 153 -> TeleportHandler.teleportPlayer(player, new Position(3184, 5471),
                        player.getSpellbook().getTeleportType());

                // Chaos tunnels here
                case 152 -> {
                    TeleportHandler.teleportPlayer(player, CityTeleports.YANILLE.getPos(), TeleportType.NORMAL);
                    player.getPacketSender().sendMessage(CityTeleports.YANILLE.getHint());
                }
                case 0 -> TeleportHandler.teleportPlayer(player,
                        new Position(3173 - (Misc.getRandom(2)), 2981 + Misc.getRandom(2)),
                        player.getSpellbook().getTeleportType());
                case 1 -> TeleportHandler.teleportPlayer(player, new Position(3279, 2964),
                        player.getSpellbook().getTeleportType());
                case 2 -> TeleportHandler.teleportPlayer(player, new Position(3085, 9672),
                        player.getSpellbook().getTeleportType());
                case 9 -> DialogueManager.start(player, Lottery.Dialogues.getLastWinner(player));
                case 10 -> ShopManager.getShops().get(26).open(player);
                case 11 -> Scoreboards.open(player, Scoreboards.TOP_ACHIEVER);
                case 12 -> TeleportHandler.teleportPlayer(player,
                        new Position(3329 + Misc.getRandom(2), 3660 + Misc.getRandom(2), 0),
                        player.getSpellbook().getTeleportType());
                case 14 -> TeleportHandler.teleportPlayer(player, new Position(2360, 5214),
                        player.getSpellbook().getTeleportType());
                case 15 -> TeleportHandler.teleportPlayer(player, new Position(3565, 3313),
                        player.getSpellbook().getTeleportType());
                case 17 -> {
                    player.setInputHandling(new ChangePassword());
                    player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
                }
                case 29 -> player.sendMessage("To remove");
                case 36 -> TeleportHandler.teleportPlayer(player, new Position(2717, 9805),
                        player.getSpellbook().getTeleportType());
                case 38 -> TeleportHandler.teleportPlayer(player, new Position(1891, 3177),
                        player.getSpellbook().getTeleportType());
                case 40 -> TeleportHandler.teleportPlayer(player, new Position(3050, 9573),
                        player.getSpellbook().getTeleportType());
                case 48 -> {
                    for (int index = 0; index < JewelryTeleports.values().length; index++) {
                        if (index == JewelryTeleports.jewelIndex(player.getStrippedJewelryName())
                                && player.getClickDelay().elapsed(4500) && !player.getMovementQueue().isLockMovement()) {
                            for (int i = 0; i < JewelryTeleports.values()[index].getItemId().length; i++) {
                                if (player.getSelectedSkillingItem() == JewelryTeleports.values()[index].getItemIdIndex(i)
                                        && player.getSelectedSkillingItemTwo() == JewelryTeleports.values()[index]
                                        .getItemIdIndex(i + 1)) {
                                    player.getInventory().delete(player.getSelectedSkillingItem(), 1);
                                    player.getInventory().add(player.getSelectedSkillingItemTwo(), 1);

                                    TeleportHandler.teleportPlayer(player, new Position(3292, 3176),
                                            TeleportType.RING_TELE);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    player.getClickDelay().reset();
                }
                case 195 -> {
                    TeleportHandler.teleportPlayer(player, new Position(3292, 3176), TeleportType.RING_TELE);
                    player.getClickDelay().reset();
                }
                case 60, 202 -> {
                    player.setDialogueActionId(64);
                    DialogueManager.start(player, 102);
                }
                case 78 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    for (AchievementDataOLD d : AchievementDataOLD.values()) {
                        if (!((player.getSkillManager().getExperience(Skill.AGILITY) >= 200000000
                                && player.getSkillManager().getExperience(Skill.ATTACK) >= 200000000
                                && player.getSkillManager().getExperience(Skill.CONSTITUTION) >= 200000000
                                && player.getSkillManager().getExperience(Skill.COOKING) >= 200000000
                                && player.getSkillManager().getExperience(Skill.CRAFTING) >= 200000000
                                && player.getSkillManager().getExperience(Skill.DEFENCE) >= 200000000
                                && player.getSkillManager().getExperience(Skill.DUNGEONEERING) >= 200000000
                                && player.getSkillManager().getExperience(Skill.FARMING) >= 200000000
                                && player.getSkillManager().getExperience(Skill.FIREMAKING) >= 200000000
                                && player.getSkillManager().getExperience(Skill.FISHING) >= 200000000
                                && player.getSkillManager().getExperience(Skill.FLETCHING) >= 200000000
                                && player.getSkillManager().getExperience(Skill.HERBLORE) >= 200000000
                                && player.getSkillManager().getExperience(Skill.HUNTER) >= 200000000
                                && player.getSkillManager().getExperience(Skill.MAGIC) >= 200000000
                                && player.getSkillManager().getExperience(Skill.MINING) >= 200000000
                                && player.getSkillManager().getExperience(Skill.PRAYER) >= 200000000
                                && player.getSkillManager().getExperience(Skill.RANGED) >= 200000000
                                && player.getSkillManager().getExperience(Skill.RUNECRAFTING) >= 200000000
                                && player.getSkillManager().getExperience(Skill.SLAYER) >= 200000000
                                && player.getSkillManager().getExperience(Skill.SMITHING) >= 200000000
                                && player.getSkillManager().getExperience(Skill.STRENGTH) >= 200000000
                                && player.getSkillManager().getExperience(Skill.THIEVING) >= 200000000
                                && player.getSkillManager().getExperience(Skill.WOODCUTTING) >= 200000000))) {
                            player.getPacketSender().sendMessage(
                                    "You must have obtained at least 200m experience in all skills to purchase this cape.");
                            return;
                        }
                    }
                    if (player.getInventory().getAmount(ItemDefinition.COIN_ID) < 200000000) {
                        player.getPacketSender().sendMessage("You do not have enough coins.");
                        return;
                    }
                    player.getInventory().delete(ItemDefinition.COIN_ID, 200000000);
                    player.getInventory().add(20081, 1);
                    player.getPacketSender().sendMessage("You've purchased a 200m Cape.");
                }
                case 68 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
                        if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().getUsername()
                                .equals(player.getUsername())) {
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().setComplexity(4);
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().sendMessage(
                                    "The party leader has changed complexity to " + player.getMinigameAttributes()
                                            .getDungeoneeringAttributes().getParty().getComplexity() + ".");
                            player.getMinigameAttributes().getDungeoneeringAttributes().getParty().refreshInterface();
                        }
                    }
                }
                case 88 -> {
                    // ShopManager.getShops().get(24).open(player); //DONATOR SHOP 3 HERE
                    player.getDonationDeals().displayReward();
                    player.getDonationDeals().displayTime();
                    player.getPacketSender().sendString(57277, "@yel@$" + player.getAmountDonatedToday());
                }
            }
        } else if (id == FIFTH_OPTION_OF_FIVE) {
            switch (player.getDialogueActionId()) {
                case 11050, 9924, 9925 -> {
                    player.setDialogueActionId(player.getDialogueActionId() + 1);
                    DialogueManager.next(player);
                }
                case 150 -> {
                    player.setDialogueActionId(151);
                    DialogueManager.next(player);
                }
                case 151 -> {
                    player.setDialogueActionId(152);
                    DialogueManager.next(player);
                }
                case 153 -> {
                    player.setDialogueActionId(14);
                    DialogueManager.next(player);
                }
                case 152 -> {
                    player.setDialogueActionId(150);
                    DialogueManager.next(player);
                }
                case 0 -> {
                    player.setDialogueActionId(1);
                    DialogueManager.next(player);
                }
                case 1 -> {
                    player.setDialogueActionId(2);
                    DialogueManager.next(player);
                }
                case 2 -> {
                    player.setDialogueActionId(0);
                    DialogueManager.start(player, 0);
                }
                case 202 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.setBonecrushEffect(!player.getBonecrushEffect());
                    player.getPacketSender()
                            .sendMessage("<img=5> You have " + (player.getBonecrushEffect() ? "activated" : "disabled")
                                    + " your cape's Bonecrusher effect.");
                }
                case 9, 10, 11, 13, 213, 17, 78, 29, 48, 195, 60, 67, 68, 88, 101 ->
                        player.getPacketSender().sendInterfaceRemoval();
                case 12 -> TeleportHandler.teleportPlayer(player, new Position(3651, 3486, 0),
                        player.getSpellbook().getTeleportType());
                case 14 -> {
                    DialogueManager.next(player);
                    player.setDialogueActionId(153);
                }
                case 15 -> {
                    DialogueManager.start(player, 32);
                    player.setDialogueActionId(18);
                }
                case 36 -> {
                    DialogueManager.start(player, 66);
                    player.setDialogueActionId(38);
                }
                case 38 -> {
                    DialogueManager.start(player, 68);
                    player.setDialogueActionId(40);
                }
                case 40 -> {
                    DialogueManager.start(player, 69);
                    player.setDialogueActionId(41);
                }
                case 83 -> DialogueManager.start(player, 138);
            }
        } else if (id == FIRST_OPTION_OF_FOUR) {
            // System.out.println("Dialogue action id: " + player.getDialogueActionId());
            // System.out.println("Slayer master: " + player.getSlayer().getSlayerMaster().toString());
            // System.out.println("ID: " + id);
            switch (player.getDialogueActionId()) {
                case 11051 -> {
                    if (player.getInventory().contains(23210, 450)) {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getInventory().delete(23210, 450);
                        player.getInventory().add(23131, 1);
                        player.sendMessage("@yel@[COMBINE] You have successfully created a Mystical Boots.");
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("You need 450 Event Crystals to combine this item.");
                    }
                }
                case 9926 -> {
                    player.setInputHandling(new WithdrawCurrencyFromCurrencyPouch(8));
                    player.getPacketSender().sendEnterAmountPrompt("How many Green charms would you like to withdraw?");
                }
                case 8 -> ShopManager.getShops().get(110).open(player);
                case 9 -> TeleportHandler.teleportPlayer(player, new Position(3184, 3434),
                        player.getSpellbook().getTeleportType());
                case 14 -> TeleportHandler.teleportPlayer(player, new Position(2871, 5318, 2),
                        player.getSpellbook().getTeleportType());
                case 17 -> {
                    if (player.getBankPinAttributes().hasBankPin()) {
                        DialogueManager.start(player, 12);
                        player.setDialogueActionId(8);
                    } else {
                        BankPin.init(player, false);
                    }
                }
                case 18 ->
                        TeleportHandler.teleportPlayer(player, new Position(2439 + Misc.getRandom(2), 5171 + Misc.getRandom(2)),
                                player.getSpellbook().getTeleportType());
                case 26 -> TeleportHandler.teleportPlayer(player, new Position(2480, 3435),
                        player.getSpellbook().getTeleportType());
                case 28 -> {
                    player.setDialogueActionId(29);
                    DialogueManager.start(player, 62);
                }
                case 30 -> player.getSlayer().assignTask();

                // System.out.println("TAsk assigned: - Master: " + player.getSlayer().getSlayerMaster().toString());
                case 31 -> DialogueManager.start(player, SlayerDialogues.findAssignment(player));
                case 3011 -> DialogueManager.start(player, BossSlayerDialogues.findAssignment(player));
                case 41 -> DialogueManager.start(player, 76);
                case 79 -> {
                    DialogueManager.start(player, 128);
                    player.setDialogueActionId(80);
                }
                case 80 -> new TheSix(player).enter(true);
                case 83 -> TeleportHandler.teleportPlayer(player, new Position(3163, 3796),
                        player.getSpellbook().getTeleportType());
                case 84 -> TeleportHandler.teleportPlayer(player, new Position(3406, 2794, 0),
                        player.getSpellbook().getTeleportType());

                // TeleportHandler.teleportPlayer(player, new Position(3420, 2777,
                // (player.getIndex()+1)*4), player.getSpellbook().getTeleportType()); //zulrah
                // instance
                case 87 -> Construction.enterHouse(player, false);
            }
        } else if (id == SECOND_OPTION_OF_FOUR) {
            switch (player.getDialogueActionId()) {
                case 11051 -> {
                    if (player.getInventory().contains(23210, 600)) {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getInventory().delete(23210, 600);
                        player.getInventory().add(23133, 1);
                        player.sendMessage("@yel@[COMBINE] You have successfully created a Mystical Wings.");
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("You need 600 Event Crystals to combine this item.");
                    }
                }
                case 9926 -> {
                    player.setInputHandling(new WithdrawCurrencyFromCurrencyPouch(9));
                    player.getPacketSender().sendEnterAmountPrompt("How many Crimson charms would you like to withdraw?");
                }
                case 8 -> LoyaltyProgramme.open(player);
                case 9 -> DialogueManager.start(player, 14);
                case 30 -> {
                    if (player.getSlayer().getSlayerMaster().getNpcId() == 1597) {
                        ShopManager.getShops().get(47).open(player);
                    }
                    if (player.getSlayer().getSlayerMaster().getNpcId() == 9085) {
                        ShopManager.getShops().get(472).open(player);
                    }
                    if (player.getSlayer().getSlayerMaster().getNpcId() == 8275) {
                        ShopManager.getShops().get(471).open(player);
                    }
                    if (player.getSlayer().getSlayerMaster().getNpcId() == 9000) {
                        ShopManager.getShops().get(107).open(player);
                    }
                }
                case 14 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < 50) {
                        player.getPacketSender()
                                .sendMessage("You need a Slayer level of at least 50 to visit this dungeon.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(2731, 5095),
                            player.getSpellbook().getTeleportType());
                }
                case 17 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getBankPinAttributes().hasBankPin()) {
                        player.getPacketSender()
                                .sendMessage("Please visit the nearest bank and enter your pin before doing this.");
                        return;
                    }
                    if (player.getSummoning().getFamiliar() != null) {
                        player.getPacketSender().sendMessage("Please dismiss your familiar first.");
                        return;
                    }
                    if (player.getMode() instanceof Normal) {
                        DialogueManager.start(player, 83);
                    } else {
                        player.setDialogueActionId(46);
                        DialogueManager.start(player, 84);
                    }
                }
                case 18 -> TeleportHandler.teleportPlayer(player, new Position(2399, 5177),
                        player.getSpellbook().getTeleportType());
                case 26 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSkillManager().getMaxLevel(Skill.AGILITY) < 35) {
                        player.getPacketSender()
                                .sendMessage("You need an Agility level of at least level 35 to use this course.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(2552, 3556),
                            player.getSpellbook().getTeleportType());
                }
                case 28 -> {
                    if (player.getSlayer().getSlayerMaster().getPosition() != null) {
                        TeleportHandler.teleportPlayer(player,
                                new Position(player.getSlayer().getSlayerMaster().getPosition().getX(),
                                        player.getSlayer().getSlayerMaster().getPosition().getY(),
                                        player.getSlayer().getSlayerMaster().getPosition().getZ()),
                                player.getSpellbook().getTeleportType());
                        if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) <= 50)
                            player.getPacketSender().sendMessage("")
                                    .sendMessage("You can train Slayer with a friend by using a Slayer gem on them.")
                                    .sendMessage("Slayer gems can be bought from all Slayer masters.");
                    }
                }
                case 31 -> DialogueManager.start(player, SlayerDialogues.resetTaskDialogue(player));
                case 41 -> WellOfGoodwill.lookDownWell(player);
                case 79 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    Barrows.resetBarrows(player);
                    VoidOfDarkness.resetBarrows(player);
                    DialogueManager.start(player, 133);
                }
                case 80 -> new TheSix(player).enter(false);
                case 83 -> TeleportHandler.teleportPlayer(player, new Position(3009, 3767),
                        player.getSpellbook().getTeleportType());
                case 84 -> TeleportHandler.teleportPlayer(player, new Position(3683, 9888, (player.getIndex() + 1) * 4),
                        player.getSpellbook().getTeleportType()); // kraken instance
                case 87 -> Construction.enterHouse(player, true);
            }
        } else if (id == THIRD_OPTION_OF_FOUR) {
            switch (player.getDialogueActionId()) {
                case 11051 -> {
                    if (player.getInventory().contains(23210, 750)) {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getInventory().delete(23210, 750);
                        player.getInventory().add(23132, 1);
                        player.sendMessage("@yel@[COMBINE] You have successfully created a Mystical Scythe.");
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("You need 750 Event Crystals to combine this item.");
                    }
                }
                case 9926 -> {
                    player.setInputHandling(new WithdrawCurrencyFromCurrencyPouch(10));
                    player.getPacketSender().sendEnterAmountPrompt("How many Blue charms would you like to withdraw?");
                }
                case 8 -> {
                    LoyaltyProgramme.reset(player);
                    player.getPacketSender().sendInterfaceRemoval();
                }
                case 9 -> ShopManager.getShops().get(41).open(player);
                case 14 -> TeleportHandler.teleportPlayer(player, new Position(1745, 5325),
                        player.getSpellbook().getTeleportType());
                case 17 -> {
                    player.setInputHandling(new ChangePassword());
                    player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
                }
                case 18 -> TeleportHandler.teleportPlayer(player, new Position(3503, 3562),
                        player.getSpellbook().getTeleportType());
                case 26 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSkillManager().getMaxLevel(Skill.AGILITY) < 55) {
                        player.getPacketSender()
                                .sendMessage("You need an Agility level of at least level 55 to use this course.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(2998, 3914),
                            player.getSpellbook().getTeleportType());
                }
                case 28 -> TeleportHandler.teleportPlayer(player, new Position(3427, 3537, 0),
                        player.getSpellbook().getTeleportType());
                case 30 -> ShopManager.getShops().get(40).open(player);
                case 31 -> DialogueManager.start(player, SlayerDialogues.totalPointsReceived(player));
                case 41 -> {
                    player.setInputHandling(new DonateToWell());
                    player.getPacketSender().sendInterfaceRemoval()
                            .sendEnterAmountPrompt("How much money would you like to contribute with?");
                }
                /*case 65:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSlayer().getDuoPartner() != null) { // slayer
                        Player partner = World.getPlayerByName(player.getSlayer().getDuoPartner());
                        boolean inPos = (player.getLocation() == Location.HOME_BANK
                                || player.getLocation() == Location.NEW_MEMBER_ZONE);
                        if (!inPos || player.busy() || player.getCombatBuilder().isBeingAttacked()) {
                            player.getPacketSender().sendMessage(
                                    "You may only teleport to your target from the Home bank, or Member Zone.");
                            break;
                        }
                        if (partner.busy() || partner.getLocation() == Location.WILDERNESS
                                || partner.getLocation() == Location.DUNGEONEERING
                                || partner.getLocation() == Location.DUEL_ARENA || partner.getLocation() == Location.JAIL
                                || partner.getLocation() == Location.BARROWS || partner.getLocation() == Location.KRAKEN
                                || partner.getLocation() == Location.NEW_MEMBER_ZONE
                                || partner.getLocation() == Location.MEMBER_ZONE
                                || partner.getLocation() == Location.FREE_FOR_ALL_ARENA
                                || partner.getLocation() == Location.FREE_FOR_ALL_WAIT
                                || partner.getLocation() == Location.GODWARS_DUNGEON
                                || partner.getLocation() == Location.PEST_CONTROL_GAME
                                || partner.getLocation() == Location.TRIO_ZONE || partner.getLocation() == Location.THE_SIX
                                || partner.getLocation() == Location.WARRIORS_GUILD
                                || partner.getLocation() == Location.ZULRAH) {
                            player.getPacketSender().sendMessage(
                                    "Your partner cannot be teleported to at the moment. Are they in combat/minigame/wild?");
                            break;
                        } else {
                            TeleportHandler.teleportPlayer(player, new Position(partner.getPosition().getX(),
                                    partner.getPosition().getY(), partner.getPosition().getZ()), TeleportType.NORMAL);
                            player.getPacketSender().sendMessage("Teleporting you to " + partner.getUsername() + "!");
                        }
                    } else {
                        player.getPacketSender().sendMessage("You need to be in a team with a partner first!");
                    }
                    break;*/
                case 79 -> {
                    player.getPacketSender().sendMessage("<shad=336600>You currently have "
                            + player.getPointsHandler().getBarrowsPoints() + " Barrows points.");
                    ShopManager.getShops().get(79).open(player);
                }
                case 80 -> DialogueManager.start(player, 129);
                case 83 -> TeleportHandler.teleportPlayer(player, new Position(3005, 3732),
                        player.getSpellbook().getTeleportType());
                case 84 -> TeleportHandler.teleportPlayer(player, new Position(2849, 9640),
                        player.getSpellbook().getTeleportType());
                case 87 -> {
                    player.setInputHandling(new EnterFriendsHouse());
                    player.getPacketSender().sendEnterInputPrompt("Enter a friend's username:");
                }
            }
        } else if (id == FOURTH_OPTION_OF_FOUR) {
            switch (player.getDialogueActionId()) {
                case 11051, 8, 9, 17, 26, 27, 28, 41, 79, 80, 84, 87, 9926 ->
                        player.getPacketSender().sendInterfaceRemoval();
                case 14 -> {
                    player.setDialogueActionId(14);
                    DialogueManager.start(player, 22);
                }
                case 18 -> {
                    DialogueManager.start(player, 25);
                    player.setDialogueActionId(15);
                }
                case 30, 31 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSlayer().getDuoPartner() != null) {
                        Slayer.resetDuo(player, World.getPlayerByName(player.getSlayer().getDuoPartner()));
                    }
                }
                case 45 -> player.getPacketSender().sendString(1, GameSettings.IronManModesUrl);
                case 83 -> {
                    player.setDialogueActionId(84);
                    DialogueManager.start(player, 138);
                }
            }
        } else if (id == FIRST_OPTION_OF_TWO) {
            switch (player.getDialogueActionId()) {
                case 753 -> {
                    if (player.getDialogue() instanceof CancelOptions cancelOptions) {
                        player.getTradingPost().cancelSlot(cancelOptions.getSlot());
                    }
                }
                case 754 -> {
                    if (player.getDialogue() instanceof PurchaseOptions purchaseOptions) {
                        player.getTradingPost().purchase(purchaseOptions.getOffer(), purchaseOptions.getAmount(), 1);
                    }
                }
                //Security
                case 11003 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    DialogueManager.start(player, 11005);
                    player.setDialogueActionId(11006);
                }
                case 11006 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.sendMessage("2FA info is not implemented. Coming soon.");
                }
                case 11008 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getPSecurity().begin2FA();
                }
                case 6666 -> {//RAIDS invite
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getInviteParty() == null) {
                        return;
                    }
                    player.getInviteParty().add(player);
                    player.setInviteParty(null);
                }
                case 8005 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (!player.getSkillManager().maxed()) {
                        DialogueManager.sendStatement(player, "You must be maxed in all skills to do this.");
                        return;
                    }
                    /*if (!player.getAchievements().hasCompletedAll()){
                                              DialogueManager.sendStatement(player, "You must complete all achievements to do this.");
                        return;
                    }*/
                    if (player.getInventory().contains(ItemDefinition.COIN_ID, 120_000_000)) {
                        player.getInventory().delete(ItemDefinition.COIN_ID, 120_000_000);
                    } else if (player.getInventory().contains(ItemDefinition.TOKEN_ID, 120000)) {
                        player.getInventory().delete(ItemDefinition.TOKEN_ID, 120000);
                    } else {
                        DialogueManager.sendStatement(player, "You need 120m coins to do this.");
                        return;
                    }
                    player.getInventory().add(14019, 1);
                    player.getPacketSender().sendMessage("You've purchased a Max cape.");
                }
                case 568 -> ShopManager.getShops().get(207).open(player);
                case 9928 -> {
                    if (player.getSlayer().getAmountToSlay() > 0) {
                        if (player.getInventory().contains(ItemDefinition.COIN_ID, 250000)) {
                            Position pos = player.getSlayer().getSlayerTask().getTaskPosition();
                            TeleportHandler.teleportPlayer(player, pos, TeleportType.TELE_TAB);
                            player.getInventory().delete(ItemDefinition.COIN_ID, 250000);
                        } else {
                            DialogueManager.sendStatement(player, "You need 250,000 coins to do this.");
                        }
                    } else {
                        DialogueManager.sendStatement(player, "You do not currently have a task.");
                    }
                }
                case 668 -> {//yes
                    if (player.getSlayer().getSlayerTask() == null) {
                        player.sendMessage("You do not have a slayer task!");
                        return;
                    }
                    if (player.getInventory().getAmount(ItemDefinition.COIN_ID) >= 250_000 && player.getInventory().contains(ItemDefinition.COIN_ID)) {
                        player.getInventory().delete(ItemDefinition.COIN_ID, 250_000);
                        TeleportHandler.teleportPlayer(player, player.getSlayer().getSlayerTask().getTaskPosition(), player.getSpellbook().getTeleportType());
                    } else {
                        player.sendMessage("You do not have enough coins!");
                    }
                    player.getPacketSender().sendInterfaceRemoval();
                }
                case 666 -> VaultOfWar.forgeGloves(player);
                case 103 -> MemberScrolls.claimBond(player);
                case 8002 -> player.getIronmanGroup().getBank().open(player);
                case 8001 -> GroupManager.createGroup(player);
                case 670 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getGroupInvitationId() != -1 && GroupManager.getGroup(player.getGroupInvitationId()) != null) {
                        GroupManager.getGroup(player.getGroupInvitationId()).addPlayer(player);
                    }
                    if (GroupManager.isInGroup(player)) {
                        GroupManager.openInterface(player);
                    }
                    if (player.getIronmanGroup().getOwner() != null) {
                        player.getIronmanGroup().getOwner().sendMessage("@blu@" + player.getUsername() + " has joined your Ironman group!");
                        GroupManager.openInterface(player.getIronmanGroup().getOwner());
                    }
                    player.setGroupInvitationId(-1);
                }
               /* case 6969:
                    if (player.getInventory().contains(19000)) {
                        int amount = player.getInventory().getAmount(19000);
                        player.getInventory().delete(19000, amount);
                        player.getInventory().add(ItemDefinition.MILL_ID, amount * 100);
                        player.sendMessage("You exchanged x" + amount + " Pet fragements for " + (amount * 100) + " Solak tokens.");
                    }
                    player.getPacketSender().sendInterfaceRemoval();
                    break;*/
                case 9923 -> PetUpgrading.upgrade(player);
                case 67 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getLocation() == Location.ZOMBIE_LOBBY
                            && player.getZombieParty() == null) {
                        if (player.getMinigameAttributes().getZombieAttributes().getPartyInvitation() != null) {
                            player.getMinigameAttributes().getZombieAttributes().getPartyInvitation().add(player);
                        }
                        player.getMinigameAttributes().getZombieAttributes().setPartyInvitation(null);
                    }
//                    if (player.getLocation() == Location.TEST_RAID_LOBBY
//                            && player.getRaidParty() == null) {
//                        if (player.getMinigameAttributes().getZombieAttributes().getPartyInvitation() != null) {
//                            player.getMinigameAttributes().getZombieAttributes().getPartyInvitation().add(player);
//                        }
//                        player.getMinigameAttributes().getZombieAttributes().setPartyInvitation(null);
//                    }
                    else if (player.getLocation() == Location.AURA_LOBBY
                            && player.getAuraParty() == null) {
                        if (player.getMinigameAttributes().getAuraAttributes().getPartyInvitation() != null) {
                            player.getMinigameAttributes().getAuraAttributes().getPartyInvitation().add(player);
                        }
                        player.getMinigameAttributes().getAuraAttributes().setPartyInvitation(null);
                    }
                }
                case 71260 -> {
                    player.getZombieParty().remove(player, true);
                    player.getPacketSender().sendInterfaceRemoval();
                    player.moveTo(ZombieRaidData.lobbyPosition);
                    player.setEnteredZombieRaids(false);
                }
                case 2012 -> ZombieRaids.start(player.getZombieParty());
                case 523 -> {
                    player.getPacketSender().sendInterfaceRemoval().sendMessage("You clear your inventory.");
                    player.getSkillManager().stopSkilling();
                    for (int i = 0; i < player.getInventory().capacity(); i++) {
                        if (player.getInventory().get(i) != null && player.getInventory().get(i).getId() > 0) {
                            if (ItemDefinition.forId(player.getInventory().get(i).getId()) == null || ItemDefinition.forId(player.getInventory().get(i).getId()).getName() == null) {
                                PlayerLogs.log(player.getUsername(), "Emptied item (id:" + player.getInventory().get(i).getId()
                                        + ", amount:" + player.getInventory().get(i).getAmount() + ")");
                            } else {
                                PlayerLogs.log(player.getUsername(),
                                        "Emptied item (id:" + player.getInventory().get(i).getId() + ", amount:"
                                                + player.getInventory().get(i).getAmount() + ") -- "
                                                + ItemDefinition.forId(player.getInventory().get(i).getId()).getName());
                            }
                        }

                        player.getInventory().resetItems().refreshItems();
                    }
                }
                case 920 -> {
                    int random = RandomUtility.inclusiveRandom(1, 100);
                    player.getGambling().bjScore += random;
                    player.forceChat("I roll a " + random + " and my score is now: " + player.getGambling().bjScore);
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getGambling().bjScore < 100) {
                        TaskManager.submit(new Task(3) {
                            @Override
                            protected void execute() {
                                player.setDialogueActionId(920);
                                DialogueManager.start(player, 216);
                                stop();
                                return;
                            }
                        });
                    } else if (player.getGambling().bjTurn == 1) {
                        player.getGambling().setHostTurn();
                    } else {
                        player.getGambling().getBlackjackWinner();
                        // System.out.println("Blackjack has ended ->");
                    }
                    player.performAnimation(new Animation(11900));
                    player.performGraphic(new Graphic(2075));
                }
                case 215 -> new WellForGlobalBossesInterface(player).open();
                case 211 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getEaster2017() == 7) {
                        player.getInventory().add(4565, 1);
                        player.getInventory().add(1037, 1);
                        player.setEaster2017(8);
                        World.sendMessage("<img=5> " + player.getUsername() + " has just completed the Easter event!");
                    }
                }
                case 100000 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    TeleportHandler.teleportPlayer(player, new Position(2665, 4020), TeleportType.NORMAL);
                }
                case 210 -> {
                    if (player.getInventory().contains(22051)) {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("I already have a letter, and should read it.");
                        return;
                    }
                    if (player.getInventory().isFull()) {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getPacketSender().sendMessage("You need at least 1 free inventory slot.");
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getInventory().add(22051, 1);
                        player.getPacketSender()
                                .sendMessage("<img=5> Take a look at the Bunny's letter for more instructions!");
                        player.setEaster2017(1);
                    }
                }
                case 198 -> {
                    for (int index = 0; index < JewelryTeleports.values().length; index++) {
                        if (index == JewelryTeleports.jewelIndex(player.getStrippedJewelryName())
                                && player.getClickDelay().elapsed(4500) && !player.getMovementQueue().isLockMovement()) {
                            for (int i = 0; i < JewelryTeleports.values()[index].getItemId().length; i++) {
                                if (player.getSelectedSkillingItem() == JewelryTeleports.values()[index].getItemIdIndex(i)
                                        && player.getSelectedSkillingItemTwo() == JewelryTeleports.values()[index]
                                        .getItemIdIndex(i + 1)) {
                                    player.getInventory().delete(player.getSelectedSkillingItem(), 1);
                                    player.getInventory().add(player.getSelectedSkillingItemTwo(), 1);

                                    TeleportHandler.teleportPlayer(player, new Position(3351, 3346),
                                            TeleportType.RING_TELE);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    player.getClickDelay().reset();
                }
                case 189 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getPacketSender().sendMessage("Party Pete hands you a gift...");
                    if (player.getInventory().isFull()) {
                        player.getPacketSender().sendMessage("Have at least 1 free slot before accepting his gift.");
                    } else {
                        player.setNewYear2017(1);
                        player.getInventory().add(2946, 1);
                        World.sendMessage("<img=5> <shad=0>@whi@" + player.getUsername()
                                + " has completed the New Year 2017 mini-event.");
                    }
                }
                case 187 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getPacketSender().sendMessage("Santa hands you a Present...");
                    if (player.getInventory().isFull()) {
                        player.getPacketSender().sendMessage("Make room in your inventory before accepting his gift!");
                    } else {
                        player.setchristmas2016(7);
                        player.getInventory().add(15420, 1);
                        World.sendMessage("<img=5> <shad=0>@red@" + player.getUsername()
                                + " has completed the @gre@Christmas 2016 event!");
                    }
                }
                case 505050 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getPacketSender().sendMessage("Jack wants me to get the following...");
                    player.getPacketSender().sendMessage("@red@<shad=0>Item list:");
                    player.getPacketSender()
                            .sendMessage("@gre@<shad=0>100 Law, 100 Cosmic, 100 Nature runes, 1 Wizard Mind Bomb.");
                    player.getPacketSender()
                            .sendMessage("@whi@<shad=0>Runes can be purchased. Mind Bomb is sold on the docks, South.");
                }
                case 180 -> {
                    player.setchristmas2016(2);
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getPacketSender().sendMessage("@red@<shad=0>Item list:");
                    player.getPacketSender()
                            .sendMessage("@gre@<shad=0>100 Law, 100 Cosmic, 100 Nature runes, 1 Wizard Mind Bomb.");
                }
                case 178 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getPacketSender()
                            .sendMessage("Unfortunately, ship charters are still being established. Check back soon.");
                }
                case 173 -> {
                    DialogueManager.start(player, 174);
                    player.setDialogueActionId(180);
                    return;
                }
                case 171 -> {
                    player.setchristmas2016(1);
                    player.getPacketSender()
                            .sendMessage("@red@<shad=0>It might be time to speak with Explorer Jack at Home.");
                    player.getPacketSender().sendInterfaceRemoval();
                }
                case 3 -> ShopManager.getShops().get(22).open(player);
                case 4 -> SummoningTab.handleDismiss(player, true);
                case 7 -> BankPin.init(player, false);
                case 8 -> BankPin.deletePin(player);
                case 16 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() < 5) {
                        player.getPacketSender()
                                .sendMessage("You must have a killcount of at least 5 to enter the tunnel.");
                        return;
                    }
                    player.moveTo(new Position(3552, 9692));
                }
                case 20 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    DialogueManager.start(player, 39);
                    player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(0, true);
                    PlayerPanel.refreshPanel(player);
                }
                case 23 -> {
                    DialogueManager.start(player, 50);
                    player.getMinigameAttributes().getNomadAttributes().setPartFinished(0, true);
                    player.setDialogueActionId(24);
                    PlayerPanel.refreshPanel(player);
                }
                case 24 -> player.getPacketSender().sendInterfaceRemoval();
                case 33 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getSlayer().resetSlayerTask();
                }
                case 34 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getSlayer().handleInvitation(true);
                }
                case 37 -> TeleportHandler.teleportPlayer(player, new Position(2961, 3882),
                        player.getSpellbook().getTeleportType());
                case 39 -> TeleportHandler.teleportPlayer(player, new Position(3281, 3914),
                        player.getSpellbook().getTeleportType());
                case 42 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getInteractingObject() != null && player.getInteractingObject().getDefinition() != null
                            && player.getInteractingObject().getDefinition().getName().equalsIgnoreCase("flowers")) {
                        if (CustomObjects.objectExists(player.getInteractingObject().getPosition())) {
                            player.getInventory().add(FlowersData.forObject(player.getInteractingObject().getId()).itemId,
                                    1);
                            CustomObjects.deleteGlobalObject(player.getInteractingObject());
                            player.setInteractingObject(null);
                        }
                    }
                }
                case 44 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(true);
                    player.moveTo(new Position(2911, 5203));
                    player.getPacketSender().sendMessage("You enter Nex's lair..");
                    NpcAggression.target(player);
                }
                case 46 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    DialogueManager.start(player, 82);
                    player.setPlayerLocked(true).setDialogueActionId(45);
                }
                case 57 -> Graveyard.start(player);
                case 66 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getLocation() == Location.DUNGEONEERING
                            && player.getMinigameAttributes().getDungeoneeringAttributes().getParty() == null) {
                        if (player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation() != null) {
                            player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation().add(player);
                            player.getPacketSender().sendMessage("You've joined the party!");
                        }
                    }
                    player.getMinigameAttributes().getDungeoneeringAttributes().setPartyInvitation(null);
                }
                case 71 -> {
                    if (player.getClickDelay().elapsed(1000)) {
                        if (Dungeoneering.doingOldDungeoneering(player)) {
                            Dungeoneering.leave(player, false, true);
                            player.getClickDelay().reset();
                        }
                    }
                }
                case 72 -> {
                    if (player.getClickDelay().elapsed(1000)) {
                        if (Dungeoneering.doingOldDungeoneering(player)) {
                            Dungeoneering.leave(player, false, player.getMinigameAttributes().getDungeoneeringAttributes()
                                    .getParty().getOwner() != player);
                            player.getClickDelay().reset();
                        }
                    }
                }
                case 73 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.moveTo(new Position(3653, player.getPosition().getY()));
                }
                case 74 -> {
                    player.getPacketSender().sendMessage("The ghost teleports you away.");
                    player.getPacketSender().sendInterfaceRemoval();
                    player.moveTo(new Position(3651, 3486));
                }
                case 76 -> {
                    player.getPacketSender().sendInterfaceRemoval();
//                    if (player.getRights().isStaff()) {
//                        player.getPacketSender().sendMessage("You cannot change your rank.");
//                        return;
//                    }
//                    player.setRights(PlayerRights.HELPER);
                    player.getPacketSender().sendRights();
                }
                case 81 -> new TheSix(player).joinClan();
                case 102 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getInventory().getAmount(11180) < 1) {
                        player.getPacketSender().sendMessage("You do not have enough tokens.");
                        return;
                    } else
                        player.getInventory().delete(11180, 1);
                    // So we grab the players pID too determine what Z they will be getting. Not
                    // sure how kraken handles it, but this is how we'll handle it.
                    player.moveTo(new Position(3025, 5231));
                    // player.getPacketSender().sendMessage("Index: " + player.getIndex());
                    // player.getPacketSender().sendMessage("Z: " + player.getIndex() * 4);
                    player.getPacketSender().sendMessage("Teleporting to Trio...");
                    player.getPacketSender()
                            .sendMessage("@red@Warning:@bla@ you @red@will@bla@ lose your items on death here!");
                    // Will sumbit a task to handle token remove, once they leave the minigame the
                    // task will be removed.
                    trioMinigame.handleTokenRemoval(player);
                }
                case 154 -> {
                    player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY(), 2));
                    player.getPacketSender().sendInterfaceRemoval();
                }
            }

        } else if (id == SECOND_OPTION_OF_TWO) {
            switch (player.getDialogueActionId()) {//no
                case 11003, 11008, 668, 8005, 568, 9928, 666, 901 -> player.getPacketSender().sendInterfaceRemoval();
                case 754 -> {
                    if (player.getDialogue() instanceof PurchaseOptions purchaseOptions) {
                        player.getTradingPost().purchase(purchaseOptions.getOffer(), purchaseOptions.getAmount(), 2);
                    }
                }
                case 753 -> player.getTradingPost().openMainInterface();
                case 11006 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    DialogueManager.start(player, 11007);
                    player.setDialogueActionId(11008);
                }
                case 8002 -> player.getBank(player.getCurrentBankTab()).open();
                case 67 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getLocation() == Location.ZOMBIE_LOBBY
                            && player.getMinigameAttributes().getZombieAttributes().getPartyInvitation() != null
                            && player.getMinigameAttributes().getZombieAttributes().getPartyInvitation()
                            .getOwner() != null) {
                        player.getMinigameAttributes().getZombieAttributes().getPartyInvitation().getOwner()
                                .getPacketSender()
                                .sendMessage("" + player.getUsername() + " has declined your invitation.");
                        player.getMinigameAttributes().getZombieAttributes().setPartyInvitation(null);
                    }
                }
                case 8001, 6969, 9923, 71260, 2012, 522, 523, 103, 524 ->
                        player.getPacketSender().sendInterfaceRemoval();
                case 920 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getGambling().bjTurn == 1) {
                        player.getGambling().setHostTurn();
                    } else {
                        player.getGambling().getBlackjackWinner();
                        // System.out.println("Declaring winner");
                    }
                }
                case 215 -> {
                    player.setDialogueActionId(41);
                    player.setInputHandling(new DonateToWell());
                    player.getPacketSender().sendInterfaceRemoval()
                            .sendEnterAmountPrompt("How much money would you like to contribute with?");
                }
                case 178 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    ShopManager.getShops().get(85).open(player);
                }
                case 505050, 173 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    ShopManager.getShops().get(28).open(player);
                }
                case 100000 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    KillsTracker.open(player);
                }
                case 171 -> {
                    // player.getPacketSender().sendMessage("denied santa");
                    player.getPacketSender().sendInterfaceRemoval();
                    return;
                }
                case 3 -> ShopManager.getShops().get(23).open(player);
                case 211, 210, 198, 189, 187, 180, 4, 16, 20, 23, 33, 37, 39, 42, 44, 46, 57, 71, 72, 73, 74, 76, 78, 81, 102, 7, 6666 ->
                        player.getPacketSender().sendInterfaceRemoval();
                case 8 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getBank(player.getCurrentBankTab()).open();
                }
                case 24 -> Nomad.startFight(player);
                case 34 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getSlayer().handleInvitation(false);
                }
                case 66 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation() != null && player
                            .getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation().getOwner() != null)
                        player.getMinigameAttributes().getDungeoneeringAttributes().getPartyInvitation().getOwner()
                                .getPacketSender()
                                .sendMessage(player.getUsername() + " has declined your invitation.");
                    player.getMinigameAttributes().getDungeoneeringAttributes().setPartyInvitation(null);
                }
                case 154 -> {
                    player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY(), 0));
                    player.getPacketSender().sendInterfaceRemoval();
                }
            }
        } else if (id == FIRST_OPTION_OF_THREE) {
            switch (player.getDialogueActionId()) {
                case 9905 -> {
                    player.skillingTask = true;
                    DailyTasks.checkTask(player);
                }
                case 65 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSlayer().getDuoPartner() != null) {
                        player.getPacketSender().sendMessage("You already have a duo partner.");
                        return;
                    }
                    player.getPacketSender()
                            .sendMessage("<img=5> To do Social slayer, simply use your Slayer gem on another player.");
                }
                case 30 -> player.getSlayer().assignTask();

                // System.out.println("TAsk assigned: - Master: " + player.getSlayer().getSlayerMaster().toString());
                case 196 -> {
                    for (int index = 0; index < JewelryTeleports.values().length; index++) {
                        if (index == JewelryTeleports.jewelIndex(player.getStrippedJewelryName())
                                && player.getClickDelay().elapsed(4500) && !player.getMovementQueue().isLockMovement()) {
                            for (int i = 0; i < JewelryTeleports.values()[index].getItemId().length; i++) {
                                if (player.getSelectedSkillingItem() == JewelryTeleports.values()[index].getItemIdIndex(i)
                                        && player.getSelectedSkillingItemTwo() == JewelryTeleports.values()[index]
                                        .getItemIdIndex(i + 1)) {
                                    player.getInventory().delete(player.getSelectedSkillingItem(), 1);
                                    player.getInventory().add(player.getSelectedSkillingItemTwo(), 1);

                                    TeleportHandler.teleportPlayer(player, new Position(3370, 3269),
                                            TeleportType.RING_TELE);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    player.getClickDelay().reset();
                }
                case 197 -> {
                    for (int index = 0; index < JewelryTeleports.values().length; index++) {
                        if (index == JewelryTeleports.jewelIndex(player.getStrippedJewelryName())
                                && player.getClickDelay().elapsed(4500) && !player.getMovementQueue().isLockMovement()) {
                            for (int i = 0; i < JewelryTeleports.values()[index].getItemId().length; i++) {
                                if (player.getSelectedSkillingItem() == JewelryTeleports.values()[index].getItemIdIndex(i)
                                        && player.getSelectedSkillingItemTwo() == JewelryTeleports.values()[index]
                                        .getItemIdIndex(i + 1)) {
                                    player.getInventory().delete(player.getSelectedSkillingItem(), 1);
                                    player.getInventory().add(player.getSelectedSkillingItemTwo(), 1);

                                    TeleportHandler.teleportPlayer(player, new Position(2898, 3562),
                                            TeleportType.RING_TELE);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    player.getClickDelay().reset();
                }
                case 214 -> TeleportHandler.teleportPlayer(player, new Position(3037, 9545),
                        player.getSpellbook().getTeleportType());
                case 15 -> {
                    DialogueManager.start(player, 35);
                    player.setDialogueActionId(19);
                }
                case 19 -> {
                    DialogueManager.start(player, 33);
                    player.setDialogueActionId(21);
                }
                case 21 -> TeleportHandler.teleportPlayer(player, new Position(3080, 3498),
                        player.getSpellbook().getTeleportType());
                case 22 -> TeleportHandler.teleportPlayer(player, new Position(1891, 3177),
                        player.getSpellbook().getTeleportType());
                case 25 -> TeleportHandler.teleportPlayer(player, new Position(2589, 4319), TeleportType.PURO_PURO);
                case 35 -> {
                    player.getPacketSender()
                            .sendEnterAmountPrompt("How many shards would you like to buy? (You can use K, M, B prefixes)");
                    player.setInputHandling(new BuyShards());
                }
                case 36 -> {
                    player.setDialogueActionId(83);
                    DialogueManager.start(player, 137);
                }
                case 41 -> TeleportHandler.teleportPlayer(player, GameSettings.CORP_CORDS,
                        player.getSpellbook().getTeleportType());
                case 47 -> TeleportHandler.teleportPlayer(player, new Position(2911, 4832),
                        player.getSpellbook().getTeleportType());
                case 48 -> {
                    if (player.getInteractingObject() != null) {
                        Mining.startMining(player, new GameObject(24444, player.getInteractingObject().getPosition()));
                    }
                    player.getPacketSender().sendInterfaceRemoval();
                }
                case 56 -> TeleportHandler.teleportPlayer(player, new Position(2561, 3867),
                        player.getSpellbook().getTeleportType());
                case 58 -> DialogueManager.start(player, AgilityTicketExchange.getDialogue(player));
                case 61 -> CharmingImp.changeConfig(player, 0, 0);
                case 62 -> CharmingImp.changeConfig(player, 1, 0);
                case 63 -> CharmingImp.changeConfig(player, 2, 0);
                case 64 -> CharmingImp.changeConfig(player, 3, 0);
                case 69 -> {
                    ShopManager.getShops().get(44).open(player);
                    player.getPacketSender().sendMessage("<img=5> <col=660000>You currently have "
                            + player.getPointsHandler().getDungeoneeringTokens() + " Dungeoneering tokens.");
                }
                case 70, 71 -> {
                    if (player.getInventory().contains(23020) && player.getClickDelay().elapsed(700)) {
                        final int amt = player.getDialogueActionId() == 70 ? 1 : player.getInventory().getAmount(23020);
                        player.getPacketSender().sendInterfaceRemoval();
                        player.getInventory().delete(23020, amt);
                        player.getPacketSender().sendMessage(
                                "You claim the " + (amt > 1 ? "scrolls" : "scroll") + " and receive your reward.");
                        int minutes = player.getMode() instanceof Normal ? 10 : 5;
                        if (player.getMinutesBonusExp() < 1) {
                            player.getBonusXp().init();
                        }
                        BonusExperienceTask.addBonusXp(player, minutes * amt);
                        player.getPacketSender().sendString(48402, "" + player.getMinutesBonusExp() + " minutes");
                        player.getClickDelay().reset();
                    }
                }
                case 86 -> Construction.buyHouse(player);
                case 99 -> {
                    DialogueManager.start(player, 147);
                    player.setDialogueActionId(102);
                }
            }
        } else if (id == SECOND_OPTION_OF_THREE) {
            switch (player.getDialogueActionId()) {
                case 9905 -> {
                    player.skillingTask = false;
                    DailyTasks.checkTask(player);
                }
                case 65 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSlayer().getDuoPartner() == null) {
                        player.sendMessage("<img=5> You do not have a duo partner!");
                    } else {
                        Slayer.resetDuo(player, World.getPlayerByName(player.getSlayer().getDuoPartner()));
                    }
                }
                case 30 -> ShopManager.getShops().get(40).open(player);
                case 196 -> {
                    for (int index = 0; index < JewelryTeleports.values().length; index++) {
                        if (index == JewelryTeleports.jewelIndex(player.getStrippedJewelryName())
                                && player.getClickDelay().elapsed(4500) && !player.getMovementQueue().isLockMovement()) {
                            for (int i = 0; i < JewelryTeleports.values()[index].getItemId().length; i++) {
                                if (player.getSelectedSkillingItem() == JewelryTeleports.values()[index].getItemIdIndex(i)
                                        && player.getSelectedSkillingItemTwo() == JewelryTeleports.values()[index]
                                        .getItemIdIndex(i + 1)) {
                                    player.getInventory().delete(player.getSelectedSkillingItem(), 1);
                                    player.getInventory().add(player.getSelectedSkillingItemTwo(), 1);

                                    TeleportHandler.teleportPlayer(player, new Position(2441, 3090),
                                            TeleportType.RING_TELE);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    player.getClickDelay().reset();
                }
                case 197 -> {
                    for (int index = 0; index < JewelryTeleports.values().length; index++) {
                        if (index == JewelryTeleports.jewelIndex(player.getStrippedJewelryName())
                                && player.getClickDelay().elapsed(4500) && !player.getMovementQueue().isLockMovement()) {
                            for (int i = 0; i < JewelryTeleports.values()[index].getItemId().length; i++) {
                                if (player.getSelectedSkillingItem() == JewelryTeleports.values()[index].getItemIdIndex(i)
                                        && player.getSelectedSkillingItemTwo() == JewelryTeleports.values()[index]
                                        .getItemIdIndex(i + 1)) {
                                    player.getInventory().delete(player.getSelectedSkillingItem(), 1);
                                    player.getInventory().add(player.getSelectedSkillingItemTwo(), 1);

                                    TeleportHandler.teleportPlayer(player, new Position(2552, 3563),
                                            TeleportType.RING_TELE);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    player.getClickDelay().reset();
                }
                case 15 -> {
                    DialogueManager.start(player, 25);
                    player.setDialogueActionId(15);
                }
                case 214 -> TeleportHandler.teleportPlayer(player, new Position(2738, 3467),
                        player.getSpellbook().getTeleportType());
                case 13 -> {
                    player.setDialogueActionId(78);
                    DialogueManager.start(player, 199);
                }
                case 21 -> RecipeForDisaster.openQuestLog(player);
                case 19 -> {
                    DialogueManager.start(player, 33);
                    player.setDialogueActionId(22);
                }
                case 22 -> Nomad.openQuestLog(player);
                case 25 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSkillManager().getCurrentLevel(Skill.HUNTER) < 23) {
                        player.getPacketSender()
                                .sendMessage("You need a Hunter level of at least 23 to visit the hunting area.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(2922, 2885),
                            player.getSpellbook().getTeleportType());
                }
                case 35 -> {
                    player.getPacketSender().sendEnterAmountPrompt(
                            "How many shards would you like to sell? (You can use K, M, B prefixes)");
                    player.setInputHandling(new SellShards());
                }
                case 41 -> TeleportHandler.teleportPlayer(player, new Position(2903, 5204),
                        player.getSpellbook().getTeleportType());
                case 47 -> {
                    TeleportHandler.teleportPlayer(player, new Position(2979, 3237),
                            player.getSpellbook().getTeleportType());
                    player.getPacketSender().sendMessage("Welcome to the new Mining area.");
                }
                case 48 -> {
                    if (player.getInteractingObject() != null) {
                        Mining.startMining(player, new GameObject(24445, player.getInteractingObject().getPosition()));
                    }
                    player.getPacketSender().sendInterfaceRemoval();
                }
                case 56 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < 60) {
                        player.getPacketSender()
                                .sendMessage("You need a Woodcutting level of at least 60 to teleport there.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(2558, 3884),
                            player.getSpellbook().getTeleportType());
                }
                case 58 -> ShopManager.getShops().get(39).open(player);
                case 61 -> CharmingImp.changeConfig(player, 0, 1);
                case 62 -> CharmingImp.changeConfig(player, 1, 1);
                case 63 -> CharmingImp.changeConfig(player, 2, 1);
                case 64 -> CharmingImp.changeConfig(player, 3, 1);
                case 69 -> {
                    if (player.getClickDelay().elapsed(1000)) {
                        Dungeoneering.start(player);
                    }
                }
                case 70, 71 -> {
                    final boolean all = player.getDialogueActionId() == 71;
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getInventory().getFreeSlots() == 0) {
                        player.getPacketSender().sendMessage("You do not have enough free inventory space to do that.");
                        return;
                    }
                    if (player.getInventory().contains(23020) && player.getClickDelay().elapsed(700)) {
                        int amt = !all ? 1 : player.getInventory().getAmount(23020);
                        player.getInventory().delete(23020, amt);
                        player.getPointsHandler().incrementVotingPoints(amt);
                        PlayerPanel.refreshPanel(player);
                        player.getPacketSender().sendMessage(
                                "You claim the " + (amt > 1 ? "scrolls" : "scroll") + " and receive your reward.");
                        player.getClickDelay().reset();
                    }
                    player.getPacketSender().sendString(48402, "" + player.getMinutesBonusExp() + " minutes");
                }
                case 36 -> DialogueManager.start(player, 65);
                case 99 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    if (player.getInventory().getAmount(11180) < 1) {
                        player.getPacketSender().sendMessage("You do not have enough tokens.");
                        return;
                    } else
                        player.getInventory().delete(11180, 1);
                    // So we grab the players pID too determine what Z they will be getting. Not
                    // sure how kraken handles it, but this is how we'll handle it.
                    player.moveTo(new Position(3025, 5231));
                    // player.getPacketSender().sendMessage("Index: " + player.getIndex());
                    // player.getPacketSender().sendMessage("Z: " + player.getIndex() * 4);
                    player.getPacketSender().sendMessage("Teleporting to Trio...");
                    player.getPacketSender()
                            .sendMessage("@red@Warning:@bla@ you @red@will@bla@ lose your items on death here!");
                    // Will sumbit a task to handle token remove, once they leave the minigame the
                    // task will be removed.
                    trioMinigame.handleTokenRemoval(player);
                }
            }
        } else if (id == THIRD_OPTION_OF_THREE) {
            switch (player.getDialogueActionId()) {
                case 9905, 9902, 30, 10, 13, 15, 19, 21, 22, 25, 35, 36, 47, 48, 56, 58, 61, 62, 63, 64, 69, 70, 71, 77, 86, 99, 196, 197, 65 ->
                        player.getPacketSender().sendInterfaceRemoval();
                case 41 -> {
                    player.setDialogueActionId(36);
                    DialogueManager.start(player, 65);
                }
            }
        }
    }

}
