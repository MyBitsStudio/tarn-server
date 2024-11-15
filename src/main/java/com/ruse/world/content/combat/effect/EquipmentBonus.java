package com.ruse.world.content.combat.effect;

import com.ruse.model.container.impl.Equipment;
import com.ruse.world.entity.impl.player.Player;

public class EquipmentBonus {

	public static int[] helms = { 11665, 11664, 11663 }; // 11665 == melee, 11664 == range, 11663 == mage
	public static int[] mhelms = { 9363, 9360, 9364 }; // 9363 == melee, 9360 == range, 9364 == mage
	public static int gloves = 8842;
	public static int[] normRobes = { 8839, 8840 };
	public static int[] eRobes = { 19785, 19786 };
	public static int[] mRobes = { 9361, 9362 };
	public static int deflector = 19712;
	public static int mace = 8841;
	public static int SSHELM = 14803;
	public static int SSBODY = 14804; // 11665 == melee, 11664 == range, 11663 == mage
	public static int SSLEGS = 14805;

	public static final int[] obsidianMeleeWeapons = { 6523, 6528, 6527, 6525 };

	public static boolean berserkerNecklaceEffect(Player player) {
		boolean good = false;
		if (player.getEquipment().get(Equipment.AMULET_SLOT).getId() == 11128) {

			for (int i = 0; i < obsidianMeleeWeapons.length; i++) {
				if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == obsidianMeleeWeapons[i]) {
					good = true;
					break;
				}
			}

		}

		if (good) {
			return true;
		}
		return false;
	}

	/*
	 * HELMS -> 11665 Melee, 11664 Range, 11663 Mage DEFLECTOR -> 19712 BODY -> 8839
	 * LEGS -> 8840 GLOVES -> 8842 ELITE BODY -> 19785 ELITE LEGS -> 19786
	 */

	public static boolean voidElite(Player player) {
		if (wearingVoid(player) && (player.checkItem(Equipment.BODY_SLOT, eRobes[0])
				&& player.checkItem(Equipment.LEG_SLOT, eRobes[1]))) {
			return true;
		}
		return false;
	}

	public static boolean voidMaster(Player player) {
		if (wearingVoid(player) && (player.checkItem(Equipment.BODY_SLOT, mRobes[0])
				&& player.checkItem(Equipment.LEG_SLOT, mRobes[1]))) {
			return true;
		}
		return false;
	}

	public static boolean voidRange(Player player) {
		if (wearingVoid(player) && player.checkItem(Equipment.HEAD_SLOT, helms[1])) {
			return true;
		}
		return false;
	}

	public static boolean voidMelee(Player player) {
		if (player.checkItem(Equipment.HEAD_SLOT, helms[0])) {
			return true;
		}
		return false;
	}

	public static boolean voidMage(Player player) {
		if (wearingVoid(player) && player.checkItem(Equipment.HEAD_SLOT, helms[2])) {
			return true;
		}
		return false;
	}

	public static boolean voidmRange(Player player) {
		if (wearingVoid(player) && player.checkItem(Equipment.HEAD_SLOT, mhelms[1])) {
			return true;
		}
		return false;
	}

	public static boolean voidmMelee(Player player) {
		if (wearingVoid(player) && player.checkItem(Equipment.HEAD_SLOT, mhelms[0])) {
			return true;
		}
		return false;
	}

	public static boolean voidmMage(Player player) {
		if (wearingVoid(player) && player.checkItem(Equipment.HEAD_SLOT, mhelms[2])) {
			return true;
		}
		return false;
	}

	public static boolean wearingVoid(Player player) {
		boolean hasHelm = false;
		int correctEquipment = 0;
		if (player.checkItem(Equipment.BODY_SLOT, eRobes[0]) || player.checkItem(Equipment.BODY_SLOT, mRobes[0])
				|| player.checkItem(Equipment.BODY_SLOT, normRobes[0])) {
			correctEquipment++;
		}
		if (player.checkItem(Equipment.LEG_SLOT, eRobes[1]) || player.checkItem(Equipment.LEG_SLOT, mRobes[1])
				|| player.checkItem(Equipment.LEG_SLOT, normRobes[1])) {
			correctEquipment++;
		}
		if (player.checkItem(Equipment.HEAD_SLOT, mhelms[0]) || player.checkItem(Equipment.HEAD_SLOT, mhelms[1])
				|| player.checkItem(Equipment.HEAD_SLOT, mhelms[2]) || player.checkItem(Equipment.HEAD_SLOT, helms[0])
				|| player.checkItem(Equipment.HEAD_SLOT, helms[1]) || player.checkItem(Equipment.HEAD_SLOT, helms[2])) {
			hasHelm = true;
			correctEquipment++;
		}
		if (player.checkItem(Equipment.SHIELD_SLOT, deflector)) {
			correctEquipment++;
		}
		if (player.checkItem(Equipment.HANDS_SLOT, gloves)) {
			correctEquipment++;
		}
		if (player.checkItem(Equipment.WEAPON_SLOT, mace)) {
			correctEquipment++;
		}

		if (correctEquipment > 3 && hasHelm) {
			// // System.out.println("Returned true.");
			return true;
		}
		// // System.out.println("Returned false.");
		return false;
	}

}
