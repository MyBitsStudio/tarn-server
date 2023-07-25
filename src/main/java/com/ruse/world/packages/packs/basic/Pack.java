package com.ruse.world.packages.packs.basic;

import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Pack {

    public abstract void open(Player player);
    public abstract int getPackId();
    public abstract int openSpaces();

    public void openPack(@NotNull Player player){
        if(player.getInventory().contains(getPackId())){
            if(player.getInventory().getFreeSlots() < openSpaces()){
                player.sendMessage("You need at least " + openSpaces() + " free inventory spaces to open this pack.");
            } else {
                player.getInventory().delete(getPackId(), 1);
                open(player);
            }
        } else {
            player.sendMessage("How did you get here???");
        }
    }
}
