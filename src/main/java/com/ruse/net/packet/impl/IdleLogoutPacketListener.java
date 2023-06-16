package com.ruse.net.packet.impl;

import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.world.entity.impl.player.Player;

public class IdleLogoutPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
//		if (player.getRank().isStaff())
//			return;
		player.setInactive(true);
	}
}
