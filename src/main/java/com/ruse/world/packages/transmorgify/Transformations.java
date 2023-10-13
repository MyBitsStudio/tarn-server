package com.ruse.world.packages.transmorgify;

import lombok.Getter;

@Getter
public enum Transformations {

    DEATH(2862, -1),
    WARLORD(6105, -1),
    PIRATE(5665, -1),

    ;

    private final int npcId, walkIndex;

    Transformations(int npcId, int walkIndex){
        this.npcId = npcId;
        this.walkIndex = walkIndex;
    }
}
