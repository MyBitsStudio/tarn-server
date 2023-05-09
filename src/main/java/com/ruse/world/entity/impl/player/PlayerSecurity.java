package com.ruse.world.entity.impl.player;

import com.google.gson.*;
import com.ruse.util.vpn.VPNDetection;
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
    private final static VPNDetection vpn_detection = new VPNDetection();

    private static final int SALT_LENGTH = 32; // Salt length in bytes
    private static final int ITERATIONS = 10000; // Number of iterations for key stretching

    private long lockTime, lastHashed;
    private boolean accountLocked;
    private int loginTries, lockoutTries, invalidIPAttempts;
    private String username, ip, country, currency, timezone, zip, city;;

    private String[] ips;

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

    }


    private void loadSec(){
        Path path = Paths.get("./data/saves/blocks/", username + ".json");
        File file = path.toFile();

        if (!file.exists()) {
            return;
        }

        try (FileReader fileReader = new FileReader(file)) {
            JsonParser fileParser = new JsonParser();
            Gson builder = new GsonBuilder().setPrettyPrinting().create();
            JsonObject reader = (JsonObject) fileParser.parse(fileReader);



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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(){
        Path path = Paths.get("./data/saves/blocks/", username + ".json");
        File file = path.toFile();

        try (FileWriter writer = new FileWriter(file)) {

            Gson builder = new GsonBuilder().setPrettyPrinting().create();
            JsonObject object = new JsonObject();

            object.addProperty("lockout-time", lockTime);
            object.addProperty("login-tries", loginTries);
            object.addProperty("lockout-tries", lockoutTries);
            object.addProperty("account-lock", accountLocked);
            object.addProperty("last-hashed", lastHashed);
            object.addProperty("invalid-ip", invalidIPAttempts);
            object.add("reg-ips", builder.toJsonTree(ips));

            writer.write(builder.toJson(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            byte[] salt = generateSalt();
            byte[] auth = hashPassword(password, salt);
            player.setSeed(salt);
            player.setAuth(auth);
            lastHashed = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
            save();
        }
    }

    public int loginCode(){
        int response = 0;

        if(accountLocked)
            return ACCOUNT_LOCKED;

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

            if(invalidIPAttempts > 5){
                accountLocked = true;
                save();
                return ACCOUNT_LOCKED;
            }

            invalidIPAttempts++;
            save();
            return INVALID_IP;
        }

        return 0;
    }

    public void saveSec(){
        Path path = Paths.get("./data/saves/block-sec/", username + ".json");
        File file = path.toFile();

        if(!file.exists()){
            try (FileWriter writer = new FileWriter(file)) {

                Gson builder = new GsonBuilder().setPrettyPrinting().create();
                JsonObject object = new JsonObject();

                writer.write(builder.toJson(object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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

    private boolean whiteList(String ip){

        return true;
    }

    public int checkSecurity(){
        if(ip.equals("127.0.0.1") || ip.equals("localhost") || whiteList(ip)){
            return 0;
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
                ||geolocation.getGeolocationSecurity().getThreatScore() > 20.0
            ){
                return VPN_DETECTED;
            }

            Path path = Paths.get("./data/saves/block-sec/", username + ".json");
            File file = path.toFile();

            if(!file.exists()){
                country = geolocation.getCountryName();
                currency = geolocation.getCurrency().getName();
                timezone = geolocation.getTimezone().getName();
                zip = geolocation.getZipCode();
                city = geolocation.getCity();
                saveSec();
            }


        }

        return 0;
    }
}
