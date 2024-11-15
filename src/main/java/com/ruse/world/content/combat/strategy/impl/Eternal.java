package com.ruse.world.content.combat.strategy.impl;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.*;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import java.util.List;

public class Eternal implements CombatStrategy {

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
		NPC eternal = (NPC) entity;
		if (eternal.isChargingAttack() || victim.getConstitution() <= 0) {
			return true;
		}

		List<Player> players = World.getNearbyPlayers(eternal.getPosition(), 10);
		if (players.isEmpty()) {
			return true;
		}
		victim = players.get(Misc.getRandom(players.size() - 1));

		if (Misc.getRandom(15) <= 2) {
			int hitAmount = 2;
			eternal.performGraphic(new Graphic(2549));
			eternal.setConstitution(eternal.getConstitution() + hitAmount);
			eternal.getCombatBuilder().setContainer(new CombatContainer(eternal, victim, 1, 3, CombatType.MAGIC, true));
		}
		if (Locations.goodDistance(eternal.getPosition().copy(), victim.getPosition().copy(), 3)
				&& Misc.getRandom(5) <= 3) {
			eternal.performAnimation(new Animation(eternal.getDefinition().getAttackAnim()));
			// Eternal.performAnimation(new Animation(5026));
			eternal.getCombatBuilder().setContainer(new CombatContainer(eternal, victim, 1, 1, CombatType.MELEE, true));

		} else {
			eternal.setChargingAttack(true);
			eternal.performAnimation(new Animation(5026));
			eternal.getCombatBuilder().setContainer(new CombatContainer(eternal, victim, 1, 3, CombatType.MAGIC, true));
			Character finalVictim = victim;
			TaskManager.submit(new Task(1, eternal, false) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick == 0) {
						new Projectile(eternal, finalVictim, 1194, 44, 3, 41, 31, 0).sendProjectile();
					} else if (tick == 1) {
						eternal.setChargingAttack(false);
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
		return 3;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
