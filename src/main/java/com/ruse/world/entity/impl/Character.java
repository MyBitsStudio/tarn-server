package com.ruse.world.entity.impl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Animation;
import com.ruse.model.Direction;
import com.ruse.model.Flag;
import com.ruse.model.Graphic;
import com.ruse.model.Hit;
import com.ruse.model.Locations.Location;
import com.ruse.model.Position;
import com.ruse.model.RegionInstance;
import com.ruse.model.UpdateFlag;
import com.ruse.model.movement.MovementQueue;
import com.ruse.util.Stopwatch;
import com.ruse.world.content.combat.CombatBuilder;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.magic.CombatSpell;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.entity.Entity;
import com.ruse.world.entity.impl.mini.MiniPlayer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.route.RouteFinder;
import com.ruse.world.entity.impl.player.route.strategy.EntityStrategy;
import com.ruse.world.entity.impl.player.route.strategy.FixedTileStrategy;
import lombok.Getter;
import lombok.Setter;

/**
 * A player or NPC
 * 
 * @author Gabriel Hannason
 */

public abstract class Character extends Entity {


	/**REGION**/
	/**
	 * The current region id(s) that are in the viewport.
	 */
	@Getter
	protected transient final List<Integer> regionIds = new CopyOnWriteArrayList<>();

	/**
	 * The last region id the player was in.
	 */
	@Getter
	@Setter
	protected transient int currentRegionId = -1;

	/**
	 * The position when the map was last updated, for players, it is considered the center of the scene.
	 * <br>This was moved from the Player to save performance when checking for region npcs, keep it here.
	 */
	@Getter
	@Setter
	private Position currentMapCenter;

	@Getter
	@Setter
	private boolean forceMultiArea;

	public void checkMap() {
		if (currentMapCenter != null && position.withinScene(currentMapCenter)) {
			return;
		}
		if (isPlayer()) {
			asPlayer().getControllerManager().moved();
		}
		loadMap();
	}

	public abstract void loadMap();

	public abstract void removeFromMap();


	public Character(Position position) {
		super(position);
		location = Location.getLocation(this);
	}

	/*
	 * Fields
	 */

	/*** STRINGS ***/
	private String forcedChat;

	/*** LONGS **/

	/*** INSTANCES ***/
	private Direction direction, primaryDirection = Direction.NONE, secondaryDirection = Direction.NONE,
			lastDirection = Direction.NONE;
	private CombatBuilder combatBuilder = new CombatBuilder(this);
	private MovementQueue movementQueue = new MovementQueue(this);
	private Stopwatch lastCombat = new Stopwatch();
	private UpdateFlag updateFlag = new UpdateFlag();
	private Location location;
	private Position positionToFace;
	private Animation animation;
	private Graphic graphic;
	private Entity interactingEntity;
	public Position singlePlayerPositionFacing;
	private CombatSpell currentlyCasting;
	private Hit primaryHit;
	private Hit secondaryHit;
	private RegionInstance regionInstance;
	private Instance instance;

	private String instanceId = "";

	/*** INTS ***/
	private int npcTransformationId;
	private int poisonDamage;
	private int freezeDelay;
	private int stunDelay;

	/*** BOOLEANS ***/
	private boolean[] prayerActive = new boolean[34], curseActive = new boolean[20];
	private boolean registered;
	private boolean teleporting;
	private boolean resetMovementQueue;
	private boolean needsPlacement;

	/*** ABSTRACT METHODS ***/
	public abstract Character setConstitution(long constitution);

	public abstract CombatStrategy determineStrategy();

	public abstract void appendDeath();

	public abstract void heal(long damage);

	public abstract void poisonVictim(Character victim, CombatType type);

	public abstract long getConstitution();

	public abstract int getBaseAttack(CombatType type);

	public abstract int getBaseDefence(CombatType type);

	public abstract int getAttackSpeed();

	/*
	 * Getters and setters Also contains methods.
	 */

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Graphic getGraphic() {
		return graphic;
	}

	public Character setGraphic(Graphic graphic) {
		this.graphic = graphic;
		getUpdateFlag().flag(Flag.GRAPHIC);
		return this;
	}

