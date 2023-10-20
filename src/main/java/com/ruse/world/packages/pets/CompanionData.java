package com.ruse.world.packages.pets;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@Getter
public enum CompanionData {

    DRAGON_1(9820, 1500, CompanionSkills.ATTACK_HIGH_MAGIC),

    ;
    //6903
    private final int npcId, zoom;
    private final CompanionSkills[] skills;

    CompanionData(int npcId, int zoom, CompanionSkills... skills) {
        this.npcId = npcId;
        this.zoom = zoom;
        this.skills = skills;
    }

    @Contract(pure = true)
    public @Nullable CompanionData byId(int id){
        for(CompanionData c : values()){
            if(c.npcId == id){
                return c;
            }
        }
        return null;
    }
}
