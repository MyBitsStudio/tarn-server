package com.ruse.net.packet.impl;

import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class AuthPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, @NotNull Packet packet) {

		final String miss = decode(Misc.readString(packet.getBuffer()));

		if(!miss.equals("�\uFFF9�\uFFFA�\uFFFB￼\uFFFA￼\uFFF9￼\uFFF8�\uFFFA￼\uFFF8￼\uFFF8�\uFFFA")){
			System.out.println("Invalid Auth -- "+miss);
			World.deregister(player);
			return;
		}

		player.getPSettings().setSetting("is-locked", false);
	}

	private @NotNull String decode(@NotNull String code){
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < code.length(); i++) {
			char hiddenChar = code.charAt(i);
			char original = (char) ((hiddenChar >> 4) | (hiddenChar << 4));
			result.append(original);
		}
		return result.toString();
	}

}
