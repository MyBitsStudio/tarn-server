package com.ruse.security.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ruse.security.ServerSecurity;
import com.ruse.security.tools.SecurityUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class SecureSave {

    protected final Gson builder = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    protected JsonObject object = new JsonObject();

    public abstract SecureSave create();

    public abstract void save();

    public abstract String key();

    protected String set(String press){
        return press;
    }
    protected String setIp(String ip){
        return SecurityUtils.ipToDec(ip);
    }

    protected String setInt(int press){
        String tempEn = String.valueOf(press);
        StringBuilder encryptNum = new StringBuilder();
        for(int i=0;i<tempEn.length();i++) {
            int a = tempEn.charAt(i);
            a+=148113;
            encryptNum.append((char) a);
        }
        return encryptNum.toString();
    }

    protected String setBoolean(boolean press){
        return set(String.valueOf(press));
    }

    protected String setLong(long press){
        String tempEn = String.valueOf(press);
        StringBuilder encryptNum = new StringBuilder();
        for(int i=0;i<tempEn.length();i++) {
            int a = tempEn.charAt(i);
            a+=148113;
            encryptNum.append((char) a);
        }
        return encryptNum.toString();
    }

    protected String[] setIntArray(int @NotNull [] press){
        String[] encrypted = new String[press.length];
        for(int i=0;i<press.length;i++){
            encrypted[i] = setInt(press[i]);
        }
        return encrypted;
    }
}
