package com.ruse.world.content.item_upgrader;

import com.ruse.model.GameMode;
import com.ruse.model.Item;
import com.ruse.model.Skill;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.content.dialogue.DialogueManager;
import com.ruse.world.entity.impl.player.Player;

public class UpgradeHandler {

	private final int maxItems = 50;

	private Player player;

	public UpgradeHandler(Player player) {
		this.player = player;
	}

	private static final UpgradeData[] data = UpgradeData.values();

	public void init() {
		player.setUpgradeType(UpgradeType.WEAPON);
		player.getPA().sendFrame126("Recipe Interface", 47255);
	}

	public void openInterface() {
		player.setUpgradeType(UpgradeType.WEAPON);
		selectTab(-18272);
		player.getPA().sendInterface(47250);
	}

	public boolean selectTab(int buttonId) {
		switch (buttonId) {
		case -18272:
			player.setUpgradeType(UpgradeType.WEAPON);
			loadList();
			return true;

		case -18269:
			player.setUpgradeType(UpgradeType.ARMOR);
			loadList();
			return true;

		case -18266:
			player.setUpgradeType(UpgradeType.TOOL);
			loadList();
			return true;

		case -18284:
			player.getPA().removeInterface();
			return true;
		}
		return false;
	}

	public void clearList() {
		for (int i = 47281; i < 47281 + maxItems; i++) {
			player.getPA().sendFrame126("", i);
		}
	}

	public void loadList() {
		clearList();
		int frame = 47281;
		for (UpgradeData data : data) {
			if (data.getType() == player.getUpgradeType()) {
				player.getPA().sendFrame126(Misc.capitalize(data.name().replace("_", " ")), frame++);
				if (frame >= 47281 + maxItems) {
					System.err.println("You are placing a value greater than the max list items");
					return;
				}
			}
		}
	}

	public void clearItems() {
		for (int i = 0; i < 12; i++)
			player.getPA().sendItemOnInterface(47332, -1, i, -1, -1);
		player.getPA().sendItemOnInterface(47263, -1, 0, -1,-1);
	}

	public void displayItems(int buttonId) {
		clearItems();
		for (UpgradeData data : data) {
			if (data.getType() == player.getUpgradeType()) {
				if (buttonId == data.getClickId()) {
					for (int i = 0; i < data.getIngredients().length; i++)
						player.getPA().sendItemOnInterface(47332, data.getIngredients()[i].getId(), i,
								data.getIngredients()[i].getAmount(), data.getIngredients()[i].getBonus());
					player.getPA().sendItemOnInterface(47263, data.getResultItem(), 0, 1, -1);
					player.getPA().sendItemOnInterface(47278, data.getSafeItem(), 0, 1, -1);
				}
			}
		}
	}

	public boolean button(int buttonId) {
		for (UpgradeData data : data) {
			if (data.getType() == player.getUpgradeType()) {
				if (buttonId == data.getClickId()) {
					player.setCurrentUpgrade(data);
					// player.sendMessage("Selected: "+data);
					player.getPA().sendFrame126("Required Level: " + data.getCurrencyAmount() + "", 47262);
					displayItems(buttonId);
					return true;
				}
			}
		}
		return false;
	}

	public void upgrade() {
		if (player.getCurrentUpgrade() == null) {
			player.sendMessage("Please select a recipe first.");
		} else {
			for (UpgradeData data : data) {
				if (player.getSkillManager().getCurrentLevel(Skill.HERBLORE) < player.getCurrentUpgrade().getCurrencyAmount()) {
					DialogueManager.sendStatement(player, "You need a Herblore level of " + player.getCurrentUpgrade().getCurrencyAmount() + " to concoct this recipe.");
				return;
					}
				}
			for (int i = 0; i < player.getCurrentUpgrade().getIngredients().length; i++) {
				if (player.getInventory().getAmount(player.getCurrentUpgrade().getIngredients()[i]
								.getId()) < player.getCurrentUpgrade().getIngredients()[i].getAmount()) {
					player.sendMessage("You don't have the required ingredients to concoct "
							+ ItemDefinition.forId(player.getCurrentUpgrade().getResultItem()).getName() + ".");
					return;
				}
			}
			int randomInt = Misc.random(100) + 1;
			if (randomInt <= player.getCurrentUpgrade().getSuccessRate()) {
				for (int k = 0; k < player.getCurrentUpgrade().getIngredients().length; k++) {
					player.getInventory().delete(player.getCurrentUpgrade().getIngredients()[k].getId(),
							player.getCurrentUpgrade().getIngredients()[k].getAmount());
				}
			} else {
				for (int k = 0; k < player.getCurrentUpgrade().getIngredients().length; k++) {
					if (player.getCurrentUpgrade().getIngredients()[k].getId() != player.getCurrentUpgrade()
							.getSafeItem())
						player.getInventory().delete(player.getCurrentUpgrade().getIngredients()[k].getId(),
								player.getCurrentUpgrade().getIngredients()[k].getAmount());
				}
			}
			player.getSkillManager().addExperience(Skill.HERBLORE, player.getCurrentUpgrade().getOtherCurrency());
			if (player.getGameMode() == GameMode.VETERAN_MODE) {
				player.getSkillManager().addExperience(Skill.HERBLORE, player.getCurrentUpgrade().getOtherCurrency() * 100);
			}
			player.sendMessage("Congratulations, you successfully concoct "
					+ ItemDefinition.forId(player.getCurrentUpgrade().getResultItem()).getName() + ".");
			player.getInventory().add(player.getCurrentUpgrade().getResultItem(), 1);
			player.getSeasonPass().incrementExp(1000);
			// }
		}
	}
}
