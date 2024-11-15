package com.ruse.world.packages.event;

import com.ruse.world.packages.event.impl.DonatorReleaseEvent;
import com.ruse.world.packages.event.impl.FallEvent;
import com.ruse.world.packages.event.impl.HalloweenEvent;
import lombok.Getter;

/**
 * Dates :
 * [0] - Start Month
 * [1] - Start Day
 * [2] - End Month
 * [3] - End Day
 */
@Getter
public enum Events {

    DONATOR_RELEASE(new DonatorReleaseEvent(), new int[]{9, 2, 10, 6}),
    FALL(new FallEvent(), new int[]{9, 1, 11, 1}),
    HALLOWEEN(new HalloweenEvent(), new int[]{10, 13, 11, 7})

    ;

    private final Event event;
    private final int[] dates;

    Events(Event event, int[] dates) {
        this.event = event;
        this.dates = dates;
    }
}
