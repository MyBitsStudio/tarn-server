package com.ruse.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class WipeBlocks {

    public static void main(String[] args){
        Path path = Paths.get("./data/saves/blocks/");
        File file = path.toFile();

        if(!file.exists()){
            System.out.println("File does not exist!");
            return;
        }

        File[] files = file.listFiles();
        String[] ips = new String[0];
        for(File f : Objects.requireNonNull(files)){
            try (FileReader fileReader = new FileReader(file)) {
                JsonParser fileParser = new JsonParser();
                Gson builder = new GsonBuilder().setPrettyPrinting().create();
                JsonObject reader = (JsonObject) fileParser.parse(fileReader);

                if(reader.has("reg-ips")){
                    ips = builder.fromJson(reader.get("reg-ips"), String[].class);
                }
                fileReader.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try (FileWriter writer = new FileWriter(file)) {

                Gson builder = new GsonBuilder().setPrettyPrinting().create();
                JsonObject object = new JsonObject();

                object.addProperty("lockout-time", 0);
                object.addProperty("login-tries", 0);
                object.addProperty("lockout-tries", 0);
                object.addProperty("account-lock", false);
                object.addProperty("last-hashed", System.currentTimeMillis());
                object.addProperty("invalid-ip", 0);
                object.addProperty("security-check", 0);
                object.addProperty("invalid-words", 0);
                object.add("reg-ips", builder.toJsonTree(ips));

                writer.write(builder.toJson(object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
