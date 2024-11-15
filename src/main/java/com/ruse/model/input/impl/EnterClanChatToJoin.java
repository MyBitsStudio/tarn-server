package com.ruse.model.input.impl;

import com.ruse.model.input.Input;
import com.ruse.world.packages.clans.ClanManager;
import com.ruse.world.entity.impl.player.Player;

public class EnterClanChatToJoin extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if (syntax.length() <= 1) {
			player.getPacketSender().sendMessage("Invalid syntax entered.");
			return;
		}
		ClanManager.getManager().joinChat(player, syntax);
	}
}
