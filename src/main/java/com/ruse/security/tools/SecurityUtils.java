package com.ruse.security.tools;

import de.taimos.totp.TOTP;
import io.ipgeolocation.api.IPGeolocationAPI;
import lombok.val;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class SecurityUtils {

    public final static IPGeolocationAPI api = new IPGeolocationAPI("99ed94ea6c6242c684dcd8e699c28004");
    public static String SERVER_SECURITY_FILE = "./data/security/serverSecurity.json",
            PLAYER_SECURITY_FILE = "./data/security/player/",
            PLAYER_LOCK_FILE = "./data/security/locks/",
            PLAYER_FILE = "./data/security/saves/",
            DONATE = "./data/security/donate/";

    public static String[] seeds = {
            "GkXzCXaw821lnzsyCGyYuPJ2", "7VQUVMYkeGsBaMZrfeEub78J6Hud1d96"
    };

    private static final int SALT_LENGTH = 64; // Salt length in bytes
    private static final int ITERATIONS = 240000; // Number of iterations for key stretching

    public static final int OLD_SALT = 32;
    public static final int OLD_ITER = 10000;



    public static @NotNull
    String createRandomString(int length){
        int leftLimit = 48;
        int rightLimit = 122;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static byte[] salt = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };

    public static String encryptFromStringKey(String strToEncrypt, String secret) {
        try {
            Key aesKey = new SecretKeySpec(secret.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] decodedValue = Base64.getDecoder().decode(strToEncrypt.getBytes());
            val encrypted = cipher.doFinal(decodedValue);
            StringBuilder sb = new StringBuilder();
            for (byte b: encrypted) {
                sb.append((char)b);
            }

            return sb.toString();
        } catch (Exception var3) {
            System.out.println("Error while encrypting: " + var3);
            throw new RuntimeException(var3);
        }
    }

    @Contract("_, _ -> new")
    public static @NotNull String decryptFromStringKey(String enc, String secret) {
        try {
            Key aesKey = new SecretKeySpec(secret.getBytes(), "AES");
            byte[] bb = new byte[enc.length()];
            for (int i=0; i<enc.length(); i++) {
                bb[i] = (byte) enc.charAt(i);
            }

            Cipher cipher = Cipher.getInstance("AES");
            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] decodedValue = Base64.getDecoder().decode(bb);
            val decryptedVal = cipher.doFinal(decodedValue);
            return new String(decryptedVal);
        } catch (Exception var3) {
            System.out.println("Error while decrypting: " + var3);
            throw new RuntimeException(var3);
        }
    }

    public static byte @NotNull [] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] hashPassword(String password, byte[] salt) {
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

    public static byte[] hashOld(String password, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Perform key stretching
            for (int i = 0; i < OLD_ITER; i++) {
                digest.reset();
                hash = digest.digest(hash);
            }

            return hash;
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    public static boolean verifyPassword(String password, byte[] hashedPassword, byte[] salt) {
        byte[] hash = hashPassword(password, salt);
        return Arrays.equals(hashedPassword, hash);
    }

    public static boolean verifyOld(String password, byte[] hashed, byte[] salt){
        byte[] hash = hashOld(password, salt);
        return Arrays.equals(hashed, hash);
    }

    public static String ipToDec(@NotNull String ip){
        long decimal = 0;
        String[] octets = ip.split("\\.");
        for(int i = 0; i < octets.length; i++){
            int power = 3 - i;
            decimal += Integer.parseInt(octets[i]) * Math.pow(256, power);
        }
        return String.valueOf(decimal);
    }

    public static String decToIp(long i){
        return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + (i & 0xFF);
    }

    public static String base32Key(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public static boolean playerExists(String name){
        return new File(PLAYER_SECURITY_FILE + name + ".json").exists();
    }
}