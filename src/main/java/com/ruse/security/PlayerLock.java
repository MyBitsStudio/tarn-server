package com.ruse.security;

import com.ruse.security.save.impl.player.PlayerLockLoad;
import com.ruse.security.save.impl.player.PlayerLockSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.World;
import com.ruse.world.packages.discordbot.AdminCord;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static com.ruse.net.login.LoginResponses.LOGIN_SUCCESSFUL;
import static com.ruse.net.login.LoginResponses.TEMP_LOCK;
import static com.ruse.security.tools.SecurityUtils.PLAYER_LOCK_FILE;
import static com.ruse.world.entity.impl.player.PlayerFlags.FORCE_KICK;

public class PlayerLock {

    private Player player;

    private String username;

    private AtomicLong lockTime = new AtomicLong(0L);

    public long getLockTime() {
        return lockTime.get();
    }

    public void setLockTime(long time){
        lockTime.set(time);
    }
    private String LOCK = "";

    public String getLock() {
        return LOCK;
    }

    public void setLock(String lock){
        LOCK = lock;
    }

    /**
     * A map of associations that are used to store data that is associated with this lock.
     * Login Tries - The amount of times a player has tried to login -- *logTime* -- (int)
     * Lockout Login Tries - The amount of times a player has tried to login and failed -- *lockTime* -- (int)
     * Invalid IP Attempts - The amount of times a player has tried to login with an invalid IP -- *ipAtt* -- (int)
     * Invalid Words - The amount of times a player has used with an invalid word -- *wordAtt* -- (int)
     * Security Lock - The amount of times a player has violated Banned Words -- *secLock* -- (int)
     * Invalid Pin Attempts - The amount of times a player has tried to login with an invalid pin -- *pinAtt* -- (int)
     * Invalid 2FA Attempts - The amount of times a player has tried to login with an invalid 2FA -- *faAtt* -- (int)
     */
    private final Map<String, Integer> associations = new ConcurrentHashMap<>();

    public Map<String, Integer> getAssociations() {
    	return associations;
    }

    public void setAssociations(Map<String, Integer> associations) {
    	this.associations.putAll(associations);
    }

    public int getAssociation(String key){
        return associations.getOrDefault(key, 0);
    }

    public void setAssociation(String key, int value){
        associations.put(key, value);
    }

    /**
     * A map of logs that are used to store data that is associated with this lock.
     * Invalid Passwords - The amount of times a player has tried to login with an invalid password -- *logTime* -- (List<String>)
     * Invalid Words - The invalid words spoken by the player -- *wordAtt* -- (List<String>)
     * Banned Words - The banned words spoken by the player -- *secLock* -- (List<String>)
     * Invalid IP - The invalid IPs used by the player -- *ipAtt* -- (List<String>)
     * Pin Attempts - The invalid pins used by the player -- *pinAtt* -- (List<String>)
     * 2FA Attempts - The invalid 2FAs used by the player -- *faAtt* -- (List<String>)
     */
    private final Map<String, List<String>> lockLogs = new ConcurrentHashMap<>();

    public Map<String, List<String>> getLockLogs() {
    	return lockLogs;
    }

    public void setLockLogs(Map<String, List<String>> lockLogs) {
        this.lockLogs.clear();
    	this.lockLogs.putAll(lockLogs);
    }

    public void addLog(String key, String value){
        lockLogs.get(key).add(value);
    }

    /**
     * Different Type of Player Locks
     * -- Invalid Password -- The player has tried to login with an invalid password -- *logTime*
     * -- Invalid Words -- The player has used an invalid word -- *wordAtt*
     * -- Invalid IP -- The player has tried to login with an invalid IP -- *ipAtt*
     * -- Security Lock -- The player has violated Banned Words -- *secLock*
     * -- Invalid Pin -- The player has tried to login with an invalid pin -- *pinAtt*
     * -- Invalid 2FA -- The player has tried to login with an invalid 2FA -- *faAtt*
     * -- Unlock -- The player has unlocked their account -- *unlock*
     */
    private final Map<String, Boolean> playerLocks = new ConcurrentHashMap<>();

    public Map<String, Boolean> getPlayerLocks() {
    	return playerLocks;
    }

    public void setPlayerLocks(Map<String, Boolean> playerLocks) {
    	this.playerLocks.putAll(playerLocks);
    }

    public boolean isLocked(String key){
        return playerLocks.getOrDefault(key, false);
    }

    public boolean isLocked(){
        for(boolean lock : playerLocks.values()){
            if(lock){
                LOCK = playerLocks.entrySet().stream().filter(Map.Entry::getValue).findFirst().get().getKey();
                return true;
            }
        }
        return false;
    }

    public void setLocked(String key, boolean value){
        playerLocks.put(key, value);
    }

    public PlayerLock(@NotNull Player player) {
        this.player = player;
        this.username = player.getUsername();
    }

    public PlayerLock(String username){
        this.username = username;
    }

    public void setUsername(String username) {
    	this.username = username;
    }

