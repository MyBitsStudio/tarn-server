package com.ruse.world.content.battlepass;

import com.ruse.model.Item;

import java.util.stream.IntStream;

public enum BattlePassData {

    TIER_ONE(1, "Whip", new Item(4151), new Item(4152), 10),
    TIER_TWO(2, "Whip", new Item(4151), new Item(4152), 20);

    BattlePassData(int tier, String name, Item freeReward, Item premiumReward, int xpRequired) {
        this.tier = tier;
        this.name = name;
        this.freeReward = freeReward;
        this.premiumReward = premiumReward;
        this.xpRequired = xpRequired;
    }

    public static int SEASON = 1;

    private static int[] bosses = new int[] {50, 1265};

    private Item freeReward;

    private Item premiumReward;
    private String name;
    private int xpRequired;

    private int tier;


    public Item getFreeReward() {
        return this.freeReward;
    }

    public Item getPremiumReward() {
        return this.premiumReward;
    }

    public String getName() {
        return this.name;
    }

    public int getXPRequired() {
        return this.xpRequired;
    }

    public int getTier() {
        return this.tier;
    }

    public static int getXPForTier(int tier) {
        return tier >= BattlePassData.values().length ? BattlePassData.values()[BattlePassData.values().length - 1].getXPRequired() : BattlePassData.values()[tier - 1].getXPRequired();
    }

    public static int[] getBosses() {
        return bosses;
    }

    public static boolean isBoss(int npc) {
        return IntStream.of(getBosses()).anyMatch(b -> b == npc);
    }

    public static int getTierForXP(int xp) {
        BattlePassData last = BattlePassData.values()[BattlePassData.values().length - 1];
        if(xp > last.getXPRequired())
            return last.getTier();
        for(BattlePassData data : BattlePassData.values()) {
            if(data == null)
                continue;
            if(xp > data.getXPRequired())
                continue;
            return data.getTier();
        }
        return 0;
    }

    public static BattlePassData dataForTier(int tier) {
        for(BattlePassData data : BattlePassData.values()) {
            if(data == null)
                continue;
            if(data.getTier() == tier)
                return data;
        }
        return null;
    }

    public static Item[] getRewardForTier(int tier) {
        BattlePassData data = dataForTier(tier);
        return new Item[] {data.getFreeReward(), data.getPremiumReward()};
    }
}
