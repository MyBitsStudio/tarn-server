package com.ruse.world.content.transportation;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.engine.task.impl.AutorelFixTask;
import com.ruse.model.Animation;
import com.ruse.model.Graphic;
import com.ruse.model.Position;
import com.ruse.world.content.Sounds;
import com.ruse.world.content.Sounds.Sound;
import com.ruse.world.content.skill.impl.old_dungeoneering.Dungeoneering;
import com.ruse.world.entity.impl.player.Player;


public class TeleportHandler2 {

	public static void teleportPlayer(final Player player, final Position targetLocation, final TeleportType teleportType) {
		if(teleportType != TeleportType.LEVER) {
			if(!checkReqs(player, targetLocation)) {
				return;
			}
		}
		if (!player.getControllerManager().processTeleport(teleportType, targetLocation)) {
			player.sendMessage("You cannot teleport right now.");
			return;
		}

		/*if (player.inFFALobby){
			player.sendMessage("Use the portal to leave the ffa lobby!");
			return;
		}
		
		if (player.inFFA) {
			player.sendMessage("You can not teleport out of FFA!");
			return;
		}
		
		if(player.inCustomFFA) {
			player.sendMessage("You can not teleport out of Custom FFA!");
			return;
		}
		if(player.inCustomFFALobby) {
			player.sendMessage("Use the portal to leave the ffa lobby!");
			return;
		}
		
		if(player.inLMS) {
			player.sendMessage("You can not teleport out of Last Man Standing");
			return;
		}
		
		if(player.inLMSLobby) {
			player.sendMessage("Use the portal to leave the LMS lobby!");
			return;
		}
		if(player.getLocation() == Location.CONSTRUCTION) {
			player.getPacketSender().sendMessage("Please use the portal to exit your house");
			return;
		}*/
		if(!player.getClickDelay().elapsed(4500) || player.getMovementQueue().isLockMovement())
			return;

		player.getControllerManager().magicTeleported(teleportType.ordinal());
		player.getCombatBuilder().reset(true);
		TaskManager.submit(new AutorelFixTask(player));
		player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
		cancelCurrentActions(player);
		player.performAnimation(teleportType.getStartAnimation());
		player.performGraphic(teleportType.getStartGraphic());
		Sounds.sendSound(player, Sound.TELEPORT);
	//	player.endKeyRoom(false);
		//player.endCustomBossRoom();
		TaskManager.submit(new Task(1, player, true) {
			int tick = 0;
			@Override
			public void execute() {
				switch(teleportType) {
				case LEVER:
					if(tick == 0)
						player.performAnimation(new Animation(2140));
					else if(tick == 2) {
						player.performAnimation(new Animation(8939, 20));
						player.performGraphic(new Graphic(1576));
					} else if(tick == 4) {
						player.performAnimation(new Animation(8941));
						player.performGraphic(new Graphic(1577));
						player.moveTo(targetLocation).setPosition(targetLocation);
						player.getMovementQueue().setLockMovement(false).reset();
						stop();
					}
					break;
				default:
					if(tick == teleportType.getStartTick()) {
						cancelCurrentActions(player);
						player.performAnimation(teleportType.getEndAnimation());
						player.performGraphic(teleportType.getEndGraphic());
						
						if(Dungeoneering.doingOldDungeoneering(player)) {
							final Position dungEntrance = player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getDungeoneeringFloor().getEntrance().copy().setZ(player.getPosition().getZ());
							player.moveTo(dungEntrance).setPosition(dungEntrance);
						} else {
							player.moveTo(targetLocation).setPosition(targetLocation);
						}
						
						player.setTeleporting(false);
					} else if(tick == teleportType.getStartTick() + 3) {
						player.getMovementQueue().setLockMovement(false).reset();
					} else if(tick == teleportType.getStartTick() + 4)
						stop();
					break;
				}
				tick++;
			}
			@Override
			public void stop() {
				setEventRunning(false);
				player.setTeleporting(false);
				player.getClickDelay().reset(0);
			}
		});
		player.getClickDelay().reset();
	}
	
