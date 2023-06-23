package com.ruse.world.content.tradingpost.persistance;

import com.zaxxer.hikari.HikariDataSource;

public class DataSource {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/tradingpost";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static final HikariDataSource ds = new HikariDataSource();
    static {
        ds.setJdbcUrl(JDBC_URL);
        ds.setUsername(USERNAME);
        ds.setPassword(PASSWORD);
    }
}
