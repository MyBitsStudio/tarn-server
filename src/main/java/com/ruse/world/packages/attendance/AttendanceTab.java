package com.ruse.world.packages.attendance;

import com.google.common.collect.ImmutableList;
import com.ruse.model.Item;

import java.time.Month;

public enum AttendanceTab {
    LOYAL(0,
            new MonthlyReward(Month.SEPTEMBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.OCTOBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.NOVEMBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.DECEMBER,
                    new Item(6585, 1))),
    CHRISTMAS(1,
            new MonthlyReward(Month.SEPTEMBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.OCTOBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.NOVEMBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.DECEMBER,
                    new Item(6585, 1))),

    EVENT(2,
            new MonthlyReward(Month.SEPTEMBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.OCTOBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.NOVEMBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.DECEMBER,
                    new Item(6585, 1))),
    DONATOR(3,
            new MonthlyReward(Month.SEPTEMBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.OCTOBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.NOVEMBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.DECEMBER,
                    new Item(6585, 1)))
    ,
    FALL(4,
            new MonthlyReward(Month.SEPTEMBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.OCTOBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.NOVEMBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.DECEMBER,
                    new Item(6585, 1)))
    ,

    ;
    
    private final int key;
    private final MonthlyReward[] monthlyReward;

    AttendanceTab(int key, MonthlyReward... monthlyReward) {
        this.key = key;
        this.monthlyReward = monthlyReward;
    }

    public int getKey() {
        return key;
    }

    public MonthlyReward[] getMonthlyReward() {
        return monthlyReward;
    }
}
