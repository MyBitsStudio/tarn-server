package com.ruse.world.packages.tracks.impl.tarn;

import com.ruse.util.Misc;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public enum TarnTasks {

    DEATH_GOD_1(0, 0, new int[]{1, 100}),
    DEATH_GOD_2(0, 0, new int[]{5, 400}),

    ;

    private final int rank, npcId;
    private final int[] limits;
    TarnTasks(int rank, int npcId, int[] limits) {
        this.rank = rank;
        this.npcId = npcId;
        this.limits = limits;
    }

    public static TarnTasks getRandomTaskForRank(int rank){
        List<TarnTasks> tasks = new ArrayList<>();
        for(TarnTasks task : TarnTasks.values()){
            if(task.getRank() == rank){
                tasks.add(task);
            }
        }
        Collections.shuffle(tasks);
        return tasks.get(Misc.random(tasks.size() - 1));
    }
}
