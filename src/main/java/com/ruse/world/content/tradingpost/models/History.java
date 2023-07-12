package com.ruse.world.content.tradingpost.models;

import java.util.Date;

public record History(int itemId, int amount, int price, String seller, String buyer, long timestamp, Date date) {
}
