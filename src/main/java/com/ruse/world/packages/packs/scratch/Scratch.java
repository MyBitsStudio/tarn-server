package com.ruse.world.packages.packs.scratch;

import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.RandomUtility;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import lombok.Setter;

public abstract class Scratch {

    private final Player player;
    private Item item1, item2, item3;

    @Setter
    private boolean scratching = false;

    public Scratch(Player player) {
        this.player = player;
    }

    public abstract Item[] commons();
    public abstract Item[] rares();

    public abstract int cardId();

    public void open() {
        player.getPacketSender().sendInterface(23630);
        for (int i = 0; i < commons().length; i++)
            player.getPacketSender().sendItemOnInterface(23642, commons()[i].getId(), i, commons()[i].getAmount());
        for (int x = 0; x < rares().length; x++)
            player.getPacketSender().sendItemOnInterface(23645, rares()[x].getId(), x, rares()[x].getAmount());
    }

    public void scratch() {
        if (scratching) {
            player.sendMessage("@red@Please wait till current game is finished");
            return;
        }

        if (player.getInventory().contains(cardId())) {
            player.getInventory().delete(cardId(), 1);
        } else {
            player.sendMessage("U don't have a scratchcard.");
            return;
        }

        int random = RandomUtility.exclusiveRandom(10);
        Item[] itemsArray = (random == 3) ? rares() : commons();
        int arrayLength = itemsArray.length - 1;

        item1 = itemsArray[RandomUtility.inclusiveRandom(0, arrayLength)];
        item2 = itemsArray[RandomUtility.inclusiveRandom(0, arrayLength)];
        item3 = itemsArray[RandomUtility.inclusiveRandom(0, arrayLength)];

        player.getPacketSender().sendScratchcardItems(item1.getId(), item2.getId(), item3.getId());
        scratching = true;
    }

    public void getWinnings() {
        int count = 0;

        int id1 = item1.getId();
        int id2 = item2.getId();
        int id3 = item3.getId();

        if (id1 == id2 && id1 == id3) {
            count = 2;
        } else if (id1 == id2 || id1 == id3 || id2 == id3) {
            count = 1;
        }

        if (count == 0) {
            player.sendMessage("None of the same ones so u don't win anything :(");
        } else if (count == 1) {
            player.sendMessage("Congrats, there were 2 of the same matches. You were given 5$ as a reward!");
            player.getInventory().add(10946,5);
        } else {
            player.sendMessage("WINNER! there were 3 of the same matches!");
            player.getInventory().add(item1);
            String name = ItemDefinition.forId(item1.getId()).getName();
            World.sendMessage("@blu@<img=5>[SCRATCHCARD]<img=5> @red@" + player.getUsername() + " has got an " + name
                    + " from scratchcards!");
        }

    }


}
