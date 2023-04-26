package com.ruse.world.content.seasonpass;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeasonPassConfig {

    transient private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    transient private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    private static SeasonPassConfig INSTANCE;

    private int season;
    private String endDate;
    private int interval;

    public SeasonPassConfig() {
        INSTANCE = this;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    @Transient
    public LocalDate getLocalTime() {
        try {
            return sdf.parse(endDate).toInstant().atZone((ZoneId.of("UTC"))).toLocalDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public static SeasonPassConfig getInstance() {
        return INSTANCE;
    }

    public void rewriteConfig() {
        executorService.submit(() -> {
            LocalDate localDate = getLocalTime().plusDays(INSTANCE.interval);
            INSTANCE.setEndDate(localDate.getMonth().getValue() + "/" + localDate.getDayOfMonth() + "/" + localDate.getYear());
            System.out.println("EndDate: " + INSTANCE.getEndDate());
            ObjectMapper om = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
            om.enable(SerializationFeature.INDENT_OUTPUT);
            om.registerModule(new JavaTimeModule());
            try {
                File file = new File("./data/seasonpass/configuration.yaml");
                if (!file.exists()) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }
                om.writeValue(file, SeasonPassConfig.getInstance());
            } catch (IOException e) {
                System.err.println("Error writing YAML file: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
