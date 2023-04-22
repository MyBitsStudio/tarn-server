package com.ruse.world.content;

import java.util.Map;

import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.entity.impl.player.Player;

public class UIMBank {

	private final Player player;

	public UIMBank(Player player) {
		this.player = player;
	}

	public void open() {
		player.getPacketSender().resetItemsOnInterface(48721, 70);
		player.getPacketSender().sendInterfaceSet(48700, 3321);
		player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
		update();
	}

	public void deposit(int id, int amount) {
		if(player.getInterfaceId() == 48700) {
//			if (player.getUimBankItems().size() >= 70) {
//				player.sendMessage("@red@Your Ultimate ironman bank can only hold 70 items.");
//				return;
//			}

			if (!player.getInventory().contains(id)) {
				return;
			}

			Item item = player.getInventory().getById(id).copy();

			player.getInventory().delete(item.getId(), amount, item.getRarity(), item.getBonus(), item.getEffect());

			//player.getUimBankItems().merge(id, amount, Integer::sum);
			player.getUIMBank().add(item);

			update();
		}
	}

	public void withdraw(int id, int amount, int slot) {
//		ItemDefinition def = new Item(id).getDefinition();
//		boolean stackable = def.isNoted() || def.isStackable();
//		if (player.getInventory().getFreeSlots() < amount) { // Done, gn.
//			if (stackable) {
//
//				if (player.getInventory().getFreeSlots() == 0) {
//					player.sendMessage("Get some more free inventory space");
//					return;
//				}
//			} else {
//				player.sendMessage("Get some more free inventory space");
//				return;
//			}
//		}

//		if (player.getUimBankItems().get(id) != null) {
//			if (amount >= player.getUimBankItems().get(id)) {
//				player.getUimBankItems().remove(id);
//				player.getInventory().add(id, amount);
//			} else {
//				player.getUimBankItems().replace(id, player.getUimBankItems().get(id) - amount);
//				player.getInventory().add(id, amount);
//			}
//		}

		Item item = player.getUIMBank().get(slot).copy();
		if(item == null) {
		//	System.out.println("error in uim");
		} else {
			item.setAmount(amount);
			if (player.getInventory().getFreeSlots() < amount) {
				if (item.getDefinition().isStackable()) {

					if (player.getInventory().getFreeSlots() == 0) {
						player.sendMessage("Get some more free inventory space");
						return;
					}
				} else {
					player.sendMessage("Get some more free inventory space");
					return;
				}
			}
			player.getUIMBank().delete(item, slot);
			player.getInventory().add(item);
		}
		update();

	}

	private void update() {
		player.getInventory().refreshItems();
		player.getUIMBank().sortItems().refreshItems();
		player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
		player.getPacketSender().resetItemsOnInterface(48721, 70);
		player.getPacketSender().sendItemContainer(player.getUIMBank(), 48721);
//		int index = 0;
//		for (Map.Entry<Integer, Integer> entry : player.getUimBankItems().entrySet()) {
//			if (entry.getValue() > 0) {
//				player.getPacketSender().sendItemOnInterface(48721, entry.getKey(), index, entry.getValue(),-1);
//			}
//			index++;
//		}
	}

}
