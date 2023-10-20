package com.ruse.world.packages.pets;

import com.ruse.world.entity.impl.npc.NPC;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class Companion extends NPC {

    private int xp = 0, level = 1;
    private final CompanionData data;
    @Setter
    private CompanionSkills skill;
    private final CompanionSkills[] availableSkills;

    private final AtomicBoolean active = new AtomicBoolean(false);

    public Companion(CompanionData data){
        super(data.getNpcId());
        this.data = data;
        this.availableSkills = data.getSkills();
        this.skill = availableSkills[0];
    }

    public Companion(CompanionData data, int xp, int level){
        super(data.getNpcId());
        this.data = data;
        this.availableSkills = data.getSkills();
        this.skill = availableSkills[0];
        this.xp = xp;
        this.level = level;
    }

    public void addXP(int amount){
        xp += amount;
        adjustLevel();
    }

    public int xpRequiredForNextLevel(){
        return (int) ((level + 1) * (100.0 * ((double) (level + 1) / 2)));
    }
    private void adjustLevel(){
        int xpReq = xpRequiredForNextLevel();
        if(xp >= xpReq){
            if(level >= 100)
                return;

            level++;
            xp -= xpReq;
            getSpawnedFor()[0].sendMessage("Your companion has leveled up to level " + level + "!");
            adjustLevel();
        }
    }
}
