package com.ruse.world.content.skill.impl.slayer;

import com.ruse.model.Position;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.util.Misc;

import java.util.*;

/**
 * @author Gabriel Hannason
 */

public enum SlayerTasks {

	NO_TASK(null, -1, null, -1, null),


	//boss slayer tasks ///////////
	// Old bosses
	/*FRIEZA(SlayerMaster.BOSS_SLAYER, 252, "Frieza can be found by using the Teleport interface", 500,
			new Position(2516, 3042, 0)),
	PERFECT_CELL(SlayerMaster.BOSS_SLAYER, 449, "Perfect cell can be found by using the Teleport interface.", 500,
			new Position(3000, 2511, 0)),
	SUPER_BUU(SlayerMaster.BOSS_SLAYER, 452, "Super Buu can be found by using the Teleport interface.", 500,
			new Position(3342, 3022, 0)),
	HERBAL_ROGUE(SlayerMaster.BOSS_SLAYER, 2342, "Herbal Rogue can be found by using the Teleport interface.", 500,
			new Position(3044, 2969, 0)),
	AZURE_BEAST(SlayerMaster.BOSS_SLAYER, 3831, "Azure Beast can be found by using the Teleport interface.", 500,
			new Position(2924, 2842, 0)),
	JOKER(SlayerMaster.BOSS_SLAYER, 185, "Joker can be found by using the Teleport interface.", 500,
			new Position(1807, 3211, 0)),

	SUPREME_BOSS(SlayerMaster.BOSS_SLAYER, 440, "Supreme Boss can be found by using the Teleport interface.", 600,
			new Position(2403, 5082, 1000)),
	VASA_NISTIRIO(SlayerMaster.BOSS_SLAYER, 1120, "Vasa Nistirio can be found by using the Teleport interface.", 600,
			new Position(2910, 2593, 0)),*/

	// Boss slayer tasks 375-750k * 2 = 750-1500k / task
	// Easy bosses
	MALVEK(SlayerMaster.HARD_SLAYER, 8002, "Malvek can be found by using the Teleport interface.", 905,
			new Position(2911, 3991, 0)),
	ASMODEUS(SlayerMaster.BOSS_SLAYER, 9903, "Asmodeus can be found by using the Teleport interface.", 900,
			new Position(2975, 4000, 0)),
	DRAGONITE(SlayerMaster.BOSS_SLAYER, 9816, "Dragonite can be found by using the Teleport interface.", 805,
			new Position(3036, 4003, 0)),
	//Med bosses
	GORVEK(SlayerMaster.BOSS_SLAYER, 9806, "Gorvek can be found by using the Teleport interface.", 800,
			new Position(1887, 5468, 0)),
	MUTANT_HYDRA(SlayerMaster.BOSS_SLAYER, 9839, "Mutant Hydra can be found by using the Teleport interface.", 705,
			new Position(2335, 3998, 0)),
	SANCTUM_GOLEM(SlayerMaster.BOSS_SLAYER, 9017, "Sanctum Golem can be found by using the Teleport interface.", 700,
			new Position(1884, 5334, 0)),

	// Hard bosses
	GROUDON(SlayerMaster.BOSS_SLAYER, 8010, "Groudon can be found by using the Teleport interface.", 2000,
			new Position(2784, 4445, 0)),
	LILINRYSS(SlayerMaster.BOSS_SLAYER, 3014, "Lilinryss can be found by using the Teleport interface.", 1500,
			new Position(2721, 4450, 0)),
	AG_THOMOTH(SlayerMaster.BOSS_SLAYER, 3013, "Ag'thomoth can be found by using the Teleport interface.", 1300,
			new Position(2761, 4575, 0)),
	ZEIDAN_GRIMM(SlayerMaster.BOSS_SLAYER, 3010, "Zeidan Grimm can be found by using the Teleport interface.", 1105,
			new Position(2785, 4525, 0)),
	ONYX_GRIFFIN(SlayerMaster.BOSS_SLAYER, 1746, "Onyx Griffin can be found by using the Teleport interface.", 1000,
			new Position(2712, 4508, 0)),


	// Easy slayer tasks 12.5k-50k * 2 = 25-100k / task

