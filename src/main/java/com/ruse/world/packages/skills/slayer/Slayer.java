package com.ruse.world.packages.skills.slayer;

import com.ruse.model.Skill;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.util.Misc;
import com.ruse.world.content.KillsTracker;
import com.ruse.world.content.PlayerPanel;
import com.ruse.world.content.achievement.Achievements;
import com.ruse.world.entity.impl.player.Player;
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
    private int streak;

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
            master = SlayerMasters.SPECIAL;
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
                        resetTask(player);
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

    private void resetTask(@NotNull Player player){
        player.sendMessage("Coming Soon!");
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
            SlayerMonsters monster = monsters.stream().filter(mon -> check(player, mon.getId())).findFirst().orElse(null);
            if(monster == null){
                player.getPacketSender().sendMessage("There was an error assigning your task. Error : AST-7810");
                return;
            }
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

    public void handleGem(Player player, int id, int option){
        if(id != GEM){
            return;
        }
        switch(option){
            case 1 -> {
                if(task == null){
                    player.sendMessage("You do not have a task to check.");
                } else {
                    player.sendMessage("You have "+(task.getAmount() - task.getSlayed())+" "+NpcDefinition.forId(task.getId()).getName()+"s left to kill.");
                }
            }
            case 2 -> {
                if(task == null){
                    player.sendMessage("You do not have a task to check.");
                } else {
                    player.sendMessage("Task teleport is disabled for now");
                }
            }
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
        player.getPacketSender().sendMessage("You have completed "+streak+" tasks in a row.");

        if(player.getEquipment().hasDoubleSlayer())
            amount *= 2;

        if(player.getEquipment().contains(15586))
            amount *= 2;

        player.getInventory().add(SLAYER_TICKETS, amount);
        player.getSkillManager().addExperience(Skill.SLAYER, monsters.getXp());

        Achievements.doProgress(player, Achievements.Achievement.COMPLETE_20_SLAYER_TASKS);
        Achievements.doProgress(player, Achievements.Achievement.COMPLETE_50_SLAYER_TASKS);
        Achievements.doProgress(player, Achievements.Achievement.COMPLETE_150_SLAYER_TASKS);

        PlayerPanel.refreshPanel(player);

    }

}
