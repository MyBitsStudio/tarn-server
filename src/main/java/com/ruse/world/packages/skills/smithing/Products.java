package com.ruse.world.packages.skills.smithing;

import lombok.Getter;

@Getter
public enum Products {

    MASTER_STEEL(9077),


    ;

    private int itemId;
    private Materials[] materials;
    Products(int itemId, Materials... materials){
        this.itemId = itemId;
        this.materials = materials;
    }
}
