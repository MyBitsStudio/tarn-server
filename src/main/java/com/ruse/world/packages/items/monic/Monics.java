package com.ruse.world.packages.items.monic;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.monic.*;
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
        player.getItems().useCharge(monic.getName(), perk.charges());
        switch(perk.desc()){
            case "ancient-xp" -> player.getTimers().register(new DXPMonicHour(player));
            case "ancient-dr" -> player.getTimers().register(new DDRMonicHour(player));
            case "ancient-ddr" -> player.getTimers().register(new DDDRMonicHour(player));
            case "ancient-perk" -> player.getInventory().add(23149, 1);
            case "ancient-enhance" -> player.getInventory().add(20500, 1);
            case "ancient-tickets" -> player.getInventory().add(23253, 1);
            case "ancient-blue" -> player.getInventory().add(4558, 5);
            case "ancient-deep-blue" -> player.getInventory().add(4559, 5);
            case "ancient-coins" -> player.getInventory().add(995, 25000);

            case "crystal-prayer" -> player.getTimers().register(new PrayerMonicHour(player));
            case "crystal-damage" -> player.getTimers().register(new DDMGMonicHour(player));
            case "crystal-enhance" -> player.getInventory().add(20502, 1);
            case "crystal-tickets" -> player.getInventory().add(23255, 1);
            case "crystal-pack" -> player.getInventory().add(19001, 1);
            case "crystal-rare-perk" -> player.getInventory().add(23147, 1);
            case "crystal-white" -> player.getInventory().add(4560, 5);
            case "crystal-pinks" -> player.getInventory().add(4564, 5);
            case "crystal-coins" -> player.getInventory().add(10835, 5000);
        }
        player.sendMessage("You have activated the "+perk.name()+" perk.");
        sendInterface(player, id, false);
    }
}
