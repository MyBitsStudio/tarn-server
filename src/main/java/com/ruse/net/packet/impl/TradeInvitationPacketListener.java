package com.ruse.net.packet.impl;

import com.ruse.engine.task.impl.WalkToTask;
import com.ruse.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.ruse.model.Locations;
import com.ruse.model.Locations.Location;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player accepts a trade offer, whether
 * it's from the chat box or through the trading player menu.
 * 
 * @author relex lawl
 */

public class TradeInvitationPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0) {
			System.out.println("Player is dead, cannot trade.");
			return;
		}
		if (player.isTeleporting()) {
			System.out.println("Player is teleporting, cannot trade.");
			return;
		}
		if (player.isPlayerLocked()) {
			System.out.println("Player is locked, cannot trade.");
			return;
		}
		player.getNewSkills().stopSkilling();
		int index = packet.getOpcode() == TRADE_OPCODE ? (packet.readShort() & 0xFF) : packet.readLEShort();
		if (index < 0 || index > World.getPlayers().capacity()) {
			System.out.println("Invalid player index: " + index);
			return;
		}
		Player target = World.getPlayers().get(index);
		if (target == null || !Locations.goodDistance(player.getPosition(), target.getPosition(), 13)) {
			System.out.println("Null : " + index);
			return;
		}
		player.setWalkToTask(
				new WalkToTask(player, target.getPosition(), target.getSize(), () -> {
					if (target.getIndex() != player.getIndex())
						player.getTrading().requestTrade(target);
				}));
	}

	public static final int TRADE_OPCODE = 39;
	public static final int CHATBOX_TRADE_OPCODE = 139;
}
