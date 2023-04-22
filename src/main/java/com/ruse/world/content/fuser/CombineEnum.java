package com.ruse.world.content.fuser;

import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.Setter;

public enum CombineEnum {


    AURA(new Item[] {new Item(8136,1),new Item(10949,5),
            new Item(10835,5000000) },12537,100, -1),
    AURA2(new Item[] {new Item(8136,1),new Item(10949,5),
            new Item(10835,5000000) },12537,100, -1),
    AURA3(new Item[] {new Item(8136,1),new Item(10949,5),
            new Item(10835,5000000) },12537,100, -1),
    AURA4(new Item[] {new Item(8136,1),new Item(10949,5),
            new Item(10835,5000000) },12537,100, -1),
    AURA5(new Item[] {new Item(8136,1),new Item(10949,5),
            new Item(10835,5000000) },12537,100, -1),
    AURA6(new Item[] {new Item(8136,1),new Item(10949,5),
            new Item(10835,5000000) },12537,100, -1),
    AURA7(new Item[] {new Item(8136,1),new Item(10949,5),
            new Item(10835,5000000) },12537,100, -1);

    CombineEnum(Item[] requirements, int endItem, int chance, long timer) {
        this.requirements = requirements;
        this.endItem = endItem;
        this.chance = chance;
        this.timer = timer;
    }

    Item[] requirements;
    int endItem;
    int chance;
    @Getter
    @Setter
    long timer; //TIME IS IN MILLISECONDS.. 1h = 3600000

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public Item[] getRequirements() {
        return requirements;
    }

    public int getEndItem() {
        return endItem;
    }



    public static boolean checkRequirements(CombineEnum combine, Player player) {
        if (!(System.currentTimeMillis() >= player.getFuseCombinationTimer())) {
            player.sendMessage("You have @red@" + CombineHandler.timeLeft(player) + "@bla@ until you can claim this item.");
            player.getPacketSender().sendInterfaceRemoval();
            return false;
        }



        Item[] reqs = combine.getRequirements();
        if (player.getInventory().contains(reqs)){
          //  System.out.println("True! Had all items");
        } else {
          //  System.out.println("FALSE! Had all items");

        }

        return player.getInventory().contains(reqs);
    }

    public static void handlerFuser(Player player, CombineEnum chosenItem) {

        if (player.isFuseInProgress() && player.getFuseCombinationTimer() > 0) {
            player.getPacketSender()
                    .sendMessage("@red@You have not finished fusing your @blu@"
                            + ItemDefinition.forId(player.getFuseItemSelected()).getName()
                            + "@red@ yet!");
            return;
        }

        if (!player.isClaimedFuseItem() && player.getFuseItemSelected() > 0) {
            player.getPacketSender()
                    .sendMessage("@red@You haven't claimed your @blu@"
                            + ItemDefinition.forId(player.getFuseItemSelected()).getName()
                            + "@red@ yet!");
            return;
        }

        if (checkRequirements(chosenItem, player)) {
            removeRequirements(chosenItem, player);
            player.setFuseCombinationTimer(System.currentTimeMillis() + (chosenItem.getTimer()));
            player.setClaimedFuseItem(false);
            player.setFuseInProgress(true);
            player.setFuseItemSelected(chosenItem.getEndItem());
            player.getPacketSender().sendString(43541, CombineHandler.timeLeft(player));
        } else
            player.sendMessage("You don't meet the requirements for this item!");
    }


    public static void removeRequirements(CombineEnum combine, Player player){
        Item[] reqs = combine.getRequirements();
        for(Item req : reqs) {
            if(player.getInventory().contains(new Item[] {req})) {
                player.getInventory().delete(req.getId(),req.getAmount());
                player.sendMessage("@bla@ Removed "+req.getAmount()+"x "+ ItemDefinition.forId(req.getId()).getName() + " From your inventory!");
            }
        }
    }

    // OLD AND SHIT
    public static void claimItem(Player player) {
        if (System.currentTimeMillis() >= player.getFuseCombinationTimer()){
            player.setFuseInProgress(false);
        }
        if (!player.isClaimedFuseItem() && !player.isFuseInProgress() && player.getFuseItemSelected() > 0){ //If the player has an unclaimed item, and a fuse is not in progress
            if (player.getInventory().getFreeSlots() <= 1){
                player.getPacketSender().sendMessage("You need 1 free slot to claim your fused item!");
                return;
            }
            player.getInventory().add(player.getFuseItemSelected(), 1);
           // World.sendMessageDiscord("[News] " + player.getUsername() + "has Fused a " + ItemDefinition.forId(player.getFuseItemSelected()).getName() + "!");
            player.setClaimedFuseItem(true);
            player.setFuseInProgress(false);
            player.setFuseItemSelected(0);
        }
    }
}