	IMPERIAL_HOUND(SlayerMaster.EASY_SLAYER, 9838, "Imperial Hound can be found by using the Teleport interface.", 74, new Position(3487, 3615, 16)),
	IMPERIAL_SCORPION(SlayerMaster.EASY_SLAYER, 9845, "Imperial Scorpion can be found by using the Teleport interface.", 80, new Position(3487, 3615, 20)),
	IMPERIAL_RANGER(SlayerMaster.EASY_SLAYER, 9910, "Imperial Ranger can be found by using the Teleport interface.", 85, new Position(3486, 3617, 24)),
	IMPERIAL_PALADIN(SlayerMaster.EASY_SLAYER, 9807, "Imperial Paladin can be found by using the Teleport interface.", 90, new Position(3486, 3617, 28)),

	NAGENDRA(SlayerMaster.MEDIUM_SLAYER, 811, "Nagendra can be found by using the Teleport interface.", 105, new Position(1903, 4850)),
	INFERNO(SlayerMaster.MEDIUM_SLAYER, 202, "Inferno can be found by using the Teleport interface.", 104, new Position(1901, 4878)),
	DR_ABERRANT(SlayerMaster.MEDIUM_SLAYER, 8003, "Dr. Aberrant can be found by using the Teleport interface.", 103, new Position(1878, 4912)),

	ZINQRUX(SlayerMaster.EASY_SLAYER, 8014, "Zinqrux can be found by using the Teleport interface.", 102, new Position(1847, 4899)),
	REVENANT_WYVERN(SlayerMaster.EASY_SLAYER, 6692, "Revenant Wyvern can be found by using the Teleport interface.", 101, new Position(1813, 4908)),
	// Med slayer tasks 50k-125k * 2 = 100-250k / task

//new
	MISCREATION(SlayerMaster.MEDIUM_SLAYER, 3313, "Miscreation can be found by using the Teleport interface.", 305, new Position(2711, 4752, 44)),
	MAZE_GUARDIAN(SlayerMaster.MEDIUM_SLAYER, 92, "Maze Guardian can be found by using the Teleport interface.", 302, new Position(1652, 5600, 32)),
	DOOMWATCHER(SlayerMaster.MEDIUM_SLAYER, 9836, "Doomwatcher can be found by using the Teleport interface.", 301, new Position(1652, 5600, 28)),
	ERAGON(SlayerMaster.MEDIUM_SLAYER, 9026, "Eragon can be found by using the Teleport interface.", 300, new Position(1652, 5600, 24)),
	AVALON(SlayerMaster.MEDIUM_SLAYER, 9025, "Avalon can be found by using the Teleport interface.", 207, new Position(1652, 5600, 20)),
	ZERNATH(SlayerMaster.MEDIUM_SLAYER, 3831, "Zernath can be found by using the Teleport interface.", 205, new Position(2964, 9478, 20)),
	IG_THAUR(SlayerMaster.MEDIUM_SLAYER, 9920, "Ig'thaur can be found by using the Teleport interface.", 204, new Position(2964, 9478, 16)),
	YISDAR(SlayerMaster.MEDIUM_SLAYER, 9817, "Yisdar can be found by using the Teleport interface.", 202, new Position(2964, 9478, 12)),
	KOL_GAL(SlayerMaster.MEDIUM_SLAYER, 9815, "Kol'gal can be found by using the Teleport interface.", 201, new Position(2964, 9478, 8)),

	//old




	// Hard slayer tasks 125-250k * 2 = 250-500k / task
	SASUKE(SlayerMaster.HARD_SLAYER, 9914, "Sasuke can be found by using the Teleport interface.", 607, new Position(2964, 9478, 72)),
	//old
	BOWSER(SlayerMaster.HARD_SLAYER, 9914, "Bowser can be found by using the Teleport interface.", 605, new Position(2964, 9478, 68)),
	BROLY(SlayerMaster.HARD_SLAYER, 9918, "Broly can be found by using the Teleport interface.", 602, new Position(2964, 9478, 64)),
	LUFFY(SlayerMaster.HARD_SLAYER, 9916, "Luffy can be found by using the Teleport interface.", 600, new Position(2527, 2527, 64)),
	GOLDEN_GOLEM(SlayerMaster.HARD_SLAYER, 9024, "Golden Golem can be found by using the Teleport interface.", 507, new Position(2527, 2527, 60)),
	EMERALD_SLAYER(SlayerMaster.HARD_SLAYER, 2342, "Emerald Slayer can be found by using the Teleport interface.", 505, new Position(2527, 2527, 56)),
	DEATH_GOD(SlayerMaster.HARD_SLAYER, 9915, "Death God can be found by using the Teleport interface.", 502, new Position(2711, 4752, 56)),
	ZORBAK(SlayerMaster.HARD_SLAYER, 1906, "Zorbak can be found by using the Teleport interface.", 500, new Position(2711, 4752, 52)),
	AVATAR_TITAN(SlayerMaster.HARD_SLAYER, 8008, "Avatar Titan can be found by using the Teleport interface.", 407, new Position(2711, 4752, 48)),

