package com.ruse.world.content.skill;
import com.ruse.model.Skill;
import com.ruse.world.entity.impl.player.Player;

/**
 * Represents a player's skills in the game, also manages calculations such as
 * combat level and total level.
 *
 * DEPRECATED -- Use new SkillingManager
 *
 * @author relex lawl
 * @editor Gabbe
 */

public class SkillManager {

    /**
     * The maximum amount of skills in the game.
     */
    public static final int MAX_SKILLS = 25;
    private Skills skills;
    private Player player;

    /**
     * The skillmanager's constructor
     *
     * @param player The player's who skill set is being represented.
     */
    public SkillManager(Player player) {
        this.player = player;
    }

    public Skills getSkills() {
        return skills;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    public int getCurrentLevel(Skill skill) {
        return skills.level[skill.ordinal()];
    }

    public int getMaxLevel(Skill skill) {
        return skills.maxLevel[skill.ordinal()];
    }

    public int getExperience(Skill skill) {
        return skills.experience[skill.ordinal()];
    }


    public static class Skills {

        private final int[] level, maxLevel, experience;

        public Skills() {
            level = new int[MAX_SKILLS];
            maxLevel = new int[MAX_SKILLS];
            experience = new int[MAX_SKILLS];
        }

    }

}