package com.ruse.world.packages.tracks.impl.tarn.elite;

import com.ruse.world.packages.tracks.impl.TaskReward;
import com.ruse.world.packages.tracks.impl.tarn.normal.TarnNormalTasks;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum TarnEliteTasks {

    TASK_001(0, 10, "Kill 25000 Death God", new TaskReward(23147, 3)
            , 9915, 25000),
    TASK_002(0, 10, "Kill 25000 Golden Golem", new TaskReward(20502, 2)
            , 9024, 25000),
    TASK_003(0, 10, "Kill 25000 Malvek", new TaskReward(604, 5)
            , 8002, 25000),
    TASK_004(0, 10, "Kill 25000 Galvek", new TaskReward(22124, 10)
            , 8000, 25000),
    TASK_005(0, 10, "Kill 25000 Bowser", new TaskReward(23147, 3)
            , 9919, 25000),
    TASK_006(0, 10, "Kill 25000 Slender Man", new TaskReward(20502, 2)
            , 9913, 25000),
    TASK_007(0, 10, "Kill 25000 Agumon", new TaskReward(604, 5)
            , 3020, 25000),
    TASK_008(0, 10, "Kill 25000 Queen Fazula", new TaskReward(20502, 2)
            , 1311, 25000),
    TASK_009(0, 10, "Kill 25000 Lord Yasuda", new TaskReward(23107, 5)
            , 1313, 25000),
    TASK_010(0, 10, "Kill 25000 Sasuke", new TaskReward(22124, 10)
            , 9914, 25000),

    TASK_050(0, 15, "Kill 15000 Ag'thomoth", new TaskReward(23250, 1)
            , 3013, 15000),
    TASK_051(0, 15, "Kill 15000 Zernath", new TaskReward(23251, 1)
            , 3831, 15000),
    TASK_052(0, 15, "Kill 15000 Sanctum Golem", new TaskReward(23252, 1)
            , 9017, 15000),
    TASK_053(0, 15, "Kill 15000 Varthramoth", new TaskReward(23251, 1)
            , 3016, 15000),
    TASK_054(0, 15, "Kill 15000 Exoden", new TaskReward(23251, 1)
            , 12239, 15000),

    TASK_100(1, 10, "Finish 1000 Slayer Tasks", new TaskReward(25102, 10)
            , -1, 1000),
    TASK_101(1, 15, "Finish 2500 Slayer Tasks", new TaskReward(23104, 5)
            , -1, 2500),
    TASK_102(1, 20, "Finish 5000 Slayer Tasks", new TaskReward(5023, 50000)
            , -1, 5000),
    TASK_103(1, 10, "Dissolve 500 Pieces", new TaskReward(10835, 5000)
            , -1, 500),
    TASK_104(1, 15, "Dissolve 1000 Pieces", new TaskReward(10835, 10000)
            , -1, 1000),
    TASK_105(1, 20, "Dissolve 5000 Pieces", new TaskReward(10835, 50000)
            , -1, 5000),
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

    public static List<TarnEliteTasks> slayers(){
        List<TarnEliteTasks> tasks = new ArrayList<>();
        tasks.add(TASK_100);
        tasks.add(TASK_101);
        tasks.add(TASK_102);
        return tasks;
    }

    public static List<TarnEliteTasks> dissolve(){
        List<TarnEliteTasks> tasks = new ArrayList<>();
        tasks.add(TASK_103);
        tasks.add(TASK_104);
        tasks.add(TASK_105);
        return tasks;
    }
}
