package com.ruse.world.packages.tracks.impl.starter;

import com.ruse.world.packages.tracks.impl.TaskReward;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum StarterTasks {

    TASK_001(0, 5, "Kill 1 Blurite Goblin", new TaskReward(995, 1000)
    , 9837, 1),
    TASK_002(0, 25, "Kill 50 Blurite Goblins", new TaskReward(995, 1000)
            , 9837, 50),
    TASK_003(0, 5, "Kill 1 Blurite Orc", new TaskReward(995, 1000)
            , 9027, 1),
    TASK_004(0, 25,"Kill 50 Blurite Orcs", new TaskReward(995, 1000)
            , 9027, 50),
    TASK_005(0, 5, "Kill 1 Blurite Demon", new TaskReward(995, 1000)
            , 9835, 1),
    TASK_006(0, 25,"Kill 50 Blurite Demons", new TaskReward(995, 1000)
            , 9835, 50),
    TASK_007(0, 5, "Kill 1 Blurite Werewolf", new TaskReward(995, 1000)
            , 9911, 1),
    TASK_008(0, 25,"Kill 50 Blurite Werewolves", new TaskReward(995, 1000)
            , 9911, 50),
    TASK_009(0, 5, "Kill 1 Blurite Centaur", new TaskReward(995, 1000)
            , 9922, 1),
    TASK_010(0, 25,"Kill 50 Blurite Centaurs", new TaskReward(995, 1000)
            , 9922, 50),

    TASK_050(1, 10,"Gain 1 99 Skill", new TaskReward(995, 1000)
            , -1, -1),
    TASK_051(1, 50, "Gain 10 99 Skills", new TaskReward(995, 1000)
            , -1, -1),
    TASK_052(1, 150,"Gain All 99 Skills", new TaskReward(995, 1000)
            , -1, -1),

    TASK_100(2, 10,"Kill 1 Avalon", new TaskReward(995, 1000)
            , -1, -1),
    TASK_101(2, 40,"Kill 100 Avalons", new TaskReward(995, 1000)
            , -1, -1),

    TASK_151(3, 10,"Clear LVL 5 Tarn Tower", new TaskReward(995, 1000)
            , -1, -1),

    TASK_200(4, 5,"Vote 5 Times", new TaskReward(995, 1000)
            , -1, -1),
    TASK_201(4, 50,"Vote 50 Times", new TaskReward(995, 1000)
            , -1, -1),
    TASK_202(4, 100,"Vote 100 Times", new TaskReward(995, 1000)
            , -1, -1),
    TASK_203(4, 50,"Donate 10$", new TaskReward(995, 1000)
            , -1, -1),
    TASK_204(4, 250,"Donate 50$", new TaskReward(995, 1000)
            , -1, -1),
    TASK_205(4, 500,"Donate 100$", new TaskReward(995, 1000)
            , -1, -1),
    ;
    private final int category, xp, npc, count;
    private final String taskName;
    private final TaskReward reward;

    StarterTasks(int category, int xp, String name, TaskReward reward, int npc, int count){
        this.category = category;
        this.xp = xp;
        this.taskName = name;
        this.reward = reward;
        this.npc = npc;
        this.count = count;
    }

    public static @NotNull List<StarterTasks> getMonsterTasks(){
        List<StarterTasks> tasks = new ArrayList<>();
        for(StarterTasks task : StarterTasks.values()){
            if(task.getCategory() == 0){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static @NotNull List<StarterTasks> getSkillTasks(){
        List<StarterTasks> tasks = new ArrayList<>();
        for(StarterTasks task : StarterTasks.values()){
            if(task.getCategory() == 1){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static @NotNull List<StarterTasks> getMiniTasks(){
        List<StarterTasks> tasks = new ArrayList<>();
        for(StarterTasks task : StarterTasks.values()){
            if(task.getCategory() == 3){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static @NotNull List<StarterTasks> getBossTasks(){
        List<StarterTasks> tasks = new ArrayList<>();
        for(StarterTasks task : StarterTasks.values()){
            if(task.getCategory() == 2){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static @NotNull List<StarterTasks> getMiscTasks(){
        List<StarterTasks> tasks = new ArrayList<>();
        for(StarterTasks task : StarterTasks.values()){
            if(task.getCategory() == 4){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static @Nullable StarterTasks getByIndexAndCategory(int category, int index){
        List<StarterTasks> tasks = new ArrayList<>();
        for(StarterTasks task : StarterTasks.values()){
            if(task.getCategory() == category){
                tasks.add(task);
            }
        }
        if(tasks.size() > index){
            return tasks.get(index);
        }
        return null;
    }

    public static @Nullable StarterTasks byKills(int npc){
        for(StarterTasks task : StarterTasks.values()){
            if(task.getNpc() == -1)
                continue;
            if(task.getNpc() == npc){
                return task;
            }
        }
        return null;
    }
}
