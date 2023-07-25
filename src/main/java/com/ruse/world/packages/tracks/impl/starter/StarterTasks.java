package com.ruse.world.packages.tracks.impl.starter;

import com.ruse.world.packages.tracks.impl.TaskReward;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum StarterTasks {

    TASK_001(0, 5, "Kill 1 Blurite Goblin", new TaskReward(23200, 1)
    , 9837, 1),
    TASK_002(0, 25, "Kill 50 Blurite Goblins", new TaskReward(12852, 50)
            , 9837, 50),
    TASK_003(0, 5, "Kill 1 Blurite Orc", new TaskReward(23200, 1)
            , 9027, 1),
    TASK_004(0, 25,"Kill 50 Blurite Orcs", new TaskReward(12852, 50)
            , 9027, 50),
    TASK_005(0, 5, "Kill 1 Blurite Demon", new TaskReward(23200, 1)
            , 9835, 1),
    TASK_006(0, 25,"Kill 50 Blurite Demons", new TaskReward(12852, 50)
            , 9835, 50),
    TASK_007(0, 5, "Kill 1 Blurite Werewolf", new TaskReward(23200, 1)
            , 9911, 1),
    TASK_008(0, 25,"Kill 50 Blurite Werewolves", new TaskReward(12852, 50)
            , 9911, 50),
    TASK_009(0, 5, "Kill 1 Blurite Centaur", new TaskReward(23200, 1)
            , 9922, 1),
    TASK_010(0, 25,"Kill 50 Blurite Centaurs", new TaskReward(12852, 50)
            , 9922, 50),
    TASK_011(0, 5,"Kill 1 Zinqrux", new TaskReward(23201, 1)
            , 8014, 1),
    TASK_012(0, 25,"Kill 50 Zinqrux", new TaskReward(12852, 50)
            , 8014, 50),
    TASK_013(0, 5,"Kill 1 Dr.Aberrant", new TaskReward(23201, 1)
            , 8003, 1),
    TASK_014(0, 25,"Kill 50 Dr.Aberrant", new TaskReward(12852, 50)
            , 8003, 50),
    TASK_015(0, 5,"Kill 1 Nagenda", new TaskReward(23201, 1)
            , 811, 1),
    TASK_016(0, 25,"Kill 50 Nagenda", new TaskReward(12852, 50)
            , 811, 50),
    TASK_017(0, 5,"Kill 1 Yisdar", new TaskReward(23201, 1)
            , 9817, 1),
    TASK_018(0, 25,"Kill 50 Yisdar", new TaskReward(12852, 50)
            , 9817, 50),
    TASK_019(0, 5,"Kill 1 Doomwatcher", new TaskReward(23202, 1)
            , 9836, 1),
    TASK_020(0, 25,"Kill 50 Doomwatcher", new TaskReward(12852, 50)
            , 9836, 50),
    TASK_021(0, 5,"Kill 1 Maze Guardian", new TaskReward(23202, 1)
            , 92, 1),
    TASK_022(0, 25,"Kill 50 Maze Guardian", new TaskReward(12852, 50)
            , 92, 50),
    TASK_023(0, 5,"Kill 1 Miscreation", new TaskReward(23202, 1)
            , 3313, 1),
    TASK_024(0, 25,"Kill 50 Miscreation", new TaskReward(12852, 50)
            , 3313, 50),
    TASK_025(0, 5,"Kill 1 Zorbak", new TaskReward(23202, 1)
            , 1906, 1),
    TASK_026(0, 25,"Kill 50 Zorbak", new TaskReward(12852, 50)
            , 1906, 50),

    TASK_050(1, 10,"Gain 1 99 Skill", new TaskReward(995, 10000)
            , -1, -1),
    TASK_051(1, 50, "Gain 10 99 Skills", new TaskReward(15004, 1)
            , -1, -1),
    TASK_052(1, 150,"Gain All 99 Skills", new TaskReward(15004, 5)
            , -1, -1),

    TASK_100(2, 10,"Kill 1 Avalon", new TaskReward(995, 1000)
            , 9025, 1),
    TASK_101(2, 40,"Kill 100 Avalons", new TaskReward(995, 1000)
            , 9025, 100),
    TASK_102(2, 10,"Kill 1 Eragon", new TaskReward(995, 1000)
            , 9026, 1),
    TASK_103(2, 40,"Kill 100 Eragons", new TaskReward(995, 1000)
            , 9026, 100),
    TASK_104(2, 10,"Kill 1 Avatar Titan", new TaskReward(995, 1000)
            , 8008, 1),
    TASK_105(2, 40,"Kill 100 Avatar Titans", new TaskReward(995, 1000)
            , 8008, 100),
    TASK_106(2, 10,"Kill 1 Emerald Slayer", new TaskReward(995, 1000)
            , 2342, 1),
    TASK_107(2, 40,"Kill 100 Emerald Slayer", new TaskReward(995, 1000)
            , 2342, 100),
    TASK_108(2, 10,"Kill 1 Mutant Hydra", new TaskReward(995, 1000)
            , 9839, 1),
    TASK_109(2, 40,"Kill 100 Mutant Hydra", new TaskReward(995, 1000)
            , 9839, 100),
    TASK_110(2, 10,"Kill 1 Gorvek", new TaskReward(995, 1000)
            , 9806, 1),
    TASK_111(2, 40,"Kill 100 Gorvek", new TaskReward(995, 1000)
            , 9806, 100),
    TASK_112(2, 10,"Kill 1 Onyx Griffin", new TaskReward(995, 1000)
            , 1746, 1),
    TASK_113(2, 40,"Kill 100 Onyx Griffin", new TaskReward(995, 1000)
            , 1746, 100),

    TASK_150(3, 10,"Clear LVL 5 Tarn Tower", new TaskReward(995, 1000)
            , -1, -1),
    TASK_151(3, 50,"Clear LVL 10 Tarn Tower", new TaskReward(995, 1000)
            , -1, -1),
    TASK_152(3, 100,"Clear LVL 20 Tarn Tower", new TaskReward(995, 1000)
            , -1, -1),

    TASK_200(4, 5,"Vote 5 Times", new TaskReward(995, 1000)
            , -1, -1),
    TASK_201(4, 50,"Vote 50 Times", new TaskReward(995, 1000)
            , -1, -1),
    TASK_202(4, 100,"Vote 100 Times", new TaskReward(995, 1000)
            , -1, -1)
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

    public static List<StarterTasks> byKills(int npc){
        List<StarterTasks> tasks = new ArrayList<>();
        for(StarterTasks task : StarterTasks.values()){
            if(task.getNpc() == -1)
                continue;
            if(task.getNpc() == npc){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static StarterTasks byNPC(int npc){
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
