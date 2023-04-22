package com.ruse.engine.task.impl;

import com.ruse.engine.task.Task;
import com.ruse.model.PlayerRights;
import com.ruse.world.World;
import com.ruse.world.content.BonusXp;
import com.ruse.world.content.PotionHandler;
import com.ruse.world.content.skill.SkillManager;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class ExpPotionTask extends Task {

	private final int type;
	public ExpPotionTask(Player player, int type) {
		super(1, player, true);
		this.player = player;
		this.type = type;
	}

	final Player player;

	@Override
	public void execute() {
		if (player == null || !player.isRegistered()) {
			stop();
			return;
		}

		if (player.getExpPotionTimer() == 20)
			player.getPacketSender().sendMessage("@red@Your EXP Potion's effect is about to run out.");
		if (player.getExpPotionTimer() <= 0) {
			player.getPacketSender().sendMessage("@red@Your EXP Potion's effect has run out.");
			player.setExpPotionTimer(0);
			stop();
		}
	}
}
