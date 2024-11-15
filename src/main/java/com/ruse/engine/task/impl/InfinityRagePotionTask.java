package com.ruse.engine.task.impl;

import com.ruse.engine.task.Task;
import com.ruse.model.*;
import com.ruse.model.Locations.Location;
import com.ruse.world.content.Consumables;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.skills.S_Skills;

import java.util.Objects;

public class InfinityRagePotionTask extends Task {

	public InfinityRagePotionTask(Player player) {
		super(1, player, true);
		this.player = player;
	}

	final Player player;

	@Override
	public void execute() {
		if (player == null || !player.isRegistered()) {
			stop();
			return;
		}
		int timer = player.getOverloadPotionTimer();
		if (timer == 600 || timer == 598 || timer == 596 || timer == 594 || timer == 592) {
			//player.processovlmode();
			player.sendTimerInter();
			player.performAnimation(new Animation(3170));
			player.dealDamage(new Hit(100, Hitmask.RED, CombatIcon.NONE));
		}

		Consumables.overloadIncrease(player, Skill.ATTACK, 0.67);
		Consumables.overloadIncrease(player, Skill.STRENGTH, 0.67);
		Consumables.overloadIncrease(player, Skill.DEFENCE, 0.67);
		Consumables.overloadIncrease(player, Skill.RANGED, 0.67);
		Consumables.overloadIncrease(player, Skill.MAGIC, 0.67);

		player.setOverloadPotionTimer(timer - 1);
		if (player.getOverloadPotionTimer() == 20)
			player.getPacketSender().sendMessage("@red@Your Rage Potion's effect is about to run out.");
		if (player.getOverloadPotionTimer() <= 0 && !player.getRank().isDeveloper()) {
			player.getPacketSender().sendMessage("@red@Your Rage Potion's effect has run out.");
			for (int i = 0; i < 7; i++) {
				if (i == 3 || i == 5)
					continue;
				//getPlayer().getNewSkills().setCurrentLevel(Objects.requireNonNull(S_Skills.forId(i)), getPlayer().getNewSkills().getMaxLevel(Objects.requireNonNull(S_Skills.forId(i))), true);
			}
			player.setOverloadPotionTimer(0);
			stop();
		}
	}
}
