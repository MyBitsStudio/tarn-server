package com.ruse.model.input.impl;

import com.ruse.model.input.Input;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.entity.impl.player.Player;

public class Enter2FAFirstPacketListener extends Input {

	@Override
	public void handleSyntax(Player player, String pin) {
		if(pin.equals(SecurityUtils.getTOTPCode(player.getPSecurity().getFA()))){
			player.sendMessage("2FA correctly entered! Make sure not to lose this!");
			player.getPlayerFlags().successFirst2FA();
			player.getPSecurity().save();
		} else {
			player.getPSecurity().setFA("");
			player.sendMessage("2FA incorrectly entered! Please try again!");
		}
	}
}
