package com.ruse.world.packages.tracks;

import com.ruse.world.content.KillsTracker;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.tracks.impl.starter.StarterTasks;
import com.ruse.world.packages.tracks.impl.tarn.normal.TarnNormalTasks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TrackInterface {
    /**
     * Adding new Tracks
     * 1. Add a new String[][] to the tracks array
     */

    public static String[][] tracks = {
            {"Starter"},
            {"Tarn Normal"},
            {},
            {},
            {}
    };

    public static void sendInterface(Player player, boolean refresh){
        if(refresh)
            resetVars(player);

        reset(player);

        player.getPacketSender().sendInterface(161000);

        String[] settings = player.getVariables().getInterfaceSettings();

        String[] trackNames = tracks[Integer.parseInt(settings[0])];

        for(int i = 0; i < trackNames.length; i++)
            player.getPacketSender().sendString(161101 + i, trackNames[i]);

        switch(Integer.parseInt(settings[0])){
            case 0 -> {
                switch(Integer.parseInt(settings[1])){
                    case 0 -> player.getStarter().sendInterfaceList();
                }
            }
            case 1-> {
                switch(Integer.parseInt(settings[1])){
                    case 0 -> player.getTarnNormal().sendInterfaceList();
                }
            }
        }

        switch(Integer.parseInt(settings[0])) {
            case 0 -> {
                switch(Integer.parseInt(settings[1])){
                    case 0 -> {
                        int type = Integer.parseInt(settings[2]);
                        int index = Integer.parseInt(settings[3]);
                        StarterTasks tasks = StarterTasks.getByIndexAndCategory(type, index);
                        if(tasks != null) {
                            switch(Integer.parseInt(settings[4])){
                                case 0 -> {
                                    player.getPacketSender().sendString(161045, tasks.getTaskName());
                                    player.getPacketSender().sendString(161046, "XP +"+tasks.getXp());
                                    player.getPacketSender().sendString(161048, (player.getStarter().completed(tasks) ? "@gre@Completed" : "@red@Not Completed"));

                                    if(tasks.getNpc() == -1) {

                                    } else {
                                        StarterTasks kill = StarterTasks.byNPC(tasks.getNpc());
                                        if(kill == null) {
                                            player.getPacketSender().sendString(161047, "Kills : @red@ERROR");
                                        } else {
                                            player.getPacketSender().sendString(161047, "Kills : "+ KillsTracker.getTotalKillsForNpc(tasks.getNpc(), player)+"/"+tasks.getCount());
                                        }
                                    }
                                }
                                case 1 -> {
                                    player.getPacketSender().sendInterfaceDisplayState(161300, false);
                                    player.getPacketSender().sendItemOnInterface(161301, tasks.getReward().item(), tasks.getReward().amount());
                                }
                            }
                        }
                    }
                }
            }
            case 1 -> {
                switch(Integer.parseInt(settings[1])){
                    case 0 -> {
                        int type = Integer.parseInt(settings[2]);
                        int index = Integer.parseInt(settings[3]);
                        TarnNormalTasks tasks = TarnNormalTasks.getByIndexAndCategory(type, index);
                        if(tasks != null) {
                            switch(Integer.parseInt(settings[4])){
                                case 0 -> {
                                    player.getPacketSender().sendString(161045, tasks.getTaskName());
                                    player.getPacketSender().sendString(161046, "XP +"+tasks.getXp());
                                    player.getPacketSender().sendString(161048, (player.getTarnNormal().completed(tasks) ? "@gre@Completed" : "@red@Not Completed"));

                                    if(tasks.getNpc() == -1) {
                                        switch(tasks){
                                            case TASK_100, TASK_101, TASK_102 -> {
                                                player.getPacketSender().sendString(161047, "Tasks : "+player.getSlayer().getTotal()+"/"+tasks.getCount());
                                            }
                                            case TASK_103, TASK_104, TASK_105 -> {
                                                player.getPacketSender().sendString(161047, "Dissolve : "+player.getPoints().get("dissolve")+"/"+tasks.getCount());
                                            }
                                        }
                                    } else {
                                        TarnNormalTasks kill = TarnNormalTasks.byNPC(tasks.getNpc());
                                        if(kill == null) {
                                            player.getPacketSender().sendString(161047, "Kills : @red@ERROR");
                                        } else {
                                            player.getPacketSender().sendString(161047, "Kills : "+ KillsTracker.getTotalKillsForNpc(tasks.getNpc(), player)+"/"+tasks.getCount());
                                        }
                                    }
                                }
                                case 1 -> {
                                    player.getPacketSender().sendInterfaceDisplayState(161300, false);
                                    player.getPacketSender().sendItemOnInterface(161301, tasks.getReward().item(), tasks.getReward().amount());
                                }
                            }
                        }
                    }
                }
            }
        }

        switch(Integer.parseInt(settings[0])){
            case 0 -> {
                player.getPacketSender().updateProgressSpriteBar(161058, player.getStarter().getProgress(), 100);
                switch(Integer.parseInt(settings[5])){
                    case 0 -> {
                        player.getPacketSender().sendString(161052, "XP : "+player.getStarter().xp+"/"+player.getStarter().maxLevel);
                        player.getPacketSender().sendString(161053, "Level : "+player.getStarter().position);
                        player.getPacketSender().sendString(161054, "Max Level : 5");
                        player.getPacketSender().sendString(161055, "Difficulty : Easy");
                        player.getPacketSender().sendString(161056, "Tasks Completed : "+player.getStarter().getCompleted());
                        player.getPacketSender().sendString(161057, "Tasks Left : "+player.getStarter().nonCompleted());

                    }
                    case 1 -> {
                        player.getPacketSender().sendInterfaceDisplayState(161400, false);
                        player.getPacketSender().sendInterfaceDisplayState(161059, false);
                        player.getPacketSender().sendString(161060, "CLAIM");

                        int used = 0;
                        for(ProgressReward reward : player.getStarter().getRewards().getRewards()) {
                            if(reward == null)
                                continue;

                            if(player.getStarter().position >= reward.getLevel() && !reward.isClaimed())
                                player.getPacketSender().sendItemOnInterface(161401 + (used++), reward.getItem(), reward.getAmount());
                        }
                        player.getPacketSender().sendString(161055, "Waiting : "+used);
                        player.getPacketSender().sendString(161056, "Tasks Completed : "+player.getStarter().getCompleted());
                        player.getPacketSender().sendString(161057, "Tasks Left : "+player.getStarter().nonCompleted());
                    }
                }
            }
            case 1 -> {
                player.getPacketSender().updateProgressSpriteBar(161058, player.getTarnNormal().getProgress(), 100);
                switch(Integer.parseInt(settings[5])){
                    case 0 -> {
                        player.getPacketSender().sendString(161052, "XP : "+player.getTarnNormal().xp+"/"+player.getTarnNormal().maxLevel);
                        player.getPacketSender().sendString(161053, "Level : "+player.getTarnNormal().position);
                        player.getPacketSender().sendString(161054, "Max Level : 5");
                        player.getPacketSender().sendString(161055, "Difficulty : Medium");
                        player.getPacketSender().sendString(161056, "Tasks Completed : "+player.getTarnNormal().getCompleted());
                        player.getPacketSender().sendString(161057, "Tasks Left : "+player.getTarnNormal().nonCompleted());

                    }
                    case 1 -> {
                        player.getPacketSender().sendInterfaceDisplayState(161400, false);
                        player.getPacketSender().sendInterfaceDisplayState(161059, false);
                        player.getPacketSender().sendString(161060, "CLAIM");

                        int used = 0;
                        for(ProgressReward reward : player.getTarnNormal().getRewards().getRewards()) {
                            if(reward == null)
                                continue;

                            if(player.getTarnNormal().position >= reward.getLevel() && !reward.isClaimed())
                                player.getPacketSender().sendItemOnInterface(161401 + (used++), reward.getItem(), reward.getAmount());
                        }
                        player.getPacketSender().sendString(161055, "Waiting : "+used);
                        player.getPacketSender().sendString(161056, "Tasks Completed : "+player.getTarnNormal().getCompleted());
                        player.getPacketSender().sendString(161057, "Tasks Left : "+player.getTarnNormal().nonCompleted());
                    }
                }
            }
        }

    }

    private static void resetVars(@NotNull Player player){
        player.getVariables().setInterfaceSettings(0, "0");
        player.getVariables().setInterfaceSettings(1, "0");
        player.getVariables().setInterfaceSettings(2, "0");
        player.getVariables().setInterfaceSettings(3, "0");
        player.getVariables().setInterfaceSettings(4, "0");
        player.getVariables().setInterfaceSettings(5, "0");
    }

    private static void reset(@NotNull Player player){
        player.getPacketSender().sendInterfaceDisplayState(161300, true);
        player.getPacketSender().sendInterfaceDisplayState(161400, true);
        player.getPacketSender().sendInterfaceDisplayState(161059, true);
        player.getPacketSender().sendString(161060, "");

        for(int i = 0; i < 45; i++)
            player.getPacketSender().sendItemOnInterface(161301 + i, -1, 1);
        for(int i = 0; i < 15; i++)
            player.getPacketSender().sendItemOnInterface(161401 + i, -1, 1);

        for(int i = 0; i < 6; i++)
            player.getPacketSender().sendString(161044 + (i + 1), "");

        for(int i = 0; i < 50; i++)
            player.getPacketSender().sendString(161101 + i, "");

        for(int i = 0; i < 50; i++)
            player.getPacketSender().sendString(161201 + i, "");

        for(int i = 0; i < 6; i++)
            player.getPacketSender().sendString(161052 + i, "");

    }

    public static boolean handleButtons(Player player, int id){
        if(id >= 161101 && id <= 161150){
            int index = id - 161101;
            player.getVariables().setInterfaceSettings(1, String.valueOf(index));
            sendInterface(player, false);
            return true;
        }
        if(id >= 161201 && id <= 161250){
            int index = id - 161201;
            player.getVariables().setInterfaceSettings(3, String.valueOf(index));
            sendInterface(player, false);
            return true;
        }
        switch(id){
            case 161006 -> {
                player.getVariables().setInterfaceSettings(0, "0");
                sendInterface(player, false);
                return true;
            }
            case 161007 -> {
                player.getVariables().setInterfaceSettings(0, "1");
                sendInterface(player, false);
                return true;
            }
            case 161008 -> {
                player.getVariables().setInterfaceSettings(0, "2");
                sendInterface(player, false);
                return true;
            }
            case 161009 -> {
                player.getVariables().setInterfaceSettings(0, "3");
                sendInterface(player, false);
                return true;
            }
            case 161010 -> {
                player.getVariables().setInterfaceSettings(0, "4");
                sendInterface(player, false);
                return true;
            }

            case 161011 -> {
                player.getVariables().setInterfaceSettings(2, "0");
                sendInterface(player, false);
                return true;
            }
            case 161012 -> {
                player.getVariables().setInterfaceSettings(2, "1");
                sendInterface(player, false);
                return true;
            }
            case 161013 -> {
                player.getVariables().setInterfaceSettings(2, "2");
                sendInterface(player, false);
                return true;
            }
            case 161014 -> {
                player.getVariables().setInterfaceSettings(2, "3");
                sendInterface(player, false);
                return true;
            }
            case 161015 -> {
                player.getVariables().setInterfaceSettings(2, "4");
                sendInterface(player, false);
                return true;
            }
            case 161016 -> {
                player.getVariables().setInterfaceSettings(2, "5");
                sendInterface(player, false);
                return true;
            }

            case 161017 -> {
                player.getVariables().setInterfaceSettings(4, "0");
                sendInterface(player, false);
                return true;
            }
            case 161018 -> {
                player.getVariables().setInterfaceSettings(4, "1");
                sendInterface(player, false);
                return true;
            }
            case 161019 -> {
                player.getVariables().setInterfaceSettings(4, "2");
                sendInterface(player, false);
                return true;
            }
            case 161020, 161023, 161024 -> {
                player.sendMessage("Coming soon!");
                return true;
            }

            case 161021 -> {
                player.getVariables().setInterfaceSettings(5, "0");
                sendInterface(player, false);
                return true;
            }
            case 161022 -> {
                player.getVariables().setInterfaceSettings(5, "1");
                sendInterface(player, false);
                return true;
            }

            case 161059 -> {
                switch(Integer.parseInt(player.getVariables().getInterfaceSettings()[0])){
                    case 0 -> {
                        switch(Integer.parseInt(player.getVariables().getInterfaceSettings()[1])){
                            case 0 -> player.getStarter().getRewards().claimRewards(player, player.getStarter().position, false, false);
                        }
                    }
                    case 1-> {
                        switch(Integer.parseInt(player.getVariables().getInterfaceSettings()[1])){
                            case 0 -> player.getTarnNormal().getRewards().claimRewards(player, player.getTarnNormal().position, false, false);
                        }
                    }
                }
                sendInterface(player, false);
                return true;
            }
        }
        return false;
    }
}
