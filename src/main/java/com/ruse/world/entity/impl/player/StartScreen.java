package com.ruse.world.entity.impl.player;

import com.ruse.GameSettings;
import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.content.PlayerPanel;
import com.ruse.world.content.discordbot.JavaCord;
import com.ruse.world.packages.mode.impl.*;
import com.ruse.world.packages.tracks.TrackInterface;
import org.jetbrains.annotations.NotNull;
//import sun.management.counter.perf.PerfLongArrayCounter;

//import com.arlania.world.content.discordbot.Main;

/**
 * Start screen functionality.
 *
 * @author Joey wijers
 */
public class StartScreen {
    public static void open(Player player) {
        sendNames(player);
        player.getPacketSender().sendInterface(116000);
        player.getPacketSender().sendConfig(5331, 0);
        player.selectedGameMode = GameModes.NORMAL;
        check(player, GameModes.NORMAL);
        sendStartPackItems(player, GameModes.NORMAL);
        sendDescription(player, GameModes.NORMAL);
    }

    public static void sendDescription(Player player, GameModes mode) {
        player.getPacketSender().sendString(116101,  mode.line1 + "\\n" + mode.line2 + "\\n" + mode.line3 + "\\n" + mode.line4 + "\\n" + mode.line5 + "\\n" + mode.line6 );
    }

    public static void sendStartPackItems(Player player, GameModes mode) {
        final int START_ITEM_INTERFACE = 116201;
        for (int i = 0; i < 28; i++) {
            int id = -1;
            int amount = 0;
            try {
                id = mode.starterPackItems[i].getId();
                amount = mode.starterPackItems[i].getAmount();
            } catch (Exception e) {

            }
            player.getPacketSender().sendItemOnInterface(START_ITEM_INTERFACE + i, id, amount);
        }
    }

    public static boolean handleButton(Player player, int buttonId) {
        final int CONFIRM = 116010;
        if (buttonId == CONFIRM) {
            if (player.didReceiveStarter()) {
                return true;
            }

            player.getPacketSender().sendInterfaceRemoval();
            player.setReceivedStarter(true);
            handleConfirm(player);
            addStarterToInv(player);

            player.setPlayerLocked(false);
            player.setNewPlayer(false);
            World.sendMessage("<img=26><shad=1><col=FF0000> [" + player.getUsername() + "] <col=9E0000>has just logged into <col=FF0000>Tarn<col=9E0000> for the first time");
            JavaCord.sendMessage(1117224946855329893L, ":tada: **[New Arrival] " + player.getUsername() + " has just logged into Tarn for the first time!** ");

            player.getPacketSender().sendRights();

            if(player.getMode() instanceof GroupIronman) {
                player.moveTo(new Position(3039, 2845, 1));
                player.setGroupIronmanLocked(true);
                player.sendMessage("@blu@Speak to the Ironman Manager to create a group or get invited to a group.");
            } else {
                player.moveTo(GameSettings.STARTER);
            }

            TrackInterface.sendInterface(player, true);

            return true;
        }
        for (GameModes mode : GameModes.values()) {
            if (mode.checkClick == buttonId || mode.textClick == buttonId) {
                selectMode(player, mode);
                return true;
            }
        }
        return false;

    }

    public static void handleConfirm(Player player) {

        setMode(player, player.selectedGameMode);
        PlayerPanel.refreshPanel(player);

    }

    public static void setMode(@NotNull Player player, @NotNull GameModes mode) {
		player.getClickDelay().reset();
		player.getPacketSender().sendInterfaceRemoval();

        switch (mode) {
            case NORMAL -> player.setMode(new Normal());
            case IRONMAN -> player.setMode(new Ironman());
            case ULTIMATE_IRONMAN -> player.setMode(new UltimateIronman());
            case GROUP_IRON -> player.setMode(new GroupIronman());
            case VETERAN_MODE -> player.setMode(new Veteran());
        }

        player.setPlayerLocked(player.newPlayer());
    }

    public static void addStarterToInv(@NotNull Player player) {
        for (Item item : player.getMode().starterItems()) {
            player.getInventory().add(item);
        }
    }

    public static void selectMode(@NotNull Player player, GameModes mode) {
        player.selectedGameMode = mode;
        check(player, mode);
        sendStartPackItems(player, mode);
        sendDescription(player, mode);
    }

    public static void check(Player player, GameModes mode) {
        for (GameModes gameMode : GameModes.values()) {
            if (player.selectedGameMode == gameMode) {
                player.getPacketSender().sendConfig(gameMode.configId, 1);
                continue;
            }
            player.getPacketSender().sendConfig(gameMode.configId, 0);
        }
    }

