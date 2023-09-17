package com.ruse.io.database;

import com.ruse.GameSettings;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

public class DataSource {

    private static HikariDataSource ds;

    public static HikariDataSource getInstance(){
        try {
            if (ds == null || ds.isClosed()
                    || ds.getConnection() == null ||
            ds.getConnection().isClosed()){
                ds = new HikariDataSource();
                ds.setJdbcUrl(GameSettings.DATABASE_URL);
                ds.setUsername(GameSettings.DATABASE_USER);
                ds.setPassword(GameSettings.DATABASE_PASS);
                ds.setMaximumPoolSize(20);
                ds.setConnectionTimeout(120000);
                ds.setLeakDetectionThreshold(300000);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ds;
    }
}
