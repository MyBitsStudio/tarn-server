package com.ruse.world.packages.skills.smithing;

import lombok.Getter;

@Getter
public enum Materials {

    MASTER_CORE(19641),
    MASTER_STEEL(9077),
    MASTER_ORE(9076),
    SHANK_SIGIL(13748),
    CROSS_SIGIL(13746),
    WINGED_SIGIL(13750),
    CROOKED_SIGIL(13752),


    ;

    public int itemId;

    Materials(int itemId){
        this.itemId = itemId;
    }
}
