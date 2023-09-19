package com.ruse.world.packages.equipmentenhancement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BoostType {
    DR(0),
    CASH(1),
    STATS(2);

    private final int position;
}
