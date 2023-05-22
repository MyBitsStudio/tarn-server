package com.ruse.security;

import com.ruse.security.save.impl.ServerSecurityLoad;
import com.ruse.security.save.impl.ServerSecuritySave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.World;
import com.ruse.world.content.discordbot.AdminCord;
import com.ruse.world.entity.impl.player.Player;
import io.ipgeolocation.api.Geolocation;
import io.ipgeolocation.api.GeolocationParams;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ruse.net.login.LoginResponses.*;
import static com.ruse.security.tools.SecurityUtils.SERVER_SECURITY_FILE;
import static com.ruse.security.tools.SecurityUtils.api;
import static com.ruse.world.entity.impl.player.PlayerFlags.FORCE_KICK;

public class ServerSecurity {

    public static String[] WHITELIST = {};
    private static ServerSecurity instance = new ServerSecurity();

    public static ServerSecurity getInstance() {
        if(instance == null) {
            instance = new ServerSecurity();
        }
        return instance;
    }

    public ServerSecurity(){
        load();
    }

    private String[] BLACKLIST;

    public String[] getBlackList(){
        return this.BLACKLIST;
    }

    public void setBlackList(String[] black){ this.BLACKLIST = black;}
    /**
     * Security Map contains values for each block
     * Time (Long value (System.currentTimeMillis() + time))
     *
     *  Player -> Player Ban -> Time -> *player*
     *  Unique Id -> UUID -> Time -> *uuid*
     *  IP -> IP -> Time -> *ip*
     *  MAC -> MAC -> Time -> *mac*
     *  HWID -> HWID -> Time -> *hwid*
     *  MUTE -> NAME -> Time -> *mute*
     *
     */
    private final Map<String, Map<String, String>> securityMap = new ConcurrentHashMap<>();

    public Map<String, Map<String, String>> getSecurityMap() {
        return securityMap;
    }

    public void setSecurityMap(Map<String, Map<String, String>> map){
        securityMap.putAll(map);
    }

    public void addSecurity(String key, String value, String time){
        Map<String, String> map = securityMap.get(key);
        if(map == null){
            map = new ConcurrentHashMap<>();
        }
        map.put(value, time);
        securityMap.put(key, map);
    }

    /**
     * A map of keys that server uses for multiple purposes.
     *
     */
    private final Map<String, Object> keys = new ConcurrentHashMap<>();

    public Map<String, Object> getKeys() {
        return keys;
    }

    public void setKeys(Map<String, Object> map){
        keys.putAll(map);
    }

    public void addKey(String key, Object value){
        keys.put(key, value);
    }

    public String getStringKey(String key){
        return (String) keys.get(key);
    }

    public void load(){
        if(!new File(SERVER_SECURITY_FILE).exists()){
            return;
        }
        new ServerSecurityLoad(this).loadJSON(SERVER_SECURITY_FILE).run();
    }

    public void save(){
        new ServerSecuritySave(this).create().save();
    }

    public void reload(){
        if(!new File(SERVER_SECURITY_FILE).exists()){
            return;
        }
        securityMap.clear();
        keys.clear();
        new ServerSecurityLoad(this).loadJSON(SERVER_SECURITY_FILE).run();
    }

    private boolean whiteList(String ip){
        return Arrays.asList(WHITELIST).contains(ip);
    }

    private boolean isBlackList(String ip){
        return Arrays.asList(BLACKLIST).contains(ip);
    }

    public int screenPlayer(Player player){
        int code = checkPlayerStatus(player);

        return code == 0 ? player.getPSecurity().loginCode() : code;
    }

