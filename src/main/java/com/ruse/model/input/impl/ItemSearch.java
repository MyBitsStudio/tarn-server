//package com.ruse.model.input.impl;
//
//import com.ruse.engine.task.Task;
//import com.ruse.engine.task.TaskManager;
//import com.ruse.model.definitions.ItemDefinition;
//import com.ruse.model.definitions.NpcDefinition;
//import com.ruse.model.input.Input;
//import com.ruse.util.Misc;
//import com.ruse.world.entity.impl.player.Player;
//import com.ruse.world.packages.dialogue.DialogueManager;
//
//public class ItemSearch extends Input {
//
//	@Override
//	public void handleSyntax(Player player, final String syntax) {
//		DialogueManager.sendStatement(player, "Searching for item: " + syntax + "...");
//		player.getMovementQueue().setLockMovement(true);
//		TaskManager.submit(new Task(5, player, false) {
//			@Override
//			protected void execute() {
//				try {
//					String syntaxCopy = syntax;
//					Object[] data = getFixedSyntax(syntaxCopy);
//					syntaxCopy = (String) data[0];
//
//					player.getPacketSender().sendInterfaceRemoval();
//					if (syntaxCopy.length() <= 3) {
//						DialogueManager.sendStatement(player, "You must be more specific than that.");
//						return;
//					}
//
//					int itemId = (int) data[1];
//					int npcDropId = -1;
//
//					for (ItemDefinition def : ItemDefinition.getDefinitions()) {
//						if (def != null) {
//							if (def.getName().contains(syntaxCopy) && itemId == -1) {
//								itemId = def.getId();
//							}
//							if (def.getName().equalsIgnoreCase(syntaxCopy)) {
//								itemId = def.getId();
//								break;
//							}
//						}
//					}
//
//					if (itemId > 0 && ItemDefinition.forId(itemId).isNoted()) {
//						itemId--;
//					}
//
//					if (itemId == 14486) {
//						itemId = 14484;
//					}
//
//					if (itemId > 0) {
//						for (NPCDrops npcDrops : NPCDrops.getDrops().values()) {
//							if (npcDrops != null) {
//								for (NpcDropItem item : npcDrops.getDropList()) {
//									if (item != null && item.getId() == itemId) {
//										for (int npcId : npcDrops.getNpcIds()) {
//											if (npcId == -1)
//												continue;
//											if (NpcDefinition.forId(npcId) != null
//													&& !NpcDefinition.forId(npcId).getName().equalsIgnoreCase("null")) {
//												npcDropId = npcId;
//												break;
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//
//					if (itemId == -1 || npcDropId == -1) {
//						DialogueManager.sendStatement(player, "No item found for: " + syntaxCopy);
//					} else {
//						DialogueManager.sendStatement(player, "Ah, yes! The " + Misc.formatText(syntaxCopy) + "." +
//								Misc.anOrA(NpcDefinition.forId(npcDropId).getName()) +" should have it");
//					}
//				} catch (Exception e) {
//				}
//
//				stop();
//			}
//
//			@Override
//			public void stop() {
//				setEventRunning(false);
//				player.getMovementQueue().setLockMovement(false);
//			}
//		});
//	}
//
//}
