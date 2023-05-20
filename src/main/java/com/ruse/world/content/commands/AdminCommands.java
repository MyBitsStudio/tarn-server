package com.ruse.world.content.commands;

import com.ruse.GameSettings;
import com.ruse.world.World;
import com.ruse.world.content.WorldBosses;
import com.ruse.world.content.WorldBosses2;
import com.ruse.world.content.WorldBosses3;
import com.ruse.world.content.WorldBosses4;
import com.ruse.world.content.donation.DonationManager;
import com.ruse.world.content.voting.VoteBossDrop;
import com.ruse.world.entity.impl.player.Player;

public class AdminCommands {

    public static boolean handleCommand(Player player, String command, String[] commands){

        Player player2;

        switch(commands[0]){
            case "broadcast":
                int time = Integer.parseInt(commands[1]);
                String message = command.substring(commands[0].length() + commands[1].length() + 2);
                for (Player players : World.getPlayers()) {
                    if (players == null) {
                        continue;
                    }
                    players.getPacketSender().sendBroadCastMessage(message, time);
                }
                World.sendBroadcastMessage(message);
                GameSettings.broadcastMessage = message;
                GameSettings.broadcastTime = time;
                return true;

            case "checkbank":
                player2 = World.getPlayerByName(command.substring(commands[0].length() + 1));
                if (player2 == null) {
                    player.getPacketSender().sendMessage("Player is offline!");
                } else {
                    player.getPacketSender().sendMessage("Loading bank..");
                    player2.getBank(0).openOther(player, true, false);
                }
                return true;

            case "checkinv":
                player2 = World.getPlayerByName(command.substring(commands[0].length() + 1));
                if (player2 == null) {
                    player.getPacketSender().sendMessage("Cannot find that player online..");
                    return true;
                }
                player.getPacketSender().sendItemContainer(player2.getInventory(), 3214);
                return true;

            case "endcheck":
                player.getInventory().refreshItems();
                return true;

            case "unban":

                return true;

            case "ban":
                switch(commands[1]){

                }
                return true;

            case "spawn":
                if(commands.length >= 2) {
                    switch (commands[1]) {
                        case "donation": case "donate":
                            DonationManager.getInstance().forceSpawn();
                            player.getPacketSender().sendMessage("Spawning donation boss.");
                            return true;
                        case "vote":
                            VoteBossDrop.handleForcedSpawn();
                            player.getPacketSender().sendMessage("Spawning vote boss.");
                            return true;
                        case "meruem":
                            WorldBosses3.handleForcedSpawn();
                            player.getPacketSender().sendMessage("Spawning vote boss.");
                            return true;
                        case "veigar":
                            WorldBosses4.handleForcedSpawn();
                            player.getPacketSender().sendMessage("Spawning vote boss.");
                            return true;
                        case "golden":
                            WorldBosses.handleForcedSpawn();
                            player.getPacketSender().sendMessage("Spawning vote boss.");
                            return true;
                        case "nine":
                            WorldBosses2.handleForcedSpawn();
                            player.getPacketSender().sendMessage("Spawning vote boss.");
                            return true;
                    }
                } else {
                    player.getPacketSender().sendMessage("Invalid spawn command.");
                }
                return true;

        }
        return false;
    }
}
