package com.ruse.world.packages.event.impl;

import com.ruse.model.GroundItem;
import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.entity.impl.GroundItemManager;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.event.Event;
import com.ruse.world.packages.items.loot.Lootbag;
import com.ruse.world.packages.mode.impl.UltimateIronman;

public class DoubleDropEvent extends Event {
    @Override
    public String name() {
        return "DoubleDrop";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onLogin(Player player) {
        player.sendMessage("@red@[EVENT]@whi@ Double Drop event is active!");
    }

    @Override
    public boolean handleItem(Player player, Item item) {
        return false;
    }

    @Override
    public boolean handleNpc(Player player, int npcId, int option) {
        return false;
    }

    @Override
    public boolean handleObject(Player player, int objectId, int option) {
        return false;
    }

    @Override
    public boolean onDeath(Player player, int npcId) {
        return false;
    }

    @Override
    public void handleEventDrop(Player player, Item drop, Position pos) {
        boolean collector = player.getEquipment().hasCollector();
        boolean hasLoot = player.getInventory().contains(Lootbag.LOOT_DEVICE);
        if(collector || hasLoot){
            if(player.getMode() instanceof UltimateIronman){
                player.getUimBank().depositForced(drop);
            } else {
                player.depositItemBank(drop);
            }
        } else {
            GroundItemManager.spawnGroundItem(player,
                    new GroundItem(drop, pos, player.getUsername(), false, 150, false, 200));
        }
        if(player.getPSettings().getBooleanValue("drop-message-personal"))
            player.sendMessage("@red@[EVENT]@whi@ Double Drop has doubled your drop!.");
    }

}
