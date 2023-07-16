package com.ruse.world.packages.tracks.impl.starter;

import com.ruse.world.packages.tracks.ProgressReward;
import com.ruse.world.packages.tracks.TrackRewards;

public class StarterTrackRewards extends TrackRewards {
    @Override
    public void loadRewards() {
        rewards.add(new ProgressReward(1, 0, 995, 1000000, -1, -1));
    }
}
