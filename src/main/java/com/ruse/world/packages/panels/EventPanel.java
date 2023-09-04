package com.ruse.world.packages.panels;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.motivote3.doMotivote;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.packages.voting.VoteBossDrop;

import java.util.Map;

public class EventPanel {

    public static void refresh(Player player){
        int interfaceID = 73501;
        player.getPacketSender().sendString(interfaceID++, "@whi@BOSSES");
        player.getPacketSender().sendString(interfaceID++, "@whi@-------------------");
        player.getPacketSender().sendString(interfaceID++, "@whi@GLOBALS");
        player.getPacketSender().sendString(interfaceID++, "@whi@Final Boss Veigar: @yel@" + GlobalBossManager.getInstance().timeLeft("veigar"));
        player.getPacketSender().sendString(interfaceID++, "@whi@Nine Tails Jinchuriki: @yel@" + GlobalBossManager.getInstance().timeLeft("ninetails"));
        player.getPacketSender().sendString(interfaceID++, "@whi@Meruem The King: @yel@" + GlobalBossManager.getInstance().timeLeft("meruem"));
        player.getPacketSender().sendString(interfaceID++, "@whi@Golden Great Ape: @yel@" + GlobalBossManager.getInstance().timeLeft("golden"));
        player.getPacketSender().sendString(interfaceID++, "@whi@Angel Lugia: @yel@" + GlobalBossManager.getInstance().timeLeft("lugia"));
        player.getPacketSender().sendString(interfaceID++, "@whi@-------------------");
        player.getPacketSender().sendString(interfaceID++, "");
        player.getPacketSender().sendString(interfaceID++, "");
        player.getPacketSender().sendString(interfaceID++, "@whi@SPECIALS");
        player.getPacketSender().sendString(interfaceID++, (VoteBossDrop.currentSpawn == null
                        ? "@whi@Vote Boss: @yel@" + doMotivote.getVoteCount() + "/50"
                        : "@whi@Vote Boss: @yel@::Vboss"));
        player.getPacketSender().sendString(interfaceID++, World.npcIsRegistered(4540) ? "@whi@Youtube Boss: @yel@::yt" : "@whi@Youtube Boss: INACTIVE");

        interfaceID = 75501;
        player.getPacketSender().sendString(interfaceID++, "@whi@DONATE DEALS");
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
