package com.ruse.world.packages.packs.locked;

import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public abstract class LockBox {

    protected final int masterKey = 23107;

    public abstract int key();

    public abstract Item[] commons();
    public abstract Item[] rares();
    public abstract Item[] ultra();

    public abstract int boxId();

    public void open(@NotNull Player player){
        if(player.getInventory().contains(key()) || player.getInventory().contains(masterKey)){
            if(player.getInventory().contains(key())){
                player.getInventory().delete(key(), 1);
            } else {
                player.getInventory().delete(masterKey, 1);
            }
        } else {
            player.getPacketSender().sendMessage("You need a "+ ItemDefinition.forId(key()).getName()+" to open this box.");
            return;
        }
        player.getInventory().delete(boxId(), 1);
        int misc = Misc.random(2650);

        Item[] items;

        if(misc >= 2122 && misc <= 2171){
            items = ultra();
        } else if(misc >= 1467 && misc <= 1951){
            items = rares();
        } else {
            items = commons();
        }

        int chose = Misc.random(items.length - 1);

        player.getInventory().add(items[chose]);
        player.getPacketSender().sendMessage("You have received a " + items[chose].getDefinition().getName() + " from the lockbox.");

    }
}
