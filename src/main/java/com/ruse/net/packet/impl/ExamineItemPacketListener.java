package com.ruse.net.packet.impl;

import com.ruse.model.Item;
import com.ruse.model.Skill;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.BonusManager;
import com.ruse.world.content.Trading;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.misc.ItemIdentifiers;
import com.ruse.world.packages.slot.SlotEffect;
import com.ruse.world.packages.tradingpost.TradingPost;
import com.ruse.world.packages.tradingpost.models.Offer;

import java.util.Objects;
import java.util.Optional;

public class ExamineItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int item = packet.readShort();
		int slot = packet.readByte();
		Item items = null;
		//System.out.println("Item: " + item + " Slot: " + slot+" "+player.getInterfaceId());
		if(player.isBanking()){
			items = player.getBank(player.getCurrentBankTab()).getItems()[slot];
		} else if(player.getTrading().inTrade()) {
			if(player.getInterfaceId() == 3323){
				if(player.getTrading().offeredItems.size() < slot || player.getTrading().offeredItems.isEmpty()){
					if(World.getPlayers().get(player.getTrading().inTradeWith).getTrading().offeredItems.size() < slot || World.getPlayers().get(player.getTrading().inTradeWith).getTrading().offeredItems.isEmpty()){
						return;
					}
					if(World.getPlayers().get(player.getTrading().inTradeWith).getTrading().offeredItems.get(slot).getId() == item){
						items = World.getPlayers().get(player.getTrading().inTradeWith).getTrading().offeredItems.get(slot);
					} else {
						player.sendMessage("An error has occured. Please try re-offering your trade.");
						player.getTrading().declineTrade(true);
						return;
					}
				} else if(player.getTrading().offeredItems.get(slot).getId() == item) {
					items = player.getTrading().offeredItems.get(slot);
				} else if (World.getPlayers().get(player.getTrading().inTradeWith).getTrading().offeredItems.get(slot).getId() == item) {
					items = World.getPlayers().get(player.getTrading().inTradeWith).getTrading().offeredItems.get(slot);
				} else {
					player.sendMessage("An error has occured. Please try re-offering your trade.");
					player.getTrading().declineTrade(true);
					return;
				}
			}
		} else if(player.getInterfaceId() == TradingPost.BUYING_INTERFACE_ID){
			Offer offer = player.getTradingPost().getViewingOfferList().get(slot);
			items = new Item(offer.getItemId(), 1, offer.getUid());
		} else if(player.getInterfaceId() == TradingPost.MAIN_INTERFACE_ID){
			//System.out.println(player.getTradingPost().getMyOfferList().get(slot));
			Offer offer = player.getTradingPost().getMyOfferList().get(slot);
			items = new Item(offer.getItemId(), 1, offer.getUid());
		} else {
			items = player.getInventory().getItems()[slot];
		}
		if(item == ItemDefinition.COIN_ID || item == 10835) {
			player.getPacketSender().sendMessage("Mhmm... Shining coins...");
			return;
		}
		ItemDefinition itemDef = ItemDefinition.forId(item);
		if(itemDef != null) {
			player.getPacketSender().sendMessage(itemDef.getDescription());
			if (items != null) {
				if(ItemIdentifiers.itemIdentifiers.containsKey(items.getUid())){
					SlotEffect effect = SlotEffect.values()[Integer.parseInt(ItemIdentifiers.getItemIdentifier(items.getUid(), "PERK"))];
					int bonus = Integer.parseInt(ItemIdentifiers.getItemIdentifier(items.getUid(), "BONUS"));
					if(effect != null && !effect.equals(SlotEffect.NOTHING)){
						player.getPacketSender().sendMessage("This item has a @red@"+effect.name()+"@bla@ perk"+(bonus != 0 && bonus != -1 ? "with bonus of "+bonus+"%." : "."));
					}
				}

			}
		}
	}

}
