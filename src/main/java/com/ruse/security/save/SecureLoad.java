package com.ruse.security.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ruse.security.ServerSecurity;
import com.ruse.security.tools.SecurityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;

public abstract class SecureLoad {

    protected final Gson builder = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    protected JsonObject object = new JsonObject();

    public abstract String key();

    public SecureLoad loadJSON(String path){
        try (FileReader fileReader = new FileReader(path)){
            object = builder.fromJson(fileReader, JsonObject.class);
        } catch(Exception e){
            e.printStackTrace();
        }
        return this;
    }

    public String get(String enc){
        return enc;
    }

    public int getInteger(@NotNull String enc){
        StringBuilder decodeText = new StringBuilder();
        for(int i=0;i<enc.length();i++) {
            int a= enc.charAt(i);
            a -= 148113;
            decodeText.append((char) a);
        }
        return Integer.parseInt(decodeText.toString());
    }

    public boolean getBoolean(@NotNull String enc){
        return Boolean.parseBoolean(get(enc));
    }

    public long getLong(@NotNull String enc){
        StringBuilder decodeText = new StringBuilder();
        for(int i=0;i<enc.length();i++) {
            int a= enc.charAt(i);
            a -= 148113;
            decodeText.append((char) a);
        }
        return Long.parseLong(decodeText.toString());
    }

    public int[] getIntArray(@NotNull String @NotNull [] enc){
        int[] decrypted = new int[enc.length];
        for(int i=0;i<enc.length;i++){
            decrypted[i] = getInteger(enc[i]);
        }
        return decrypted;
    }

    public abstract SecureLoad run();
}