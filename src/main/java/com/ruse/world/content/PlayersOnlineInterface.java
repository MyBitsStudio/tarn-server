package com.ruse.world.content;

import java.util.concurrent.CopyOnWriteArrayList;

import com.ruse.GameSettings;
import com.ruse.util.Misc;
import com.ruse.util.Stopwatch;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.impl.GroupIronman;
import com.ruse.world.packages.mode.impl.Ironman;
import com.ruse.world.packages.mode.impl.UltimateIronman;
import com.ruse.world.packages.mode.impl.Veteran;
import org.jetbrains.annotations.NotNull;

public class PlayersOnlineInterface {

	private static final Stopwatch lastResort = new Stopwatch();
	private final static CopyOnWriteArrayList<Player> PLAYERS_ONLINE_LIST = new CopyOnWriteArrayList<Player>();

	public static void add(Player player) {
		PLAYERS_ONLINE_LIST.add(player);
	}

	public static void remove(Player player) {
		PLAYERS_ONLINE_LIST.remove(player);
	}

	private static void resort() {
		if (!lastResort.elapsed(1000)) {
			return;
		}
		lastResort.reset();
		PLAYERS_ONLINE_LIST.sort((arg0, arg1) -> {
			int value1 = getValue(arg0);
			int value2 = getValue(arg1);
			return Integer.compare(value2, value1);
		});
	}

	private static void clearInterface(Player player) {
		for (int i = 57042; i < 57141; i++) {
			player.getPacketSender().sendString(i, "");
		}
		int i = 57008;
		player.getPacketSender().sendString(i++, "Name:");
		player.getPacketSender().sendString(i++, "Rank:");
		player.getPacketSender().sendString(i++, "Time Played:");
		player.getPacketSender().sendString(i++, "Claimed:");
		player.getPacketSender().sendString(i++, "Game Mode:");
		player.getPacketSender().sendString(i++, "Combat Level:");
		player.getPacketSender().sendString(i++, "Total Level:");
		player.getPacketSender().sendString(i++, "Slayer Points:");
		player.getPacketSender().sendString(i++, "Voting Points:");
		player.getPacketSender().sendString(i++, "Commendations:");
		player.getPacketSender().sendString(i++, "Dung Points:");
		player.getPacketSender().sendString(i++, "Pk Points:");
		player.getPacketSender().sendString(i++, "Wild Streak:");
		player.getPacketSender().sendString(i++, "Wild Kills:");
		player.getPacketSender().sendString(i++, "Wil Deaths:");
		player.getPacketSender().sendString(i++, "Arena Victories:");
		player.getPacketSender().sendString(i++, "Arena Losses:");
	}

	private static void sendInterfaceData(Player player) {
		int child = 57042;
		int players = World.getPlayers().size() + GameSettings.players;
		for (int i = 0; i < players; i++) {
			if (i >= PLAYERS_ONLINE_LIST.size()) {
				player.getPacketSender().sendString(child, "   N/A");
				child++;
				continue;
			}
			Player p = PLAYERS_ONLINE_LIST.get(i);
			if (p == null)
				continue;
			int rankId = p.getRank().ordinal();
			if (rankId == 0) {
				if (p.getMode() instanceof Ironman) {
					rankId = 840;
				} else if (p.getMode() instanceof UltimateIronman) {
					rankId = 839;
				} else if (p.getMode() instanceof Veteran) {
					rankId = 838;
				} else if (p.getMode() instanceof GroupIronman) {
					rankId = 1509;
				}
			}
			player.getPacketSender().sendString(child,
					(rankId > 0 ? "<img=" + rankId + ">" : "  ") + p.getUsername());
			child++;
		}
	}

	public static void showInterface(Player player) {
		resort();
		clearInterface(player);
		sendInterfaceData(player);
		int players = (int) World.getPlayers().size() + GameSettings.players;
		player.getPacketSender().sendString(57003, "Players:  @gre@" + (players) + "")
				.sendInterface(57000);
	}

	private static void updateInterface(Player player, int index) {
		if (index >= PLAYERS_ONLINE_LIST.size() || PLAYERS_ONLINE_LIST.get(index) == null) {
			showInterface(player);
			player.getPacketSender().sendMessage("That player is currently unavailable.");
			return;
		}
		Player player2 = PLAYERS_ONLINE_LIST.get(index);
		player.setPlayerViewingIndex(index);
		player.getPacketSender().sendString(57008, "Name: @whi@" + player2.getUsername())
				.sendString(57009, "Rank: @whi@" + Misc.formatText(player2.getRank().name().toLowerCase()))
				.sendString(57010,
						"Time Played: @whi@" + Misc
								.getHoursPlayed((player2.getTotalPlayTime() + player2.getRecordedLogin().elapsed())))
				.sendString(57011, "Claimed: @whi@$" + player2.getAmountDonated())
				.sendString(57012, "Game Mode: @whi@")
				.sendString(57013, "Combat Level: @whi@" + player2.getSkillManager().getCombatLevel())
				.sendString(57014, "Total Level: @whi@ " + player2.getSkillManager().getTotalLevel())
				.sendString(57015, "Slayer Points: @whi@" + player2.getPointsHandler().getSlayerPoints())
				.sendString(57016, "Voting Points: @whi@" + player2.getPointsHandler().getVotingPoints())
				.sendString(57017, "Commendations: @whi@" + player2.getPointsHandler().getCommendations())
				.sendString(57018, "Dung Tokens: @whi@" + player2.getPointsHandler().getDungeoneeringTokens())
				.sendString(57019, "Pk Points: @whi@" + player2.getPointsHandler().getPkPoints())
				.sendString(57020, "Wild Streak: @whi@" + player2.getPlayerKillingAttributes().getPlayerKillStreak())
				.sendString(57021, "Player Kills: @whi@" + player2.getPlayerKillingAttributes().getPlayerKills())
				.sendString(57022, "Wild Deaths: @whi@" + player2.getPlayerKillingAttributes().getPlayerDeaths())
				.sendString(57023, "Arena Victories: @whi@" + player2.getDueling().arenaStats[0])
				.sendString(57024, "Arena Losses: @whi@" + player2.getDueling().arenaStats[1]);
	}

	public static boolean handleButton(Player player, int button) {
		if (button >= -8494 && button <= -8407) {
			int index = (button - (-8494));
			updateInterface(player, index);
			return true;
		}
		if (button == -8511 || button == -8508) {
			Player p = player.getPlayerViewingIndex() < PLAYERS_ONLINE_LIST.size()
					? PLAYERS_ONLINE_LIST.get(player.getPlayerViewingIndex())
					: null;
			if (p == null) {
				player.getPacketSender().sendMessage("Please select an active player.");
				return true;
			}
			player.getPacketSender().sendString(button == -8511 ? 57025 : 57028, p.getUsername());
			return true;
		}
		return false;
	}

	private static int getValue(@NotNull Player p) {
		int value = switch (p.getRank()) {
			case PLAYER -> 0;
			case ADMINISTRATOR -> 11;
			case DEVELOPER -> 13;
			case MODERATOR -> 10;
			case HELPER -> 8;
			default -> 0;
		};
		if (value == 0) {
			if (p.getMode() instanceof Ironman) {
				value = 1;
			} else if (p.getMode() instanceof UltimateIronman) {
				value = 2;
				
			}else if (p.getMode() instanceof Veteran) {
				value = 3;
				
			}
		}
		return value;
	}
}
