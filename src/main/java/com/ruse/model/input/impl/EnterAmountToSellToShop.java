package com.ruse.model.input.impl;

import com.ruse.model.input.EnterAmount;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.shops.ShopHandler;
import com.ruse.world.packages.shops.TabShop;

public class EnterAmountToSellToShop extends EnterAmount {

	public EnterAmountToSellToShop(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, int amount) {
		if (player.isShopping() && getItem() > 0 && getSlot() >= 0) {
			ShopHandler.sell(player, getSlot(), amount);
		} else
			player.getPacketSender().sendInterfaceRemoval();

	}

}
