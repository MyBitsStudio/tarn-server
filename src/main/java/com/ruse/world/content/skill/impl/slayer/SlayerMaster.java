//package com.ruse.world.content.skill.impl.slayer;
//
//import ca.momoperes.canarywebhooks.WebhookIdentifier;
//import com.ruse.model.Position;
//import com.ruse.model.Skill;
//import com.ruse.model.definitions.NpcDefinition;
//import com.ruse.world.content.KillsTracker;
//import com.ruse.world.content.NpcRequirements;
//import com.ruse.world.packages.panels.PlayerPanel;
//import com.ruse.world.entity.impl.npc.NPC;
//import com.ruse.world.entity.impl.player.Player;
//
//public enum SlayerMaster {
//	BOSS_SLAYER(90, 9000, new Position(2667, 4015)),
//	EASY_SLAYER(1, 1597, new Position(2853, 9374)),
//	MEDIUM_SLAYER(60, 8275, new Position(2174, 5016)),
//	HARD_SLAYER(80, 9085, new Position(2019, 5009)),
//	ELITE_SLAYER(120, 925, new Position(2725, 2957))
//
//	;
//
//	private SlayerMaster(int slayerReq, int npcId, Position telePosition) {
//		this.slayerReq = slayerReq;
//		this.npcId = npcId;
//		this.position = telePosition;
//	}
//
//	private int slayerReq;
//	private int npcId;
//	private Position position;
//
//	public static SlayerMaster forNpcId(int id) {
//		for (SlayerMaster master : SlayerMaster.values()) {
//			if (master.npcId == id) {
//				return master;
//			}
//		}
//		return EASY_SLAYER;
//	}
//
//	public int getSlayerReq() {
//		return this.slayerReq;
//	}
//
//	public int getNpcId() {
//		return this.npcId;
//	}
//
//	public Position getPosition() {
//		return this.position;
//	}
//	public String getSlayerMasterName() {
//		String name = "";
//		NpcDefinition def = NpcDefinition.forId(getNpcId());
//		if(def != null && def.getName() != null) {
//			name = def.getName();
//		}
//		return name;
//	}
//	public static SlayerMaster forId(int id) {
//		for (SlayerMaster master : SlayerMaster.values()) {
//			if (master.ordinal() == id) {
//				return master;
//			}
//		}
//		return null;
//	}
//
//	public static void changeSlayerMaster(Player player, SlayerMaster master) {
//		player.getPacketSender().sendInterfaceRemoval();
//			int level = master.getSlayerReq();
//
//			String masterName = "Vannaka";
//			if (master == SlayerMaster.MEDIUM_SLAYER) {
//				masterName = "Duradel";
//			} else if (master == SlayerMaster.HARD_SLAYER) {
//				masterName = "Kuradal";
//			} else if (master == SlayerMaster.BOSS_SLAYER) {
//				masterName = "Boss Slayer Master";
//			} else if (master == SlayerMaster.ELITE_SLAYER) {
//				masterName = "Elite Slayer Master";
//			}
//
//			if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) < level) {
//				player.getPacketSender().sendMessage("You need a Slayer level of at least " + master.getSlayerReq()
//						+ " to use " + masterName + ".");
//				player.getPacketSender().sendInterfaceRemoval();
//				return;
//			}
//	/*	if (master == SlayerMaster.BOSS_SLAYER) {
//			if (NPC.getId() == 3) {
//				int total = KillsTracker.getTotalKillsForNpc(NPC.getId(), player);
//				if (total != 10000) {
//					player.sendMessage("TEST, 10k kills npc id 3");
//					//player.getInventory().add(6542, 1);
//					return;
//					}
//				}
//			}
//
//		 * if(player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
//		 * player.getPacketSender().
//		 * sendMessage("Please finish your current task before changing Slayer master."
//		 * ); return; } if(player.getSlayer().getSlayerMaster() == master) {
//		 * player.getPacketSender().sendMessage(""+Misc.formatText(master.toString().
//		 * toLowerCase())+" is already your Slayer master."); return; }
//		 */
//		if(player.getSlayer().getSlayerMaster() != master) {
//			player.getPacketSender().sendMessage("You've successfully changed Slayer master.");
//		}
//		player.getSlayer().setSlayerMaster(master);
//		PlayerPanel.refreshPanel(player);
//
//	}
//}
