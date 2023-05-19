package com.ruse.security.tools;

import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class FAGenerator {

    public static void main(String[] args) {
       String test = encodeDiscussionId(81759123);
       System.out.println(test +" "+ decodeDiscussionId(test));
    }

    public static String encodeDiscussionId(int Id) {

        String tempEn = Id + "";
        String encryptNum ="";
        for(int i=0;i<tempEn.length();i++) {
            int a = (int)tempEn.charAt(i);
            a+=148113;
            encryptNum +=(char)a;
        }
        return encryptNum;
    }

    public static int decodeDiscussionId(String encryptText) {

        String decodeText = "";
        for(int i=0;i<encryptText.length();i++) {
            int a= (int)encryptText.charAt(i);
            a -= 148113;
            decodeText +=(char)a;
        }
        int decodeId = Integer.parseInt(decodeText);
        return decodeId;
    }


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

    public static String encryptFromStringKey(String strToEncrypt, String secret) {
        try {
            SecretKey secretKey = setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception var3) {
            System.out.println("Error while encrypting: " + var3);
            throw new RuntimeException(var3);
        }
    }

    @Contract("_ -> new")
    public static @NotNull SecretKey setKey(@NotNull String myKey) {
        MessageDigest sha;

        byte[] key = new byte[32];

        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);

        } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
        }
        return new SecretKeySpec(key, "AES");
    }

    @Contract("_, _ -> new")
    public static @NotNull String decryptFromStringKey(String strToDecrypt, String secret) {
        try {
            SecretKey secretKey = setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(2, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception var3) {
            System.out.println("Error while decrypting: " + var3);
            throw new RuntimeException(var3);
        }
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
}