	//ELITE SLAYER
	ELITE_SCORPION(SlayerMaster.ELITE_SLAYER, 1717, "Elite Scorpion can be found by using the Teleport interface.", 350_000, new Position(2651, 3810)),
	ELITE_BLOB(SlayerMaster.ELITE_SLAYER, 1718, "Elite Blob can be found by using the Teleport interface.", 370_000, new Position(2651, 3810)),
	ELITE_TURNIP(SlayerMaster.ELITE_SLAYER, 1726, "Elite Turnip can be found by using the Teleport interface.", 390_000, new Position(2651, 3810)),
	ELITE_TOAD(SlayerMaster.ELITE_SLAYER, 1738, "Elite Toad can be found by using the Teleport interface.", 400_000, new Position(2651, 3810)),
	ELITE_SOLDIER(SlayerMaster.ELITE_SLAYER, 9807, "Elite Soldier can be found by using the Teleport interface.", 420_000, new Position(2651, 3810)),
	ONYX_GRIFFIN2(SlayerMaster.ELITE_SLAYER, 1746, "Onyx Griffin can be found by using the Teleport interface.", 450_000, new Position(2651, 3810)),
	ARMOURED_BUNNY(SlayerMaster.ELITE_SLAYER, 9020, "Armoured Bunny can be found by using the Teleport interface.", 480_000, new Position(2651, 3810)),
	AVARYSS(SlayerMaster.ELITE_SLAYER, 9800, "Avaryss can be found by using the Teleport interface.", 500_000, new Position(2651, 3810)),

	;


	/*
	 * @param taskMaster
	 * 
	 * @param npcId
	 * 
	 * @param npcLocation
	 * 
	 * @param XP
	 * 
	 * @param taskPosition
	 */

	// 1 last thing left the dILY REWARD huh
	SlayerTasks(SlayerMaster taskMaster, int npcId, String npcLocation, int XP, Position taskPosition) {
		this.taskMaster = taskMaster;
		this.npcId = npcId;
		this.npcLocation = npcLocation;
		this.XP = XP;
		this.taskPosition = taskPosition;
	}

	public static Map<SlayerMaster, ArrayList<SlayerTasks>> tasks = new HashMap<>();
	static {


		for (SlayerMaster master : SlayerMaster.values()) {
			ArrayList<SlayerTasks> slayerMasterSet = new ArrayList<>();
			for(SlayerTasks t : SlayerTasks.values()) {
				if(t.taskMaster == master) {
					slayerMasterSet.add(t);
				}
			}
			tasks.put(master, slayerMasterSet);
		}
	}
	private SlayerMaster taskMaster;
	private int npcId;
	private String npcLocation;
	private int XP;
	private Position taskPosition;

	public SlayerMaster getTaskMaster() {
		return this.taskMaster;
	}

	public int getNpcId() {
		return this.npcId;
	}

	public String getNpcLocation() {
		return this.npcLocation;
	}

	public int getXP() {
		return this.XP;
	}

	public Position getTaskPosition() {
		return this.taskPosition;
	}


	public static SlayerTaskData getNewTaskData(SlayerMaster master) {
		int slayerTaskAmount = 20;
		ArrayList<SlayerTasks> possibleTasks = tasks.get(master);
		SlayerTasks task = possibleTasks.get(Misc.getRandom(possibleTasks.size() - 1));

		/*
		 * Getting a task
		 */
		if (master == SlayerMaster.BOSS_SLAYER) {
			slayerTaskAmount = 75 + Misc.getRandom(25);
		} else if (master == SlayerMaster.EASY_SLAYER) {
			slayerTaskAmount = 25 + Misc.random(25);
		} else if (master == SlayerMaster.MEDIUM_SLAYER) {
			slayerTaskAmount = 50 + Misc.random(25);
		} else if (master == SlayerMaster.HARD_SLAYER) {
			slayerTaskAmount = 60 + Misc.random(40);
		} else if (master == SlayerMaster.ELITE_SLAYER) {
			slayerTaskAmount = 60 + Misc.random(100);
		}
		return new SlayerTaskData(task, slayerTaskAmount);
	}//

	public String getName() {
		NpcDefinition def = NpcDefinition.forId(npcId);
		return def == null ? Misc.formatText(this.toString().toLowerCase().replaceAll("_", " ")) : def.getName();
	}
}
