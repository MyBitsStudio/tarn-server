package com.ruse.world.packages.equipmentenhancement;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SlotType {
    AMULET(-16124,0, 3314, "Amulet"),
    WEAPON(-16119, 1, 3316, "Weapon"),
    GLOVES(-16116, 2, 3320, "Gloves"),
    CROWN(-16117, 3, 3302, "Halo"),
    HEAD(-16126, 4, 3312, "Headpiece"),
    BODY(-16123, 5, 3317, "Torso"),
    LEGS(-16122, 6, 3319, "Legs"),
    BOOTS(-16121, 7, 3321, "Boots"),
    CAPE(-16125, 8, 3313, "Cape"),
    SHIELD(-16120, 9, 3318, "Shield"),
    RING(-16115, 10, 3322, "Ring"),
    QUIVER(-16118, 11, 3315, "Quiver"),
    STAR(-16114, 12, 3300, "Enhancement"),
    STAR_2(-16113, 13, 3300, "Aura"),
    BREAD(-16112, 14, 3301, "Gemstone")
    ;

    private final int btn;
    private final int id;
    private final int spriteId;
    private final String name;

    public final static ImmutableList<SlotType> VALUES = ImmutableList.copyOf(values());
}
