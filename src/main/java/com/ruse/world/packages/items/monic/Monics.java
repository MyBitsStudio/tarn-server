package com.ruse.world.packages.items.monic;

import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class Monics {

    public static boolean handleMonic(Player player, int id){
        switch(id){
            case 14505, 19640 ->{
                sendInterface(player, id, true);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private static void sendInterface(Player player, int id, boolean refresh){
        Monic monic = Monic.getMonic(id);
        if(monic == null){
            return;
        }
        player.getVariables().setSetting("monic-chosen", id);
        if(refresh)
            refresh(player);
        reset(player);
        player.getPacketSender().sendInterface(166000);
        switch(id){
            case 14505 -> player.getPacketSender().sendString(166002, "Ancient - "+player.getItems().getCharges("ancient-monic"));
            case 19640 -> player.getPacketSender().sendString(166002, "Crystal - "+player.getItems().getCharges("crystal-monic"));
        }
        int i = 166101;
        for(MonicPerk perk : monic.getPerk()){
            player.getPacketSender().sendString(i++, perk.name()+" - "+perk.charges());
        }

        MonicPerk perk = monic.getPerk()[player.getVariables().getIntValue("perk-chosen")];
        if(perk != null){
            player.getPacketSender().sendString(166003, perk.name());
            player.getPacketSender().sendString(166004, perk.charges()+" charges");
        }

    }

    private static void refresh(@NotNull Player player){
        player.getVariables().setSetting("perk-chosen", 0);
    }

    private static void reset(Player player){
        for(int i = 166101; i < 166151; i++){
            player.getPacketSender().sendString(i, "");
        }
    }

    public static boolean handleButtons(Player player, int button){
        if(button >= 166101 && button <= 166151){
            int perk = (button - 166101);
            player.getVariables().setSetting("perk-chosen", perk);
            sendInterface(player, player.getVariables().getIntValue("monic-chosen"), false);
            return true;
        }
        switch(button){
            case 166005 -> {
                activateMonic(player, player.getVariables().getIntValue("monic-chosen"));
                return true;
            }
            case 166007 -> {
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public static void activateMonic(Player player, int id){
        Monic monic = Monic.getMonic(id);
        if(monic == null){
            return;
        }
        MonicPerk perk = monic.getPerk()[player.getVariables().getIntValue("perk-chosen")];
        if(perk == null){
            return;
        }
        if(player.getItems().getCharges(monic.getName()) < perk.charges()){
            player.getPacketSender().sendMessage("You do not have enough charges to activate this perk.");
            return;
        }
        player.getItems().useCharge(perk.name(), perk.charges());
        switch(perk.desc()){
            case "ancient-donator" ->{
                player.getPacketSender().sendMessage("You have received a Donator Chest I.");
                player.getInventory().add(23257, 1);
            }

        }
    }
}
