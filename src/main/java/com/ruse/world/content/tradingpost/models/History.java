package com.ruse.world.content.tradingpost.models;

public record History(int itemId, String itemEffect, int bonus, String itemRarity, int amount, int price, String seller, String buyer) {
}
