package com.ruse.world.entity.impl.player;

import com.google.gson.*;
import com.ruse.engine.GameEngine;
import com.ruse.world.World;
import io.ipgeolocation.api.Geolocation;
import io.ipgeolocation.api.GeolocationParams;
import io.ipgeolocation.api.IPGeolocationAPI;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import static com.ruse.net.login.LoginResponses.*;

public class PlayerSecurity {

    private final static IPGeolocationAPI api = new IPGeolocationAPI("99ed94ea6c6242c684dcd8e699c28004");

    public static String[] WHITELIST = {}, blackList = {"88.223.153.16", "78.61.106.113", "49.180.17.203"};

    private static final int SALT_LENGTH = 32; // Salt length in bytes
    private static final int ITERATIONS = 10000; // Number of iterations for key stretching

    private long lockTime, lastHashed;
    private boolean accountLocked, needsRehash;
    private int loginTries, lockoutTries, invalidIPAttempts, securityCheck, invalidWords;
    private String username, ip, country, currency, timezone, zip, city;;

    private String[] ips;
    private byte[] countryByte, currencyByte, timezoneByte, zipByte, cityByte;

    private final Player player;

    public PlayerSecurity(Player player){
        this.player = player;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setIp(String ip){
        this.ip = ip;
    }

    public void loadAll(){
        load();
        loadSec();
    }

    private void loadSec(){
        Path path = Paths.get("./data/saves/blocks/", username + ".json");
        File file = path.toFile();

        if (!file.exists()) {
            return;
        }

            try (FileReader fileReader = new FileReader(file)) {
                JsonParser fileParser = new JsonParser();
                JsonObject reader = (JsonObject) fileParser.parse(fileReader);

                if(reader.has("country")){
                    country = reader.get("country").getAsString();
                }

                if(reader.has("currency")){
                    currency = reader.get("currency").getAsString();
                }

                if(reader.has("timezone")){
                    timezone = reader.get("timezone").getAsString();
                }

                if(reader.has("zip")){
                    zip = reader.get("zip").getAsString();
                }

                if(reader.has("city")){
                    city = reader.get("city").getAsString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    private void load(){
        Path path = Paths.get("./data/saves/blocks/", username + ".json");
        File file = path.toFile();

        if (!file.exists()) {
            save();
            return;
        }

            try (FileReader fileReader = new FileReader(file)) {
                JsonParser fileParser = new JsonParser();
                Gson builder = new GsonBuilder().setPrettyPrinting().create();
                JsonObject reader = (JsonObject) fileParser.parse(fileReader);

                if(reader.has("lockout-time")){
                    lockTime = reader.get("lockout-time").getAsLong();
                }

                if(reader.has("login-tries")){
                    loginTries = reader.get("login-tries").getAsInt();
                }

                if(reader.has("lockout-tries")){
                    lockoutTries = reader.get("lockout-tries").getAsInt();
                }

                if(reader.has("account-lock")){
                    accountLocked = reader.get("account-lock").getAsBoolean();
                }

                if(reader.has("last-hashed")){
                    lastHashed = reader.get("last-hashed").getAsLong();
                }

                if(reader.has("reg-ips")){
                    ips = builder.fromJson(reader.get("reg-ips"), String[].class);
                }

                if(reader.has("invalid-ip")){
                    invalidIPAttempts = reader.get("invalid-ip").getAsInt();
                }

                if(reader.has("security-check")){
                    securityCheck = reader.get("security-check").getAsInt();
                }

                if(reader.has("invalid-words")){
                    invalidWords = reader.get("invalid-words").getAsInt();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public void save(){
        Path path = Paths.get("./data/saves/blocks/", username + ".json");
        File file = path.toFile();

        GameEngine.submit( () -> {
            try (FileWriter writer = new FileWriter(file)) {

                Gson builder = new GsonBuilder().setPrettyPrinting().create();
                JsonObject object = new JsonObject();

                object.addProperty("lockout-time", lockTime);
                object.addProperty("login-tries", loginTries);
                object.addProperty("lockout-tries", lockoutTries);
                object.addProperty("account-lock", accountLocked);
                object.addProperty("last-hashed", lastHashed);
                object.addProperty("invalid-ip", invalidIPAttempts);
                object.addProperty("security-check", securityCheck);
                object.addProperty("invalid-words", invalidWords);
                object.add("reg-ips", builder.toJsonTree(ips));

                writer.write(builder.toJson(object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void reset(){
        lockTime = 0;
        loginTries = 0;
        lockoutTries = 0;
        accountLocked = false;
        save();
    }

    public void success(String password){
        if(lastHashed <= System.currentTimeMillis()){
//            byte[] salt = generateSalt();
//            byte[] auth = hashPassword(password, salt);
//            rehashSec();
//            player.setSeed(salt);
//            player.setAuth(auth);
//            lastHashed = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
//            save();
        }
    }

    public void raiseSecurity(){
        securityCheck++;
        if(securityCheck >= 2){
            accountLocked = true;
            save();
            World.deregister(player);
        }
        save();
    }

    public void raiseInvalidWords(){
        invalidWords++;
        if(invalidWords >= 5){
            lockTime = System.currentTimeMillis() + (1000 * 60 * 10);
            lockoutTries++;
            invalidWords = 0;
            save();
            World.deregister(player);
        }
        save();
    }

    public int loginCode(){
        int response = 0;

        if(accountLocked)
            return ACCOUNT_LOCKED;

        if(securityCheck >= 2){
            accountLocked = true;
            save();
            return ACCOUNT_LOCKED;
        }

        if(invalidWords >= 5){
            lockTime = System.currentTimeMillis() + (1000 * 60 * 10);
            lockoutTries++;
            save();
            return ACCOUNT_LOCKED;
        }

        if(invalidIPAttempts >= 5){
            accountLocked = true;
            save();
            return ACCOUNT_LOCKED;
        }

        if(lockTime > System.currentTimeMillis())
            return TEMP_LOCKED;


        if(loginTries >= 5){
            long time = 0L;
            switch(lockoutTries){
                case 0: time = (1000 * 60 * 5); response = ACCOUNT_LOCKED_5; break;
                case 1: time = (1000 * 60 * 10); response = ACCOUNT_LOCKED_10; break;
                case 2: time = (1000 * 60 * 15); response = ACCOUNT_LOCKED_15; break;
                case 3: time = (1000 * 60 * 30); response = ACCOUNT_LOCKED_30; break;
                case 4: time = (1000 * 60 * 60); response = ACCOUNT_LOCKED_1H; break;
                case 5: accountLocked = true; response = ACCOUNT_LOCKED; break;
            }
            lockTime = System.currentTimeMillis() + time;
            lockoutTries++;
            loginTries = 0;
            save();
            return response;
        }

        response = checkIP();

        save();

        return response;
    }

    private int checkIP(){
        if(checkSecurity() != 0){
            return VPN_DETECTED;
        }

        if(ips == null){
            ips = new String[12];
            ips[0] = ipToDec(ip);
            save();
            return 0;
        }

        if(!Arrays.asList(ips).contains(ipToDec(ip))){

//            if(countryByte == null) {
//                if(country == null){
//                    if(invalidIPAttempts > 5){
//                        accountLocked = true;
//                        save();
//                        return ACCOUNT_LOCKED;
//                    }
//
//                    invalidIPAttempts++;
//                    save();
//                    return INVALID_IP;
//                } else {
//                    String[] info = getInfo(ip);
//                    if(info[0] == null){
//                        if(invalidIPAttempts > 5){
//                            accountLocked = true;
//                            save();
//                            return ACCOUNT_LOCKED;
//                        }
//
//                        invalidIPAttempts++;
//                        save();
//                        return INVALID_IP;
//                    }
//                    if(info[0].equalsIgnoreCase(country)){
//                        ips[ips.length - 1] = ipToDec(ip);
//                        save();
//                        return 0;
//                    } else {
//                        if(invalidIPAttempts > 5){
//                            accountLocked = true;
//                            save();
//                            return ACCOUNT_LOCKED;
//                        }
//                        invalidIPAttempts++;
//                        save();
//                        return INVALID_IP;
//                    }
//                }
//            } else {
//                String[] info = getInfo(ip);
//                if(info[0] == null){
//                    if(invalidIPAttempts > 5){
//                        accountLocked = true;
//                        save();
//                        return ACCOUNT_LOCKED;
//                    }
//
//                    invalidIPAttempts++;
//                    save();
//                    return INVALID_IP;
//                }
//                if(verifyPassword(info[0], countryByte, player.getSeed())){
//                    ips[ips.length - 1] = ipToDec(ip);
//                    save();
//                    return 0;
//                } else {
//                    if(invalidIPAttempts > 5){
//                        accountLocked = true;
//                        save();
//                        return ACCOUNT_LOCKED;
//                    }
//
//                    invalidIPAttempts++;
//                    save();
//                    return INVALID_IP;
//                }
//            }
        }
        return 0;
    }

    private void rehashSec(){
        String[] info = getInfo(decToIp(Long.parseLong(ips[0])));
        if(info[0] == null){

        } else {
            country = info[0];
            city = info[1];
            zip = info[2];
            timezone = info[3];
            currency = info[4];
            saveSec();
        }
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

    public void saveSec(){
        Path path = Paths.get("./data/saves/block-sec/", username + ".json");
        File file = path.toFile();
        GameEngine.submit(() -> {
            if(!file.exists()){
                try (FileWriter writer = new FileWriter(file)) {

                    Gson builder = new GsonBuilder().setPrettyPrinting().create();
                    JsonObject object = new JsonObject();

                    object.addProperty("country", country);
                    object.addProperty("currency", currency);
                    object.addProperty("timezone", timezone);
                    object.addProperty("zip", zip);
                    object.addProperty("city", city);


                    writer.write(builder.toJson(object));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void invalid(){
        loginTries++;

        save();
    }

    public void start(String password){
        byte[] salt = generateSalt();
        byte[] auth = hashPassword(password, salt);
        player.setSeed(salt);
        player.setAuth(auth);
        lastHashed = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
        save();
    }

    private byte @NotNull [] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] hashPassword(String password, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Perform key stretching
            for (int i = 0; i < ITERATIONS; i++) {
                digest.reset();
                hash = digest.digest(hash);
            }

            return hash;
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    public boolean verifyPassword(String password, byte[] hashedPassword, byte[] salt) {
        byte[] hash = hashPassword(password, salt);
        return Arrays.equals(hashedPassword, hash);
    }

    public String ipToDec(@NotNull String ip){
        long decimal = 0;
        String[] octets = ip.split("\\.");
        for(int i = 0; i < octets.length; i++){
            int power = 3 - i;
            decimal += Integer.parseInt(octets[i]) * Math.pow(256, power);
        }
        return String.valueOf(decimal);
    }

    public String decToIp(long i){
        return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + (i & 0xFF);
    }

    private boolean whiteList(String ip){
        return Arrays.asList(WHITELIST).contains(ip);
    }

    private boolean isBlackList(String ip){
        return Arrays.asList(blackList).contains(ip);
    }

    public int checkSecurity(){
        if(ip.equals("127.0.0.1") || ip.equals("localhost") || whiteList(ip)){
            return 0;
        }
        if(isBlackList(ip)){
            System.out.println("Blacklist "+ ip);
            return VPN_DETECTED;
        }
        GeolocationParams geoParams = new GeolocationParams();
        geoParams.setIPAddress(ip);
        geoParams.setFields("geo,time_zone,currency");

        geoParams.setIncludeSecurity(true);
        Geolocation geolocation = api.getGeolocation(geoParams);

        if (geolocation.getStatus() == 200) {

            if(geolocation.getGeolocationSecurity().getAnonymous() || geolocation.getGeolocationSecurity().getKnownAttacker()
                || geolocation.getGeolocationSecurity().getProxy() || !geolocation.getGeolocationSecurity().getProxyType().equals("")
                || geolocation.getGeolocationSecurity().getCloudProvider() || geolocation.getGeolocationSecurity().getTor()
                ||geolocation.getGeolocationSecurity().getThreatScore() > 40.0
            ){
                System.out.println("VPN Blocked "+ ip);
                return VPN_DETECTED;
            }

            Path path = Paths.get("./data/saves/block-sec/", username + ".json");
            File file = path.toFile();

            if(file.exists()) {
                if(countryByte != null) {
                    if (!verifyPassword(geolocation.getCountryName(), countryByte, player.getSeed())) {
                        return INVALID_IP;
                    }
                }
            } else {
                country = geolocation.getCountryName();
                currency = geolocation.getCurrency().getName();
                timezone = geolocation.getTimezone().getName();
                zip = geolocation.getZipCode();
                city = geolocation.getCity();
                saveSec();
            }

        } else {
            System.out.println("Failed to get geolocation");
            return INVALID_IP;
        }

        return 0;
    }
}
