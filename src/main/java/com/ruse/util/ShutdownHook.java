package com.ruse.util;

import java.util.logging.Logger;

import com.ruse.GameServer;
import com.ruse.io.ThreadProgressor;
import com.ruse.world.World;
import com.ruse.world.WorldIPChecker;
import com.ruse.world.content.LotterySystem;
import com.ruse.world.content.WellOfGoodwill;
import com.ruse.world.packages.clans.ClanManager;
import com.ruse.world.content.grandexchange.GrandExchangeOffers;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.PlayerHandler;
import com.ruse.world.packages.misc.ItemIdentifiers;

public class ShutdownHook extends Thread {

	/**
	 * The ShutdownHook logger to print out sendInformation.
	 */
	private static final Logger logger = Logger.getLogger(ShutdownHook.class.getName());

	@Override
	public void run() {
		logger.info("The shutdown hook is processing all required actions...");
		GameServer.setUpdating(true);
		for (Player player : World.getPlayers()) {
			if (player != null) {
				PlayerHandler.handleLogout(player, false);
			}
		}
		WellOfGoodwill.save();
		ClanManager.getManager().save();
		World.attributes.save();
		ItemIdentifiers.save();
		WorldIPChecker.getInstance().save();

		ServerPerks.getInstance().save();

		ThreadProgressor.shutdown();
		logger.info("The shudown hook actions have been completed, shutting the server down...");
	}
}
