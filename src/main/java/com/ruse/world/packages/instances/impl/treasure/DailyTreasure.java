package com.ruse.world.packages.instances.impl.treasure;

import com.ruse.model.*;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.GroundItemManager;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.instances.Instance;
import org.jetbrains.annotations.NotNull;

public class DailyTreasure extends Instance {

    private final Player owner;
    private int deaths = 0;

    private final Item[] rewards = new Item[] {
        new Item(13727, 2500), new Item(13727, 5000), new Item(13727, 10000),
        new Item(10835, 5000), new Item(10835, 10000), new Item(23250, 1),
        new Item(23251, 1), new Item(23252, 1), new Item(23256, 3),
        new Item(23257, 2), new Item(23258, 1), new Item(23147, 5),
        new Item(10946, 5), new Item(10946, 10), new Item(10946, 20),
        new Item(604, 5), new Item(604, 10), new Item(604, 20),
        new Item(23107, 5)
    };

    public DailyTreasure(Player owner) {
        super(Locations.Location.TREASURE_HUNTER);
        this.owner = owner;
    }

    @Override
    public void start(){
        owner.sendMessage("You have started your daily Treasure Hunter instance.");

        moveTo(owner, new Position(2019, 5020));
        add(owner);

        spawnAll();
    }

    private void spawnAll(){
        TreasureBosses boss1 = new TreasureBosses(9028, new Position(1994, 5024, owner.getIndex() * 4));
        boss1.setInstance(this);
        boss1.setSpawnedFor(owner);
        add(boss1);

        TreasureBosses boss2 = new TreasureBosses(9029, new Position(2015, 5045, owner.getIndex() * 4));
        boss2.setInstance(this);
        boss2.setSpawnedFor(owner);
        add(boss2);

        TreasureBosses boss3 = new TreasureBosses(9030, new Position(2036, 5024, owner.getIndex() * 4));
        boss3.setInstance(this);
        boss3.setSpawnedFor(owner);
        add(boss3);

        TreasureBosses boss4 = new TreasureBosses(606, new Position(2015, 5003, owner.getIndex() * 4));
        boss4.setInstance(this);
        boss4.setSpawnedFor(owner);
        add(boss4);
    }

    @Override
    public boolean handleObjectClick(Player player, final @NotNull GameObject object, int option){
        switch(object.getId()){
            case 41207 -> {
                if(player.getInventory().contains(23106)){
                    player.getInventory().delete(23106, 1);
                    Item item = rewards[Misc.getRandom(rewards.length - 1)];
                    player.getInventory().add(item);
                    player.sendMessage("You have opened the Treasure Chest and received "+ItemDefinition.forId(item.getId()).getName()+".");
                    dispose();
                } else {
                    player.sendMessage("You need a Treasure Key to open this chest.");
                }
                return true;
            }
        }

        return false;
    }

    public void signalDeath(){
        if(++deaths >= 4){
            end();
        }
    }

    private void dispose(){
        this.destroy();
    }

    private void end(){
        GroundItemManager.spawnGroundItem(owner,
                new GroundItem(new Item(23106), new Position(2014, 5024, owner.getIndex() * 4), owner.getUsername(), false, 50, false, 1000));

        owner.sendMessage("@blu@[TREASURE]@whi@ A Key has spawn in front of the Treasure Chest.");
    }

}
