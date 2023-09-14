package com.ruse.world.packages.serverperks;

import com.ruse.io.ThreadProgressor;
import com.ruse.util.Misc;
import com.ruse.util.StringUtils;
import com.ruse.world.World;
import com.ruse.world.packages.discordbot.JavaCord;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import lombok.Getter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class ServerPerks {

    private static ServerPerks instance = null;
    private final Map<Perk, Integer> contributions = new HashMap<>();
    private final int TIME = 12000; // 1 hour
    public static final int INTERFACE_ID = 42050;
    public static final int OVERLAY_ID = 42112;
    private final Perk[] PERKS = Perk.values();
    private final Path FILE_PATH = Paths.get("./data/serverperks.txt");
    @Getter
    private Perk activePerk;
    private int currentTime = 0;
    private boolean active = false;

    private ServerPerks() {

    }

    public static ServerPerks getInstance() {
        if (instance == null) {
            instance = new ServerPerks();
        }
        return instance;
    }

    public void open(Player player) {
        player.getPacketSender().sendInterface(INTERFACE_ID);
        player.setPerkIndex(0);
        updateInterface(player);
    }

    public void contribute(Player player, int amount) {
        if (active) {
            player.sendMessage("@red@A perk is already active");
            return;
        }
        if (!player.getInventory().contains(995, amount)) {
            amount = player.getInventory().getAmount(995);
        }
        int index = player.getPerkIndex();
        Perk perk = PERKS[index];
        int current = contributions.getOrDefault(perk, 0);
        int necessary = perk.getAmount();
        amount = Math.min(amount, necessary - current);

        player.getInventory().delete(995, amount);
        int total = contributions.merge(perk, amount, Integer::sum);
        updateInterface(player);

        updateAchievements(player, amount);

        save();
        if (amount >= 1000000) {
            World.sendMessage("<img=16><shad=1>@or2@[" + player.getUsername() + "] @yel@has just donated @gre@" + amount + " @yel@Coins to the Server Perk!");
            JavaCord.sendMessage(1117224370587304057L, "[" + player.getUsername() + "] has just donated " + amount + " Coins to the Server Perk!");
        }
        if (total >= necessary) {
            start(perk);
        }
    }

    private void updateAchievements(Player player, int amount){
        AchievementHandler.progress(player, amount, 10);
        AchievementHandler.progress(player, amount, 22);
        AchievementHandler.progress(player, amount, 38);
        AchievementHandler.progress(player, amount, 62);
        AchievementHandler.progress(player, amount, 83);
    }

    public void tick() {
        if (!active) {
            return;
        }

        currentTime--;

        if (currentTime == 0) {
            end();
        }

        if (currentTime % 100 == 0) {
            updateOverlay();
        }
    }

    private void start(Perk perk) {
        currentTime = TIME;
        active = true;

        activePerk = perk;
        updateOverlay();
        World.sendMessage("<img=16>[WORLD]<img=16> @red@Perk [" + activePerk.getName() + "] has just been activated!");
        JavaCord.sendMessage(1117224370587304057L, "[WORLD] Perk [" + activePerk.getName() + "] has just been activated!");

        //reset();
        // Erase file contents
        deleteTypeFromLog(perk);
    }

    private void end() {
        active = false;
        contributions.put(activePerk, 0);
        World.sendMessage("<img=16>[WORLD]<img=16> @red@Perk [" + activePerk.getName() + "] has ended");
        JavaCord.sendMessage(1117224370587304057L, "[WORLD] Perk [" + activePerk.getName() + "] has ended");
        activePerk = null;
        resetInterface();
    }

    private void updateOverlay() {
        if (activePerk == null) {
            return;
        }
        World.getPlayers().forEach(player -> {
            int minutes = (int) QuickUtils.tickToMin(currentTime);
            player.getPacketSender().sendSpriteChange(OVERLAY_ID + 1, activePerk.getSpriteId());
            player.getPacketSender().sendWalkableInterface(OVERLAY_ID, true);
            player.getPacketSender()
                    .sendString(OVERLAY_ID + 3, StringUtils.usToSpace(activePerk.toString()));
            player.getPacketSender().sendString(OVERLAY_ID + 2, minutes + " min");
        });
    }

    private void resetInterface() {
        World.getPlayers().forEach(player -> {
            player.getPacketSender().sendWalkableInterface(OVERLAY_ID, false);
            player.getPacketSender().updateProgressBar(INTERFACE_ID + 10, 0);
        });
    }

    private void updateInterface(Player player) {
        int index = player.getPerkIndex();
        Perk perk = PERKS[index];
        int current = contributions.getOrDefault(perk, 0);
        int required = perk.getAmount();
        int percentage = getPercentage(current, required);
        player.getPacketSender().updateProgressBar(INTERFACE_ID + 10, percentage);
        player.getPacketSender().sendString(INTERFACE_ID + 11, Misc.formatNumber(current) + " / " + Misc.formatNumber(required) + "");
    }

    private int getPercentage(int n, int total) {
        float proportion = ((float) n) / ((float) total);
        return (int) (proportion * 100f);
    }

    public boolean handleButton(Player player, int id) {
        if (id > -23465 || id < -23472) {
            return false;
        }

        int index = 23470 + id;
        player.setPerkIndex(index);
        updateInterface(player);
        return true;
    }

    public void save() {
        List<String> data = new ArrayList<>();
        contributions.forEach((k, v) -> {
            data.add(k.toString() + ", " + v);
        });

        try {
            Files.write(FILE_PATH, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try (Stream<String> lines = Files.lines(FILE_PATH)) {
            lines.forEach(line -> {
                String[] split = line.split(", ");
                if(split.length == 2)
                    contributions.put(Perk.valueOf(split[0]), Integer.parseInt(split[1]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
      //  System.out.println("Loaded: " + contributions);
    }

    public void reset() {
        contributions.clear();
        World.getPlayers().forEach(this::updateInterface);
    }
    public void deleteTypeFromLog(Perk name) {
        ThreadProgressor.submit(false, () -> {
            try {
                BufferedReader r = new BufferedReader(new FileReader(FILE_PATH.toString()));
                ArrayList<String> contents = new ArrayList<>();
                while (true) {
                    String line = r.readLine();
                    String lineUser = line;
                    if (line == null) {
                        break;
                    } else {
                        line = line.trim();
                        lineUser = line.substring(0, line.indexOf(","));
                    }
                    if (!lineUser.equalsIgnoreCase(name.name())) {
                        contents.add(line);
                    }
                }
                r.close();
                BufferedWriter w = new BufferedWriter(new FileWriter(FILE_PATH.toString()));
                for (String line : contents) {
                    w.write(line, 0, line.length());
                    w.write(System.lineSeparator());
                }
                w.flush();
                w.close();
            } catch (Exception e) {
            }
            return null;
        });
    }
    @Getter
    public enum Perk {
        SLAYER("x2 Slayer Tickets", 0, 2500000, 1522),//DONE
        DMG("50% Damage Boost", 1, 3500000, 1521),// DONE
        DR("50% Drop Boost", 2, 5000000, 1521),// DONE
        XP("x3 EXP Boost", 3, 1000000, 1524),// DONE
        ALL_PERKS("DR/DMG/XP", 4, 10000000, 1523),// DONE
        MEGA_PERK("ALL PERKS", 5, 25000000, 1525),// DONE
        ;

        private final int index, amount;
        private final int spriteId;
        private final String name;

        Perk(String name, int index, int amount, int spriteId) {
            this.name = name;
            this.index = index;
            this.amount = amount;
            this.spriteId = spriteId;
        }

    }


}