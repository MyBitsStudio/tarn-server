package com.ruse.world.content.combat;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Animation;
import com.ruse.model.Graphic;
import com.ruse.model.GraphicHeight;
import com.ruse.model.Item;
import com.ruse.model.Locations.Location;
import com.ruse.model.container.impl.Equipment;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.definitions.WeaponAnimations;
import com.ruse.util.Misc;
import com.ruse.world.content.*;
import com.ruse.world.content.combat.strategy.impl.Nex;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.npc.NPCMovementCoordinator.CoordinateState;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.slot.EffectHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HitQueue {

	public final CopyOnWriteArrayList<CombatHit> combat_hits = new CopyOnWriteArrayList<CombatHit>();

	public void append(CombatHit c) {
		if (c == null) {
			return;
		}
		if (c.initialRun()) {
			c.handleAttack();
		} else {
			combat_hits.add(c);
		}
	}

	public void process() {
		for (CombatHit c : combat_hits) {
			if (c == null) {
				combat_hits.remove(c);
				continue;
			}
			if (c.delay > 0) {
				c.delay--;
			} else {
				c.handleAttack();
				combat_hits.remove(c);
			}
		}
	}

	public static class CombatHit {

		private Player player;

		/** The attacker instance. */
		private final Character attacker;

		/** The victim instance. */
		private final Character victim;

		/** The attacker's combat builder attached to this task. */
		private final CombatBuilder builder;

		/** The attacker's combat container that will be used. */
		private final CombatContainer container;

		/** The total damage dealt during this hit. */
		private long damage;

		private int initialDelay;
		private int delay;

		public CombatHit(CombatBuilder builder, CombatContainer container) {
			this.builder = builder;
			this.container = container;
			this.attacker = builder.getCharacter();
			this.victim = builder.getVictim();
		}

		public CombatHit(CombatBuilder builder, CombatContainer container, int delay) {
			this.builder = builder;
			this.container = container;
			this.attacker = builder.getCharacter();
			this.victim = builder.getVictim();
			this.delay = initialDelay = delay;
		}

		public void handleAttack() {
			if (attacker.getConstitution() <= 0 || !attacker.isRegistered()) {
				return;
			}
			if (victim == null) {
				return;
			}

			if (container.getModifiedDamage() > 0) {
				container.allHits(context -> {
					context.getHit().setDamage(container.getModifiedDamage());
					context.setAccurate(true);
				});
			}

			// Now we send the hitsplats if needed! We can't send the hitsplats
			// there are none to send, or if we're using magic and it splashed.
			if (container.getHits().length != 0 && container.getCombatType() != CombatType.MAGIC
					|| container.isAccurate()) {

				/** PRAYERS **/
				CombatFactory.applyPrayerProtection(container, builder);

				this.damage = container.getDamage();
				victim.getCombatBuilder().addDamage(attacker, damage);
				container.dealDamage();

				/** MISC **/
				if (attacker.isPlayer()) {
					Player p = (Player) attacker;

					if (damage > 0) {
						/** ACHIEVEMENTS **/

						if(victim.isNpc()){
							NPC npc = victim.toNpc();
							npc.onDamage(p, damage);
						}

						EffectHandler.handlePlayerAttack(p, victim);

						if(p.getCompanion().getCompanion() != null && p.getCompanion().getCompanion().getActive().get()){
							p.getCompanion().handleAttack(victim);
						}

					}
					p.getControllerManager().processOutgoingHit(container);
				} else if (victim.isPlayer() && container.getCombatType() == CombatType.DRAGON_FIRE) {
					Player p = (Player) victim;
					if (Misc.getRandom(4) <= 3
							&& p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283) {
						p.setPositionToFace(attacker.getPosition().copy());
						CombatFactory.chargeDragonFireShield(p);
					}
					if (p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 1540
							|| p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 13655) {
						p.setPositionToFace(attacker.getPosition().copy());
						CombatFactory.sendFireMessage(p);
					}
					//damage *=10;
					if (damage >= 160) {
						((Player) victim).getPacketSender()
								.sendMessage("You are badly burnt by the dragon's fire!");
					}
					p.getControllerManager().processOutgoingHit(container);
				} else if (victim.isPlayer()){
					if(attacker.isNpc()){
						if(damage > 0){
							EffectHandler.handlePlayerDefence(attacker.toNpc(), victim.asPlayer(), damage);

							if(victim.asPlayer().getCompanion().getCompanion() != null && victim.asPlayer().getCompanion().getCompanion().getActive().get()){
								victim.asPlayer().getCompanion().handleDefence(attacker);
							}
						}
					}
				}
			}

			// Give experience based on the hits.
			CombatFactory.giveExperience(builder, container, damage);

			if (container.isAccurate()) {
				CombatFactory.handleArmorEffects(attacker, victim, damage, container.getCombatType());
				CombatFactory.handlePrayerEffects(attacker, victim, damage, container.getCombatType());
				CombatFactory.handleSpellEffects(attacker, victim, damage, container.getCombatType());

				attacker.poisonVictim(victim, container.getCombatType());

				// Finish the magic spell with the correct end graphic.
				if (attacker.isPlayer() && AutoCastSpell.getAutoCastSpell((Player) attacker) != null
						&& attacker.getCurrentlyCasting() != null) {
					attacker.getCurrentlyCasting().endGraphic().ifPresent(victim::performGraphic);
				} else if (container.getCombatType() == CombatType.MAGIC && attacker.getCurrentlyCasting() != null) {
					attacker.getCurrentlyCasting().endGraphic().ifPresent(victim::performGraphic);
					attacker.getCurrentlyCasting().finishCast(attacker, victim, true, damage);
					attacker.setCurrentlyCasting(null);
				}
			} else if (container.getCombatType() == CombatType.MAGIC && attacker.getCurrentlyCasting() != null) {
				victim.performGraphic(new Graphic(85, GraphicHeight.MIDDLE));
				attacker.getCurrentlyCasting().finishCast(attacker, victim, false, 0);
				attacker.setCurrentlyCasting(null);
			}

			// Degrade items that need to be degraded
			if (victim.isPlayer()) {
				CombatFactory.handleDegradingArmor((Player) victim);
			}
			if (attacker.isPlayer()) {
				CombatFactory.handleDegradingWeapons((Player) attacker);
			}

			// Send the defensive animations.
			if (victim.getCombatBuilder().getAttackTimer() <= 2) {
				if (victim.isPlayer()) {
					victim.performAnimation(new Animation(WeaponAnimations.getBlockAnimation(((Player) victim))));
				} else if (victim.isNpc()) {
					//if (!(((NPC) victim).getId() >= 6142 && ((NPC) victim).getId() <= 6145))
					victim.performAnimation(new Animation(((NPC) victim).getDefinition().getDefenceAnim()));
				}
			}

			// Fire the container's dynamic hit method.
			container.onHit(damage, container.isAccurate());

			// And finally auto-retaliate if needed.
			if (!victim.getCombatBuilder().isAttacking() || victim.getCombatBuilder().isCooldown()
					|| victim.isNpc() && ((NPC) victim).findNewTarget()) {
				if (shouldRetaliate()) {
					if (initialDelay == 0) {
						TaskManager.submit(new Task(1, victim, false) {
							@Override
							protected void execute() {
								if (shouldRetaliate()) {
									retaliate();
								}
								stop();
							}
						});
					} else {
						retaliate();
					}
				}
			}

			if (attacker.isNpc() && victim.isPlayer()) {
				NPC npc = (NPC) attacker;
				Player p = (Player) victim;
				if (npc.switchesVictim() && Misc.getRandom(6) <= 1) {
					if (npc.getDefinition().isAggressive()) {
						npc.setFindNewTarget(true);
					} else if (!p.getLocalPlayers().isEmpty()) {
						List<Player> list = p.getLocalPlayers();
						Player c = list.get(Misc.getRandom(list.size() - 1));
						npc.getCombatBuilder().attack(c);
					}
				}

				Sounds.sendSound(p, Sounds.getPlayerBlockSounds(p.getEquipment().get(Equipment.WEAPON_SLOT).getId()));

			} else if (attacker.isPlayer()) {
				Player player = (Player) attacker;

				player.getPacketSender().sendCombatBoxData(victim);

				player.setLastCombatType(container.getCombatType());

				Sounds.sendSound(player, Sounds.getPlayerAttackSound(player));

				/** CUSTOM ON DAMAGE STUFF **/
				if (victim.isNpc()) {

				} else {
					Sounds.sendSound((Player) victim, Sounds
							.getPlayerBlockSounds(((Player) victim).getEquipment().get(Equipment.WEAPON_SLOT).getId()));
				}
			}
		}

		public boolean shouldRetaliate() {
			if (victim.isPlayer()) {
				if (attacker.isNpc()) {
					if (!((NPC) attacker).getDefinition().isAttackable()) {
						return false;
					}
				}
				return victim.isPlayer() && ((Player) victim).isAutoRetaliate() && !victim.getMovementQueue().isMoving()
						&& ((Player) victim).getWalkToTask() == null;
			} else if (!(attacker.isNpc() && ((NPC) attacker).isSummoningNpc())) {
				NPC npc = (NPC) victim;
				return npc.getMovementCoordinator().getCoordinateState() == CoordinateState.HOME;
			}
			return false;
		}

		public void retaliate() {
			if (victim.isPlayer()) {
				victim.getCombatBuilder().setDidAutoRetaliate(true);
				victim.getCombatBuilder().attack(attacker);
			} else if (victim.isNpc()) {
				NPC npc = (NPC) victim;
				npc.getCombatBuilder().attack(attacker);
				npc.setFindNewTarget(false);
			}
		}



		private boolean initialRun() {
			return this.delay == 0;
		}
	}
}
