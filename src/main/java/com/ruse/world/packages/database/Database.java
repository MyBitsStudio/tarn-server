package com.ruse.world.packages.database;

import com.ruse.GameSettings;
import com.ruse.engine.GameEngine;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.database.model.Retrievals;
import com.ruse.world.packages.database.model.VoteRedeem;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Database {

    private final HikariConfig config = new HikariConfig();
    private HikariDataSource ds;

    private final ConcurrentLinkedQueue<String> statementQueue = new ConcurrentLinkedQueue<>();

    public Database(){
        config.setJdbcUrl(GameSettings.DATABASE_URL );
        config.setUsername( GameSettings.DATABASE_USER );
        config.setPassword( GameSettings.DATABASE_PASS );
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
    }


    public Connection getConnection() throws SQLException {
        if(ds == null
                || ds.isClosed()
                || ds.getConnection() == null
                || ds.getConnection().isClosed()) {
            ds = new HikariDataSource( config );
        }
        return ds.getConnection();
    }

    public void executeStatement(String statement) throws SQLException {
        statementQueue.add(statement);
    }

    public ConcurrentLinkedQueue<String> getStatementQueue() {
    	return statementQueue;
    }

    public void clearStatementQueue() {
    	statementQueue.clear();
    }

    public void executeStatementQueue() throws SQLException {
    	while(!statementQueue.isEmpty()) {
    		String statement = statementQueue.poll();
                try(Connection connection = getConnection()) {
                     connection.createStatement().execute(statement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
    	}
    }

    public List<Retrievals> getRetrievals(Player player){
        String name = Misc.formatPlayerName(player.getUsername());
        String sql = "SELECT * FROM retrievals WHERE username =?";
        List<Retrievals> retrieve = new ArrayList<>();
        try (Connection con = ds.getConnection()){
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, name);
                try (ResultSet rs = stmt.executeQuery()) {
                    while ( rs.next() ) {
                        if(rs.getInt("claimed") == 1)
                            continue;
                        retrieve.add(new Retrievals( rs.getInt("id"), rs.getInt("item_id"), rs.getInt("item_amount")));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return retrieve;
    }

    public List<VoteRedeem> redeemVotes(Player player){
        String name = Misc.formatPlayerName(player.getUsername());
        String sql = "SELECT * FROM core_votes WHERE username =?";
        List<VoteRedeem> votes = new ArrayList<>();
        try (Connection con = ds.getConnection()){
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, name);
                try (ResultSet rs = stmt.executeQuery()) {
                    while ( rs.next() ) {
                        if(rs.getInt("claimed") == 1)
                            continue;
                        if(rs.getTime("callback_date") == null)
                            continue;
                        votes.add(new VoteRedeem( rs.getInt("id"), rs.getString("uid")));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return votes;
    }
}
