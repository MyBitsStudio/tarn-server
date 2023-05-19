package com.ruse.model.input.impl;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.input.Input;
import com.ruse.world.entity.impl.player.Player;

public class EnterPinPacketListener extends Input {

	@Override
	public void handleSyntax(Player player, String pin) {
		if (pin.equalsIgnoreCase(player.getSavedPin())) {
			player.sendMessage("Pin correctly entered");
			player.getPlayerFlags().successPin();

		} else {
			player.getPSecurity().getPlayerLock().increase("pinAtt", pin);
			TaskManager.submit(new Task(1, player, false) {
				@Override
				protected void execute() {
					player.setInputHandling(new EnterPinPacketListener());
					player.getPacketSender().sendEnterInputPrompt("Enter your pin to play#confirmstatus");
					stop();
				}
			});
		}
	}
}