    public static void sendNames(Player player) {
        for (GameModes mode : GameModes.values()) {
            player.getPacketSender().sendString(mode.stringId, mode.name);
        }
    }

    public enum GameModes {
        NORMAL("Normal", 52761, 116005, 1, 0,
                new Item[]{new Item(703, 1), new Item(704, 1), new Item(705, 1),
                        new Item(19946, 1), new Item(19945, 1), new Item(19914, 1),
                        new Item(19944, 1), new Item(23089, 1), new Item(23091, 1),
                        new Item(22084, 1), new Item(22092, 1), new Item(22083, 1),
                        new Item(3025, 100), new Item(17818, 100), new Item(995, 1_000_000)
                },
                "In this game mode",
                "Play the game without any restrictions.",
                "@whi@0.0% Droprate bonus", "", "", "", ""),


        IRONMAN("Ironman", 52762, 116006, 1, 1,
                new Item[]{new Item(703, 1), new Item(704, 1), new Item(705, 1),
                        new Item(19946, 1), new Item(19945, 1), new Item(19914, 1),
                        new Item(19944, 1), new Item(23089, 1), new Item(23091, 1),
                        new Item(22084, 1), new Item(22092, 1), new Item(22083, 1),
                        new Item(3025, 100), new Item(17818, 100), new Item(22120, 1), new Item(995, 1_000_000)
                },
                "Ironman mode is a very intense gamemode",
                "You are not allowed to trade or stake",
                "You will not get a npc drop if you didn solo",
                "This is for players who like a challenge",
                "@whi@6.0% Droprate bonus", "", ""),


        ULTIMATE_IRONMAN("Ultimate Iron", 52763, 116007, 1, 2,
                new Item[]{new Item(703, 1), new Item(704, 1), new Item(705, 1),
                        new Item(19946, 1), new Item(19945, 1), new Item(19914, 1),
                        new Item(19944, 1), new Item(23089, 1), new Item(23091, 1),
                        new Item(22084, 1), new Item(22092, 1), new Item(22083, 1),
                        new Item(3025, 100), new Item(17818, 100), new Item(22119, 1), new Item(995, 1_000_000)
                },
                "Ultimate ironman is harder than ironman",
                "@red@Same as ironman mode, but you can't banks",
                "This is for players who like a challenge",
                "@whi@10.0% Droprate bonus", "", "", ""),


        GROUP_IRON("Group Ironman", 52778, 116008, 1, 4,
                new Item[]{new Item(703, 1), new Item(704, 1), new Item(705, 1),
                        new Item(19946, 1), new Item(19945, 1), new Item(19914, 1),
                        new Item(19944, 1), new Item(23089, 1), new Item(23091, 1),
                        new Item(22084, 1), new Item(22092, 1), new Item(22083, 1),
                        new Item(3025, 100), new Item(17818, 100), new Item(22118, 1), new Item(995, 1_000_000)
                },
                "Group Ironman mode is a very fun gamemode",
                "Same rules as ironman mode",
                "You can have a group with up to five players",
                "You can trade other players in your group",
                "You have a shared bank with your group",
                "@whi@10.0% Droprate bonus", ""),


        VETERAN_MODE("Veteran", 52774, 116009, 1, 3,
                new Item[]{new Item(703, 1), new Item(704, 1), new Item(705, 1),
                        new Item(19946, 1), new Item(19945, 1), new Item(19914, 1),
                        new Item(19944, 1), new Item(23089, 1), new Item(23091, 1),
                        new Item(22084, 1), new Item(22092, 1), new Item(22083, 1),
                        new Item(3025, 100), new Item(17818, 100), new Item(995, 1_000_000)
                },
                "This is probably the hardest game mode",
                "@red@The EXP rate in this mode is the hardest",
                "This mode is for players that love grinding",
                "@whi@30.0% Droprate bonus", "", "", "")

        ;
        private final String name;
        private final int stringId, checkClick, textClick, configId;
        private final Item[] starterPackItems;
        private final String line1, line2, line3, line4, line5, line6, line7;

        GameModes(String name, int stringId, int checkClick, int textClick, int configId, Item[] starterPackItems, String line1, String line2, String line3, String line4, String line5, String line6, String line7) {
            this.name = name;
            this.stringId = stringId;
            this.checkClick = checkClick;
            this.textClick = textClick;
            this.configId = configId;
            this.starterPackItems = starterPackItems;
            this.line1 = line1;
            this.line2 = line2;
            this.line3 = line3;
            this.line4 = line4;
            this.line5 = line5;
            this.line6 = line6;
            this.line7 = line7;
        }
    }
}
