package com.ruse.world.packages.commands;

import com.ruse.GameServer;
import com.ruse.model.*;
import com.ruse.model.container.impl.Equipment;
import com.ruse.model.definitions.*;
import com.ruse.motivote3.doMotivote;
import com.ruse.security.ServerSecurity;
import com.ruse.security.save.impl.server.defs.NPCDataLoad;
import com.ruse.world.World;
import com.ruse.world.content.LotterySystem;
import com.ruse.world.content.WellOfGoodwill;
import com.ruse.world.packages.clans.ClanManager;
import com.ruse.world.content.grandexchange.GrandExchangeOffers;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.content.skill.SkillManager;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.packages.shops.ShopHandler;
import com.ruse.world.packages.slot.SlotBonus;
import com.ruse.world.packages.slot.SlotEffect;
import com.ruse.world.packages.tower.TarnTower;
import com.ruse.world.packages.tracks.TrackInterface;
import com.ruse.world.packages.voting.VoteBossDrop;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import java.util.Objects;

public class OwnerCommands {

    public static boolean handleCommand(Player player, String command, String[] commands){

        int id, amount;
        String name;
        boolean found;
        Player targets;

        switch (commands[0]) {
            case "findobj" -> {
                name = command.substring(8).toLowerCase().replaceAll("_", " ");
                player.getPacketSender().sendMessage("Finding object id for object - " + name);
                found = false;
                for (int i = 0; i <  100000; i++) {
                    GameObjectDefinition def = GameObjectDefinition.forId(i);
                    if(def == null || def.getName() == null)
                        continue;
                    if ( def.getName().toLowerCase().contains(name)) {
                        player.getPacketSender().sendMessage("Found object with name ["
                                +  def.getName().toLowerCase() + "] - id: " + i);
                        found = true;
                    }
                }
                if (!found) {
                    player.getPacketSender().sendMessage("No object with name [" + name + "] has been found!");
                }
                return true;
            }
            case "region" -> {
                try {
                    int region = Integer.parseInt(commands[1]);
                    int x = (region >> 8) * 64 + 32;
                    int y = (region & 0xFF) * 64 + 32;
                    player.sendMessage("region: " + region);
                    TeleportHandler.teleportPlayer(player, new Position(x, y), TeleportType.NORMAL);
                } catch (NumberFormatException e) {
                    player.sendMessage("Usage: ::region regionid");
                    return true;
                }
                return true;
            }
            case "mypos", "postion", "coords" -> {
                player.getPacketSender().sendMessage(player.getPosition().toString());
                return true;
            }
            case "tele" -> {
                int x = Integer.parseInt(commands[1]), y = Integer.parseInt(commands[2]);
                int z = player.getPosition().getZ();
                if (commands.length > 3)
                    z = Integer.parseInt(commands[3]);
                Position position = new Position(x, y, z);
                player.moveTo(position);
                player.getPacketSender().sendMessage("Teleporting to " + position);
                return true;
            }
            case "delete" -> {
                id = Integer.parseInt(commands[1]);
                for (NPC npc : World.getNpcs()) {
                    if (npc == null)
                        continue;
                    if (npc.getId() == id) {
                        World.deregister(npc);
                    }
                }
                return true;
            }
            case "item" -> {
                id = Integer.parseInt(commands[1]);
                if (id > ItemDefinition.getMaxAmountOfItems()) {
                    player.getPacketSender().sendMessage(
                            "Invalid item id entered. Max amount of items: " + ItemDefinition.getMaxAmountOfItems());
                    return true;
                }
                amount = (commands.length == 2 ? 1
                        : Integer.parseInt(commands[2].trim().toLowerCase().replaceAll("k", "000").replaceAll("m", "000000")
                        .replaceAll("b", "000000000")));
                Item item = new Item(id, amount);
                player.getInventory().add(item, true);
                return true;
            }
            case "giveall" -> {
                id = Integer.parseInt(commands[1]);
                amount = Integer.parseInt(commands[2]);
                World.getPlayers().stream()
                        .filter(Objects::nonNull)
                        .forEach(p -> {
                            p.getInventory().add(id, amount);
                            p.sendMessage("You have recieved: " + ItemDefinition.forId(id).getName() + " from " + player.getUsername() + " for being beasts.");
                        });
                return true;
            }
            case "master" -> {
                for (Skill skill : Skill.values()) {
                    int level = SkillManager.getMaxAchievingLevel(skill);
                    player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                            SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
                }
                player.getPacketSender().sendMessage("You are now a master of all skills.");
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                return true;
            }
            case "setyellhex" -> {
                if (commands.length >= 2) {
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
            case "bank" -> {
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
            case "findnpc" -> {
                name = command.substring(commands[0].length() + 1);
                player.getPacketSender().sendMessage("Finding item id for item - " + name);
                found = false;
                for (int i = 0; i < NpcDefinition.definitions.length; i++) {
                    if (NpcDefinition.forId(i) == null || NpcDefinition.forId(i).getName() == null) {
                        continue;
                    }
                    if (NpcDefinition.forId(i).getName().toLowerCase().contains(name)) {
                        player.getPacketSender().sendMessage(
                                "Found NPC with name [" + NpcDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
                        found = true;
                    }
                }
                if (!found) {
                    player.getPacketSender().sendMessage("No NPC with name [" + name + "] has been found!");
                }
                return true;
            }
            case "god" -> {

            }
            case "update" -> {
                int time = Integer.parseInt(commands[1]);
                if (time > 0) {
                    GameServer.setUpdating(true);
                    World.sendNewsMessage("<col=FF0066><img=2> [SERVER]<col=6600FF> " + player.getUsername()
                            + " just started an update in " + (int) ((time * 0.6)) + " ticks.");
                    World.sendNewsMessage("<col=FF0066><img=2> [SERVER]<col=6600FF> Please finish what you are doing now!");
                    World.sendNewsMessage("<col=FF0066><img=2> [SERVER]<col=6600FF> Wait until announcement to login again!");
                    for (Player players : World.getPlayers()) {
                        if (players == null)
                            continue;
                        players.getPacketSender().sendSystemUpdate(time);
                    }
                }
                return true;
            }
            case "whip" -> {
                World.getPlayers().stream()
                        .filter(Objects::nonNull)
                        .forEach(players -> {

                        });
                return true;
            }
            case "takeall" -> {
                int items = Integer.parseInt(commands[1]);
                World.getPlayers().stream()
                        .filter(Objects::nonNull)
                        .forEach(players -> {
                            if (players.getInventory().contains(items))
                                players.getInventory().delete(items, players.getInventory().getAmount(items));
                            if (players.getEquipment().contains(items))
                                players.getEquipment().delete(items, players.getEquipment().getAmount(items));
                            for (int i = 0; i < players.bankssize(); i++) {
                                if (players.getBank(i).contains(items))
                                    players.getBank(i).delete(items, players.getBank(i).getAmount(items));
                            }
                            players.sendMessage("@red@[SERVER] " + ItemDefinition.forId(items).getName() + " has been removed from your inventory.");
                        });
                return true;
            }
            case "add" -> {
                if (commands.length >= 2) {
                    switch (commands[1]) {
                        case "vote" -> {
                            amount = Integer.parseInt(commands[2]);

                            return true;
                        }
                    }
                } else {
                    player.getPacketSender().sendMessage("Use as ::add [donate/vote] [amount]");
                }
                return true;
            }
            case "reload" -> {
                if (commands.length >= 2) {
                    switch (commands[1]) {
                        case "all" -> {
                            ShopHandler.load();
                            //NPCDrops.parseDrops().load();
                            ItemDefinition.init();
                            WeaponInterfaces.parseInterfaces().load();
                            new NPCDataLoad().loadJSON("./.core/server/defs/npc/npc_data.json").run();
                            WeaponInterfaces.init();
                            ServerSecurity.getInstance().reload();

                            World.handler.reload();
                            player.sendMessage("Reloaded all definitions.");
                            return true;
                        }
                        case "some" -> {
                            new NPCDataLoad().loadJSON("./.core/server/defs/npc/npc_data.json").run();
                            ItemDefinition.init();
                           // NPCDrops.parseDrops().load();
                            ShopHandler.load();
                            return true;
                        }
                        case "shops" -> {
                            ShopHandler.load();
                            player.sendMessage("Shop reload");
                            return true;
                        }
                        case "bans" -> {
                            ServerSecurity.getInstance().reload();
                            player.sendMessage("Bans reload");
                            return true;
                        }
                        case "events" -> {
                            World.handler.reload();
                            player.sendMessage("Events reload");
                            return true;
                        }
                    }
                }
                return true;
            }
            case "saveall" -> {
                World.savePlayers();
                return true;
            }
            case "obj", "object" -> {
                id = Integer.parseInt(commands[1]);
                player.getPacketSender().sendObject(new GameObject(id, player.getPosition(), 10, 3));
                player.getPacketSender().sendMessage("Sending object: " + id);
                return true;
            }
            case "inter", "interface" -> {
                id = Integer.parseInt(commands[1]);
                player.getPacketSender().sendInterface(id);
                return true;
            }
            case "npc" -> {
                id = Integer.parseInt(commands[1]);
                NPC npc = new NPC(id, new Position(player.getPosition().getX(), player.getPosition().getY(),
                        player.getPosition().getZ()));
                World.register(npc);
                return true;
            }
            case "gfx" -> {
                id = Integer.parseInt(commands[1]);
                player.performGraphic(new Graphic(id));
                player.getPacketSender().sendMessage("Sending graphic: " + id);
                return true;
            }
            case "anim" -> {
                id = Integer.parseInt(commands[1]);
                player.performAnimation(new Animation(id));
                player.getPacketSender().sendMessage("Sending animation: " + id);
                return true;
            }
            case "shutnow" -> {
                World.sendNewsMessage("<col=FF0066><img=2> [SERVER]<col=6600FF> Server is shutting down now!");
                for (Player players : World.getPlayers()) {
                    if (players != null) {
                        players.save();
                        World.endDereg(players);
                    }
                }
                WellOfGoodwill.save();
                GrandExchangeOffers.save();
                ClanManager.getManager().save();
                LotterySystem.saveTickets();
                ServerPerks.getInstance().save();
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.exit(0);
                return true;
            }

            case "fullscrap" -> {
                return true;
            }
            case "vipadd" -> {
                id = Integer.parseInt(commands[1]);
                player.getPlayerVIP().addDonation(id);
                return true;
            }
            case "addvip" -> {
                id = Integer.parseInt(commands[1]);
                targets = World.getPlayer(command.substring(commands[0].length() + commands[1].length() + 2));
                if (targets == null) {
                    player.getPacketSender().sendMessage(command.substring(commands[0].length() + commands[1].length() + commands[2].length() + commands[3].length() + 4) + " must be online to give them stuff!");
                } else {
                    targets.getPlayerVIP().addDonation(id);
                    player.getPacketSender().sendMessage(
                            "Gave " + targets.getUsername() + " VIP $" + id + ".");
                }
                return true;
            }
            case "tower" -> {
                TarnTower.startTower(player);
                return true;
            }
            case "attack" -> {
                for (NpcDefinition def : NpcDefinition.definitions) {
                    if (def == null)
                        continue;
                    if (def.isAttackable()) {
                        System.out.println(def.getId() + " " + def.getName());
                    }
                }
                return true;
            }

            case "firewall" -> {
                player.getEquipment().getSlotBonuses()[Equipment.WEAPON_SLOT] = new SlotBonus(SlotEffect.FIREWALL, 1);
                player.getEquipment().refreshItems();
                return true;
            }


        }
        return false;
    }
}
