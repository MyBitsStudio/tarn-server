package com.ruse.world.content.skill.impl.summoning;

import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

public class BossPets {

	@Getter
	public enum BossPet {

		//AENEAS(9020, 9021, 23018),
		//TLAHUICOLE(9020, 9021, 23018),
		//NEYTIRI(9020, 9021, 23018),
		//ATTICUS(9020, 9021, 23018),
		//ELEANOR(9020, 9021, 23018),
		//SOREN(9020, 9021, 23018),
		//CASSIUS(9020, 9021, 23018),
		//ZORA(9020, 9021, 23018),
		//ANAIS(9020, 9021, 23018),
		//ASTAIRE(9020, 9021, 23018),
		//GAEL(9020, 9021, 23018),
		//SKILLING(9020, 9021, 23018),
		//MONSTEROUS(9020, 9021, 23018),
		//SLAYER(9020, 9021, 23018),
		//COLLECTOR(9020, 9021, 23018),
		//MASTER(9020, 9021, 23018),


		MAGE_PET(9031, 23041, 500,"10% Magic damage boost"),
		MELEE_PET(9032, 23042,500, "10% Melee damage boost"),
		RANGED_PET(9033, 23043, 500,"10% Ranged damage boost"),
		KILJAEDEN_PET(9826, 23114, 500,"15% Overall damage boost"),

		SKREEG_PET(9827, 23115, 400,"10% Drop rate boost\\n10% Double Drop rate boost"),
		ORIX_PET(9828, 23116, 400,"20% Drop rate boost\\n20% Double Drop rate boost"),
		CRYSTAL_ORC_PET(9829, 23117, 500,"25% Drop rate boost\\n25% Double Drop rate boost"),
		RAMMUS_PET(9822, 23164, 400,"20% Upgrade boost"),

		DEMON_PET(9819, 23161, 300,"10% Overall damage boost\\n10% Drop rate boost\\n10% Double Drop rate boost"),
		GOLEM_PET(9821, 23163, 400,"15% Overall damage boost\\n15% Drop rate boost\\n15% Double Drop rate boost"),
		DRAGON_PET(9820, 23162, 1500,"25% Overall damage boost\\n25% Drop rate boost\\n25% Double Drop rate boost"),

		DBZ(302, 11314, "Double killcount on all npc"),
		HOOKER(4444, 20511, "Extra AFK Ore"),

		FENRIR_PET(9830, 23108, 300,"2X EXP Boost\\n25% off on skill island shops"),
		GREEN_FENRIR_PET(9831, 23109, 300,"10% Overall damage boost,\\n 10% Drop rate boost,\\n 10% Double Drop rate boost,\\n while doing minigames"),
		RED_FENRIR_PET(9832, 23110,300, "2X Slayer Tickets on Task \\n20% Overall dmg boost on task"),
		HEIMDALL_PET(9834, 23112, 400,"Collects all items on the\\nground to the bank."),
		ODIN_PET(9833, 23111, 400,
				"20% Overall damage boost\\n25% DR, DDR, Upgrade Boost\\n Collects items on ground to bank\\n 2X EXP Boost"),

