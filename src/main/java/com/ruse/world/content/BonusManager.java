package com.ruse.world.content;

import com.ruse.model.Item;
import com.ruse.model.Prayerbook;
import com.ruse.model.container.impl.Equipment;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.projectile.ItemEffect;
import com.ruse.world.content.combat.Maxhits;
import com.ruse.world.content.combat.prayer.CurseHandler;
import com.ruse.world.content.combat.prayer.PrayerHandler;
import com.ruse.world.content.equipmentenhancement.BoostType;
import com.ruse.world.entity.impl.player.Player;
import lombok.var;

public class BonusManager {
	public static final String[] BONUS_NAMES = new String[]{"Stab", "Slash", "Crush", "Magic", "Ranged", "Stab",
			"Slash", "Crush", "Magic", "Ranged", "ELO", "", "", "",
			"Strength", "Ranged STR", "Prayer", "Magic STR"};

	public static void update(Player player) {

		double[] bonuses = new double[18];
		for (Item item : player.getEquipment().getItems()) {
			ItemDefinition definition = ItemDefinition.forId(item.getId());
			for (int i = 0; i < definition.getBonus().length; i++) {
				bonuses[i] += definition.getBonus()[i];
				if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
					bonuses[10] += definition.getBonus()[i] / 1000;
				} else {
					bonuses[10] += definition.getBonus()[i] / 3000;
				}
			}
			ItemEffect effect = item.getEffect();
			if(effect == ItemEffect.NOTHING)
				continue;

		}
		for (Item item : player.getSecondaryEquipment().getItems()) {
			ItemDefinition definition = ItemDefinition.forId(item.getId());
			for (int i = 0; i < definition.getBonus().length; i++) {
				bonuses[i] += definition.getBonus()[i];
				if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
					bonuses[10] += definition.getBonus()[i] / 1000;
				} else {
					bonuses[10] += definition.getBonus()[i] / 3000;
				}
			}
			ItemEffect effect = item.getEffect();
			if(effect == ItemEffect.NOTHING)
				continue;
		}
		if (player.isMini()) {
			for (Item item : player.getEquipment().getItems()) {
				ItemDefinition definition = ItemDefinition.forId(item.getId());
				for (int i = 0; i < definition.getBonus().length; i++) {
					bonuses[i] += definition.getBonus()[i];
				}
			}
		}

		var extraBonus = player.getEquipmentEnhancement().getBoost(BoostType.STATS);
		for (int i = 0; i < STRING_ID.length; i++) {
			double bonus = bonuses[i] * (1 + (extraBonus / 100.0));
			if (i <= 4) {
				player.getBonusManager().attackBonus[i] = bonus;
			} else if (i <= 9) {
				int index = i - 5;
				player.getBonusManager().defenceBonus[index] = bonus;
			} else if (i <= 13) {
				int index = i - 10;
				player.getBonusManager().extraBonus[index] = bonus;
			} else {
				int index = i - 14;
				player.getBonusManager().otherBonus[index] = bonus;
			}
			if(i == 11 || i == 12 || i == 13)
				continue;
			player.getPacketSender().sendString(21190 + i, BONUS_NAMES[i] + ": " + ((int) bonus > 0 ? "+ " + formatNumber((int) bonus) : formatNumber((int) bonus)));
		}

		player.getPacketSender().sendString(66106, "Drop Rate Bonus: " + CustomDropUtils.drBonus(player, player.getSlayer().getSlayerTask().getNpcId()));
		player.getPacketSender().sendString(66107, "Double Drop Bonus: " + CustomDropUtils.getDoubleDropChance(player, player.getSlayer().getSlayerTask().getNpcId()));

		player.getPacketSender().sendString(66108, "Melee Maxhit: " +  formatNumber(Maxhits.melee(player, player)));
		player.getPacketSender().sendString(66109, "Ranged Maxhit: " +  formatNumber(Maxhits.ranged(player, player)));
		player.getPacketSender().sendString(66110, "Magic Maxhit: " +  formatNumber(Maxhits.magic(player, player)));

	}

	public double[] getAttackBonus() {
		return attackBonus;
	}

	public double[] getDefenceBonus() {
		return defenceBonus;
	}

	public double[] getExtraBonus() {
		return extraBonus;
	}

	public double[] getOtherBonus() {
		return otherBonus;
	}

	private double[] attackBonus = new double[5];

	private double[] defenceBonus = new double[5];

	private double[] extraBonus = new double[4];

	private double[] otherBonus = new double[4];

	private static final String[][] STRING_ID = { { "1675", "Stab" }, // 0
			{ "1676", "Slash" }, // 1
			{ "1677", "Crush" }, // 2
			{ "1678", "Magic" }, // 3
			{ "1679", "Range" }, // 4

			{ "1680", "Stab" }, // 5
			{ "1681", "Slash" }, // 6
			{ "1682", "Crush" }, // 7
			{ "1683", "Magic" }, // 8
			{ "1684", "Range" }, // 9

			{ "15115", "ELO" }, // 10
			{ "15116", "" }, // 11
			{ "15117", "" }, // 12
			{ "15118", "" }, // 13

			{ "1686", "Strength" }, // 14
			{ "15119", "Ranged Strength" }, // 15
			{ "1687", "Prayer" }, // 16
			{ "15120", "Magic Damage" } // 17
	};

	public static final int ATTACK_STAB = 0, ATTACK_SLASH = 1, ATTACK_CRUSH = 2, ATTACK_MAGIC = 3, ATTACK_RANGE = 4,

	DEFENCE_STAB = 0, DEFENCE_SLASH = 1, DEFENCE_CRUSH = 2, DEFENCE_MAGIC = 3, DEFENCE_RANGE = 4,

			DEFENCE_SUMMONING = 0, ABSORB_MELEE = 1, ABSORB_MAGIC = 2, ABSORB_RANGED = 3,

	BONUS_STRENGTH = 0, RANGED_STRENGTH = 1, BONUS_PRAYER = 2, MAGIC_DAMAGE = 3;

	/** CURSES **/

	public static void sendCurseBonuses(final Player p) {
		if (p.getPrayerbook() == Prayerbook.CURSES || p.getPrayerbook() == p.getPrayerbook().HOLY) {
			sendAttackBonus(p);
			sendDefenceBonus(p);
			sendStrengthBonus(p);
			sendRangedBonus(p);
			sendMagicBonus(p);
		}
	}

