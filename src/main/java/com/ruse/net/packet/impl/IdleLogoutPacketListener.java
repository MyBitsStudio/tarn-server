package com.ruse.net.packet.impl;

import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class IdleLogoutPacketListener implements PacketListener {

	@Override
	public void handleMessage(@NotNull Player player, Packet packet) {
		player.setInactive(true);
		player.getAfk().setAFK(true);
	}
}
