package com.ruse.net.packet.impl;

import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.world.entity.impl.player.Player;

/**
 * This packet listener handles player's mouse click on the "Click here to
 * continue" option, etc.
 * 
 * @author relex lawl
 */

public class DialoguePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		switch (packet.getOpcode()) {
			case DIALOGUE_OPCODE:
				if(player.getChat() != null)
					player.getChat().nextStage();
				break;
			}
	}

	public static final int DIALOGUE_OPCODE = 40;
}
