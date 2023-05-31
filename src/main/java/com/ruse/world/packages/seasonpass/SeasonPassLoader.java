package com.ruse.world.packages.seasonpass;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;

public class SeasonPassLoader {

    @SneakyThrows
    private static void loadLevelData() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File file = new File("./data/seasonpass/levels.yaml");
        SeasonPass.levels = mapper.readValue(file, SeasonPassLevel[].class);
    }

    @SneakyThrows
    private static void loadExperienceData() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File file = new File("./data/seasonpass/experience.yaml");
        SeasonPassManager.EXP_MAP.putAll(mapper.readValue(file, Map.class));
    }

    @SneakyThrows
    private static void loadConfig() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File file = new File("./data/seasonpass/configuration.yaml");
        if(file.length() == 0) {
            new SeasonPassConfig();
            SeasonPassConfig.getInstance().setSeason(1);
            SeasonPassConfig.getInstance().setInterval(30);
            LocalDate localDate = LocalDate.now().plusDays(30);
            SeasonPassConfig.getInstance().setEndDate(localDate.getMonth().getValue() + "/" + localDate.getDayOfMonth() + "/" + localDate.getYear());
            SeasonPassConfig.getInstance().rewriteConfig();
            System.out.println("Writing new config");
        } else {
            mapper.readValue(file, SeasonPassConfig.class);
        }
    }

    public static void load() {
        loadLevelData();
        loadExperienceData();
        loadConfig();
        new SeasonPassScheduler().register();
    }
}
