package com.ruse.io.data.model;

import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.general.ArrayRowData;
import com.github.jasync.sql.db.mysql.MySQLConnection;
import com.github.jasync.sql.db.mysql.MySQLConnectionBuilder;
import com.github.jasync.sql.db.mysql.codec.PreparedStatement;
import com.github.jasync.sql.db.pool.ConnectionPool;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DataHandler {

    private static DataHandler instance;

    public static DataHandler getInstance(){
        if(instance == null)
            instance = new DataHandler();
        return instance;
    }

    private ConnectionPool<MySQLConnection> pool;

    public Connection getConnection() throws ExecutionException, InterruptedException {
        if(pool == null){
            pool = MySQLConnectionBuilder.createConnectionPool(
                    "jdbc:mysql://198.12.12.226:3306/tarnser1_website_core_en_91827?user=tarnser1_website_core_en_user_019278&password=Z7W88DW4Q0lAWZ2v1oA1hbhd"
            );
        }
        return pool.connect().get();
    }

    public CompletableFuture<QueryResult> sendStatement(String query) throws ExecutionException, InterruptedException {
        if(!getConnection().isConnected()){
            throw new RuntimeException("Connection is voided");
        }

        return getConnection().sendPreparedStatement(query);
    }

    public CompletableFuture<QueryResult> result(String query) throws ExecutionException, InterruptedException {
        if(!getConnection().isConnected()){
            throw new RuntimeException("Connection is voided");
        }

        return getConnection().sendQuery(query);
    }

}
