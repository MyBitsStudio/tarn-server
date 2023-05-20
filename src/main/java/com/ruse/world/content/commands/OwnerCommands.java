package com.ruse.world.content.commands;

import com.ruse.GameServer;
import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.*;
import com.ruse.model.container.impl.Shop;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.definitions.NPCDrops;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.model.definitions.WeaponInterfaces;
import com.ruse.model.projectile.ItemEffect;
import com.ruse.motivote3.doMotivote;
import com.ruse.world.World;
import com.ruse.world.content.LotterySystem;
import com.ruse.world.content.WellOfGoodwill;
import com.ruse.world.content.clan.ClanChatManager;
import com.ruse.world.content.combat.weapon.CombatSpecial;
import com.ruse.world.content.donation.DonationManager;
import com.ruse.world.content.donation.FlashDeals;
import com.ruse.world.content.grandexchange.GrandExchangeOffers;
import com.ruse.world.content.pos.PlayerOwnedShopManager;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.content.skill.SkillManager;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.content.voting.VoteBossDrop;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

public class OwnerCommands {

    public static boolean handleCommand(Player player, String command, String[] commands){

        int id, amount;
        String name;
        boolean found;

        switch(commands[0]){
            case "region":
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
            case "mypos": case "postion": case "coords":
                player.getPacketSender().sendMessage(player.getPosition().toString());
                return true;

            case "tele":
                int x = Integer.parseInt(commands[1]), y = Integer.parseInt(commands[2]);
                int z = player.getPosition().getZ();
                if (commands.length > 3)
                    z = Integer.parseInt(commands[3]);
                Position position = new Position(x, y, z);
                player.moveTo(position);
                player.getPacketSender().sendMessage("Teleporting to " + position);
                return true;

            case "delete":
                id = Integer.parseInt(commands[1]);
                for (NPC npc : World.getNpcs()) {
                    if (npc == null)
                        continue;
                    if (npc.getId() == id) {
                        World.deregister(npc);
                    }
                }
                return true;

            case "item":
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

            case "giveitem":
                id = Integer.parseInt(commands[1]);
                amount = Integer.parseInt(commands[2]);
                String plrName = command
                        .substring(commands[0].length() + commands[1].length() + commands[2].length() + 3);
                Player target = World.getPlayerByName(plrName);
                if (target == null) {
                    player.getPacketSender().sendMessage(plrName + " must be online to give them stuff!");
                } else {
                    target.getInventory().add(id, amount);
                    player.getPacketSender().sendMessage(
                            "Gave " + amount + "x " + ItemDefinition.forId(id).getName() + " to " + plrName + ".");
                }
                return true;

            case "giveall":
                id = Integer.parseInt(commands[1]);
                amount = Integer.parseInt(commands[2]);
                for (Player players : World.getPlayers()) {
                    if (players != null) {
                        players.getInventory().add(id, amount);
                        players.sendMessage(
                                "You have recieved: " + ItemDefinition.forId(id).getName() + " from "+player.getUsername()+" for being beasts.");
                    }
                }
                return true;

            case "master":
                for (Skill skill : Skill.values()) {
                    int level = SkillManager.getMaxAchievingLevel(skill);
                    player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
                            SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
                }
                player.getPacketSender().sendMessage("You are now a master of all skills.");
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                return true;

            case "findnpc":
                name = command.substring(commands[0].length() + 1);
                player.getPacketSender().sendMessage("Finding item id for item - " + name);
                found = false;
                for (int i = 0; i < NpcDefinition.getDefinitions().length; i++) {
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

            case "find":
                name = command.substring(5).toLowerCase().replaceAll("_", " ");
                player.getPacketSender().sendMessage("Finding item id for item - " + name);
                found = false;
                for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
                    if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
                        player.getPacketSender().sendMessage("Found item with name ["
                                + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
                        found = true;
                    }
                }
                if (!found) {
                    player.getPacketSender().sendMessage("No item with name [" + name + "] has been found!");
                }
                return true;

            case "god":
                if (player.isOpMode()) {
                    player.setSpecialPercentage(100);
                    CombatSpecial.updateBar(player);
                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                            player.getSkillManager().getMaxLevel(Skill.PRAYER));
                    player.getSkillManager().setCurrentLevel(Skill.ATTACK,
                            player.getSkillManager().getMaxLevel(Skill.ATTACK));
                    player.getSkillManager().setCurrentLevel(Skill.STRENGTH,
                            player.getSkillManager().getMaxLevel(Skill.STRENGTH));
                    player.getSkillManager().setCurrentLevel(Skill.DEFENCE,
                            player.getSkillManager().getMaxLevel(Skill.DEFENCE));
                    player.getSkillManager().setCurrentLevel(Skill.RANGED,
                            player.getSkillManager().getMaxLevel(Skill.RANGED));
                    player.getSkillManager().setCurrentLevel(Skill.MAGIC,
                            player.getSkillManager().getMaxLevel(Skill.MAGIC));
                    player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
                            player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
                    player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                            player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                    player.setSpecialPercentage(100);
                    player.setHasVengeance(false);
                    player.performAnimation(new Animation(860));
                    player.getPacketSender().sendMessage("You cool down, and forfeit op mode.");
                } else {
                    player.setSpecialPercentage(15000);
                    CombatSpecial.updateBar(player);
                    player.getSkillManager().setCurrentLevel(Skill.PRAYER, 150000);
                    player.getSkillManager().setCurrentLevel(Skill.ATTACK, 15000);
                    player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 15000);
                    player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 15000);
                    player.getSkillManager().setCurrentLevel(Skill.RANGED, 15000);
                    player.getSkillManager().setCurrentLevel(Skill.MAGIC, 15000);
                    player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 150000);
                    player.getSkillManager().setCurrentLevel(Skill.SUMMONING, 15000);
                    player.setHasVengeance(true);
                    player.performAnimation(new Animation(725));
                    player.performGraphic(new Graphic(1555));
                    player.getPacketSender().sendMessage("You're on op mode now, and everyone knows it.");
                }
                player.setOpMode(!player.isOpMode());
                return true;

            case "update":
                int time = Integer.parseInt(commands[1]);
                if (time > 0) {
                    GameServer.setUpdating(true);
                    World.sendStaffMessage("<col=FF0066><img=2> [SERVER]<col=6600FF> " + player.getUsername()
                            + " just started an update in " + time + " ticks.");
                    World.sendStaffMessage("<col=FF0066><img=2> [SERVER]<col=6600FF> Please finish what you are doing now!");
                    for (Player players : World.getPlayers()) {
                        if (players == null)
                            continue;
                        players.getPacketSender().sendSystemUpdate(time);
                    }
                    TaskManager.submit(new Task(time) {
                        int tick = 0;
                        @Override
                        protected void execute() {
                            switch(tick){
                                case 1:
                                    World.sendStaffMessage("<col=FF0066><img=2> [SERVER]<col=6600FF> Server is shutting down now!");
                                    break;

                                case 3:
                                    World.sendStaffMessage("<col=FF0066><img=2> [SERVER]<col=6600FF> Wait until announcement to login again!");
                                    break;

                                case 6:
                                    World.sendStaffMessage("<col=FF0066><img=2> [SERVER]<col=6600FF> Updating now! See you soon!");
                                    break;

                                case 10:
                                    for (Player player : World.getPlayers()) {
                                        if (player != null) {
                                            World.deregister(player);
                                        }
                                    }
                                    WellOfGoodwill.save();
                                    GrandExchangeOffers.save();
                                    ClanChatManager.save();
                                    PlayerOwnedShopManager.saveShops();
                                    Shop.ShopManager.saveTaxShop();
                                    LotterySystem.saveTickets();
                                    ServerPerks.getInstance().save();
                                    GameServer.getLogger().info("Update task finished! Shutting off...");

                                    break;

                                case 15:
                                    System.exit(0);
                                    stop();
                                    break;
                            }
                            tick++;
                        }
                    });
                }
                return true;

            case "flashdeal":
                FlashDeals.getDeals().reload();
                World.sendStaffMessage("<img=5> @blu@[DEALS] Flash Deals are active. Check them out now!");
                for (Player players : World.getPlayers()) {
                    if (players == null) {
                        continue;
                    }
                    players.getPacketSender().sendBroadCastMessage("FLASH DEALS ARE ACTIVE! CHECK THEM OUT BEFORE THEY ARE GONE!", 300);
                }
                return true;

            case "add":
                if(commands.length >= 2){
                    switch(commands[1]){
                        case "donate":
                            amount = Integer.parseInt(commands[1]);
                            DonationManager.getInstance().addToTotalDonation(amount);
                            return true;

                        case "vote":
                            amount = Integer.parseInt(commands[1]);
                            doMotivote.setVoteCount(doMotivote.getVoteCount() + amount);
                            VoteBossDrop.save();

                            if (doMotivote.getVoteCount() >= 50) {
                                VoteBossDrop.handleSpawn();
                            }
                            return true;
                    }
                } else {
                    player.getPacketSender().sendMessage("Use as ::add [donate/vote] [amount]");
                }
                return true;

            case "reload":
                if(commands.length >= 2){
                    switch(commands[1]){
                        case "all":
                           Shop.ShopManager.parseShops().load();
                            NPCDrops.parseDrops().load();
                            ItemDefinition.init();
                            WeaponInterfaces.parseInterfaces().load();
                            NpcDefinition.parseNpcs().load();
                            WeaponInterfaces.init();
                            player.sendMessage("Reloaded all definitions.");
                            return true;

                        case "some":
                            NpcDefinition.parseNpcs().load();
                            ItemDefinition.init();
                            NPCDrops.parseDrops().load();
                            Shop.ShopManager.parseShops().load();
                            return true;

                        case "shops":
                            Shop.ShopManager.parseShops().load();
                            return true;
                    }
                }
                return true;

            case "saveall":
                World.savePlayers();
                return true;

            case "givesitem":
                int ids = Integer.parseInt(commands[1]);
                ItemEffect effect = ItemEffect.getEffectForName(commands[2]);
                int bonus = Integer.parseInt(commands[3]);
                Player targets = World.getPlayerByName(command.substring(commands[0].length() + commands[1].length() + commands[2].length() + 3));
                if (targets == null) {
                    player.getPacketSender().sendMessage(" must be online to give them stuff!");
                } else {
                    targets.getInventory().add(new Item(ids, 1, effect, bonus));
                    player.getPacketSender().sendMessage(
                            "Gave " + 1 + "x " + ItemDefinition.forId(ids).getName() + " to " + targets.getUsername() + " with effect "+effect.name()+" and bonus "+bonus+".");
                }
                return true;

            case "testdono":

                return true;
        }
        return false;
    }
}
