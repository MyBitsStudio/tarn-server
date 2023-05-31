package com.ruse.world.packages.seasonpass;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
@Setter
public class SeasonPass {

    private static final int INTERFACE_ID = 49450;
    public static final int REWARD_AMOUNT = 98;
    private static final byte MAX_LEVEL = 49;
    private static final byte PREMIUM_OFFSET = 49;
    public static SeasonPassLevel[] levels;

    private int season = SeasonPassConfig.getInstance().getSeason();
    private boolean premium;
    private int page;
    private int level;
    private int exp;
    private int totalExperience;
    private boolean[] rewardsClaimed = new boolean[REWARD_AMOUNT];
    private final Player player;

    private boolean hasPremium() {
        return premium;
    }

    public void setPremium(boolean set){
        this.premium = set;
    }

    private int getMaxPassLevel() {
        return levels.length-1;
    }

    private void levelUp(int overflow) {
        if(level == getMaxPassLevel()) {
            exp = getSpLevel(level).getExpNeeded();
            return;
        }
        level++;
        exp = 0;
        incrementExp(overflow, true);
    }

    private SeasonPassLevel getSpLevel(int level) {
        return levels[level];
    }

    private void selectClaimReward() {
        boolean hasClaim = false;
       for(int i = 0; i < MAX_LEVEL; i++) {
            if(canClaimFree(i) || canClaimPremium(i)) {
                if(!claimReward(i)) {
                    player.getPacketSender().sendMessage("@red@You need inventory spaces to claim.");
                    return;
                }
                hasClaim = true;
            }
        }
        if(!hasClaim) {
            player.getPacketSender().sendMessage("@red@You have no items to claim");
            if(!isPremium()) {
                player.getPacketSender().sendMessage("@red@Buy premium to unlock more rewards!");
            }
        }
    }

    private boolean canClaimFree(int tier) {
        return (level > tier || exp >= getSpLevel(level).getExpNeeded()) && !rewardsClaimed[tier];
    }

    private boolean canClaimPremium(int tier) {
        return (level > tier || exp >= getSpLevel(level).getExpNeeded()) && !rewardsClaimed[tier + PREMIUM_OFFSET] && isPremium();
    }

    private boolean hasClaimable() {
        for(int i = 0; i < MAX_LEVEL; i++) {
            if(canClaimFree(i) || canClaimPremium(i)) {
                return true;
            }
        }
        return false;
    }

    private boolean claimReward(int tier) {
        Item[] items = new Item[2];
        SeasonPassLevel spLevel = getSpLevel(tier);
        int spacesNeeded = 0;
        if(!rewardsClaimed[tier]) {
            items[0] = new Item(spLevel.getFreeItemId(), spLevel.getFreeAmount());
            spacesNeeded += items[0].getDefinition().isStackable() ? 1 : items[0].getAmount();
        }
        if(hasPremium() && !rewardsClaimed[tier + PREMIUM_OFFSET]) {
            items[1] = new Item(spLevel.getPremiumItemId(), spLevel.getPremiumAmount());
            spacesNeeded += items[1].getDefinition().isStackable() ? 1 : items[1].getAmount();
        }
        for(int i = 0; i < items.length; i++) {
            Item item = items[i];
            if(item == null) {
                continue;
            }
            if(player.getInventory().getFreeSlots() < spacesNeeded) {
                return false;
            }
            if(i == 0) {
                rewardsClaimed[tier] = true;
            } else {
                rewardsClaimed[tier + PREMIUM_OFFSET] = true;
            }
            player.getInventory().add(item);
            changeRewardPage();
        }
        return true;
    }

    public boolean handleButtonClick(int buttonId) {
        if(buttonId == -16080 && page != 6) {
            page++;
            changeRewardPage();
            return true;
        } else if(buttonId == -16077 && page > 0) {
            page--;
            changeRewardPage();
            return true;
        } else if(buttonId == -16071) {
            selectClaimReward();
            return true;
        } else if(buttonId == -16074) {
            // purchase premium button
            return true;
        }
        return false;
    }

    public void incrementExp(int amount, boolean isOverflow) {
        if(totalExperience == 0) {
            calculateTotalExperience();
        }
        if(!isOverflow) {
            totalExperience += amount;
        }
        exp += amount;
        int expToNextLevel = getSpLevel(getLevel()).getExpNeeded();
        if(exp == expToNextLevel) {
            levelUp(0);
        } else if(exp > expToNextLevel) {
            int overflow = exp - expToNextLevel;
            levelUp(overflow);
        }
    }

    public void calculateTotalExperience() {
        for(int i = 0; i < level; i++) {
            totalExperience += getSpLevel(i).getExpNeeded();
        }
    }

    public void showInterface() {
        page = 0;
        int level = getLevel();
        int expNeeded = getSpLevel(level).getExpNeeded();
        if(totalExperience == 0) {
            calculateTotalExperience();
        }
        changeRewardPage();
        player.getPacketSender()
                .sendString(49486, hasPremium() ? "Premium" : "Free")
                .sendString(49487, player.getUsername())
                .sendString(49488, String.valueOf(level+1))
                .sendString(49489, String.valueOf(Math.min(MAX_LEVEL, level+1)))
                .sendString(49490, String.valueOf(totalExperience))
                .sendString(49479, (level == getMaxPassLevel() ? String.valueOf(Math.min(expNeeded, exp)) : exp) + "/" + expNeeded)
                .sendString(49500, "Resets: " + SeasonPassConfig.getInstance().getEndDate())
                .sendSpriteChange(49470, hasPremium() ? 65535 : 3338)
                .updateProgressBar(49471, (int) (((double) (expNeeded - exp) / (double) expNeeded) * 100));

        if(hasClaimable()) {
            player.getPacketSender().sendMessage("@red@You have some items ready to claim");
        }

        player.getPacketSender().sendInterface(INTERFACE_ID);
    }

    private void changeRewardPage() {
        SeasonPassLevel[] spLevels = Arrays.copyOfRange(levels, page * 7, page * 7 + 7);
        Item[] items = new Item[14];
        for(int i = 0; i < items.length; i++) {
            if(i < items.length / 2) {
                if (rewardsClaimed[i + (page * 7)]) {
                    player.getPacketSender().sendSpriteChange(49472 + i, 3341);
                } else {
                    player.getPacketSender().sendSpriteChange(49472 + i, 65535);
                }
                items[i] = new Item(spLevels[i].getFreeItemId(), spLevels[i].getFreeAmount());
                player.getPacketSender().sendString(49493+i, "Level " + (i+(page*7)+1));
            } else {
                if (rewardsClaimed[i + (page * 7) + PREMIUM_OFFSET - 7]) {
                    player.getPacketSender().sendSpriteChange(49501 + i - 7, 3341);
                } else {
                    player.getPacketSender().sendSpriteChange(49501 + i - 7, 65535);
                }
                items[i] = new Item(spLevels[i-7].getPremiumItemId(), spLevels[i-7].getPremiumAmount());
            }
        }
        player.getPacketSender().sendItemContainer(items, 49469);
    }

    public void checkExpAndFix() {
        int level = getLevel();
        totalExperience = 0;
        if(level == getMaxPassLevel()) return;
        for(int i = level+1; i < MAX_LEVEL; i++) {
            int expNeeded = getSpLevel(i).getExpNeeded();
            if(exp > expNeeded) {
                exp -= expNeeded;
                levelUp(exp);
                totalExperience += expNeeded;
            } else {
                totalExperience += exp;
            }
        }
    }
}
