package com.ruse.world.packages.panels;

import com.ruse.GameSettings;
import com.ruse.model.Position;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.BestItemsInterface;
import com.ruse.world.content.DropsInterface;
import com.ruse.world.content.KillTrackerInterface;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.packages.combat.drops.DropCalculator;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import com.ruse.world.packages.misc.PossibleLootInterface;
import com.ruse.world.packages.serverperks.ServerPerks;
import com.ruse.world.packages.mode.impl.*;
import com.ruse.world.packages.panels.EventPanel;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.tracks.TrackInterface;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class PlayerPanel {

    public static void refreshCurrentTab(Player player) {
        refreshPanel(player);
    }

    private static String getMode(Player player){
        if(player.getMode() instanceof Normal){
            return "NORMAL";
        } else if(player.getMode() instanceof Ironman){
            return "IRONMAN";
        } else if(player.getMode() instanceof Veteran){
            return "VETERAN";
        } else if(player.getMode() instanceof UltimateIronman){
            return "ULTIMATE";
        } else if(player.getMode() instanceof GroupIronman){
            return "GROUP";
        } else {
            return "NORMAL";
        }
    }

    public static boolean handleButton(Player player, int button){
        switch(button){
            case 111801 -> {
                TrackInterface.sendInterface(player, true);
                return true;
            }
            case 111804 -> {
                player.getSeasonPass().showInterface();
                return true;
            }
            case 111807 -> {
                AchievementHandler.sendInterface(player);
                return true;
            }
            case 111810 -> {
                DropsInterface.open(player);
                return true;
            }
            case 111813 -> {
                player.getCollectionLog().open();
                return true;
            }
            case 111816 -> {
                PossibleLootInterface.openInterface(player, PossibleLootInterface.LootData.values()[0]);
                return true;
            }
            case 111819 -> {
                BestItemsInterface.openInterface(player, 0);
                return true;
            }
            case 111822 -> {
                KillTrackerInterface.open(player);
                return true;
            }
            case 111825 -> {
                player.getForge().showInterface();
                return true;
            }
            case 111828 -> {
                player.getPlayerVIP().sendInterface();
                return true;
            }
        }
        return false;
    }

    public static void refreshPanel(Player player) {
        EventPanel.refresh(player);
        MagePanel.refreshPanel(player);
        int players = World.getPlayers().size();

        if(players > 100)
          players += 21;
        else if( players > 75)
          players += 16;
        else if( players > 50)
          players += 12;
        else if(players > 25)
          players += 7;
        else if(players > 10)
          players += 2;

        player.getPacketSender().sendString(111203, "@whi@Players Online: @yel@" + ((players)));

        player.getPacketSender().sendString(111206, "@whi@Server Time: @yel@" + Misc.getCurrentServerTime());

        player.getPacketSender().sendString(111209, "@whi@Date: @yel@" + LocalDate.now(ZoneOffset.UTC));

        player.getPacketSender().sendString(111212, "@whi@Current Perk: @yel@" +
                ( ServerPerks.getInstance().getActivePerk() != null ?
                        ServerPerks.getInstance().getActivePerk().getName() :
                        "N/A"));

        player.getPacketSender().sendString(111403, "@whi@Total Played: @yel@" + Misc.getHoursPlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed())));

        player.getPacketSender().sendString(111406, "@whi@Mode: @yel@" + Misc.capitalizeString(getMode(player).toLowerCase().replace("_", " ")));

        player.getPacketSender().sendString(111409, "@whi@Staff: @yel@" + Misc.formatText(player.getRank().toString().toLowerCase()));

        player.getPacketSender().sendString(111412, "@whi@VIP: @yel@" + Misc.formatText(player.getVip().toString().toLowerCase()));

        player.getPacketSender().sendString(111415, "@whi@Kill Count: @yel@" + player.getPointsHandler().getNPCKILLCount());

        player.getPacketSender().sendString(111418, "@whi@Total Votes: @yel@" + player.getPoints().get("voted"));

        player.getPacketSender().sendString(111421, "@whi@VIP Points: @yel@" + player.getPlayerVIP().getPoints());

        player.getPacketSender().sendString(111424, "@whi@VIP Exp: @yel@" + player.getPlayerVIP().getExp());

        player.getPacketSender().sendString(111427, "@whi@Pack Exp: @yel@" + player.getPlayerVIP().getPackXp());

        player.getPacketSender().sendString(111430, "@whi@Loyalty Exp: @yel@" + player.getLoyalty().getXp());

        player.getPacketSender().sendString(111433, "@whi@Loyalty Level: @yel@" + player.getLoyalty().getLevel());


    }

}