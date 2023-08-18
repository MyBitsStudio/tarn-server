package com.ruse.world.entity.impl.player.timers;

import com.ruse.model.Timer;
import com.ruse.world.entity.impl.player.timers.impl.potion.InfiniteHealing1;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum PlayerTimer {

    INFINITE_HEALING_1("inf-heal-1",new InfiniteHealing1()),

    ;

    private final Timer timerClass;
    private final String name;

    PlayerTimer(String name, Timer timerClass){
        this.name = name;
        this.timerClass = timerClass;
    }

    public static @Nullable PlayerTimer getTimer(String name) {
    	for(PlayerTimer timer : values()) {
    		if(timer.getName().equals(name)) {
    			return timer;
    		}
    	}
    	return null;
    }

}
