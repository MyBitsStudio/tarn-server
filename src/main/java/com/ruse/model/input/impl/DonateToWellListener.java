package com.ruse.model.input.impl;

import com.ruse.model.input.EnterAmount;
import com.ruse.model.input.Input;
import com.ruse.world.content.globalBoss.GlobalBoss;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.globals.GlobalBossManager;

public class DonateToWellListener extends EnterAmount {
	public DonateToWellListener(Player player, int id) {
		super(id);
		player.getVariables().setSetting("item-chosen", id);
	}

//	 player.setInputHandling(new ChangePinPacketListener());
//                player.getPacketSender().sendEnterInputPrompt("Enter a new pin");

	@Override
	public void handleAmount(Player player, int amount) {
		int item = player.getVariables().getIntValue("item-chosen");
		if(!player.getInventory().contains(item)){
			player.getPacketSender().sendMessage("You do not have any of that item.");
			return;
		}
		if(player.getInventory().getAmount(item) < amount){
			amount = player.getInventory().getAmount(item);
		}
		GlobalBossManager.getInstance().addToWell(player, item, amount);
	}
}
