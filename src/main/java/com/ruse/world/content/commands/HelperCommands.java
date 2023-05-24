package com.ruse.world.content.commands;

import com.ruse.GameSettings;
import com.ruse.model.Graphic;
import com.ruse.model.Position;
import com.ruse.net.security.ConnectionHandler;
import com.ruse.security.ServerSecurity;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.content.PlayerPunishment;
import com.ruse.world.content.discordbot.AdminCord;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.entity.impl.player.Player;

import java.io.File;
import java.time.LocalDateTime;

import static com.ruse.world.entity.impl.player.PlayerFlags.FORCE_KICK;

public class HelperCommands {

    public static boolean handleCommand(Player player, String command, String[] commands){
        String playerToTele;
        Player player2;
        switch(commands[0]){
            case "checkalt":
                Player target = World.getPlayerByName(command.substring(commands[0].length() + 1));
                if (target == null) {
                    player.sendMessage(command.substring(commands[0].length() + 1) + " is not valid");
                } else {
                    player.sendMessage("Searching...");

                    for (Player plr : World.getPlayers()) {
                        if (plr != null) {
                            if (plr.getHostAddress().equals(target.getHostAddress()) && !plr.equals(target)
                                    && !plr.getUsername().equalsIgnoreCase("nucky")
                                    && !target.getUsername().equalsIgnoreCase("nucky")
                                    && !plr.getUsername().equalsIgnoreCase("test")
                                    && !target.getUsername().equalsIgnoreCase("test")
                                    && !plr.getUsername().equalsIgnoreCase("james")
                                    && !target.getUsername().equalsIgnoreCase("james")) {
                                player.sendMessage(
                                        plr.getUsername() + " has the same Ip address as " + target.getUsername());
                            }
                        }
                    }

                    player.sendMessage("Done search");
                }
                return true;

            case "teleto":
                playerToTele = command.substring(commands[0].length() + 1);
                player2 = World.getPlayerByName(playerToTele);
                if (player2 == null) {
                    player.getPacketSender().sendMessage("Cannot find that player online..");
                    return true;
                } else {
                    boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy());
                    if (canTele) {
                        player.performGraphic(new Graphic(342));

                        TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.ZOOM);
                        player.getPacketSender().sendMessage("Teleporting to player: " + player2.getUsername());
                        player2.performGraphic(new Graphic(730));
                        player2.getPacketSender().sendMessage("<img=5> ATTENTION: <col=6600FF>" + player.getRights() + " " + player.getUsername() + " is teleporting to you.");
                    } else
                        player.getPacketSender()
                                .sendMessage("You can not teleport to this player at the moment. Minigame maybe?");
                }
                return true;

