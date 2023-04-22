package com.ruse.model.input.impl;

import com.ruse.model.Item;
import com.ruse.model.container.impl.Bank;
import com.ruse.model.input.EnterAmount;
import com.ruse.world.entity.impl.player.Player;

public class EnterAmountToIronBank extends EnterAmount {

	public EnterAmountToIronBank(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, int amount) {
		if (!player.isIronBanking())
			return;
		int item = player.getInventory().getItems()[getSlot()].getId();
		if (item != getItem())
			return;
		int invAmount = player.getInventory().getAmount(item);
		if (amount > invAmount)
			amount = invAmount;
		if (amount <= 0)
			return;
		player.getInventory().switchItem(player.getGroupIronmanBank(), new Item(item, amount,player.getInventory().getItems()[getSlot()].getEffect(), player.getInventory().getItems()[getSlot()].getBonus()),
				getSlot(), false, true);
	}
}
