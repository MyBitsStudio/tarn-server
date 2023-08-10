package com.ruse.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ruse.engine.GameEngine;
import com.ruse.security.tools.SecurityUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PriceStater {

    static final Gson builder = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    static JsonObject object = new JsonObject();

    public static void main(String[] args) {
        Map<Integer, Integer> prices = new HashMap<>();

        prices.put(1, 100);

        object.add("prices", builder.toJsonTree(prices));


            try (FileWriter file = new FileWriter("./.core/prices.json")) {
                file.write(builder.toJson(object));
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }



    }
}
