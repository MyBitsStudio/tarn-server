package com.ruse.security;

import com.ruse.security.save.impl.server.ServerMapsLoad;
import com.ruse.security.save.impl.server.ServerMapsSave;
import com.ruse.security.save.impl.server.ServerSecurityLoad;
import com.ruse.security.save.impl.server.ServerSecuritySave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.World;
import com.ruse.world.content.discordbot.AdminCord;
import com.ruse.world.entity.impl.player.Player;
import io.ipgeolocation.api.Geolocation;
import io.ipgeolocation.api.GeolocationParams;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ruse.net.login.LoginResponses.*;
import static com.ruse.security.tools.SecurityUtils.*;
import static com.ruse.world.entity.impl.player.PlayerFlags.FORCE_KICK;

public class ServerSecurity {
    private static ServerSecurity instance = new ServerSecurity();

    public static ServerSecurity getInstance() {
        if(instance == null) {
            instance = new ServerSecurity();
        }
        return instance;
    }

    public ServerSecurity(){
        loadAll();
    }

    private String[] BLACKLIST, WHITELIST;

    public String[] getBlackList(){
        return this.BLACKLIST;
    }

    public void setBlackList(String[] black){ this.BLACKLIST = black;}

    public String[] getWhiteList(){
        return this.WHITELIST;
    }

    public void setWhiteList(String[] black){ this.WHITELIST = black;}
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


    private final Map<String, List<String>> ipMap = new ConcurrentHashMap<>();
    private final Map<String, List<String>> hwidMap = new ConcurrentHashMap<>();
    private final Map<String, List<String>> macMap = new ConcurrentHashMap<>();

    private final List<String> flagged = new CopyOnWriteArrayList<>();

    public List<String> getFlagged() {
    	return flagged;
    }

    public void setFlagged(List<String> flagged) {
    	this.flagged.clear();
    	this.flagged.addAll(flagged);
    }

    public Map<String, List<String>> getIpMap() {
        return ipMap;
    }

    public Map<String, List<String>> getHwidMap() {
        return hwidMap;
    }

    public Map<String, List<String>> getMacMap() {
        return macMap;
    }

    public void setIpMap(Map<String, List<String>> map){
        ipMap.clear();
        ipMap.putAll(map);
    }

    public void setHwidMap(Map<String, List<String>> map){
        hwidMap.clear();
        hwidMap.putAll(map);
    }

    public void setMacMap(Map<String, List<String>> map){
        macMap.clear();
        macMap.putAll(map);
    }

    public void loadAll(){
        if(!new File(SERVER_SECURITY_FILE).exists()){
            return;
        }
        new ServerSecurityLoad(this).loadJSON(SERVER_SECURITY_FILE).run();
        if(!new File(SERVER_MAPS).exists()){
            saveMaps();
            return;
        }
        new ServerMapsLoad(this).loadJSON(SERVER_MAPS).run();
    }

    public void load(){
        if(!new File(SERVER_SECURITY_FILE).exists()){
            return;
        }
        new ServerSecurityLoad(this).loadJSON(SERVER_SECURITY_FILE).run();
    }

    public void save(){
        new ServerSecuritySave(this).create().save(SecurityUtils.SERVER_SECURITY_FILE);
    }

