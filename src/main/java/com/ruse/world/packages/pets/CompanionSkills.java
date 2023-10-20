package com.ruse.world.packages.pets;

import lombok.Getter;

@Getter
public enum CompanionSkills {

    NOTHING(-1),

    ATTACK_LOW_MELEE(1),
    ATTACK_MED_MELEE(2),
    ATTACK_HIGH_MELEE(3),

    ATTACK_LOW_RANGED(4),
    ATTACK_MED_RANGED(5),
    ATTACK_HIGH_RANGED(6),

    ATTACK_LOW_MAGIC(7),
    ATTACK_MED_MAGIC(8),
    ATTACK_HIGH_MAGIC(9),

    HEAL_LOW(10),
    HEAL_MED(11),
    HEAL_HIGH(12),

    ;

    private final int skillId;

    CompanionSkills(int skillId) {
        this.skillId = skillId;
    }

    public boolean isAttackSkill(){
        return skillId >= 1 && skillId <= 9;
    }
}
