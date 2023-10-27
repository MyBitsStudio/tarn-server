package com.ruse.world.packages.taskscrolls;

import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;

import java.util.stream.Stream;

import static com.ruse.world.packages.taskscrolls.TaskScrollConstants.*;

public class TaskScrollHandler {

    public static boolean openTaskBottle(Player player, int itemId) {
        if(!World.attributes.getSetting("scrolls")) {
            player.getPacketSender().sendMessage("Task scrolls are currently disabled.");
            return false;
        }
        TaskType taskType = getTaskTypeByBottle(itemId);
        if(taskType == null) {
            return false;
        }
        int[] gearRestrictions = taskType.getGearRestrictions().getRandomRestrictions(taskType.getRestrictedAmount());
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
        return player.getPlayerTask() == null ? openTaskBottle(player, itemId) : openTaskScroll(player, itemId);
    }

    public static boolean handleButtonClick(Player player, int btnId) {
        if(btnId == 167656) {
            claimRewards(player);
            return true;
        } else if(btnId == 167657) {
            showProgressOverlayInterface(player);
            return true;
        } else if(btnId == 167653){
            player.getPacketSender().sendInterfaceRemoval();
            return true;
        }
        return false;
    }

    public static void claimRewards(Player player) {
        if(!World.attributes.getSetting("scrolls")) {
            player.getPacketSender().sendMessage("Task scrolls are currently disabled.");
            return;
        }
        PlayerTask playerTask = player.getPlayerTask();
        if(playerTask == null) {
            return;
        }
        if(playerTask.isComplete()) {
            int key = playerTask.getTaskKeyType();
            TaskType taskType = TaskType.getTypeByKey(key);
            Item[] rewards = taskType.getRewards();
            player.getInventory().delete(taskType.getTaskScrollItemId(), 1);
            Item[] randomItems = Stream.generate(() -> Misc.randomElement(rewards)).limit(2).toArray(Item[]::new);
            for(Item reward : randomItems) player.addItemUnderAnyCircumstances(reward);
            player.setPlayerTask(null);
            if(player.isHasPlayerTaskTracker()){
                showProgressOverlayInterface(player);
            }
        } else {
            player.getPacketSender().sendMessage("@red@You haven't completed your task yet.");
        }
    }

    private static void showTaskScrollInterface(Player player) {
        if(!World.attributes.getSetting("scrolls")) {
            player.getPacketSender().sendMessage("Task scrolls are currently disabled.");
            return;
        }
        PlayerTask playerTask = player.getPlayerTask();
        int key = playerTask.getTaskKeyType();
        TaskType taskType = TaskType.getTypeByKey(key);
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Your task is to kill @yel@")
                .append(playerTask.getCompletionAmount())
                .append("@lre@ of ")
                .append(NpcDefinition.forId(playerTask.getNpcTaskId()).getName())
                .append(" while wearing ");

        int[] restrictedWears = playerTask.getRestrictedWears();
        for(int i = 0; i < restrictedWears.length; i++) {
            stringBuilder.append("@red@").append(ItemDefinition.forId(restrictedWears[i]).getName());
            if(i != restrictedWears.length-1) {
                stringBuilder.append("@lre@ and ");
            }
        }

        stringBuilder.append("@lre@.");
        Item[] rewards = taskType.getRewards();
        int compAmount = playerTask.getCompletionAmount();
        int currentAmount = playerTask.getProgress();
        player.getPacketSender().sendString(167664, stringBuilder.toString())
                .sendItemContainer(rewards, 167663)
                .setScrollBar(167662, Math.max(209,(rewards.length / 3) * 40))
                .updateProgressBar(167658, (int) (getPercentageDone(playerTask)))
                .sendString(167661, currentAmount + "/" + compAmount)
                .sendInterface(167650);
    }

    private static void showProgressOverlayInterface(Player player) {
        player.setHasPlayerTaskTracker(!player.isHasPlayerTaskTracker());
        player.getPacketSender().sendWalkableInterface(167665, player.isHasPlayerTaskTracker());
    }

    public static float getPercentageDone(PlayerTask playerTask) {
        int compAmount = playerTask.getCompletionAmount();
        int currentAmount = playerTask.getProgress();
        return (((float) currentAmount / compAmount) * 100);
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
