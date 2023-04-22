package com.ruse.engine.task.impl;

import com.ruse.engine.task.Task;
import com.ruse.world.entity.impl.player.Player;

public class DdrPotionTask extends Task {

	private final int type;
	public DdrPotionTask(Player player, int type) {
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

		if (player.getDdrPotionTimer() == 20)
			player.getPacketSender().sendMessage("@red@Your DR Potion's effect is about to run out.");
		if (player.getDdrPotionTimer() <= 0) {
			player.getPacketSender().sendMessage("@red@Your DR Potion's effect has run out.");
			player.setDdrPotionTimer(0);
			stop();
		}
	}
}
