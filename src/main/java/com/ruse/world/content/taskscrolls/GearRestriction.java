package com.ruse.world.content.taskscrolls;

import com.ruse.util.Misc;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.ruse.world.content.taskscrolls.TaskScrollConstants.RESTRICTED_AMOUNT;

public record GearRestriction(LinkedHashMap<Integer, HashSet<Integer>> gearRestrictionsMap) {
    public static class Builder {
        private final LinkedHashMap<Integer, HashSet<Integer>> map = new LinkedHashMap<>();

        public Builder addRestriction(int slot, int... itemId) {
            HashSet<Integer> set = map.computeIfAbsent(slot, x -> new HashSet<>());
            for (int ints : itemId) {
                set.add(ints);
            }
            return this;
        }

        public GearRestriction build() {
            return new GearRestriction(map);
        }
    }

    public int[] getRandomRestrictions() {
        int[] restrictedWearIds = new int[RESTRICTED_AMOUNT];
        AtomicInteger counter = new AtomicInteger();
        Stream.generate(() -> gearRestrictionsMap.get(Misc.random(gearRestrictionsMap.size())))
                .filter(Objects::nonNull)
                .distinct()
                .limit(RESTRICTED_AMOUNT)
                .toList()
                .forEach(integers -> restrictedWearIds[counter.getAndIncrement()] = Misc.random(integers));
        return restrictedWearIds;
    }
}