            case "teletome":
                playerToTele = command.substring(commands[0].length() + 1);
                player2 = World.getPlayerByName(playerToTele);
                if (player2 == null) {
                    player.getPacketSender().sendMessage("Cannot find that player..");
                    return true;
                } else {
                    boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy());
                    if (canTele && player2.getDueling().duelingStatus < 5) {
                        player.getPacketSender().sendMessage("Moving player: " + player2.getUsername());
                        player2.getPacketSender().sendMessage("You've been moved to " + player.getUsername());
                        World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
                                + " just moved " + player2.getUsername() + " to them.");
                        player2.moveTo(player.getPosition().copy());
                        player2.performGraphic(new Graphic(342));
                    } else
                        player.getPacketSender()
                                .sendMessage("Failed to move player to your coords. Are you or them in a minigame?")
                                .sendMessage("Also will fail if they're in duel/wild.");
                }
                return true;

            case "sz": case "staffzone":
                if (commands.length > 1 && commands[1].equalsIgnoreCase("all") && player.getRights().OwnerDeveloperOnly()) {
                    player.getPacketSender().sendMessage("Teleporting all staff to staffzone.");
                    for (Player players : World.getPlayers()) {
                        if (players != null) {
                            if (players.getRights().isStaff()) {
                                TeleportHandler.teleportPlayer(players, new Position(2007, 4439), TeleportType.NORMAL);
                            }
                        }
                    }
                } else {
                    TeleportHandler.teleportPlayer(player, new Position(2007, 4439), TeleportType.NORMAL);
                }
                return true;

            case "kick":
                playerToTele = command.substring(commands[0].length() + 1);
                player2 = World.getPlayerByName(playerToTele);
                if (player2 == null) {
                    player.getPacketSender()
                            .sendMessage("Player " + playerToTele + " couldn't be found on " + GameSettings.RSPS_NAME + ".");
                    return true;
                } else if (player2.getDueling().duelingStatus < 5) {
                    //PlayerHandler.handleLogout(playerToKick, false);
                    player2.save();
                    player2.getPlayerFlags().setFlag(FORCE_KICK, true);
                    player.getPacketSender().sendMessage("Kicked " + player2.getUsername() + ".");
                    PlayerLogs.log(player.getUsername(),
                            player.getUsername() + " just kicked " + player2.getUsername() + "!");
                    World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
                            + " just kicked " + player2.getUsername() + ".");
                    AdminCord.sendMessage(1109203238907027527L, player.getUsername() + " used command ::" + command
                            + " | Player rights = " + player.getRights());
                } else {
                    PlayerLogs.log(player.getUsername(), player.getUsername() + " just tried to kick "
                            + player2.getUsername() + " in an active duel.");
                    World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
                            + " just tried to kick " + player2.getUsername() + " in an active duel.");
                    AdminCord.sendMessage(1109203238907027527L,  player.getUsername()
                            + " just tried to kick " + player2.getUsername() + " in an active duel.");
                    player.getPacketSender().sendMessage("You've tried to kick someone in duel arena/wild. Logs written.");
                }
                return true;

            case "kill":
                playerToTele = command.substring(commands[0].length() + 1);
                player2 = World.getPlayerByName(playerToTele);
                if (player2 == null) {
                    player.getPacketSender()
                            .sendMessage("Player " + playerToTele + " couldn't be found on " + GameSettings.RSPS_NAME + ".");
                    return true;
                } else if (player2.getDueling().duelingStatus < 5) {
                    player2.setConstitution(0);
                    //PlayerHandler.handleLogout(playerToKick, false);
                } else {
                    player2.setConstitution(0);
                }
                return true;

            case "unmute":
                playerToTele = command.substring(commands[0].length() + 1);
                player2 = World.getPlayerByName(playerToTele);

                if(player2 == null){
                    player.getPacketSender().sendMessage("Player: " + playerToTele + " is not online.");
                    return true;
                }

                ServerSecurity.getInstance().unMute(playerToTele);
                AdminCord.sendMessage(1109203238907027527L,  player.getUsername()
                        + " just ummuted " + playerToTele);
                player.getPacketSender().sendMessage("Player " + playerToTele + " was successfully unmuted");
                player2.getPacketSender().sendMessage("@red@[STAFF] You have been unmuted by Staff. Please respect the rules next time.");
                return true;

            case "mute":
                try {
                    int timer = Integer.parseInt(commands[1]);
                    playerToTele = command.substring(commands[0].length() + commands[1].length() + 2);
                    player2 = World.getPlayerByName(playerToTele);

                    if(player2 == null){
                        player.getPacketSender().sendMessage("Player: " + playerToTele + " is not online.");
                        return true;
                    }

                    player2.save();

                    if (ServerSecurity.getInstance().isPlayerMuted(playerToTele)) {
                        player.getPacketSender().sendMessage("Player: " + playerToTele + " already has an active mute.");
                        return true;
                    }
                    PlayerLogs.log(player.getUsername(), player.getUsername() + " just muted " + playerToTele);
                    World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + playerToTele +" was just muted for breaking the Terms of Conduct.");
                    AdminCord.sendMessage(1109203238907027527L,  player.getUsername()
                            + " just muted " + playerToTele);
                    ServerSecurity.getInstance().mutePlayer(player2, timer);
                    player.getPacketSender().sendMessage("Player " + playerToTele + " was successfully muted");
                    player2.getPacketSender().sendMessage("@red@[STAFF] You have been muted by a staff member for "+(timer * 5)+" minutes.");
                } catch (Exception e) {
                    player.getPacketSender().sendMessage("Error! Usage ::mute 1 username");
                }
                return true;

            case "remindvote":
                World.sendMessage(
                        "<img=5> <col=008FB2><shad=0>Remember to collect rewards by using the ::vote command every 12 hours!");
                return true;

            case "remindhelp":
                World.sendMessage(
                        "<img=5> <col=008FB2><shad=0>Staff members are always available, pm them if you need help!");
                return true;

            case "warn":
                playerToTele = command.substring(commands[0].length() + 1);
                player2 = World.getPlayer(playerToTele);

                if(player2 == null) {
                    player.sendMessage("This player isn't online");
                } else {
                    player2.getPacketSender().sendMessage("@red@[STAFF] You have been warned by staff members!");
                    player.sendMessage(playerToTele + " has been warned.");

                    AdminCord.sendMessage(1109203238907027527L, player.getUsername()
                            + " just warned " + playerToTele + ".");
                }
                return true;

            case "isiron":
                playerToTele = command.substring(commands[0].length() + 1);
                player2 = World.getPlayerByName(playerToTele);
                if(player2 != null)
                    player.sendMessage(player2.getUsername()+" is "+(player2.getGameMode().isIronman() ? "@gre@Ironman" : "@red@Not Ironman"));
                else
                    player.sendMessage("This player isn't online");
                return true;

            case "remove":
                playerToTele = command.substring(commands[0].length() + 1);
                player2 = World.getPlayerByName(playerToTele);

                if(player2 != null) {
                    World.removePlayer(player2);
                }
                return true;
        }
        return false;
    }
}