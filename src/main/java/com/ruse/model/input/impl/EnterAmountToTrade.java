package com.ruse.model.input.impl;

import com.ruse.model.input.EnterAmount;
import com.ruse.model.projectile.ItemEffect;
import com.ruse.world.entity.impl.player.Player;

public class EnterAmountToTrade extends EnterAmount {

	public EnterAmountToTrade(int item, int slot, ItemEffect effect) {
		super(item, slot, effect == null ? ItemEffect.NOTHING : effect);
	}

	@Override
	public void handleAmount(Player player, int amount) {
		if (player.getTrading().inTrade() && getItem() > 0) {
			player.getTrading().tradeItem(getItem(), amount, getSlot(), getEffect() == null ? ItemEffect.NOTHING : getEffect());
		}else{
			player.getPacketSender().sendInterfaceRemoval();
		}
	}

}
