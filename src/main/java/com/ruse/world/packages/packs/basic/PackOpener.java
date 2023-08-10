package com.ruse.world.packages.packs.basic;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.impl.remake.*;

public class PackOpener {

    public static boolean openPack(Player player, int id){
        switch(id){
            case 23250 -> {
                new CertPackI().openPack(player); return true;
            }
            case 23251 -> {
                new CertPackII().openPack(player); return true;
            }
            case 23252 -> {
                new CertPackIII().openPack(player); return true;
            }
            case 20500 -> {
                new EnhancePackI().openPack(player); return true;
            }
            case 20501 -> {
                new EnhancePackII().openPack(player); return true;
            }
            case 20502 -> {
                new EnhancePackIII().openPack(player); return true;
            }
            case 23253 -> {
                new TicketPackI().openPack(player); return true;
            }
            case 23254 -> {
                new TicketPackII().openPack(player); return true;
            }
            case 23255 -> {
                new TicketPackIII().openPack(player); return true;
            }
            case 23276 -> {
                new UltimateCertPack().openPack(player); return true;
            }

        }

        return false;
    }
}
