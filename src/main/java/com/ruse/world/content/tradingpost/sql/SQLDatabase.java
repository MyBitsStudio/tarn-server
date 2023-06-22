package com.ruse.world.content.tradingpost.sql;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.content.tradingpost.Offer;
import com.ruse.world.content.tradingpost.TradingPostHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLDatabase {

    private static final TradingPostService service = TradingPostService.getInstance();

    private static final String CREATE_OFFER = "INSERT INTO live_offers(item_id, item_name, item_bonus, item_effect, item_rarity, item_initial_amount, item_amount_sold, price, seller) VALUES(?,?,?,?,? ?,?,?,?)";
    private static final String GET_ALL_OFFERS = "SELECT * FROM live_offers";

    public static void createOffer(Offer offer) {
        service.takeRequest((connection) -> {
            try(PreparedStatement stmt = connection.prepareStatement(CREATE_OFFER)
            ) {
                stmt.setInt(1, offer.getItemId());
                stmt.setString(2, ItemDefinition.forId(offer.getItemId()).getName());
                stmt.setInt(3,offer.getItemBonus());
                stmt.setString(4, offer.getItemEffect());
                stmt.setString(5, offer.getItemRarity());
                stmt.setInt(6, offer.getInitialAmount());
                stmt.setInt(7, offer.getAmountSold());
                stmt.setInt(8, offer.getPrice());
                stmt.setString(9, offer.getSeller());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void createCoffer() {

    }

    public static void createItemHistory() {

    }

    public static void updateOffer() {

    }

    public static void updateCoffer() {

    }

    public static void loadOffers() {
        service.takeRequest((connection) -> {
            try(Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(GET_ALL_OFFERS)
            ) {
                while(rs.next()) {
                    Offer offer = new Offer(rs.getInt(2), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getInt(9), rs.getString(10));
                    offer.setAmountSold(rs.getInt(8));
                    TradingPostHandler.addToLiveOffers(offer);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
