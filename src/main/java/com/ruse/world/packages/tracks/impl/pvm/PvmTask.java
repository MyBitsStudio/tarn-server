package com.ruse.world.packages.tracks.impl.pvm;

import lombok.Getter;

@Getter
public class PvmTask {

    private final int npcId, amount;
    private int current = 0;
    public PvmTask(int npcId, int amount) {
        this.npcId = npcId;
        this.amount = amount;
    }

    public boolean increment() {
        if(current >= amount)
            return false;
    	current++;
        return true;
    }
}
