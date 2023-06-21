package com.ruse.world.packages.vip;

import com.ruse.model.Item;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class Donation {

    private final int[] item;
    private final int amount;
    private final String date;

    public Donation(int[] item, int amount, long date) {
        this.item = item;
        this.amount = amount;
        this.date = longToDate(date);
    }

    private @NotNull String longToDate(long dates){
        Date date = new Date(dates);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
