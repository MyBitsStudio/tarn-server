package com.ruse.world.packages.mode;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.impl.*;
import org.jetbrains.annotations.NotNull;

public class GameModeConstants {

    public static void setMode(Player player, @NotNull String mode){
        switch(mode){
            case "NORMAL" -> player.setMode(new Normal());
            case "IRONMAN" -> player.setMode(new Ironman());
            case "ULTIMATE_IRONMAN" -> player.setMode(new UltimateIronman());
            case "VETERAN" -> player.setMode(new Veteran());
            case "GROUP_IRONMAN" -> player.setMode(new GroupIronman());
            default -> {
                System.out.println("Invalid mode: " + mode);
            }
        }
    }

    public static @NotNull String getMode(@NotNull Player player){
        if(player.getMode() instanceof Normal){
            return "NORMAL";
        } else if(player.getMode() instanceof Ironman){
            return "IRONMAN";
        } else if(player.getMode() instanceof Veteran){
            return "VETERAN";
        } else if(player.getMode() instanceof UltimateIronman){
            return "ULTIMATE_IRONMAN";
        } else if(player.getMode() instanceof GroupIronman){
            return "GROUP_IRONMAN";
        } else {
            return "NORMAL";
        }
    }

    public static int ordinal(Player player){
        if(player.getMode() instanceof Normal){
            return 0;
        } else if(player.getMode() instanceof Ironman){
            return 2;
        } else if(player.getMode() instanceof Veteran){
            return 3;
        } else if(player.getMode() instanceof UltimateIronman){
            return 1;
        } else if(player.getMode() instanceof GroupIronman){
            return 4;
        } else {
            return 0;
        }
    }

    public static boolean isIronman(@NotNull Player player){
        return player.getMode() instanceof Ironman || player.getMode() instanceof UltimateIronman || player.getMode() instanceof GroupIronman;
    }
}
