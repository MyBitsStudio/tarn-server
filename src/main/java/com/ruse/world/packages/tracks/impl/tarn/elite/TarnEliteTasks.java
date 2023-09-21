package com.ruse.world.packages.tracks.impl.tarn.elite;

import com.ruse.world.packages.tracks.impl.TaskReward;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum TarnEliteTasks {

    TASK_001(0, 10, "Kill 25000 Death God", new TaskReward(23209, 1)
            , 9915, 1),
    TASK_002(0, 10, "Kill 25000 Golden Golem", new TaskReward(23209, 1)
            , 9024, 1),
    TASK_003(0, 10, "Kill 25000 Malvek", new TaskReward(23210, 1)
            , 8002, 1),
    TASK_004(0, 10, "Kill 25000 Galvek", new TaskReward(23210, 1)
            , 8000, 1),
    TASK_005(0, 10, "Kill 25000 Bowser", new TaskReward(23211, 1)
            , 9919, 1),
    TASK_006(0, 10, "Kill 25000 Slender Man", new TaskReward(23211, 1)
            , 9913, 1),
    TASK_007(0, 10, "Kill 25000 Agumon", new TaskReward(23212, 1)
            , 3020, 1),
    TASK_008(0, 10, "Kill 25000 Queen Fazula", new TaskReward(23213, 1)
            , 1311, 1),
    TASK_009(0, 10, "Kill 25000 Lord Yasuda", new TaskReward(23213, 1)
            , 1313, 1),
    TASK_010(0, 10, "Kill 25000 Sasuke", new TaskReward(23214, 1)
            , 9914, 1),

    TASK_050(0, 15, "Kill 15000 Ag'thomoth", new TaskReward(23209, 1)
            , 3013, 1),
    TASK_051(0, 15, "Kill 15000 Zernath", new TaskReward(23210, 1)
            , 3831, 1),
    TASK_052(0, 15, "Kill 15000 Sanctum Golem", new TaskReward(23211, 1)
            , 9017, 1),
    TASK_053(0, 15, "Kill 15000 Varthramoth", new TaskReward(23212, 1)
            , 3016, 1),
    TASK_054(0, 15, "Kill 15000 Exoden", new TaskReward(23213, 1)
            , 12239, 1),

    TASK_100(1, 10, "Finish 1000 Slayer Tasks", new TaskReward(23255, 1)
            , -1, 10),
    TASK_101(1, 15, "Finish 2500 Slayer Tasks", new TaskReward(23255, 2)
            , -1, 25),
    TASK_102(1, 20, "Finish 5000 Slayer Tasks", new TaskReward(23255, 3)
            , -1, 50),
    TASK_103(1, 10, "Dissolve 500 Pieces", new TaskReward(20502, 1)
            , -1, 50),
    TASK_104(1, 15, "Dissolve 1000 Pieces", new TaskReward(20502, 2)
            , -1, 100),
    TASK_105(1, 20, "Dissolve 5000 Pieces", new TaskReward(20502, 3)
            , -1, 250),
   ;


    private final int category, xp, npc, count;
    private final String taskName;
    private final TaskReward reward;

    TarnEliteTasks(int category, int xp, String name, TaskReward reward, int npc, int count){
        this.category = category;
        this.xp = xp;
        this.taskName = name;
        this.reward = reward;
        this.npc = npc;
        this.count = count;
    }

    public static @NotNull List<TarnEliteTasks> getMonsterTasks(){
        List<TarnEliteTasks> tasks = new ArrayList<>();
        for(TarnEliteTasks task : TarnEliteTasks.values()){
            if(task.getCategory() == 0){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static @NotNull List<TarnEliteTasks> getSkillTasks(){
        List<TarnEliteTasks> tasks = new ArrayList<>();
        for(TarnEliteTasks task : TarnEliteTasks.values()){
            if(task.getCategory() == 1){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static @NotNull List<TarnEliteTasks> getBossTasks(){
        List<TarnEliteTasks> tasks = new ArrayList<>();
        for(TarnEliteTasks task : TarnEliteTasks.values()){
            if(task.getCategory() == 2){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static @Nullable TarnEliteTasks getByIndexAndCategory(int category, int index){
        List<TarnEliteTasks> tasks = new ArrayList<>();
        for(TarnEliteTasks task : TarnEliteTasks.values()){
            if(task.getCategory() == category){
                tasks.add(task);
            }
        }
        if(tasks.size() > index){
            return tasks.get(index);
        }
        return null;
    }

    public static List<TarnEliteTasks> byKills(int npc){
        List<TarnEliteTasks> tasks = new ArrayList<>();
        for(TarnEliteTasks task : TarnEliteTasks.values()){
            if(task.getNpc() == -1)
                continue;
            if(task.getNpc() == npc){
                tasks.add(task);
            }
        }
        return tasks;
    }

    public static TarnEliteTasks byNPC(int npc){
        for(TarnEliteTasks task : TarnEliteTasks.values()){
            if(task.getNpc() == -1)
                continue;
            if(task.getNpc() == npc){
                return task;
            }
        }
        return null;
    }
}
