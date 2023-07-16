package com.ruse.world.packages.tracks;

import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class Track {

    protected Player player;
    protected Date endDate;

    protected int season, position, maxLevel, xp;
    protected boolean hasPremium, premium;

    protected TrackRewards rewards;


    public Track(Player player){
        this.player = player;
    }

    public abstract void sendInterfaceList();



}
