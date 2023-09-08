package com.ruse.world.packages.commands;

import com.ruse.GameSettings;
import com.ruse.model.*;
import com.ruse.model.input.impl.ChangePassword;
import com.ruse.model.input.impl.ChangePinPacketListener;
import com.ruse.model.input.impl.EnterReferral;
import com.ruse.security.ServerSecurity;
import com.ruse.util.Misc;
import com.ruse.util.RandomUtility;
import com.ruse.world.World;
import com.ruse.world.WorldIPChecker;
import com.ruse.world.content.*;
import com.ruse.world.content.globalBoss.GlobalBossHandler;
import com.ruse.world.packages.attendance.AttendanceTab;
import com.ruse.world.content.combat.prayer.CurseHandler;
import com.ruse.world.content.combat.prayer.PrayerHandler;
import com.ruse.world.content.dailyTask.DailyTaskHandler;
import com.ruse.world.packages.combat.drops.DropCalculator;
import com.ruse.world.packages.combat.max.MagicMax;
import com.ruse.world.packages.combat.max.MeleeMax;
import com.ruse.world.packages.combat.max.RangeMax;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.dialogue.impl.EmptyInv;
import com.ruse.world.content.minigames.impl.dungeoneering.DungeoneeringParty;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.packages.misc.Retrieval;
import com.ruse.world.packages.ranks.StaffRank;
import com.ruse.world.packages.tracks.TrackInterface;
import com.ruse.world.packages.vip.VIPManager;
import com.ruse.world.packages.voting.VoteHandler;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCommands {

    public static boolean handleCommand(Player player, String command, String[] commands){

        Player target;

        if(YoutuberLinks.handleCommand(player, commands[0]))
            return true;

        if(handleGlobalSpawn(player, commands[0]))
            return true;

        switch (commands[0]) {

            case "redeem" -> {
                if(commands.length >= 2) {
                    switch(commands[1]){
                        case "voting", "voted", "vote" -> {
                            VoteHandler.processVote(player);
                            return true;
                        }
                        case "donation", "donated", "donate" -> {
                            VIPManager.claim(player);
                            return true;
                        }
                        case "retrieve", "ret", "claim" -> {
                            Retrieval.retrieve(player);
                            return true;
                        }
                    }

                } else {
                    player.sendMessage("::redeem donate|donated|donation|vote|voted|voting|retrieve|ret|claim");
                }
                return true;
            }
            case "sp", "bp" -> {
                player.getSeasonPass().showInterface();
                return true;
            }
            case "security" -> {
                player.getPSecurity().sendInterface();
                if (!player.getPSettings().getBooleanValueDef("security"))
                    player.sendMessage("@red@[SECURITY] Security is turned off! USe ::settings security to turn back on!");
                return true;
            }
            case "settings" -> {
                if (commands.length >= 2) {
                    switch (commands[1]) {
                        case "drop" -> {
                            player.getPSettings().setSetting("drop-messages", !player.getPSettings().getBooleanValue("drop-messages"));
                            player.sendMessage("HDrop Messages is now : " + (player.getPSettings().getBooleanValue("drop-messages") ? "@gre@Active" : "@red@Inactive"));
                        }
                        case "hide" -> {
                            player.getPSettings().setSetting("hidden-players", !player.getPSettings().getBooleanValue("hidden-players"));
                            player.sendMessage("Hidden Players is now : " + (player.getPSettings().getBooleanValue("hidden-players") ? "@gre@Active" : "@red@Inactive"));
                        }
                        case "toggle" -> {
                            player.getPSettings().setSetting("drop-message-personal", !player.getPSettings().getBooleanValue("drop-message-personal"));
                            player.sendMessage("Personal Drops is now : " + (player.getPSettings().getBooleanValue("drop-message-personal") ? "@gre@Active" : "@red@Inactive"));
                        }
                        case "security" -> {
                            player.getPSettings().setSetting("security", !player.getPSettings().getBooleanValue("security"));
                            player.sendMessage("Security is now : " + (player.getPSettings().getBooleanValue("security") ? "@gre@Active" : "@red@Inactive"));
                        }
                    }
                } else {
                    player.getPacketSender().sendMessage("::settings drop - Toggle drop messages");
                    player.getPacketSender().sendMessage("::settings hide - Toggle hidden players");
                    player.getPacketSender().sendMessage("::settings toggle - Toggle off Personal drops");
                    player.getPacketSender().sendMessage("::settings security - Turns off account security");
                }
                return true;
            }
            case "forge" -> {
                player.getForge().showInterface();
                return true;
            }
            case "slayer" -> {
                TeleportHandler.teleportPlayer(player, new Position(2207, 3745, 0),
                        player.getSpellbook().getTeleportType());
                return true;
            }
            case "kills" -> {
                player.getPacketSender().sendInterfaceRemoval();
                KillTrackerInterface.open(player);
            }
            case "dr", "mydr", "droprate" -> {
                player.getPacketSender()
                        .sendMessage("Droprate  bonus is + @red@" + DropCalculator.getDropChance(player, 9837)
                                + "@bla@%. / X2 Drop bonus is + <col=248f8f>" + DropCalculator.getDoubleDropChance(player, 9837)
                                + "@bla@%.");
                return true;
            }
            case "ddr" -> {
                player.getPacketSender().sendMessage(
                        "Your Double  bonus is + @red@" + DropCalculator.getDoubleDropChance(player, 9837) + "@bla@%.");
                return true;
            }
            case "maxhit" -> {
                player.getPacketSender().sendMessage("<shad=1>@red@Melee Maxhit: " + (MeleeMax.newMelee(player, player) / 10));
                player.getPacketSender()
                        .sendMessage("<shad=1>@gre@Ranged Maxhit: " + (RangeMax.newRange(player, player) / 10));
                player.getPacketSender().sendMessage("<shad=1>@cya@Magic Maxhit: " + (MagicMax.newMagic(player, player) / 10));
                return true;
            }
            case "collection", "collectionlogs" -> {
                player.getCollectionLog().open();
                return true;
            }
            case "commands" -> {
                sendCommands(player);
                return true;
            }
            case "donate", "store" -> {
                player.getPacketSender().sendString(1, GameSettings.StoreUrl);
                player.getPacketSender().sendMessage("Attempting to open the store");
                return true;
            }
            case "discord", "forums", "forum", "updates" -> {
                player.getPacketSender().sendString(1, GameSettings.DiscordUrl);
                player.getPacketSender().sendMessage("Attempting to open our Discord Server");
                return true;
            }
            case "rule", "rules" -> {
                sendRules(player);
                return true;
            }
            case "vote" -> {
                player.getPacketSender().sendString(1, GameSettings.VoteUrl);// "http://Ruseps.com/vote/?user="+player.getUsername());
                player.getPacketSender().sendMessage("When you vote do ::redeem vote to redeem votes");
                return true;
            }
            case "changepass", "changepassword" -> {
                player.setInputHandling(new ChangePassword());
                player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
                return true;
            }
            case "die" -> {
                player.setConstitution(0);
                return true;
            }
            case "changepin" -> {
                player.setInputHandling(new ChangePinPacketListener());
                player.getPacketSender().sendEnterInputPrompt("Enter a new pin");
                return true;
            }
            case "logout" -> {
                World.getPlayers().remove(player);
                return true;
            }
//            case "ref", "refer", "referal" -> {
//                if (player.hasReferral) {
//                    player.getPacketSender().sendMessage("You have already claimed a referral reward on this account!");
//                } else {
//                    player.getPacketSender().sendEnterInputPrompt("Please type your refer code to receive a reward!");
//                    player.setInputHandling(new EnterReferral());
//                }
//                return true;
//            }
            case "home" -> {
                if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
                        || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
                    player.getPacketSender().sendMessage("You cannot do this at the moment.");
                    return true;
                }
                Position pos = new Position(2222, 3747);
                TeleportHandler.teleportPlayer(player, pos, player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Teleporting you home!");
                return true;
            }
            case "dson" -> {
                player.getPacketSender().sendWalkableInterface(60_000, true);
                return true;
            }
            case "dsoff" -> {
                player.getPacketSender().sendWalkableInterface(60_000, false);
                return true;
            }
            case "youtube", "stream" -> {
                if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
                        || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
                    player.getPacketSender().sendMessage("You cannot do this at the moment.");
                    return true;
                }
                Position[] locations = new Position[]{new Position(2852, 2708, 0)};
                Position teleportLocation = locations[RandomUtility.exclusiveRandom(0, locations.length)];
                TeleportHandler.teleportPlayer(player, teleportLocation, player.getSpellbook().getTeleportType());
                return true;
            }
            case "help" -> {
                if (player.getLastYell().elapsed(30000)) {
                    if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
                            || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
                        World.sendStaffMessage("<col=FF0066><img=5> [TICKET SYSTEM]<col=6600FF> " + player.getUsername()
                                + " has requested help, but is @red@*IN LEVEL " + player.getWildernessLevel()
                                + " WILDERNESS*<col=6600FF>. Be careful.");
                    }
                    if (ServerSecurity.getInstance().isPlayerMuted(player.getUsername())) {
                        World.sendStaffMessage("<col=FF0066><img=5> [TICKET SYSTEM]<col=6600FF> " + player.getUsername()
                                + " has requested help, but is @red@*MUTED*<col=6600FF>. Be careful.");
                    } else {
                        World.sendStaffMessage("<col=FF0066><img=5> [TICKET SYSTEM]<col=6600FF> " + player.getUsername()
                                + " has requested help. Please help them!");
                    }
                    player.getLastYell().reset();
                    player.getPacketSender()
                            .sendMessage("<col=663300>Your help request has been received. Please be patient.");
                } else {
                    player.getPacketSender().sendMessage("<col=663300>You need to wait 30 seconds before using this again.")
                            .sendMessage(
                                    "<col=663300>If it's an emergency, please private message a staff member directly instead.");
                }
                return true;
            }
            case "empty" -> {
                DialogueManager.sendDialogue(player, new EmptyInv(player), -1);
                return true;
            }
            case "fly" -> {
                if (!player.canFly()) {
                    player.getPacketSender().sendMessage("You do not understand the complexities of flight.");
                    return true;
                }
                if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
                        || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
                    player.getPacketSender().sendMessage("You cannot fly in the Wilderness.");
                    return true;
                }
                if (player.canFly() && player.isFlying()) {
                    player.getPacketSender().sendMessage("You stop flying.");
                    player.setFlying(false);
                    player.newStance();
                    return true;
                }
                if (player.canFly() && !player.isFlying()) {
                    player.getPacketSender().sendMessage("You begin flying.");
                    player.setFlying(true);
                    player.newStance();
                    return true;
                }
                return true;
            }
            case "ghost" -> {
                if (!player.canGhostWalk()) {
                    player.getPacketSender().sendMessage("You do not understand the complexities of death.");
                    return true;
                }
                if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
                        || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
                    player.getPacketSender().sendMessage("You cannot ghost walk in the Wilderness.");
                    return true;
                }
                if (player.canGhostWalk() && player.isGhostWalking()) {
                    player.getPacketSender().sendMessage("You stop ghost-walking.");
                    player.setGhostWalking(false);
                    player.newStance();
                    return true;
                }
                if (player.canGhostWalk() && !player.isGhostWalking()) {
                    player.getPacketSender().sendMessage("You begin ghost walking.");
                    player.setGhostWalking(true);
                    player.newStance();
                    return true;
                }
                return true;
            }
            case "yell" -> {
                handleYell(player, command);
                return true;
            }
            case "daily" -> {
                player.getAttendenceUI().showInterface(AttendanceTab.LOYAL);
                player.getAttendenceManager().claim();
                return true;
            }
            case "vip" -> {
                player.getPlayerVIP().sendInterface();
                return true;
            }
            case "track", "tracks" -> {
                TrackInterface.sendInterface(player, true);
                return true;
            }
        }
        return false;
    }

    public static void sendRules(Player player){
        player.getPacketSender().sendInterfaceRemoval();
        for (int i = 8145; i < 8196; i++)
            player.getPacketSender().sendString(i, "");

        player.getPacketSender().sendInterface(8134);

        player.getPacketSender().sendString(8136, "Close window");
        player.getPacketSender().sendString(8144, "In-Game Rules");
        player.getPacketSender().sendString(8145, "");
        int index = 8147;
        String color = "@dre@";
        String color1 = "@red@";

        player.getPacketSender().sendString(index++, color1 + "In-Game Rules");
        player.getPacketSender().sendString(index++, color + "1. No duping, manipulating, or abusing bugs");
        player.getPacketSender().sendString(index++, color + "2. 3 Accounts max (1 Main, 1 Iron & 1 Veteran)");
        player.getPacketSender().sendString(index++, color + "3. No trading rewards between accounts");
        player.getPacketSender().sendString(index++, color + "4. BP is for two accounts (Main & Iron)");
        player.getPacketSender().sendString(index++, color + "5. RESPECT ALL STAFF!");
        player.getPacketSender().sendString(index++, color + "6. No advertising other servers");
        player.getPacketSender().sendString(index++, color + "7. No scamming or baiting");
        player.getPacketSender().sendString(index++, color + "8. No impersonating staff members");
        player.getPacketSender().sendString(index++, color + "9. Any racism/sexism/discrimination is not tolerated");
        player.getPacketSender().sendString(index++, color + "10. No Multi Accounting on PVM");
        player.getPacketSender().sendString(index++, color + "11. One account per Global boss");
        player.getPacketSender().sendString(index++, color + "12. No quitting giveaways");
        player.getPacketSender().sendString(index++, color + "13. No sharing accounts / account services");
        player.getPacketSender().sendString(index++, color + "14. No selling items/accounts for rl currency");
        player.getPacketSender().sendString(index++, color + "15. No inappropriate player names");
        player.getPacketSender().sendString(index++, color1 + "!!----------------------------------!!");
        player.getPacketSender().sendString(index++, color1 + "!!-----------!! WARNING !!----------!!");
        player.getPacketSender().sendString(index++, color1 + "!!----------------------------------!!");
        player.getPacketSender().sendString(index++, color1 + "!! Any abuse can result in rollbacks or ban !!");
        player.getPacketSender().sendString(index++, color1 + "!! Racism/Sexism is immediate mute !!");
        player.getPacketSender().sendString(index++, color1 + "!! Selling items/accounts is immediate ban !!");
        player.getPacketSender().sendString(index++, color1 + "!! Repeated offences will result in ban !!");
        player.getPacketSender().sendString(index++, color1 + "!! BP abuse will result in no refund !!");
        player.getPacketSender().sendString(index++, color1 + "!! Warnings and Mutes stack towards chances !!");
        player.getPacketSender().sendString(index++, color1 + "!!----------------------------------!!");
        player.getPacketSender().sendString(index++, color + "To submit a staff abuse ticket, please visit");
        player.getPacketSender().sendString(index++, color + "our discord, and message Corrupt or Mutant");
    }

    public static void sendCommands(Player player){
        player.sendMessage("@red@Being reworked!");
//        for (int i = 8145; i < 8196; i++)
//            player.getPacketSender().sendString(i, "");
//
//        player.getPacketSender().sendInterface(8134);
//
//        player.getPacketSender().sendString(8136, "Close window");
//        player.getPacketSender().sendString(8144, "Commands");
//        player.getPacketSender().sendString(8145, "");
//        int index = 8147;
//        String color = "@dre@";
//        String color1 = "@red@";
//
//        player.getPacketSender().sendString(index++, color1 + "Main Commands:");

    }

    public static boolean handleGlobalSpawn(Player player, @NotNull String type){
        switch (type) {
            case "lugia" -> {
                if(!WorldIPChecker.getInstance().addToContent(player, "lugia")){
                    return false;
                }
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 4),
                        TeleportType.NORMAL);
                return true;
            }
            case "ninetails" -> {
                if(!WorldIPChecker.getInstance().addToContent(player, "ninetails")){
                    return false;
                }
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 0),
                        player.getSpellbook().getTeleportType());
                return true;
            }
            case "mewtwo" -> {
                if(!WorldIPChecker.getInstance().addToContent(player, "mewtwo")){
                    return false;
                }
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 12),
                        TeleportType.NORMAL);
                return true;
            }
            case "groudon" -> {
                if(!WorldIPChecker.getInstance().addToContent(player, "groudon")){
                    return false;
                }
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 8),
                        TeleportType.NORMAL);
                return true;
            }
            case "vboss", "voteboss" -> {
                if(!WorldIPChecker.getInstance().addToContent(player, "vote-boss")){
                    return false;
                }
                TeleportHandler.teleportPlayer(player, new Position(2530, 2648, 0),
                        TeleportType.NORMAL);
                return true;
            }
            case "donboss", "donatorboss", "dboss" -> {
                if(!WorldIPChecker.getInstance().addToContent(player, "donate-boss")){
                    return false;
                }
                TeleportHandler.teleportPlayer(player, new Position(2530, 2648, 4),
                        TeleportType.NORMAL);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public static void handleYell(Player player, String yell){


        if (ServerSecurity.getInstance().isPlayerMuted(player.getUsername())) {
            player.getPacketSender().sendMessage("You are muted and cannot yell.");
            return;
        }
        if(player.getRank() != StaffRank.YOUTUBER && !player.getRank().isStaff() && !player.getDonator().isClericPlus()){
            player.getPacketSender().sendMessage("You need to be a Donator to yell.");
            return;
        }
//        if (player.getAmountDonated() < Donation.SAPPHIRE_DONATION_AMOUNT && !(player.getRights().isStaff() || player.getRights() == PlayerRights.YOUTUBER)) {
//
//        }
        int delay = player.getRank().isStaff() ? 0 : player.getDonator().getYellDelay();
        if (!player.getLastYell().elapsed((delay * 1000L))) {
            player.getPacketSender().sendMessage("You must wait at least " + delay + " seconds between every yell-message you send.");
            return;
        }
        String yellMessage = Misc.capitalizeJustFirst(yell.substring(5));

        String[] invalid = {"<img="};
        for (String s : invalid) {
            if (yellMessage.contains(s)) {
                player.getPacketSender().sendMessage("Your message contains invalid characters.");
                return;
            }
        }
        if(player.getRank().isStaff()){
            World.sendYellMessage("<img="+player.getRank().getImg()+"><col=" + player.getRank().getYellPrefix() +
                    " [" + Misc.ucFirst(player.getRank().name().replaceAll("_", " ")) + "]<shad=0><col=" + player.getYellHex() + "> " + player.getUsername() +
                    ": " + yellMessage);

            player.getLastYell().reset();
            return;
        }

        World.sendYellMessage(player.getDonator().getPrefix()
                + "<img=" + player.getDonator().ordinal()
                + "><col=" + player.getDonator().getPrefix() +
                " [" + Misc.ucFirst(player.getDonator().name().replaceAll("_", " ")) + "]<shad=0><col=" + player.getYellHex() + "> " + player.getUsername() +
                ": " + yellMessage);

        player.getLastYell().reset();
    }
}