    public PlayerLock load(String username){
        this.username = username;
        Path path = Paths.get(PLAYER_LOCK_FILE, username + ".json");
        File file = path.toFile();

        if (!file.exists()) {
            start();
            save();
            return this;
        }
        new PlayerLockLoad(this).loadJSON(path.toString()).run();
        return this;
    }

    private void start(){
        associations.put("logTime", 0);
        associations.put("lockTime", 0);
        associations.put("ipAtt", 0);
        associations.put("wordAtt", 0);
        associations.put("secLock", 0);
        associations.put("pinAtt", 0);
        associations.put("faAtt", 0);

        lockLogs.put("logTime", new CopyOnWriteArrayList<>());
        lockLogs.put("wordAtt", new CopyOnWriteArrayList<>());
        lockLogs.put("secLock", new CopyOnWriteArrayList<>());
        lockLogs.put("ipAtt", new CopyOnWriteArrayList<>());
        lockLogs.put("pinAtt", new CopyOnWriteArrayList<>());
        lockLogs.put("faAtt", new CopyOnWriteArrayList<>());
        lockLogs.put("unlock", new CopyOnWriteArrayList<>());

        playerLocks.put("logTime", false);
        playerLocks.put("wordAtt", false);
        playerLocks.put("ipAtt", false);
        playerLocks.put("secLock", false);
        playerLocks.put("pinAtt", false);
        playerLocks.put("faAtt", false);
    }

    public void save(){
        new PlayerLockSave(username, this).create().save(SecurityUtils.PLAYER_LOCK_FILE+username+".json");
    }

    public int lockCode(){
        if(lockTime.get() > System.currentTimeMillis()){
            return TEMP_LOCK;
        } else if (isLocked(LOCK)) {
            unlock(LOCK);
            if (isLocked()) {
                return TEMP_LOCK;
            }
        }

        return LOGIN_SUCCESSFUL;
    }

    public boolean increase(String key, String log){
        setAssociation(key, getAssociation(key) + 1);
        switch(key){
            case "logTime":
                if(getAssociation(key) >= 3){
                    addLog(key, log);
                    lock(key);
                    return true;
                }
                break;
            case "ipAtt":
                if(getAssociation(key) >= 3){
                    player.getPSecurity().blockIP();
                    addLog(key, log);
                    lock(key);
                    return true;
                }
                break;
            case "wordAtt":
                if(getAssociation(key) >= 5){
                    addLog(key, log);
                    lock(key);
                    return true;
                }
                break;
            case "secLock":
            case "faAtt":
            case "pinAtt":
                if(getAssociation(key) >= 2){
                    addLog(key, log);
                    lock(key);
                    return true;
                }
                break;
        }
        addLog(key, log);
        addLog(key);
        save();
        AdminCord.sendMessage(1116230759225765968L, player.getUsername()+" has been ticked by the security system for "+key);
        return false;
    }

    public void lock(@NotNull String key){
        setAssociation("lockTime", getAssociation("lockTime") + 1);
        LOCK = key;
        setLocked(key, true);
        lockTime.set(System.currentTimeMillis() + lockTime(key));
        addLog(key);
        setAssociation(key, 0);

        save();

        if(World.getPlayerByName(player.getUsername()) != null){
            player.getPlayerFlags().setFlag(FORCE_KICK, true);
        }

        AdminCord.sendMessage(1116230759225765968L, player.getUsername()+" has been locked by the security system for "+key);

    }

    public void unlock(){
        AtomicBoolean unlock = new AtomicBoolean(false);
        playerLocks.forEach((key, value) -> {
            if(value){
                setLocked(key, false);
                unlock.set(true);
            }
        });
        if(unlock.get()){
            lockTime.set(0L);
            LOCK = "";
            addLog("unlock");
            save();
        }
    }

    public void unlock(String key){
        if(isLocked(key)){
            if(key.equals("secLock"))
                return;
            setLocked(key, false);
            if(lockTime.get() > 0){
                lockTime.set(0L);
            }
            LOCK = "";
            addLog("unlock");
            save();
        }
    }

    private long lockTime(String key){
        switch(key){
            case "secLock":
            case "ipAtt":
                return TimeUnit.DAYS.toMillis(1) * getAssociation("lockTime");
            case "wordAtt":
            case "logTime":
            case "pinAtt":
            case "faAtt":
                return TimeUnit.MINUTES.toMillis(5) * getAssociation("lockTime");
        }
        return 0L;
    }

    private void addLog(String key){
        if(key.equals("unlock")){
            addLog(key, "[ " + key + " "+getTime()+" ] " + username + " has been unlocked.");
            return;
        }
        addLog(key, "[ " + key + " "+getTime()+" ] " + username + " has been locked for " + getAssociation(key) + " times and has been locked for " + getTimeToFormat(lockTime(key)) + " time.");
    }

    public String getTime(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return format.format(date);
    }

    public String getTimeToFormat(long time){
        String hours = String.valueOf(TimeUnit.MILLISECONDS.toHours(time));
        String minutes = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)));
        String seconds = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
        return hours + " hours, " + minutes + " minutes, " + seconds + " seconds";
    }

}
