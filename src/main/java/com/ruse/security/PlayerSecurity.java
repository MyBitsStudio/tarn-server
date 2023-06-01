package com.ruse.security;

import com.ruse.model.input.impl.Enter2FAFirstPacketListener;
import com.ruse.net.login.LoginDetailsMessage;
import com.ruse.security.save.impl.player.PlayerSecureLoad;
import com.ruse.security.save.impl.player.PlayerSecureSave;
import com.ruse.security.save.impl.player.PlayerSecurityLoad;
import com.ruse.security.save.impl.player.PlayerSecuritySave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.content.dialogue.DialogueManager;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.PlayerFlags;
import com.ruse.world.entity.impl.player.PlayerLoading;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ruse.net.login.LoginResponses.*;
import static com.ruse.security.tools.SecurityUtils.*;
import static com.ruse.world.entity.impl.player.PlayerFlags.*;

public class PlayerSecurity {

    /**
     * Security Associations -- Values that are contained from past accounts
     * Username (List<String>) - *username*
     * IP (List<String>) - *ip*
     * MAC (List<String>) - *mac*
     * HWID (List<String>) - *hwid*
     * UUID (List<String>) - *uuid*
     */
    private final Map<String, List<String>> associations = new ConcurrentHashMap<>();

    public Map<String, List<String>> getAssociations() {
        return associations;
    }

    public void setAssociations(Map<String, List<String>> ass){
        associations.putAll(ass);
    }
    public List<String> getAssociation(String key){
        return associations.getOrDefault(key, new CopyOnWriteArrayList<>());
    }

    public void addAssociation(String key, String value){
        List<String> list = getAssociation(key);
        if(!list.contains(value)){
            list.add(value);
        }
        associations.put(key, list);
    }

    public void removeAssociation(String key, String value){
        List<String> list = getAssociation(key);
        list.remove(value);
        associations.put(key, list);
    }

    public boolean containsAssociation(String key, String value){
        return getAssociation(key).contains(value);
    }
    /**
     * Security Map -- Values to keep account safe
     * Flagged IPS -- Any IP that has been flagged on this account as unsafe -- *ip* (List<String>)
     * Flagged Passwords -- Any password that has been flagged on this account as unsafe -- *password* (List<String>)
     * Auth IPs - Any IP that has been authenticated on this account -- *auth* (List<String>)
     * Security Question - Security Question for account -- *sQ* (String)
     * Security Answer - Security Answer for account -- *sA* (String)
     * Country - Country of account -- *country* (String)
     * City - City of account -- *city* (String)
     * Last Changed Password -- Last time password was changed -- *lastPassword* (String)
     * Last Changed PIN - Last time PIN was changed -- *lastPin* (String)
     */
    private final Map<String, Object> securityMap = new ConcurrentHashMap<>();

    public Map<String, Object> getSecurityMap() {
        return securityMap;
    }

    public void setSecurityMap(Map<String, Object> map){
        securityMap.putAll(map);
    }
    public List<String> getSecurityListStringValue(String key){
        return (List<String>) securityMap.getOrDefault(key, new CopyOnWriteArrayList<>());
    }

    public void addSecurityListStringValue(String key, String value){
        List<String> list = getSecurityListStringValue(key);
        if(!list.contains(value)){
            list.add(value);
        }
        securityMap.put(key, list);
    }

    public void removeSecurityListStringValue(String key, String value){
        List<String> list = getSecurityListStringValue(key);
        list.remove(value);
        securityMap.put(key, list);
    }

    public String getSecurityStringValue(String key){
        return (String) securityMap.getOrDefault(key, "");
    }

    public List<Long> getSecurityListLongValue(String key){
        return (List<Long>) securityMap.getOrDefault(key, new CopyOnWriteArrayList<>());
    }

    public void addSecurityListLongValue(String key, long value){
        List<Long> list = getSecurityListLongValue(key);
        if(!list.contains(value)){
            list.add(value);
        }
        securityMap.put(key, list);
    }

    public void removeSecurityListLongValue(String key, long value){
        List<Long> list = getSecurityListLongValue(key);
        list.remove(value);
        securityMap.put(key, list);
    }

    public boolean containsSecurityListLongValue(String key, long value){
        return getSecurityListLongValue(key).contains(value);
    }

    /**
     * List of all login data
     */
    private final List<String> logins = new CopyOnWriteArrayList<>();
    /**
     * Seed used to hash password
     */
    private byte[] seed, auth;
    private String fa = "";

    private volatile String username, ip, hwid;

    private Player player;

    private final PlayerLock lock;

    public PlayerSecurity(String username){
        this.username = username;
        this.lock = new PlayerLock(username);
    }

    public PlayerSecurity(Player player){
        this.player = player;
        this.lock = new PlayerLock(player);
    }

