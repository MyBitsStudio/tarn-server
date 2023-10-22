package com.ruse.world.content.taskscrolls;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;

import static com.ruse.world.content.taskscrolls.TaskScrollConstants.*;

public class TaskScrollHandler {

    public static boolean openTaskBottle(Player player, int itemId) {
        TaskType taskType = getTaskTypeByBottle(itemId);
        if(taskType == null) {
            return false;
        }
        int[] gearRestrictions = taskType.getGearRestrictions().getRandomRestrictions();
        int npcToKill = taskType.getRandomNpc();
        int requiredAmount = taskType.getRandomNpcKillRequiredAmount();
        int key = taskType.getKey();
        player.getInventory().delete(itemId, 1).add(new Item(taskType.getTaskScrollItemId(), 1));
        player.setPlayerTask(new PlayerTask(gearRestrictions, key, npcToKill, requiredAmount));
        return true;
    }

    public static boolean openTaskScroll(Player player, int itemId) {
        if(itemId != EASY_TASK_SCROLL_ID && itemId != MEDIUM_TASK_SCROLL_ID && itemId != HARD_TASK_SCROLL_ID && itemId != ELITE_TASK_SCROLL_ID) {
            return false;
        }
        showTaskScrollInterface(player);
        return true;
    }

    public static boolean handleItemClick(Player player, int itemId) {
        return player.getPlayerTask() != null ? openTaskBottle(player, itemId) : openTaskScroll(player, itemId);
    }

    public static boolean handleButtonClick(Player player, int btnId) {
        if(btnId == 1) {
            claimRewards(player);
            return true;
        } else if(btnId == 2) {
            showProgressOverlayInterface(player);
            return true;
        }
        return false;
    }

    private static void claimRewards(Player player) {
        PlayerTask playerTask = player.getPlayerTask();
        if(playerTask == null) {
            return;
        }
        if(playerTask.isComplete()) {
            int key = playerTask.getTaskKeyType();
            TaskType taskType = TaskType.getTypeByKey(key);
            Item[] rewards = taskType.getRewards();
            player.getInventory().delete(taskType.getTaskScrollItemId(), 1);
            for(Item reward : rewards) player.addItemUnderAnyCircumstances(reward);
            showRewardsInterface(rewards);
            player.setPlayerTask(null);
        }
    }

    private static void showRewardsInterface(Item[] items) {

    }

    private static void showTaskScrollInterface(Player player) {
        int key = player.getPlayerTask().getTaskKeyType();
        TaskType taskType = TaskType.getTypeByKey(key);
    }

    private static void showProgressOverlayInterface(Player player) {

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
