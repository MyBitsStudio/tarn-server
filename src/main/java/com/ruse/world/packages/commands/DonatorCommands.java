package com.ruse.world.packages.commands;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.util.RandomUtility;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.entity.impl.player.Player;

public class DonatorCommands {

    public static boolean handleCommand(Player player, String[] commands){
        if (player.getDonator().isClericPlus()) {
            switch(commands[0]){
                case "getyellhex":
                    player.getPacketSender().sendMessage(
                            "Your current yell hex is: <shad=0><col=" + player.getYellHex() + ">#" + player.getYellHex());
                    return true;
            }
        }
        if (player.getDonator().isTormentedPlus()) {
            switch(commands[0]){
                case "ezone":
                    if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
                            || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
                        player.getPacketSender().sendMessage("You cannot do this at the moment.");
                        return true;
                    }
                    Position[] locations = new Position[]{new Position(2602, 2774, 0)};
                    Position teleportLocation = locations[RandomUtility.exclusiveRandom(0, locations.length)];
                    TeleportHandler.teleportPlayer(player, teleportLocation, player.getSpellbook().getTeleportType());
                    player.getPacketSender().sendMessage("Teleporting you to the Emerald Donator Zone!");
                   return true;
                case "setyellhex":
                    if(commands.length >= 2) {
                        String hex = commands[1].replaceAll("#", "");
                        player.setYellHex(hex);
                        player.getPacketSender().sendMessage("You have set your hex color to: <shad=0><col=" + hex + ">#" + hex);
                        if (player.getYellHex() == null)
                            player.getPacketSender().sendMessage("There was an error setting your yell hex. You entered: " + hex);
                    } else {
                        player.getPacketSender().sendMessage("You must enter a hex color code. Example: ::setyellhex 00FF00");
                    }
                    return true;
            }
            if (player.getDonator().isMysticalPlus()) {
                switch(commands[0]){
                    case "rzone":
                        if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
                                || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
                            player.getPacketSender().sendMessage("You cannot do this at the moment.");
                            return true;
                        }

                        Position[] locations = new Position[]{new Position(2530, 2716, 0),new Position(2534, 2716, 0), new Position(2535, 2711, 0), new Position(2530, 2711, 0)};
                        Position teleportLocation = locations[RandomUtility.exclusiveRandom(0, locations.length)];
                        TeleportHandler.teleportPlayer(player, teleportLocation, player.getSpellbook().getTeleportType());
                        player.getPacketSender().sendMessage("Teleporting you to the Ruby Donator Zone!");
                        return true;
                    case "bank":
                        if (player.getInterfaceId() > 0) {
                            player.getPacketSender()
                                    .sendMessage("Please close the interface you have open before opening another one.");
                            return true;
                        }
                        if (player.getLocation() == Locations.Location.WILDERNESS || player.getLocation() == Locations.Location.DUNGEONEERING
                                || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS
                                || player.getLocation() == Locations.Location.DUEL_ARENA) {
                            player.getPacketSender().sendMessage("You cannot open your bank here.");
                            return true;
                        }
                        player.getBank(player.getCurrentBankTab()).open();
                        return true;
                }
            }
//            if (player.getAmountDonated() >= Donation.DIAMOND_DONATION_AMOUNT) {
//                switch(commands[0]){
//                    case "dzone":
//                        if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
//                                || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
//                            player.getPacketSender().sendMessage("You cannot do this at the moment.");
//                            return true;
//                        }
//                        Position[] locations = new Position[]{new Position(2590, 2718, 0)};
//                        Position teleportLocation = locations[RandomUtility.exclusiveRandom(0, locations.length)];
//                        TeleportHandler.teleportPlayer(player, teleportLocation, player.getSpellbook().getTeleportType());
//                        player.getPacketSender().sendMessage("Teleporting you to the Diamond Donator Zone!");
//                        return true;
//                }
//            }
//            if (player.getAmountDonated() >= Donation.ONYX_DONATION_AMOUNT) {
//                switch(commands[0]){
//                    case "ozone":
//                        if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
//                                || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
//                            player.getPacketSender().sendMessage("You cannot do this at the moment.");
//                            return true;
//                        }
//                        Position[] locations = new Position[]{new Position(2525, 2659, 0)};
//                        Position teleportLocation = locations[RandomUtility.exclusiveRandom(0, locations.length)];
//                        TeleportHandler.teleportPlayer(player, teleportLocation, player.getSpellbook().getTeleportType());
//                        player.getPacketSender().sendMessage("Teleporting you to the Onyx Donator Zone!");
//                        return true;
//                }
//            }
//            if (player.getAmountDonated() >= Donation.ZENYTE_DONATION_AMOUNT) {
//                switch(commands[0]){
//                    case "zzone":
//                        if (player.getLocation() != null && player.getLocation() == Locations.Location.WILDERNESS
//                                || player.getLocation() != null && player.getLocation() == Locations.Location.CUSTOM_RAIDS) {
//                            player.getPacketSender().sendMessage("You cannot do this at the moment.");
//                            return true;
//                        }
//                        Position[] locations = new Position[]{new Position(2594, 2658, 0)};
//                        Position teleportLocation = locations[RandomUtility.exclusiveRandom(0, locations.length)];
//                        TeleportHandler.teleportPlayer(player, teleportLocation, player.getSpellbook().getTeleportType());
//                        player.getPacketSender().sendMessage("Teleporting you to the Zenyte Donator Zone!");
//                        return true;
//                }
//            }
        }
        return false;
    }
}
