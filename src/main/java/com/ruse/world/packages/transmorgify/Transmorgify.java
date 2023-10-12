package com.ruse.world.packages.transmorgify;

import com.ruse.model.Flag;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Transmorgify {

    @Getter
    private final List<Transformations> transformations = new ArrayList<>();

    private Player player;

    @Getter
    private Transformations currentTransformation;

    public Transmorgify(Player player){
        this.player = player;
    }

    public void addTransformation(Transformations transformation){
        transformations.add(transformation);
    }

    public void load(List<Transformations> transformations){
        this.transformations.addAll(transformations);
    }

    public void returnToNormal(){
        if(currentTransformation != null){
            currentTransformation = null;
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }
    }

    public void transmorgify(Transformations trans){
        if(currentTransformation != null){
            currentTransformation = null;
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }

        currentTransformation = trans;
        player.getStrategy(trans.getNpcId());
        player.getUpdateFlag().flag(Flag.APPEARANCE);
    }

}
