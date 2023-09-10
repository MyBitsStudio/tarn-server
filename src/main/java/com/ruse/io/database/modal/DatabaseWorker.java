package com.ruse.io.database.modal;

import com.ruse.io.ThreadProgressor;
import com.ruse.io.database.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseWorker implements Runnable{
    @Override
    public void run() {
        while(true) {
            Connection connection = null;
            try {
                DatabaseRequest request = ThreadProgressor.getBoss().take();
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
