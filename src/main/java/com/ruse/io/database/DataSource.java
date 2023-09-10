package com.ruse.io.database;

import com.ruse.GameSettings;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {

    public static final HikariDataSource ds = new HikariDataSource();
    static {
        ds.setJdbcUrl(GameSettings.DATABASE_URL);
        ds.setUsername(GameSettings.DATABASE_USER);
        ds.setPassword(GameSettings.DATABASE_PASS);
        ds.setMaximumPoolSize(20);
        ds.setConnectionTimeout(120000);
        ds.setLeakDetectionThreshold(300000);
    }
}
