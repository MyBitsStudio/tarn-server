package com.ruse.world.packages.tracks.impl.pvm;

import com.ruse.util.Misc;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public enum PvMTasks {

    DEATH_GOD_1(0, 0, new int[]{1, 100}),
    DEATH_GOD_2(0, 0, new int[]{5, 400}),

    ;

    private final int rank, npcId;
    private final int[] limits;
    PvMTasks(int rank, int npcId, int[] limits) {
        this.rank = rank;
        this.npcId = npcId;
        this.limits = limits;
    }

    public static PvMTasks getRandomTaskForRank(int rank){
        List<PvMTasks> tasks = new ArrayList<>();
        for(PvMTasks task : PvMTasks.values()){
            if(task.getRank() == rank){
                tasks.add(task);
            }
        }
        Collections.shuffle(tasks);
        return tasks.get(Misc.random(tasks.size() - 1));
    }
}
