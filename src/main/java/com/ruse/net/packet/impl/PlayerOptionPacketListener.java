
package com.ruse.net.packet.impl;

import com.ruse.model.Locations.Location;
import com.ruse.model.PlayerInteractingOption;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.content.combat.CombatFactory;
import com.ruse.world.content.minigames.impl.dungeoneering.Dungeoneering;
import com.ruse.world.content.skill.impl.old_dungeoneering.UltimateIronmanHandler;
import com.ruse.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player has clicked on another player's
 * menu actions.
 *
 * @author relex lawl
 */

public class PlayerOptionPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		if (player.isTeleporting())
			return;
        switch (packet.getOpcode()) {
            case 153 -> attack(player, packet);
            case 128 -> option1(player, packet);
            case 37 -> option2(player, packet);
            case 227 -> option3(player, packet);
        }
	}

	private static void attack(Player player, Packet packet) {
		int index = packet.readLEShort();
		if (index > World.getPlayers().capacity() || index < 0)
			return;
		final Player attacked = World.getPlayers().get(index);



		if (Misc.checkForOwner()) {
			World.sendOwnerDevMessage(
					player.getUsername() + " attacked index: " + index + ", target = " + attacked.getUsername());
		}

		if (attacked.equals(player)) {
			player.getMovementQueue().reset();
			World.sendStaffMessage("[BUG TRACKER] Error 959.1 has occured. PLEASE REPORT TO CRIMSON!");
			PlayerLogs.log("1 - PVP BUGS",
					"Error 959.1 PVP bug occured with " + player.getUsername() + " attacking " + attacked.getUsername()
							+ ". Pos(p): " + player.getPosition().toString() + " Pos(a): "
							+ attacked.getPosition().toString());
			// System.out.println("Bug Found [959.1]: Attacker: " + player.getUsername() + " Player Attacked: "
			//+ attacked.getUsername());
			return;
		}

		if (attacked.getConstitution() <= 0) {
			return;
		}

		if(player.getPlayerInteractingOption() == PlayerInteractingOption.INVITE) {

//			if(player.getLocation() == Location.DUNGEONEERING) {
//				if(!Dungeoneering.Companion.checkInv(player, attacked)) {
//					return;
//				}
//
//				player.getMinigameAttributes().getDungeoneeringAttributes().setLastInvitation(System.currentTimeMillis());
//				DialogueManager.start(attacked, new DungPartyInvitation(player, attacked));
//				player.getPacketSender().sendMessage("An invitation has been sent to " + attacked.getUsername());
//			}

		}


		if (player.getCombatBuilder().getStrategy() == null) {
			player.getCombatBuilder().determineStrategy();
		}
		if (CombatFactory.checkAttackDistance(player, attacked)) {
			player.getMovementQueue().reset();
		}

		if (player.getCombatBuilder().getAttackTimer() <= 0)
			player.getCombatBuilder().attack(attacked);
	}

	/**
	 * Manages the first option click on a player option menu.
	 *
	 * @param player The player clicking the other entity.
	 * @param packet The packet to read values from.
	 */
	private static void option1(Player player, Packet packet) {
		int id = packet.readShort() & 0xFFFF;
		if (id < 0 || id > World.getPlayers().capacity())
			return;
		Player victim = World.getPlayers().get(id);
		if (victim == null)
			return;
		/*
		 * GameServer.getTaskScheduler().schedule(new WalkToTask(player,
		 * victim.getPosition(), new FinalizedMovementTask() {
		 *
		 * @Override public void execute() { //do first option here } }));
		 */
	}

	/**
	 * Manages the second option click on a player option menu.
	 *
	 * @param player The player clicking the other entity.
	 * @param packet The packet to read values from.
	 */
	private static void option2(Player player, Packet packet) {
		int id = packet.readShort() & 0xFFFF;
		if (id < 0 || id > World.getPlayers().capacity())
			return;
		Player victim = World.getPlayers().get(id);
		if (victim == null)
			return;
		/*
		 * GameServer.getTaskScheduler().schedule(new WalkToTask(player,
		 * victim.getPosition(), new FinalizedMovementTask() {
		 *
		 * @Override public void execute() { //do second option here } }));
		 */
	}

	/**
	 * Manages the third option click on a player option menu.
	 *
	 * @param player The player clicking the other entity.
	 * @param packet The packet to read values from.
	 */
	private static void option3(Player player, Packet packet) {
		int id = packet.readLEShortA() & 0xFFFF;
		if (id < 0 || id > World.getPlayers().capacity())
			return;
		Player victim = World.getPlayers().get(id);
		if (victim == null)
			return;
		/*
		 * GameServer.getTaskScheduler().schedule(new WalkToTask(player,
		 * victim.getPosition(), new FinalizedMovementTask() {
		 *
		 * @Override public void execute() { //do third option here } }));
		 */
	}
}
