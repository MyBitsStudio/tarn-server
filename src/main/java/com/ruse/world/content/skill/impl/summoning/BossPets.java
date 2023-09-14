package com.ruse.world.content.skill.impl.summoning;

import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

		//COSMETIC
		GORILLA_PET(3001, 460, 500,""),
		WOLF_PET(4414, 459, 500,""),
		BEAR_PET(105, 458, 500,""),
		UNICORN_PET(133, 457, 500,""),
		SCORPION_PET(271, 456, 500,""),
		PENGUIN_PET(6305, 22017, 500,""),

		//EFFECT
		MAGE_PET(9031, 23041, 500,"25% Magic damage boost"),
		MELEE_PET(9032, 23042,500, "25% Melee damage boost"),
		RANGED_PET(9033, 23043, 500,"25% Ranged damage boost"),
		BUNNY_PET(9830, 23108, 300,"20% Defence Boost"),
		VOTE_PET(6324, 22050, 300,"Double Vote Rewards"),
		FENRIR_PET(9830, 23108, 300,"250 DR & DDR & 10% Damage"),
		RAICHU_PET(1801, 11318, 500,"Double Damage Towards Globals"),
		MARLEY_PET(8007, 22018, 500,"Double Loyalty Tickets"),

		//ELITE
		GREEN_FENRIR_PET(9831, 23109, 300,"500 DR & DDR & 15% Damage"),
		RED_FENRIR_PET(9832, 23110,300, "1000 DR & DDR & 20% Damage"),
		HOOKER(4444, 20511, "Extra Coins and Collector"),
		SHADOW(302, 11314, "100 % DR Rate & 50% Damage"),
		HEIMDALL_PET(9834, 23112, 400,"6x6 AoE Effect"),
		ODIN_PET(9833, 23111, 400, "Firewall Perk"),
		WARLORD_PET(6304, 22016, 400,""),

		//BOSS

		SANCTUM_GOLEM_PET(3023, 19481, 300,""),
		MUTANT_HYDRA_PET(3024, 19482, 300,""),
		GORVEK_PET(9808, 23081, 300,""),
		DRAGONITE_PET(3025, 19483, 300,""),
		ASMODEUS_PET(9905, 19484, 300,""),
		MALVEK_PET(3026, 19485, 300,""),
		ONYX_GRIFFIN_PET(3027, 19486, 300,""),
		ZEIDAN_GRIMM_PET(3028, 19487, 300,""),
		AGTHOMOTH_PET(3029, 19488, 300,""),
		LILINRYSS_PET(3030, 19489, 300,""),
		GROUDON_PET(3031, 19490, 300,""),
		VARTHRAMOTH_PET(3032, 19491, 300,""),
		TYRANT_LORD_PET(3033, 19492, 300,""),
		LUCIFER_PET(9013, 19493, 300,""),
		VIRTUOSO_PET(3034, 19494, 300,""),
		AGUMON_PET(3035, 19495, 300,""),
		WHITE_BEARD_PET(3036, 20582, 300,""),
		PANTHER_PET(3037, 20583, 300,""),
		LEVIATHAN_PET(3038, 20584, 300,""),
		CALAMITY_PET(3039, 20585, 300,""),
		SLENDER_MAN_PET(3040, 20586, 300,""),
		CHARYBDIS_PET(3041, 20587, 300,""),
		SCYLLA_PET(3042, 20588, 300,""),
		EXODEN_PET(3043, 20589, 300,""),
		EZKEL_NOJAD_PET(3044, 20590, 300,""),
		JANEMBA_PET(3045, 20602, 300,""),
		FRIEZA_PET(3046, 20603, 300,""),
		PERFECT_CELL_PET(928, 20604, 300,""),
		SUPER_BUU_PET(4001, 20605, 300,""),
		GOKU_PET(3047, 13774, 300,""),
		BYAKUYA_PET(3048, 13775, 300,""),
		FAZULA_PET(1312, 4073, 300,""),
		JOKER_PET(184, 5074, 300,""),

		SKREEG_PET(9827, 23115, 400,"10% Drop rate boost\\n10% Double Drop rate boost"),
		ORIX_PET(9828, 23116, 400,"20% Drop rate boost\\n20% Double Drop rate boost"),
		CRYSTAL_ORC_PET(9829, 23117, 500,"25% Drop rate boost\\n25% Double Drop rate boost"),
		RAMMUS_PET(9822, 23164, 400,"20% Upgrade boost"),

		DEMON_PET(9819, 23161, 300,"10% Overall damage boost\\n10% Drop rate boost\\n10% Double Drop rate boost"),
		GOLEM_PET(9821, 23163, 400,"15% Overall damage boost\\n15% Drop rate boost\\n15% Double Drop rate boost"),
		DRAGON_PET(9820, 23162, 1500,"25% Overall damage boost\\n25% Drop rate boost\\n25% Double Drop rate boost"),

		//NEW BOSS PETS START

		//NEW BOSS PETS END

		VINDICTA_PET(9809, 23082),
		AVARYSS_PET(9803, 23083),
		CONJKOINED_PET(9804,  23084),
		NYMORA_PET(9805, 23085),

//		DEMON_LIGHT_PET(9840, 23151),
//		BAT_LIGHT_PET(9841, 23152),
//		GOBLIN_LIGHT_PET(9842, 23153),
//		LAVA_HOUND_PET(9843,  23154),
//		MUTATED_HOUND_PET(9844,  23155),
//
//		SCORPION_PET(9847, 23156),
//		ZOMBIE_PET(9848,  23157),
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

		@Contract(pure = true)
		public static @Nullable BossPet forId(int itemId) {
			for (BossPet p : BossPet.values()) {
				if (p != null && p.itemId == itemId) {
					return p;
				}
			}
			return null;
		}

		@Contract(pure = true)
		public static @Nullable BossPet forSpawnId(int spawnNpcId) {
			for (BossPet p : BossPet.values()) {
				if (p != null && p.npcId == spawnNpcId) {
					return p;
				}
			}
			return null;
		}
	}

	public static boolean pickup(Player player, @NotNull NPC npc) {
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