    private void saveMaps(){
        new ServerMapsSave(this).create().save();
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

    public int screenPlayer(Player player, int base){
        int code = checkPlayerStatus(player);

        if(code != LOGIN_SUCCESSFUL){
            return code;
        }

        code = checkAssociation(player, base);

        return code == 0 ? player.getPSecurity().loginCode() : code;
    }

    private int checkAssociation(@NotNull Player player, int code){
        String ip = player.getHostAddress();
        String mac = player.getMac();
        PlayerSecurity sec = player.getPSecurity();
        String hwid = sec.getHwid();

        if(sec.getAssociation("username").size() > 3){
            if (!flagged.contains(player.getUsername())) {
                flagged.add(player.getUsername());
                AdminCord.sendMessage(1116230759225765968L, "[" + player.getUsername() + "] had been flagged more multi accounts.");
            }
        }

        if(ipMap.containsKey(ip)){
            List<String> list = ipMap.get(ip);
            for(String name : list){
                if(sec.containsAssociation("username", name)){
                    continue;
                }
                sec.addAssociation("username", name);
            }
//            if(!list.contains(player.getUsername())){
//                list.add(player.getUsername());
//                ipMap.put(ip, list);
//            }
        } else {
            ipMap.put(ip, Collections.singletonList(player.getUsername()));
        }

        if(macMap.containsKey(mac)){
            List<String> list = macMap.get(mac);
            for(String name : list){
                if(sec.containsAssociation("username", name)){
                    continue;
                }
                sec.addAssociation("username", name);
            }
//            if(!list.contains(player.getUsername())){
//                list.add(player.getUsername());
//                macMap.put(mac, list);
//            }
        } else {
            macMap.put(mac, Collections.singletonList(player.getUsername()));
        }

        if(hwidMap.containsKey(hwid)){
            List<String> list = hwidMap.get(hwid);
            for(String name : list){
                if(sec.containsAssociation("username", name)){
                    continue;
                }
                sec.addAssociation("username", name);
            }
//            if(!list.contains(player.getUsername())){
//                list.add(player.getUsername());
//                hwidMap.put(hwid, list);
//            }
        } else {
            hwidMap.put(hwid, Collections.singletonList(player.getUsername()));
        }

        saveMaps();
        sec.save();

        if(sec.getAssociation("username").size() > 3){
            if(code == NEW_ACCOUNT) {
                ipMap.get(ip).remove(player.getUsername());
                macMap.get(mac).remove(player.getUsername());
                hwidMap.get(hwid).remove(player.getUsername());
                sec.removeAssociation("username", player.getUsername());
                AdminCord.sendMessage(1116230759225765968L, "[" + player.getUsername() + "] is flagged for creating multiple accounts.");
                return NEW_ACCOUNT_LIMIT;
            }
        }

        return 0;
    }

    public void banPlayer(Player player, int type, long time){
        switch(type){
            case 0: //normal
                addSecurity("player", player.getUsername().toLowerCase(), String.valueOf(System.currentTimeMillis()+ time));
                break;
            case 1: //tri
                addSecurity("player", player.getUsername().toLowerCase(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("ip", player.getPSecurity().getIp(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("mac", player.getMac(), String.valueOf(System.currentTimeMillis()+ time));
                break;
            case 2: //full
                addSecurity("mac", player.getMac(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("player", player.getUsername().toLowerCase(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("ip", player.getPSecurity().getIp(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("hwid", player.getHWID(), String.valueOf(System.currentTimeMillis()+ time));
                break;
            case 3: // max
                addSecurity("mac", player.getMac(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("hwid", player.getHWID(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("player", player.getUsername().toLowerCase(), String.valueOf(System.currentTimeMillis()+ time));
                addSecurity("ip", player.getPSecurity().getIp(), String.valueOf(System.currentTimeMillis()+ time));
                break;

            case 4://perm
                addSecurity("mac", player.getMac(), String.valueOf(-1));
                addSecurity("player", player.getUsername().toLowerCase(), String.valueOf(-1));
                addSecurity("hwid", player.getHWID(), String.valueOf(-1));
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
        AdminCord.sendMessage(1116230759225765968L, player.getUsername()+" was just banned "+type);
    }

    public void mutePlayer(Player player, int time){
        addSecurity("mute", player.getUsername().toLowerCase(), String.valueOf(System.currentTimeMillis()+ (1000 * 60 * (5L * time))));
        player.save();
        save();
    }

    private int checkPlayerStatus(@NotNull Player player){
        if(isPlayerBanned(player.getUsername().toLowerCase())){
            AdminCord.sendMessage(1116230759225765968L, player.getUsername()+" is banned and attempted to login");
            return ACCOUNT_BANNED;
        }
        if(isIPBanned(player.getHostAddress())){
            AdminCord.sendMessage(1116230759225765968L, player.getUsername()+" is ipbanned and attempted to login");
            return ACCOUNT_BANNED;
        }
        if(isMACBanned(player.getMac())){
            AdminCord.sendMessage(1116230759225765968L, player.getUsername()+" is macbanned and attempted to login");
            return ACCOUNT_BANNED;
        }
        if(isHWIDBanned(player.getPSecurity().getHwid())){
            return ACCOUNT_BANNED;
        }

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
                || whiteList(player.getUsername().toLowerCase())){
            return 0;
        }
        if(isBlackList(player.getPSecurity().getIp())){
            AdminCord.sendMessage(1116230759225765968L, player.getUsername()+" is blacklisted "+player.getPSecurity().getIp());
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
                AdminCord.sendMessage(1116230759225765968L, player.getUsername()+" is using a VPN "+player.getPSecurity().getIp());
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
