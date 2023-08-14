package com.ruse.world.packages.event;

import com.ruse.world.packages.event.impl.Solstice;
import lombok.Getter;

/**
 * Dates :
 * [0] - Start Month - 1
 * [1] - Start Day
 * [2] - End Month - 1
 * [3] - End Day
 */
@Getter
public enum Events {

    SOLSTICE(new Solstice(), new int[]{7, 10, 8, 23}),

    ;

    private final Event event;
    private final int[] dates;

    Events(Event event, int[] dates) {
        this.event = event;
        this.dates = dates;
    }
}
