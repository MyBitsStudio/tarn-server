package com.ruse.world.content.seasonpass;

import java.util.HashMap;

import static com.ruse.world.content.seasonpass.SeasonPass.REWARD_AMOUNT;

public class SeasonPassManager {

    public static final HashMap<String, Integer> EXP_MAP = new HashMap<>();

    public static void resetSeasonPass(SeasonPass seasonPass) {
        seasonPass.setExp(0);
        seasonPass.setLevel(0);
        seasonPass.setTotalExperience(0);
        seasonPass.setPremium(false);
        resetRewards(seasonPass);
        seasonPass.setSeason(SeasonPassConfig.getInstance().getSeason());
        seasonPass.getPlayer().sendMessage("@red@Your season pass has been reset");
        seasonPass.getPlayer().sendMessage("@red@Season " + seasonPass.getSeason() + " has begun!");
    }

    public static void resetRewards(SeasonPass seasonPass) {
        boolean[] arr = new boolean[REWARD_AMOUNT];
        for(int i = 0; i < REWARD_AMOUNT; i++) {
            arr[i] = false;
        }
        seasonPass.setRewardsClaimed(arr);
    }

    public static void addNpcKillExp(SeasonPass seasonPass, String npc) {
        if(EXP_MAP.isEmpty()) {
            return;
        }
        Integer amount = EXP_MAP.get(npc);
        if(amount == null) {
            return;
        }
        seasonPass.incrementExp(amount);
    }
}
