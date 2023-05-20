package com.ruse.net.packet.impl;

import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.security.ServerSecurity;
import com.ruse.util.Misc;
import com.ruse.util.StringCleaner;
import com.ruse.world.content.PlayerPunishment;
import com.ruse.world.content.clan.ClanChatManager;
import com.ruse.world.content.dialogue.DialogueManager;
import com.ruse.world.content.discordbot.JavaCord;
import com.ruse.world.entity.impl.player.Player;

import java.util.Arrays;

public class SendClanChatMessagePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		String clanMessage = Misc.readString(packet.getBuffer());
		if (clanMessage == null || clanMessage.length() < 1)
			return;
		if (ServerSecurity.getInstance().isPlayerMuted(player.getUsername())) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return;
		}
		if(StringCleaner.securityBreach(clanMessage)){
			player.getPSecurity().getPlayerLock().increase("secLock", clanMessage);
			System.out.println("Security breach: "+ clanMessage);
			player.getPacketSender().sendMessage("@red@[SECURITY] This is your only warning. Do not attempt to breach the security of the server again.");
			return;
		}

		if(StringCleaner.censored(clanMessage)){
			player.getPSecurity().getPlayerLock().increase("wordAtt", clanMessage);
			System.out.println("Censored word: "+clanMessage);
			player.getPacketSender().sendMessage("@red@[SECURITY] This is your only warning. Do not attempt to breach the security of the server again.");
			return;
		}
		player.afkTicks = 0;
		player.afk = false;
		ClanChatManager.sendMessage(player, clanMessage);
		if(!clanMessage.contains("@"))
			JavaCord.sendMessage("\uD83D\uDCAC│\uD835\uDDF0\uD835\uDDF9\uD835\uDDEE\uD835\uDDFB-\uD835\uDDF0\uD835\uDDF5\uD835\uDDEE\uD835\uDE01", "**[" + player.getUsername() + "]  " + clanMessage + "  ** ");
	}

}
