package com.ruse.world.content;

import com.ruse.GameSettings;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.motivote3.doMotivote;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.packages.combat.drops.DropCalculator;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.packages.event.Event;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.packages.mode.impl.*;
import com.ruse.world.packages.panels.EventPanel;
import com.ruse.world.packages.voting.VoteBossDrop;
import com.ruse.world.entity.impl.player.Player;

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

    public static void refreshPanel(Player player) {
        EventPanel.refresh(player);
        int interfaceID = 111201;
        int players = World.getPlayers().size() + GameSettings.players;
        String[] add = new String[World.handler.getEvents().size()];
        int n = 0;
        for(Event event : World.handler.getEvents()){
            add[n++] = event.name();
        }
        String[] Messages = new String[]{
                //
                "@whi@Main",
                "@whi@Players Online: @yel@" + ((players)),
                "@whi@Server Time: @yel@" + Misc.getCurrentServerTime(),
                //
                "@whi@Events",
                "@whi@Current Perk: @yel@" +
                        ( ServerPerks.getInstance().getActivePerk() != null ?
                                ServerPerks.getInstance().getActivePerk().getName() :
                                "N/A"),
                "@whi@Well of Globals: @yel@VIP : "+GlobalBossManager.getInstance().getWells().get("VIP")+" / 50",
                "@whi@Vote Boss: @yel@"+ World.attributes.getAmount("vote-boss")+" / 50",
                "@whi@Donation Boss: @yel@"+ World.attributes.getAmount("donation-boss")+" / 50",
                "@whi@Bonus Xp: @yel@" + (player.getTimers().get("double-damage") == null ? 0 :(Misc.format((int) player.getTimers().get("vote-xp").returnLeft() / (1000 * 60)) == null ? "0" : Misc.format((int) (player.getTimers().get("vote-xp").returnLeft() / (1000 * 60)))) + " minutes left"),
        };


        for (String string : Messages) {
            player.getPacketSender().sendString(interfaceID++, string);
        }
        for (String s : add) {
            player.getPacketSender().sendString(interfaceID++, s);
        }



        interfaceID = 111401;

        Messages = new String[]{"@whi@Main",
                "@whi@Time Played: @yel@"
                        + Misc.getHoursPlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed())),
                "@whi@Username: @yel@" + player.getUsername(),
                "@whi@Total Donated: @yel@$" + player.getPlayerVIP().getTotal(),
                "@whi@Mode: @yel@"
                        + Misc.capitalizeString(getMode(player).toLowerCase().replace("_", " ")),
                "@whi@Staff: @yel@" + Misc.formatText(player.getRank().toString().toLowerCase()),
                "@whi@VIP: @yel@" + Misc.formatText(player.getVip().toString().toLowerCase()),
                " ",
                "@whi@Others: @yel@",
                " ",
                "@whi@Droprate bonus: @yel@" + DropCalculator.getDropChance(player, 9837) + "%",
                "@whi@Double drop bonus: @yel@" + DropCalculator.getDoubleDropChance(player, 9837) + "%",
                "@whi@NPC kill Count: @yel@ " + player.getPointsHandler().getNPCKILLCount(),
                "@whi@Total Votes: @yel@ " + player.getPoints().get("voted"),
                "@whi@VIP Points: @yel@" + player.getPlayerVIP().getPoints(),
                "@whi@VIP Exp: @yel@" + player.getPlayerVIP().getExp(),
                "@whi@Pack Exp: @yel@" + player.getPlayerVIP().getPackXp()
        };

        for (String message : Messages) {
            player.getPacketSender().sendString(interfaceID++, message);
        }


        interfaceID = 131101;

        Messages = new String[]{"@whi@Slayer Information",
                "@whi@Master: @yel@" + Misc
                        .formatText(player.getSlayer().getMaster().toString().toLowerCase().replaceAll("_", " ")),
                (player.getSlayer().getTask() != null
                        ? "@whi@Task: @yel@" + NpcDefinition.forId(player.getSlayer().getTask().getId()).getName()
                        : "@whi@Task: @yel@None"),
                "@whi@Amount: @yel@" + (player.getSlayer().getTask() != null ? player.getSlayer().getTask().getAmount() - player.getSlayer().getTask().getSlayed() : 0),
                "@whi@Streak: @yel@" + player.getSlayer().getStreak(),
        };

        for (String message : Messages) {
            player.getPacketSender().sendString(interfaceID++, message);
        }

        interfaceID = 111601;
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Tracks");
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Battlepass");
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Achievements");
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Drop Tables");
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Collection Log");
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Possible Loot");
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Best In Slot Items");
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Kill Tracker");
        player.getPacketSender().sendString(interfaceID++, "@yel@Change Password");
        player.getPacketSender().sendString(interfaceID++, "@yel@Edit Pin");




    }

}