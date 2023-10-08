package com.ruse.world.packages.chests;

import com.ruse.model.Animation;
import com.ruse.model.GameObject;
import com.ruse.model.Graphic;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.World;
import com.ruse.world.content.CustomObjects;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.chests.impl.SlayerChest;

public class ChestHandler {

    public static boolean openChest(Player player, GameObject id){
        if(!World.attributes.getSetting("chests")){
            if(!player.getRank().isStaff()) {
                player.sendMessage("This feature is currently disabled.");
                return true;
            }
        }
        Chests chests = Chests.get(id.getId());
        if(chests == null)
            return false;

        Chest chest = null;

        switch(chests){
            case SLAYER_CHEST ->
                chest = new SlayerChest();
        }

        if(chest == null)
            return false;

        if(!player.getInventory().contains(chest.keyId(), 5) && !player.getInventory().contains(23107, 5)){
            player.sendMessage("You need 5 "+ ItemDefinition.forId(chest.keyId()).getName()+" or 5 Master Keys to open this chest.");
            return true;
        }

        if(chest.animationId() != -1){
            player.performAnimation(new Animation(chest.animationId()));
        }

        if(chest.graphicId() != -1){
            player.performGraphic(new Graphic(chest.graphicId()));
        }

        if(chest.open(player)){
            CustomObjects.globalObjectRespawnTask(new GameObject(chests.getOpenId(), id.getPosition().copy(), 10, id.objectFace), id, 10);
            player.sendMessage("You have opened the chest and found an amazing reward!");
            return true;
        }

        return false;
    }
}
