package com.ruse.world.packages.vip;

import com.ruse.model.Item;
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

    private final Player player;
    private int exp, claimedTicket, claimedPack, total, packXp;
    private List<Donation> donations = new CopyOnWriteArrayList<>();

    public VIP(Player player) {
        this.player = player;
    }

    public void addDonation(int amount, int[] items) {
        exp += amount;
        packXp += amount;
        total += amount;
        player.getPacketSender().sendMessage("@yel@[VIP] You have gained " + amount + " VIP experience. Currently at : "+ exp);
        addToList(items, amount);
        claimPack();
        reCalculate();
    }

    private void addToList(int[] item, int amount){
        donations.add(new Donation(item, amount, System.currentTimeMillis()));
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
        if(exp < 25)
            return 0;
        if(exp < 50)
            return 1;
        if(exp < 150)
            return 2;
        if(exp < 300)
            return 3;
        if(exp < 500)
            return 4;
        if(exp < 750)
            return 5;
        if(exp < 1000)
            return 6;
        if(exp < 1500)
            return 7;
        if(exp < 2500)
            return 8;
        if(exp < 5000)
            return 9;
        return 10;
    }

    public static @Nullable Item rewardForLevel(int level){
        return switch (level) {
            case 1 -> new Item(20501, 2);
            case 2 -> new Item(23225, 3);
            case 3 -> new Item(23204, 20);
            case 4 -> new Item(23203, 20);
            case 5 -> new Item(15328, 1);
            case 6 -> new Item(23252, 1);
            case 7 -> new Item(15330, 1);
            case 8 -> new Item(23253, 1);
            case 9 -> new Item(23002, 2);
            case 10 -> new Item(20507, 1);
            default -> null;
        };
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
        if(Calendar.DAY_OF_MONTH != claimedTicket && player.getVip().getRank() > 0){
            claimedTicket = Calendar.DAY_OF_MONTH;
            int tickets = player.getVip().getRank() * 2;
            player.getInventory().addDropIfFull(23003, tickets);
            player.getPacketSender().sendMessage("You have gained " + tickets + " VIP tickets.");
        }
    }

    public void sendInterface(){

    }

    enum VIPPacks {
        PACK_1(50, new int[]{20501}, new int[]{1}),
        PACK_2(100, new int[]{20502}, new int[]{1}),
        PACK_3(150, new int[]{23225}, new int[]{1}),
        PACK_4(200, new int[]{23217}, new int[]{5}),
        PACK_5(250, new int[]{20506}, new int[]{2}),
        PACK_6(300, new int[]{23204}, new int[]{10}),
        PACK_7(350, new int[]{23209}, new int[]{10}),
        PACK_8(400, new int[]{23217}, new int[]{10}),
        PACK_9(500, new int[]{3686}, new int[]{1}),
        PACK_10(600, new int[]{23060}, new int[]{2}),
        PACK_11(700, new int[]{20501}, new int[]{4}),
        PACK_12(800, new int[]{20490}, new int[]{2}),
        PACK_13(900, new int[]{23218}, new int[]{5}),
        PACK_14(1000, new int[]{23002}, new int[]{1})
        ;
        @Getter
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