    public void banPlayer(Player player, int type, long time){
        switch(type){
            case 0: //normal
                addSecurity("player", player.getUsername().toLowerCase(), String.valueOf(System.currentTimeMillis()+ time));
                //addSecurity("UUID", player.getUUID(), String.valueOf(System.currentTimeMillis()+ time));
                break;
            case 1: //tri
                addSecurity("player", player.getUsername().toLowerCase(), String.valueOf(System.currentTimeMillis()+ time));
                //addSecurity("UUID", player.getUUID(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("ip", player.getPSecurity().getIp(), String.valueOf(System.currentTimeMillis()+ time));
                break;
            case 2: //full
                addSecurity("mac", player.getMac(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("player", player.getUsername().toLowerCase(), String.valueOf(System.currentTimeMillis()+ time));
                //addSecurity("UUID", player.getUUID(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("ip", player.getPSecurity().getIp(), String.valueOf(System.currentTimeMillis()+ time));
                break;
            case 3: // max
                //addSecurity("UUID", player.getUUID(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("mac", player.getMac(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("player", player.getUsername().toLowerCase(), String.valueOf(System.currentTimeMillis()+ time));
                //addSecurity("hwid", player.getUUID(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("ip", player.getPSecurity().getIp(), String.valueOf(System.currentTimeMillis()+ time));
                break;

            case 4://perm
               // addSecurity("UUID", player.getUUID(), String.valueOf(-1));
                addSecurity("mac", player.getMac(), String.valueOf(-1));
                addSecurity("player", player.getUsername().toLowerCase(), String.valueOf(-1));
                //addSecurity("hwid", player.getUUID(), String.valueOf(-1));
                addSecurity("ip", player.getPSecurity().getIp(), String.valueOf(-1));
                break;
        }
        for(Player players : World.getPlayers()){
            if(players == null)
                continue;
            if(players.getPSecurity().getIp().equals(player.getPSecurity().getIp())){
                players.save();
                players.getPlayerFlags().setFlag(FORCE_KICK, true);
            }
        }
        player.save();
        player.getPlayerFlags().setFlag(FORCE_KICK, true);
        save();
        AdminCord.sendMessage(1109203346520277013L, player.getUsername()+" was just banned "+type);
    }

    public void mutePlayer(Player player, int time){
        addSecurity("mute", player.getUsername().toLowerCase(), String.valueOf(System.currentTimeMillis()+ (1000 * 60 * (5L * time))));
        player.save();
        save();
    }

    private int checkPlayerStatus(@NotNull Player player){
        if(isPlayerBanned(player.getUsername().toLowerCase())){
            AdminCord.sendMessage(1109203346520277013L, player.getUsername()+" is banned and attempted to login");
            return ACCOUNT_BANNED;
        }
//        if(isUUIDBanned(player.getUUID())){
//            return ACCOUNT_BANNED;
//        }
        if(isIPBanned(player.getHostAddress())){
            AdminCord.sendMessage(1109203346520277013L, player.getUsername()+" is ipbanned and attempted to login");
            return ACCOUNT_BANNED;
        }
        if(isMACBanned(player.getMac())){
            AdminCord.sendMessage(1109203346520277013L, player.getUsername()+" is macbanned and attempted to login");
            return ACCOUNT_BANNED;
        }
//        if(isHWIDBanned(player)){
//            return 5;
//        }
        int code = checkSecurity(player);
        return code == 0 ? LOGIN_SUCCESSFUL : code;
    }

    public boolean isPlayerMuted(String username){
        if(securityMap.get("mute").get(username.toLowerCase()) == null){
            return false;
        }
        long time = Long.parseLong(securityMap.get("mute").get(username.toLowerCase()));
        return time == -1 || time > System.currentTimeMillis();
    }

    public void unMute(String username){
        if(securityMap.get("mute").get(username.toLowerCase()) != null){
            securityMap.get("mute").remove(username.toLowerCase());
            save();
        }
    }

    private boolean isPlayerBanned(String username){
        if(securityMap.get("player").get(username) == null){
            return false;
        }
        long time = Long.parseLong(securityMap.get("player").get(username));
        return time == -1 || time > System.currentTimeMillis();
    }

    private boolean isUUIDBanned(String uuid){
        if(securityMap.get("UUID").get(uuid) == null){
            return false;
        }
        long time = Long.parseLong(securityMap.get("UUID").get(uuid));
        return time == -1 || time > System.currentTimeMillis();
    }

    private boolean isIPBanned(String ip){
        if(securityMap.get("ip").get(ip) == null){
            return false;
        }
        long time = Long.parseLong(securityMap.get("ip").get(ip));
        return time == -1 || time > System.currentTimeMillis();
    }

    private boolean isMACBanned(String mac){
        if(securityMap.get("mac").get(mac) == null){
            return false;
        }
        long time = Long.parseLong(securityMap.get("mac").get(mac));
        return time == -1 || time > System.currentTimeMillis();
    }

    private boolean isHWIDBanned(String hwid){
        if(securityMap.get("hwid").get(hwid) == null){
            return false;
        }
        long time = Long.parseLong(securityMap.get("hwid").get(hwid));
        return time == -1 || time > System.currentTimeMillis();
    }

    private int checkSecurity(@NotNull Player player){
        if(player.getPSecurity().getIp().equals("127.0.0.1") || player.getPSecurity().getIp().equals("localhost")
                || whiteList(player.getPSecurity().getIp())){
            return 0;
        }
        if(isBlackList(player.getPSecurity().getIp())){
            AdminCord.sendMessage(1109203346520277013L, player.getUsername()+" is blacklisted "+player.getPSecurity().getIp());
            return BLACKLIST_IP;
        }
        GeolocationParams geoParams = new GeolocationParams();
        geoParams.setIPAddress(player.getPSecurity().getIp());
        geoParams.setFields("geo,time_zone,currency");

        geoParams.setIncludeSecurity(true);
        Geolocation geolocation = api.getGeolocation(geoParams);

        if (geolocation.getStatus() == 200) {

            if(geolocation.getGeolocationSecurity().getAnonymous() || geolocation.getGeolocationSecurity().getKnownAttacker()
                    || geolocation.getGeolocationSecurity().getProxy() || !geolocation.getGeolocationSecurity().getProxyType().equals("")
                    || geolocation.getGeolocationSecurity().getCloudProvider() || geolocation.getGeolocationSecurity().getTor()
                    ||geolocation.getGeolocationSecurity().getThreatScore() > 20.0
            ){
                AdminCord.sendMessage(1109203346520277013L, player.getUsername()+" is using a VPN "+player.getPSecurity().getIp());
                return VPN_DETECTED;
            }

        } else {
            return INVALID_IP;
        }

        return 0;
    }

    private String @NotNull [] getInfo(String ip){
        GeolocationParams geoParams = new GeolocationParams();
        geoParams.setIPAddress(ip);
        geoParams.setFields("geo,time_zone,currency");

        geoParams.setIncludeSecurity(true);
        Geolocation geolocation = api.getGeolocation(geoParams);
        String[] info = new String[5];

        if (geolocation.getStatus() == 200) {

            info[0] = geolocation.getCountryName();
            info[1] = geolocation.getCity();
            info[2] = geolocation.getZipCode();
            info[3] = geolocation.getTimezone().getName();
            info[4] = geolocation.getCurrency().getName();

        }
        return info;
    }



}
