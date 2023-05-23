package com.ruse.world.content.minigames.impl;

import java.util.ArrayList;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Graphic;
import com.ruse.model.Locations.Location;
import com.ruse.model.Position;
import com.ruse.model.RegionInstance;
import com.ruse.model.RegionInstance.RegionInstanceType;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.dialogue.DialogueManager;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

public class TheSix {

	/* Allows us to instance a singular player object. */
	public TheSix(Player player) {
		this.player = player;
	}

	private Player player;

	public void enter(boolean clan) {
		player.getPacketSender().sendInterfaceRemoval();


		int z = (player.getIndex() + 1) * 4;
		final Position pos = new Position(2384, 4721, z);
		final Position orig = player.getPosition().copy();

		if (!clan) {
			Barrows.resetBarrows(player);
			player.setDoingClanBarrows(clan);
			player.setBarrowsKilled(0);
			player.getPacketSender().sendInterfaceRemoval();
			player.moveTo(pos);
		}

		Barrows.resetBarrows(player);
		player.setDoingClanBarrows(clan);
		player.setBarrowsKilled(0);
		player.getPacketSender().sendInterfaceRemoval();
		player.moveTo(pos);

		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.THE_SIX));

		spawn(clan);
	}

	public void joinClan() {
		player.getPacketSender().sendInterfaceRemoval();

	}

	public void leave(boolean move) {

		final int killcount = player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount();
		if (killcount > 0) {
			int points = player.doingClanBarrows() ? 1 * killcount : 2 * killcount;
			player.getPointsHandler().setBarrowsPoints(points, true);
			player.getPacketSender().sendMessage("You've received " + points + " Barrows points.");
		}

		Barrows.resetBarrows(player);

		if (move) {
			player.moveTo(new Position(3562, 3311));
		}

		if (player.doingClanBarrows()) {

		}

		player.setDoingClanBarrows(false);
	}

	public boolean allKilled() {
		if (player.getBarrowsKilled() >= 6) {
			player.setBarrowsKilled(0);
			for (int i = 0; i < player.getMinigameAttributes().getBarrowsMinigameAttributes()
					.getBarrowsData().length; i++)
				player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[i][1] = 0;
			Barrows.updateInterface(player);
			return true;
		}
		return false;
	}

	public void spawn(boolean clan) {
		final int z = player.getPosition().getZ();
		TaskManager.submit(new Task(3, player, false) {
			int tick = 0;

			@Override
			protected void execute() {
				if (player.getLocation() != Location.THE_SIX
						|| !clan && player.getRegionInstance() == null) {
					leave(false);
					stop();
					return;
				}
				Position pos = null;
				NPC npc = null;
				switch (tick) {
				case 0:
					pos = new Position(2385, 4717, z);
					npc = new NPC(2030, pos);
					break;
				case 1:
					pos = new Position(2384, 4723, z);
					npc = new NPC(2026, pos);
					break;
				case 2:
					pos = new Position(2388, 4720, z);
					npc = new NPC(2025, pos);
					break;
				case 3:
					pos = new Position(2379, 4720, z);
					npc = new NPC(2028, pos);
					break;
				case 4:
					pos = new Position(2382, 4723, z);
					npc = new NPC(2029, pos);
					break;
				case 5:
					pos = new Position(2387, 4722, z);
					npc = new NPC(2027, pos);
					break;
				case 6:
					stop();
					break;
				}
				if (npc != null && pos != null) {
					World.register(npc);
					npc.performGraphic(new Graphic(354));
					Player target = player;
					if (clan) {
						ArrayList<Player> LIST = new ArrayList<Player>();

						target = LIST.get(Misc.getRandom(LIST.size() - 1));
					}
					npc.getCombatBuilder().attack(target);
					if (clan) {

					} else {
						player.getRegionInstance().getNpcsList().add(npc);
					}
				}
				tick++;
			}
		});
	}
}
