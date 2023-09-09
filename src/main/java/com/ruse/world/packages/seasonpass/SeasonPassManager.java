package com.ruse.world.packages.seasonpass;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.ruse.world.packages.seasonpass.SeasonPass.REWARD_AMOUNT;

public class SeasonPassManager {

    public static final Map<Integer, Integer> EXP_MAP = new HashMap<>();

    public static void put(Map map){
        for(Object key : map.keySet()){
            EXP_MAP.put(Integer.parseInt((String) key), (Integer) map.get(key));
        }
    }

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

    public static void resetRewards(@NotNull SeasonPass seasonPass) {
        boolean[] arr = new boolean[REWARD_AMOUNT];
        seasonPass.setRewardsClaimed(arr);
    }

    public static void addNpcKillExp(SeasonPass seasonPass, int npc) {
        if(EXP_MAP.isEmpty()) {
            System.out.println("EXP_MAP is empty");
            return;
        }
        //System.out.println("Adding exp "+npc+" "+EXP_MAP.get(npc));
        Integer amount = EXP_MAP.get(npc);
        if(amount == null) {
            //System.out.println("Amount is null");
            return;
        }
        seasonPass.incrementExp(amount, false);
    }
}
