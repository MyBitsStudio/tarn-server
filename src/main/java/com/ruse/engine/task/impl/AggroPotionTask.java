package com.ruse.engine.task.impl;

import com.ruse.engine.task.Task;
import com.ruse.model.*;
import com.ruse.model.Locations.Location;
import com.ruse.world.World;
import com.ruse.world.content.Consumables;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;

public class AggroPotionTask extends Task {

	private final int type;
	public AggroPotionTask(Player player, int type) {
		super(1, player, true);
		this.player = player;
		this.type = type;
	}

	final Player player;

	@Override
	public void execute() {
		if (player == null || !player.isRegistered()) {
			stop();
			return;
		}

		ObjectArrayList<NPC> npcs = World.getNearbyNPCs(player.getPosition(), 6);
		for(NPC npc : npcs){
			if(npc != null){
				if(!npc.isAggressive() && npc.getDefinition().isAttackable()){
					npc.setAggressive(true);
					npc.setAggressiveDistance(10);
					npc.setForceAggressive(true);
				}
			}
		}

		if (player.getAggroPotionTimer() == 20)
			player.getPacketSender().sendMessage("@red@Your Aggro Potion's effect is about to run out.");
		if (player.getAggroPotionTimer() <= 0) {
			player.getPacketSender().sendMessage("@red@Your Aggro Potion's effect has run out.");
			player.setAggroPotionTimer(0);
			stop();
		}
	}
}
