package com.ruse.world.packages.tracks.impl.starter;

import com.ruse.world.content.KillsTracker;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.tracks.Track;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StarterTrack extends Track {

    @Getter
    private final Map<StarterTasks, Boolean> tasks = new HashMap<>();

    public StarterTrack(Player player) {
        super(player);
        setUp();
    }

    public void setTasks(@NotNull Map<StarterTasks, Boolean> tasks) {
        for(StarterTasks task : tasks.keySet())
            if(task != null)
                if(this.tasks.containsKey(task))
                    this.tasks.put(task, tasks.get(task));
    }

    private void setUp(){
        this.rewards = new StarterTrackRewards();
        this.hasPremium = false;
        this.position = 0;
        this.maxLevel = 341;
        for(StarterTasks task : StarterTasks.values())
            tasks.put(task, false);
    }

    public boolean completed(StarterTasks task){
        return tasks.containsKey(task) && tasks.get(task);
    }

    @Override
    public void sendInterfaceList(){
        List<StarterTasks> tasks = null;
        switch(Integer.parseInt(player.getVariables().getInterfaceSettings()[2])) {
            case 0 -> tasks = StarterTasks.getMonsterTasks();
            case 1 -> tasks = StarterTasks.getSkillTasks();
            case 2 -> tasks = StarterTasks.getBossTasks();
            case 3 -> tasks = StarterTasks.getMiniTasks();
            case 4 -> tasks = StarterTasks.getMiscTasks();
            default -> player.getPacketSender().sendMessage("Invalid task type.");
        }

        if(tasks == null)
            return;

        for (int i = 0; i < Objects.requireNonNull(tasks).size(); i++) {
            StarterTasks task = tasks.get(i);
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

    public void handleKillCount(int npcId) {
        List<StarterTasks> taska = StarterTasks.byKills(npcId);
        for(StarterTasks task : taska) {
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
            }
        }
    }

    public void handleSkillCount(int amount){
        List<StarterTasks> tasks = StarterTasks.getSkillTasks();
        for(StarterTasks task : tasks){
            if(task != null){
                if(!this.tasks.containsKey(task))
                    this.tasks.put(task, false);
                if(!this.tasks.get(task)){
                    if(amount >= task.getCount()){
                        this.tasks.put(task, true);
                        reward(task);
                        addXP(task.getXp());
                    }
                }
            }
        }
    }

    public void handleTower(int level){
        List<StarterTasks> tasks = StarterTasks.getMiniTasks();
        for(StarterTasks task : tasks){
            if(task != null){
                if(!this.tasks.containsKey(task))
                    this.tasks.put(task, false);
                if(!this.tasks.get(task)){
                    if(level >= task.getCount()){
                        this.tasks.put(task, true);
                        reward(task);
                        addXP(task.getXp());
                    }
                }
            }
        }
    }

    public void handleVote(int votes){
        List<StarterTasks> tasks = StarterTasks.getMiscTasks();
        for(StarterTasks task : tasks){
            if(task != null){
                if(!this.tasks.containsKey(task))
                    this.tasks.put(task, false);
                if(!this.tasks.get(task)){
                    if(votes >= task.getCount()){
                        this.tasks.put(task, true);
                        reward(task);
                        addXP(task.getXp());
                    }
                }
            }
        }
    }



    private void addXP(int amount){
        this.xp += amount;
        if(xp >= maxLevel){
            int left = xp - maxLevel;
            position++;
            xp = left;
        }
    }

    private void reward(@NotNull StarterTasks task){
        player.getInventory().addDropIfFull(task.getReward().item(),
                task.getReward().amount());
        player.sendMessage("@gre@ You have completed a starter task and received a reward!");
    }

    public int getCompleted(){
        int completed = 0;
        for(StarterTasks task : tasks.keySet())
            if(tasks.get(task))
                completed++;
        return completed;
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

}