//	public static String formatNumber(long num) {
////		boolean negative = false;
////		if (num < 0) {
////			num = -num;
////			negative = true;
////		}
////		int length = String.valueOf(num).length();
////		String number = Long.toString(num);
////		String numberString = number;
////		String end = "";
////		if (length == 4) {
////			numberString = number.substring(0, 1) + "K";
////			double doubleVersion = 0.0;
////			doubleVersion = num / 1000.0;
////			if (doubleVersion != (int)doubleVersion) {
////				if (num - (1000 * Math.ceil(doubleVersion)) > 100) {
////					numberString = number.substring(0, 1) + "." + number.substring(1, 2) + "K";
////				}
////			}
////		} else if (length == 5) {
////			numberString = number.substring(0, 2) + "K";
////		} else if (length == 6) {
////			numberString = number.substring(0, 3) + "K";
////		} else if (length == 7) {
////			String sub = number.substring(1, 2);
////			if (sub.equals("0")) {
////				numberString = number.substring(0, 1) + "M";
////			} else {
////				numberString = number.substring(0, 1) + "." + number.substring(1, 2) + "M";
////			}
////		} else if (length == 8) {
////			end = "." + number.substring(2, 3);
////			if (end.equals(".0")) {
////				end = "";
////			}
////			numberString = number.substring(0, 2) + end + "M";
////		} else if (length == 9) {
////			end = "." + number.substring(3, 4);
////			if (end.equals(".0")) {
////				end = "";
////			}
////			numberString = number.substring(0, 3) + end + "M";
////		} else if (length == 10) {
////			numberString = number.substring(0, 4) + "M";
////		} else if (length == 11) {
////			numberString = number.substring(0, 2) + "." + number.substring(2, 5) + "B";
////		} else if (length == 12) {
////			numberString = number.substring(0, 3) + "." + number.substring(3, 6) + "B";
////		} else if (length == 13) {
////			numberString = number.substring(0, 4) + "." + number.substring(4, 7) + "B";
////		}
////		if (negative) {
////			numberString = "-" + numberString;
////		}
////		return numberString;
////	}

	public static String formatNumber(long num) {
		boolean negative = false;
		if (num < 0) { num = -num; negative = true; }
		int length = (int) Math.log10(num) + 1; // compute length more efficiently
		 String numberString; if (length <= 6) {// handle cases up to 999,999
			numberString = Long.toString(num);
		 } else if (length <= 9) { // handle cases up to 999,999,999
			 double doubleVersion = num / 1_000_000.0;
			 if (doubleVersion == Math.floor(doubleVersion) && doubleVersion < 1000) {
				 numberString = String.format("%.0fM", doubleVersion); // no decimal places if pure integer
			 } else {
				 numberString = String.format("%.1fM", doubleVersion); // 1 decimal place if not pure integer
			 }
		 } else { // handle cases greater than 999,999,999
			  double doubleVersion = num / 1_000_000_000.0;
			  if (doubleVersion == Math.floor(doubleVersion)) {
				  numberString = String.format("%.0fB", doubleVersion);
			  } else {
				  numberString = String.format("%.1fB", doubleVersion);
			  }
		 }
		 if (negative) {
			 numberString = "-" + numberString;
		 }
		 return numberString;
	}

	public static void sendAttackBonus(Player p) {
		double boost = p.getLeechedBonuses()[0];
		int bonus = 0;
		if (p.getCurseActive()[CurseHandler.LEECH_ATTACK]) {
			bonus = 5;
		}if (p.getPrayerActive()[PrayerHandler.FURY_SWIPE]) {
			bonus += 20;
		}if (p.getPrayerActive()[PrayerHandler.HUNTERS_EYE]) {
			bonus += 20;
		}if (p.getPrayerActive()[PrayerHandler.SOUL_LEECH]) {
			bonus += 10;
		} else if (p.getCurseActive()[CurseHandler.TURMOIL])
			bonus = 15;
		bonus += boost;
		if (bonus < -25)
			bonus = -25;
		p.getPacketSender().sendString(690, "" + getColor(bonus) + "" + bonus + "%");
	}

	public static void sendRangedBonus(Player p) {
		double boost = p.getLeechedBonuses()[4];
		int bonus = 0;
		if (p.getPrayerActive()[PrayerHandler.FORTITUDE]) {
			bonus += 20;
		}
		if (p.getPrayerActive()[PrayerHandler.FURY_SWIPE]) {
			bonus += 20;
		}
		if (p.getPrayerActive()[PrayerHandler.SOUL_LEECH]) {
			bonus += 10;
		}
		if (p.getCurseActive()[CurseHandler.LEECH_RANGED]){
			bonus = 5;
		} else if (p.getCurseActive()[CurseHandler.TURMOIL])
			bonus = 23;
		bonus += boost;
		if (bonus < -25)
			bonus = -25;
		p.getPacketSender().sendString(693, "" + getColor(bonus) + "" + bonus + "%");
	}

	public static void sendMagicBonus(Player p) {
		double boost = p.getLeechedBonuses()[6];
		int bonus = 0;
		if (p.getCurseActive()[CurseHandler.LEECH_MAGIC]) {
			bonus = 5;
		}
		if (p.getPrayerActive()[PrayerHandler.FURY_SWIPE]){
			bonus += 20;
		}
		if (p.getPrayerActive()[PrayerHandler.SOUL_LEECH]){
			bonus += 10;
		}
		if (p.getPrayerActive()[PrayerHandler.DESTRUCTION]){
			bonus += 20;

		} else if (p.getCurseActive()[CurseHandler.TURMOIL])
			bonus = 23;
		bonus += boost;
		if (bonus < -25)
			bonus = -25;
		p.getPacketSender().sendString(694, "" + getColor(bonus) + "" + bonus + "%");
	}

	public static void sendDefenceBonus(Player p) {
		double boost = p.getLeechedBonuses()[1];
		int bonus = 0;
		if (p.getPrayerActive()[PrayerHandler.FURY_SWIPE])
			bonus += 10;
		if (p.getPrayerActive()[PrayerHandler.FORTITUDE])
			bonus += 10;
		if (p.getPrayerActive()[PrayerHandler.HUNTERS_EYE])
			bonus += 10;
		if (p.getPrayerActive()[PrayerHandler.DESTRUCTION])
			bonus += 10;
		if (p.getCurseActive()[CurseHandler.LEECH_DEFENCE])
			bonus = 5;
		else if (p.getCurseActive()[CurseHandler.TURMOIL])
			bonus = 15;
		bonus += boost;
		if (bonus < -25)
			bonus = -25;
		p.getPacketSender().sendString(692, "" + getColor(bonus) + "" + bonus + "%");
	}

	public static void sendStrengthBonus(Player p) {
		double boost = p.getLeechedBonuses()[2];
		int bonus = 0;
		if (p.getCurseActive()[CurseHandler.LEECH_STRENGTH])
			bonus += 5;
		if (p.getPrayerActive()[PrayerHandler.HUNTERS_EYE])
			bonus += 20;
		if (p.getPrayerActive()[PrayerHandler.FURY_SWIPE])
			bonus += 20;
		if (p.getPrayerActive()[PrayerHandler.SOUL_LEECH])
			bonus += 10;
		else if (p.getCurseActive()[CurseHandler.TURMOIL])
			bonus = 23;
		bonus += boost;
		if (bonus < -25)
			bonus = -25;
		p.getPacketSender().sendString(691, "" + getColor(bonus) + "" + bonus + "%");
	}

	public static String getColor(int i) {
		if (i > 0)
			return "@gre@+";
		if (i < 0)
			return "@red@";
		return "";
	}
}
