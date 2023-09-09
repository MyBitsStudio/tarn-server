package com.ruse.world.packages.vip;

import com.ruse.model.Item;
import com.ruse.world.WorldCalendar;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.ranks.VIPRank;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class VIP {

    // Amount given for free +25 (+20 bp)

    private final Player player;
    private int exp = 0, claimedTicket, claimedPack, total = 0, packXp = 0, points = 0;
    private List<Donation> donations = new CopyOnWriteArrayList<>();

    public VIP(Player player) {
        this.player = player;
    }

    public void addDonation(int amount) {
        exp += amount;
        packXp += amount;
        total += amount;
        points += (amount * 10);
        player.getPacketSender().sendMessage("@yel@[VIP] You have gained " + amount + " VIP experience. Currently at : "+ exp);
        addToList(amount);
        claimPack();
        reCalculate();
    }

    public void takePoints(int amount){
        this.points -= amount;
    }

    public void addPoints(int amount){
        this.points += amount;
    }

    private void addToList(int amount){
        donations.add(new Donation(amount, System.currentTimeMillis()));
    }

    private void reCalculate(){
        int level = calculateLevel();
        if(level != player.getVip().getRank()){
            player.sendMessage("@yel@[VIP] You have reached VIP level " + level + "!");
            player.setVip(VIPRank.forRank(level));
            if(rewardForLevel(level) != null) {
                player.getInventory().addDropIfFull(Objects.requireNonNull(rewardForLevel(level)).getId(), Objects.requireNonNull(rewardForLevel(level)).getAmount());
            }
        }
    }

    private void claimPack() {
        int bread = calculatePack();
        int deltaPacks = bread - claimedPack;

        if (deltaPacks > 0) {
            sendPack(claimedPack, deltaPacks);
            claimedPack += deltaPacks;

            if (claimedPack >= VIPPacks.values().length) {
                int amount = packXp - 1000;
                claimedPack = 0;
                packXp = amount;
                player.sendMessage("@yel@[VIP] You have finished your VIP track, and have reset, with "+amount+" rolled over.");
            }
        }
    }

    private void sendPack(int last, int packs){
        VIPPacks[] pack = VIPPacks.packsForIncrease(last, packs);
        for(VIPPacks p : pack){
            if(p != null)
                for(int i = 0; i < p.items.length; i++){
                    player.getInventory().addDropIfFull(p.items[i], p.amounts[i]);
                }
        }
    }

    public int calculateLevel() {
        if(exp < 50)
            return 0;
        if(exp < 100)
            return 1;
        if(exp < 250)
            return 2;
        if(exp < 500)
            return 3;
        if(exp < 750)
            return 4;
        if(exp < 1000)
            return 5;
        if(exp < 1500)
            return 6;
        if(exp < 2500)
            return 7;
        if(exp < 5000)
            return 8;
        if(exp < 10000)
            return 9;
        return 10;
    }

    public static @Nullable Item rewardForLevel(int level){
        return switch (level) {
            case 1 -> new Item(15330, 1);
            case 2 -> new Item(15328, 1);
            case 3 -> new Item(12630, 1);
            case 4 -> new Item(23330, 1);
            case 5 -> new Item(15230, 1);
            case 6 -> new Item(15231, 1);
            case 7 -> new Item(15232, 1);
            case 8 -> new Item(15234, 1);
            case 9 -> new Item(15233, 1);
            case 10 -> new Item(23276, 1);
            default -> null;
        };
    }

    public void addCertificate(){
        exp += 5;
        reCalculate();
    }

    public int calculatePack() {
        if(packXp < 50)
            return 0;
        if(packXp < 100)
            return 1;
        if(packXp < 150)
            return 2;
        if(packXp < 200)
            return 3;
        if(packXp < 250)
            return 4;
        if(packXp < 300)
            return 5;
        if(packXp < 350)
            return 6;
        if(packXp < 400)
            return 7;
        if(packXp < 500)
            return 8;
        if(packXp < 600)
            return 9;
        if(packXp < 700)
            return 10;
        if(packXp < 800)
            return 11;
        if(packXp < 900)
            return 12;
        if(packXp < 1000)
            return 13;
        return 14;
    }

    public void onLogin(){
        if(WorldCalendar.getInstance().getDay() != claimedTicket && player.getVip().getRank() > 0){
            claimedTicket = WorldCalendar.getInstance().getDay();
            int tickets = player.getVip().getRank();
            player.getInventory().addDropIfFull(23003, tickets);
            player.getPacketSender().sendMessage("You have gained " + tickets + " VIP tickets.");
        }
    }

    public void sendInterface(){
        reset();
        player.getPacketSender().sendInterface(77000);

        player.getPacketSender().sendString(77008, "Points : @gre@"+points);
        player.getPacketSender().sendString(77009, "Rank : @gre@"+player.getVip());
        player.getPacketSender().sendString(77010, "Exp : @gre@"+exp);
        player.getPacketSender().sendString(77011, "Instance : @gre@0M");
        player.getPacketSender().sendString(77012, "Tickets : @gre@"+(player.getVip().getRank() * 2));
        player.getPacketSender().sendString(77013, "Bonus : @gre@0%");

        int level = calculatePack();
        VIPPacks pack = VIPPacks.values()[level];

        int amount = pack.getAmount() - packXp;

        player.getPacketSender().sendString(77014, "Next : "+amount+"$");
        player.getPacketSender().sendItemOnInterface(77015, pack.getItems()[0], pack.getAmounts()[0]);
    }

    private void reset(){
        for(int i = 78001; i < 78001 + 35; i++){
            player.getPacketSender().sendString(i, "");
        }
    }

    @Getter
    enum VIPPacks {
        PACK_1(50, new int[]{23250}, new int[]{1}),
        PACK_2(100, new int[]{23251}, new int[]{1}),
        PACK_3(150, new int[]{23252}, new int[]{1}),
        PACK_4(200, new int[]{20502}, new int[]{2}),
        PACK_5(250, new int[]{23255}, new int[]{2}),
        PACK_6(300, new int[]{23107}, new int[]{5}),
        PACK_7(350, new int[]{23148}, new int[]{5}),
        PACK_8(400, new int[]{23147}, new int[]{3}),
        PACK_9(500, new int[]{3686}, new int[]{1}),
        PACK_10(600, new int[]{23256}, new int[]{5}),
        PACK_11(700, new int[]{23257}, new int[]{3}),
        PACK_12(800, new int[]{23258}, new int[]{2}),
        PACK_13(900, new int[]{23059}, new int[]{1}),
        PACK_14(1000, new int[]{23259}, new int[]{3})
        ;
        final int amount;
        final int[] items, amounts;
        VIPPacks(int amount , int[] items, int[] amounts){
            this.amount = amount;
            this.items = items;
            this.amounts = amounts;
        }

        public static VIPPacks @NotNull [] packsForIncrease(int amount, int packs){
            VIPPacks[] packs1 = new VIPPacks[packs];
            int start = 0;
            for(int i = amount; i < amount + packs; i++){
                if(i > VIPPacks.values().length)
                    break;
                packs1[start++] = VIPPacks.values()[i];
            }
            return packs1;
        }

    }

}
