package com.ruse.world.content.combat.strategy.impl;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.*;
import com.ruse.util.Misc;
import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import java.util.List;

public class ChampRaids implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC vetion = (NPC) entity;
		if (vetion.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}
		if (Locations.goodDistance(vetion.getPosition().copy(), victim.getPosition().copy(), 3)
				&& Misc.getRandom(5) <= 3) {
			vetion.performAnimation(new Animation(14221));
			vetion.getCombatBuilder().setContainer(new CombatContainer(vetion, victim, 3, 3, CombatType.MAGIC, true));
		} else {
			vetion.setChargingAttack(true);
			vetion.performAnimation(new Animation(vetion.getDefinition().getAttackAnimation()));

			final Position start1 = new Position(vetion.getPosition().getX(), vetion.getPosition().getY());


			final Position area1 = new Position(start1.getX() + Misc.getRandom(2), start1.getY() + Misc.getRandom(2));

			final Position area2 = new Position(start1.getX() + Misc.getRandom(3), start1.getY() + Misc.getRandom(2));
			final Position area3 = new Position(start1.getX() + Misc.getRandom(4), start1.getY() + Misc.getRandom(2));
			final Position area4 = new Position(start1.getX() + Misc.getRandom(5), start1.getY() + Misc.getRandom(2));
			final Position area5 = new Position(start1.getX() + Misc.getRandom(6), start1.getY() + Misc.getRandom(2));
			final Position area6 = new Position(start1.getX() + Misc.getRandom(7), start1.getY() + Misc.getRandom(2));
			final Position area7 = new Position(start1.getX() + Misc.getRandom(8), start1.getY() + Misc.getRandom(2));
			final Position area8 = new Position(start1.getX() + Misc.getRandom(9), start1.getY() + Misc.getRandom(2));
			final Position area9 = new Position(start1.getX() + Misc.getRandom(10), start1.getY() + Misc.getRandom(2));

			final Position area10 = new Position(start1.getX() + Misc.getRandom(2), start1.getY() + Misc.getRandom(3));
			final Position area11 = new Position(start1.getX() + Misc.getRandom(2), start1.getY() + Misc.getRandom(4));
			final Position area12 = new Position(start1.getX() + Misc.getRandom(2), start1.getY() + Misc.getRandom(5));
			final Position area13 = new Position(start1.getX() + Misc.getRandom(2), start1.getY() + Misc.getRandom(6));
			final Position area14 = new Position(start1.getX() + Misc.getRandom(2), start1.getY() + Misc.getRandom(7));
			final Position area15 = new Position(start1.getX() + Misc.getRandom(2), start1.getY() + Misc.getRandom(8));
			final Position area16 = new Position(start1.getX() + Misc.getRandom(2), start1.getY() + Misc.getRandom(9));
			final Position area17 = new Position(start1.getX() + Misc.getRandom(2), start1.getY() + Misc.getRandom(10));

			final Position area18 = new Position(start1.getX() + Misc.getRandom(-2), start1.getY() + Misc.getRandom(-2));

			final Position area19 = new Position(start1.getX() + Misc.getRandom(-3), start1.getY() + Misc.getRandom(-2));
			final Position area20 = new Position(start1.getX() + Misc.getRandom(-4), start1.getY() + Misc.getRandom(-2));
			final Position area21 = new Position(start1.getX() + Misc.getRandom(-5), start1.getY() + Misc.getRandom(-2));
			final Position area22 = new Position(start1.getX() + Misc.getRandom(-6), start1.getY() + Misc.getRandom(-2));
			final Position area23 = new Position(start1.getX() + Misc.getRandom(-7), start1.getY() + Misc.getRandom(-2));
			final Position area24 = new Position(start1.getX() + Misc.getRandom(-8), start1.getY() + Misc.getRandom(-2));
			final Position area25 = new Position(start1.getX() + Misc.getRandom(-9), start1.getY() + Misc.getRandom(-2));
			final Position area26 = new Position(start1.getX() + Misc.getRandom(-10), start1.getY() + Misc.getRandom(-2));

			final Position area27 = new Position(start1.getX() + Misc.getRandom(-2), start1.getY() + Misc.getRandom(-3));
			final Position area28 = new Position(start1.getX() + Misc.getRandom(-2), start1.getY() + Misc.getRandom(-4));
			final Position area29 = new Position(start1.getX() + Misc.getRandom(-2), start1.getY() + Misc.getRandom(-5));
			final Position area30 = new Position(start1.getX() + Misc.getRandom(-2), start1.getY() + Misc.getRandom(-6));
			final Position area31 = new Position(start1.getX() + Misc.getRandom(-2), start1.getY() + Misc.getRandom(-7));
			final Position area32 = new Position(start1.getX() + Misc.getRandom(-2), start1.getY() + Misc.getRandom(-8));
			final Position area33 = new Position(start1.getX() + Misc.getRandom(-2), start1.getY() + Misc.getRandom(-9));
			final Position area34 = new Position(start1.getX() + Misc.getRandom(-2), start1.getY() + Misc.getRandom(-10));


			final Player p = (Player) victim;
			final List<Player> list = Misc.getCombinedPlayerList(p);

			TaskManager.submit(new Task(1, vetion, false) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick == 0) {
						vetion.forceChat("Prepare yourself..");
						vetion.performAnimation(new Animation(5633));
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area1);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area2);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area3);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area4);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area5);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area6);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area7);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area7);

						for (Player t : list) {
							if (t == null)
								continue;
							if (t.getPosition().equals(area1) || t.getPosition().equals(area2) || t.getPosition().equals(area3)
									|| t.getPosition().equals(area4) || t.getPosition().equals(area5) || t.getPosition().equals(area6)
									|| t.getPosition().equals(area7) || t.getPosition().equals(area8) || t.getPosition().equals(area9)
									|| t.getPosition().equals(area10) || t.getPosition().equals(area11) || t.getPosition().equals(area12)
									|| t.getPosition().equals(area13) || t.getPosition().equals(area14) || t.getPosition().equals(area15)
									|| t.getPosition().equals(area16) || t.getPosition().equals(area17) || t.getPosition().equals(area18)
									|| t.getPosition().equals(area19) || t.getPosition().equals(area20) || t.getPosition().equals(area21)
									|| t.getPosition().equals(area22) || t.getPosition().equals(area23) || t.getPosition().equals(area24)
									|| t.getPosition().equals(area25) || t.getPosition().equals(area26) || t.getPosition().equals(area27)
									|| t.getPosition().equals(area28) || t.getPosition().equals(area29) || t.getPosition().equals(area30)
									|| t.getPosition().equals(area31) || t.getPosition().equals(area32) || t.getPosition().equals(area33)
									|| t.getPosition().equals(area34)) {
								t.dealDamage(new Hit(125, Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
							}
						}

					}
					else if (tick == 1) {
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area8);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area9);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area10);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area11);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area12);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area13);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area14);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area15);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area16);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area17);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area18);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area19);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area20);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area21);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area22);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area23);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area24);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area25);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area26);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area27);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area28);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area29);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area30);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area31);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area32);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area33);
						p.getPacketSender().sendGlobalGraphic(new Graphic(281), area34);

						for (Player t : list) {
							if (t == null)
								continue;
							if (t.getPosition().equals(area1) || t.getPosition().equals(area2) || t.getPosition().equals(area3)
									|| t.getPosition().equals(area4) || t.getPosition().equals(area5) || t.getPosition().equals(area6)
									|| t.getPosition().equals(area7) || t.getPosition().equals(area8) || t.getPosition().equals(area9)
									|| t.getPosition().equals(area10) || t.getPosition().equals(area11) || t.getPosition().equals(area12)
									|| t.getPosition().equals(area13) || t.getPosition().equals(area14) || t.getPosition().equals(area15)
									|| t.getPosition().equals(area16) || t.getPosition().equals(area17) || t.getPosition().equals(area18)
									|| t.getPosition().equals(area19) || t.getPosition().equals(area20) || t.getPosition().equals(area21)
									|| t.getPosition().equals(area22) || t.getPosition().equals(area23) || t.getPosition().equals(area24)
									|| t.getPosition().equals(area25) || t.getPosition().equals(area26) || t.getPosition().equals(area27)
									|| t.getPosition().equals(area28) || t.getPosition().equals(area29) || t.getPosition().equals(area30)
									|| t.getPosition().equals(area31) || t.getPosition().equals(area32) || t.getPosition().equals(area33)
									|| t.getPosition().equals(area34)) {
								t.dealDamage(new Hit(125, Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
							}
						}
					}
					else if (tick == 2) {
						for (Player t : list) {
							if (t == null)
								continue;
							if (t.getPosition().equals(area1) || t.getPosition().equals(area2) || t.getPosition().equals(area3)
									|| t.getPosition().equals(area4) || t.getPosition().equals(area5) || t.getPosition().equals(area6)
									|| t.getPosition().equals(area7) || t.getPosition().equals(area8) || t.getPosition().equals(area9)
									|| t.getPosition().equals(area10) || t.getPosition().equals(area11) || t.getPosition().equals(area12)
									|| t.getPosition().equals(area13) || t.getPosition().equals(area14) || t.getPosition().equals(area15)
									|| t.getPosition().equals(area16) || t.getPosition().equals(area17) || t.getPosition().equals(area18)
									|| t.getPosition().equals(area19) || t.getPosition().equals(area20) || t.getPosition().equals(area21)
									|| t.getPosition().equals(area22) || t.getPosition().equals(area23) || t.getPosition().equals(area24)
									|| t.getPosition().equals(area25) || t.getPosition().equals(area26) || t.getPosition().equals(area27)
									|| t.getPosition().equals(area28) || t.getPosition().equals(area29) || t.getPosition().equals(area30)
									|| t.getPosition().equals(area31) || t.getPosition().equals(area32) || t.getPosition().equals(area33)
									|| t.getPosition().equals(area34)) {
								t.dealDamage(new Hit(125, Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
							}
						}
					}
					else if (tick == 3) {
						for (Player t : list) {
							if (t == null)
								continue;
							if (t.getPosition().equals(area1) || t.getPosition().equals(area2) || t.getPosition().equals(area3)
									|| t.getPosition().equals(area4) || t.getPosition().equals(area5) || t.getPosition().equals(area6)
									|| t.getPosition().equals(area7) || t.getPosition().equals(area8) || t.getPosition().equals(area9)
									|| t.getPosition().equals(area10) || t.getPosition().equals(area11) || t.getPosition().equals(area12)
									|| t.getPosition().equals(area13) || t.getPosition().equals(area14) || t.getPosition().equals(area15)
									|| t.getPosition().equals(area16) || t.getPosition().equals(area17) || t.getPosition().equals(area18)
									|| t.getPosition().equals(area19) || t.getPosition().equals(area20) || t.getPosition().equals(area21)
									|| t.getPosition().equals(area22) || t.getPosition().equals(area23) || t.getPosition().equals(area24)
									|| t.getPosition().equals(area25) || t.getPosition().equals(area26) || t.getPosition().equals(area27)
									|| t.getPosition().equals(area28) || t.getPosition().equals(area29) || t.getPosition().equals(area30)
									|| t.getPosition().equals(area31) || t.getPosition().equals(area32) || t.getPosition().equals(area33)
									|| t.getPosition().equals(area34)) {
								t.dealDamage(new Hit(125, Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
							}
						}
					}
					else if (tick == 4) {
						for (Player t : list) {
							if (t == null)
								continue;
							if (t.getPosition().equals(area1) || t.getPosition().equals(area2) || t.getPosition().equals(area3)
									|| t.getPosition().equals(area4) || t.getPosition().equals(area5) || t.getPosition().equals(area6)
									|| t.getPosition().equals(area7) || t.getPosition().equals(area8) || t.getPosition().equals(area9)
									|| t.getPosition().equals(area10) || t.getPosition().equals(area11) || t.getPosition().equals(area12)
									|| t.getPosition().equals(area13) || t.getPosition().equals(area14) || t.getPosition().equals(area15)
									|| t.getPosition().equals(area16) || t.getPosition().equals(area17) || t.getPosition().equals(area18)
									|| t.getPosition().equals(area19) || t.getPosition().equals(area20) || t.getPosition().equals(area21)
									|| t.getPosition().equals(area22) || t.getPosition().equals(area23) || t.getPosition().equals(area24)
									|| t.getPosition().equals(area25) || t.getPosition().equals(area26) || t.getPosition().equals(area27)
									|| t.getPosition().equals(area28) || t.getPosition().equals(area29) || t.getPosition().equals(area30)
									|| t.getPosition().equals(area31) || t.getPosition().equals(area32) || t.getPosition().equals(area33)
									|| t.getPosition().equals(area34)) {
								t.dealDamage(new Hit(125, Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
							}
						}
					}
					else if (tick == 5) {
						for (Player t : list) {
							if (t == null)
								continue;
							if (t.getPosition().equals(area1) || t.getPosition().equals(area2) || t.getPosition().equals(area3)
									|| t.getPosition().equals(area4) || t.getPosition().equals(area5) || t.getPosition().equals(area6)
									|| t.getPosition().equals(area7) || t.getPosition().equals(area8) || t.getPosition().equals(area9)
									|| t.getPosition().equals(area10) || t.getPosition().equals(area11) || t.getPosition().equals(area12)
									|| t.getPosition().equals(area13) || t.getPosition().equals(area14) || t.getPosition().equals(area15)
									|| t.getPosition().equals(area16) || t.getPosition().equals(area17) || t.getPosition().equals(area18)
									|| t.getPosition().equals(area19) || t.getPosition().equals(area20) || t.getPosition().equals(area21)
									|| t.getPosition().equals(area22) || t.getPosition().equals(area23) || t.getPosition().equals(area24)
									|| t.getPosition().equals(area25) || t.getPosition().equals(area26) || t.getPosition().equals(area27)
									|| t.getPosition().equals(area28) || t.getPosition().equals(area29) || t.getPosition().equals(area30)
									|| t.getPosition().equals(area31) || t.getPosition().equals(area32) || t.getPosition().equals(area33)
									|| t.getPosition().equals(area34)) {
								t.dealDamage(new Hit(125, Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
							}
						}
					}
					else if (tick == 6) {
						for (Player t : list) {
							if (t == null)
								continue;
							if (t.getPosition().equals(area1) || t.getPosition().equals(area2) || t.getPosition().equals(area3)
									|| t.getPosition().equals(area4) || t.getPosition().equals(area5) || t.getPosition().equals(area6)
									|| t.getPosition().equals(area7) || t.getPosition().equals(area8) || t.getPosition().equals(area9)
									|| t.getPosition().equals(area10) || t.getPosition().equals(area11) || t.getPosition().equals(area12)
									|| t.getPosition().equals(area13) || t.getPosition().equals(area14) || t.getPosition().equals(area15)
									|| t.getPosition().equals(area16) || t.getPosition().equals(area17) || t.getPosition().equals(area18)
									|| t.getPosition().equals(area19) || t.getPosition().equals(area20) || t.getPosition().equals(area21)
									|| t.getPosition().equals(area22) || t.getPosition().equals(area23) || t.getPosition().equals(area24)
									|| t.getPosition().equals(area25) || t.getPosition().equals(area26) || t.getPosition().equals(area27)
									|| t.getPosition().equals(area28) || t.getPosition().equals(area29) || t.getPosition().equals(area30)
									|| t.getPosition().equals(area31) || t.getPosition().equals(area32) || t.getPosition().equals(area33)
									|| t.getPosition().equals(area34)) {
								t.dealDamage(new Hit(125, Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
							}
						}
					}
					else if (tick == 7) {
						for (Player t : list) {
							if (t == null)
								continue;
							if (t.getPosition().equals(area1) || t.getPosition().equals(area2) || t.getPosition().equals(area3)
									|| t.getPosition().equals(area4) || t.getPosition().equals(area5) || t.getPosition().equals(area6)
									|| t.getPosition().equals(area7) || t.getPosition().equals(area8) || t.getPosition().equals(area9)
									|| t.getPosition().equals(area10) || t.getPosition().equals(area11) || t.getPosition().equals(area12)
									|| t.getPosition().equals(area13) || t.getPosition().equals(area14) || t.getPosition().equals(area15)
									|| t.getPosition().equals(area16) || t.getPosition().equals(area17) || t.getPosition().equals(area18)
									|| t.getPosition().equals(area19) || t.getPosition().equals(area20) || t.getPosition().equals(area21)
									|| t.getPosition().equals(area22) || t.getPosition().equals(area23) || t.getPosition().equals(area24)
									|| t.getPosition().equals(area25) || t.getPosition().equals(area26) || t.getPosition().equals(area27)
									|| t.getPosition().equals(area28) || t.getPosition().equals(area29) || t.getPosition().equals(area30)
									|| t.getPosition().equals(area31) || t.getPosition().equals(area32) || t.getPosition().equals(area33)
									|| t.getPosition().equals(area34)) {
								t.dealDamage(new Hit(125, Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
							}
						}
					}
					else if (tick == 8) {
						for (Player t : list) {
							if (t == null)
								continue;
							if (t.getPosition().equals(area1) || t.getPosition().equals(area2) || t.getPosition().equals(area3)
									|| t.getPosition().equals(area4) || t.getPosition().equals(area5) || t.getPosition().equals(area6)
									|| t.getPosition().equals(area7) || t.getPosition().equals(area8) || t.getPosition().equals(area9)
									|| t.getPosition().equals(area10) || t.getPosition().equals(area11) || t.getPosition().equals(area12)
									|| t.getPosition().equals(area13) || t.getPosition().equals(area14) || t.getPosition().equals(area15)
									|| t.getPosition().equals(area16) || t.getPosition().equals(area17) || t.getPosition().equals(area18)
									|| t.getPosition().equals(area19) || t.getPosition().equals(area20) || t.getPosition().equals(area21)
									|| t.getPosition().equals(area22) || t.getPosition().equals(area23) || t.getPosition().equals(area24)
									|| t.getPosition().equals(area25) || t.getPosition().equals(area26) || t.getPosition().equals(area27)
									|| t.getPosition().equals(area28) || t.getPosition().equals(area29) || t.getPosition().equals(area30)
									|| t.getPosition().equals(area31) || t.getPosition().equals(area32) || t.getPosition().equals(area33)
									|| t.getPosition().equals(area34)) {
								t.dealDamage(new Hit(125, Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
							}
						}
					}
					else if (tick == 9) {
						for (Player t : list) {
							if (t == null)
								continue;
							if (t.getPosition().equals(area1) || t.getPosition().equals(area2) || t.getPosition().equals(area3)
									|| t.getPosition().equals(area4) || t.getPosition().equals(area5) || t.getPosition().equals(area6)
									|| t.getPosition().equals(area7) || t.getPosition().equals(area8) || t.getPosition().equals(area9)
									|| t.getPosition().equals(area10) || t.getPosition().equals(area11) || t.getPosition().equals(area12)
									|| t.getPosition().equals(area13) || t.getPosition().equals(area14) || t.getPosition().equals(area15)
									|| t.getPosition().equals(area16) || t.getPosition().equals(area17) || t.getPosition().equals(area18)
									|| t.getPosition().equals(area19) || t.getPosition().equals(area20) || t.getPosition().equals(area21)
									|| t.getPosition().equals(area22) || t.getPosition().equals(area23) || t.getPosition().equals(area24)
									|| t.getPosition().equals(area25) || t.getPosition().equals(area26) || t.getPosition().equals(area27)
									|| t.getPosition().equals(area28) || t.getPosition().equals(area29) || t.getPosition().equals(area30)
									|| t.getPosition().equals(area31) || t.getPosition().equals(area32) || t.getPosition().equals(area33)
									|| t.getPosition().equals(area34)) {
								t.dealDamage(new Hit(125, Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
							}
						}
					}
					else if (tick == 10) {
						for (Player t : list) {
							if (t == null)
								continue;
							if (t.getPosition().equals(area1) || t.getPosition().equals(area2) || t.getPosition().equals(area3)
									|| t.getPosition().equals(area4) || t.getPosition().equals(area5) || t.getPosition().equals(area6)
									|| t.getPosition().equals(area7) || t.getPosition().equals(area8) || t.getPosition().equals(area9)
									|| t.getPosition().equals(area10) || t.getPosition().equals(area11) || t.getPosition().equals(area12)
									|| t.getPosition().equals(area13) || t.getPosition().equals(area14) || t.getPosition().equals(area15)
									|| t.getPosition().equals(area16) || t.getPosition().equals(area17) || t.getPosition().equals(area18)
									|| t.getPosition().equals(area19) || t.getPosition().equals(area20) || t.getPosition().equals(area21)
									|| t.getPosition().equals(area22) || t.getPosition().equals(area23) || t.getPosition().equals(area24)
									|| t.getPosition().equals(area25) || t.getPosition().equals(area26) || t.getPosition().equals(area27)
									|| t.getPosition().equals(area28) || t.getPosition().equals(area29) || t.getPosition().equals(area30)
									|| t.getPosition().equals(area31) || t.getPosition().equals(area32) || t.getPosition().equals(area33)
									|| t.getPosition().equals(area34)) {
								t.dealDamage(new Hit(125, Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
							}
						}
					}
					else if (tick == 11) {

						vetion.setChargingAttack(false);
						stop();
					}
					tick++;
				}
			});
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 4;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
