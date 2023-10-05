package com.ruse.world.content.teleport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ruse.model.Item;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.drops.Drop;
import com.ruse.world.packages.combat.drops.DropManager;
import org.jetbrains.annotations.NotNull;

public class TeleInterface {

    private Player player;
    private TeleInterfaceData[] category;
    private TeleInterfaceData data;

    public TeleInterface(Player player) {
        this.player = player;
    }

    public void open() {
        setCategory(TeleInterfaceData.forCategory(TeleInterfaceCategory.MONSTERS));
        player.getPacketSender().sendString(19007, "Monsters").sendString(19008, "Slayer").sendString(19009, "Minigames").sendString(19010, "Misc");
        player.getPacketSender().sendInterface(19000);
    }

    public void openCategory(TeleInterfaceCategory category) {
        if(category == getCategory()[0].getCategory()) {
            //player.sendMessage("You are already viewing the " + category.toString() + " category.");
            return;
        }
        setCategory(TeleInterfaceData.forCategory(category));
    }
    public void teleport() {
        if(getData() == null) {
            player.sendMessage("Please select a teleport before clicking this button.");
            return;
        }
        if(getData() == TeleInterfaceData.DAILY_DONATOR){
            player.getPlayerDailies().enterDonatorMaterialZone(player);
        } else if(getData() == TeleInterfaceData.DAILY_TREASURE){
            player.getPlayerDailies().enterTreasureHunterInstance(player);
        } else {
            TeleportHandler.teleportPlayer(player, getData().getLocation().getPosition(), TeleportType.NORMAL);
        }

    }

    public void sendItems() {
        if(getData().getDrops() == null) {
            loadItems();
        }
        for(int i = 0; i < 18; i++) {
            if(getData().getDrops().length > i) {
                player.getPacketSender().sendItemOnInterface(19093, getData().getDrops()[i].getId(), i, getData().getDrops()[i].getAmount());
            } else {
                player.getPacketSender().sendItemOnInterface(19093, -1, i, 0);
            }
        }
    }

    public boolean handleButton(int id) {
        if(id >= 19056 && id <= 19090) {
            int index = id - 19056;
            if(index >= getCategory().length)
                return false;
            setData(getCategory()[index]);
            return true;
        }
        switch (id) {
            case 19016 -> {
                teleport();
                return true;
            }
            case 19003 -> {
                openCategory(TeleInterfaceCategory.MONSTERS);
                return true;
            }
            case 19004 -> {
                openCategory(TeleInterfaceCategory.BOSSES);
                return true;
            }
            case 19005 -> {
                openCategory(TeleInterfaceCategory.MINIGAMES);
                return true;
            }
            case 19006 -> {
                openCategory(TeleInterfaceCategory.MISC);
                return true;
            }
        }
        return false;
    }

    public void loadItems() {
        List<Drop> drop = getCleanList();

        List<Item> list = new ArrayList<>();

        for (int i = 0; i < 18; i++) {
            if(i == drop.size())
                break;
            Item item = new Item(drop.get(i).id(), drop.get(i).max());
            if (item.getDefinition() == null)
                continue;
            if(item.getId() == -1 || item.getAmount() == -1)
                continue;
            list.add(item);
        }
        getData().setDrops(list.toArray(new Item[0]));
    }

    public List<Drop> getCleanList() {
        CopyOnWriteArrayList<Drop> drop = new CopyOnWriteArrayList<>(Arrays.asList(DropManager.getManager().forId(getData().getNpcId()).customTable().drops()));
        for(Drop item : drop) {
            if(item == null)
                drop.remove(item);
            if(item.id() == -1 || item.min() == -1)
                drop.remove(item);
        }
        return drop;
    }

    public TeleInterfaceData[] getCategory() {
        return this.category;
    }
    public void setCategory(TeleInterfaceData @NotNull [] category) {
        this.category = category;
        setData(category[0]);
        for(int i = 0; i < 35; i++)
            player.getPacketSender().sendString(19056 + i, i >= getCategory().length ? "" : getCategory()[i].getName());
    }
    public TeleInterfaceData getData() {
        return this.data;
    }
    public void setData(TeleInterfaceData data) {
        this.data = data;
        player.getPacketSender().sendString(19012, getData().getName());
        player.getPacketSender().sendNpcOnInterface(19013, getData().getNpcId(), getData().getZoom());
        player.getPacketSender().sendString(19015, getData().getLocation().getDescription());
        sendItems();
    }

}