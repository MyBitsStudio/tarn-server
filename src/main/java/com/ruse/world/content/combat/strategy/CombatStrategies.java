package com.ruse.world.content.combat.strategy;

import com.ruse.world.content.combat.strategy.impl.*;
import com.ruse.world.content.minigames.impl.dungeoneering.DungeoneeringBossNpc;
import com.ruse.world.packages.combat.scripts.npc.DefaultMonsterScript;
import com.ruse.world.packages.combat.scripts.npc.master.ZeidanCombat;
import com.zaros.world.entity.actor.combat.strategy.impl.dung.DungeoneeringBossCombat;

import java.util.HashMap;
import java.util.Map;


public class CombatStrategies {

	private static final DefaultMeleeCombatStrategy defaultMeleeCombatStrategy = new DefaultMeleeCombatStrategy();
	private static final DefaultMagicCombatStrategy defaultMagicCombatStrategy = new DefaultMagicCombatStrategy();
	private static final DefaultRangedCombatStrategy defaultRangedCombatStrategy = new DefaultRangedCombatStrategy();
	private static final Map<Integer, CombatStrategy> STRATEGIES = new HashMap<>();
	private static final EmptyCombatStrategy emptyCombatStrategy = new EmptyCombatStrategy();

	public static void init() {
		DefaultMonsterScript script = new DefaultMonsterScript();

		/*
		Starter Monsters
		 */
		STRATEGIES.put(9837, script);
		STRATEGIES.put(9027, script);
		STRATEGIES.put(9835, script);
		STRATEGIES.put(9911, script);
		STRATEGIES.put(9922, script);
		STRATEGIES.put(8014, script);
		STRATEGIES.put(8003, script);
		STRATEGIES.put(811, script);
		STRATEGIES.put(9817, script);
		STRATEGIES.put(9836, script);
		STRATEGIES.put(92, script);
		STRATEGIES.put(3313, script);
		STRATEGIES.put(1906, script);
		STRATEGIES.put(1742, script);
		STRATEGIES.put(1743, script);
		STRATEGIES.put(1744, script);
		STRATEGIES.put(1745, script);
		STRATEGIES.put(1738, script);
		STRATEGIES.put(1739, script);
		STRATEGIES.put(1740, script);
		STRATEGIES.put(1741, script);

		/**
		 * Beggining Bosses
		 */
		STRATEGIES.put(9025, script);
		STRATEGIES.put(9026, script);
		STRATEGIES.put(8088, script);
		STRATEGIES.put(2342, script);
		STRATEGIES.put(9839, script);
		STRATEGIES.put(9806, script);

		/**
		 * Instances
		 */

		STRATEGIES.put(9915, script);
		STRATEGIES.put(9024, script);
		STRATEGIES.put(8002, script);
		STRATEGIES.put(8000, script);
		STRATEGIES.put(3020, script);
		STRATEGIES.put(9919, script);
		STRATEGIES.put(9916, script);
		STRATEGIES.put(9913, script);
		STRATEGIES.put(1313, script);
		STRATEGIES.put(1311, script);
		STRATEGIES.put(9914, script);
		STRATEGIES.put(185, script);
		STRATEGIES.put(188, script);
		STRATEGIES.put(3117, script);
		STRATEGIES.put(9800, script);
		STRATEGIES.put(9802, script);
		STRATEGIES.put(5861, script);
		STRATEGIES.put(667, script);
		STRATEGIES.put(12843, script);
		STRATEGIES.put(440, script);
		STRATEGIES.put(438, script);
		STRATEGIES.put(3019, script);


		// special
		STRATEGIES.put(9818, script);
		STRATEGIES.put(591, script);
		STRATEGIES.put(593, script);
		STRATEGIES.put(1880, script);
		STRATEGIES.put(126, script);

		//event
		STRATEGIES.put(1736, script);
		STRATEGIES.put(6430, new Crimson());

		//events
		STRATEGIES.put(199, script);
		STRATEGIES.put(6330, script);
		STRATEGIES.put(6093, script);
		STRATEGIES.put(5422, script);
		STRATEGIES.put(5411, script);
		STRATEGIES.put(5408, script);
		STRATEGIES.put(5401, script);
		STRATEGIES.put(8162, script);
		STRATEGIES.put(6104, script);
		STRATEGIES.put(6105, script);
		STRATEGIES.put(6100, script);
		STRATEGIES.put(5665, script);

		// Globals
		STRATEGIES.put(9904, new Eternal());
		STRATEGIES.put(3308, new Eternal());
		STRATEGIES.put(9905, new Eternal());
		STRATEGIES.put(8010, new Eternal());
		STRATEGIES.put(587, new Eternal());
		STRATEGIES.put(8013, new Eternal());
		STRATEGIES.put(9006, new Eternal());
		STRATEGIES.put(9906, new Eternal());
		STRATEGIES.put(14378, new Eternal());
		STRATEGIES.put(7553, new Eternal());


		// Masters
		STRATEGIES.put(3010, new ZeidanCombat());

		DefaultMagicCombatStrategy defaultMagicStrategy = new DefaultMagicCombatStrategy();
//		STRATEGIES.put(13, defaultMagicStrategy);
//		STRATEGIES.put(172, defaultMagicStrategy);
//		STRATEGIES.put(174, defaultMagicStrategy);
//		STRATEGIES.put(2025, defaultMagicStrategy);
//		STRATEGIES.put(3495, defaultMagicStrategy);
//		STRATEGIES.put(3496, defaultMagicStrategy);
//		STRATEGIES.put(3491, defaultMagicStrategy);
//		STRATEGIES.put(2882, defaultMagicStrategy);
//		STRATEGIES.put(13451, defaultMagicStrategy);
//		STRATEGIES.put(13452, defaultMagicStrategy);
//		STRATEGIES.put(13453, defaultMagicStrategy);
//		STRATEGIES.put(13454, defaultMagicStrategy);
//		STRATEGIES.put(1643, defaultMagicStrategy);
//		STRATEGIES.put(6254, defaultMagicStrategy);
//		STRATEGIES.put(6257, defaultMagicStrategy);
//		STRATEGIES.put(6278, defaultMagicStrategy);
//		STRATEGIES.put(6221, defaultMagicStrategy);
//		STRATEGIES.put(109, defaultMagicStrategy);
//		STRATEGIES.put(3580, defaultMagicStrategy);
//		STRATEGIES.put(2007, defaultMagicStrategy);
//		DefaultRangedCombatStrategy defaultRangedStrategy = new DefaultRangedCombatStrategy();
//		STRATEGIES.put(688, defaultRangedStrategy);
//		STRATEGIES.put(2028, defaultRangedStrategy);
//		STRATEGIES.put(6220, defaultRangedStrategy);
//		STRATEGIES.put(6256, defaultRangedStrategy);
//		STRATEGIES.put(6276, defaultRangedStrategy);
//		STRATEGIES.put(6252, defaultRangedStrategy);
//		STRATEGIES.put(1233, defaultRangedStrategy);
//		STRATEGIES.put(27, defaultRangedStrategy);
//		STRATEGIES.put(9029, defaultMeleeCombatStrategy);
//		STRATEGIES.put(9028, defaultMagicStrategy);
//		STRATEGIES.put(9030, defaultRangedStrategy);
//		STRATEGIES.put(1511, defaultRangedStrategy);
//		STRATEGIES.put(1508, defaultRangedStrategy);

//		STRATEGIES.put(7553, new TheGeneral());
//		STRATEGIES.put(9176, new SkeletalHorror());
//		STRATEGIES.put(8000, new Galvek());
//		STRATEGIES.put(8002, new Galvek());
//		STRATEGIES.put(2745, new Jad());
//		STRATEGIES.put(3112, new Jad());
//		STRATEGIES.put(8528, new Nomad());
//		STRATEGIES.put(252, new FriezaCombatScript());
//		STRATEGIES.put(449, new FriezaCombatScript());
//		STRATEGIES.put(595, new FriezaCombatScript());
//		STRATEGIES.put(591, new FriezaCombatScript());
//		STRATEGIES.put(593, new FriezaCombatScript());
//		STRATEGIES.put(6692, new FriezaCombatScript());
//		STRATEGIES.put(DungeoneeringBossNpc.Constants.BOSS_NOHEADICON, DungeoneeringBossCombat.INSTANCE);
//		STRATEGIES.put(DungeoneeringBossNpc.Constants.BOSS_PROT_MAGE, DungeoneeringBossCombat.INSTANCE);
//		STRATEGIES.put(DungeoneeringBossNpc.Constants.BOSS_PROT_RANGE, DungeoneeringBossCombat.INSTANCE);
//		STRATEGIES.put(DungeoneeringBossNpc.Constants.BOSS_PROT_MELEE, DungeoneeringBossCombat.INSTANCE);
//
//		STRATEGIES.put(8349, new TormentedDemon());
//
//		STRATEGIES.put(3200, new ChaosElemental());
//
//		STRATEGIES.put(8133, new CorporealBeast());
//		STRATEGIES.put(13447, new Nex());
//		STRATEGIES.put(2896, new Spinolyp());
//		STRATEGIES.put(2881, new DagannothSupreme());
//		STRATEGIES.put(6260, new Graardor());
//		STRATEGIES.put(6263, new Steelwill());
//		STRATEGIES.put(6265, new Grimspike());
//		STRATEGIES.put(6222, new KreeArra());
//		STRATEGIES.put(6223, new WingmanSkree());
//		STRATEGIES.put(6225, new Geerin());
//		STRATEGIES.put(6203, new Tsutsuroth());
//		STRATEGIES.put(6208, new Kreeyath());
//		STRATEGIES.put(6206, new Gritch());
//		STRATEGIES.put(6247, new Zilyana());
//		STRATEGIES.put(6250, new Growler());
//		STRATEGIES.put(1382, new Glacor());
//		STRATEGIES.put(9939, new PlaneFreezer());
//		STRATEGIES.put(2010, new Kilik());
//		STRATEGIES.put(199, new Brandon());
//		STRATEGIES.put(200, new Crimson());
//		STRATEGIES.put(2042, new ZulrahLogic());
//		STRATEGIES.put(2043, new ZulrahLogic());
//		STRATEGIES.put(2044, new ZulrahLogic());
//		STRATEGIES.put(795, new IceQueen());
//		// STRATEGIES.put(286, new MutantKFC());
//		Dragon dragonStrategy = new Dragon();
//		STRATEGIES.put(8015, new EliteDragon());
//		STRATEGIES.put(2949, new ScarletFalcon());
//		STRATEGIES.put(4972, new Eternal());
//
//		STRATEGIES.put(50, dragonStrategy);
//		STRATEGIES.put(941, dragonStrategy);
//		STRATEGIES.put(55, dragonStrategy);
//		STRATEGIES.put(53, dragonStrategy);
//		STRATEGIES.put(54, dragonStrategy);
//		STRATEGIES.put(51, dragonStrategy);
//		STRATEGIES.put(1590, dragonStrategy);
//		STRATEGIES.put(1591, dragonStrategy);
//		STRATEGIES.put(2012, dragonStrategy);
//		STRATEGIES.put(1592, dragonStrategy);
//		STRATEGIES.put(5362, dragonStrategy);
//		STRATEGIES.put(5363, dragonStrategy);

//		Aviansie aviansieStrategy = new Aviansie();
//		STRATEGIES.put(6246, aviansieStrategy);
//		STRATEGIES.put(6230, aviansieStrategy);
//		STRATEGIES.put(6231, aviansieStrategy);
//
//		KalphiteQueen kalphiteQueenStrategy = new KalphiteQueen();
//		STRATEGIES.put(1158, kalphiteQueenStrategy);
//		STRATEGIES.put(1160, kalphiteQueenStrategy);
//
//		Revenant revenantStrategy = new Revenant();
//		STRATEGIES.put(13465, revenantStrategy);
//		STRATEGIES.put(13469, revenantStrategy);
//		STRATEGIES.put(13474, revenantStrategy);
//		STRATEGIES.put(13478, revenantStrategy);
//		STRATEGIES.put(13479, revenantStrategy);
//		STRATEGIES.put(6692, dragonStrategy);
//		STRATEGIES.put(408, dragonStrategy);
//		STRATEGIES.put(401, dragonStrategy);
//
//		STRATEGIES.put(2009, new Callisto());
//		STRATEGIES.put(2000, new Venenatis());
//		STRATEGIES.put(2006, new Vetion());
//		STRATEGIES.put(2001, new Scorpia());
//		//STRATEGIES.put(12239, new Exoden());
//		STRATEGIES.put(3975, new MaxHitStone());
//		STRATEGIES.put(8009, new NazastaroolCombatStrategy());
//		STRATEGIES.put(2, new Tekton());
//
//		STRATEGIES.put(2342, new HerbalRogue());
//		STRATEGIES.put(3830, new OptimusPrime());
//		STRATEGIES.put(NazastaroolCombatStrategy.NAZASTAROOL, new NazastaroolCombatStrategy());
//		STRATEGIES.put(DrakeCombatStrategy.DRAKE, new DrakeCombatStrategy());
//
//		STRATEGIES.put(9012, new LuciferCombatScript());
//		STRATEGIES.put(3831, new AzureBeast());
//		STRATEGIES.put(6430, new CrystalQueen());
//
//		STRATEGIES.put(1234, new CrazyWitch());
//		STRATEGIES.put(12810, new InfernalDemon());
//		STRATEGIES.put(3712, new RaidsMobs());
//		STRATEGIES.put(586, new RaidsMobs());
//		STRATEGIES.put(125, new RaidsMobs());
//		STRATEGIES.put(688, new RaidsMobs());
//		STRATEGIES.put(585, new RaidsMobs());
//		STRATEGIES.put(12843, new FractiteDemon());
//		STRATEGIES.put(449, new FriezaCombatScript());
//		STRATEGIES.put(452, new FriezaCombatScript());
//		STRATEGIES.put(252, new FriezaCombatScript());
//		STRATEGIES.put(187, new FriezaCombatScript());
//		STRATEGIES.put(188, new FriezaCombatScript());
//		STRATEGIES.put(1880, new FriezaCombatScript());
//		STRATEGIES.put(1120, new FriezaCombatScript());
//		STRATEGIES.put(440, new FriezaCombatScript());
//		STRATEGIES.put(438, new FriezaCombatScript());
//		STRATEGIES.put(1318, new FriezaCombatScript());
//		STRATEGIES.put(1311, new FriezaCombatScript());
//		STRATEGIES.put(1313, new FriezaCombatScript());
//
//
//		STRATEGIES.put(9904, new SolakTheGreat());
//		STRATEGIES.put(9906, new SolakTheGreat());
//		STRATEGIES.put(4540, new SolakTheGreat());
//		STRATEGIES.put(9907, new GolemOfSolak());
//
//		STRATEGIES.put(9908, new Vetion());
//		STRATEGIES.put(810, new InfernalDemon());
//		STRATEGIES.put(1307, new InfernalDemon());
//		STRATEGIES.put(1308, new InfernalDemon());
//		STRATEGIES.put(1309, new InfernalDemon());
//		STRATEGIES.put(1310, new InfernalDemon());
//
//		STRATEGIES.put(12239, new GolemOfSolak());
//		STRATEGIES.put(8010, new GolemOfSolak());
//		STRATEGIES.put(3112, new GolemOfSolak());
//
//		STRATEGIES.put(9020, new ArmouredBunnyCombatScript());
//		//STRATEGIES.put(9014, new SolakTheGreat());
//		//STRATEGIES.put(9017, new SolakTheGreat());
//
//		STRATEGIES.put(6047, new PantherWolf());
//
//
//		STRATEGIES.put(9815, new SimpleScript());
//		STRATEGIES.put(9816, new SimpleScript());
//		STRATEGIES.put(9817, new SimpleScript());
//		STRATEGIES.put(9818, new SimpleScript());


	}

	public static CombatStrategy getStrategy(int npc) {
		if (STRATEGIES.get(npc) != null) {
			return STRATEGIES.get(npc);
		}
		return defaultMeleeCombatStrategy;
	}

	public static CombatStrategy getDefaultMeleeStrategy() {
		return defaultMeleeCombatStrategy;
	}

	public static CombatStrategy getDefaultMagicStrategy() {
		return defaultMagicCombatStrategy;
	}

	public static CombatStrategy getDefaultRangedStrategy() {
		return defaultRangedCombatStrategy;
	}

	public static CombatStrategy getEmptyCombatStrategy() {
		return emptyCombatStrategy;
	}
}
