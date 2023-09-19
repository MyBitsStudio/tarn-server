package com.ruse.world.packages.tracks.impl.tarn.normal;

import com.ruse.world.packages.tracks.ProgressReward;
import com.ruse.world.packages.tracks.TrackRewards;

public class TarnNormalTrackRewards extends TrackRewards {
    @Override
    public void loadRewards() {
        rewards.add(new ProgressReward(1, 0, 23256, 2, -1, -1));
        rewards.add(new ProgressReward(2, 0, 23335, 5, -1, -1));
        rewards.add(new ProgressReward(3, 0, 23168, 5, -1, -1));
        rewards.add(new ProgressReward(4, 0, 19001, 10, -1, -1));
        rewards.add(new ProgressReward(5, 0, 23252, 1, -1, -1));
    }
}
