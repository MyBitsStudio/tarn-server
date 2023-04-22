package com.ruse.engine.task.impl;

import com.ruse.engine.task.Task;
import com.ruse.world.entity.impl.player.Player;

public class DmgPotionTask extends Task {

	private final int type;
	public DmgPotionTask(Player player, int type) {
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

		if (player.getDmgPotionTimer() == 20)
			player.getPacketSender().sendMessage("@red@Your DR Potion's effect is about to run out.");
		if (player.getDmgPotionTimer() <= 0) {
			player.getPacketSender().sendMessage("@red@Your DR Potion's effect has run out.");
			player.setDmgPotionTimer(0);
			stop();
		}
	}
}
