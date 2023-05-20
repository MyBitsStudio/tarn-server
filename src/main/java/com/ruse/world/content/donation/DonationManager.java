package com.ruse.world.content.donation;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import com.ruse.GameSettings;
import com.ruse.engine.GameEngine;
import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.security.save.impl.PlayerDonationSave;
import com.ruse.world.World;
import com.ruse.world.content.discordbot.AdminCord;
import com.ruse.world.content.discordbot.JavaCord;
import com.ruse.world.content.donation.boss.DonationBoss;
import com.ruse.world.content.donation.boss.DonationMinion;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DonationManager {

    private static DonationManager instance;

    public static DonationManager getInstance() {
        if (instance == null) {
            instance = new DonationManager();
        }
        return instance;
    }

    private DonationBoss boss;
    private final List<DonationMinion> minions = new CopyOnWriteArrayList<>();
    @Getter
    private int totalDonated;
   @Getter private static int totalNeeded = 350;

    public void nullBoss(){
        if(boss != null){
            boss.setDying(true);
            World.deregister(boss);
            boss = null;
        }

        for(DonationMinion minion : minions){
            minion.setDying(true);
            World.deregister(minion);
        }
        minions.clear();
        check();
    }

    public void forceNull(){
        if(boss != null){
            boss.setDying(true);
            World.deregister(boss);
            boss = null;
        }

        for(DonationMinion minion : minions){
            minion.setDying(true);
            World.deregister(minion);
        }
        minions.clear();
    }

    public DonationBoss getBoss() {
        return boss;
    }

    public List<DonationMinion> getMinions() {
        return minions;
    }

    public void addToTotalDonation(int amount){
        totalDonated += amount;

        World.sendMessage("<img=857><col=FF0000><shad=1>[DBOSS] +"+ amount+"$ towards donation boss - "+totalDonated+"$ / "+totalNeeded+"$");
        save();
        check();
    }

    public void addToTotalDonation(int itemId, int amount){
        double price = StorePacks.getPriceForItem(itemId);
        Preconditions.checkArgument(price > 0, "Price for item " + itemId + " is not set.");

        totalDonated += (price * amount);

        World.sendMessage("<img=857><col=FF0000><shad=1>[DBOSS] +"+(price * amount)+"$ towards donation boss - "+totalDonated+"$ / "+totalNeeded+"$");

        save();
        check();
    }
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final JsonObject jsonObject = new JsonObject();

    private void save() {
        GameEngine.submit(() -> {
            synchronized(jsonObject) { // synchronize access to jsonObject to avoid race conditions
                jsonObject.addProperty("amount", totalDonated);
                jsonObject.addProperty("lastSaved", System.currentTimeMillis());
                try (FileWriter writer = new FileWriter("./data/saves/donations.json")) {
                    gson.toJson(jsonObject, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void load() {
        try (FileReader reader = new FileReader("./data/saves/donations.json")) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            if (jsonObject != null) {
                totalDonated = jsonObject.get("amount").getAsInt();
            }
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
        }

        check();
    }

    private void check(){
        if(totalDonated >= totalNeeded){
            totalDonated -= totalNeeded;
            save();
            spawnBoss();
        }
    }

    public void forceSpawn(){
        if(boss != null){
            forceNull();
        }

        spawnBoss();
    }

    private void spawnBoss(){
        if(boss == null){
            boss = new DonationBoss();

            World.register(boss);
            World.sendMessage(
                    "<img=28><shad=f9f6f6>Donation boss has spawned at ::donboss . Get it now before its gone!<shad=-1>");
            JavaCord.sendMessage("\uD83E\uDD16│\uD835\uDDEE\uD835\uDDF0\uD835\uDE01\uD835\uDDF6\uD835\uDE03\uD835\uDDF6\uD835\uDE01\uD835\uDE06", "**Donation boss has spawned at ::donboss . Get it now before its gone!**");
        }
    }

    public void spawnMinion(Position pos){
        if(boss == null) {
            System.out.println("Boss is null");
        } else {
            DonationMinion minion = new DonationMinion(pos);
            minions.add(minion);
            World.register(minion);
        }
    }

    public void claimDonation(Player player){
        GameEngine.submit(() -> {
            try {
                com.everythingrs.donate.Donation[] donations = com.everythingrs.donate.Donation.donations("szUziRZAX4r853p8wSF7icsTKkqLS8VgtLLJ2TQinhWRHIXqT5nyWvMQON924RSg5iOos4u4",
                        player.getUsername());
                if (donations.length == 0) {
                    player.getPacketSender().sendMessage("You currently don't have any items waiting. You must donate first!");
                    return;
                }
                if (donations[0].message != null) {
                    player.getPacketSender().sendMessage(donations[0].message);
                    return;
                }
                int [] items = new int[donations.length];
                int i = 0, amount = 0;
                for (com.everythingrs.donate.Donation donate: donations) {
                    player.getInventory().add(new Item(donate.product_id, donate.product_amount));
                    DonationManager.getInstance().addToTotalDonation(donate.product_id, donate.product_amount);
                    items[i] = donate.product_id;
                    amount += donate.product_price;
                    i++;
                }

                logPurchase(player, items, amount);

                FlashDeals.getDeals().handleFlashDeals(player, amount, items);

                player.getPacketSender().sendMessage("Thank you for donating!");
                World.sendMessage("<img=857><col=FF0000><shad=1>[" + player.getUsername() + "] Just Donated For " + donations[0].product_name + " x" + donations[0].product_amount + ". Thank You For The Support!");
                JavaCord.sendMessage("\uD83E\uDD16│\uD835\uDDEE\uD835\uDDF0\uD835\uDE01\uD835\uDDF6\uD835\uDE03\uD835\uDDF6\uD835\uDE01\uD835\uDE06", "**[" + player.getUsername() + "] Just Donated For " + donations[0].product_name + " x" +donations[0].product_amount + " ! Thanks for the support !** :heart: ");

            } catch (Exception e) {
                player.getPacketSender().sendMessage("Api Services are currently offline. Please check back shortly");
                e.printStackTrace();
            }
        });
    }

    public void test(Player player){
        logPurchase(player, new int[]{995}, 1000000);
    }

    private void logPurchase(Player player, int[] items, int amount){
        new PlayerDonationSave(player, items, amount).create().save();
    }
}
