package com.ruse.util;

import java.util.logging.Logger;

import com.ruse.GameServer;
import com.ruse.world.World;
import com.ruse.world.content.LotterySystem;
import com.ruse.world.content.WellOfGoodwill;
import com.ruse.world.packages.clans.ClanManager;
import com.ruse.world.content.grandexchange.GrandExchangeOffers;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.PlayerHandler;

public class ShutdownHook extends Thread {

	/**
	 * The ShutdownHook logger to print out sendInformation.
	 */
	private static final Logger logger = Logger.getLogger(ShutdownHook.class.getName());

	@Override
	public void run() {
		logger.info("The shutdown hook is processing all required actions...");
		// World.savePlayers();
		GameServer.setUpdating(true);
		for (Player player : World.getPlayers()) {
			if (player != null) {
				// World.deregister(player);
				PlayerHandler.handleLogout(player, false);
			}
		}
		WellOfGoodwill.save();
		GrandExchangeOffers.save();
		ClanManager.getManager().save();

		// PlayerOwnedShopManager.saveShops();
		//Shop.ShopManager.saveTaxShop();
		LotterySystem.saveTickets();
		ServerPerks.getInstance().save();
		logger.info("The shudown hook actions have been completed, shutting the server down...");
	}
}
