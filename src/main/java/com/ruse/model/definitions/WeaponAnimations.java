package com.ruse.model.definitions;

import com.ruse.model.CharacterAnimations;
import com.ruse.model.Item;
import com.ruse.model.container.impl.Equipment;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.CombatConstants;

/**
 * A static utility class that contains methods for changing the appearance
 * animation for a player whenever a new weapon is equipped or an existing item
 * is unequipped.
 * 
 * @author lare96
 */
public final class WeaponAnimations {

	/**
	 * Executes an animation for the argued player based on the animation of the
	 * argued item.
	 * 
	 * @param player the player to animate.
	 * @param item   the item to get the animations for.
	 */

	public static void assign(Player player, Item item) {
		player.getCharacterAnimations().reset();
		player.setCharacterAnimations(getUpdateAnimation(player));
	}

	public static void update(Player player) {
		// player.getCharacterAnimations().reset();
		player.setCharacterAnimations(getUpdateAnimation(player));
	}

	public static CharacterAnimations getUpdateAnimation(Player player) {
		Item item = player.getEquipment().getItems()[Equipment.WEAPON_SLOT];
		int weaponId = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
		int shieldId = player.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId();
		String weaponName = item.getDefinition().getName().toLowerCase();
		int playerStandIndex = 0x328;
		int playerWalkIndex = 0x333;
		int playerRunIndex = 0x338;
		int playerTurnIndex = 0x337;
		int playerTurn180Index = 0x334;
		int playerTurn90CWIndex = 0x335;
		int playerTurn90CCWIndex = 0x336;
		if (weaponName.contains("halberd") || weaponName.contains("guthan")) {
			playerStandIndex = 809;
			playerWalkIndex = 1146;
			playerRunIndex = 1210;
		}else if (weaponName.startsWith("basket")) {
			playerStandIndex = 1836;
			playerWalkIndex = 1836;
			playerRunIndex = 1836;
		} else if (weaponName.contains("dharok") || (weaponName.contains("barb axe"))) {
			playerStandIndex = 0x811;
			playerWalkIndex = 0x67F;
			playerRunIndex = 0x680;
		} else if (weaponName.contains("sled")) {
			playerStandIndex = 1461;
			playerWalkIndex = 1468;
			playerRunIndex = 1467;
			playerTurnIndex = 1468;
			playerTurn180Index = 1468;
			playerTurn90CWIndex = 1468;
			playerTurn90CCWIndex = 1468;
		} else if (weaponName.contains("dinh")) {
			playerStandIndex = 7508;
			playerWalkIndex = 7510;
			playerRunIndex = 7509;
			playerTurnIndex = 7509;
			playerTurn180Index = 7509;
			playerTurn90CWIndex = 7509;
			playerTurn90CCWIndex = 7509;
		
		} else if (weaponName.contains("ahrim")) {
			playerStandIndex = 809;
			playerWalkIndex = 1146;
			playerRunIndex = 1210;
		} else if (weaponName.contains("verac")) {
			playerStandIndex = 0x328;
			playerWalkIndex = 0x333;
			playerRunIndex = 824;
		} else if (weaponName.contains("longsword") || weaponName.contains("scimitar")) {
			playerStandIndex = 15069;// 12021;
			playerRunIndex = 15070;// 12023;
			playerWalkIndex = 15073; // 12024;
		} else if (weaponName.contains("rpg") || weaponName.contains("bazooka")) {
			playerStandIndex = 15069;// 12021;
			playerRunIndex = 15070;// 12023;
			playerWalkIndex = 15073; // 12024;
		} else if (weaponName.contains("ojaku")) { // kojaku
			playerStandIndex = 12021;
			playerRunIndex = 15070;
			playerWalkIndex = 15073;
		} else if (weaponName.contains("silverlight") || weaponName.contains("korasi's")
				|| weaponName.contains("katana") || weaponName.toLowerCase().contains("rapier")
				|| weaponName.contains("tempest") || weaponName.contains("ojaku")) {
			// playerStandIndex = 12021;
			// playerRunIndex = 12023;
			// playerRunIndex = 1210; <-
			// playerWalkIndex = 12024;
			playerStandIndex = 8980;
			playerRunIndex = 1210;
			playerWalkIndex = 1146;
		} else if (weaponName.contains("wand") || weaponName.contains("staff") || weaponName.contains("staff") || weaponName.contains("scythe")
				|| weaponName.contains("spear") || item.getId() == 21005 || item.getId() == 21010 || item.getId() == 14355 || item.getId() == 2544) {
			playerStandIndex = 8980;
			playerRunIndex = 1210;
			playerWalkIndex = 1146;
		} else if (weaponName.contains("karil")) {
			playerStandIndex = 2074;
			playerWalkIndex = 2076;
			playerRunIndex = 2077;
		} else if (weaponName.contains("2h sword") || weaponName.contains("godsword")
				|| weaponName.contains("saradomin sw")) {
			playerStandIndex = 7047;
			playerWalkIndex = 7046;
			playerRunIndex = 7039;
		} else if (weaponName.contains("bow")) {
			playerStandIndex = 808;
			playerWalkIndex = 819;
			playerRunIndex = 824;
		}
		switch (item.getId()) {
		case 18353: // maul chaotic
		case 16425:
		case 16184:
			playerStandIndex = 13217;
			playerWalkIndex = 13218;
			playerRunIndex = 13220;
			break;
		case 5095:
		case 8009:
		case 6936:
		case 22091:
		case 5096:
		case 7640:
		case 18629:
		case 22092:
		case 3739:
		case 8089:
		case 16249:
		case 8412:
			// case 20554:
		case 18356:
		case 19331:
		case 13640:
			case 2396:
		case 20555:
		
			playerStandIndex = 809;
			playerWalkIndex = 819;
			playerRunIndex = 70;
			break;
		case 22080:
			playerStandIndex = 809;
			playerWalkIndex = 70;
			playerRunIndex = 70;
			break;
		case 5497:// sled
			playerStandIndex = 1461;
			playerWalkIndex = 1468;
			playerRunIndex = 1467;
			break;
		case 4151:
		case 21371: // vine whip
		case 13444:
		case 15441: // whip
		case 15442: // whip
		case 15443: // whip
		case 15444: // whip
		case 22008: // abyssal tent
		case 18686:
		case 22077:
		case 17624:
		case 18011:
		case 16835:
		case 4178: // maximum whip
		case 15888:
		case 18750:
		case 20534:
		case 20535:
		case 20536:
		case 20537:
		case 20538:
		case 20539:
		case 20540:
		case 20554:
		case 20541:
		case 20542:
		case 14919:
		case 8088:
			case 2220:
		case 21606:
			
		case 8411:
			case 22148:
			case 2410:
		case 22089:
		case 8001:
		case 7543:
			case 2632:
	
		case 9929:
		case 14056:
			playerStandIndex = 11973;
			playerWalkIndex = 11975;
		
			playerRunIndex = 1661;
			break;
		case 4569:
			playerStandIndex = 7531;
			break;
//		case 4568:
//			playerStandIndex = 2762;
//			playerWalkIndex = 2763;
//			playerRunIndex = 2750;
//			break;
			
		case 10887:
			playerStandIndex = 5869;
			playerWalkIndex = 5867;
			playerRunIndex = 5868;
			break;
		case 6528:
		case 20084:
			playerStandIndex = 0x811;
			playerWalkIndex = 2064;
			playerRunIndex = 1664;
			break;
		case 4153:
		case 17734:
		case 13760:
			playerStandIndex = 1662;
			playerWalkIndex = 1663;
			playerRunIndex = 1664;
			break;
		case 15241:
		case 19843:
		case 19137:
		case 17712:
		case 18636:
		case 8253:
		case 19135:
		case 671:
			case 22090:
		case 19136:
			playerStandIndex = 12155;
			playerWalkIndex = 12154;
			playerRunIndex = 12154;
			break;
		case 20000:
		case 20001:
		case 20002:
		case 20003:
		case 11694:
		case 11696:
		case 11730:
		case 11698:
		case 11700:
			break;
		case 1305:
		case 20549:
		case 8087:
		case 15788:
		case 8410:
			playerStandIndex = 809;
			break;
		case 1419:
			playerStandIndex = 847;
			break;
		}
		if (weaponId == 17698 && shieldId == 17700) {
			playerStandIndex = 0x328;
			playerWalkIndex = 0x333;
			playerRunIndex = 4029;
		}
		if (player.canFly() && player.isFlying()) {
			playerStandIndex = 1501;
			playerWalkIndex = 1501;
			playerRunIndex = 1851;
			playerTurnIndex = 1501;
			playerTurn180Index = 1501;
			playerTurn90CWIndex = 1501;
			playerTurn90CCWIndex = 1501;
		}
		if (player.canGhostWalk() && player.isGhostWalking()
		//|| CombatConstants.wearingGhost(player)
		) {
			playerStandIndex = 15;
			playerWalkIndex = 13;
			playerRunIndex = 13;
			playerTurnIndex = 13;
			playerTurn180Index = 13;
			playerTurn90CWIndex = 13;
			playerTurn90CCWIndex = 13;
		}
		return new CharacterAnimations(playerStandIndex, playerWalkIndex, playerRunIndex, playerTurnIndex,
				playerTurn180Index, playerTurn90CWIndex, playerTurn90CCWIndex);
	}

