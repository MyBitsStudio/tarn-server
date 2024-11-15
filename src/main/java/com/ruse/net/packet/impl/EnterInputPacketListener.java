package com.ruse.net.packet.impl;

import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.util.Misc;
import com.ruse.util.StringCleaner;
import com.ruse.world.entity.impl.player.Player;

/**
 * This packet manages the input taken from chat box interfaces that allow
 * input, such as withdraw x, bank x, enter name of friend, etc.
 * 
 * @author Gabriel Hannason
 */

public class EnterInputPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case ENTER_SYNTAX_OPCODE:
			String name = Misc.readString(packet.getBuffer());
			if(StringCleaner.securityBreach(name)){
				player.getPSecurity().getPlayerLock().increase("secLock", name);
				System.out.println("Security breach Enter Input Syntax: "+ name);
				return;
			}
			if(StringCleaner.censored(name)){
				player.getPSecurity().getPlayerLock().increase("wordAtt", name);
				System.out.println("Security breach Enter Input Syntax: "+ name);
				return;
			}
			if (player.getInputHandling() != null)
				player.getInputHandling().handleSyntax(player, name);
			player.setInputHandling(null);
			break;
		case ENTER_AMOUNT_OPCODE:
			int amount = packet.readInt();
			if (amount <= 0)
				return;
			if (player.getInputHandling() != null)
				player.getInputHandling().handleAmount(player, amount);
			player.setInputHandling(null);
			break;
		case ENTER_LONG_AMOUNT_OPCODE:
			long longAmount = packet.readLong();
			if (longAmount <= 0)
				return;
			if (player.getInputHandling() != null)
				player.getInputHandling().handleLongAmount(player, longAmount);
			player.setInputHandling(null);
			break;
		}
	}

	public static final int ENTER_AMOUNT_OPCODE = 208, ENTER_SYNTAX_OPCODE = 60, ENTER_LONG_AMOUNT_OPCODE = 209;
}
