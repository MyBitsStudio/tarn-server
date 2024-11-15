package com.ruse.security.save.impl.server;

import com.ruse.engine.GameEngine;
import com.ruse.security.PlayerLock;
import com.ruse.security.save.SecureSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.entity.impl.player.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerDonationSave extends SecureSave {

    private final Player player;
    private final int amount;
    private final String id;
    public PlayerDonationSave(Player player, int amount, String id) {
        this.player = player;
        this.amount = amount;
        this.id = id;
    }
    @Override
    public SecureSave create() {
        object.addProperty("username", player.getUsername());
        object.addProperty("amount", amount);
        object.addProperty("id", id);
        object.addProperty("time", getTime());
        object.addProperty("timestamp", System.currentTimeMillis());
        object.addProperty("ip", player.getHostAddress());
        object.addProperty("uuid", player.getUUID());
        object.addProperty("serial", player.getSerialNumber());
        object.addProperty("confirmed", true);
        return this;
    }

    @Override
    public void save() {
        try (FileWriter file = new FileWriter(SecurityUtils.DONATE+player.getUsername()+"-"+getTime()+".json")) {
                file.write(builder.toJson(object));
                file.flush();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    public String getTime(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date).replace(":", "-").replace(" ", "_");
    }

    @Override
    public String key() {
        return null;
    }
}
