package com.ruse.world.content.commands;

import com.ruse.GameSettings;
import com.ruse.model.*;
import com.ruse.model.input.impl.ChangePassword;
import com.ruse.model.input.impl.ChangePinPacketListener;
import com.ruse.model.input.impl.EnterReferral;
import com.ruse.security.ServerSecurity;
import com.ruse.util.Misc;
import com.ruse.util.RandomUtility;
import com.ruse.world.World;
import com.ruse.world.content.*;
import com.ruse.world.content.combat.DesolaceFormulas;
import com.ruse.world.content.combat.Maxhits;
import com.ruse.world.content.combat.prayer.CurseHandler;
import com.ruse.world.content.combat.prayer.PrayerHandler;
import com.ruse.world.content.dailyTask.DailyTaskHandler;
import com.ruse.world.content.dialogue.DialogueManager;
import com.ruse.world.content.donation.DonationManager;
import com.ruse.world.content.minigames.impl.dungeoneering.DungeoneeringParty;
import com.ruse.world.content.progressionzone.ProgressionZone;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.content.skill.impl.slayer.Slayer;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.content.voting.VoteHandler;
import com.ruse.world.entity.impl.player.Player;
import mysql.impl.Donation;

public class PlayerCommands {

    public static boolean handleCommand(Player player, String command, String[] commands){

        Player target;

        if(YoutuberLinks.handleCommand(player, commands[0]))
            return true;

        if(handleGlobalSpawn(player, commands[0]))
            return true;

        switch(commands[0]){
            case "sp": case"bp":
                player.getSeasonPass().showInterface();
                return true;

            case "security":
                player.getPSecurity().sendInterface();
                if(!player.getPSettings().getBooleanValueDef("security"))
                    player.sendMessage("@red@[SECURITY] Security is turned off! USe ::settings security to turn back on!");
                return true;

            case "settings":
                if(commands.length >= 2) {
                    switch (commands[1]) {
                        case "drop":
                            player.getPSettings().setSetting("drop-messages", !player.getPSettings().getBooleanValue("drop-messages"));
                            player.sendMessage("HDrop Messages is now : "+(player.getPSettings().getBooleanValue("drop-messages") ? "@gre@Active" : "@redInactive"));
                            break;
                        case "hide":
                            player.getPSettings().setSetting("hidden-players", !player.getPSettings().getBooleanValue("hidden-players"));
                            player.sendMessage("Hidden Players is now : "+(player.getPSettings().getBooleanValue("hidden-players") ? "@gre@Active" : "@redInactive"));
                            break;
                        case "toggle":
                            player.getPSettings().setSetting("drop-message-personal", !player.getPSettings().getBooleanValue("drop-message-personal"));
                            player.sendMessage("Personal Drops is now : "+(player.getPSettings().getBooleanValue("drop-message-personal") ? "@gre@Active" : "@redInactive"));
                            break;
                        case "security":
                            player.getPSettings().setSetting("security", !player.getPSettings().getBooleanValue("security"));
                            player.sendMessage("Security is now : "+(player.getPSettings().getBooleanValue("security") ? "@gre@Active" : "@redInactive"));
                            break;
                    }
                } else {
                    player.getPacketSender().sendMessage("::settings drop - Toggle drop messages");
                    player.getPacketSender().sendMessage("::settings hide - Toggle hidden players");
                    player.getPacketSender().sendMessage("::settings toggle - Toggle off Personal drops");
                    player.getPacketSender().sendMessage("::settings security - Turns off account security");
                }
                return true;

            case "resetduo":
                player.getPacketSender().sendInterfaceRemoval();
                if (player.getSlayer().getDuoPartner() != null) {
                    Slayer.resetDuo(player, World.getPlayerByName(player.getSlayer().getDuoPartner()));
                }
                return true;

            case "forge":
                //player.getForge().showInterface();
                return true;

            case "pickaxe":
                player.getInventory().add(1265, 1);
                return true;

            case "claim":
                DonationManager.getInstance().claimDonation(player);
                return true;

            case "reward":
                VoteHandler.processVote(player);
                return true;

            case "eq":
                player.getEquipmentEnhancement().openInterface();
                return true;

            case "totem": case "buff":
                TeleportHandler.teleportPlayer(player, new Position(2232, 3757, 0),
                        player.getSpellbook().getTeleportType());
                return true;

            case "slayer":
                TeleportHandler.teleportPlayer(player, new Position(2179, 3747, 0),
                        player.getSpellbook().getTeleportType());
                return true;

            case "perks":
                ServerPerks.getInstance().open(player);
                break;

            case "startzones": case "starterzone": case "train": case "start" : case "starter": case "training":
                ProgressionZone.teleport(player);
                break;

            case "kills":
                player.getPacketSender().sendInterfaceRemoval();
                KillTrackerInterface.open(player);
                break;

            case "requestraid": case "invplayer":
                String targets = command
                        .substring(commands[0].length() + 1);
                target = World.getPlayerByName(targets);
                player.getMinigameAttributes().getDungeoneeringAttributes().getParty().invite(target);
                return true;

            case "createraidparty": case "createparty":
                DungeoneeringParty.create(player);
                return true;

            case "deleteraidparty": case "deleteparty":
                com.ruse.world.content.skill.impl.old_dungeoneering.Dungeoneering.leave(player, false, true);
                player.getPacketSender().sendString(29053, "").sendString(29054, "");

                for (int i = 0; i < 10; i++) {
                    player.getPacketSender().sendString(29095 + i, "");
                }
                return true;

            case "startraids": case "startraid":
                if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
                        || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
                    player.getPacketSender().sendMessage("You cannot do this at the moment.");
                    return true;
                }
                com.ruse.world.content.skill.impl.old_dungeoneering.Dungeoneering.start(player);
                return true;

            case "deals":
                player.sendMessage(
                        "<shad=1>@yel@<img=14>Please check out the donation deals in our ::Discord - #Donation-deals");
                return true;

            case "dr": case "mydr": case "droprate":
                player.getPacketSender()
                        .sendMessage("Droprate  bonus is + @red@" + CustomDropUtils.drBonus(player, player.getSlayer().getSlayerTask().getNpcId())
                                + "@bla@%. / X2 Drop bonus is + <col=248f8f>" + CustomDropUtils.getDoubleDropChance(player, player.getSlayer().getSlayerTask().getNpcId())
                                + "@bla@%.");
                return true;

            case "ddr":
                player.getPacketSender().sendMessage(
                        "Your Double  bonus is + @red@" + CustomDropUtils.getDoubleDropChance(player, player.getSlayer().getSlayerTask().getNpcId()) + "@bla@%.");
                return true;

            case "maxhit":
                player.getPacketSender().sendMessage("<shad=1>@red@Melee Maxhit: " + (Maxhits.melee(player, player) / 10));
                player.getPacketSender()
                        .sendMessage("<shad=1>@gre@Ranged Maxhit: " + (Maxhits.ranged(player, player) / 10));
                player.getPacketSender().sendMessage("<shad=1>@cya@Magic Maxhit: " + (Maxhits.magic(player, player) / 10));
                return true;

            case "achieve": case "achievements": case "dailytasks": case "tasks":
                player.getAchievements().refreshInterface(player.getAchievements().currentInterface);
                player.getPacketSender().sendConfig(6000, 3);
                return true;

            case "resetdaily":
                new DailyTaskHandler(player).resetTasks();
                return true;

            case "collection": case "collectionlogs":
                player.getCollectionLog().open();
                return true;

            case "commands":
                sendCommands(player);
                return true;

            case "donate": case "store":
                player.getPacketSender().sendString(1, GameSettings.StoreUrl);
                player.getPacketSender().sendMessage("Attempting to open the store");
                return true;

            case "discord": case "forums": case "forum": case "updates": case "rule": case "rules":
                player.getPacketSender().sendString(1, GameSettings.DiscordUrl);
                player.getPacketSender().sendMessage("Attempting to open our Discord Server");
                return true;

            case "vote":
                player.getPacketSender().sendString(1, GameSettings.VoteUrl);// "http://Ruseps.com/vote/?user="+player.getUsername());
                player.getPacketSender().sendMessage("When you vote do ::reward to redeem votes");
                return true;

            case "changepass": case "changepassword":
                player.setInputHandling(new ChangePassword());
                player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
                return true;

            case "die":
                player.setConstitution(0);
                return true;

            case "changepin":
                player.setInputHandling(new ChangePinPacketListener());
                player.getPacketSender().sendEnterInputPrompt("Enter a new pin");
                return true;

            case "attacks":
                int attack = DesolaceFormulas.getMeleeAttack(player);
                int range = DesolaceFormulas.getRangedAttack(player);
                int magic = DesolaceFormulas.getMagicAttack(player);
                player.getPacketSender().sendMessage("@bla@Melee attack: @or2@" + attack + "@bla@, ranged attack: @or2@"
                        + range + "@bla@, magic attack: @or2@" + magic);
                return true;

            case "elo":
                player.sendMessage("ELO Rating : @red@" + Math.round(player.getBonusManager().getExtraBonus()[BonusManager.DEFENCE_SUMMONING]));
                return true;

            case "logout":
                World.getPlayers().remove(player);
                return true;

            case "ref": case "refer": case "referal":
                if (player.hasReferral) {
                    player.getPacketSender().sendMessage("You have already claimed a referral reward on this account!");
                } else {
                    player.getPacketSender().sendEnterInputPrompt("Please type your refer code to receive a reward!");
                    player.setInputHandling(new EnterReferral());
                }
                return true;

            case "switchbook":
                if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 30) {
                    player.getPacketSender().sendMessage("You need a Defence level of at least 30 to use this altar.");
                    return true;
                }
                player.performAnimation(new Animation(645));
                if (player.getPrayerbook() == Prayerbook.NORMAL) {
                    player.getPacketSender().sendMessage("You sense a surge of power flow through your body!");
                    player.setPrayerbook(Prayerbook.CURSES);
                } else {
                    player.getPacketSender().sendMessage("You sense a surge of purity flow through your body!");
                    player.setPrayerbook(Prayerbook.NORMAL);
                }
                player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
                PrayerHandler.deactivateAll(player);
                CurseHandler.deactivateAll(player);
                return true;

