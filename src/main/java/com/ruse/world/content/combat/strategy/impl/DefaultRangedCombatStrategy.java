package com.ruse.world.content.combat.strategy.impl;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.*;
import com.ruse.model.Locations.Location;
import com.ruse.model.RegionInstance.RegionInstanceType;
import com.ruse.model.container.impl.Equipment;
import com.ruse.model.definitions.WeaponAnimations;
import com.ruse.model.definitions.WeaponInterfaces;
import com.ruse.model.definitions.WeaponInterfaces.WeaponInterface;
import com.ruse.util.Misc;
import com.ruse.world.content.ItemDegrading;
import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatContainer.ContainerHit;
import com.ruse.world.content.combat.CombatFactory;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.effect.CombatPoisonEffect.PoisonType;
import com.ruse.world.content.combat.range.CombatRangedAmmo.AmmunitionData;
import com.ruse.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.ruse.world.content.combat.range.CombatRangedAmmo.RangedWeaponType;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.combat.weapon.CombatSpecial;
import com.ruse.world.content.combat.weapon.FightStyle;
import com.ruse.world.content.minigames.impl.Dueling;
import com.ruse.world.content.minigames.impl.Dueling.DuelRule;
import com.ruse.world.content.tbdminigame.Game;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.GroundItemManager;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import java.util.Arrays;

/**
 * The default combat strategy assigned to an {@link Character} during a ranged
 * based combat session.
 * 
 * @author lare96
 */
public class DefaultRangedCombatStrategy implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {

		// We do not need to check npcs.
		if (entity.isNpc()) {
			return true;
		}

		// Create the player instance.
		Player player = (Player) entity;

		// If we are using a crystal bow then we don't need to check for ammo.
		if (CombatFactory.crystalBow(player)) {
			return true;
		}

		if (Dueling.checkRule(player, DuelRule.NO_RANGED)) {
			player.getPacketSender().sendMessage("Ranged-attacks have been turned off in this duel!");
			player.getCombatBuilder().reset(true);
			return false;
		}

		// Check the ammo before proceeding.
		if (!checkAmmo(player)) {
			if (player.isSpecialActivated()) {
				player.setSpecialActivated(false);
				CombatSpecial.updateBar(player);
			}
			return false;
		}
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		if (entity.isNpc()) {
			NPC npc = (NPC) entity;
			AmmunitionData ammo = AmmunitionData.ADAMANT_ARROW;
			switch (npc.getId()) {
			case 688:
				ammo = AmmunitionData.BRONZE_ARROW;
				break;
			case 27:
				ammo = AmmunitionData.STEEL_ARROW;
				break;
			case 2028:
				ammo = AmmunitionData.BOLT_RACK;
				break;
			case 6220:
			case 6256:
			case 6276:
				ammo = AmmunitionData.RUNE_ARROW;
				break;
			case 6225:
				ammo = AmmunitionData.STEEL_JAVELIN;
				break;
			case 6252:
				case 1511:
				case 1508:
				ammo = AmmunitionData.RUNE_ARROW;
				break;
			}

			entity.performAnimation(new Animation(npc.getDefinition().getAttackAnim()));

			entity.performGraphic(new Graphic(ammo.getStartGfxId(),
					ammo.getStartHeight() >= 43 ? GraphicHeight.HIGH : GraphicHeight.MIDDLE));

			fireProjectile(npc, victim, ammo, false);

			CombatContainer combatContainer = new CombatContainer(entity, victim, 1, CombatType.RANGED, true);

			if(((NPC) entity).isTreeMinigameNpc)
				combatContainer.setModifiedDamage(Misc.random(Game.NPC_MIN_DAMAGE,Game.NPC_MAX_DAMAGE));

			return combatContainer;
		}

		Player player = (Player) entity;
		final boolean dBow = CombatFactory.darkBow(player);

		player.setFireAmmo(0);

		startAnimation(player);

		AmmunitionData ammo = RangedWeaponData.getAmmunitionData(player);


		CombatContainer container = new CombatContainer(entity, victim, dBow ? 2 : 1, CombatType.RANGED, true);

