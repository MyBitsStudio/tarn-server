package com.ruse.world.packages.attendance;

import com.google.common.collect.ImmutableList;
import com.ruse.model.Item;
import lombok.Getter;

import java.time.Month;

@Getter
public enum AttendanceTab {
    LOYAL(0,
            new MonthlyReward(Month.SEPTEMBER,
                    new Item(995, 10000),
                    new Item(13727, 50),
                    new Item(21815, 5),
                    new Item(21814, 5),
                    new Item(23149, 2),
                    new Item(4558, 10),
                    new Item(23335, 2),
                    new Item(995, 25000),
                    new Item(13727, 100),
                    new Item(21815, 10),
                    new Item(21814, 10),
                    new Item(23148, 2),
                    new Item(4559, 10),
                    new Item(23335, 3),
                    new Item(995, 50000),
                    new Item(13727, 150),
                    new Item(21815, 25),
                    new Item(21814, 25),
                    new Item(23147, 1),
                    new Item(4560, 10),
                    new Item(23335, 5),
                    new Item(995, 100000),
                    new Item(13727, 250),
                    new Item(21815, 35),
                    new Item(21814, 35),
                    new Item(23147, 2),
                    new Item(4564, 10),
                    new Item(23335, 7)
                    ),
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
                    new Item(995, 25000),
                    new Item(14819, 25),
                    new Item(28, 2),
                    new Item(14819, 50),
                    new Item(29, 2),
                    new Item(14819, 100),
                    new Item(995, 50000),
                    new Item(14819, 125),
                    new Item(28, 5),
                    new Item(14819, 150),
                    new Item(29, 5),
                    new Item(14819, 200)
            ),
            new MonthlyReward(Month.OCTOBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.NOVEMBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.DECEMBER,
                    new Item(6585, 1))),
    DONATOR(3,
            new MonthlyReward(Month.SEPTEMBER,
                    new Item(10835, 50),
                    new Item(20501, 2),
                    new Item(23254, 2),
                    new Item(22121, 5),
                    new Item(604, 2),
                    new Item(8788, 5),
                    new Item(15355, 2),
                    new Item(15356, 2),
                    new Item(15357, 2),
                    new Item(19001, 2),
                    new Item(22122, 5),
                    new Item(23107, 5),
                    new Item(23257, 2),
                    new Item(23258, 2)
                    ),
            new MonthlyReward(Month.OCTOBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.NOVEMBER,
                    new Item(6585, 1)),
            new MonthlyReward(Month.DECEMBER,
                    new Item(6585, 1)))
    ,
    FALL(4,
            new MonthlyReward(Month.SEPTEMBER,
                    new Item(10835, 100),
                    new Item(14819, 75),
                    new Item(28, 3),
                    new Item(14819, 150),
                    new Item(29, 3),
                    new Item(14819, 250),
                    new Item(10835, 150),
                    new Item(14819, 375),
                    new Item(28, 5),
                    new Item(14819, 500),
                    new Item(29, 5),
                    new Item(14819, 1000)),
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

}
