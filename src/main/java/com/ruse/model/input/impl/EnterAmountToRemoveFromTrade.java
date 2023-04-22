package com.ruse.model.input.impl;

import com.ruse.model.input.EnterAmount;
import com.ruse.model.projectile.ItemEffect;
import com.ruse.world.entity.impl.player.Player;

public class EnterAmountToRemoveFromTrade extends EnterAmount {

	public EnterAmountToRemoveFromTrade(int item, ItemEffect effect) {
		super(item, effect);
	}

	@Override
	public void handleAmount(Player player, int amount) {
		if (player.getTrading().inTrade() && getItem() > 0)
			player.getTrading().removeTradedItem(getItem(), amount, getEffect());
		else
			player.getPacketSender().sendInterfaceRemoval();
	}

}