	public Animation getAnimation() {
		return animation;
	}

	public Character setAnimation(Animation animation) {
		this.animation = animation;
		getUpdateFlag().flag(Flag.ANIMATION);
		return this;
	}

	public Character copy() {
		return this;
	}


	/**
	 * Deals one damage to this entity.
	 * 
	 * @param hit the damage to be dealt.
	 */
	public void dealDamage(Hit hit) {
		if (isPlayer()) {
			asPlayer().getControllerManager().processIncommingHit(hit, this);
		}
		if (getUpdateFlag().flagged(Flag.SINGLE_HIT)) {
			dealSecondaryDamage(hit);
			return;
		}
		if (getConstitution() <= 0)
			return;
		primaryHit = decrementHealth(hit);
		getUpdateFlag().flag(Flag.SINGLE_HIT);
	}

	public Hit decrementHealth(Hit hit) {
		if (isNpc() && (toNpc().getId() == 8028 || toNpc().getId() ==  8022)) {
			return hit;
		}
		if (getConstitution() <= 0)
			return hit;
		if (hit.getDamage() > getConstitution())
			hit.setDamage(getConstitution());
		if (hit.getDamage() < 0)
			hit.setDamage(0);
		long outcome = getConstitution() - hit.getDamage();
		if (outcome < 0)
			outcome = 0;
		setConstitution(outcome);
		return hit;
	}

	/**
	 * Deal secondary damage to this entity.
	 * 
	 * @param hit the damage to be dealt.
	 */
	private void dealSecondaryDamage(Hit hit) {
		secondaryHit = decrementHealth(hit);
		getUpdateFlag().flag(Flag.DOUBLE_HIT);
	}

	/**
	 * Deals two damage splats to this entity.
	 * 
	 * @param hit       the first hit.
	 * @param secondHit the second hit.
	 */
	public void dealDoubleDamage(Hit hit, Hit secondHit) {
		dealDamage(hit);
		dealSecondaryDamage(secondHit);
	}

	/**
	 * Deals three damage splats to this entity.
	 * 
	 * @param hit       the first hit.
	 * @param secondHit the second hit.
	 * @param thirdHit  the third hit.
	 */
	public void dealTripleDamage(Hit hit, Hit secondHit, final Hit thirdHit) {
		dealDoubleDamage(hit, secondHit);

		TaskManager.submit(new Task(1, this, false) {
			@Override
			public void execute() {
				if (!registered) {
					this.stop();
					return;
				}
				dealDamage(thirdHit);
				this.stop();
			}
		});
	}

	/**
	 * Deals four damage splats to this entity.
	 * 
	 * @param hit       the first hit.
	 * @param secondHit the second hit.
	 * @param thirdHit  the third hit.
	 * @param fourthHit the fourth hit.
	 */
	public void dealQuadrupleDamage(Hit hit, Hit secondHit, final Hit thirdHit, final Hit fourthHit) {
		dealDoubleDamage(hit, secondHit);

		TaskManager.submit(new Task(1, this, false) {
			@Override
			public void execute() {
				if (!registered) {
					this.stop();
					return;
				}
				dealDoubleDamage(thirdHit, fourthHit);
				this.stop();
			}
		});
	}

	/**
	 * Get the primary hit for this entity.
	 * 
	 * @return the primaryHit.
	 */
	public Hit getPrimaryHit() {
		return primaryHit;
	}

	/**
	 * Get the secondary hit for this entity.
	 * 
	 * @return the secondaryHit.
	 */
	public Hit getSecondaryHit() {
		return secondaryHit;
	}

	/**
	 * Prepares to cast the argued spell on the argued victim.
	 * 
	 * @param spell  the spell to cast.
	 * @param victim the victim to cast the spell on.
	 */
	public void prepareSpell(CombatSpell spell, Character victim) {
		currentlyCasting = spell;
		currentlyCasting.startCast(this, victim);
	}

	/**
	 * Gets if this entity is registered.
	 * 
	 * @return the unregistered.
	 */
	public boolean isRegistered() {
		return registered;
	}

