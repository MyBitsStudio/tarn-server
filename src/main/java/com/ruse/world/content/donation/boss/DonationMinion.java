package com.ruse.world.content.donation.boss;

import com.ruse.model.Position;
import com.ruse.model.definitions.NPCDrops;
import com.ruse.world.content.combat.CombatBuilder;
import com.ruse.world.content.combat.CombatFactory;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.donation.DonationManager;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import java.util.*;

public class DonationMinion extends NPC {
    public DonationMinion(Position pos) {
        super(586, pos, false);
        this.getDefinition().setAggressive(true);
        this.getDefinition().setMulti(true);
        this.getDefinition().setRespawnTime(-1);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new MinionCombat();
    }


}
