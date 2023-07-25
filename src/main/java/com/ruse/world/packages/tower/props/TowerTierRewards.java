package com.ruse.world.packages.tower.props;

import com.ruse.model.Item;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum TowerTierRewards {

    T1(0, new Item(15004, 3)),
    T2(1, new Item(10835, 100)),
    T3(0, new Item(20301, 1)),

    ;

    private final int tier;
    private final Item[] rewards;
    TowerTierRewards(int tier, Item... rewards){
        this.tier = tier;
        this.rewards = rewards;
    }

    public static @Nullable TowerTierRewards get(int tier){
        for(TowerTierRewards tower : values()){
            if(tower.getTier() == tier){
                return tower;
            }
        }
        return null;
    }
}
