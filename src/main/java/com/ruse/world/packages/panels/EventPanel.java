package com.ruse.world.packages.panels;

import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.globals.GlobalBossManager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EventPanel {

    public static void refresh(@NotNull Player player){
        int interfaceID = 73501;
        player.getPacketSender().sendString(interfaceID++, "@whi@BOSSES");
        player.getPacketSender().sendString(interfaceID++, "@whi@-------------------");
        player.getPacketSender().sendString(interfaceID++, "@whi@GLOBALS");
        player.getPacketSender().sendString(interfaceID++, "@whi@Lugia: @yel@" + GlobalBossManager.getInstance().timeLeft("lugia"));
        player.getPacketSender().sendString(interfaceID++, "@whi@Nine Tails Jinchuriki: @yel@" + GlobalBossManager.getInstance().timeLeft("ninetails"));
        player.getPacketSender().sendString(interfaceID++, "@whi@Groudon: @yel@" + GlobalBossManager.getInstance().timeLeft("groudon"));
        player.getPacketSender().sendString(interfaceID++, "@whi@Mewtwo: @yel@" + (World.npcIsRegistered(9005) ? "@gre@::mewtwo" : "@red@INACTIVE"));
        player.getPacketSender().sendString(interfaceID++, "@whi@-------------------");
        player.getPacketSender().sendString(interfaceID++, "@whi@Vote Boss: @yel@"+ World.attributes.getAmount("vote-boss")+" / 100");
        player.getPacketSender().sendString(interfaceID++, "@whi@Donation Boss: @yel@"+ World.attributes.getAmount("donation-boss")+" / 350");
        player.getPacketSender().sendString(interfaceID++, "@whi@Well of Globals: @yel@VIP : "+GlobalBossManager.getInstance().getWells().get("VIP")+" / 50");
        player.getPacketSender().sendString(interfaceID++, "@whi@SPECIALS");
        player.getPacketSender().sendString(interfaceID++, World.npcIsRegistered(587) ? "@whi@Donation Boss: @yel@::donboss" : "@whi@Donation Boss: @red@INACTIVE");
        player.getPacketSender().sendString(interfaceID++, World.npcIsRegistered(8013) ? "@whi@Vote Boss: @yel@::vboss" : "@whi@Vote Boss: @red@INACTIVE");
        player.getPacketSender().sendString(interfaceID++, "");
        player.getPacketSender().sendString(interfaceID++, "");

        interfaceID = 75501;
        player.getPacketSender().sendString(interfaceID++, "@whi@-------------------");
        player.getPacketSender().sendString(interfaceID++, "@whi@-------------------");

        player.getPacketSender().sendString(interfaceID++, "");
        player.getPacketSender().sendString(interfaceID++, "");
        player.getPacketSender().sendString(interfaceID++, "");
        player.getPacketSender().sendString(interfaceID++, "");
        player.getPacketSender().sendString(interfaceID++, "");
        player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");

            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");



    }
}
