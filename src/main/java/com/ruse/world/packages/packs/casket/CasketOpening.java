package com.ruse.world.packages.packs.casket;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.casket.packs.*;
import com.ruse.world.packages.packs.casket.packs.limited.HalloweenCracker;
import com.ruse.world.packages.packs.casket.packs.limited.LimitedMaster1;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class CasketOpening {

    private final Player player;
    private final int INTERFACE_ID = 110000;
    private boolean canCasketOpening = true;
    private Box slotPrize;
    private Caskets currentCasket;
    public CasketOpening(Player player) {
        this.player = player;
    }


    public static Box getLoot(Box[] loot) {
        HashMap<Double, ArrayList<Box>> dropRates = new HashMap<>();
        ArrayList<Box> potentialDrops = new ArrayList<>();

        for (Box drop : loot) {
            if (drop == null)
                continue;
            double divisor = drop.getRate();
            if (dropRates.containsKey(divisor)) {
                dropRates.get(divisor).add(drop);
            } else {
                ArrayList<Box> items = new ArrayList<>();
                items.add(drop);
                dropRates.put(divisor, items);
            }
        }
        for (double dropRate : dropRates.keySet()) {
            double rate = dropRate * 1000;
            if (Misc.getRandom(100000) <= rate) {
                potentialDrops.add(dropRates.get(dropRate).get(Misc.getRandom(dropRates.get(dropRate).size() - 1)));
            }
        }

        if (potentialDrops.size() > 0) {
            return potentialDrops.get(Misc.getRandom((potentialDrops.size() - 1)));
        } else {
            return loot[Misc.getRandom(1)];
        }
    }

    public static Box getLoot1(Box[] loot) {
        HashMap<Double, ArrayList<Box>> dropRates = new HashMap<>();
        ArrayList<Box> potentialDrops = new ArrayList<>();

        for (Box drop : loot) {
            if (drop == null)
                continue;
            double divisor = drop.getRate();
            if (dropRates.containsKey(divisor)) {
                dropRates.get(divisor).add(drop);
            } else {
                ArrayList<Box> items = new ArrayList<>();
                items.add(drop);
                dropRates.put(divisor, items);
            }
        }
        for (double dropRate : dropRates.keySet()) {
            double rate = dropRate * 1000;
            if (Misc.getRandom(100000) <= rate) {
                potentialDrops.add(dropRates.get(dropRate).get(Misc.getRandom(dropRates.get(dropRate).size() - 1)));
            }
        }

        if (potentialDrops.size() > 0) {
            return potentialDrops.get(Misc.getRandom((potentialDrops.size() - 1)));
        } else {
            return loot[Misc.getRandom(loot.length - 1)];
        }
    }

    public boolean hasItems() {
        if (!player.getInventory().contains(getCurrentCasket().getItemID())) {
            player.sendMessage("You need a " + ItemDefinition.forId(getCurrentCasket().getItemID()).getName() + " to do this.");
            return false;
        }
        return true;
    }

    public boolean removeItems() {
        if (player.getInventory().getAmount(getCurrentCasket().getItemID()) >= 1) {
            player.getInventory().delete(getCurrentCasket().getItemID(), 1);
        }
        return false;
    }

    public void spin() {
        if (getCurrentCasket() == null) {
            return;
        }

        if (!canCasketOpening) {
            player.sendMessage("Please finish your current spin.");
            return;
        }
        if (hasItems()) {
            if (player.getInventory().getFreeSlots() == 0) {
                player.getPacketSender().sendMessage("You don't have enough free inventory space.");
                return;
            }
            removeItems();
            player.setSpinning(true);
            player.getMovementQueue().setLockMovement(true);
            player.sendMessage(":resetCasket");
            player.sendMessage(":spinCasket");
            process();
        }
    }

    public void quickSpin() {
        if (getCurrentCasket() == null) {
            return;
        }
        if (!canCasketOpening) {
            player.sendMessage("Please finish your current spin.");
            return;
        }
        if (hasItems()) {
            if (player.getInventory().getFreeSlots() == 0) {
                player.getPacketSender().sendMessage("You don't have enough free inventory space.");
                return;
            }
            removeItems();
            player.sendMessage(":resetCasket");
            processQuick();
        }
    }

    public void process() {
        slotPrize = null;
        canCasketOpening = false;
        Box[] loot =getCurrentCasket().getLoot() ;
        slotPrize = getLoot1(loot);
        if (slotPrize.getRate() < 10D && Misc.getRandom(1) == 0){
            slotPrize = getLoot1(loot);
        }
        if (slotPrize.getRate() < 10D && Misc.getRandom(2) == 0){
            slotPrize = getLoot1(loot);
        }

        boolean announce = slotPrize.isAnnounce();

        for (int i = 0; i < 28; i++) {
            Box NOT_PRIZE = getLoot1(loot);
            if (NOT_PRIZE.getRate() > 10 && Misc.getRandom(2) == 0) {
                NOT_PRIZE = getLoot1(loot);
            }
            sendItem(i, 23, slotPrize.getId(), slotPrize.getMax(), NOT_PRIZE.getId(), NOT_PRIZE.getMax(), 110501);
        }

        final boolean announceLoot = announce;
        TaskManager.submit(new Task(7, player, false) {

            @Override
            public void execute() {
                reward(announceLoot);
                player.setSpinning(false);
                player.getMovementQueue().setLockMovement(false);
                stop();
            }
        });
    }

    public void processQuick() {
        slotPrize = null;
        canCasketOpening = false;
        Box[] loot =getCurrentCasket().getLoot() ;
        slotPrize = getLoot1(loot);
        if (slotPrize.getRate() < 10D && Misc.getRandom(1) == 0){
            slotPrize = getLoot1(loot);
        }
        if (slotPrize.getRate() < 10D && Misc.getRandom(2) == 0){
            slotPrize = getLoot1(loot);
        }


        boolean announce = slotPrize.isAnnounce();
        for (int i = 0; i < 7; i++) {
            Box NOT_PRIZE = getLoot1(loot);
            if (NOT_PRIZE.getRate() > 10 && Misc.getRandom(2) == 0) {
                NOT_PRIZE = getLoot1(loot);
            }
            sendItem(i, 3, slotPrize.getId(), slotPrize.getMax(), NOT_PRIZE.getId(), NOT_PRIZE.getMax(), 110501);
        }

        reward(announce);
        player.setSpinning(false);
    }

    public void sendItem(int i, int prizeSlot, int PRIZE_ID, int prizeamount, int NOT_PRIZE, int amount,
                         int ITEM_FRAME) {
        if (i == prizeSlot) {
            player.sendMessage("casketopening##" + ITEM_FRAME + "##" + PRIZE_ID + "##" + prizeamount + "##" + i + "##");
        } else {
            player.sendMessage("casketopening##" + ITEM_FRAME + "##" + NOT_PRIZE + "##" + amount + "##" + i + "##");
        }
    }

    public void reward(boolean announce) {
        if (slotPrize == null) {
            return;
        }
        ItemDefinition definition = ItemDefinition.forId(slotPrize.getId());
        int amount = slotPrize.getAmount();
        String name = definition.getName();
        if(definition.isEquitable) {
            Item item = new Item(slotPrize.getId(), amount);
            player.getInventory().add(item);
        } else {
            player.getInventory().add(slotPrize.getId(), amount);
        }
        player.sendMessage(
                "@red@You won x" + amount + " " + name);

        if (announce) {
            String message = "<img=5> @blu@News: @or2@" +"@red@" + player.getUsername() + " <col=ff812f>has just received @red@"
                    + (amount > 1 ? "x" + amount : "") + " "
                    + name + "<col=ff812f> from a @red@" +
                    ItemDefinition.forId(currentCasket.getItemID()).getName() + "!";
            World.sendFilterMessage(message);
        }

        canCasketOpening = true;
    }

    public void openInterface() {
        player.sendMessage(":resetCasket");

        player.getPacketSender().sendItemOnInterface(110009, 13759, 1);
        player.getPacketSender().sendItemOnInterface(110010, 13758, 1);

        Box[] loot = getCurrentCasket().getLoot();

        int length = loot.length;
        if (length >= 160)
            length = 160;
        if (length <= 16)
            length = 16;

        length += 8 - (length % 8);

        for (int i = 0; i < length; i++) {
            if (loot.length > i)
                player.getPacketSender().sendItemOnInterface(110101 + i, loot[i].getId(), loot[i].getMax());
            else
                player.getPacketSender().sendItemOnInterface(110101 + i, -1, 0);
        }

        for (int i = 0; i < length; i++) {
            if (loot.length > i)
                player.getPacketSender().sendString(110261 + i, "1/" + getRate(loot[i].getRate()));
            else
                player.getPacketSender().sendString(110261 + i, "");
        }
        int scroll = 9 + ((loot.length / 8) + 1) * 55;
        if (scroll <= 165)
            scroll = 165;
        player.getPacketSender().setScrollBar(110100, scroll);


        player.getPA().sendInterface(INTERFACE_ID);
    }

    public int getRate(double rate) {
        return (int) (100 / rate);
    }


    public Caskets getCurrentCasket() {
        return currentCasket;
    }

    public void setCurrentCasket(Caskets currentCasket) {
        this.currentCasket = currentCasket;
    }

    public boolean handleAction(int id){
        Optional<Caskets> casket = Arrays.stream(Caskets.values()).filter(c -> c.itemID == id).findFirst();
        if(casket.isPresent()){
            setCurrentCasket(casket.get());
            openInterface();
            return true;
        }
        return false;
    }

    @Getter
    public enum Caskets {

        STARTER_I(15004, StarterIBox.loot),
        STARTER_II(23300, StarterIIBox.loot),
        STARTER_III(23301, StarterIIIBox.loot),

        DONATOR_BOX_I(23256, DonatorBoxI.loot),
        DONATOR_BOX_II(23257, DonatorBoxII.loot),
        DONATOR_BOX_III(23258, DonatorBoxIII.loot),

        ULTIMATE_DONATOR(23259, UltimateDonatorBox.loot),

        LIMITED_MASTERS(23260, LimitedMaster1.loot),

        VOTE_BOX(18768, VoteBox.loot),

        SLAYER_CASKET(2734, SlayerCasket.loot),

        PvM_CASKET(2736, PvMCasket.loot),
        ASTA_BOX(20488, AstaFireChest.loot),
        DEATH_BOX(14490, DeathChest.loot),
        TARN_CHEST(23002, TarnMasterChest.loot),
        H_CRACKER(20083, HalloweenCracker.loot),
        ;
        private final int itemID;
        private final Box[] loot;

        Caskets(int itemID, Box[] loot) {
            this.itemID = itemID;
            this.loot = loot;
        }
    }

}