package com.ruse.world.packages.tracks.impl.tarn;

import com.ruse.world.packages.tracks.ProgressReward;
import com.ruse.world.packages.tracks.TrackRewards;

public class TarnTrackRewards extends TrackRewards {
    @Override
    public void loadRewards() {
        rewards.add(new ProgressReward(1, 5, 995, 1000, -1, -1));
    }
}
