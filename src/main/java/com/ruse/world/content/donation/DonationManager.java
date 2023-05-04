package com.ruse.world.content.donation;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import com.ruse.engine.GameEngine;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.content.discordbot.JavaCord;
import com.ruse.world.content.donation.boss.DonationBoss;
import com.ruse.world.content.donation.boss.DonationMinion;
import lombok.Getter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
}
