package com.ruse.world.packages.tracks.impl.tarn.normal;

import com.ruse.world.packages.tracks.impl.TaskReward;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum TarnNormalTasks {

    TASK_001(0, 10, "Kill 1 Death God", new TaskReward(23209, 1)
            , 9915, 1),
    TASK_002(0, 10, "Kill 1 Golden Golem", new TaskReward(23209, 1)
            , 9024, 1),
    TASK_003(0, 10, "Kill 1 Malvek", new TaskReward(23210, 1)
            , 8002, 1),
    TASK_004(0, 10, "Kill 1 Galvek", new TaskReward(23210, 1)
            , 8000, 1),
    TASK_005(0, 10, "Kill 1 Bowser", new TaskReward(23211, 1)
            , 9919, 1),
    TASK_006(0, 10, "Kill 1 Slender Man", new TaskReward(23211, 1)
            , 9913, 1),
    TASK_007(0, 10, "Kill 1 Agumon", new TaskReward(23212, 1)
            , 3020, 1),
    TASK_008(0, 10, "Kill 1 Queen Fazula", new TaskReward(23213, 1)
            , 1311, 1),
    TASK_009(0, 10, "Kill 1 Lord Yasuda", new TaskReward(23213, 1)
            , 1313, 1),
    TASK_010(0, 10, "Kill 1 Sasuke", new TaskReward(23214, 1)
            , 9914, 1),

    TASK_050(0, 15, "Kill 1 Ag'thomoth", new TaskReward(23209, 1)
            , 3013, 1),
    TASK_051(0, 15, "Kill 1 Zernath", new TaskReward(23210, 1)
            , 3831, 1),
    TASK_052(0, 15, "Kill 1 Sanctum Golem", new TaskReward(23211, 1)
            , 9017, 1),
    TASK_053(0, 15, "Kill 1 Varthramoth", new TaskReward(23212, 1)
            , 3016, 1),
    TASK_054(0, 15, "Kill 1 Exoden", new TaskReward(23213, 1)
            , 12239, 1),

    TASK_100(1, 10, "Finish 10 Slayer Tasks", new TaskReward(23255, 1)
            , -1, 10),
    TASK_101(1, 15, "Finish 25 Slayer Tasks", new TaskReward(23255, 2)
            , -1, 25),
    TASK_102(1, 20, "Finish 50 Slayer Tasks", new TaskReward(23255, 3)
            , -1, 50),
    TASK_103(1, 10, "Dissolve 50 Pieces", new TaskReward(20502, 1)
            , -1, 50),
    TASK_104(1, 15, "Dissolve 100 Pieces", new TaskReward(20502, 2)
            , -1, 100),
    TASK_105(1, 20, "Dissolve 250 Pieces", new TaskReward(20502, 3)
            , -1, 250),

    TASK_151(2, 20, "Kill 200 Death God", new TaskReward(23209, 5)
            , 9915, 200),
    TASK_152(2, 20, "Kill 200 Golden Golem", new TaskReward(23209, 5)
            , 9024, 200),
    TASK_153(2, 20, "Kill 200 Malvek", new TaskReward(23210, 5)
            , 8002, 200),
    TASK_154(2, 20, "Kill 200 Galvek", new TaskReward(23210, 5)
            , 8000, 200),
    TASK_155(2, 20, "Kill 200 Bowser", new TaskReward(23211, 5)
            , 9919, 200),
    TASK_156(2, 20, "Kill 200 Slender Man", new TaskReward(23211, 5)
            , 9913, 200),
    TASK_157(2, 20, "Kill 200 Agumon", new TaskReward(23212, 5)
            , 3020, 200),
    TASK_158(2, 20, "Kill 200 Queen Fazula", new TaskReward(23213, 5)
            , 1311, 200),
    TASK_159(2, 20, "Kill 200 Lord Yasuda", new TaskReward(23213, 5)
            , 1313, 200),
    TASK_160(2, 20, "Kill 200 Sasuke", new TaskReward(23214, 5)
            , 9914, 200),

    TASK_200(2, 20, "Kill 75 Ag'thomoth", new TaskReward(23209, 3)
            , 3013, 75),
    TASK_201(2, 20, "Kill 75 Zernath", new TaskReward(23210, 3)
            , 3831, 75),
    TASK_202(2, 20, "Kill 75 Sanctum Golem", new TaskReward(23211, 3)
            , 9017, 75),
    TASK_203(2, 20, "Kill 75 Varthramoth", new TaskReward(23212, 3)
            , 3016, 75),
    TASK_204(2, 20, "Kill 75 Exoden", new TaskReward(23213, 3)
            , 12239, 75),
   ;


    private final int category, xp, npc, count;
    private final String taskName;
    private final TaskReward reward;

    TarnNormalTasks(int category, int xp, String name, TaskReward reward, int npc, int count){
        this.category = category;
        this.xp = xp;
        this.taskName = name;
        this.reward = reward;
        this.npc = npc;
        this.count = count;
    }

    public static @NotNull List<TarnNormalTasks> getMonsterTasks(){
        List<TarnNormalTasks> tasks = new ArrayList<>();
        for(TarnNormalTasks task : TarnNormalTasks.values()){
            if(task.getCategory() == 0){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static @NotNull List<TarnNormalTasks> getSkillTasks(){
        List<TarnNormalTasks> tasks = new ArrayList<>();
        for(TarnNormalTasks task : TarnNormalTasks.values()){
            if(task.getCategory() == 1){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static @NotNull List<TarnNormalTasks> getBossTasks(){
        List<TarnNormalTasks> tasks = new ArrayList<>();
        for(TarnNormalTasks task : TarnNormalTasks.values()){
            if(task.getCategory() == 2){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static @Nullable TarnNormalTasks getByIndexAndCategory(int category, int index){
        List<TarnNormalTasks> tasks = new ArrayList<>();
        for(TarnNormalTasks task : TarnNormalTasks.values()){
            if(task.getCategory() == category){
                tasks.add(task);
            }
        }
        if(tasks.size() > index){
            return tasks.get(index);
        }
        return null;
    }

    public static List<TarnNormalTasks> byKills(int npc){
        List<TarnNormalTasks> tasks = new ArrayList<>();
        for(TarnNormalTasks task : TarnNormalTasks.values()){
            if(task.getNpc() == -1)
                continue;
            if(task.getNpc() == npc){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static List<TarnNormalTasks> slayers(){
        List<TarnNormalTasks> tasks = new ArrayList<>();
        tasks.add(TASK_100);
        tasks.add(TASK_101);
        tasks.add(TASK_102);
        return tasks;
    }

    public static List<TarnNormalTasks> dissolve(){
        List<TarnNormalTasks> tasks = new ArrayList<>();
        tasks.add(TASK_103);
        tasks.add(TASK_104);
        tasks.add(TASK_105);
        return tasks;
    }

    public static TarnNormalTasks byNPC(int npc){
        for(TarnNormalTasks task : TarnNormalTasks.values()){
            if(task.getNpc() == -1)
                continue;
            if(task.getNpc() == npc){
                return task;
            }
        }
        return null;
    }
}
