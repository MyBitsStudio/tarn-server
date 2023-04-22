package com.ruse.world.content.seasonpass;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import lombok.RequiredArgsConstructor;

import java.util.BitSet;
import java.util.HashMap;

@RequiredArgsConstructor
public class SeasonPass {

    private static final int REWARD_AMOUNT = 100;
    private static final byte MAX_REWARDS = 50;
    private static final byte PREMIUM_OFFSET = 50;
    public static final HashMap<String, Integer> EXP_MAP = new HashMap<>();
    public static SPLevel[] levels;

    private int passState;
    private final BitSet rewardsClaimed = new BitSet(REWARD_AMOUNT);
    private final Player player;

    private boolean hasPremium() {
        return (passState & 0x1) != 0;
    }

    private void enablePremium() {
        passState |= 0x1;
    }

    private void disablePremium() {
        passState &= ~0x1;
    }

    private int getLevel() {
        return (passState >> 1) & 0xff;
    }

    private void setLevel(int level) {
        passState = (passState & 0xFFFFFF01) | ((level << 1) & 0xFE);
    }

    private int getExp() {
        return (passState & 0xFFFFFE00) >>> 1;
    }

    private void setExp(int exp) {
        passState = (passState & 0x1) | ((getLevel() << 9) & 0x1FE) | ((exp << 1) & 0xFFFFFE00);
    }

    private int getMaxPassLevel() {
        return levels.length;
    }

    private void levelUp(int overflow) {
        int currLevel = getLevel();
        if(currLevel == getMaxPassLevel()) {
            return;
        }
        setLevel(getLevel() + 1);
        setExp(overflow);
    }

    private SPLevel getSpLevel(int level) {
        return levels[level];
    }

    private void selectClaimReward() {
        for(int i = 0; i < MAX_REWARDS; i++) {
            if(getLevel() < i) {
                return;
            }
            if(!rewardsClaimed.get(i) || (hasPremium() && !rewardsClaimed.get(i + PREMIUM_OFFSET))) {
                claimReward(i);
            }
        }
    }

    private void claimReward(int tier) {
        Item[] items = new Item[2];
        SPLevel spLevel = levels[tier];
        if(!rewardsClaimed.get(tier)) {
            items[0] = new Item(spLevel.getFreeItemId(), spLevel.getFreeAmount());
        }
        if(hasPremium() && !rewardsClaimed.get(tier + PREMIUM_OFFSET)) {
            items[1] = new Item(spLevel.getPremiumItemId(), spLevel.getPremiumAmount());
        }
        for(int i = 0; i < items.length; i++) {
            Item item = items[i];
            if(item == null) {
                continue;
            }
            if(!player.getInventory().canHold(item)) {
                continue;
            }
            if(i == 0) {
                rewardsClaimed.set(tier);
            } else {
                rewardsClaimed.set(tier + PREMIUM_OFFSET);
            }
            player.getInventory().add(item);
        }
    }

    public boolean handleButtonClick(int buttonId) {

        return false;
    }

    public void incrementExp(int amount) {
        int exp = getExp() + amount;
        int expToNextLevel = getSpLevel(getLevel()).getExpNeeded();
        if(exp < expToNextLevel) {
            setExp(exp);
            return;
        } else if(exp == expToNextLevel) {
            levelUp(0);
            return;
        }
        int overflow = exp - expToNextLevel;
        levelUp(overflow);
    }
}
