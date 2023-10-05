package com.ruse.model;

import com.ruse.GameSettings;
import com.ruse.model.RegionInstance.RegionInstanceType;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.WorldIPChecker;
import com.ruse.world.content.KillsTracker;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.content.PlayerPunishment.Jail;
import com.ruse.world.content.Zulrah;
import com.ruse.world.content.aura.AuraRaids;
import com.ruse.world.content.combat.strategy.impl.Scorpia;
import com.ruse.world.content.minigames.impl.*;
import com.ruse.world.content.minigames.impl.dungeoneering.DungeoneeringParty;
import com.ruse.world.content.progressionzone.ProgressionZone;
import com.ruse.world.content.skill.impl.old_dungeoneering.Dungeoneering;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.content.zombie.ZombieRaids;
import com.ruse.world.entity.Entity;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Locations {
	public static boolean inside(Position start, int startSize, Position target, int targetSize) {
		if (target == null) {
			return false;
		}
		int distanceX = start.getX() - target.getX();
		int distanceY = start.getY() - target.getY();
		if (distanceX < targetSize && distanceX > -startSize && distanceY < targetSize && distanceY > -startSize) {
			return true;
		}
		return false;
	}
	public static void login(Player player) {
		player.setLocation(Location.getLocation(player));
		player.getLocation().login(player);
		player.getLocation().enter(player);
	}

	public static void logout(Player player) {
		player.getLocation().logout(player);
		if (player.getRegionInstance() != null)
			player.getRegionInstance().destruct();
		if (player.getLocation() != Location.GODWARS_DUNGEON) {
			player.getLocation().leave(player);
		}
	}

	public static int PLAYERS_IN_WILD;
	public static int PLAYERS_IN_DUEL_ARENA;

	public enum Location {

		KEEPERS_OF_LIGHT_LOBBY(new int[] { 2304, 2344 }, new int[] { 4992, 5050 }, false, false, true, false, false, true) {

			@Override
			public void process(Player player) {
			}

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public void leave(Player player) {
				//TODO broken code remove?
				if (player.getLocation() != KEEPERS_OF_LIGHT_GAME) {
					KeepersOfLight.leave(player, true);
				}
			}

			@Override
			public void logout(Player player) {
				KeepersOfLight.leave(player, true);
			}
		},

		KEEPERS_OF_LIGHT_GAME(new int[] { 2345, 2431 }, new int[] { 4992, 5054 }, true, false, true, false, true, true) {

			/*@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender()
						.sendMessage("Teleport spells are blocked on this island. Win or die!");
				return false;
			}*/

			@Override
			public void leave(Player player) {
				KeepersOfLight.leave(player, true);
			}

			@Override
			public void logout(Player player) {
				KeepersOfLight.leave(player, true);
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				return true;
			}

			@Override
			public void onDeath(Player player) {
				KeepersOfLight.leave(player, true);
			}
		},
		EXODEN(new int[] { 2562, 2600}, new int[] { 4485, 4526},
				true, true, true, false, false, true) {},


		LUCIFER(new int[] { 2301, 2367}, new int[] { 3970, 4024},
				false, true, true, false, false, true) {},
		ZOMBIE(new int[]{2712, 2740}, new int[]{2633, 2661}, true, false, true, false, true, true) {
			@Override
			public void logout(Player player) {

			}

			@Override
			public void leave(Player player) {

			}

			@Override
			public void login(Player player) {
			}

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public void enter(Player player) {
//				player.setRegionInstance(new RegionInstance(player, RegionInstance.RegionInstanceType.ZOMBIE));
//				player.getPacketSender().sendInteractionOption("null", 2, true);
			}

			@Override
			public void onDeath(Player player) {
			}

			@Override
			public void process(Player player) {
				//if (player.getZombieParty() != null)
				//player.getZombieParty().refreshInterface();
			}

		},
		ZOMBIE_LOBBY(new int[]{2691, 2706}, new int[]{2639, 2655}, true, false, true, false, true, true) {
			@Override
			public void leave(Player player) {
			}

			@Override
			public void enter(Player player) {
			}

			@Override
			public void login(Player player) {

			}

			@Override
			public void process(Player player) {

			}
		},


		AURA(new int[]{2637, 2655}, new int[]{3035, 3053}, true, false, true, false, true, true) {
			@Override
			public void logout(Player player) {

			}

			@Override
			public void leave(Player player) {

			}

			@Override
			public void login(Player player) {
			}

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public void enter(Player player) {
			}

			@Override
			public void onDeath(Player player) {
			}

			@Override
			public void process(Player player) {
			}

		},
		AURA_LOBBY(new int[]{2656, 2666}, new int[]{3039, 3049}, true, false, true, false, true, true) {
			@Override
			public void leave(Player player) {
			}

			@Override
			public void enter(Player player) {

			}

			@Override
			public void login(Player player) {

			}

			@Override
			public void process(Player player) {

			}
		},

		FREIZA(new int[] { 2433, 2494 }, new int[] { 2817, 2878 }, false, true, true, false, false, false) {
		}, //minigame??? 2738, 5081
		//2922, 2790
		CRYSTALQUEEN(new int[] { 2858, 2878 }, new int[] { 9933, 9959 }, true, true, true, false, false, false) {
		},  /// ?????
		TRANSFORMER(new int[] { 3274, 3311 }, new int[] { 3013, 3036 }, true, true, true, false, false, false) {
		}, /// skilling/quest area
		MAGEBANK_SAFE(new int[] { 2525, 2550 }, new int[] { 4707, 4727 }, true, true, true, false, false, false) {

		},

		// xyyx
//		ZONES3(new int[] { 1665, 1725 }, new int[] { 5590, 5611 }, true, true, true, false, false, false) {
//		}, // delete punchbags // 1703, 5600 //2372, 5113 -- guantlet (jad)

		CUSTOMINI(new int[] { 2567, 2597 }, new int[] { 2564, 2600 }, false, true, true, true, false, false) {
		},
		// xyyx
		GENERAL(new int[] { 2319, 2351 }, new int[] { 9806, 9828 }, true, true, true, true, false, false) {
		}, // minigame
		TRAININZONEMULTI(new int[] { 2504, 2551 }, new int[] { 2502, 2562 }, false, true, true, false, false, false) {
		},

//		CUSTOM_RAIDS_LOBBY(new int[] { 2715, 2735 }, new int[] { 2730, 2745 }, true, false, true, false, true, false) {
//			@Override
//			public void login(Player player) {
//
//
//			}
//
//			@Override
//			public void logout(Player player) {
//				Dungeoneering.leave(player, false, true);
//			}
//
//			@Override
//			public void leave(Player player) {
//				Dungeoneering.raidCount = 0;
//				player.getPacketSender().sendWalkableInterface(29050, false);
//				player.getPacketSender().sendInteractionOption("null", 7, false);
//			}
//
//			@Override
//			public void enter(Player player) {
//				player.getPacketSender().sendWalkableInterface(29050, true);
//				player.getPacketSender().sendInteractionOption("Invite To Raid Party", 7, false);
//			}
//
//			@Override
//			public void onDeath(Player player) {
//				Dungeoneering.leave(player, false, true);
//			}
//
//			@Override
//			public boolean handleKilledNPC(Player killer, NPC npc) {
//				return false;
//			}
//
//			@Override
//			public void process(Player player) {
//				// // System.out.println("In here");
//			}
//		},
//
//		CUSTOM_RAIDS(new int[] { 2702, 2730 }, new int[] { 2702, 2730 }, true, false, true, false, true, false) {
//			@Override
//			public void login(Player player) {
//
//			}
//
//			@Override
//			public void logout(Player player) {
//				Dungeoneering.leave(player, false, true);
//			}
//
//			@Override
//			public void leave(Player player) {
//
//				// player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getPlayers().size());
//				// player.getPacketSender().sendWalkableInterface(-1);
//				// Dungeoneering.leave(player, false, true);
//				// Dungeoneering.leave(player, true, true);
//			}
//
//			@Override
//			public void enter(Player player) {
//			}
//
//			@Override
//			public void onDeath(Player player) {
//				Dungeoneering.handlePlayerDeath(player);
//			}
//
//			@Override
//			public boolean handleKilledNPC(Player killer, NPC npc) {
//				return false;
//			}
//
//			@Override
//			public void process(Player player) {
//				if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() == null) {
//					TeleportHandler.teleportPlayer(player, new Position(2722, 2737),
//							player.getSpellbook().getTeleportType());
//				}
//			}
//		},



//		DUNGEONEERING(new int[] { 2240, 2303 }, new int[] { 4992, 5053 }, true, false, true,
//				false, true, false) {
//			@Override
//			public void login(Player player) {
//				player.getPacketSender().sendDungeoneeringTabIcon(true).sendTabInterface(GameSettings.QUESTS_TAB, 27224).sendTab(GameSettings.QUESTS_TAB);
//
//				DungeoneeringParty.clearInterface(player);
//				if (player.getPlayerInteractingOption() != PlayerInteractingOption.INVITE) {
//					player.getPacketSender().sendInteractionOption("Invite", 2, true);
//				}
//			}
//
//			@Override
//			public void leave(Player player) {
//				com.ruse.world.content.minigames.impl.dungeoneering.Dungeoneering.Companion.leaveLobby(player);
//			}
//
//			@Override
//			public void enter(Player player) {
//				if (player.getPlayerInteractingOption() != PlayerInteractingOption.INVITE) {
//					player.getPacketSender().sendInteractionOption("Invite", 2, true);
//				}
//
//				if(player.getMinigameAttributes().getDungeoneeringAttributes().getParty() == null) {
//					player.getPacketSender().sendDungeoneeringTabIcon(true).sendTabInterface(GameSettings.QUESTS_TAB, 27224).sendTab(GameSettings.QUESTS_TAB);
//					DungeoneeringParty.clearInterface(player);
//				}
//			}



/*
			@Override
			public void leave(Player player) {
				if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() != null) {
					if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner().equals(player)) {
						World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.DUNGEONEERING, player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getHeight(), player));
					}
				}
				com.ruse.world.content.minigames.impl.dungeoneering.Dungeoneering.Companion.leaveLobby(player);
			}

			@Override
			public void enter(Player player) {
				player.getPacketSender().sendCameraNeutrality();
				player.setRegionInstance(new RegionInstance(player, RegionInstance.RegionInstanceType.DUNGEONEERING));
			}*/
	//	},


//		DUNGEONEERING_ROOM(new int[] { 2240, 2303 }, new int[] { 4992, 5027 }, true, false, true,
//				false, true, false) {
//		},
		// Location(int[] x, int[] y, boolean multi, boolean summonAllowed, boolean
		// followingAllowed, boolean cannonAllowed, boolean firemakingAllowed, boolean
		// aidingAllowed) {

		HOME_BANK(new int[] { 2698, 2724 }, new int[] { 2968, 2994 }, false, true, true, false, true, true) {
		},
		VAULT_OF_WAR(new int[] { 1729, 1791 }, new int[] { 5313, 5375 }, false, true, true, false, false, false) {

		},

		///// HERE //////
		SLASH_BASH(new int[] { 2504, 2561 }, new int[] { 9401, 9473 }, true, true, true, true, true, true) {
		},
		BANDOS_AVATAR(new int[] { 2340, 2396 }, new int[] { 4929, 4985 }, true, true, true, true, true, true) {
		},
		KALPHITE_QUEEN(new int[] { 3464, 3500 }, new int[] { 9478, 9523 }, true, true, true, true, true, true) {
		},
		PHOENIX(new int[] { 2824, 2862 }, new int[] { 9545, 9594 }, true, true, true, true, true, true) {
		},
		// BANDIT_CAMP(new int[]{3020, 3150, 3055, 3195}, new int[]{3684, 3711, 2958,
		// 3003}, true, true, true, true, true, true) {
		// },
		ROCK_CRABS(new int[] { 2689, 2727 }, new int[] { 3691, 3730 }, true, true, true, true, true, true) {
		},
		ARMOURED_ZOMBIES(new int[] { 3077, 3132 }, new int[] { 9657, 9680 }, true, true, true, true, true, true) {
		},
		CORPOREAL_BEAST(new int[] { 2879, 2962 }, new int[] { 4368, 4413 }, true, true, true, false, true, true) {
			@Override
			public void process(Player player) {
				int x1 = 2889;
				int x2 = 2908;
				int y1 = 4381;
				int y2 = 4403;
				int currentx = player.getPosition().getX();
				int currenty = player.getPosition().getY();

				boolean safe = currentx >= x1 && currentx <= x2 && currenty >= y1 && currenty <= y2;
				if (safe) {
					// player.getPacketSender().sendMessage("You are safe");
					player.getPacketSender().sendWalkableInterface(16152, false);// .sendMessage("sendwalkint-1");
					/*
					 * player.setWalkableInterfaceId(-1);
					 * player.getPacketSender().sendMessage("setwalkint-1");
					 * player.getPacketSender().sendInterfaceRemoval().sendMessage("sendintremoval")
					 * ; player.getPacketSender().sendInterfaceReset().sendMessage("sendintreset");
					 */
				} else {
					// player.getPacketSender().sendMessage("Get out of the gas!");
					player.dealDamage(new Hit(Misc.getRandom(15) * 10, Hitmask.DARK_PURPLE, CombatIcon.CANNON));
					if (player.getWalkableInterfaceId() != 16152) {
						player.getPacketSender().sendWalkableInterface(16152, true);
					}
					// player.setWalkableInterfaceId(16152);
				}
			}

		},
		DAGANNOTH_DUNGEON(new int[] { 2886, 2938 }, new int[] { 4431, 4477 }, true, true, true, false, true, true) {
		},
		WILDERNESS(new int[] { 2940, 3392, 2986, 3012, 3653, 3720, 3650, 3653, 3150, 3199, 2994, 3041 },
				new int[] { 3523, 3968, 10338, 10366, 3441, 3538, 3457, 3472, 3796, 3869, 3733, 3790 }, false, true,
				true, true, true, true) {


			// -- Contains Any requires something to be there, so if you currently do NOT HAVE an actually allowed item everywhere it will crash

			private final int[] ALLOWED_ITEMS = new int[] { 1157,1165,1161,1159,1163,5574,5575,5576,10828,1119,1125,1121,1123,1127,3140,4087,3751,1069,1077,1071,1079,1073,6128,6129,6130,3753,1193,1195,1197,1199,1201,10589,6809,10564,4131,1321,1323,1325,1327,1329,1331,1333,4587,1291,1293,1295,1297,1299,1301,1303,1422,1420,1424,1426,1428,1430,1432,6528,4153,1307,1309,1311,1313,1315,1318,1319,14017,1009,1796,1654,1656,1658,1712,1710,1708,1706,1731,1725,1727,1729,1478,1635,1637,1639,1641,1643,2550,2570,11118,11113,325,339,329,361,379,373,7946,385,391,2442,2436,2440,2444,3040,6685,2452,3024,2434,2446,882,884,886,888,890,892,11212,810,811,11230,877,9140,9144,9142,9143,9144,9240,9241,9243,9244,9242,9341,9245,864,865,866,867,868,550,551,552,553,554,555,556,557,558,559,560,561,562,563,564,565,566,567,568,569,570,8007,8008,8009,8010,8011,8012,8013,8012,10499,841,843,845,847,849,851,853,855,857,859,861,1167,1129,1095,1063,9174,9177,9179,9181,9183,9185,10370,10372,10368,6328,3749,2499,2493,2487,2501,2495,2489,2503,2497,2491,1381,1383,1385,1387,4675,6914,6889,6918,6916,6924,6922,6920,4089,4091,4093,4095,4097,4099,4101,4103,4105,4107,4109,4111,4113,4115,4117,14499,14497,14501,7400,2412,2413,2414,7399,7398 };
			@Override
			public void process(Player player) {
				int x = player.getPosition().getX();
				int y = player.getPosition().getY();
				boolean ghostTown = x >= 3650 && y <= 3538;
				/*for (Item item : player.getEquipment().getItems()) {



					if(item.getId()!=-1&&!Arrays.stream(ALLOWED_ITEMS).anyMatch(i -> i == item.getId())) {
					player.sendMessage("<shad=1>@red@<img=18>Important Alert:");
					player.sendMessage("<shad=1>@red@<img=18>You are bringing one of the items that are not allowed!");
					player.sendMessage("<shad=1>@red@<img=18>Please type ::pvpitems to know what items are allowed!");
					Position[] locations = new Position[] { new Position(2375, 4021, 0), new Position(2375, 4022, 0) };
					Position teleportLocation = locations[RandomUtility.exclusiveRandom(0, locations.length)];
					TeleportHandler.teleportPlayer(player, teleportLocation, player.getSpellbook().getTeleportType());
					return;
				}}

for (Item item : player.getInventory().getItems()) {



					if(item.getId()!=-1&&!Arrays.stream(ALLOWED_ITEMS).anyMatch(i -> i == item.getId())) {

					player.sendMessage("<shad=1>@red@<img=18>Important Alert:");
					player.sendMessage("<shad=1>@red@<img=18>You are bringing one of the items that are not allowed!");
					player.sendMessage("<shad=1>@red@<img=18>Please type ::pvpitems to know what items are allowed!");
					Position[] locations = new Position[] { new Position(2375, 4021, 0), new Position(2375, 4022, 0) };
					Position teleportLocation = locations[RandomUtility.exclusiveRandom(0, locations.length)];
					TeleportHandler.teleportPlayer(player, teleportLocation, player.getSpellbook().getTeleportType());
					return;
				}}*/

				/*if (player.isFlying()) {
					player.getPacketSender().sendMessage("You cannot fly in the Wilderness.");
					player.setFlying(false);
					player.newStance();
				}
				if (player.isGhostWalking()) {
					player.getPacketSender().sendMessage("You cannot ghost walk in the Wilderness.");
					player.setGhostWalking(false);
					player.newStance();
				}*/
				/*
				 * boolean banditCampA = x >= 3020 && x <= 3150 && y >= 3684 && y <= 3711;
				 * boolean banditCampB = x >= 3055 && x <= 3195 && y >= 2958 && y <= 3003;
				 * if(banditCampA || banditCampB) { }
				 */
				/*if (ghostTown) {
					player.setWildernessLevel(60);

				} else {
					player.setWildernessLevel(((((y > 6400 ? y - 6400 : y) - 3520) / 8) + 1));
				}
				player.getPacketSender().sendString(42023, "" + player.getWildernessLevel());
				// player.getPacketSender().sendString(25355, "Levels:
				// "+CombatFactory.getLevelDifference(player, false) +" -
				// "+CombatFactory.getLevelDifference(player, true));
				BountyHunter.process(player);*/
			}

			@Override
			public void leave(Player player) {
				/*player.getPacketSender().sendWalkableInterface(42020, false);
				if (player.getLocation() != this) {
					player.getPacketSender().sendString(19000,
							"Combat level: " + player.getSkillManager().getCombatLevel());
					player.getUpdateFlag().flag(Flag.APPEARANCE);
				}
				PLAYERS_IN_WILD--;*/
			}

			@Override
			public void enter(Player player) {
				/*player.getPacketSender().sendInteractionOption("Attack", 2, true);
				player.getPacketSender().sendWalkableInterface(42020, true);
				player.getPacketSender().sendString(19000,
						"Combat level: " + player.getSkillManager().getCombatLevel());
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				PLAYERS_IN_WILD++;*/
			}

			@Override
			public boolean canTeleport(Player player) {
				/*if (Jail.isJailed(player.getUsername())) {
					player.getPacketSender().sendMessage("That'd be convenient.");
					return false;
				}
				if (player.getWildernessLevel() > 35) {
					if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR

							|| player.getRights() == PlayerRights.DEVELOPER) {
						player.getPacketSender()
								.sendMessage("@red@You've teleported out of deep Wilderness, logs have been written.");
						PlayerLogs.log(player.getUsername(), " teleported out of level " + player.getWildernessLevel()
								+ " wildy. Were in combat? " + player.getCombatBuilder().isBeingAttacked());
						return true;
					}
				//	player.getPacketSender().sendMessage("Teleport spells are blocked in this level of Wilderness.");
				//	player.getPacketSender()
				//			.sendMessage("You must be below level 20 of Wilderness to use teleportation spells.");
					return true;
				}*/
				return true;
			}

			@Override
			public void login(Player player) {
				//player.performGraphic(new Graphic(2000, 8));
			}

		/*	@Override
			public boolean canAttack(Player player, Player target) {
				int combatDifference = CombatFactory.combatLevelDifference(player.getSkillManager().getCombatLevel(),
						target.getSkillManager().getCombatLevel());
				if (combatDifference > player.getWildernessLevel() + 5
						|| combatDifference > target.getWildernessLevel() + 5) {
					player.getPacketSender()
							.sendMessage("Your combat level difference is too great to attack that player here.");
					player.getMovementQueue().reset();
					return false;
				}
				if (target.getLocation() != Location.WILDERNESS) {
					player.getPacketSender()
							.sendMessage("That player cannot be attacked, because they are not in the Wilderness.");
					player.getMovementQueue().reset();
					return false;
				}
				if (Jail.isJailed(player.getUsername())) {
					player.getPacketSender().sendMessage("You cannot do that right now.");
					return false;
				}
				if (Jail.isJailed(target.getUsername())) {
					player.getPacketSender().sendMessage("That player cannot be attacked right now.");
					return false;
				}
				return true;
			}*/
		},
		BARROWS(new int[] { 3520, 3598, 3543, 3584, 3543, 3560 }, new int[] { 9653, 9750, 3265, 3314, 9685, 9702 },
				false, true, true, true, true, true) {
			@Override
			public void process(Player player) {
				if (player.getWalkableInterfaceId() != 37200)
					player.getPacketSender().sendWalkableInterface(37200, true);


			}

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public void leave(Player player) {
				player.getPacketSender().sendWalkableInterface(37200, false);
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				Barrows.killBarrowsNpc(killer, npc, true);
				return true;
			}
		},
		THE_SIX(new int[] { 2376, 2395 }, new int[] { 4711, 4731 }, true, true, true, true, true, true) {
			@Override
			public void process(Player player) {
				if (player.getWalkableInterfaceId() != 37200)
					player.getPacketSender().sendWalkableInterface(37200, true);
			}

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public void leave(Player player) {
				if (!player.doingClanBarrows()) {
					if (player.getRegionInstance() != null) {
						player.getRegionInstance().destruct();
					}
					new TheSix(player).leave(false);
				}
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				boolean respawn = false;

				if (!killer.doingClanBarrows()) {
					Barrows.killBarrowsNpc(killer, npc, true);
					if (new TheSix(killer).allKilled()) {
						respawn = true;
					}
				}

				if (respawn) {
					new TheSix(killer).spawn(killer.doingClanBarrows());
				}

				return true;
			}
		},

		INFERNAL_DEMON(new int[]{2337, 2370}, new int[]{9880, 9920}, true, true, true, false, false, true) {
			@Override
			public void enter(Player player) {

				player.sendMessage("You have entered the Infernal demon lair, proceed at your own risk!");
			}

		},

		INVADING_GAME(new int[] { 2216, 2223 }, new int[] { 4936, 4943 }, true, true, true, false, true, true) {
			@Override
			public void process(Player player) {
				//if (player.getWalkableInterfaceId() != 21005)
				//	player.getPacketSender().sendWalkableInterface(21005, true);
			}
		},
		PEST_CONTROL_GAME(new int[] { 2624, 2690 }, new int[] { 2550, 2619 }, true, true, true, false, true, true) {
			@Override
			public void process(Player player) {
				if (player.getWalkableInterfaceId() != 21100)
					player.getPacketSender().sendWalkableInterface(21100, true);
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender()
						.sendMessage("Teleport spells are blocked on this island. Wait for the game to finish!");
				return false;
			}

			@Override
			public void leave(Player player) {
				PestControl.leave(player, true);
			}

			@Override
			public void logout(Player player) {
				PestControl.leave(player, true);
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				return true;
			}

			@Override
			public void onDeath(Player player) {
				player.moveTo(new Position(2657, 2612, 0));
			}
		},
		PEST_CONTROL_BOAT(new int[] { 2660, 2663 }, new int[] { 2638, 2643 }, false, false, false, false, false, true) {
			@Override
			public void process(Player player) {
				if (player.getWalkableInterfaceId() != 21005)
					player.getPacketSender().sendWalkableInterface(21005, true);
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("You must leave the boat before teleporting.");
				return false;
			}

			@Override
			public void leave(Player player) {
				if (player.getLocation() != PEST_CONTROL_GAME) {
					PestControl.leave(player, true);
				}
			}

			@Override
			public void logout(Player player) {
				PestControl.leave(player, true);
			}
		},
		SOULWARS(new int[] { -1, -1 }, new int[] { -1, -1 }, true, true, true, false, true, true) {
			@Override
			public void process(Player player) {

			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender()
						.sendMessage("If you wish to leave, you must use the portal in your team's lobby.");
				return false;
			}

			@Override
			public void logout(Player player) {

			}

			@Override
			public void onDeath(Player player) {

			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {

				return false;
			}

		},
		SOULWARS_WAIT(new int[] { -1, -1 }, new int[] { -1, -1 }, false, false, false, false, false, true) {
			@Override
			public void process(Player player) {
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("You must leave the waiting room before being able to teleport.");
				return false;
			}

			@Override
			public void logout(Player player) {
			}
		},

		FIGHT_PITS(new int[] { 2370, 2425 }, new int[] { 5133, 5167 }, true, true, true, false, false, true) {
			@Override
			public void process(Player player) {
				if (FightPit.inFightPits(player)) {
					FightPit.updateGame(player);
					if (player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK)
						player.getPacketSender().sendInteractionOption("Attack", 2, true);
				}
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage(
						"Teleport spells are blocked here. If you'd like to leave, use the northern exit.");
				return false;
			}

			@Override
			public void logout(Player player) {
				FightPit.removePlayer(player, "leave game");
			}

			@Override
			public void leave(Player player) {
				onDeath(player);
			}

			@Override
			public void onDeath(Player player) {
				if (FightPit.getState(player) != null) {
					FightPit.removePlayer(player, "death");
				}
			}

			@Override
			public boolean canAttack(Player player, Player target) {
				String state1 = FightPit.getState(player);
				String state2 = FightPit.getState(target);
				return state1 != null && state1.equals("PLAYING") && state2 != null && state2.equals("PLAYING");
			}
		},
		FIGHT_PITS_WAIT_ROOM(new int[] { 2393, 2404 }, new int[] { 5168, 5176 }, false, false, false, false, false,
				true) {
			@Override
			public void process(Player player) {
				FightPit.updateWaitingRoom(player);
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage(
						"Teleport spells are blocked here. If you'd like to leave, use the northern exit.");
				return false;
			}

			@Override
			public void logout(Player player) {
				FightPit.removePlayer(player, "leave room");
			}

			@Override
			public void leave(Player player) {
				if (player.getLocation() != FIGHT_PITS) {
					FightPit.removePlayer(player, "leave room");
				}
			}

		},
		GRAVEYARD(new int[] { 3485, 3517 }, new int[] { 3559, 3580 }, true, true, false, true, false, false) {
			@Override
			public boolean canTeleport(Player player) {
				if (player.getMinigameAttributes().getGraveyardAttributes().hasEntered()) {
					player.getPacketSender().sendInterfaceRemoval()
							.sendMessage("A spell teleports you out of the graveyard.");
					Graveyard.leave(player);
					return false;
				}
				return true;
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				return killer.getMinigameAttributes().getGraveyardAttributes().hasEntered()
						&& Graveyard.handleDeath(killer, npc);
			}

			@Override
			public void logout(Player player) {
				if (player.getMinigameAttributes().getGraveyardAttributes().hasEntered()) {
					Graveyard.leave(player);
				}
			}

			@Override
			public void onDeath(Player player) {
				Graveyard.leave(player);
			}

			@Override
			public void process(Player player) {
			}
		},
		DUEL_ARENA(new int[] { 3322, 3394, 3311, 3323, 3331, 3391 }, new int[] { 3195, 3291, 3223, 3248, 3242, 3260 },
				false, false, false, false, false, false) {
			@Override
			public void process(Player player) {
				if (player.getWalkableInterfaceId() != 201)
					player.getPacketSender().sendWalkableInterface(201, true);
				if (player.getDueling().duelingStatus == 0) {
					if (player.getPlayerInteractingOption() != PlayerInteractingOption.CHALLENGE)
						player.getPacketSender().sendInteractionOption("Challenge", 2, false);

				} else if (player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK)
					player.getPacketSender().sendInteractionOption("Attack", 2, true);
			}

			@Override
			public void enter(Player player) {
				PLAYERS_IN_DUEL_ARENA++;
				player.getPacketSender().sendMessage(
						"<img=5> <col=996633>Warning! Do not stake items which you are not willing to lose.");
			}

			@Override
			public boolean canTeleport(Player player) {
				if (player.getDueling().duelingStatus == 5) {
					player.getPacketSender().sendMessage("To forfiet a duel, run to the west and use the trapdoor.");
					return false;
				}
				return true;
			}

			@Override
			public void logout(Player player) {
				boolean dc = false;
				if (player.getDueling().inDuelScreen && player.getDueling().duelingStatus != 5) {
					player.getDueling().declineDuel(player.getDueling().duelingWith > 0 ? true : false);
				} else if (player.getDueling().duelingStatus == 5) {
					if (player.getDueling().duelingWith > -1) {
						Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
						if (duelEnemy != null) {
							duelEnemy.getDueling().duelVictory();
						} else {
							dc = true;
						}
					}
				}
				player.moveTo(new Position(3368, 3268));
				if (dc) {
					World.removePlayer(player);
				}
			}

			@Override
			public void leave(Player player) {
				if (player.getDueling().duelingStatus == 5) {
					onDeath(player);
				}
				PLAYERS_IN_DUEL_ARENA--;
			}

			@Override
			public void onDeath(Player player) {
				if (player.getDueling().duelingStatus == 5) {
					if (player.getDueling().duelingWith > -1) {
						Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
						if (duelEnemy != null) {
							duelEnemy.getDueling().duelVictory();
							duelEnemy.getPacketSender().sendMessage("You won the duel! Congratulations!");
						}
					}
					PlayerLogs.log(player.getUsername(), "Has lost their duel.");
					player.getPacketSender().sendMessage("You've lost the duel.");
					player.getDueling().arenaStats[1]++;
					player.getDueling().reset();
				}
				player.moveTo(new Position(3368 + Misc.getRandom(5), 3267 + Misc.getRandom(3)));
				player.getDueling().reset();
			}

			@Override
			public boolean canAttack(Player player, Player target) {
				if (target.getIndex() != player.getDueling().duelingWith) {
					player.getPacketSender().sendMessage("That player is not your opponent!");
					return false;
				}
				if (player.getDueling().timer != -1) {
					player.getPacketSender().sendMessage("You cannot attack yet!");
					return false;
				}
				return player.getDueling().duelingStatus == 5 && target.getDueling().duelingStatus == 5;
			}
		}, // 3340 3351
		// 3364 3333//ok continue sorry. uay
		// GALVEKMULTI(new int[]{3340, 3364
		GODWARS_DUNGEON(new int[] { 2800, 2950, 2858, 2943 }, new int[] { 5200, 5400, 5180, 5230 }, true, true, true,
				false, true, true) {
			@Override
			public void process(Player player) {

				if ((player.getPosition().getX() == 2842 && player.getPosition().getY() == 5308) // ARMADYL
						|| (player.getPosition().getX() == 2876 && player.getPosition().getY() == 5369) // BANDOS
						|| (player.getPosition().getX() == 2936 && player.getPosition().getY() == 5331) // ZAMMY
						|| (player.getPosition().getX() == 2907 && player.getPosition().getY() == 5272)) { // NORTH
					// EAST,
					// saradomin
					player.moveTo(new Position(player.getPosition().getX() - 1, player.getPosition().getY() - 1,
							player.getPosition().getZ()));
					player.getMovementQueue().reset();
				}
				if ((player.getPosition().getX() == 2842 && player.getPosition().getY() == 5296) // ARMADYL
						|| (player.getPosition().getX() == 2876 && player.getPosition().getY() == 5351) // BANDOS
						|| (player.getPosition().getX() == 2936 && player.getPosition().getY() == 5318) // ZAMMY
						|| (player.getPosition().getX() == 2907 && player.getPosition().getY() == 5258)) { // saradomin,
					// SOUTH
					// EAST
					player.moveTo(new Position(player.getPosition().getX() - 1, player.getPosition().getY() + 1,
							player.getPosition().getZ()));
					player.getMovementQueue().reset();
				}
				if ((player.getPosition().getX() == 2824 && player.getPosition().getY() == 5296) // ARMADYL
						|| (player.getPosition().getX() == 2864 && player.getPosition().getY() == 5351) // BANDOS
						|| (player.getPosition().getX() == 2918 && player.getPosition().getY() == 5318) // ZAMMY
						|| (player.getPosition().getX() == 2895 && player.getPosition().getY() == 5258)) { // saradomin,
					// SOUTH
					// WEST
					player.moveTo(new Position(player.getPosition().getX() + 1, player.getPosition().getY() + 1,
							player.getPosition().getZ()));
					player.getMovementQueue().reset();
				}
				if ((player.getPosition().getX() == 2824 && player.getPosition().getY() == 5308) // ARMADYL
						|| (player.getPosition().getX() == 2864 && player.getPosition().getY() == 5369) // BANDOS
						|| (player.getPosition().getX() == 2918 && player.getPosition().getY() == 5331) // ZAMMY
						|| (player.getPosition().getX() == 2895 && player.getPosition().getY() == 5272)) { // saradomin,
					// NORTH
					// WEST
					player.moveTo(new Position(player.getPosition().getX() + 1, player.getPosition().getY() - 1,
							player.getPosition().getZ()));
					player.getMovementQueue().reset();
				}

				// if(player.getWalkableInterfaceId() != 16210)
				// player.getPacketSender().sendWalkableInterface(16210);
			}

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public void onDeath(Player player) {
				leave(player);
			}

			@Override
			public void leave(Player p) {
				for (int i = 0; i < p.getMinigameAttributes().getGodwarsDungeonAttributes()
						.getKillcount().length; i++) {
					// p.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[i] =
					// 0;
					// p.getPacketSender().sendString((16216+i), "0");
				}
				// p.getMinigameAttributes().getGodwarsDungeonAttributes().setAltarDelay(0).setHasEnteredRoom(false);
				// p.getPacketSender().sendMessage("Your Godwars dungeon progress has been
				// reset.");
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				int index = -1;
				int npc = n.getId();
				if (npc == 6246 || npc == 6229 || npc == 6230 || npc == 6231) // Armadyl
					index = 0;
				else if (npc == 102 || npc == 3583 || npc == 115 || npc == 113 || npc == 6273 || npc == 6276
						|| npc == 6277 || npc == 6288) // Bandos
					index = 1;
				else if (npc == 6258 || npc == 6259 || npc == 6254 || npc == 6255 || npc == 6257 || npc == 6256) // Saradomin
					index = 2;
				else if (npc == 10216 || npc == 6216 || npc == 1220 || npc == 6007 || npc == 6219 || npc == 6220
						|| npc == 6221 || npc == 49 || npc == 4418) // Zamorak
					index = 3;
				if (index != -1) {
					// killer.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index]++;
					// killer.getPacketSender().sendString((16216+index),
					// ""+killer.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index]);
				}
				return false;
			}
		},
		NOMAD(new int[] { 3342, 3377 }, new int[] { 5839, 5877 }, true, true, false, true, false, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage(
						"Teleport spells are blocked here. If you'd like to leave, use the southern exit.");
				return false;
			}

			@Override
			public void leave(Player player) {
				if (player.getRegionInstance() != null)
					player.getRegionInstance().destruct();
				player.moveTo(new Position(1890, 3177));
				player.restart();
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				if (npc.getId() == 8528) {
					Nomad.endFight(killer, true);
					return true;
				}
				return false;
			}
		},
		RECIPE_FOR_DISASTER(new int[] { 1885, 1913 }, new int[] { 5340, 5369 }, true, true, false, false, false,
				false) {

			/*
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender()
						.sendMessage("Teleport spells are blocked here. If you'd like to leave, use a portal.");
				return false;
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				RecipeForDisaster.handleNPCDeath(killer, npc);
				return true;
			}

			@Override
			public void leave(Player player) {
				if (player.getRegionInstance() != null)
					player.getRegionInstance().destruct();
				player.moveTo(new Position(3081, 3500));
			}

			@Override
			public void onDeath(Player player) {
				leave(player);
			}*/
		},
		FREE_FOR_ALL_ARENA(new int[] { 2755, 2876 }, new int[] { 5512, 5627 }, true, true, true, false, false, true) {
			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public void onDeath(Player player) {
				player.moveTo(new Position(2375, 4021));
			}

			@Override
			public boolean canAttack(Player player, Player target) {
				if (target.getLocation() != FREE_FOR_ALL_ARENA) {
					player.getPacketSender().sendMessage("That player has not entered the dangerous zone yet.");
					player.getMovementQueue().reset();
					return false;
				}
				return true;
			}

			@Override
			public void enter(Player player) {
				if (player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK) {
					player.getPacketSender().sendInteractionOption("Attack", 2, true);
				}
			}

		},
		FREE_FOR_ALL_WAIT(new int[] { 2755, 2876 }, new int[] { 5507, 5627 }, false, false, true, false, false, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender()
						.sendMessage("Teleport spells are blocked here, if you wish to teleport, use the portal.");
				return false;
			}

			@Override
			public void onDeath(Player player) {
				player.moveTo(new Position(2815, 5511));
			}
		},
		WARRIORS_GUILD(new int[] { 2833, 2879 }, new int[] { 3531, 3559 }, false, true, true, false, false, true) {
			@Override
			public void logout(Player player) {
			}

			@Override
			public void leave(Player player) {
			}
		},
		PURO_PURO(new int[] { 2556, 2630 }, new int[] { 4281, 4354 }, false, true, true, false, false, true) {
		},
		FLESH_CRAWLERS(new int[] { 2033, 2049 }, new int[] { 5178, 5197 }, false, true, true, false, true, true) {
		},
		RUNESPAN(new int[] { 2122, 2159 }, new int[] { 5517, 5556 }, false, false, true, true, true, false) {
		},
		//// xyyx
		EARTHQUAKE(new int[] { 2883, 2942 }, new int[] { 5441, 5498 }, true, true, true, false, false, false) {
		},
		EXODENLOC(new int[] { 2816, 2879 }, new int[] { 2816, 2879 }, true, true, true, false, false, false) {
		},


		DEFAULT(null, null, false, true, true, true, true, true) {
			@Override
			public void enter(Player player) {
				player.getPacketSender().sendWalkableInterface(60_000, false);
			}
		},

		ASTA_LOBBY(new int[] { 3059, 3071 }, new int[] { 2752, 2764 }, true, true, true, false, false, false) {
		},

		YOUTUBE(new int[] { 2844, 2867 }, new int[] { 2696, 2720 }, true, true, true, false, false, false) {
		},
		HOME_AREA(new int[] { 2175, 2237 }, new int[] { 3719, 3777 }, false, true, true, false, false, true) {
			@Override
			public void enter(Player player) {
				if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) < player.getSkillManager()
						.getMaxLevel(Skill.CONSTITUTION)) {
					player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
							player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
					player.getPacketSender()
							.sendMessage("As you enter home, your health regenerates to full.");
				}
				if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
						.getMaxLevel(Skill.PRAYER)) {
					player.getSkillManager().setCurrentLevel(Skill.PRAYER,
							player.getSkillManager().getMaxLevel(Skill.PRAYER));
					player.getPacketSender().sendMessage("As you enter home, the gods restore your prayer.");
				}

				player.getPacketSender().sendWalkableInterface(63000, false);
				player.getPacketSender().sendWalkableInterface(60000, false);
			}
		},

		//Monster Locations
		STARTER(new int[] { 3022, 3052 }, new int[] { 10255, 10286 }, false, true, true, false, false, true) {
			@Override
			public void leave(Player player) {
				player.getPacketSender().sendWalkableInterface(60_000, false);
			}
		},

		//BOSS LOCATIONS
		INSTANCE_LOBBY(new int[]{2642, 2668}, new int[]{2778, 2804}, false, false, true, false, true, true) {
		},
		NORMAL_INSTANCE(new int[] {3009, 3029 }, new int[] { 2752, 2772 }, true, true, true, false, false, false) {
			@Override
			public void leave(Player player) {
				player.getPacketSender().sendWalkableInterface(60_000, false);
			}
		},

		SINGLE_INSTANCE(new int[] { 1999, 2028}, new int[] { 4495, 4530},
				true, true, true, false, false, true) {
			@Override
			public void leave(Player player) {
				player.getPacketSender().sendWalkableInterface(60_000, false);
			}
		},

