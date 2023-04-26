package com.ruse.world.content.seasonpass;

import java.util.HashMap;

public class SeasonPassManager {

    public static final HashMap<String, Integer> EXP_MAP = new HashMap<>();

    public static void resetSeasonPass(SeasonPass seasonPass) {
        seasonPass.setExp(0);
        seasonPass.setLevel(0);
        seasonPass.setPremium(false);
        seasonPass.setSeason(SeasonPassConfig.getInstance().getSeason());
        seasonPass.getPlayer().sendMessage("@red@Your season pass has been reset");
        seasonPass.getPlayer().sendMessage("@red@Season " + seasonPass.getSeason() + " has begun!");
    }

    public static void addNpcKillExp(SeasonPass seasonPass, String npc) {
        Integer amount = EXP_MAP.get(npc);
        if(amount == null) {
            return;
        }
        seasonPass.incrementExp(amount);
    }
}