		/** CROSSBOW BOLTS EFFECT **/
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition() != null && player.getEquipment()
				.get(Equipment.WEAPON_SLOT).getDefinition().getName().toLowerCase().contains("crossbow")) {
			if (Misc.getRandom(12) >= 10) {
				container.setModifiedDamage(getModifiedDamage(player, victim, container));
			}
		}
		if (!player.isSpecialActivated()) {
			if (CombatFactory.toxicblowpipe(player)) {
				if (Misc.inclusiveRandom(1, 3) > 1) {
					ItemDegrading.handleItemDegrading(player, ItemDegrading.DegradingItem.TOXIC_BLOWPIPE);
				} /*
				 * else {
				 * player.getPacketSender().sendMessage("did not degrade bp because rng"); }
				 */
			}
			if (!CombatFactory.crystalBow(player)) {
				decrementAmmo(player, victim.getPosition());
				if (dBow || player.getRangedWeaponData() == RangedWeaponData.MAGIC_SHORTBOW
						&& player.isSpecialActivated() && player.getCombatSpecial() != null
						&& player.getCombatSpecial() == CombatSpecial.MAGIC_SHORTBOW) {
					decrementAmmo(player, victim.getPosition());
				}
			}

			player.performGraphic(new Graphic(ammo.getStartGfxId(), ammo.getStartGfxId() == 2138 ? GraphicHeight.LOW
					: ammo.getStartHeight() >= 43 ? GraphicHeight.HIGH : GraphicHeight.MIDDLE));

			fireProjectile(player, victim, ammo, dBow);
		}
		return container;
	}

	public static void fireProjectile(Character e, Character victim, final AmmunitionData ammo, boolean dBow) {
		TaskManager.submit(new Task(1, e.getCombatBuilder(), false) { // TODO FIX THIS PROJECTILE, SHOULDNT BE A TASK
			@Override
			protected void execute() {
				new Projectile(e, victim, ammo.getProjectileId(), ammo.getProjectileDelay() + 16,
						ammo.getProjectileSpeed(), ammo.getStartHeight(), ammo.getEndHeight(), 0).sendProjectile();
				if (dBow) {
					new Projectile(e, victim, ammo.getProjectileId(), ammo.getProjectileDelay() + 32,
							ammo.getProjectileSpeed(), ammo.getStartHeight() - 2, ammo.getEndHeight(), 0)
									.sendProjectile();
				}
				stop();
			}
		});
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {

		// The default distance for all npcs using ranged is 6.
		if (entity.isNpc()) {
			return 6;
		}

		// Create the player instance.

		Player player = (Player) entity;

		int distance = 5;
		if (player.getRangedWeaponData() != null)
			distance = player.getRangedWeaponData().getType().getDistanceRequired();

		if (player.getRegionInstance() != null && (player.getRegionInstance().equals(RegionInstanceType.KRAKEN)
				|| player.getRegionInstance().equals(RegionInstanceType.ZULRAH))) {
			distance += 3;
		}

		return distance + (player.getFightType().getStyle() == FightStyle.DEFENSIVE ? 2 : 0);
	}

	/**
	 * Starts the performAnimation for the argued {@link Player} in the current
	 * combat hook.
	 * 
	 * @param player the player to start the performAnimation for.
	 */
	private void startAnimation(Player player) {
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition().getName().startsWith("Karils")) {
			player.performAnimation(new Animation(2075));
		} else {
			player.performAnimation(new Animation(WeaponAnimations.getAttackAnimation(player)));
		}
	}

	/**
	 * Checks the ammo to make sure the argued {@link Player} has the right type and
	 * amount before attacking.
	 * 
	 * @param player the player's ammo to check.
	 * @return <code>true</code> if the player has the right ammo,
	 *         <code>false</code> otherwise.
	 */
	private boolean checkAmmo(Player player) {
		RangedWeaponData data = player.getRangedWeaponData();
		if (data.getType() == RangedWeaponType.THROW)
			return true;
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 22010
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 16337
						|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3738
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21018
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 20497
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 22134
								|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21053
										|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21054
												|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21055
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 2040
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 2074
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 2158
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 2206
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 2220
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 2296
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 22115
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 2470
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 14343
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 2550
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 2632
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 2658
														|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21056
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 20173
						|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 671
								|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 16879
										|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 18332
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 20553
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 22083
						|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 18636
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 21606
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 4017
								|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 8088
										|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 5073
										|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 5012
										|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 12843
												|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 5011
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 22113
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 5010
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 8411
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 22148


										|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 8001
										|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 15785
										|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 14919
								|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 8253
										|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 7543
										|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 2632
										|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 22089
												|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 9929
														|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 14056
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 19843
						|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 9941
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 19137
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 19135
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 19136) { // ||
																						// player.getEquipment().get(Equipment.WEAPON_SLOT).getId()
																						// == 20171) {
			return true;

		}

		if (CombatFactory.toxicblowpipe(player)) {
			int charges = player.getBlowpipeCharges();
			// // System.out.println(charges);
			if (charges <= 0) {
				player.getPacketSender().sendMessage("You have no charges in your blowpipe!");
				// player.getPacketSender().sendMessage("You have no charges!");
				return false;
			}
		}

		Item ammunition = player.getEquipment().getItems()[data.getType() == RangedWeaponType.THROW
				? Equipment.WEAPON_SLOT
				: Equipment.AMMUNITION_SLOT];
		boolean darkBow = data.getType() == RangedWeaponType.DARK_BOW && ammunition.getAmount() < 2
				|| data == RangedWeaponData.MAGIC_SHORTBOW && player.isSpecialActivated()
						&& player.getCombatSpecial() != null
						&& player.getCombatSpecial() == CombatSpecial.MAGIC_SHORTBOW && ammunition.getAmount() < 2;
		/*if (ammunition.getId() == -1 || ammunition.getAmount() < 1 || darkBow) {
			player.getPacketSender().sendMessage(darkBow ? "You need at least 2 arrows to fire this bow."
					: "You don't have any ammunition to fire.");
			player.getCombatBuilder().reset(true);
			return false;
		}*/
		/*boolean properEquipment = false;
		for (AmmunitionData ammo : data.getAmmunitionData()) {
			for (int i : ammo.getItemIds()) {
				if (i == ammunition.getId()) {
					properEquipment = true;
					break;
				}
			}
		}
		if (!properEquipment) {
			String ammoName = ammunition.getDefinition().getName(),
					weaponName = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getDefinition().getName(),
					add = !ammoName.endsWith("s") && !ammoName.endsWith("(e)") ? "s" : "";
			player.getPacketSender().sendMessage("You can not use " + ammoName + "" + add + " with "
					+ Misc.anOrA(weaponName) + " " + weaponName + ".");
			player.getCombatBuilder().reset(true);
			return false;
		}
*/
		return true;
	}

	/**
	 * Decrements the amount ammo the {@link Player} currently has equipped.
	 * 
	 * @param player the player to decrement ammo for.
	 */
	public static void decrementAmmo(Player player, Position pos) {

		// Determine which slot we are decrementing ammo from.
		int slot = player.getWeapon() == WeaponInterface.SHORTBOW || player.getWeapon() == WeaponInterface.LONGBOW
				|| player.getWeapon() == WeaponInterface.CROSSBOW || player.getWeapon() == WeaponInterface.BLOWPIPE
				|| player.getWeapon() == WeaponInterface.BSOAT || player.getWeapon() == WeaponInterface.ARMADYLXBOW
						? Equipment.AMMUNITION_SLOT
						: Equipment.WEAPON_SLOT;

		// Set the ammo we are currently using.
		player.setFireAmmo(player.getEquipment().get(slot).getId());
		final boolean ardy = player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 19748;
		final boolean avas = player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 10499
				|| player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 3805;
		//final boolean skillcape = player.getSkillManager().skillCape(Skill.RANGED);
		final boolean zaryte = player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 20171
				|| player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 20553;

		if (player.getFireAmmo() != 22006) {
			if ((avas || ardy) && Misc.getRandom(11) <= 9) { // Avas
				return;
			}
			if (zaryte) {
				return;
			}
		}
		
		
		//MAKES BOWS NOT REQUIRE AMMO
		if (slot == Equipment.AMMUNITION_SLOT){
			return;
		}

		// Decrement the ammo in the selected slot.
		player.getEquipment().get(slot).decrementAmount();

		if (player.getFireAmmo() != 22006){
		if ((!avas || !ardy || !zaryte) && player.getFireAmmo() != 15243) {
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(player.getFireAmmo()), pos,
						player.getUsername(), false, 120, true, 120));
		}}

		// If we are at 0 ammo remove the item from the equipment completely.

		int[] ignore = {
				22010, 20553, 21018, 20497, 22134, 16337, 3738, 21053, 21054, 21055, 21056, 19137, 20173, 671,
				16879, 18332, 20083, 19136, 19135, 18636, 15785, 8088, 5073, 8411, 22148, 5012, 12843, 5011,
				22113, 5010, 8001, 21606, 4017, 14919, 8253, 7543, 2632, 22089, 9929, 14056, 9941, 2040, 2074,
				2158, 2206, 2220, 2296, 22115, 2470, 14343, 2550, 2632, 2658, 19843
		};

		if (player.getEquipment().get(slot).getAmount() == 0) {
			if(Arrays.stream(ignore).anyMatch(i -> i == player.getEquipment().get(slot).getId())){
				return;
			} else {
				player.getPacketSender().sendMessage("You have run out of ammunition!");
				player.getEquipment().set(slot, new Item(-1));
			}

			if (slot == Equipment.WEAPON_SLOT) {
				WeaponInterfaces.assign(player, new Item(-1));
			}
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}

		// Refresh the equipment interface.
		player.getEquipment().refreshItems();
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		return false;
	}

	private static long getModifiedDamage(Player player, Character target, CombatContainer container) {
		if (container == null || container.getHits().length < 1)
			return 0;
		ContainerHit hit = container.getHits()[0];
		if (!hit.isAccurate())
			return 0;
		long damage = container.getHits()[0].getHit().getDamage();
		int ammo = player.getFireAmmo();
		if (ammo == -1) {
			return damage;
		}
		double multiplier = 1;
		Player pTarget = target.isPlayer() ? ((Player) target) : null;
		switch (ammo) {
		case 9236: // Lucky Lightning
		case 21053:
			target.performGraphic(new Graphic(749));
			multiplier = 1.3;
			break;
			
		case 9237: // Earth's Fury
		case 21054:
			target.performGraphic(new Graphic(755));
			multiplier = 1.05;
			break;
		case 9238: // Sea Curse
		case 21055:
			target.performGraphic(new Graphic(750));
			multiplier = 1.1;
			break;
		case 21056:
			target.performGraphic(new Graphic(860));
			multiplier = 1.7;
			break;
//		case 9239: // Down To Earth
//
//			target.performGraphic(new Graphic(757));
//			if (pTarget != null) {
//				pTarget.getSkillManager().setCurrentLevel(Skill.MAGIC,
//						pTarget.getSkillManager().getCurrentLevel(Skill.MAGIC) - 3);
//				pTarget.getPacketSender().sendMessage("Your Magic level has been reduced.");
//			}
//			break;
//		case 9240: // Clear Mind
//			target.performGraphic(new Graphic(751));
//			if (pTarget != null) {
//				pTarget.getSkillManager().setCurrentLevel(Skill.PRAYER,
//						pTarget.getSkillManager().getCurrentLevel(Skill.PRAYER) - 40);
//				if (pTarget.getSkillManager().getCurrentLevel(Skill.PRAYER) < 0) {
//					pTarget.getSkillManager().setCurrentLevel(Skill.PRAYER, 0);
//				}
//				pTarget.getPacketSender().sendMessage("Your Prayer level has been leeched.");
//				player.getSkillManager().setCurrentLevel(Skill.PRAYER,
//						pTarget.getSkillManager().getCurrentLevel(Skill.PRAYER) + 40);
//				if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) > player.getSkillManager()
//						.getMaxLevel(Skill.PRAYER)) {
//					player.getSkillManager().setCurrentLevel(Skill.PRAYER,
//							player.getSkillManager().getMaxLevel(Skill.PRAYER));
//				} else {
//					player.getPacketSender()
//							.sendMessage("Your enchanced bolts leech some Prayer points from your opponent..");
//				}
//			}
//			break;
		case 9241: // Magical Posion
			target.performGraphic(new Graphic(752));
			CombatFactory.poisonEntity(target, PoisonType.MILD);
			break;
//		case 9242:
//			if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION)
//					- player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) / 200 < 10) {
//				break;
//			}
//			int priceDamage = (int) (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) * 0.08);
//			if (priceDamage < 0) {
//				return damage;
//			}
//			int dmg2 = (int) ((int) ((int) target.getConstitution() * 0.065) > 1000 ? 650 + Misc.getRandom(50)
//					: ((int) target.getConstitution() * 0.065));
//			if (dmg2 <= 0) {
//				return damage;
//			}
//			target.performGraphic(new Graphic(754));
//			player.dealDamage(new Hit(priceDamage, Hitmask.RED, CombatIcon.RANGED));
//			return dmg2;
		case 9243:
			target.performGraphic(new Graphic(758, GraphicHeight.MIDDLE));
			multiplier = 1.15;
			break;
		case 9244:
			target.performGraphic(new Graphic(756));
			if (pTarget != null && (pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 1540
					|| pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 13655
					|| pTarget.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283
					|| pTarget.getFireImmunity() > 0)) {
				return damage;
			}
			if (damage < 300 && Misc.getRandom(3) <= 1) {
				damage = 300 + Misc.getRandom(150);
			}
			multiplier = 1.25;
			break;
		case 9245:
			target.performGraphic(new Graphic(753));
			multiplier = 1.26;
			int heal = (int) (damage * 0.25) + 10;
			player.heal(heal);
			if (damage < 250 && Misc.getRandom(3) <= 1) {
				damage += 150 + Misc.getRandom(80);
			}
			// player.getPacketSender().sendMessage("[DEBUG]: Onyx bolts healed: "+heal);
			break;
		}
		return (int) (damage * multiplier);
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}
}
