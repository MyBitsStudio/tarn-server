package com.ruse.world.packages.tracks.impl.starter;

import com.ruse.world.packages.tracks.ProgressReward;
import com.ruse.world.packages.tracks.TrackRewards;

public class StarterTrackRewards extends TrackRewards {
    @Override
    public void loadRewards() {
        rewards.add(new ProgressReward(1, 0, 995, 100000, -1, -1));
        rewards.add(new ProgressReward(2, 0, 995, 125000, -1, -1));
        rewards.add(new ProgressReward(3, 0, 995, 150000, -1, -1));
    }
}
