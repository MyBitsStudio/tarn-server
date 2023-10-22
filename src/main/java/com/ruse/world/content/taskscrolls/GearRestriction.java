package com.ruse.world.content.taskscrolls;

import com.ruse.util.Misc;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.ruse.world.content.taskscrolls.TaskScrollConstants.RESTRICTED_AMOUNTS;

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
        LinkedHashMap<Integer, HashSet<Integer>> map = gearRestrictionsMap;
        int size = map.size();
        List<HashSet<Integer>> sets = Stream.generate(() -> {
                    int randomIndex = Misc.random(size);
                    Integer key = map.keySet().stream().skip(randomIndex).findFirst().orElse(null);
                    if (key != null) {
                        return map.get(key);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .distinct()
                .limit(RESTRICTED_AMOUNTS)
                .toList();
        int[] restrictedWearIds = new int[RESTRICTED_AMOUNTS];
        for(int i = 0; i < RESTRICTED_AMOUNTS; i++) {
            restrictedWearIds[i] = Misc.random(sets.get(i));
        }
        return restrictedWearIds;
    }
}
