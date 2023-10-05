package com.ruse.world.packages.skills;

import lombok.Getter;

@Getter
public enum SkillingTimer {

    MINING(2284, 5)

    ;

    SkillingTimer(int clientSprite, int time) {
        this.clientSprite = clientSprite;
        this.time = time;
    }

    private final int clientSprite, time;
}
