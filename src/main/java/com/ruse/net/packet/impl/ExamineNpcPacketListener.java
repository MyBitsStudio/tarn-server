package com.ruse.net.packet.impl;

import com.ruse.model.definitions.NpcDefinition;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.world.content.KillsTracker;
import com.ruse.world.entity.impl.player.Player;

public class ExamineNpcPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int npc = packet.readShort();
		if (npc <= 0) {
			return;
		}
		NpcDefinition npcDef = NpcDefinition.forId(npc);
		if(player.getRank().isDeveloper()) {
			player.getPA().sendMessage("NPC ID: " + npc);
		}
		if(npcDef == null){
			player.getPacketSender().sendMessage("No such NPC exists.");
			return;
		}
		int total = KillsTracker.getTotalKillsForNpc(npc, player);
		player.getPacketSender().sendMessage("@bla@You currently have @red@"+ total +" " + npcDef.getName()+ "@bla@ kills.");
	}

}
