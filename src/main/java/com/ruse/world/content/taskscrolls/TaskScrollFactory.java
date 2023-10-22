package com.ruse.world.content.taskscrolls;

import static com.ruse.world.content.taskscrolls.TaskScrollConstants.RESTRICTED_AMOUNTS;

public final class TaskScrollFactory {
    private TaskScrollFactory() {

    }

    public static PlayerTask newInstance(TaskType taskType) {
        int[] gearRestrictions = taskType.getRandomRestrictions(RESTRICTED_AMOUNTS);
        int npcToKill = taskType.getRandomNpc();
        int requiredAmount = taskType.getRandomNpcKillRequiredAmount();
        int key = taskType.getKey();
        return new PlayerTask(gearRestrictions, key, npcToKill, requiredAmount);
    }
}
