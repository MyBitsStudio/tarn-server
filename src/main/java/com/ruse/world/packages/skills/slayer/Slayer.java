package com.ruse.world.packages.skills.slayer;

import com.ruse.model.Skill;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.KillsTracker;
import com.ruse.world.content.PlayerPanel;
import com.ruse.world.packages.serverperks.ServerPerks;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.CombatConstants;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.dialogue.impl.slayer.ResetTask;
import com.ruse.world.packages.event.impl.SlayerBonusEvent;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import com.ruse.world.packages.johnachievementsystem.PerkType;
import com.ruse.world.packages.shops.ShopHandler;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Slayer {

    @Getter
    @Setter
    private SlayerTask task, lastTask;
    @Getter
    @Setter
    private SlayerMasters master = SlayerMasters.EASY;
    @Getter
    @Setter
    private int streak, total;

    private static int SLAYER_TICKETS = 5023, GEM = 4155, RESET_TOKEN = 9719;

    public Slayer(){
        this.streak = 0;
    }

    public void assignTask(Player player, boolean force){
        if(task != null){
            player.getPacketSender().sendMessage("You already have a task assigned.");
            return;
        }

        if(!force)
            assignMaster(player);

        SlayerMonsters monsters = SlayerMonsters.randomForCategory(master.getCategory());
        if(monsters == null){
            player.getPacketSender().sendMessage("There was an error assigning your task. Error : AST-1129");
            return;
        }
        if(!check(player, monsters.getId())){
            signalCheck(player);
            return;
        }
        task = new SlayerTask(monsters.getId(), Misc.random(monsters.getRanges()[0], monsters.getRanges()[1]));
        player.getPacketSender().sendMessage("You have been assigned to kill "+task.getAmount()+" "+ NpcDefinition.forId(monsters.getId()).getName() +"s.");
        PlayerPanel.refreshPanel(player);
    }

    private void assignMaster(@NotNull Player player){
        int level = player.getSkillManager().getCurrentLevel(Skill.SLAYER);
        if(level >= 99){
            master = SlayerMasters.ELITE;
        } else if(level >= 92){
            master = SlayerMasters.ELITE;
        } else if(level >= 76){
            master = SlayerMasters.HARD;
        } else if(level >= 40){
            master = SlayerMasters.MEDIUM;
        } else {
            master = SlayerMasters.EASY;
        }
    }

    public boolean handleButtons(Player player, int button){
        switch(button){
                case 166208 -> {
                    if(task == null){
                        player.getPacketSender().sendInterfaceRemoval();
                        assignTask(player, false);
                    } else {
                        player.getPacketSender().sendInterfaceRemoval();
                        DialogueManager.sendDialogue(player, new ResetTask(player), 9000);
                    }
                    return true;
                }
                case 166210 -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    ShopHandler.getShop(2).ifPresent(shop -> shop.send(player, true));
                    return true;
                }
                case 166212 -> {
                    player.sendMessage("Coming Soon!");
                    return true;
                }
        }
        return false;
    }

    public void resetTask(@NotNull Player player){
        if(task == null){
            player.getPacketSender().sendMessage("You do not have a task to reset.");
            return;
        }
        if(player.getInventory().contains(RESET_TOKEN)){
            player.getInventory().delete(RESET_TOKEN, 1);
            player.getPacketSender().sendMessage("You have reset your task.");
            lastTask = task;
            task = null;
            streak = 0;
        } else {
            player.getPacketSender().sendMessage("You need a reset token to reset your task.");
        }
    }

    public void sendInterface(@NotNull Player player){
        player.getPacketSender().sendInterface(166200);
        player.getPacketSender().sendString(166206, task == null ? "NO TASK" : NpcDefinition.forId(task.getId()).getName());
        player.getPacketSender().sendNpcOnInterface(166207, task == null ? 1 : task.getId(), 1325);
        player.getPacketSender().sendTooltip(166208, task == null ? "Assign": "Reset");
        player.getPacketSender().sendString(166209, task == null ? "Assign" : "Reset");
    }

    private boolean check(Player player, int id){
        return KillsTracker.getTotalKillsForNpc(id, player) > 1;
    }

    private void signalCheck(Player player){
        List<SlayerMonsters> monsters = SlayerMonsters.forCategory(master.getCategory());
        boolean canAssign = monsters.stream().anyMatch(monster -> check(player, monster.getId()));
        if(canAssign) {
            List<SlayerMonsters> monstera = monsters.stream().filter(mon -> check(player, mon.getId())).toList();
            if(monstera.isEmpty()){
                player.getPacketSender().sendMessage("There was an error assigning your task. Error : AST-7810");
                return;
            }
            SlayerMonsters monster = monstera.get(Misc.random(monstera.size() - 1));
            task = new SlayerTask(monster.getId(), Misc.random(monster.getRanges()[0], monster.getRanges()[1]));
            player.getPacketSender().sendMessage("You have been assigned to kill "+task.getAmount()+" "+ NpcDefinition.forId(monster.getId()).getName() +"s.");
        } else if (master.getCategory() == 0) {
            task = new SlayerTask(SlayerMonsters.BLURITE_GOBLIN.getId(), 10);
            player.getPacketSender().sendMessage("You have been assigned to kill "+task.getAmount()+" "+ NpcDefinition.forId(SlayerMonsters.BLURITE_GOBLIN.getId()).getName() +"s.");
        } else {
            SlayerMasters master = SlayerMasters.forCategory(this.master.getCategory() - 1);
            if (master == null) {
                player.getPacketSender().sendMessage("There was an error assigning your task. Error : AST-3250");
                return;
            }
            this.master = master;
            player.getPacketSender().sendMessage("You've been assigned a new master.");
            assignTask(player, true);
        }
    }

    public void handleSlayerTask(Player player, int id){
        if(this.task != null) {
            if (id == task.getId()) {
                task.increment();
                if (task.getAmount() <= task.getSlayed()) {
                    player.getPacketSender().sendMessage("You have completed your slayer task.");
                    lastTask = task;
                    task = null;
                    handleDeath(player, SlayerMonsters.forId(id));
                }
            }
        }
    }

    private void handleDeath(@NotNull Player player, @NotNull SlayerMonsters monsters){
        int amount = monsters.getTickets();

        streak++;
        total++;
        player.getPacketSender().sendMessage("You have completed "+streak+" tasks in a row.");

        if(player.getEquipment().hasDoubleSlayer())
            amount *= 2;

        if(player.getEquipment().contains(15586))
            amount *= 2;

        if(player.getEquipment().contains(21570))
            amount *= 2;

        if(player.getEquipment().contains(20661))
            amount *= 2;

        if(player.getEquipment().contains(19892))
            amount *= 1.10;

        if(ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.SLAYER ||
                ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.MEGA_PERK){
            amount *= 2;
        }

        amount = handleEquipmentBonuses(player, amount);

        if(World.handler.eventActive("SlayerBonus")) {
            if (World.attributes.getSetting("slayer-bonus")) {
                SlayerBonusEvent event = (SlayerBonusEvent) World.handler.getEvent("SlayerBonus");
                amount *= (1 + (event.getBonus() / 100));
            }
        }

        if(AchievementHandler.hasUnlocked(player, PerkType.SKILLING)){
            amount *= (1 + ((AchievementHandler.getPerkLevel(player, PerkType.SKILLING) * 5) / 100));
        }

        player.getInventory().add(SLAYER_TICKETS, amount);
        player.getSkillManager().addExperience(Skill.SLAYER, monsters.getXp());

        randomBox(player);

        player.getTarnNormal().handleSlayerTasks(total);
        player.getTarnElite().handleSlayerTasks(total);

        player.getSeasonPass().incrementExp(2650, false);

        AchievementHandler.progress(player, 1, 45, 47, 48, 69, 70, 90, 91);

        PlayerPanel.refreshPanel(player);

    }

    private void randomBox(Player player){
        if(Misc.random(100) <= 16){
            player.getInventory().add(2734, 1);
            player.getPacketSender().sendMessage("@yel@You have received a Slayer Casket.");
        }
        if(Misc.random(1000) >= 987){
            player.getInventory().add(25102, 1);
            player.getPacketSender().sendMessage("@yel@You got lucky and received a Locked Slayer Chest.");
        }
    }

    private int handleEquipmentBonuses(Player player, int amount){
        if(CombatConstants.wearingSlayerArmor(player)){
            amount *= 2;
        } else if(CombatConstants.wearingAnySlayer(player)){
            amount *= (int) CombatConstants.multiply(player);
        }
        return amount;
    }

    public void upgradeHelmet(Player player, int item){
        switch(item){
            case 13263 -> {
                if(player.getInventory().contains(4155, 5) &&
                player.getInventory().contains(5023, 500)){
                    player.getInventory().delete(4155, 5).delete(5023, 500).
                            delete(13263, 1).add(21075, 1);
                    player.getPacketSender().sendMessage("You have upgraded your Slayer Helmet to a Slayer Helmet [T1].");
                } else {
                    player.getPacketSender().sendMessage("You need 5 Slayer Gems and 500 Slayer Tickets to upgrade.");
                }
            }
            case 21075 -> {
                if(player.getInventory().contains(4155, 10) &&
                        player.getInventory().contains(5023, 1500)){
                    player.getInventory().delete(4155, 10).delete(5023, 1500).
                            delete(21075, 1).add(21076, 1);
                    player.getPacketSender().sendMessage("You have upgraded your Slayer Helmet to a Slayer Helmet [T2].");
                } else {
                    player.getPacketSender().sendMessage("You need 10 Slayer Gems and 1500 Slayer Tickets to upgrade.");
                }
            }
            case 21076 -> {
                if(player.getInventory().contains(4155, 20) &&
                        player.getInventory().contains(5023, 2500)){
                    player.getInventory().delete(4155, 20).delete(5023, 2500).
                            delete(21076, 1).add(21077, 1);
                    player.getPacketSender().sendMessage("You have upgraded your Slayer Helmet to a Slayer Helmet [T3].");
                } else {
                    player.getPacketSender().sendMessage("You need 20 Slayer Gems and 2500 Slayer Tickets to upgrade.");
                }
            }
            case 21077 -> {
                if(player.getInventory().contains(4155, 40) &&
                        player.getInventory().contains(5023, 5000)){
                    player.getInventory().delete(4155, 40).delete(5023, 5000).
                            delete(21077, 1).add(21078, 1);
                    player.getPacketSender().sendMessage("You have upgraded your Slayer Helmet to a Slayer Helmet [T4].");
                } else {
                    player.getPacketSender().sendMessage("You need 40 Slayer Gems and 5000 Slayer Tickets to upgrade.");
                }
            }
            case 21078 -> {
                if(player.getInventory().contains(4155, 100) &&
                        player.getInventory().contains(5023, 25000)){
                    player.getInventory().delete(4155, 100).delete(5023, 25000).
                            delete(21078, 1).add(21079, 1);
                    player.getPacketSender().sendMessage("You have upgraded your Slayer Helmet to a Slayer Helmet [MAX].");
                } else {
                    player.getPacketSender().sendMessage("You need 100 Slayer Gems and 25000 Slayer Tickets to upgrade.");
                }
            }
        }
    }

}
