package com.ruse.model.input.impl;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.input.Input;
import com.ruse.world.entity.impl.player.Player;

public class ChangePinPacketListener extends Input {

	@Override
	public void handleSyntax(Player player, String pin) {
		player.setSavedPin(pin);
		player.getPSecurity().changePin();
	}
}
