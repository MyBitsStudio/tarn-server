package com.ruse.net.packet.impl;

import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.world.content.grandexchange.GrandExchange;
import com.ruse.world.entity.impl.player.Player;

public class TradingPostItemSelectListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int item = packet.readInt();
		if (item <= 0) return;
		ItemDefinition def = ItemDefinition.forId(item);
		if (def != null) {
			if (!Item.tradeable(item) || item == ItemDefinition.COIN_ID) {
				player.getPacketSender()
						.sendMessage("This item can currently not be purchased or sold in the trading post.");
				return;
			}
			if(player.getOverlayInterface() == 150857) {
				player.getTradingPost().sendHistoryData(item);
				return;
			}
			player.getTradingPost().searchItem(item);
		}
	}
}