	public static int getAttackAnimation(Player c) {
		int weaponId = c.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
		int shieldId = c.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId();
		String weaponName = ItemDefinition.forId(weaponId).getName().toLowerCase();
		String prop = c.getFightType().toString().toLowerCase();
		if (weaponId == 1419) {
			if (prop.contains("jab")) {
				return 11981;
			}
			return 2066;
		}
		if (weaponId == 18373)
			return 1074;
		if (weaponId == 9941 || weaponId == 20553)
			return 5061;
		if (weaponId == 5012)
			return 1074;
		if (weaponId == 12843)
			return 1074;
		if (weaponId == 2296)
			return 1074;
		if (weaponId == 20542 || weaponId == 11308)
			return 440;
		if (weaponId == 17698 && shieldId == 17700)
			return 4841;
		if (weaponId == 22010)// ginrei
			return 1074;
		if (weaponId == 21606)
			return 4230;
		if (weaponId == 4017)
			return 4230;
		if (weaponId == 19137 || weaponId == 19135 || weaponId == 671 || weaponId == 19136 || weaponId == 8253 || weaponId == 18636 || weaponId == 19843 || weaponId == 19843
				|| weaponId == 17712)// Minigun
			return 12153;
		if (weaponId == 17704)
			return 4230;
		if (weaponId == 10033 || weaponId == 10034)
			return 2779;
		if (prop.contains("dart")) {
			if (prop.contains("long"))
				return 6600;
			return 582;
		}
		if (weaponName.contains("javelin") || weaponName.contains("thrownaxe")) {
			return 806;
		}
		if (weaponId == 22080 || weaponName.contains("halberd")) {
			return 440;
		}
		if (weaponName.startsWith("dragon dagger") || weaponId == 22039) {
			if (prop.contains("slash"))
				return 377;
			return 376;
		}
		if (weaponName.endsWith("dagger")) {
			if (prop.contains("slash"))
				return 13048;
			return 13049;
		}
		if (weaponName.equals("staff of light") || weaponId == 21005 || weaponId == 21010) {
			if (prop.contains("stab"))
				return 13044;
			else if (prop.contains("lunge"))
				return 13047;
			else if (prop.contains("slash"))
				return 13048;
			else if (prop.contains("block"))
				return 13049;
		} else if (weaponName.startsWith("staff") || weaponName.endsWith("staff")) {
			// bash = 400
			return 401;
		}
		if (weaponName.endsWith("warhammer") || weaponName.endsWith("battleaxe") || weaponName.contains("hammer"))
			return 401;
		if (weaponName.contains("2h sword") || weaponName.contains("godsword")
				|| weaponName.contains("saradomin sword")) {
			/*
			 * if(prop.contains("stab")) return 11981; else if(prop.contains("slash"))
			 * return 11980;
			 */
			return 11979;
		}
		if (weaponName.contains("brackish")) {
			if (prop.contains("lunge") || prop.contains("slash"))
				return 12029;
			return 12028;
		}
		if (weaponName.contains("scimitar") || weaponName.contains("longsword") || weaponName.contains("korasi's")
				|| weaponName.contains("katana") || weaponName.contains("tempest") || weaponName.contains("sickle")) {
			if (prop.contains("lunge"))
				return 15072;
			
			return 15071;
		}
		if (weaponName.contains("spear")) {
			if (prop.contains("lunge"))
				return 13045;
			else if (prop.contains("slash"))
				return 13047;
			return 13044;
		}
		if (weaponName.contains("rapier")) {
			if (prop.contains("slash"))
				return 12029;
			return 386;
		}
		if (weaponName.contains("claws"))
			return 393;
		if (weaponName.contains("maul") && !weaponName.contains("granite"))
			return 13055;
		if (weaponName.contains("dharok")) {
			if (prop.contains("block"))
				return 2067;
			return 2066;
		}
		if (weaponName.contains("sword")) {
			return prop.contains("slash") ? 12311 : 12310;
		}
		if (weaponName.contains("karil"))
			return 2075;
		else if (weaponName.contains("'bow") || weaponName.contains("crossbow"))
			return 4230;
		if (weaponName.contains("bow") && !weaponName.contains("'bow"))
			return 426;
		if (weaponName.contains("pickaxe")) {
			if (prop.contains("smash"))
				return 401;
			return 400;
		}
		if (weaponName.contains("mace")) {
			if (prop.contains("spike"))
				return 13036;
			return 13035;
		}
		switch (weaponId) { // if you don't want
		// to use strings
		case 17702:
		return 10961;
		case 14919:
		case 22089:
		case 7543:
			case 2632:
		case 14056:
		case 9929:
		case 5073:
			return 4230;
		case 21057:
			return 4603;
		case 20000:
		case 20001:
		case 20002:
		case 20003:
			return 7041;
		case 22083:
			return 4230;
		case 6522: // Obsidian throw
			return 2614;
		case 4153: // granite maul
		case 17734:
		case 16137:
		case 13760:
			return 1665;
		case 21053:
		case 21054:
		case 21055:
			case 2074:
		case 21056:
		return 4230;
		case 17726:
			return 12028;
		case 17724:
			return 12029;
		case 13879:
		case 13956:
		case 13883:
			return 806;
		case 16184:
			return 2661;
		case 16425:
			return 2661;
		case 15241:
			return 12153;
		case 4747: // torag
			return 0x814;
		case 4710: // ahrim
			return 406;
		case 18353:
			return 13055;
		case 18349:
			return 386;
		case 11798:
			return 7512;
		case 19146:
			return 386;
		case 4755: // verac
			return 2062;
		case 4734: // karil
			return 2075;
		case 10887:
			
			return 5865;
			
		case 4151:
		case 13444:
		case 15441: // whip
		case 15442: // whip
		case 15443: // whip
		case 15444: // whip
		case 22008: // abyssal whip
		case 18686:
		case 21371: // vine whip
			if (prop.contains("flick"))
				return 11968;
			else if (prop.contains("lash"))
				return 11969;
			else if (prop.contains("deflect"))
				return 11970;
		case 6528:
		case 20084:
			return 2661;
		default:
			return c.getFightType().getAnimation();
		}
	}

