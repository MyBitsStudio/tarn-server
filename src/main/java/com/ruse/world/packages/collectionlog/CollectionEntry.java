package com.ruse.world.packages.collectionlog;

import com.ruse.world.entity.impl.player.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CollectionEntry {
    private int npcId;
    private int item;
    private int amount;

    public void submit(Player player) {
    	if(player.getCollectionLogData().stream().anyMatch(data -> data.npcId == npcId && data.item == item)) {
    		var edit = player.getCollectionLogData().stream().filter(data -> data.npcId == npcId && data.item == item).findFirst().get();
    		player.getCollectionLogData().stream().filter(data -> data.npcId == npcId && data.item == item).findFirst().get().setAmount(edit.getAmount() + 1);
    		return;
    	}
        player.getCollectionLogData().add(this);
        player.getCollectionLog().checkForObtainedAll(npcId);
    }
}
