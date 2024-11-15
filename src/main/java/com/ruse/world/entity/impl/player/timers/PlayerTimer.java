package com.ruse.world.entity.impl.player.timers;

import com.ruse.model.Timer;
import com.ruse.world.entity.impl.player.timers.impl.monic.*;
import com.ruse.world.entity.impl.player.timers.impl.potion.*;
import com.ruse.world.entity.impl.player.timers.impl.scroll.*;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum PlayerTimer {

    INFINITE_HEALING_1("inf-heal-1",new InfiniteHealing1()),
    INFINITE_HEALING_2("inf-heal-2",new InfiniteHealing2()),
    INFINITE_HEALING_3("inf-heal-3",new InfiniteHealing3()),
    INFINITE_OVERLOAD_1("inf-over-1",new InfiniteOverload1()),
    INFINITE_OVERLOAD_2("inf-over-2",new InfiniteOverload2()),
    INFINITE_OVERLOAD_3("inf-over-3",new InfiniteOverload3()),
    INFINITE_PRAYER_1("inf-pray-1",new InfinitePrayer1()),
    INFINITE_PRAYER_2("inf-pray-2",new InfinitePrayer2()),
    INFINITE_PRAYER_3("inf-pray-3",new InfinitePrayer3()),
    INFINITE_PRAYER("inf-pray",new InfinitePrayer()),
    INFINITE_RAGE("inf-rage-1",new InfiniteRage()),

    DOUBLE_DR_HOUR("DoubleDRH", new DoubleDRHour()),
    DOUBLE_DDR_HOUR("DoubleDDRH", new DoubleDDRHour()),
    DOUBLE_DMG_HOUR("DoubleDMGH", new DoubleDamageHour()),
    DOUBLE_DR_30("DoubleDR30", new DoubleDRHalf()),
    DOUBLE_DMG_30("DoubleDMG30", new DoubleDamageHalf()),

    VOTE_XP("VoteXP", new VoteXP()),
    VOTE_DR("VoteDR", new VoteDR()),

    MONIC_DDR("MonicDDDRH", new DDDRMonicHour()),
    MONIC_DMG("MonicDMGH", new DDMGMonicHour()),
    MONIC_DR("MonicDDRH", new DDRMonicHour()),
    MONIC_PRAYER("MonicPrayerH", new PrayerMonicHour()),
    MONIC_XP("MonicXpH", new DXPMonicHour()),
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
