package com.ruse.world.packages.panels;

import com.ruse.GameSettings;
import com.ruse.model.Position;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.world.World;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.globals.GlobalBossManager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EventPanel {
//  player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 73000);
//            player.getPacketSender().sendConfig(6000, 0);
    public static boolean handleButtons(Player player, int button){
        switch(button){
            case 73501 -> {
                player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 73000);
                player.getPacketSender().sendConfig(6000, 0);
                return true;
            }
            case 73502 -> {
                player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 75000);
                player.getPacketSender().sendConfig(6000, 1);
                return true;
            }
            case 73503 -> {
                player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 121000);
                player.getPacketSender().sendConfig(6000, 2);
                return true;
            }

            case 121060 -> {
                TeleportHandler.teleportPlayer(player, new Position(2212, 3749, 0),
                        player.getSpellbook().getTeleportType());
                return true;
            }

            case 74001 -> {
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 4),
                        TeleportType.NORMAL);
                return true;
            }
            case 74003 -> {
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 0),
                        player.getSpellbook().getTeleportType());
                return true;
            }
            case 74005 -> {
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 8),
                        TeleportType.NORMAL);
                return true;
            }
            case 74007 -> {
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 12),
                        TeleportType.NORMAL);
                return true;
            }
            case 74009 -> {
                TeleportHandler.teleportPlayer(player, new Position(2530, 2648, 4),
                        TeleportType.NORMAL);
                return true;
            }
            case 74011 -> {
                TeleportHandler.teleportPlayer(player, new Position(2530, 2648, 0),
                        TeleportType.NORMAL);
                return true;
            }
            case 74013 -> {
                TeleportHandler.teleportPlayer(player, new Position(2139, 5018, 16),
                        player.getSpellbook().getTeleportType());
                return true;
            }
            case 74015 -> {
                TeleportHandler.teleportPlayer(player, new Position(3032, 5232, 0),
                        TeleportType.LUNAR);
                return true;
            }
            case 74017 -> {
                TeleportHandler.teleportPlayer(player, new Position(3032, 5232, 4),
                        TeleportType.NORMAL);
                return true;
            }
            case 74021 -> {
                player.getPacketSender().sendString(1, GameSettings.VoteUrl);
                return true;
            }
            case 74023 -> {
                player.getPacketSender().sendString(1, GameSettings.StoreUrl);
                return true;
            }
            case 74025 -> {
                TeleportHandler.teleportPlayer(player, new Position(2221, 3742, 0),
                        TeleportType.NORMAL);
                return true;
            }
        }
        return false;
    }
    public static void refresh(@NotNull Player player){

        for(int i = 74027; i < 74041; i++){
            player.getPacketSender().sendInterfaceVisibility(i, false);
        }

        player.getPacketSender().sendString(74002, "@whi@Lugia: @yel@" + GlobalBossManager.getInstance().timeLeft("lugia"));
        player.getPacketSender().sendString(74004, "@whi@Nine Tails: @yel@" + GlobalBossManager.getInstance().timeLeft("ninetails"));
        player.getPacketSender().sendString(74006, "@whi@Groudon: @yel@" + GlobalBossManager.getInstance().timeLeft("groudon"));
        player.getPacketSender().sendString(74008, "@whi@Mewtwo: @yel@" + (World.npcIsRegistered(9005) ? "@gre@::mewtwo" : "@red@INACTIVE"));

        player.getPacketSender().sendString(74010, World.npcIsRegistered(587) ? "@whi@Donation Boss: @yel@::donboss" : "@whi@Donation Boss: @red@INACTIVE");
        player.getPacketSender().sendString(74012, World.npcIsRegistered(8013) ? "@whi@Vote Boss: @yel@::vboss" : "@whi@Vote Boss: @red@INACTIVE");

        player.getPacketSender().sendString(74014, World.npcIsRegistered(7553) ? "@whi@General Khazard: @yel@::khazard" : "@whi@General Khazard: @red@INACTIVE");
        player.getPacketSender().sendString(74016, World.npcIsRegistered(9906) ? "@whi@Veigar: @yel@::veigar" : "@whi@Veigar: @red@INACTIVE");
        player.getPacketSender().sendString(74018, World.npcIsRegistered(14378) ? "@whi@Cherubimon: @yel@::cherub" : "@whi@Cherubimon: @red@INACTIVE");

        player.getPacketSender().sendString(74020, "Values");

        player.getPacketSender().sendString(74022, "@whi@Vote Boss: @yel@"+ World.attributes.getAmount("vote-boss")+" / 100");
        player.getPacketSender().sendString(74024, "@whi@Donation Boss: @yel@"+ World.attributes.getAmount("donation-boss")+" / 350");
        player.getPacketSender().sendString(74026, "@whi@VIP Well: "+GlobalBossManager.getInstance().getWells().get("VIP")+" / 50");

        if(player.getSlayer().getTask() == null){
            player.getPacketSender().sendString(121051, "@yel@None");
            player.getPacketSender().sendString(121056, "@yel@0 / 0");
        } else {
            player.getPacketSender().sendString(121051, "@yel@"+ NpcDefinition.forId(player.getSlayer().getTask().getId()).getName());
            player.getPacketSender().sendNpcOnInterface(121053, player.getSlayer().getTask().getId(),
                    1100);
            player.getPacketSender().sendString(121056, "@yel@"+(player.getSlayer().getTask().getAmount() - player.getSlayer().getTask().getSlayed()) + "/"+player.getSlayer().getTask().getAmount());
        }

        player.getPacketSender().sendString(121059, "@yel@Streak : "+ player.getSlayer().getStreak());

    }
}
