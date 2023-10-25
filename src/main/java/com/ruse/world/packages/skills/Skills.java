package com.ruse.world.packages.skills;

import lombok.Getter;

import static com.ruse.world.packages.skills.SkillingManager.MAX_SKILLS;

public class Skills {

    public int[] level, maxLevel, experience;

    public Skills() {
        level = new int[MAX_SKILLS];
        maxLevel = new int[MAX_SKILLS];
        experience = new int[MAX_SKILLS];
    }
}
