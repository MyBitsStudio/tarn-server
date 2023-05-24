package com.ruse.world.content.bosses;

import com.ruse.model.Locations;

public class BossLocations extends Locations {

    public enum BossLocation {
        L;

        private int[] positions;
        private boolean multi, follow, tele, cannon;

        BossLocation(){

        }
    }
}
