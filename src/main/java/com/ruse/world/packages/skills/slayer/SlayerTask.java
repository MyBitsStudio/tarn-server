package com.ruse.world.packages.skills.slayer;

import lombok.Getter;

@Getter
public class SlayerTask {

    private int id, amount, slayed;

    public SlayerTask(int id, int amount){
        this.id = id;
        this.amount = amount;
        this.slayed = 0;
    }

    public void increment(){
        this.slayed++;
    }

    public boolean isComplete(){
        return this.slayed >= this.amount;
    }

    public void reset(){
        this.slayed = 0;
    }

}
