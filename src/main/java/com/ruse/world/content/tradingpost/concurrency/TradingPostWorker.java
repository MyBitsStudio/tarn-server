package com.ruse.world.content.tradingpost.concurrency;

import com.ruse.world.content.tradingpost.persistance.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class TradingPostWorker implements Runnable {

    private final TradingPostService service;

    public TradingPostWorker(TradingPostService service) {
        this.service = service;
    }

    @Override
    public void run() {
        while(true) {
            Connection connection = null;
            try {
                TradingPostRequest request = service.getBoss().take();
                connection = DataSource.ds.getConnection();
                connection.setAutoCommit(false);
                request.execute(connection);
                connection.commit();
            } catch (InterruptedException | SQLException e) {
                if(connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                throw new RuntimeException(e);
            } finally {
                try {
                    if(connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
