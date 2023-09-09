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
    TASK_027(0, 5,"Kill 1 Unicorn", new TaskReward(23203, 1)
            , 1742, 1),
    TASK_028(0, 25,"Kill 50 Unicorn", new TaskReward(12852, 50)
            , 1742, 50),
    TASK_029(0, 5,"Kill 1 Hyndra", new TaskReward(23203, 1)
            , 1743, 1),
    TASK_030(0, 25,"Kill 50 Hyndra", new TaskReward(12852, 50)
            , 1743, 50),
    TASK_031(0, 5,"Kill 1 Grooter", new TaskReward(23203, 1)
            , 1744, 1),
    TASK_032(0, 25,"Kill 50 Grooter", new TaskReward(12852, 50)
            , 1744, 50),
    TASK_033(0, 5,"Kill 1 Turtle", new TaskReward(23203, 1)
            , 1745, 1),
    TASK_034(0, 25,"Kill 50 Turtle", new TaskReward(12852, 50)
            , 1745, 50),
    TASK_035(0, 5,"Kill 1 Toad", new TaskReward(23204, 1)
            , 1738, 1),
    TASK_036(0, 25,"Kill 50 Toad", new TaskReward(12852, 50)
            , 1738, 50),
    TASK_037(0, 5,"Kill 1 Cloud", new TaskReward(23204, 1)
            , 1739, 1),
    TASK_038(0, 25,"Kill 50 Cloud", new TaskReward(12852, 50)
            , 1739, 50),
    TASK_039(0, 5,"Kill 1 Moss", new TaskReward(23204, 1)
            , 1740, 1),
    TASK_040(0, 25,"Kill 50 Moss", new TaskReward(12852, 50)
            , 1740, 50),
    TASK_041(0, 5,"Kill 1 Fire", new TaskReward(23204, 1)
            , 1741, 1),
    TASK_042(0, 25,"Kill 50 Fire", new TaskReward(12852, 50)
            , 1741, 50),

    TASK_050(1, 10,"Gain 1 99 Skill", new TaskReward(995, 100000)
            , -1, 1),
    TASK_051(1, 50, "Gain 10 99 Skills", new TaskReward(15004, 1)
            , -1, 10),
    TASK_052(1, 150,"Gain All 99 Skills", new TaskReward(15004, 5)
            , -1, 99),

    TASK_100(2, 10,"Kill 1 Avalon", new TaskReward(23203, 1)
            , 9025, 1),
    TASK_101(2, 40,"Kill 100 Avalons", new TaskReward(20500, 1)
            , 9025, 100),
    TASK_102(2, 10,"Kill 1 Eragon", new TaskReward(23203, 2)
            , 9026, 1),
    TASK_103(2, 40,"Kill 100 Eragons", new TaskReward(20500, 2)
            , 9026, 100),
    TASK_104(2, 10,"Kill 1 Avatar Titan", new TaskReward(23204, 1)
            , 8008, 1),
    TASK_105(2, 40,"Kill 100 Avatar Titans", new TaskReward(20500, 3)
            , 8008, 100),
    TASK_106(2, 10,"Kill 1 Emerald Slayer", new TaskReward(23204, 2)
            , 2342, 1),
    TASK_107(2, 40,"Kill 100 Emerald Slayer", new TaskReward(23253, 1)
            , 2342, 100),
    TASK_108(2, 10,"Kill 1 Mutant Hydra", new TaskReward(23205, 1)
            , 9839, 1),
    TASK_109(2, 40,"Kill 100 Mutant Hydra", new TaskReward(23253, 2)
            , 9839, 100),
    TASK_110(2, 10,"Kill 1 Gorvek", new TaskReward(23205, 2)
            , 9806, 1),
    TASK_111(2, 40,"Kill 100 Gorvek", new TaskReward(23253, 3)
            , 9806, 100),
    TASK_112(2, 10,"Kill 1 Onyx Griffin", new TaskReward(23206, 1)
            , 1746, 1),
    TASK_113(2, 40,"Kill 100 Onyx Griffin", new TaskReward(23256, 1)
            , 1746, 100),
    TASK_114(2, 10,"Kill 1 Tyrant Lord", new TaskReward(23206, 2)
            , 4972, 1),
    TASK_115(2, 40,"Kill 100 Tyrant Lord", new TaskReward(23256, 1)
            , 4972, 100),
    TASK_116(2, 10,"Kill 1 White Beard", new TaskReward(23207, 1)
            , 3021, 1),
    TASK_117(2, 40,"Kill 100 White Beard", new TaskReward(23256, 2)
            , 3021, 100),
    TASK_118(2, 10,"Kill 1 Panther", new TaskReward(23208, 1)
            , 3305, 1),
    TASK_119(2, 40,"Kill 100 Panther", new TaskReward(23256, 2)
            , 3305, 100),
    TASK_120(2, 10,"Kill 1 Warrior", new TaskReward(23209, 1)
            , 125, 1),
    TASK_121(2, 40,"Kill 100 Warrior", new TaskReward(23256, 5)
            , 125, 100),

    TASK_150(3, 10,"Clear LVL 5 Tarn Tower", new TaskReward(23253, 1)
            , -1, 5),
    TASK_151(3, 50,"Clear LVL 10 Tarn Tower", new TaskReward(23254, 1)
            , -1, 10),
    TASK_152(3, 100,"Clear LVL 20 Tarn Tower", new TaskReward(23255, 1)
            , -1, 20),

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