		//NEW BOSS PETS START
		SANCTUM_GOLEM_PET(3023, 19481, 300,"10% Drop rate boost\\n10% Double Drop rate boost"),
		MUTANT_HYDRA_PET(3024, 19482, 300,"12% Drop rate boost\\n12% Double Drop rate boost"),
		GORVEK_PET(9808, 23081, 300,"14% Drop rate boost\\n14% Double Drop rate boost"),
		DRAGONITE_PET(3025, 19483, 300,"16% Drop rate boost\\n16% Double Drop rate boost"),
		ASMODEUS_PET(9905, 19484, 300,"18% Drop rate boost\\n18% Double Drop rate boost"),
		MALVEK_PET(3026, 19485, 300,"20% Drop rate boost\\n20% Double Drop rate boost"),
		ONYX_GRIFFIN_PET(3027, 19486, 300,"22% Drop rate boost\\n22% Double Drop rate boost"),
		ZEIDAN_GRIMM_PET(3028, 19487, 300,"24% Drop rate boost\\n24% Double Drop rate boost"),
		AGTHOMOTH_PET(3029, 19488, 300,"26% Drop rate boost\\n26% Double Drop rate boost"),
		LILINRYSS_PET(3030, 19489, 300,"28% Drop rate boost\\n28% Double Drop rate boost"),
		GROUDON_PET(3031, 19490, 300,"30% Drop rate boost\\n30% Double Drop rate boost"),
		VARTHRAMOTH_PET(3032, 19491, 300,"32% Drop rate boost\\n32% Double Drop rate boost"),
		TYRANT_LORD_PET(3033, 19492, 300,"34% Drop rate boost\\n34% Double Drop rate boost"),
		LUCIFER_PET(9013, 19493, 300,"36% Drop rate boost\\n36% Double Drop rate boost"),
		VIRTUOSO_PET(3034, 19494, 300,"38% Drop rate boost\\n38% Double Drop rate boost"),
		AGUMON_PET(3035, 19495, 300,"40% Drop rate boost\\n40% Double Drop rate boost"),
		WHITE_BEARD_PET(3036, 20582, 300,"42% Drop rate boost\\n42% Double Drop rate boost"),
		PANTHER_PET(3037, 20583, 300,"44% Drop rate boost\\n44% Double Drop rate boost"),
		LEVIATHAN_PET(3038, 20584, 300,"46% Drop rate boost\\n46% Double Drop rate boost"),
		CALAMITY_PET(3039, 20585, 300,"48% Drop rate boost\\n48% Double Drop rate boost"),
		SLENDER_MAN_PET(3040, 20586, 300,"50% Drop rate boost\\50% Double Drop rate boost"),
		CHARYBDIS_PET(3041, 20587, 300,"52% Drop rate boost\\52% Double Drop rate boost"),
		SCYLLA_PET(3042, 20588, 300,"54% Drop rate boost\\54% Double Drop rate boost"),
		EXODEN_PET(3043, 20589, 300,"56% Drop rate boost\\56% Double Drop rate boost"),
		EZKEL_NOJAD_PET(3044, 20590, 300,"58% Drop rate boost\\58% Double Drop rate boost"),
		JANEMBA_PET(3045, 20602, 300,"60% Drop rate boost\\60% Double Drop rate boost"),
		FRIEZA_PET(3046, 20603, 300,"62% Drop rate boost\\62% Double Drop rate boost"),
		PERFECT_CELL_PET(928, 20604, 300,"64% Drop rate boost\\64% Double Drop rate boost"),
		SUPER_BUU_PET(4001, 20605, 300,"66% Drop rate boost\\66% Double Drop rate boost"),
		GOKU_PET(3047, 13774, 300,"68% Drop rate boost\\68% Double Drop rate boost"),
		BYAKUYA_PET(3048, 13775, 300,"70% Drop rate boost\\70% Double Drop rate boost"),
		FAZULA_PET(1312, 4073, 300,"75% Drop rate boost\\75% Double Drop rate boost"),
		//NEW BOSS PETS END

		VINDICTA_PET(9809, 23082),
		AVARYSS_PET(9803, 23083),
		CONJKOINED_PET(9804,  23084),
		NYMORA_PET(9805, 23085),

		DEMON_LIGHT_PET(9840, 23151),
		BAT_LIGHT_PET(9841, 23152),
		GOBLIN_LIGHT_PET(9842, 23153),
		LAVA_HOUND_PET(9843,  23154),
		MUTATED_HOUND_PET(9844,  23155),

		SCORPION_PET(9847, 23156),
		ZOMBIE_PET(9848,  23157),
		;

		BossPet(int npcId, int itemId) {
			this.npcId = npcId;
			this.itemId = itemId;
		}
		BossPet(int npcId, int itemId, String boost) {
			this.npcId = npcId;
			this.itemId = itemId;
			this.boost = boost;
		}
		BossPet(int npcId, int itemId, int zoom, String boost) {
			this.npcId = npcId;
			this.itemId = itemId;
			this.zoom = zoom;
			this.boost = boost;
		}

		public int npcId, itemId, zoom = 800;
		public String boost = "None";

		public static BossPet forId(int itemId) {
			for (BossPet p : BossPet.values()) {
				if (p != null && p.itemId == itemId) {
					return p;
				}
			}
			return null;
		}

		public static BossPet forSpawnId(int spawnNpcId) {
			for (BossPet p : BossPet.values()) {
				if (p != null && p.npcId == spawnNpcId) {
					return p;
				}
			}
			return null;
		}
	}

	public static boolean pickup(Player player, NPC npc) {
		BossPet pet = BossPet.forSpawnId(npc.getId());
		if (pet != null) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.getPacketSender().sendMessage("You need a free inventory space to pick up a pet.");
				return false;
			}
			if (player.getSummoning().getFamiliar() != null
					&& player.getSummoning().getFamiliar().getSummonNpc() != null
					&& player.getSummoning().getFamiliar().getSummonNpc().isRegistered()) {
				if (player.getSummoning().getFamiliar().getSummonNpc().getId() == pet.npcId
						&& player.getSummoning().getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
					player.getSummoning().unsummon(true, true);
					player.getPacketSender().sendMessage("You pick up your pet.");
					return true;
				} else {
					player.getPacketSender().sendMessage("This is not your pet to pick up.");
				}
			} else {
				player.getPacketSender().sendMessage("This is not your pet to pick up.");
			}
		}
		return false;
	}

}
