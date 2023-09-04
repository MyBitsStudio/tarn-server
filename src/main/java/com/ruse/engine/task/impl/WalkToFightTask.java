package com.ruse.engine.task.impl;

import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

/**
 * Represents a movement action for a game character.
 * 
 * @author Gabriel Hannason
 */

public class WalkToFightTask {

	public interface FinalizedMovementTask {
		void execute();
	}

	/**
	 * The WalkToTask constructor.
	 *
	 * @param entity        The associated game character.
	 * @param destination   The destination the game character will move to.
	 * @param finalizedTask The task a player must execute upon reaching said
	 *                      destination.
	 */
	public WalkToFightTask(Player entity, NPC target, Position destination, FinalizedMovementTask finalizedTask) {
		this.player = entity;
		CombatStrategy strategy = player.getCombatBuilder().getStrategy();
		this.distance = strategy.attackDistance(target);
		this.destination = destination;
		this.finalizedTask = finalizedTask;
	}

	private final int distance;

	/**
	 * The associated game character.
	 */
	private final Player player;

	/**
	 * The destination the game character will move to.
	 */
	private final Position destination;

	/**
	 * The task a player must execute upon reaching said destination.
	 */
	private final FinalizedMovementTask finalizedTask;

	/**
	 * Executes the action if distance is correct
	 */
	public void tick() {
		if (player == null)
			return;
		if (!player.isRegistered()) {
			player.setWalkToTask(null);
			return;
		}
		if (player.isTeleporting() || player.getConstitution() <= 0 || destination == null) {
			player.setWalkToTask(null);
			return;
		}

		if (Locations.goodDistance(player.getPosition().getX(), player.getPosition().getY(), destination.getX(),
				destination.getY(), distance) || destination.equals(player.getPosition())) {
			finalizedTask.execute();
			player.setEntityInteraction(null);
			player.setWalkToTask(null);
		}
	}
}
