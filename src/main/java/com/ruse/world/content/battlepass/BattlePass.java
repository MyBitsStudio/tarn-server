package com.ruse.world.content.battlepass;

import com.ruse.world.entity.impl.player.Player;

public class BattlePass {

    private Player player;
    private int tier = 1;
    private int xp;
    private boolean premium;

    public BattlePass(Player player) {
        this.player = player;
    }
    public void openInterface() {
        int tier = BattlePassData.getTierForXP(getXp());
        player.getPacketSender().sendString(105007, "XP: " + getXp() + "/" + BattlePassData.getXPForTier(tier));
        player.getPacketSender().sendString(105008, "" + tier);
        player.getPacketSender().sendString(105009, "SEASON " + BattlePassData.SEASON);
        int days = BattlePassHandler.getDaysRemaining();
        player.getPacketSender().sendString(105010, "Season Ends: " + days + " day" + (days > 1 ? "s" : ""));

        for(int i = 0; i < BattlePassData.values().length; i++) {
            BattlePassData data = BattlePassData.values()[i];
            if(tier <= i + 1)
                break;
            player.getPacketSender().sendItemOnInterface(105105 + (i * 8), data.getPremiumReward().getId(), data.getFreeReward().getAmount());
            player.getPacketSender().sendSpriteChange(105107 + (i * 8), 1113);
            if(!isPremium())
                continue;
            player.getPacketSender().sendItemOnInterface(105106 + (i * 8), data.getPremiumReward().getId(), data.getPremiumReward().getAmount());
            player.getPacketSender().sendSpriteChange(105108 + (i * 8), 1113);
        }

        player.getPacketSender().sendInterface(105000);
    }

    public void check() {
        int nextTier = BattlePassData.getTierForXP(getXp()) + 1;
        if(getXp() != BattlePassData.getXPForTier(nextTier) || nextTier > BattlePassData.values().length)
            return;
        player.sendMessage("You reached tier level " + nextTier + "!");
        int slot = isPremium() ? 2 : 1;
        if(player.getInventory().getFreeSlots() >= slot) {
            player.getInventory().addItemSet(BattlePassData.getRewardForTier(nextTier));
            return;
        }
        player.getBank(0).addItemSet(BattlePassData.getRewardForTier(nextTier));
    }

    public void reset() {
        setPremium(false);
        setXp(0);
    }


    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void incrementXP(int xp) {
        this.xp += xp;
        check();
    }

    public boolean isPremium() {
        return this.premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
}
