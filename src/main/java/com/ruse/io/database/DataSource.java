//package com.ruse.io.database;
//
//import com.ruse.GameSettings;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//
//import java.sql.SQLException;
//
//public class DataSource {
//
//    private static HikariDataSource ds;
//
//    public static HikariDataSource getInstance(){
//        try {
//            if (ds == null || ds.isClosed()
//                    || ds.getConnection() == null ||
//            ds.getConnection().isClosed()){
//                HikariConfig config = new HikariConfig();
//                config.setJdbcUrl(GameSettings.DATABASE_URL);
//                config.setUsername(GameSettings.DATABASE_USER);
//                config.setPassword(GameSettings.DATABASE_PASS);
//                config.setMaximumPoolSize(20);
//                config.setConnectionTimeout(120000);
//                config.setLeakDetectionThreshold(300000);
//                ds = new HikariDataSource(config);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return ds;
//    }
//}
