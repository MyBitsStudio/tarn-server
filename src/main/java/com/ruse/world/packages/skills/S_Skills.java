package com.ruse.world.packages.skills;

import com.ruse.model.Skill;
import com.ruse.util.Misc;
import lombok.Getter;

@Getter
public enum S_Skills {

    ATTACK(0, 6247, 124007, 124008),
    DEFENCE(1, 6253, 124015, 124016),
    STRENGTH(2, 6206, 124023, 124024),
    HITPOINTS(3, 6216, 124031, 124032),
    RANGED(4, 4443, 124039, 124040),
    PRAYER(5, 6242, 124047, 124048),
    MAGIC(6, 6211, 124055, 124056),
    CRAFTING(7, 6263, 124063, 124064),
    MINING(8, 4416,124071, 124072),
    SMITHING(9, 6226, 124079, 124080),
    THIEVING(10, 4272, 124087, 124088),
    SLAYER(11, 12122, 124095, 124096),
    ALCHEMY(12, 4261, 124103, 124104),
    ;

    private final int chatBoxSprite, skillId, progressId, progressXpBar;
    private final int[] modifiers;

    S_Skills(int skillId, int chatBoxSprite, int progressId , int progressXpBar, int... modifiers) {
        this.skillId = skillId;
        this.chatBoxSprite = chatBoxSprite;
        this.progressId = progressId;
        this.progressXpBar = progressXpBar;
        this.modifiers = modifiers;
    }

    public static S_Skills forId(int id) {
        for (S_Skills skill : S_Skills.values()) {
            if (skill.getSkillId() == id) {
                return skill;
            }
        }
        return null;
    }

    public String getName(){
        return Misc.capitalizeJustFirst(this.name().toLowerCase());
    }
}
