package com.ruse.world.packages.tracks;

import com.ruse.world.entity.impl.player.Player;

import java.util.Date;

public abstract class Track {

    protected Player player;
    protected Date endDate;

    protected int season, position, maxLevel;
    protected boolean hasPremium, premium;

    protected TrackRewards rewards;


    public Track(Player player){
        this.player = player;
    }


}
