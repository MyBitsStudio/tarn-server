package com.ruse.world.packages.tower.props;

import com.ruse.model.Locations;
import lombok.Getter;

@Getter
public enum TowerLocations {

    TIER_1(0, Locations.Location.TOWER_1),
    TIER_2(1, Locations.Location.TOWER_1),

    ;

    private final int tier;
    private final Locations.Location location;
    TowerLocations(int tier, Locations.Location location){
        this.tier = tier;
        this.location = location;
    }

    public static TowerLocations get(int tier){
        for(TowerLocations tower : values()){
            if(tower.getTier() == tier){
                return tower;
            }
        }
        return null;
    }
}
