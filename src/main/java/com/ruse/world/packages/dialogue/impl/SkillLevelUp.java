package com.ruse.world.packages.dialogue.impl;

import com.ruse.model.Skill;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

public class SkillLevelUp extends Dialogue {

    private final Skill skill;
    public SkillLevelUp(Player player, Skill skill) {
        super(player);
        this.skill = skill;
    }

    @Override
    public DialogueType type() {
        return DialogueType.STATEMENT;
    }

    @Override
    public DialogueExpression animation() {
        return null;
    }

    @Override
    public String[] items() {
        return new String[0];
    }

    @Override
    public void next(int stage) {

    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        return false;
    }
}
