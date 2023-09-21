package com.ruse.world.packages.tracks.impl.tarn.elite;

import com.ruse.world.packages.tracks.ProgressReward;
import com.ruse.world.packages.tracks.TrackRewards;

public class TarnEliteTrackRewards extends TrackRewards {
    @Override
    public void loadRewards() {
        rewards.add(new ProgressReward(1, 0, 19001, 20, -1, -1));
        rewards.add(new ProgressReward(2, 0, 23256, 5, -1, -1));
        rewards.add(new ProgressReward(3, 0, 20502, 15, -1, -1));
        rewards.add(new ProgressReward(4, 0, 23255, 15, -1, -1));
        rewards.add(new ProgressReward(5, 0, 23257, 5, -1, -1));
        rewards.add(new ProgressReward(6, 0, 23258, 5, -1, -1));
        rewards.add(new ProgressReward(7, 0, 23250, 5, -1, -1));
        rewards.add(new ProgressReward(8, 0, 23521, 5, -1, -1));
        rewards.add(new ProgressReward(9, 0, 23252, 5, -1, -1));
        rewards.add(new ProgressReward(10, 0, 23259, 5, -1, -1));
    }
}
