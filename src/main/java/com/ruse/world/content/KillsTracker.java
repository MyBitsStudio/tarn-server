package com.ruse.world.content;

import java.util.Collections;
import java.util.Comparator;

import com.ruse.model.definitions.NpcDefinition;
import com.ruse.world.entity.impl.player.Player;

public class KillsTracker {

	public static void submit(Player player, KillsEntry[] kills) {
		for (KillsEntry kill : kills) {
			if (kill != null)
				submit(player, kill);
		}
	}

	public static void submit(Player player, KillsEntry kill) {
		int index = getIndex(player, kill);
		if (index >= 0) {
			player.getKillsTracker().get(index).amount += kill.amount;
		} else {
			player.getKillsTracker().add(kill);
		}
		/*
		 * if(player.isKillsTrackerOpen()) { open(player); }
		 */
	}

	public static void open(Player player) {
		try {
			/* RESORT THE KILLS */
			player.getKillsTracker().sort((kill1, kill2) -> {
				if (kill1.boss && !kill2.boss) {
					return -1;
				}
				if (kill2.boss && !kill1.boss) {
					return 1;
				}
				if (kill1.boss && kill2.boss || !kill1.boss && !kill2.boss) {
					if (kill1.amount > kill2.amount) {
						return -1;
					} else if (kill2.amount > kill1.amount) {
						return 1;
					} else if (kill1.npcName.compareTo(kill2.npcName) > 0) {
						return 1;
					} else {
						return -1;
					}
				}
				return 0;
			});
			/* SHOW THE INTERFACE */
			player.setKillsTrackerOpen(true);
			resetInterface(player);
			player.getPacketSender().sendString(8144, "NPC Kill Tracker").sendInterface(8134);
			player.getPacketSender().sendString(8147, "@blu@-@whi@ Boss kills");
			int index = 1;
			for (KillsEntry entry : player.getKillsTracker()) {
				if (entry.boss) {
					int toSend = 8147 + index > 8196 ? 12174 + index : 8147 + index;
					player.getPacketSender().sendString(toSend, "@blu@ " + entry.npcName + ": " + entry.amount + "");
					index++;
				}
			}
			index += 1;
			player.getPacketSender().sendString(8147 + index, "@dre@-@whi@ Other Kills");
			index++;
			for (KillsEntry entry : player.getKillsTracker()) {
				if (entry.boss)
					continue;
				int toSend = 8147 + index > 8196 ? 12174 + index : 8147 + index;
				player.getPacketSender().sendString(toSend, "@dre@ " + entry.npcName + ": " + entry.amount);
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public static void resetInterface(Player player) {
		for (int i = 8145; i < 8196; i++)
			player.getPacketSender().sendString(i, "");
		for (int i = 12174; i < 12224; i++)
			player.getPacketSender().sendString(i, "");
		player.getPacketSender().sendString(8136, "Close window");
	}

	public static int getIndex(Player player, KillsEntry kill) {
		for (int i = 0; i < player.getKillsTracker().size(); i++) {
			if (player.getKillsTracker().get(i).npcName.equals(kill.npcName)) {
				return i;
			}
		}
		return -1;
	}
	public static void submitById(Player player, int npcId, boolean runningTotal, boolean boss) {
		KillsEntry entry = entryForID(player, npcId, boss);

		int amount = 1;
		if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
				&& player.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
			amount = 2;
		}

		if(player.getEquipment().hasQuadKills())
			amount += 3;
		else if(player.getEquipment().hasTripleKills())
			amount += 2;
		else if(player.getEquipment().hasDoubleKills())
			amount += 1;

		if(runningTotal)
			entry.setRunningTotal(entry.getRunningTotal() + amount);
		else
			entry.setAmount(entry.getAmount() + amount);
	}
	public static KillsEntry entryForID(final Player player, final int npcId, boolean boss) {
		//If the task tracker contains a entry for the npc return the entry.
		for (KillsEntry killsEntry : player.getKillsTracker()) {
			if(killsEntry.getId() == npcId)
				return killsEntry;
		}
		//If the kill tracker does not contain a entry for the npc return a new entry with 0 kills.
		final KillsEntry entry = new KillsEntry(npcId, 0, boss);
		player.getKillsTracker().add(entry);
		return entry;
	}

	public static class KillsEntry {

		public KillsEntry(int npcId, int amount, boolean boss) {
			this.npcName = NpcDefinition.forId(npcId).getName();
			this.amount = amount;
			this.runningTotal = amount;
			this.boss = boss;
			this.npcId = npcId;
		}

		public int getId() {
			return npcId;
		}
		public int getAmount() {
			return amount;
		}
		public int getRunningTotal() {
			return this.runningTotal;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}
		public void setRunningTotal(int amount) {
			this.runningTotal = amount;
		}

		public int runningTotal;
		public int npcId;
		public String npcName;
		public int amount;
		public boolean boss;

	}


	public static int getTotalKills(Player player) {
        int totalKills = 0;
        for(KillsEntry entry : player.getKillsTracker()) {
            totalKills += entry.getRunningTotal();
        }
        return totalKills;
    }
	
	public static int getTotalKillsForNpc(int npcId, Player player) {
		int total = 0;
		for (KillsEntry entry : player.getKillsTracker()) {
			if (entry.getId() != npcId) {
				continue;
			}
			total += entry.getAmount();
		}
		return total;
	}

}
