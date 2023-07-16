package com.ruse.world.packages.tracks;

import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class ProgressReward {

    private final int level, xp, item, amount, premiumItem, premiumAmount;
    private boolean claimed = false, premiumClaimed = false;

    public ProgressReward(int level, int xp, int item, int amount, int premiumItem, int premiumAmount){
        this.level = level;
        this.xp = xp;
        this.item = item;
        this.amount = amount;
        this.premiumItem = premiumItem;
        this.premiumAmount = premiumAmount;
    }

    public void claim(){
        claimed = true;
    }

    public void claimPremium(@NotNull Player player){
        premiumClaimed = true;
        player.getPacketSender().sendMessage("You have unlocked a premium reward!");
    }
}