    public PlayerSecurity setUsername(String username){
        this.username = username;
        return this;
    }

    public PlayerSecurity setIp(String ip){
        this.ip = ip;
        return this;
    }

    public PlayerSecurity setHWID(String hwid){
        this.hwid = hwid;
        return this;
    }

    public String getUsername(){
        return username;
    }

    public String getIp(){
        return ip;
    }

    public byte[] getSeed(){
        return seed;
    }

    public void setSeed(byte[] seed){
        this.seed = seed;
    }

    public byte[] getAuth(){
        return auth;
    }

    public void setAuth(byte[] auth){
        this.auth = auth;
    }

    public String getFA(){
        return fa;
    }

    public void setFA(String fa){
        this.fa = fa;
    }

    public String getHwid(){
        return hwid;
    }

    public List<String> getLogins(){
        return logins;
    }

    public void setLogins(List<String> logins){
        this.logins.clear();
        this.logins.addAll(logins);
    }

    public PlayerLock getPlayerLock(){
        return lock;
    }

    public boolean faEnabled(){
        return fa != null && !fa.isEmpty();
    }

    public void changePass(String pass){
        seed = generateSalt();
        auth = hashPassword(pass, seed);
        securityMap.put("lastPassword", String.valueOf(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30)));
        save();
    }

    public void changePin(){
        securityMap.put("lastPin", String.valueOf(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30)));
        save();
    }

    private void start(String password, String ip, String mac, String hwid, String uuid){
        seed = generateSalt();
        auth = hashPassword(password, seed);

        associations.put("username", new CopyOnWriteArrayList<>());
        associations.put("ip", new CopyOnWriteArrayList<>());
        associations.put("mac", new CopyOnWriteArrayList<>());
        associations.put("hwid", new CopyOnWriteArrayList<>());
        associations.put("uuid", new CopyOnWriteArrayList<>());

        securityMap.put("ip", new CopyOnWriteArrayList<>());
        securityMap.put("password", new CopyOnWriteArrayList<>());
        securityMap.put("auth", new CopyOnWriteArrayList<>());
        securityMap.put("sQ", "");
        securityMap.put("sA", "");
        securityMap.put("country", "");
        securityMap.put("city", "");
        securityMap.put("lastPassword", String.valueOf(System.currentTimeMillis()));
        securityMap.put("lastPin", String.valueOf(System.currentTimeMillis()));

        addAssociation("username", username);
        addAssociation("ip", ip);
        addAssociation("mac", mac);
        addAssociation("hwid", hwid);
        addAssociation("uuid", uuid);

        addSecurityListStringValue("auth", ip);
        addSecurityListStringValue("hwid", hwid);
        save();
    }

    public boolean isRootIP(String ip){
        return getSecurityListStringValue("auth").contains(ip);
    }

    public void loadAll(LoginDetailsMessage msg){
        if (!load()) {
            start(msg.getPassword(), msg.getHost(), msg.getMac(), msg.getSerialNumber(), String.valueOf(msg.getUid()));
        }
    }

    public boolean load(){
        Path path = Paths.get(PLAYER_SECURITY_FILE, username + ".json");
        File file = path.toFile();

        if (!file.exists()) {
            return false;
        }

        new PlayerSecurityLoad(this).loadJSON(path.toString()).run();
        return true;
    }

    public void save(){
       new PlayerSecuritySave(this).create().save();
    }

    public void blockIP(){
        ((List<String>)securityMap.get("ip")).add(ip);
    }

    public int attemptLogin(LoginDetailsMessage msg){
        int code = loginCode();

        if(code != LOGIN_SUCCESSFUL){
            return code;
        }

        if(Paths.get("./data/saves/characters/", player.getUsername() + ".json").toFile().exists()){
            code = PlayerLoading.getResult(player);
            if(code != LOGIN_SUCCESSFUL){
                invalid(player.getPassword());
                return code;
            }
            File file = Paths.get("./data/saves/characters/", player.getUsername() + ".json").toFile();
            file.delete();
            success();
            new PlayerSecureSave(player).create().save();
        } else if(new File(PLAYER_FILE + player.getUsername()+".json").exists()){
            if (SecurityUtils.verifyPassword(msg.getPassword(), auth, seed)) {
                success();
                new PlayerSecureLoad(player).loadJSON(PLAYER_FILE + player.getUsername()+".json").run();
            } else {
                invalid(msg.getPassword());
                return INVALID_CREDENTIALS;
            }
        } else {
            return NEW_ACCOUNT;
        }

        player.setHWID(hwid);

        if(!isRootIP(msg.getHost()) && !faEnabled() && player.getPSettings().getBooleanValue("security")){
            lock.increase("ipAtt", msg.getHost());
            return BLOCK_IP;
        }

        if (player.getHasPin() && !isRootIP(msg.getHost()) && player.getPSettings().getBooleanValue("security")) {
            player.getPlayerFlags().setFlag(PlayerFlags.PIN_ENTER, true);
        } else if(player.getPSecurity().faEnabled() && !isRootIP(msg.getHost()) && player.getPSettings().getBooleanValue("security")) {
            player.getPlayerFlags().setFlag(PlayerFlags.TWO_FACTOR_AUTH, true);
        } else {
            addSecurityListStringValue("hwid", hwid);
            addSecurityListStringValue("auth", player.getHostAddress());
            addAssociation("hwid", hwid);
            addAssociation("auth", player.getHostAddress());
        }

        return code;
    }

    public void success(){

        String time = (String) securityMap.get("lastPassword");
        if(time != null && !time.isEmpty()){
            long lastPassword = Long.parseLong(time);
            if(lastPassword <= System.currentTimeMillis()){
                player.getPlayerFlags().setFlag(CHANGE_PASSWORD, true);
            }
        }

        String pin = (String) securityMap.get("lastPin");
        if(pin != null && !pin.isEmpty()){
            long lastPin = Long.parseLong(pin);
            if(lastPin <= System.currentTimeMillis()){
                player.getPlayerFlags().setFlag(CHANGE_PIN, true);
            }
        }

        if(!player.getPSettings().getBooleanValue("security")){
            player.getPlayerFlags().setFlag(UNSECURE, true);
        }
    }

    public int loginCode(){
        int code = checkIP();
        if(code != LOGIN_SUCCESSFUL){
            return code;
        }
        return lock.lockCode();
    }

    private int checkIP(){
        if(getSecurityListStringValue("ip").contains(ip)){
             return BLOCK_IP;
        }
        //more checks here
        return LOGIN_SUCCESSFUL;
    }

    public void invalid(String pass){
       lock.increase("logTime", pass);

       save();
    }

    public void logLogin(int code){
        String success = code == LOGIN_SUCCESSFUL ? "S" : "F";
        this.logins.add(getTime()+"-"+success+"-"+ip);
        save();
    }

    public String getTime(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(date);
    }

    public void sendInterface(){
        player.getPacketSender().sendInterfaceRemoval();

        player.getPacketSender().sendInterface(70000);

        if(securityScore() <= 30){
            player.getPacketSender().sendString(70009, "@red@Unsecure");
            player.getPacketSender().sendString(70012, "Security Score : @red@"+securityScore());
        } else if(securityScore() <= 59){
            player.getPacketSender().sendString(70009, "@yel@Secure");
            player.getPacketSender().sendString(70012, "Security Score : @yel@"+securityScore());
        } else {
            player.getPacketSender().sendString(70009, "@gre@Enhanced");
            player.getPacketSender().sendString(70012, "@gre@Security Score : "+securityScore());
        }

        if(!player.getPlayerFlags().isFlagged(CHANGE_PIN))
            player.getPacketSender().sendString(70016, "@gre@Secure");
        else
            player.getPacketSender().sendString(70016, "@red@Unsecure");

        if(!player.getPlayerFlags().isFlagged(CHANGE_PASSWORD))
            player.getPacketSender().sendString(70018, "@gre@Secure");
        else
            player.getPacketSender().sendString(70018, "@red@Unsecure");

        if(faEnabled())
            player.getPacketSender().sendString(70017, "@gre@Secure");
        else
            player.getPacketSender().sendString(70017, "@red@Unsecure");

        int i =0;
        Collections.reverse(logins);

        for(int b = 0; b < 35; b++){
            player.getPacketSender().sendString(70025+b, "");
        }

        for(String log : logins){
            if(i >= 35)
                continue;
            player.getPacketSender().sendString(70025+i, log);
            i++;
        }

    }

    public int securityScore(){
        int score = 0;
        if(player.getHasPin())
            score+=20;
        if(!player.getPlayerFlags().isFlagged(CHANGE_PIN))
            score+=20;
        if(!player.getPlayerFlags().isFlagged(CHANGE_PASSWORD))
            score+=20;
        if(faEnabled())
            score+=40;
        return score;
    }

    public void start2FA(){
        player.getPacketSender().sendInterfaceRemoval();
        DialogueManager.start(player, 11000);
        player.setDialogueActionId(11003);
    }

    public void begin2FA(){
        fa = SecurityUtils.base32Key();
        player.setInputHandling(new Enter2FAFirstPacketListener());
        player.getPacketSender().sendEnterInputPrompt("2FA : "+fa);
    }

    public void setIP(){
        addSecurityListStringValue("auth", player.getHostAddress());
        save();
    }

}
