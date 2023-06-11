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
        int players = (int) World.getPlayers().size() + GameSettings.players;
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
//                "@whi@Global",
//                (VoteBossDrop.currentSpawn == null
//                        ? "@whi@Vote Boss: @yel@" + doMotivote.getVoteCount() + "/50"
//                        : "@whi@Vote Boss: @yel@::Vboss"),
//                (DonationManager.getInstance().getBoss() == null
//                ? "@whi@Donation Boss: @yel@" + DonationManager.getInstance().getTotalDonated() + "/" + DonationManager.getTotalNeeded()
//                        : "@whi@Donation Boss: @yel@::Donboss"),
//                "@whi@Final Boss Veigar: @yel@" + GlobalBossManager.getInstance().timeLeft("veigar"),
//                "@whi@Nine Tails Jinchuriki: @yel@" + GlobalBossManager.getInstance().timeLeft("ninetails"),
//                "@whi@Meruem The King: @yel@" + GlobalBossManager.getInstance().timeLeft("meruem"),
//                "@whi@Golden Great Ape: @yel@" + GlobalBossManager.getInstance().timeLeft("golden"),
//                /* (WorldBosses.currentBoss == null
//                         ? "Global bosses: @whi@" + WorldBosses.timeLeft()
//                         : "Global bosses: @whi@::global"),*/
//                //"Next Prime Respawn:",
//                //"@whi@" + (!SkeletalHorror.wyrmAlive ? SkeletalHorror.getTimeLeft() : "Currently Alive @::prime"),
//                //
//                "@whi@Useful links",
//                "@yel@Open @yel@Homepage",
//                "@yel@Open @yel@Forums",
//                "@yel@Open @yel@Vote",
//                "@yel@Open @yel@Store",
//                "@yel@Open @yel@Discord",
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
                "@whi@Rank: @yel@" + Misc.formatText(player.getRights().toString().toLowerCase()),

                "@whi@Droprate bonus: @yel@" + CustomDropUtils.drBonus(player, player.getSlayer().getSlayerTask().getNpcId()) + "%",
                "@whi@Double drop bonus: @yel@" + CustomDropUtils.getDoubleDropChance(player, player.getSlayer().getSlayerTask().getNpcId()) + "%",
                //"Triple drop bonus: @whi@" + CustomDropUtils.getTripleDropChance(player) + "%",
                //"Exp Lock: " + (player.experienceLocked() ? "Locked" : "@gre@Unlocked") + "",
                //"Voting x2 DR Boost: @whi@" + player.getMinutesVotingDR() + "min",
                //"Voting x2 DMG Boost: @whi@" + player.getMinutesVotingDMG() + "min",

                //
                "@whi@Points & Statistics",
                "@whi@NPC kill Count: @yel@ " + player.getPointsHandler().getNPCKILLCount(),
                //"Boss Points: @whi@ " + player.getPointsHandler().getBossPoints(),
                "@whi@Donator Points: @yel@" + player.getPointsHandler().getDonatorPoints(),
                "@whi@Voting Points: @yel@ " + player.getPointsHandler().getVotingPoints(),
        };

        for (int i = 0; i < Messages.length; i++) {
            player.getPacketSender().sendString(interfaceID++, Messages[i]);
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

        for (int i = 0; i < Messages.length; i++) {
            player.getPacketSender().sendString(interfaceID++, Messages[i]);
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