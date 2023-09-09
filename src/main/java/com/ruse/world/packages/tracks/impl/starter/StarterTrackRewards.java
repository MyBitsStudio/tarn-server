package com.ruse.world.packages.tracks.impl.starter;

import com.ruse.world.packages.tracks.ProgressReward;
import com.ruse.world.packages.tracks.TrackRewards;

public class StarterTrackRewards extends TrackRewards {
    @Override
    public void loadRewards() {
        rewards.add(new ProgressReward(1, 0, 20501, 2, -1, -1));
        rewards.add(new ProgressReward(2, 0, 20502, 2, -1, -1));
        rewards.add(new ProgressReward(3, 0, 23250, 1, -1, -1));
        rewards.add(new ProgressReward(4, 0, 23166, 1, -1, -1));
        rewards.add(new ProgressReward(5, 0, 23335, 5, -1, -1));
    }
}
