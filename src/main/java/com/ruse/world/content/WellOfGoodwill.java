package com.ruse.world.content;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.panels.PlayerPanel;

import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

//import com.ruse.tools.discord.DiscordConstant;
//import com.ruse.tools.discord.DiscordManager;

public class WellOfGoodwill {

	private static final int AMOUNT_NEEDED = 100000000; // 100m
	private static final int LEAST_DONATE_AMOUNT_ACCEPTED = 1000000; // 1m
	private static final int BONUSES_DURATION = 120; // 2 hours in minutes

	private static final CopyOnWriteArrayList<Player> DONATORS = new CopyOnWriteArrayList<Player>();
	private static WellState STATE = WellState.EMPTY;
	private static long START_TIMER = 0;
	private static int MONEY_IN_WELL = 0;

	public static void init() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("./data/saves/edgeville-well.txt"));
			if (in != null) {
				String line = in.readLine();
				if (line != null) {
					long startTimer = Long.parseLong(line);
					if (startTimer > 0) {
						STATE = WellState.FULL;
						START_TIMER = startTimer;
						MONEY_IN_WELL = AMOUNT_NEEDED;
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("./data/saves/edgeville-well.txt"));
			out.write("" + START_TIMER);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static void lookDownWell(Player player) {
//		if (checkFull(player)) {
//			return;
//		}
//		DialogueManager.start(player, new Dialogue() {
//
//			@Override
//			public DialogueType type() {
//				return DialogueType.NPC_STATEMENT;
//			}
//
//			@Override
//			public DialogueExpression animation() {
//				return DialogueExpression.NORMAL;
//			}
//
//			@Override
//			public String[] dialogue() {
//				return new String[] { "It looks like the well could hold another "
//						+ Misc.insertCommasToNumber("" + getMissingAmount() + "") + " coins." };
//			}
//
//			@Override
//			public int npcId() {
//				return 802;
//			}
//
//			@Override
//			public Dialogue nextDialogue() {
//				return DialogueManager.getDialogues().get(75);
//			}
//
//		});
//	}

	public static boolean checkFull(Player player) {
		return STATE == WellState.FULL;
	}

	public static void donate(Player player, int amount) {
		if (checkFull(player)) {
			player.sendMessage("The well is already full.");
			return;
		}
		if (amount < LEAST_DONATE_AMOUNT_ACCEPTED) {
			DialogueManager.sendStatement(player, "You must donate at least 1 million coins.");
			return;
		}
		if (amount > getMissingAmount()) {
			amount = getMissingAmount();
		}
		if (player.getInventory().getAmount(ItemDefinition.COIN_ID) < amount) {
			DialogueManager.sendStatement(player, "You do not have that much money in your inventory or money pouch.");
			return;
		}
		player.getInventory().delete(ItemDefinition.COIN_ID, amount);
		if (!DONATORS.contains(player)) {
			DONATORS.add(player);
		}
		MONEY_IN_WELL += amount;
		if (amount > 25000000) {
			World.sendMessage("<img=5> <col=6666FF>" + player.getUsername() + " has donated "
					+ Misc.insertCommasToNumber("" + amount + "") + " coins to the Well of Goodwill!");
		}
		DialogueManager.sendStatement(player, "Thank you for your donation.");
		if (getMissingAmount() <= 0) {
			STATE = WellState.FULL;
			START_TIMER = System.currentTimeMillis();
			World.sendMessage("<img=5> <col=6666FF>The Well of Goodwill has been filled!");
			World.sendMessage("<img=5> <col=6666FF>It is now granting everyone 2 hours of 30% bonus experience.");
			// new DiscordManager(DiscordConstant.EVENTS_CHANNEL, "Well Of Goodwill", "The
			// Well of Goodwill has been filled!").log1();
			// new DiscordManager(DiscordConstant.EVENTS_CHANNEL, "Well Of Goodwill", "It is
			// now granting everyone 2 hours of 30% bonus experience.").log1();
			World.getPlayers().forEach(p -> PlayerPanel.refreshPanel(p));
			// World.getPlayers().forEach(p -> p.getPacketSender().sendString(39163,
			// "@or2@Well of Goodwill: @yel@Active"));
		}
	}

	public static void updateState() {
		if (STATE == WellState.FULL) {
			if (getMinutesRemaining() <= 0) {
				World.sendMessage("<img=5> <col=6666FF>The Well of Goodwill is no longer granting bonus experience.");
				// new DiscordManager(DiscordConstant.EVENTS_CHANNEL, "Well Of Goodwill", "WOGW
				// Is no longer granting bonus experience. Donate to re-active it!").log1();
				// World.getPlayers().forEach(p -> p.getPacketSender().sendString(39163,
				// "@or2@Well of Goodwill: @yel@N/A"));
				setDefaults();
			}
		}
	}

	public static void setDefaults() {
		DONATORS.clear();
		STATE = WellState.EMPTY;
		START_TIMER = 0;
		MONEY_IN_WELL = 0;
		World.getPlayers().forEach(player -> PlayerPanel.refreshPanel(player));
	}

	public static int getMissingAmount() {
		return (AMOUNT_NEEDED - MONEY_IN_WELL);
	}

	public static int getMinutesRemaining() {

		return (BONUSES_DURATION - Misc.getMinutesPassed(System.currentTimeMillis() - START_TIMER));
	}

	public static boolean isActive() {
		updateState();
		return STATE == WellState.FULL;
	}

	public static boolean bonusLoyaltyPoints(Player player) {
		updateState();
		return STATE == WellState.FULL && DONATORS.contains(player);
	}

	public enum WellState {
		EMPTY, FULL
	}
}