	public static void teleportPlayer2(final Player player, final Position targetLocation, final TeleportType teleportType) {
		if(teleportType != TeleportType.LEVER) {
			if(!checkReqs(player, targetLocation)) {
				return;
			}
		}
		/*if (player.inFFALobby){
			player.sendMessage("Use the portal to leave the ffa lobby!");
			return;
		}
		if (player.inFFA) {
			player.sendMessage("You can not teleport out of FFA!");
			return;
		}
		
		if(player.inCustomFFA) {
			player.sendMessage("You can not teleport out of Custom FFA!");
			return;
		}
		if(player.inCustomFFALobby) {
			player.sendMessage("Use the portal to leave the ffa lobby!");
			return;
		}
		
		if(player.getLocation() == Location.CONSTRUCTION) {
			player.getPacketSender().sendMessage("Please use the portal to exit your house");
			return;
		}*/
		if(!player.getClickDelay().elapsed(4500) || player.getMovementQueue().isLockMovement())
			return;
		player.getCombatBuilder().reset(true);
		player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
		cancelCurrentActions(player);
		player.performAnimation(teleportType.getStartAnimation());
		player.performGraphic(teleportType.getStartGraphic());
		Sounds.sendSound(player, Sound.TELEPORT);
		TaskManager.submit(new Task(1, player, true) {
			int tick = 0;
			@Override
			public void execute() {
				switch(teleportType) {
				case LEVER:
					if(tick == 0)
						player.performAnimation(new Animation(2140));
					else if(tick == 2) {
						player.performAnimation(new Animation(8939, 20));
						player.performGraphic(new Graphic(1576));
					} else if(tick == 4) {
						player.performAnimation(new Animation(8941));
						player.performGraphic(new Graphic(1577));
						player.moveTo(targetLocation).setPosition(targetLocation);
						player.getMovementQueue().setLockMovement(false).reset();
						stop();
					}
					break;
				default:
					if(tick == teleportType.getStartTick()) {
						cancelCurrentActions(player);
						player.performAnimation(teleportType.getEndAnimation());
						player.performGraphic(teleportType.getEndGraphic());
						
						if(Dungeoneering.doingOldDungeoneering(player)) {
							final Position dungEntrance = player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getDungeoneeringFloor().getEntrance().copy().setZ(player.getPosition().getZ());
							player.moveTo(dungEntrance).setPosition(dungEntrance);
						} else {
							player.moveTo(targetLocation).setPosition(targetLocation);
						}
						
						player.setTeleporting(false);
					} else if(tick == teleportType.getStartTick() + 3) {
						player.getMovementQueue().setLockMovement(false).reset();
					} else if(tick == teleportType.getStartTick() + 4)
						stop();
					break;
				}
				tick++;
			}
			@Override
			public void stop() {
				setEventRunning(false);
				player.setTeleporting(false);
				player.getClickDelay().reset(0);
			}
		});
		player.getClickDelay().reset();
	}


	public static boolean interfaceOpen(Player player) {
		if(player.getInterfaceId() > 0 && player.getInterfaceId() != 50100) {
			player.getPacketSender().sendMessage("Please close the interface you have open before opening another.");
			return true;
		}
		return false;
	}

	public static boolean checkReqs(Player player, Position targetLocation) {
		if(player.getConstitution() <= 0)
			return false;
		if(player.getTeleblockTimer() > 0) {
			player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
			return false;
		}
		if(player.getLocation() != null && !player.getLocation().canTeleport(player))
			return false;
		if ((player.isPlayerLocked() || player.isGroupIronmanLocked()) || player.isCrossingObstacle()) {
			player.getPacketSender().sendMessage("You cannot teleport right now.");
			return false;
		}
		return true;
	}

	public static void cancelCurrentActions(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		player.setTeleporting(false);
		player.setWalkToTask(null);
		player.setInputHandling(null);
		player.getNewSkills().stopSkilling();
		player.setEntityInteraction(null);
		player.getMovementQueue().setFollowCharacter(null);
		player.getCombatBuilder().cooldown(false);
		player.setResting(false);
	}
}
