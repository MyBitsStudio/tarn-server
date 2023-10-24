package com.ruse.world.packages.taskscrolls;

import com.google.common.collect.ImmutableList;
import com.ruse.model.Item;
import com.ruse.model.container.impl.Equipment;
import org.apache.commons.lang3.Range;

import java.security.SecureRandom;

import static com.ruse.world.packages.taskscrolls.TaskScrollConstants.*;

public enum TaskType {
    EASY(Range.between(20,40),
            new GearRestriction.Builder()
                    .addRestriction(Equipment.WEAPON_SLOT, 19993, 5012, 9942, 18629, 19998, 21018, 8088, 17011, 23055)
                    .addRestriction(Equipment.AMULET_SLOT, 19991, 21068, 20092)
                    .addRestriction(Equipment.LEG_SLOT, 19986, 21064, 4686, 20088, 21044, 21017, 21038, 18003, 23052)
                    .addRestriction(Equipment.CAPE_SLOT, 20400, 21071, 9939, 21045, 21039, 18005)
                    .addRestriction(Equipment.BODY_SLOT, 19985, 21063, 4685, 20087, 21043, 21016, 21037, 18001, 23051)
                    .addRestriction(Equipment.HEAD_SLOT, 19984, 4684, 20086, 21042, 21015, 21036, 17999, 23050)
                    .addRestriction(Equipment.SHIELD_SLOT, 19987, 23056)
                    .addRestriction(Equipment.HANDS_SLOT, 19988, 21066, 8273, 20091, 21046, 21040, 23053)
                    .addRestriction(Equipment.FEET_SLOT, 19989, 21067, 8274, 20089, 21047, 21041, 18009, 23054)
                    .addRestriction(Equipment.RING_SLOT, 19992, 21069, 20093)
                    .build(), 0, new Item[] {
            new Item(995,10000), new Item(995,25000), new Item(995,50000),
            new Item(13727,50), new Item(13727,100), new Item(13727,250),
            new Item(19000,10), new Item(19000,25), new Item(19000,50),
            new Item(19002,20), new Item(10946,1), new Item(10946,5),
            new Item(15690,10), new Item(15689,5),
            new Item(15358,1), new Item(15359,1), new Item(23149,3),
            new Item(23149,5), new Item(23148,3)
    }, EASY_TASK_SCROLL_ID, 2, 9837, 9027, 9835, 9911, 9922, 8014, 8003, 811, 9817, 9836, 92, 3313, 1906, 1742, 1743, 1744, 1745, 1738, 1739, 1740, 1741, 9025, 9026, 8008, 2342),

    MEDIUM(Range.between(40,75),
            new GearRestriction.Builder()
                    .addRestriction(Equipment.WEAPON_SLOT, 23079, 14924, 14919, 17720)
                    .addRestriction(Equipment.LEG_SLOT, 23077, 23129, 23136, 23141)
                    .addRestriction(Equipment.CAPE_SLOT, 23133)
                    .addRestriction(Equipment.BODY_SLOT, 23076, 23128, 23135, 23140)
                    .addRestriction(Equipment.HEAD_SLOT, 23075, 23127, 23134, 23139)
                    .addRestriction(Equipment.SHIELD_SLOT, 23080)
                    .addRestriction(Equipment.HANDS_SLOT, 23130, 23137, 23142)
                    .addRestriction(Equipment.FEET_SLOT, 23131, 23138, 23143)
                    .build(), 1, new Item[] {
            new Item(995,20000), new Item(995,45000), new Item(995,75000),
            new Item(13727,75), new Item(13727,125), new Item(13727,300),
            new Item(19000,15), new Item(19000,40), new Item(19000,75),
            new Item(19002,30), new Item(10946,1), new Item(10946,5),
            new Item(15689,10), new Item(15691,5),
            new Item(15358,2), new Item(15359,2), new Item(23148,3),
            new Item(23148,5), new Item(23147,3)
    }, MEDIUM_TASK_SCROLL_ID, 2, 9839,9806,1746,4972,3021, 3305, 125),

