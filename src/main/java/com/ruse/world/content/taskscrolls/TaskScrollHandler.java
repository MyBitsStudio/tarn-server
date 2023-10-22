package com.ruse.world.content.taskscrolls;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;

import static com.ruse.world.content.taskscrolls.TaskScrollConstants.*;

public class TaskScrollHandler {

    public static void openTaskBottle(Player player, int itemId) {
        if(player.getPlayerTask() != null) {
            player.getPacketSender().sendMessage("@red@You already have a task.");
            return;
        }
        TaskType taskType = getTaskTypeByBottle(itemId);
        int[] gearRestrictions = taskType.getGearRestrictions().getRandomRestrictions();
        int npcToKill = taskType.getRandomNpc();
        int requiredAmount = taskType.getRandomNpcKillRequiredAmount();
        int key = taskType.getKey();
        player.getInventory().delete(itemId, 1).add(new Item(taskType.getTaskScrollItemId(), 1));
        player.setPlayerTask(new PlayerTask(gearRestrictions, key, npcToKill, requiredAmount));
    }

    public static void openTaskScroll(Player player) {
        if(player.getPlayerTask() == null) {
            return;
        }
        int key = player.getPlayerTask().getTaskKeyType();
        TaskType taskType = TaskType.getTypeByKey(key);
    }

    private static TaskType getTaskTypeByBottle(int itemId) {
        return switch (itemId) {
            case EASY_TASK_BOTTLE_ID -> TaskType.EASY;
            case MEDIUM_TASK_BOTTLE_ID -> TaskType.MEDIUM;
            case HARD_TASK_BOTTLE_ID -> TaskType.HARD;
            case ELITE_TASK_BOTTLE_ID -> TaskType.ELITE;
            default -> null;
        };
    }
}
