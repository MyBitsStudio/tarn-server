package com.ruse.world.content.seasonpass;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeasonPassLoader {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private static void loadLevelData() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            File file = new File("./data/seasonpass/levels.yaml");
            SeasonPass.levels = mapper.readValue(file, SPLevel[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadExperienceData() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            File file = new File("./data/seasonpass/experience.yaml");
            SeasonPass.EXP_MAP.putAll(mapper.readValue(file, Map.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        loadLevelData();
        loadExperienceData();
    }

    public static void reloadLevelData() {
        executorService.submit(SeasonPassLoader::loadLevelData);
    }

    public static void reloadExpData() {
        SeasonPass.EXP_MAP.clear();
        executorService.submit(SeasonPassLoader::loadExperienceData);
    }

}