	public static int getBlockAnimation(Player c) {
		int weaponId = c.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
		String shield = ItemDefinition.forId(c.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId()).getName()
				.toLowerCase();
		String weapon = ItemDefinition.forId(weaponId).getName().toLowerCase();
		if (shield.contains("defender"))
			return 4177;
		if (shield.contains("2h"))
			return 7050;
		if (shield.contains("book") && (weapon.contains("wand")))
			return 420;
		if (shield.contains("shield"))
			return 1156;
		if (weapon.contains("scimitar") || weapon.contains("longsword") || weapon.contains("katana")
				|| weapon.contains("korasi") || weapon.contains("tempest"))
			return 15074;
		switch (weaponId) {
		
		case 4755:
			return 2063;
		case 15241:
		case 18636:
		case 8253:
	
		case 19843:
		case 17712:
		case 19137:
		case 19135:
			case 2040:
		case 671:
		case 19136:
			return 12156;
		case 13899:
			return 13042;
		case 18355:
			return 13046;
		case 14484:
		case 18685:
		case 17652:
			return 397;
		case 11716:
			return 12008;
		case 4153:
		case 17734:
			return 1666;
		case 1419:
			return 7050;
		case 4151:
		case 21371: // vine whip
		case 13444:
		case 15441: // whip
		case 15442: // whip
		case 15443: // whip
		case 15444: // whip
		case 22008: // abyssal whip
		case 18686:
			return 11974;
		case 15486:
		case 15502:
		case 22209:
		case 22211:
		case 22207:
		case 22213:
		case 21005:
		case 21010:
		case 14004:
		case 14005:
		case 14006:
		case 14007:
		case 19908:
			return 12806;
		case 18349:
			return 12030;
		case 18353:
			return 13054;
		case 18351:
			return 13042;
		case 22010:
			return 13042;
		case 20000:
		case 20001:
		case 20002:
		case 20003:
		case 11694:
		case 11698:
		case 11700:
		case 11696:
		case 11730:
			return 7050;
		case -1:
			return 424;
		default:
			return 424;
		}
	}
}
