package com.ruse.world.packages.tradingpost.persistance;

import com.zaxxer.hikari.HikariDataSource;

public class DataSource {

    private static final String JDBC_URL = "jdbc:mysql://198.12.12.226:3306/tarnserv_trading_stand_615";
    private static final String USERNAME = "tarnserv_trading_admin_061";
    private static final String PASSWORD = "hmHZYAytpdUDsMMCWTozqVjj";

    public static final HikariDataSource ds = new HikariDataSource();
    static {
        ds.setJdbcUrl(JDBC_URL);
        ds.setUsername(USERNAME);
        ds.setPassword(PASSWORD);
        ds.setMaximumPoolSize(20);
        ds.setConnectionTimeout(120000);
        ds.setLeakDetectionThreshold(300000);
    }
}
