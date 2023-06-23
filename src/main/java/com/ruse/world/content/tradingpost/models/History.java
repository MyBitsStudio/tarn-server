package com.ruse.world.content.tradingpost.models;

public record History(int itemId, String itemEffect, int bonus, int amount, int price, String seller, String buyer) {
}
