package com.ruse.world.content.taskscrolls;

import com.ruse.util.Misc;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public record GearRestriction(LinkedHashMap<Integer, HashSet<Integer>> restrictions) {
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

    public int[] getRandomRestrictions(int amount) {
        int[] restrictedWearIds = new int[amount];
        AtomicInteger counter = new AtomicInteger();
        Stream.generate(() -> restrictions.get(Misc.random(restrictions.size())))
                .filter(Objects::nonNull)
                .distinct()
                .limit(amount)
                .toList()
                .forEach(integers -> restrictedWearIds[counter.getAndIncrement()] = Misc.random(integers));
        return restrictedWearIds;
    }
}