//		HULK(new int[] { 3472, 3503}, new int[] { 3601, 3632},
//				true, true, true, false, false, true) {},

		//Globals
		NORMAL_GLOBAL(new int[] { 2130, 2156 }, new int[] { 5001, 5028 }, true, false, true, false, false, false) {
			@Override
			public void enter(Player player) {
				if(!WorldIPChecker.getInstance().addToContent(player, "global")){
					player.moveTo(GameSettings.DEFAULT_POSITION);
					player.sendMessage("You are only allowed one account in globals");
				}
			}

			@Override
			public void logout(Player player) {
				WorldIPChecker.getInstance().leaveContent(player);
			}

			@Override
			public void onDeath(Player player) {
				WorldIPChecker.getInstance().leaveContent(player);
			}

			@Override
			public void leave(Player player) {
				WorldIPChecker.getInstance().leaveContent(player);
			}
		},
		//TOWER
		TOWER_LOBBY(new int[] {2843, 2874 }, new int[] { 2734, 2751 }, false, false, false, false, false, false) {
		},
		TOWER_1(new int[] {3011, 3071 }, new int[] { 2813, 2879 }, true, true, true, false, false, false) {
		},

		SLAYER_ZONES(new int[] {2623, 2687 }, new int[] { 3776, 3841 }, true, false, true, false, false, false) {
			@Override
			public void leave(Player player) {
				player.getPacketSender().sendWalkableInterface(60_000, false);
			}
		},

		RAID_LOBBY(new int[] { 1670, 1721 }, new int[] { 5591, 5605 }, false, false, true, false, false, false) {
			@Override
			public void enter(Player player) {
				player.getPacketSender().sendDungeoneeringTabIcon(true).sendTabInterface(GameSettings.STAFF_TAB, 27224).sendTab(GameSettings.STAFF_TAB);

				if (player.getPlayerInteractingOption() != PlayerInteractingOption.INVITE) {
					player.getPacketSender().sendInteractionOption("Invite", 2, true);
				}
			}

			@Override
			public void process(Player player) {
				//player.getPacketSender().sendDungeoneeringTabIcon(true).sendTabInterface(GameSettings.STAFF_TAB, 27224).sendTab(GameSettings.STAFF_TAB);

				if(player.getRaidParty() != null){
					player.getRaidParty().refreshInterface();
				}
			}

			@Override
			public void onDeath(Player player) {
			}

			@Override
			public void leave(Player player) {
			}
		},

		LORDS(new int[] { 3342, 3388 }, new int[] { 9615, 9663 }, false, false, false, false, false, false) {

			@Override
			public void enter(Player player) {
				player.getPacketSender().sendWalkableInterface(63_000, true);
			}

		}, // donator material daily
		TREASURE_HUNTER(new int[] { 1986, 2045 }, new int[] { 4994, 5052 }, true, true, true, false, false, false) {
		}, // donator//minigame???
		AFK(new int[] { 3024, 3056 }, new int[] { 4050, 4082 }, false, true, true, false, false, true) {

			@Override
			public void enter(Player player) {
				if(!WorldIPChecker.getInstance().addToContent(player, "afk")){
					player.moveTo(GameSettings.DEFAULT_POSITION);
					player.sendMessage("You are only allowed one account in AFK zone");
				}
			}

			@Override
			public void leave(Player player) {
				WorldIPChecker.getInstance().leaveContent(player);
			}

			@Override
			public void login(Player player) {
				player.getPacketSender().sendCameraNeutrality();
				if (player.getLocation() == AFK) {
					player.moveTo(GameSettings.HOME_CORDS);
				}
				WorldIPChecker.getInstance().leaveContent(player);
			}
			@Override
			public void logout(Player player) {
				WorldIPChecker.getInstance().leaveContent(player);
				player.moveTo(GameSettings.HOME_CORDS);
			}
		},
		;

		Location(int[] x, int[] y, boolean multi, boolean summonAllowed, boolean followingAllowed,
				 boolean cannonAllowed, boolean firemakingAllowed, boolean aidingAllowed) {
			this.x = x;
			this.y = y;
			this.multi = multi;
			this.summonAllowed = summonAllowed;
			this.followingAllowed = followingAllowed;
			this.cannonAllowed = cannonAllowed;
			this.firemakingAllowed = firemakingAllowed;
			this.aidingAllowed = aidingAllowed;
		}

		private int[] x, y;
		private boolean multi;
		private boolean summonAllowed;
		private boolean followingAllowed;
		private boolean cannonAllowed;
		private boolean firemakingAllowed;
		private boolean aidingAllowed;

		public int[] getX() {
			return x;
		}

		public int[] getY() {
			return y;
		}
		public static boolean inMulti(Character gc) {
			if (gc.isForceMultiArea()) {
				return true;
			}
			gc.setLocation(Locations.Location.getLocation(gc));
			return gc.getLocation().multi;
		}

		public boolean isSummoningAllowed() {
			return summonAllowed;
		}

		public boolean isFollowingAllowed() {
			return followingAllowed;
		}

		public boolean isCannonAllowed() {
			return cannonAllowed;
		}

		public boolean isFiremakingAllowed() {
			return firemakingAllowed;
		}

		public boolean isAidingAllowed() {
			return aidingAllowed;
		}

		public static Location getLocation(Entity gc) {
			for (Location location : Location.values()) {
				if (location != Location.DEFAULT)
					if (inLocation(gc, location))
						return location;
			}
			return Location.DEFAULT;
		}

		public static boolean inLocation(Entity gc, Location location) {
			if (location == Location.DEFAULT) {
				return getLocation(gc) == Location.DEFAULT;
			}
			return inLocation(gc.getPosition().getX(), gc.getPosition().getY(), location);
		}

		public static boolean inLocation(int absX, int absY, Location location) {
			int checks = location.getX().length - 1;
			for (int i = 0; i <= checks; i += 2) {
				if (absX >= location.getX()[i] && absX <= location.getX()[i + 1]) {
					if (absY >= location.getY()[i] && absY <= location.getY()[i + 1]) {
						return true;
					}
				}
			}
			return false;
		}

		public void process(Player player) {

		}

		public boolean canTeleport(Player player) {
			return true;
		}

		public void login(Player player) {

		}

		public void enter(Player player) {

		}

		public void leave(Player player) {

		}

		public void logout(Player player) {

		}

		public void onDeath(Player player) {

		}

		public boolean handleKilledNPC(Player killer, NPC npc) {
			return false;
		}

		public boolean canAttack(Player player, Player target) {
			return false;
		}

		/**
		 * SHOULD AN ENTITY FOLLOW ANOTHER ENTITY NO MATTER THE DISTANCE BETWEEN THEM?
		 **/
		public static boolean ignoreFollowDistance(Character character) {
			return false;
		}
	}

	public static void process(Character gc) {
		Location newLocation = Location.getLocation(gc);
		if (gc.getLocation() == newLocation) {
			if (gc.isPlayer()) {
				Player player = (Player) gc;
				gc.getLocation().process(player);
				if (Location.inMulti(player)) {
					if (player.getMultiIcon() != 1)
						player.getPacketSender().sendMultiIcon(1);
				} else if (player.getMultiIcon() == 1)
					player.getPacketSender().sendMultiIcon(0);
			}
		} else {
			Location prev = gc.getLocation();
			if (gc.isPlayer()) {
				Player player = (Player) gc;
				if (player.getMultiIcon() > 0)
					player.getPacketSender().sendMultiIcon(0);
				if (player.getWalkableInterfaceId() > 0 && player.getWalkableInterfaceId() != 37400
						&& player.getWalkableInterfaceId() != 50000)
					player.getPacketSender().sendWalkableInterface(50000, false);
				if (player.getPlayerInteractingOption() != PlayerInteractingOption.NONE)
					player.getPacketSender().sendInteractionOption("null", 2, true);
			}
			gc.setLocation(newLocation);
			if (gc.isPlayer()) {
				if (
						(prev == Location.ZOMBIE_LOBBY && newLocation == Location.ZOMBIE)
						|| (prev == Location.ZOMBIE && newLocation == Location.ZOMBIE_LOBBY)
						|| (prev == Location.AURA_LOBBY && newLocation == Location.AURA)
						|| (prev == Location.AURA && newLocation == Location.AURA_LOBBY)
						|| prev == Location.NORMAL_INSTANCE || prev == Location.SINGLE_INSTANCE
						|| prev == Location.TOWER_1
				) {
				} else {
					prev.leave(((Player) gc));
				}
				gc.getLocation().enter(((Player) gc));
			}
		}
	}

	public static boolean goodDistance(Position start, int startSize, Position target, int targetSize, int distance) {
		if (target == null) {
			return false;
		}
		if (start.getZ() != target.getZ()) {
			return false;
		}
		final int deltaX1 = start.getX() - (target.getX() + targetSize - 1) - distance;
		final int deltaX2 = start.getX() + startSize - 1 - target.getX() + distance;
		final int deltaY1 = start.getY() + startSize - 1 - target.getY() + distance;
		final int deltaY2 = start.getY() - (target.getY() + targetSize - 1) - distance;

		boolean correctX = !(deltaX1 > 0) && !(deltaX2 < 0);
		boolean correctY = !(deltaY1 < 0) && !(deltaY2 > 0);
		return correctX && correctY;
	}
	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		if (playerX == objectX && playerY == objectY)
			return true;
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean goodDistance(Position pos1, Position pos2, int distanceReq) {
		if (pos1.getZ() != pos2.getZ())
			return false;
		return goodDistance(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(), distanceReq);
	}

	public static boolean goodDistance(Character entity, Character entity2, int distance) {
		return goodDistance(entity.getPosition(), entity.getSize(), entity2.getPosition(), entity2.getSize(), distance);
	}


	public static int distanceTo(Position position, Position destination, int size) {
		final int x = position.getX();
		final int y = position.getY();
		final int otherX = destination.getX();
		final int otherY = destination.getY();
		int distX, distY;
		if (x < otherX)
			distX = otherX - x;
		else if (x > otherX + size)
			distX = x - (otherX + size);
		else
			distX = 0;
		if (y < otherY)
			distY = otherY - y;
		else if (y > otherY + size)
			distY = y - (otherY + size);
		else
			distY = 0;
		if (distX == distY)
			return distX + 1;
		return Math.max(distX, distY);
	}

	public static List<Location> bossLocations = Collections.synchronizedList(Arrays.asList(
			Location.NORMAL_INSTANCE, Location.SINGLE_INSTANCE, Location.TOWER_1, Location.LORDS,
			Location.TREASURE_HUNTER
	));
}
