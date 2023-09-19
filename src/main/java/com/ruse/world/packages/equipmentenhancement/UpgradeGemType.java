package com.ruse.world.packages.equipmentenhancement;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UpgradeGemType {
    WEAK_GEM(2380),
    MEDIUM_GEM(2381),
    STRONG_GEM(2382);

    private final int itemId;
    public final static ImmutableList<UpgradeGemType> VALUES = ImmutableList.copyOf(values());
}
