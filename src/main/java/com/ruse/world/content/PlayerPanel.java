package com.ruse.world.content;

import com.ruse.GameSettings;
import com.ruse.motivote3.doMotivote;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.packages.donation.DonationManager;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.content.skill.impl.slayer.SlayerTasks;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.packages.panels.EventPanel;
import com.ruse.world.packages.voting.VoteBossDrop;
import com.ruse.world.entity.impl.player.Player;

public class PlayerPanel {

    public static void refreshCurrentTab(Player player) {
        refreshPanel(player);
    }

    public static void refreshPanel(Player player) {
        EventPanel.refresh(player);
        int interfaceID = 111201;
        int players = World.getPlayers().size() + GameSettings.players;
        String[] Messages = new String[]{
                //
                "@whi@Main",
                "@whi@Players Online: @yel@" + ((players)),
                "@whi@Server Time: @yel@" + Misc.getCurrentServerTime(),
                //
                "@whi@Events",
                "@whi@Current Event: @yel@" +
                        ( ServerPerks.getInstance().getActivePerk() != null ?
                                ServerPerks.getInstance().getActivePerk().getName() :
                                "N/A"),
                (WellOfGoodwill.isActive() ? "@whi@Well of Goodwill: @yel@On" : "@whi@Well of Goodwill: @yel@Off"),
                "@whi@Bonus Xp: @yel@" + (Misc.format(player.getMinutesBonusExp()) == null ? "0" : Misc.format(player.getMinutesBonusExp())) + " minutes left",
        };

        for (int i = 0; i < Messages.length; i++) {
            player.getPacketSender().sendString(interfaceID++, Messages[i]);
        }


        interfaceID = 111401;

        Messages = new String[]{"@whi@Main",
                "@whi@Time Played: @yel@"
                        + Misc.getHoursPlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed())),
                "@whi@Username: @yel@" + player.getUsername(),
                "@whi@Total Donated: @yel@$" + player.getAmountDonated(),
                "@whi@Mode: @yel@"
                        + Misc.capitalizeString(player.getGameMode().toString().toLowerCase().replace("_", " ")),
                "@whi@Staff: @yel@" + Misc.formatText(player.getRank().toString().toLowerCase()),
                "@whi@Donator: @yel@" + Misc.formatText(player.getDonator().toString().toLowerCase()),
                "@whi@VIP: @yel@" + Misc.formatText(player.getVip().toString().toLowerCase()),

                "@whi@Others: @yel@",

                "@whi@Droprate bonus: @yel@" + CustomDropUtils.drBonus(player, player.getSlayer().getSlayerTask().getNpcId()) + "%",
                "@whi@Double drop bonus: @yel@" + CustomDropUtils.getDoubleDropChance(player, player.getSlayer().getSlayerTask().getNpcId()) + "%",
                "@whi@Points & Statistics",
                "@whi@NPC kill Count: @yel@ " + player.getPointsHandler().getNPCKILLCount(),
                "@whi@Donator Points: @yel@" + player.getPointsHandler().getDonatorPoints(),
                "@whi@Voting Points: @yel@ " + player.getPointsHandler().getVotingPoints(),
                "@whi@VIP Exp: @yel@" + player.getPlayerVIP().getExp(),
                "@whi@Pack Exp: @yel@" + player.getPlayerVIP().getPackXp(),
                "@whi@Total Donated: @yel@" + player.getPlayerVIP().getTotal(),
        };

        for (String message : Messages) {
            player.getPacketSender().sendString(interfaceID++, message);
        }


        interfaceID = 131101;

        Messages = new String[]{"@whi@Slayer Information",
                "@whi@Master: @yel@" + Misc
                        .formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_", " ")),
                (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK
                        ? "@whi@Task: @yel@" + player.getSlayer().getSlayerTask().getName()
                        : "@whi@Task: @yel@" + player.getSlayer().getSlayerTask().getName()
                        + "s"),
                "@whi@Amount: @yel@" + player.getSlayer().getAmountToSlay(),
                "@whi@Streak: @yel@" + player.getSlayer().getTaskStreak(),
                "@whi@Slayer Multiplier: @yel@ " + player.getPointsHandler().getSlayerRate() + "%",
                "@whi@Slayer Points: @yel@ " + player.getPointsHandler().getSlayerPoints() + " ",
                // (player.getSlayer().getDuoPartner() != null
                //        ? "Duo Partner: @whi@" + player.getSlayer().getDuoPartner()
                //      : "Duo Partner: @whi@N/A"),
        };

        for (String message : Messages) {
            player.getPacketSender().sendString(interfaceID++, message);
        }

        interfaceID = 111601;
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Achievements");
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Drop Tables");
        //player.getPacketSender().sendString(interfaceID++, "View Item List");
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Collection Log");
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Possible Loot");
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Best In Slot Items");
        player.getPacketSender().sendString(interfaceID++, "@yel@View @yel@Kill Tracker");
        player.getPacketSender().sendString(interfaceID++, "@yel@Change Password");
        player.getPacketSender().sendString(interfaceID++, "@yel@Edit Pin");
        //player.getPacketSender().sendString(interfaceID++, "@whi@View @yel@Kill Tracker");



    }

}