package com.ruse.world.packages.tracks.impl.tarn.normal;

import com.ruse.world.content.KillsTracker;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import com.ruse.world.packages.tracks.Track;
import com.ruse.world.packages.tracks.impl.starter.StarterTasks;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public class TarnNormalTrack extends Track {

    private final Map<TarnNormalTasks, Boolean> tasks = new HashMap<>();

    private int level = 0;

    public TarnNormalTrack(Player player) {
        super(player);
        setUp();
    }

    public void setTasks(Map<TarnNormalTasks, Boolean> tasks) {
        for(TarnNormalTasks task : tasks.keySet())
            if(task != null)
                if(this.tasks.containsKey(task))
                    this.tasks.put(task, tasks.get(task));
    }

    public boolean completed(TarnNormalTasks task){
        return tasks.containsKey(task) && tasks.get(task);
    }

    @Override
    public void sendInterfaceList() {
        List< TarnNormalTasks> tasks = null;
        switch(Integer.parseInt(player.getVariables().getInterfaceSettings()[2])) {
            case 0 -> tasks =  TarnNormalTasks.getMonsterTasks();
            case 1 -> tasks =  TarnNormalTasks.getSkillTasks();
            case 2 -> tasks =  TarnNormalTasks.getBossTasks();
            default -> player.getPacketSender().sendMessage("Invalid task type.");
        }

        if(tasks == null)
            return;

        for (int i = 0; i < Objects.requireNonNull(tasks).size(); i++) {
            TarnNormalTasks task = tasks.get(i);
            if (this.tasks.containsKey(task)) {
                if (this.tasks.get(task)) {
                    player.getPacketSender().sendString(161201 + i, "@gre@" + task.getTaskName());
                } else {
                    player.getPacketSender().sendString(161201 + i,"@red@" + task.getTaskName());
                }
            } else {
                player.getPacketSender().sendString(161201 + i, "@red@" +task.getTaskName());
            }
        }
    }

    @Override
    public void setUp() {
        this.rewards = new TarnNormalTrackRewards();
        this.hasPremium = false;
        this.position = 0;
        this.maxLevel = 115;
        for(TarnNormalTasks task : TarnNormalTasks.values())
            tasks.put(task, false);
    }

    public int getTotal(){
        return tasks.size();
    }

    public int getProgress(){
        return (int) (((double) getCompleted() / (double) getTotal()) * 100);
    }

    public int nonCompleted(){
        return getTotal() - getCompleted();
    }

    public int getCompleted(){
        int completed = 0;
        for(TarnNormalTasks task : tasks.keySet())
            if(tasks.get(task))
                completed++;
        return completed;
    }

    private void addXP(int amount){
        this.xp += amount;
        if(xp >= maxLevel){
            int left = xp - maxLevel;
            position++;
            xp = left;
        }
    }

    private void reward(@NotNull TarnNormalTasks task){
        player.getSeasonPass().incrementExp(95, false);
        player.getInventory().addDropIfFull(task.getReward().item(),
                task.getReward().amount());
        player.sendMessage("@gre@ You have completed a Tarn Normal task and received a reward!");
        AchievementHandler.progress(player, 1, 11, 12);
        player.save();
    }

    public void handleKillCount(int npcId) {
        List<TarnNormalTasks> taska = TarnNormalTasks.byKills(npcId);
        for(TarnNormalTasks task : taska) {
            if (task != null) {
                if (!tasks.containsKey(task))
                    tasks.put(task, false);
                if (!tasks.get(task)) {
                    if (KillsTracker.getTotalKillsForNpc(npcId, player) >= task.getCount()) {
                        tasks.put(task, true);
                        reward(task);
                        addXP(task.getXp());
                    }
                }
                getPlayer().save();
            }
        }
    }

    public void handleSlayerTasks(int amount) {
        List<TarnNormalTasks> taska = TarnNormalTasks.slayers();
        for(TarnNormalTasks task : taska) {
            if (task != null) {
                if (!tasks.containsKey(task))
                    tasks.put(task, false);
                if (!tasks.get(task)) {
                    if (amount >= task.getCount()) {
                        tasks.put(task, true);
                        reward(task);
                        addXP(task.getXp());
                    }
                }
                getPlayer().save();
            }
        }
    }

    public void handleDissolveTasks(int amount) {
        List<TarnNormalTasks> taska = TarnNormalTasks.dissolve();
        for(TarnNormalTasks task : taska) {
            if (task != null) {
                if (!tasks.containsKey(task))
                    tasks.put(task, false);
                if (!tasks.get(task)) {
                    if (amount >= task.getCount()) {
                        tasks.put(task, true);
                        reward(task);
                        addXP(task.getXp());
                    }
                }
                getPlayer().save();
            }
        }
    }
}
