package com.ruse.world.packages.instances.impl.casket;

import com.ruse.model.*;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.GroundItemManager;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.packages.instances.impl.treasure.TreasureBosses;
import org.jetbrains.annotations.NotNull;

public class DailyCasket extends Instance {

    private final Player owner;
    private int deaths = 0;
    private boolean green, red, blue;

    private final Item[] rewards = new Item[] {
        new Item(20083, 1), new Item(20083, 1), new Item(20083, 1),
        new Item(10835, 5000), new Item(10835, 10000), new Item(17831, 100),
        new Item(17831, 250), new Item(17831, 500), new Item(20422, 3),
        new Item(20422, 5), new Item(20422, 10), new Item(1960, 5),
        new Item(1960, 10), new Item(1960, 20), new Item(10946, 5),
        new Item(10946, 10),new Item(10946, 20)
    };

    public DailyCasket(Player owner) {
        super(Locations.Location.CASKET_RAID);
        this.owner = owner;
    }

    @Override
    public void start(){
        owner.sendMessage("You have started your daily Casket Raid instance.");

        moveTo(owner, new Position(1954, 5010));
        add(owner);

        spawnAll();
    }

    private void spawnAll(){


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
            case 41203 -> {
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
            case 41202 -> {
                if(!green){
                    green = true;
                    CasketBosses boss1 = new CasketBosses(8011, new Position(1936, 5025, owner.getIndex() * 4));
                    boss1.setInstance(this);
                    boss1.setSpawnedFor(owner);
                    add(boss1);
                    player.sendMessage("Fight for your life!");
                }
                return true;
            }
            case 41201 -> {
                if(!blue){
                    blue = true;
                    CasketBosses boss1 = new CasketBosses(8011, new Position(1954, 5027, owner.getIndex() * 4));
                    boss1.setInstance(this);
                    boss1.setSpawnedFor(owner);
                    add(boss1);
                    player.sendMessage("Fight for your life!");
                }
                return true;
            }
            case 41200 -> {
                if(!red){
                    red = true;
                    CasketBosses boss1 = new CasketBosses(8011, new Position(1971, 5017, owner.getIndex() * 4));
                    boss1.setInstance(this);
                    boss1.setSpawnedFor(owner);
                    add(boss1);
                    player.sendMessage("Fight for your life!");
                }
                return true;
            }
        }

        return false;
    }

    public void signalDeath(){
        if(++deaths >= 3){
            end();
        }
    }

    private void dispose(){
        this.destroy();
    }

    private void end(){
        owner.getInventory().add(new Item(23106, 1));

        owner.sendMessage("@blu@[EVENT]@whi@ A Key has spawned in your inventory!");
    }

}
