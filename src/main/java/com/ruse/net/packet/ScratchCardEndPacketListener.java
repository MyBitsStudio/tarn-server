package com.ruse.net.packet;

import com.ruse.world.entity.impl.player.Player;

public class ScratchCardEndPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		if (packet.readByte() != 1)
			return;

		player.getCard().setScratching(false);

		player.getCard().getWinnings();

	}

}
