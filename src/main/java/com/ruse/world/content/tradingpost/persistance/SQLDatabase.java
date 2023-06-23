package com.ruse.world.content.tradingpost.persistance;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.content.tradingpost.models.Coffer;
import com.ruse.world.content.tradingpost.models.Offer;
import com.ruse.world.content.tradingpost.concurrency.TradingPostService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SQLDatabase implements Database {

    private static final TradingPostService service = TradingPostService.getInstance();

    private static final String CREATE_OFFER = "INSERT INTO live_offers(item_id, item_name, item_bonus, item_effect, item_rarity, item_initial_amount, item_amount_sold, price, seller, slot) VALUES(?,?,?,?,?,?,?,?,?, ?)";
    private static final String CREATE_COFFER = "INSERT INTO coffers(username, amount) VALUES(?,?)";
    private static final String GET_ALL_OFFERS = "SELECT * FROM live_offers";

    @Override
    public void createOffer(Offer offer) {
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
                stmt.setInt(10, offer.getSlot());
                System.out.println(offer);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void createCoffer(Coffer coffer) {
        service.takeRequest((connection) -> {
            try(PreparedStatement stmt = connection.prepareStatement(CREATE_COFFER)
            ) {
                stmt.setString(1, coffer.getUsername());
                stmt.setInt(2, coffer.getAmount());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void loadOffers(List<Offer> offerList) {
        service.takeRequest((connection) -> {
            try(Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(GET_ALL_OFFERS)
            ) {
                while(rs.next()) {
                    Offer offer = new Offer(rs.getInt(2), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getInt(9), rs.getString(10), rs.getInt(11));
                    offer.setAmountSold(rs.getInt(8));
                    offerList.add(offer);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
