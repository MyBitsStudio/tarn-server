package com.ruse.net.packet.impl;

import com.ruse.model.Locations.Location;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.util.Misc;
import com.ruse.util.StringCleaner;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;

import java.util.Arrays;

public class DungeoneeringPartyInvitatationPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		String plrToInvite = Misc.readString(packet.getBuffer());
		if(StringCleaner.securityBreach(plrToInvite)){
			player.getPSecurity().getPlayerLock().increase("secLock", plrToInvite);
			System.out.println("Security breach Dungeon Party: "+ plrToInvite);
			return;
		}

		if(StringCleaner.censored(plrToInvite)){
			player.getPSecurity().getPlayerLock().increase("wordAtt", plrToInvite);
			System.out.println("Security breach Dungeon Party: "+ plrToInvite);
			return;
		}
		if (plrToInvite == null || plrToInvite.length() <= 0)
			return;
		plrToInvite = Misc.formatText(plrToInvite);
//		if (player.getLocation() == Location.DUNGEONEERING) {
//			if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty() == null
//					|| player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner() == null)
//				return;
//			player.getPacketSender().sendInterfaceRemoval();
//			if (player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getOwner() != player) {
//				player.getPacketSender().sendMessage("Only the party leader can invite other players.");
//				return;
//			}
//			Player invite = World.getPlayerByName(plrToInvite);
//			if (invite == null) {
//				player.getPacketSender().sendMessage("That player is currently not online.");
//				return;
//			}
//			player.getMinigameAttributes().getDungeoneeringAttributes().getParty().invite(invite);
//		}
	}
}
