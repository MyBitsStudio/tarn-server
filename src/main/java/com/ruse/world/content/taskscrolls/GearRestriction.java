package com.ruse.world.content.taskscrolls;

import java.util.HashSet;
import java.util.LinkedHashMap;

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
}
