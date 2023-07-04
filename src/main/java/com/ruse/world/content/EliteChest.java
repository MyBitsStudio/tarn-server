package com.ruse.world.content;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Animation;
import com.ruse.model.GameObject;
import com.ruse.model.Graphic;
import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.List;

public class EliteChest {

	public static void handleChest(final Player p, final GameObject chest, boolean command) {
		if (!p.getClickDelay().elapsed(3000))
			return;
		if (!p.getInventory().contains(18647)) {
			p.getPacketSender().sendMessage("This chest can only be opened with an Elite Slayer Key.");
			return;
		}
		if (p.getInventory().getFreeSlots() < 3) {
			p.getPacketSender()
					.sendMessage("You need at least 3 free inventory slots to loot this chest.");
			return;
		}
		if (command) {
			p.performAnimation(new Animation(1818));
		} else {
			p.performAnimation(new Animation(827));
		}
		p.performGraphic(new Graphic(1322));
		p.getInventory().delete(18647, 1);
		p.getPacketSender().sendMessage("You open the chest..");
		TaskManager.submit(new Task(1, p, false) {
			int tick = 0;

			@Override
			public void execute() {
				switch (tick) {
				case 2:
					Item item = itemRewards[Misc.randomMinusOne(itemRewards.length)];
					if(p.getEquipment().hasDoubleCash() && item.getId() == 995) {
						item.setAmount(item.getAmount() * 2);
					}
					p.getInventory().add(item);
					if (item.getDefinition() == null || item.getDefinition().getName() == null) {
						p.getPacketSender().sendMessage("..and find an item!");
					} else {
						p.getPacketSender().sendMessage("..and find " + Misc.anOrA(item.getDefinition().getName()) + " "
								+ item.getDefinition().getName() + "!");
					}
					stop();
					break;
				}
				tick++;
			}
		});
		p.getClickDelay().reset();
	}

