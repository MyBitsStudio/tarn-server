package com.ruse.world.packages.panels;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.motivote3.doMotivote;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.donation.DonateSales;
import com.ruse.world.packages.donation.DonationManager;
import com.ruse.world.packages.donation.FlashDeals;
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
        player.getPacketSender().sendString(interfaceID++,  (DonationManager.getInstance().getBoss() == null
                ? "@whi@Donation Boss: @yel@" + DonationManager.getInstance().getTotalDonated() + "/" + DonationManager.getTotalNeeded()
                        : "@whi@Donation Boss: @yel@::Donboss"));
        player.getPacketSender().sendString(interfaceID++, World.npcIsRegistered(4540) ? "@whi@Youtube Boss: @yel@::yt" : "@whi@Youtube Boss: INACTIVE");

        interfaceID = 75501;
        player.getPacketSender().sendString(interfaceID++, "@whi@DONATE DEALS");
        player.getPacketSender().sendString(interfaceID++, "@whi@-------------------");
        player.getPacketSender().sendString(interfaceID++, "@whi@SALES - "+(DonateSales.getInstance().isActive() ? "@gre@ACTIVE" : "@red@INACTIVE"));
        int deals = 0;
        if(DonateSales.getInstance().isActive()){
            for(Map.Entry<Integer, String> deal : DonateSales.getInstance().getDeals().entrySet()){
                player.getPacketSender().sendString(interfaceID++, "@whi@"+(++deals)+": @yel@"+ItemDefinition.forId(deal.getKey()).getName()+" - @gre@"+deal.getValue());
            }
            if(deals < 8){
                for(int i = deals; i < 10; i++){
                    player.getPacketSender().sendString(interfaceID++, "");
                }
            }
        } else {
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
        }
        player.getPacketSender().sendString(interfaceID++, "@whi@SPECIALS - "+ (FlashDeals.getDeals().isActive() ? "@gre@ACTIVE" : "@red@INACTIVE"));
        deals = 0;
        if(FlashDeals.getDeals().isActive()){
            for(Map.Entry<Integer, Map<Integer, Integer>> deal : FlashDeals.getDeals().getSpecialDeals().entrySet()){
                player.getPacketSender().sendString(interfaceID++, "@whi@$"+deal.getKey()+" - @gre@"+ItemDefinition.forId(deal.getValue().keySet().iterator().next()).getName()+" x"+deal.getValue().values().iterator().next());
                deals++;
            }
            if(deals < 10){
                for(int i = deals; i < 10; i++){
                    player.getPacketSender().sendString(interfaceID++, "");
                }
            }
            deals = 0;
            player.getPacketSender().sendString(interfaceID++, "@whi@DOUBLED");
            for(int doubles : FlashDeals.getDeals().getDoubledItems()){
                player.getPacketSender().sendString(interfaceID++, "@whi@"+ItemDefinition.forId(doubles).getName());
                deals++;
            }
            if(deals < 10){
                for(int i = deals; i < 10; i++){
                    player.getPacketSender().sendString(interfaceID++, "");
                }
            }
        } else {
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
}
