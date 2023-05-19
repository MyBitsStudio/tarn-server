package com.ruse.model.input.impl;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.input.Input;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.entity.impl.player.Player;

public class Enter2FAPacketListener extends Input {

	@Override
	public void handleSyntax(Player player, String pin) {
		System.out.println("2FA entered: " + pin +" - " + SecurityUtils.getTOTPCode(player.getPSecurity().getFA()));
		if (pin.equalsIgnoreCase(SecurityUtils.getTOTPCode(player.getPSecurity().getFA()))){
			player.sendMessage("2FA correctly entered");
			player.getPlayerFlags().success2FA();
			TaskManager.submit(new Task(1, player, false) {
				@Override
				protected void execute() {
					player.setInputHandling(new RegisterIPName());
					player.getPacketSender().sendEnterInputPrompt("Do you wish to add this IP to your list : (yes/no)");
					stop();
				}
			});
		} else {
			System.out.println("2FA incorrectly entered! Please try again!");
			player.getPSecurity().getPlayerLock().increase("faAtt", pin);
			TaskManager.submit(new Task(1, player, false) {
				@Override
				protected void execute() {
					player.setInputHandling(new Enter2FAPacketListener());
					player.getPacketSender().sendEnterInputPrompt("Enter your 2FA to unlock#confirm2fa");
					stop();
				}
			});
		}
	}

	@Override
	public void handleAmount(Player player, int amount) {
		System.out.println("2FA entered integer: " + amount +" - " + SecurityUtils.getTOTPCode(player.getPSecurity().getFA()));
	}

	@Override
	public void handleLongAmount(Player player, long amount) {
		System.out.println("2FA entered long: " + amount +" - " + SecurityUtils.getTOTPCode(player.getPSecurity().getFA()));
	}
}
