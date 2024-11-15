package com.ruse.world.content.skill.impl.prayer;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Animation;
import com.ruse.model.Graphic;
import com.ruse.model.Skill;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.input.impl.EnterAmountOfBonesToSacrifice;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;

public class BonesOnAltar {

	public static void openInterface(Player player, int itemId) {
		player.getNewSkills().stopSkilling();
		player.setSelectedSkillingItem(itemId);
		player.setInputHandling(new EnterAmountOfBonesToSacrifice());
		player.getPacketSender().sendString(2799, ItemDefinition.forId(itemId).getName())
				.sendInterfaceModel(1746, itemId, 150).sendChatboxInterface(4429);
		player.getPacketSender().sendString(2800, "How many would you like to offer?");
	}

	public static void offerBones(final Player player, final int amount) {
		final int boneId = player.getSelectedSkillingItem();
		player.getNewSkills().stopSkilling();
		final BonesData currentBone = BonesData.forId(boneId);
		if (currentBone == null)
			return;
		player.getPacketSender().sendInterfaceRemoval();
		player.setCurrentTask(new Task(2, player, true) {
			int amountSacrificed = 0;

			@Override
			public void execute() {
				if (amountSacrificed >= amount) {
					stop();
					return;
				}
				if (!player.getInventory().contains(boneId)) {
					player.getPacketSender()
							.sendMessage("You have run out of " + ItemDefinition.forId(boneId).getName() + ".");
					stop();
					return;
				}
				if (player.getInteractingObject() != null) {
					player.setPositionToFace(player.getInteractingObject().getPosition().copy());
					player.getInteractingObject().performGraphic(new Graphic(624));
				}
				amountSacrificed++;
				player.getInventory().delete(boneId, 1);
				player.performAnimation(new Animation(713));

				AchievementHandler.progress(player, 1, 96, 97, 98, 99, 100);


			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.getPacketSender().sendMessage("You have pleased Crimson with your "
						+ (amountSacrificed == 1 ? "sacrifice" : "sacrifices") + ".");
			}
		});
		TaskManager.submit(player.getCurrentTask());
	}
}