    HARD(Range.between(75, 250),
            new GearRestriction.Builder()
                    .addRestriction(Equipment.WEAPON_SLOT, 23144, 23145, 23146, 14018, 8001, 17600, 15121, 14056,
                            22135, 22155, 22156, 22157, 3472, 4071, 3738, 8410, 8411, 8412, 17718, 13333, 22173, 22177,
                            23066, 23064, 22184, 22167, 22176, 8110, 22134, 14305)
                    .addRestriction(Equipment.LEG_SLOT, 8818, 19158, 11342, 11423, 15117, 20063, 15647, 22154,
                            3473, 4068, 3725, 17618, 5070, 13330, 8833, 23063, 22182, 22164, 21609, 8102, 8107, 14206)
                    .addRestriction(Equipment.CAPE_SLOT, 26, 3470, 17606, 8830, 22202, 23068, 22166)
                    .addRestriction(Equipment.BODY_SLOT, 8817, 19159, 11341, 11422, 15116, 20062, 15646, 22153,
                            4077, 4067, 3724, 17616, 5069, 13329, 8829, 23062, 22181, 22165, 21608, 8101, 8106, 14204)
                    .addRestriction(Equipment.HEAD_SLOT, 8816, 19160, 11340, 11421, 15115, 20060, 15645, 22152,
                            4075, 4066, 3723, 17614, 5068, 13328, 8828, 23061, 22183, 22163, 21607, 8100, 8105, 14202)
                    .addRestriction(Equipment.SHIELD_SLOT, 19800, 4072, 23067, 23065, 21610, 14307)
                    .addRestriction(Equipment.HANDS_SLOT, 15118, 20073, 22159, 4085, 4070, 5071, 22180, 22162,
                            21611, 8104, 8109, 14301)
                    .addRestriction(Equipment.FEET_SLOT, 15119, 22158, 4083, 4069, 5072, 13332, 22203, 22179,
                            22161, 21612, 8103, 8108, 14303)
                    .addRestriction(Equipment.RING_SLOT, 8831)
                    .build(), 2, new Item[] {
            new Item(10835,50), new Item(10835,100), new Item(10835,250),
            new Item(13727,100), new Item(13727,250), new Item(13727,500),
            new Item(19000,25), new Item(19000,75), new Item(19000,150),
            new Item(19002,50), new Item(10946,1), new Item(10946,5),
            new Item(10946,10), new Item(15693,10), new Item(15693,5),
            new Item(15694,10), new Item(15694,5), new Item(15698,3),
            new Item(15355,1), new Item(15356,1),  new Item(15357,1),
            new Item(23148,5), new Item(23149,3), new Item(23147,5)
    }, HARD_TASK_SCROLL_ID, 3, 9915, 9024, 8002, 8000, 9919, 3020, 9913, 1311, 1313, 9914, 185,
            188, 3117, 9800, 9802, 12843, 667, 5861, 440, 438, 3019),

    ELITE(Range.between(50, 200),
            new GearRestriction.Builder()
                    .addRestriction(Equipment.WEAPON_SLOT, 22254, 14343, 14349, 14355)
                    .addRestriction(Equipment.LEG_SLOT, 22246, 14347)
                    .addRestriction(Equipment.CAPE_SLOT, 22252, 14357)
                    .addRestriction(Equipment.BODY_SLOT, 22244, 14339)
                    .addRestriction(Equipment.HEAD_SLOT, 22242, 14359)
                    .addRestriction(Equipment.SHIELD_SLOT, 14351)
                    .addRestriction(Equipment.HANDS_SLOT, 22248, 14345)
                    .addRestriction(Equipment.FEET_SLOT, 22250, 14341)
                    .addRestriction(Equipment.ENCHANTMENT_SLOT, 14337)
                    .addRestriction(Equipment.RING_SLOT, 14353)
                    .addRestriction(Equipment.HALO_SLOT, 14361)
                    .addRestriction(Equipment.AMULET_SLOT, 14363)
                    .build(), 3, new Item[] {
            new Item(10835,250), new Item(10835,500), new Item(10835,1000),
            new Item(13727,500), new Item(13727,1000), new Item(13727,2500),
            new Item(19002,25), new Item(19002,75), new Item(19002,150),
            new Item(10946,5), new Item(10946,10), new Item(23058,1),
            new Item(15698,10), new Item(15700,5), new Item(15700,10),
            new Item(15696,5), new Item(23256,3), new Item(23256,5),
            new Item(23257,2), new Item(23257,4), new Item(23258,1),
            new Item(23258,3), new Item(23259,1), new Item(15355,5),
            new Item(15356,5), new Item(15357,5), new Item(23147,10),
            new Item(19641,1), new Item(9076,1), new Item(9076,3),
            new Item(9077,1)
    }, ELITE_TASK_SCROLL_ID, 4, 3010);

    private final Range<Integer> range;
    private final int[] npcs;
    private final GearRestriction gearRestrictions;
    private final int key;
    private final Item[] rewards;
    private final int taskScrollItemId;
    private final int restrictedAmount;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final ImmutableList<TaskType> VALUES = ImmutableList.copyOf(values());

    TaskType(Range<Integer> range, GearRestriction gearRestrictions, int key, Item[] rewards, int taskScrollItemId, int restrictedAmount, int... npcs) {
        this.range = range;
        this.gearRestrictions = gearRestrictions;
        this.key = key;
        this.rewards = rewards;
        this.taskScrollItemId = taskScrollItemId;
        this.restrictedAmount = restrictedAmount;
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

    public int getRestrictedAmount() {
        return restrictedAmount;
    }
}
