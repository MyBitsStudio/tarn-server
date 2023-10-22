package com.ruse.world.content.taskscrolls;

import com.google.common.collect.ImmutableList;
import com.ruse.model.Item;
import com.ruse.model.container.impl.Equipment;
import org.apache.commons.lang3.Range;

import java.security.SecureRandom;

import static com.ruse.world.content.taskscrolls.TaskScrollConstants.*;

public enum TaskType {
    EASY(Range.between(10,25),
            new GearRestriction.Builder()
                    .addRestriction(Equipment.WEAPON_SLOT, 4151)
                    .addRestriction(Equipment.AMULET_SLOT, 6585)
                    .addRestriction(Equipment.CAPE_SLOT, 6461)
                    .build(), 0, new Item[] {
            new Item(4151,1),
            new Item(4151,1)
    }, EASY_TASK_SCROLL_ID, 1,2,3,4,5),

    MEDIUM(Range.between(15,30),
            new GearRestriction.Builder()
                    .addRestriction(Equipment.WEAPON_SLOT, 4151)
                    .addRestriction(Equipment.AMULET_SLOT, 6585)
                    .build(), 1, new Item[] {
            new Item(4151,1),
            new Item(4151,1)
    }, MEDIUM_TASK_SCROLL_ID, 1,2,3,4,5),

    HARD(Range.between(20,35),
            new GearRestriction.Builder()
                    .addRestriction(Equipment.WEAPON_SLOT, 4151)
                    .addRestriction(Equipment.AMULET_SLOT, 6585)
                    .build(), 2, new Item[] {
            new Item(4151,1),
            new Item(4151,1)
    }, HARD_TASK_SCROLL_ID, 1,2,3,4,5),

    ELITE(Range.between(25,40),
            new GearRestriction.Builder()
                    .addRestriction(Equipment.WEAPON_SLOT, 4151)
                    .addRestriction(Equipment.AMULET_SLOT, 6585)
                    .build(), 3, new Item[] {
            new Item(4151,1),
            new Item(4151,1)
    }, ELITE_TASK_SCROLL_ID, 1,2,3,4,5);

    private final Range<Integer> range;
    private final int[] npcs;
    private final GearRestriction gearRestrictions;
    private final int key;
    private final Item[] rewards;
    private final int taskScrollItemId;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final ImmutableList<TaskType> VALUES = ImmutableList.copyOf(values());

    TaskType(Range<Integer> range, GearRestriction gearRestrictions, int key, Item[] rewards, int taskScrollItemId, int... npcs) {
        this.range = range;
        this.gearRestrictions = gearRestrictions;
        this.key = key;
        this.rewards = rewards;
        this.taskScrollItemId = taskScrollItemId;
        this.npcs = npcs;
    }

    public int[] getNpcs() {
        return npcs;
    }

    public Range<Integer> getRange() {
        return range;
    }

    public GearRestriction getGearRestrictions() {
        return gearRestrictions;
    }

    public int getKey() {
        return key;
    }

    public Item[] getRewards() {
        return rewards;
    }

    public int getTaskScrollItemId() {
        return taskScrollItemId;
    }

    public int getRandomNpc() {
        return npcs[secureRandom.nextInt(npcs.length)];
    }

    public int getRandomNpcKillRequiredAmount() {
        return secureRandom.nextInt(range.getMinimum(), range.getMaximum());
    }

    public static TaskType getTypeByKey(int key) {
        return VALUES.stream().filter(it -> it.key == key).findFirst().orElse(null);
    }
}