	public static void sendRewardInterface(Player player) {
		try {

			List<Item> list = new ArrayList<Item>();
			for (int i = 0; i < itemRewards.length; i++) {
				list.add(itemRewards[i]);
			}

			resetInterface(player);

			player.getPacketSender().sendString(8144, "Crystal Chest loot table").sendInterface(8134);

			int index = 0, start = 8147, cap = 8196, secondstart = 12174, secondcap = 12224, index2 = 0;
			boolean newline = false;

			for (int i = 0; i < list.size(); i++) {

				if (ItemDefinition.forId(list.get(i).getId()) == null
						|| ItemDefinition.forId(list.get(i).getId()).getName() == null) {
					continue;
				}

				int toSend = 8147 + index;

				if (index + start > cap) {
					newline = true;
				}

				if (newline) {
					toSend = secondstart + index2;
				}

				if (newline && toSend >= secondcap) {
					player.getPacketSender().sendMessage("<shad=ffffff>" + list.get(i).getAmount() + "x <shad=0>"
							+ Misc.getColorBasedOnValue(
									ItemDefinition.forId(list.get(i).getId()).getValue() * list.get(i).getAmount())
							+ ItemDefinition.forId(list.get(i).getId()).getName() + ".");

					continue;
				}
				player.getPacketSender().sendString(toSend,
						list.get(i).getAmount() + "x "
								+ Misc.getColorBasedOnValue(
										ItemDefinition.forId(list.get(i).getId()).getValue() * list.get(i).getAmount())
								+ ItemDefinition.forId(list.get(i).getId()).getName() + ".");

				if (newline) {
					index2++;
				} else {
					index++;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void resetInterface(Player player) {
		for (int i = 8145; i < 8196; i++)
			player.getPacketSender().sendString(i, "");
		for (int i = 12174; i < 12224; i++)
			player.getPacketSender().sendString(i, "");
		player.getPacketSender().sendString(8136, "Close window");
	}

	public static final Item[] loot22 = {
			new Item(995, 1000000), // coins
			new Item(9719, 1),
			new Item(23071, 1),
			new Item(23118, 1),
			new Item(23121, 1),
			new Item(23124, 1),
			new Item(23069, 1),
			new Item(23045, 1),
			new Item(19886, 1),
			new Item(23041, 1),
			new Item(23042, 1),
			new Item(23043, 1),
			new Item(23070, 1),
			new Item(23114, 1),
			new Item(23046, 1),
			new Item(23122, 1),
			new Item(23119, 1),
			new Item(23125, 1),
			new Item(23074, 1),
			new Item(23110, 1),
			new Item(19888, 1),
			new Item(23047, 1),
			new Item(18883, 1),
			new Item(10946, 1),
			new Item(10946, 5),
			new Item(23057, 1),
			new Item(18881, 1),
			new Item(15003, 1),
			new Item(15002, 1),

	};

	public static final Item[] itemRewards = {
			new Item(995, 200000), // coins
			new Item(995, 250000), // coins
			new Item(995, 300000), // coins
			new Item(995, 350000), // coins
			new Item(995, 400000), // coins
			new Item(995, 450000), // coins
			new Item(995, 500000), // coins
			new Item(995, 550000), // coins
			new Item(995, 600000), // coins
			new Item(995, 650000), // coins
			new Item(995, 700000), // coins
			new Item(995, 750000), // coins
			new Item(995, 800000), // coins
			new Item(995, 850000), // coins
			new Item(995, 900000), // coins
			new Item(995, 950000), // coins
			new Item(995, 1000000), // coins
			new Item(995, 200000), // coins
			new Item(995, 250000), // coins
			new Item(995, 300000), // coins
			new Item(995, 350000), // coins
			new Item(995, 400000), // coins
			new Item(995, 450000), // coins
			new Item(995, 500000), // coins
			new Item(995, 550000), // coins
			new Item(995, 600000), // coins
			new Item(995, 650000), // coins
			new Item(995, 700000), // coins
			new Item(995, 750000), // coins
			new Item(995, 800000), // coins
			new Item(995, 850000), // coins
			new Item(995, 900000), // coins
			new Item(995, 950000), // coins
			new Item(995, 1000000), // coins
			new Item(995, 200000), // coins
			new Item(995, 250000), // coins
			new Item(995, 300000), // coins
			new Item(995, 350000), // coins
			new Item(995, 400000), // coins
			new Item(995, 450000), // coins
			new Item(995, 500000), // coins
			new Item(995, 550000), // coins
			new Item(995, 600000), // coins
			new Item(995, 650000), // coins
			new Item(995, 700000), // coins
			new Item(995, 750000), // coins
			new Item(995, 800000), // coins
			new Item(995, 850000), // coins
			new Item(995, 900000), // coins
			new Item(995, 950000), // coins
			new Item(995, 1000000), // coins
			new Item(995, 200000), // coins
			new Item(995, 250000), // coins
			new Item(995, 300000), // coins
			new Item(995, 350000), // coins
			new Item(995, 400000), // coins
			new Item(995, 450000), // coins
			new Item(995, 500000), // coins
			new Item(995, 550000), // coins
			new Item(995, 600000), // coins
			new Item(995, 650000), // coins
			new Item(995, 700000), // coins
			new Item(995, 750000), // coins
			new Item(995, 800000), // coins
			new Item(995, 850000), // coins
			new Item(995, 900000), // coins
			new Item(995, 950000), // coins
			new Item(995, 1000000), // coins
			new Item(9719, 1),
			new Item(23071, 1),
			new Item(23118, 1),
			new Item(23121, 1),
			new Item(23124, 1),
			new Item(23069, 1),
			new Item(23045, 1),
			new Item(19886, 1),
			new Item(23041, 1),
			new Item(23042, 1),
			new Item(23043, 1),
			new Item(23070, 1),
			new Item(23114, 1),
			new Item(23046, 1),
			new Item(23122, 1),
			new Item(23119, 1),
			new Item(23125, 1),
			new Item(23074, 1),
			new Item(23110, 1),
			new Item(19888, 1),
			new Item(23047, 1),
			new Item(9719, 1),
			new Item(23071, 1),
			new Item(23118, 1),
			new Item(23121, 1),
			new Item(23124, 1),
			new Item(23069, 1),
			new Item(23045, 1),
			new Item(19886, 1),
			new Item(23041, 1),
			new Item(23042, 1),
			new Item(23043, 1),
			new Item(23070, 1),
			new Item(23114, 1),
			new Item(23046, 1),
			new Item(23122, 1),
			new Item(23119, 1),
			new Item(23125, 1),
			new Item(23074, 1),
			new Item(23110, 1),
			new Item(19888, 1),
			new Item(23047, 1),
			new Item(9719, 1),
			new Item(23071, 1),
			new Item(23118, 1),
			new Item(23121, 1),
			new Item(23124, 1),
			new Item(23069, 1),
			new Item(23045, 1),
			new Item(19886, 1),
			new Item(23041, 1),
			new Item(23042, 1),
			new Item(23043, 1),
			new Item(23070, 1),
			new Item(23114, 1),
			new Item(23046, 1),
			new Item(23122, 1),
			new Item(23119, 1),
			new Item(23125, 1),
			new Item(23074, 1),
			new Item(23110, 1),
			new Item(19888, 1),
			new Item(23047, 1),
			new Item(18883, 1),
			new Item(10946, 1),
			new Item(10946, 5),
			new Item(23057, 1),
			new Item(18881, 1),
			new Item(15003, 1),
			new Item(15002, 1),

	};

}
