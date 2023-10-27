package com.ruse.world.packages.skills;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Flag;
import com.ruse.model.Graphic;
import com.ruse.model.Skill;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class SkillingManager {

    //change later
    private static final int[] EXP_ARRAY = {0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107,
            2411, 2746, 3115, 3523, 3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363, 14833,
            16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983,
            75127, 83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254, 224466, 247886, 273742,
            302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445, 899257, 992895,
            1096278, 1210421, 1336443, 1475581, 1629200, 1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594,
            3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629, 7944614, 8771558, 9684577, 10692629,
            11805606, 13034431, 14391160, 15889109, 17542976, 19368992, 21385073, 23611006, 26068632, 28782069,
            31777943, 35085654, 38737661, 42769801, 47221641, 52136869, 57563718, 63555443, 70170840, 77474828,
            85539082, 94442737, 104273167};

    public static int MAX_SKILLS = 13;

    private final Skills skill;

    private final Player player;

    private final AtomicBoolean isSkilling = new AtomicBoolean(false);

    public SkillingManager(Player player) {
        this.player = player;
        this.skill = new Skills();
        for (int i = 0; i < MAX_SKILLS; i++) {
            skill.level[i] = skill.maxLevel[i] = 1;
            skill.experience[i] = 0;
        }
        skill.level[S_Skills.HITPOINTS.getSkillId()] = skill.maxLevel[S_Skills.HITPOINTS.getSkillId()] = 100;
        skill.experience[S_Skills.HITPOINTS.getSkillId()] = 1184;
        skill.level[S_Skills.PRAYER.getSkillId()] = skill.maxLevel[S_Skills.PRAYER.getSkillId()] = 10;
    }

    public void setSkills(@NotNull Skills skill){
        this.skill.level = skill.level;
        this.skill.maxLevel = skill.maxLevel;
        this.skill.experience = skill.experience;
    }

    public int getCurrentLevel(S_Skills skill) {
        return this.skill.level[skill.getSkillId()];
    }

    public int getMaxLevel(S_Skills skill) {
        return this.skill.maxLevel[skill.getSkillId()];
    }

    public int xpForLevel(int level){
        return EXP_ARRAY[--level > 119 ? 119 : level];
    }

    public int levelForXp(int xp){
        for (int i = 0; i < EXP_ARRAY.length; i++) {
            if (EXP_ARRAY[i] > xp) {
                return i;
            }
        }
        return 120;
    }

    private int maxedSkills(){
        int maxed = 0;
        for (int i = 0; i < MAX_SKILLS; i++) {
            if (skill.level[i] >= 99) {
                maxed++;
            }
        }
        return maxed;
    }

    public int getTotalLevel(){
        int total = 0;
        for (int i = 0; i < MAX_SKILLS; i++) {
            total += skill.level[i];
        }
        return total;
    }

    public int getTotalXp(){
        int total = 0;
        for (int i = 0; i < MAX_SKILLS; i++) {
            total += skill.experience[i];
        }
        return total;
    }

    public SkillingManager setCurrentLevel(S_Skills skill, int level, boolean refresh) {
        this.skill.level[skill.getSkillId()] = Math.max(level, 0);
        if (refresh){
            updateSkill(skill);
        }
        return this;
    }

    public SkillingManager setMaxLevel(S_Skills skill, int level, boolean refresh) {
        this.skill.maxLevel[skill.getSkillId()] = Math.max(level, 0);
        if (refresh){
            updateSkill(skill);
        }
        return this;
    }

    public SkillingManager setExperience(S_Skills skill, int experience, boolean refresh) {
        this.skill.experience[skill.getSkillId()] = Math.max(experience, 0);
        if (refresh){
            updateSkill(skill);
        }
        return this;
    }

    public SkillingManager stopSkilling() {
        if (player.getCurrentTask() != null) {
            player.getCurrentTask().stop();
            player.setCurrentTask(null);
        }
        player.setResetPosition(null);
        player.setInputHandling(null);
        this.isSkilling.set(false);
        return this;
    }

    public boolean isSpecialCase(S_Skills skill){
        return skill.equals(S_Skills.HITPOINTS) || skill.equals(S_Skills.PRAYER);
    }

    public SkillingManager xpUp(S_Skills skill, int amount){
        if (player.experienceLocked())
            return this;


        if (this.skill.experience[skill.getSkillId()] >= 2000000000)
            return this;

        int modified = modifiedExp(skill, amount);

        int startingLevel = isSpecialCase(skill) ? (this.skill.maxLevel[skill.getSkillId()] / 10)
                : this.skill.maxLevel[skill.getSkillId()];
        /*
         * Adds the experience to the skill's experience.
         */
        this.skill.experience[skill.ordinal()] = Math.min(this.skill.experience[skill.ordinal()] + modified, 2000000000);

        int newLevel = levelForXp(this.skill.experience[skill.getSkillId()]);

        if (newLevel > startingLevel) {
            int level = newLevel - startingLevel;
            String skillName = Misc.formatText(skill.toString().toLowerCase());
            this.skill.maxLevel[skill.getSkillId()] += isSpecialCase(skill) ? level * 10 : level;

            if (!isSpecialCase(skill)) {
                if (getCurrentLevel(skill) < newLevel)
                    setCurrentLevel(skill, this.skill.maxLevel[skill.ordinal()], true);
            }

            player.setChat(null);

            player.performGraphic(new Graphic(312));
            player.getPacketSender()
                    .sendMessage("You've just advanced " + skillName + " level! You have reached level " + newLevel);

            if (this.skill.maxLevel[skill.getSkillId()] == getMaxAchievingLevel(skill)) {
                player.getStarter().handleSkillCount(maxedSkills());
                player.getPacketSender()
                        .sendMessage("Well done! You've achieved the highest possible level in this skill!");

                World.sendFilterMessage("<shad=15536940><img=5> " + player.getUsername()
                        + " has just achieved the highest possible level in " + skillName + "!");

                TaskManager.submit(new Task(2, player, true) {
                    int localGFX = 1634;

                    @Override
                    public void execute() {
                        player.performGraphic(new Graphic(localGFX));
                        if (localGFX == 1637) {
                            stop();
                            return;
                        }
                        localGFX++;
                        player.performGraphic(new Graphic(localGFX));
                    }
                });
            }

            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
        updateSkill(skill);
        return this;
    }

    //interface update
    public void updateSkill(S_Skills skill){
        player.getPacketSender().sendSkill(skill);

        player.getPacketSender().updateProgressSpriteBar(skill.getProgressId(), skillPercent(skill) > 0 ? skillPercent(skill) : 1, 100);
        player.getPacketSender().updateProgressSpriteBar(skill.getProgressXpBar(), levelPercent(skill) > 0 ? levelPercent(skill) : 1, 100);

        player.getPacketSender().updateProgressSpriteBar(124105, totalLevelPercent(), 100);
        player.getPacketSender().updateProgressSpriteBar(124107, totalXpPercent() > 0 ? totalXpPercent() : 1, 100);

    }

    public int combatLevel(){
        int attack = skill.level[S_Skills.ATTACK.getSkillId()];
        int defence = skill.level[S_Skills.DEFENCE.getSkillId()];
        int strength = skill.level[S_Skills.STRENGTH.getSkillId()];
        int combatLevel = getCombatLevel(attack, strength, defence);
        if (combatLevel > 138) {
            return 138;
        } else if (combatLevel < 3) {
            return 3;
        }
        return combatLevel / 4;
    }

    private int getCombatLevel(int attack, int strength, int defence) {
        int hp = skill.level[S_Skills.HITPOINTS.getSkillId()];
        int prayer = skill.level[S_Skills.PRAYER.getSkillId()];
        int ranged = skill.level[S_Skills.RANGED.getSkillId()];
        int magic = skill.level[S_Skills.MAGIC.getSkillId()];
        int combatLevel;
        int base = Math.max(attack, strength);
        base += defence;
        base += hp;
        base += prayer / 2;
        if (ranged > magic) {
            combatLevel = (int) (base + (ranged * 1.5));
        } else {
            combatLevel = (int) (base + (magic * 1.5));
        }
        return combatLevel;
    }

    public double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }

    private int skillPercent(S_Skills skill){
        int lastLevel = xpForLevel(this.skill.level[skill.getSkillId()]);
        int nextLevel = xpForLevel(this.skill.level[skill.getSkillId()] + 1);
        int currentXp = this.skill.experience[skill.getSkillId()];
        int gainedXp = currentXp - lastLevel;
        int xpForLevel = nextLevel - lastLevel;
        return (int) calculatePercentage(gainedXp, xpForLevel);
        //return (int) calculatePercentage(this.skill.experience[skill.getSkillId()] > 0 ? this.skill.experience[skill.getSkillId()] : 1, xpForLevel(this.skill.level[skill.getSkillId()] + 1));
    }

    private int levelPercent(S_Skills skill){
        return (int) calculatePercentage(this.skill.level[skill.getSkillId()], isSpecialCase(skill) ? 1200 : 120);
    }

    private int totalLevelPercent(){
        int total = 0;
        for (int i = 0; i < MAX_SKILLS; i++) {
            if(i == S_Skills.PRAYER.getSkillId() || i == S_Skills.HITPOINTS.getSkillId())
                total += 1200;
            else
                total += 120;
        }
        return (int) calculatePercentage(getTotalLevel(), total);
    }

    private int totalXpPercent(){
        return (int) calculatePercentage(getTotalXp(), MAX_SKILLS * 104273167L);
    }

    public int getMaxAchievingLevel(S_Skills skill) {
        int level = 120;
        if (isSpecialCase(skill)) {
            level = 1200;
        }
        return level;
    }

    public int modifiedExp(S_Skills skill, int amount){
        int modified = amount;

        return modified;
    }

    public void mergeOld(){
        if(player.getPSettings().getBooleanValue("settings-rewire"))
            return;

        this.skill.level[S_Skills.ATTACK.getSkillId()] = player.getSkillManager().getCurrentLevel(Skill.ATTACK);
        this.skill.level[S_Skills.STRENGTH.getSkillId()] = player.getSkillManager().getCurrentLevel(Skill.STRENGTH);
        this.skill.level[S_Skills.DEFENCE.getSkillId()] = player.getSkillManager().getCurrentLevel(Skill.DEFENCE);
        this.skill.level[S_Skills.RANGED.getSkillId()] = player.getSkillManager().getCurrentLevel(Skill.RANGED);
        this.skill.level[S_Skills.PRAYER.getSkillId()] = player.getSkillManager().getCurrentLevel(Skill.PRAYER);
        this.skill.level[S_Skills.MAGIC.getSkillId()] = player.getSkillManager().getCurrentLevel(Skill.MAGIC);
        this.skill.level[S_Skills.HITPOINTS.getSkillId()] = player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
        this.skill.level[S_Skills.CRAFTING.getSkillId()] = player.getSkillManager().getCurrentLevel(Skill.CRAFTING);
        this.skill.level[S_Skills.MINING.getSkillId()] = player.getSkillManager().getCurrentLevel(Skill.MINING);
        this.skill.level[S_Skills.SMITHING.getSkillId()] = player.getSkillManager().getCurrentLevel(Skill.SMITHING);
        this.skill.level[S_Skills.SLAYER.getSkillId()] = player.getSkillManager().getCurrentLevel(Skill.SLAYER);
        this.skill.level[S_Skills.THIEVING.getSkillId()] = player.getSkillManager().getCurrentLevel(Skill.THIEVING);

        this.skill.experience[S_Skills.ATTACK.getSkillId()] = player.getSkillManager().getExperience(Skill.ATTACK);
        this.skill.experience[S_Skills.STRENGTH.getSkillId()] = player.getSkillManager().getExperience(Skill.STRENGTH);
        this.skill.experience[S_Skills.DEFENCE.getSkillId()] = player.getSkillManager().getExperience(Skill.DEFENCE);
        this.skill.experience[S_Skills.RANGED.getSkillId()] = player.getSkillManager().getExperience(Skill.RANGED);
        this.skill.experience[S_Skills.PRAYER.getSkillId()] = player.getSkillManager().getExperience(Skill.PRAYER);
        this.skill.experience[S_Skills.MAGIC.getSkillId()] = player.getSkillManager().getExperience(Skill.MAGIC);
        this.skill.experience[S_Skills.HITPOINTS.getSkillId()] = player.getSkillManager().getExperience(Skill.CONSTITUTION);
        this.skill.experience[S_Skills.CRAFTING.getSkillId()] = player.getSkillManager().getExperience(Skill.CRAFTING);
        this.skill.experience[S_Skills.MINING.getSkillId()] = player.getSkillManager().getExperience(Skill.MINING);
        this.skill.experience[S_Skills.SMITHING.getSkillId()] = player.getSkillManager().getExperience(Skill.SMITHING);
        this.skill.experience[S_Skills.SLAYER.getSkillId()] = player.getSkillManager().getExperience(Skill.SLAYER);
        this.skill.experience[S_Skills.THIEVING.getSkillId()] = player.getSkillManager().getExperience(Skill.THIEVING);

        this.skill.maxLevel[S_Skills.ATTACK.getSkillId()] = player.getSkillManager().getMaxLevel(Skill.ATTACK);
        this.skill.maxLevel[S_Skills.STRENGTH.getSkillId()] = player.getSkillManager().getMaxLevel(Skill.STRENGTH);
        this.skill.maxLevel[S_Skills.DEFENCE.getSkillId()] = player.getSkillManager().getMaxLevel(Skill.DEFENCE);
        this.skill.maxLevel[S_Skills.RANGED.getSkillId()] = player.getSkillManager().getMaxLevel(Skill.RANGED);
        this.skill.maxLevel[S_Skills.PRAYER.getSkillId()] = player.getSkillManager().getMaxLevel(Skill.PRAYER);
        this.skill.maxLevel[S_Skills.MAGIC.getSkillId()] = player.getSkillManager().getMaxLevel(Skill.MAGIC);
        this.skill.maxLevel[S_Skills.HITPOINTS.getSkillId()] = player.getSkillManager().getMaxLevel(Skill.CONSTITUTION);
        this.skill.maxLevel[S_Skills.CRAFTING.getSkillId()] = player.getSkillManager().getMaxLevel(Skill.CRAFTING);
        this.skill.maxLevel[S_Skills.MINING.getSkillId()] = player.getSkillManager().getMaxLevel(Skill.MINING);
        this.skill.maxLevel[S_Skills.SMITHING.getSkillId()] = player.getSkillManager().getMaxLevel(Skill.SMITHING);
        this.skill.maxLevel[S_Skills.SLAYER.getSkillId()] = player.getSkillManager().getMaxLevel(Skill.SLAYER);
        this.skill.maxLevel[S_Skills.THIEVING.getSkillId()] = player.getSkillManager().getMaxLevel(Skill.THIEVING);

        player.getPSettings().setSetting("settings-rewire", true);
        player.sendMessage("@red@[SYSTEM] Please re-login for skills to be adjusted.");
    }
}