	/**
	 * Sets if this entity is registered,
	 * 
	 * @param unregistered the unregistered to set.
	 */
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	/**
	 * Gets the combat session.
	 * 
	 * @return the combat session.
	 */
	public CombatBuilder getCombatBuilder() {
		return combatBuilder;
	}

	/**
	 * @return the lastCombat
	 */
	public Stopwatch getLastCombat() {
		return lastCombat;
	}

	public int getAndDecrementPoisonDamage() {
		return poisonDamage -= 15;
	}

	public int getPoisonDamage() {
		return poisonDamage;
	}

	public void setPoisonDamage(int poisonDamage) {
		this.poisonDamage = poisonDamage;
	}

	public boolean isPoisoned() {
		if (poisonDamage < 0)
			poisonDamage = 0;
		return poisonDamage != 0;
	}

	public Position getPositionToFace() {
		return positionToFace;
	}

	public Character setPositionToFace(Position positionToFace) {
		this.positionToFace = positionToFace;
		getUpdateFlag().flag(Flag.FACE_POSITION);
		return this;
	}

	public Character moveTo(Position teleportTarget) {
		getMovementQueue().reset();
		super.setPosition(teleportTarget.copy());
		setNeedsPlacement(true);
		setResetMovementQueue(true);
		setTeleporting(true);
		checkMap();

		return this;
	}

	private boolean moving;

	public void delayedMoveTo(final Position teleportTarget, final int delay) {
		if (moving)
			return;
		moving = true;
		TaskManager.submit(new Task(delay, this, false) {
			@Override
			protected void execute() {
				moveTo(teleportTarget);
				stop();
			}

			@Override
			public void stop() {
				setEventRunning(false);
				moving = false;
			}
		});
	}

	public UpdateFlag getUpdateFlag() {
		return updateFlag;
	}

	public Character setMovementQueue(MovementQueue movementQueue) {
		this.movementQueue = movementQueue;
		return this;
	}

	public MovementQueue getMovementQueue() {
		return movementQueue;
	}

	public Character forceChat(String message) {
		setForcedChat(message);
		getUpdateFlag().flag(Flag.FORCED_CHAT);
		return this;
	}

	public Character setEntityInteraction(Entity entity) {
		if(this.isNpc() && (((NPC)this).getId() == 550 || ((NPC)this).getId() == 551)) return this;
		this.interactingEntity = entity;
		getUpdateFlag().flag(Flag.ENTITY_INTERACTION);
		return this;
	}

	public Entity getInteractingEntity() {
		return interactingEntity;
	}

	@Override
	public void performAnimation(Animation animation) {
		if (animation == null)
			return;
		if(isPlayer()) {
            Player player = (Player) this;
            MiniPlayer miniPlayer = null;
            if (player.getMiniPManager().getMiniPlayer() != null) {
                miniPlayer = player.getMiniPManager().getMiniPlayer();
                miniPlayer.setAnimation(animation);
            }
        }
        setAnimation(animation);
	}

	@Override
	public void performGraphic(Graphic graphic) {
		if (graphic == null)
			return;
		if(isPlayer()) {
            Player player = (Player) this;
            MiniPlayer miniPlayer = null;
            if (player.getMiniPManager().getMiniPlayer() != null) {
                miniPlayer = player.getMiniPManager().getMiniPlayer();
                miniPlayer.setGraphic(graphic);
            }
        }
        setGraphic(graphic);
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
		int[] directionDeltas = direction.getDirectionDelta();
		setPositionToFace(getPosition().copy().add(directionDeltas[0], directionDeltas[1]));
	}

	/**
	 * Sets the value for {@link CharacterNode#secondaryDirection}.
	 *
	 * @param secondaryDirection the new value to set.
	 */
	public final void setSecondaryDirection(Direction secondaryDirection) {
		this.secondaryDirection = secondaryDirection;
	}

	/**
	 * Gets the last direction this character was facing.
	 *
	 * @return the last direction.
	 */
	public final Direction getLastDirection() {
		return lastDirection;
	}

	/**
	 * Sets the value for {@link CharacterNode#lastDirection}.
	 *
	 * @param lastDirection the new value to set.
	 */
	public final void setLastDirection(Direction lastDirection) {
		this.lastDirection = lastDirection;
	}