            case "home":
                if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
                        || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
                    player.getPacketSender().sendMessage("You cannot do this at the moment.");
                    return true;
                }
                Position pos = new Position(2222, 3747);
                TeleportHandler.teleportPlayer(player, pos, player.getSpellbook().getTeleportType());
                player.getPacketSender().sendMessage("Teleporting you home!");
                return true;

            case "dson":
                player.getPacketSender().sendWalkableInterface(60_000, true);
                return true;

            case "dsoff":
                player.getPacketSender().sendWalkableInterface(60_000, false);
                return true;

            case "youtube": case "stream":
                if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
                        || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
                    player.getPacketSender().sendMessage("You cannot do this at the moment.");
                    return true;
                }
                Position[] locations = new Position[]{new Position(2852, 2708, 0)};
                Position teleportLocation = locations[RandomUtility.exclusiveRandom(0, locations.length)];

                TeleportHandler.teleportPlayer(player, teleportLocation, player.getSpellbook().getTeleportType());
                return true;

            case "iron":
                if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
                        || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
                    player.getPacketSender().sendMessage("You cannot do this at the moment.");
                    return true;
                }
                if (!player.getRights().isStaff()) {
                    if (!player.getGameMode().isIronman()) {
                        player.getPacketSender().sendMessage("Become an ironman!");
                        return true;
                    }
                }
                Position position = new Position(3811, 2839, 0);

                TeleportHandler.teleportPlayer(player, position, TeleportType.NORMAL);
                return true;

            case "help":
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

            case "empty":
                player.setDialogueActionId(523);
                DialogueManager.start(player, 523);
                return true;

            case "droplog":
                player.getPacketSender().sendInterfaceRemoval();
                return true;

            case "fly":
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

            case "ghost":
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

            case "yell":
                handleYell(player, command);
                return true;

            case "daily":
                player.getAttendenceUI().showInterface();
                return true;
        }
        return false;
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
//        player.getPacketSender().sendString(index++, color + "::home - Teleports you home");
//        player.getPacketSender().sendString(index++, color + "::perks - Opens up the server perks interface");
//        player.getPacketSender().sendString(index++, color + "::train/train2 - Teleports you to the starter areas");
//        player.getPacketSender().sendString(index++, color + "::checkdaily - Checks your daily dask");
//        player.getPacketSender().sendString(index++, color + "::shops - Teleports you to all shops");
//        player.getPacketSender().sendString(index++, color + "");
//        player.getPacketSender().sendString(index++, color1 + "Interface Commands:");
//        player.getPacketSender().sendString(index++, color + "::kills - opens up your personal kill tracker list");
//        player.getPacketSender().sendString(index++, color + "::pos - opens the player owned shops interface");
//        player.getPacketSender().sendString(index++, color + "::tasks - opens the achievements interface");
//        player.getPacketSender().sendString(index++, color + "::rewards - opens the possible loot interface");
//        player.getPacketSender().sendString(index++, color + "::drops - opens the loot viewer interface for npcs");
//        player.getPacketSender().sendString(index++, color + "::collection - opens the collection log interface");
//        player.getPacketSender().sendString(index++, color + "::itemstats - opens up best items interface");
//        player.getPacketSender().sendString(index++, color + "");
//        player.getPacketSender().sendString(index++, color1 + "Other Commands:");
//        player.getPacketSender().sendString(index++, color + "::dr/ddr - shows you your current droprate");
//        player.getPacketSender().sendString(index++, color + "::maxhit - shows you your current droprate");
//        player.getPacketSender().sendString(index++, color + "::changepass - allows you to change your password");
//
//
//        player.getPacketSender().sendString(index++,
//                color + "::global - teleports to global bosses");
//        player.getPacketSender().sendString(index++,
//                color + "::bank - opens up your bank ($50 total claimed required)");
//        player.getPacketSender().sendString(index++,
//                color + "::players - tells you how many players are currently online");
//        player.getPacketSender().sendString(index++, color + "::forums - opens up our forums for Tarn");
//        player.getPacketSender().sendString(index++, color + "::client - downloads our client launcher");
//        player.getPacketSender().sendString(index++, color + "::rules - opens up our rules");
//        player.getPacketSender().sendString(index++, color + "::discord - opens up our discord for Tarn");
//        player.getPacketSender().sendString(index++, color + "::vote - opens up our site for voting");
//        player.getPacketSender().sendString(index++, color + "::voted - claims your votes");
//        player.getPacketSender().sendString(index++, color + "::donate - opens up our donation site");
//        player.getPacketSender().sendString(index++, color + "::donated - claims your donation");
//        player.getPacketSender().sendString(index++, color + "::donationdeals - see if there are any promotions");
//        player.getPacketSender().sendString(index++,
//                color + "::whatdrops (item name) - tells you what drops the item");
//        player.getPacketSender().sendString(index++,
//                color + "::dropmessage - removes messages of drops going to your inv/bank");
//        player.getPacketSender().sendString(index++, color + "::help - requests assistance from a staff member");
//        player.getPacketSender().sendString(index++, color + "::yell - sends a global message");
//        player.getPacketSender().sendString(index++, color + "");
    }

    public static boolean handleGlobalSpawn(Player player, String type){
        int account = 0, z = 0;
        switch(type){
            case "ninetails":
                for (Player p : World.getPlayers()) {
                    if (p == null)
                        continue;
                    if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                        if (p.getLocation() == Locations.Location.GLOBAL_BOSS && p.getPosition().getZ() == 12) {
                            account++;
                        }
                    }
                }
                break;
            case "golden":
                for (Player p : World.getPlayers()) {
                    if (p == null)
                        continue;
                    if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                        if (p.getLocation() == Locations.Location.GLOBAL_BOSS) {
                            account++;
                        }
                    }
                }
                break;
            case "meruem":
                for (Player p : World.getPlayers()) {
                    if (p == null)
                        continue;
                    if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                        if (p.getLocation() == Locations.Location.GLOBAL_BOSS && p.getPosition().getZ() == 4) {
                            account++;
                        }
                    }
                }
                break;
            case "veigar":
                for (Player p : World.getPlayers()) {
                    if (p == null)
                        continue;
                    if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                        if (p.getLocation() == Locations.Location.GLOBAL_BOSS && p.getPosition().getZ() == 8) {
                            account++;
                        }
                    }
                }
                break;
            case "vboss": case "voteboss":
                for (Player p : World.getPlayers()) {
                    if (p == null)
                        continue;
                    if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                        if (p.getLocation() == Locations.Location.VBOSS) {
                            account++;
                        }
                    }
                }
                break;
            case "donboss": case "donatorboss": case "dboss":
                for (Player p : World.getPlayers()) {
                    if (p == null)
                        continue;
                    if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                        if (p.getLocation() == Locations.Location.GODWARSPLATFORM && p.getPosition().getZ() == 4) {
                            account++;
                        }
                    }
                }
                break;
            case "afk":
                for (Player p : World.getPlayers()) {
                    if (p == null)
                        continue;
                    if (!player.equals(p) && player.getHostAddress().equals(p.getHostAddress())) {
                        if (p.getLocation() == Locations.Location.AFK) {
                            account++;
                        }
                    }
                }
                break;
            default:
                return false;
        }

        if (account >= 1) {
            player.getPacketSender().sendMessage("You already have an account there!");
            player.getCombatBuilder().reset(true);
            return true;
        }
        switch(type){
            case "ninetails":
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 12),
                        player.getSpellbook().getTeleportType());
                return true;
            case "meruem":
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 4),
                        player.getSpellbook().getTeleportType());
                return true;
            case "veigar":
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 8),
                        player.getSpellbook().getTeleportType());
                return true;
            case "vboss": case "voteboss":
                TeleportHandler.teleportPlayer(player, new Position(2980, 2771, 0),
                        TeleportType.NORMAL);
                return true;
            case "dboss" : case "donboss": case "donatorboss":
                TeleportHandler.teleportPlayer(player, new Position(2529, 2646, 4),
                        TeleportType.NORMAL);
                return true;
            case "golden":
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 0),
                        player.getSpellbook().getTeleportType());
                return true;
            case "afk":
                TeleportHandler.teleportPlayer(player, new Position(3037, 4062, 0),
                        TeleportType.NORMAL);
                return true;
            default:
                return false;
        }
    }

    public static void handleYell(Player player, String yell){


        if (ServerSecurity.getInstance().isPlayerMuted(player.getUsername())) {
            player.getPacketSender().sendMessage("You are muted and cannot yell.");
            return;
        }
        if (player.getAmountDonated() < Donation.SAPPHIRE_DONATION_AMOUNT && !(player.getRights().isStaff() || player.getRights() == PlayerRights.YOUTUBER)) {
            player.getPacketSender().sendMessage("You need to be a Donator to yell.");
            return;
        }
        int delay = player.getRights().isStaff() ? 0 : player.getRights().getYellDelay();
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
        if (player.getAmountDonated() == Donation.ZENYTE_DONATION_AMOUNT) {
            World.sendYellMessage("<img=1508><col=" + player.getRights().getYellPrefix() +
                    " [" + Misc.ucFirst(player.getRights().name().replaceAll("_", " ")) + "]<shad=0><col=" + player.getYellHex() + "> " + player.getUsername() +
                    ": " + yellMessage);

            player.getLastYell().reset();
            return;
        }
        World.sendYellMessage(player.getRights().getYellPrefix()
                + "<img=" + player.getRights().ordinal()
                + "><col=" + player.getRights().getYellPrefix() +
                " [" + Misc.ucFirst(player.getRights().name().replaceAll("_", " ")) + "]<shad=0><col=" + player.getYellHex() + "> " + player.getUsername() +
                ": " + yellMessage);

        player.getLastYell().reset();
    }
}
