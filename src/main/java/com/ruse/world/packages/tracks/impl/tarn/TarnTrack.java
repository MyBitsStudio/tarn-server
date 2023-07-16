package com.ruse.world.packages.tracks.impl.tarn;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.tracks.Track;

public class TarnTrack extends Track {

    private TarnTask[] tasks = new TarnTask[3];
    public TarnTrack(Player player) {
        super(player);
        this.rewards = new TarnTrackRewards();
        this.hasPremium = true;
        this.position = 0;
        this.maxLevel = 50;
    }

    @Override
    public void sendInterfaceList() {

    }

    private void setTasks(){
        int rank = Math.floorDiv(position, 10);
        tasks = new TarnTask[3];
        for(int i = 0; i < 3; i++){


        }
    }

}