	public boolean isTeleporting() {
		return this.teleporting;
	}

	public Character setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
		return this;
	}

	public String getForcedChat() {
		return forcedChat;
	}

	public Character setForcedChat(String forcedChat) {
		this.forcedChat = forcedChat;
		return this;
	}

	public boolean[] getPrayerActive() {
		return prayerActive;
	}

	public boolean[] getCurseActive() {
		return curseActive;
	}

	public Character setPrayerActive(boolean[] prayerActive) {
		this.prayerActive = prayerActive;
		return this;
	}

	public Character setPrayerActive(int id, boolean prayerActive) {
		this.prayerActive[id] = prayerActive;
		return this;
	}

	public Character setCurseActive(boolean[] curseActive) {
		this.curseActive = curseActive;
		return this;
	}

	public Character setCurseActive(int id, boolean curseActive) {
		this.curseActive[id] = curseActive;
		return this;
	}

	public int getNpcTransformationId() {
		return npcTransformationId;
	}

	public Character setNpcTransformationId(int npcTransformationId) {
		this.npcTransformationId = npcTransformationId;
		return this;
	}

	/*
	 * Movement queue
	 */

	public void setPrimaryDirection(Direction primaryDirection) {
		this.primaryDirection = primaryDirection;
	}

	public Direction getPrimaryDirection() {
		return primaryDirection;
	}

	public Direction getSecondaryDirection() {
		return secondaryDirection;
	}

	public CombatSpell getCurrentlyCasting() {
		return currentlyCasting;
	}

	public void setCurrentlyCasting(CombatSpell currentlyCasting) {
		this.currentlyCasting = currentlyCasting;
	}

	public int getFreezeDelay() {
		return freezeDelay;
	}

	public void setFreezeDelay(int freezeDelay) {
		this.freezeDelay = freezeDelay;
	}

	public int decrementAndGetFreezeDelay() {
		return this.freezeDelay--;
	}

	public boolean isFrozen() {
		return freezeDelay > 0;
	}

	public int getStunDelay() {
		return freezeDelay;
	}

	public void setStunDelay(int stunDelay) {
		this.stunDelay = stunDelay;
	}

	public int decrementAndGetStunDelay() {
		return this.stunDelay--;
	}

	public boolean isStunned() {
		return stunDelay > 0;
	}

	/**
	 * Determines if this character needs to reset their movement queue.
	 *
	 * @return {@code true} if this character needs to reset their movement queue,
	 *         {@code false} otherwise.
	 */
	public final boolean isResetMovementQueue() {
		return resetMovementQueue;
	}

	/**
	 * Sets the value for {@link CharacterNode#resetMovementQueue}.
	 *
	 * @param resetMovementQueue the new value to set.
	 */
	public final void setResetMovementQueue(boolean resetMovementQueue) {
		this.resetMovementQueue = resetMovementQueue;
	}

	public void setNeedsPlacement(boolean needsPlacement) {
		this.needsPlacement = needsPlacement;
	}

	public boolean isNeedsPlacement() {
		return needsPlacement;
	}

	public RegionInstance getRegionInstance() {
		return regionInstance;
	}

	public void setRegionInstance(RegionInstance regionInstance) {
		this.regionInstance = regionInstance;
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceId() {
		return instanceId;
	}



	public static void walk(Character walker, int targetX, int targetY, int targetSize) {
		int steps = RouteFinder.findRoute(walker.getPosition().getX(), walker.getPosition().getY(), walker.getPosition().getZ(), walker.getSize(), 
				targetSize == 0 ? new FixedTileStrategy(targetX, targetY) : new EntityStrategy(targetX, targetY, targetSize), true);
		if (steps < 1) {
			return;
		}
		Deque<Position> path = new ArrayDeque<>(steps);
		int[] bufferX = RouteFinder.getLastPathBufferX();
		int[] bufferY = RouteFinder.getLastPathBufferY();
		for (int step = steps - 1; step >= 0; step--) {
			path.addLast(new Position(bufferX[step], bufferY[step], walker.getPosition().getZ()));
		}
		walker.getMovementQueue().walk(path);
	}
}