package com.ruse.world.packages.tradingpost.persistance;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.packages.tradingpost.models.Coffer;
import com.ruse.world.packages.tradingpost.models.History;
import com.ruse.world.packages.tradingpost.models.Offer;
import com.ruse.world.packages.tradingpost.concurrency.TradingPostService;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class SQLDatabase implements Database {

    private static final TradingPostService service = TradingPostService.getInstance();

    private static final String CREATE_OFFER = "INSERT INTO live_offers(item_id, item_name, item_initial_amount, item_amount_sold, price, seller, slot, time_stamp, uid, perk, bonus) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    private static final String CREATE_COFFER = "INSERT INTO coffers(username, amount) VALUES(?,?)";
    private static final String GET_ALL_OFFERS = "SELECT * FROM live_offers";
    private static final String DELETE_OFFER = "DELETE FROM live_offers WHERE seller = ? AND slot = ? LIMIT 1";
    private static final String UPDATE_OFFER = "UPDATE live_offers SET item_amount_sold = ? WHERE seller = ? AND slot = ? LIMIT 1";
    private static final String CREATE_HISTORY = "INSERT INTO offer_history(item_id, item_name, item_amount, price_each, total_price, seller, buyer, time_stamp) VALUES(?,?,?,?,?,?,?,?)";
    private static final String UPDATE_COFFER = "UPDATE coffers SET amount = ? WHERE username = ? LIMIT 1";
    private static final String GET_ALL_COFFERS = "SELECT * FROM coffers";
    private static final String GET_ALL_HISTORY = "SELECT * FROM offer_history";

    @Override
    public void createOffer(Offer offer) {
        service.takeRequest((connection) -> {
            try(PreparedStatement stmt = connection.prepareStatement(CREATE_OFFER)
            ) {
                stmt.setInt(1, offer.getItemId());
                stmt.setString(2, ItemDefinition.forId(offer.getItemId()).getName());
                stmt.setInt(3, offer.getInitialAmount());
                stmt.setInt(4, offer.getAmountSold());
                stmt.setInt(5, offer.getPrice());
                stmt.setString(6, offer.getSeller());
                stmt.setInt(7, offer.getSlot());
                stmt.setLong(8, offer.getTimestamp());
                stmt.setString(9, offer.getUid());
                stmt.setString(10, offer.getPerk());
                stmt.setString(11, offer.getBonus());
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
    public void updateCoffer(Coffer coffer) {
        service.takeRequest((connection) -> {
            try(PreparedStatement stmt = connection.prepareStatement(UPDATE_COFFER)
            ) {
                stmt.setInt(1, coffer.getAmount());
                stmt.setString(2, coffer.getUsername());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void loadOffers(LinkedList<Offer> offerList) {
        service.takeRequest((connection) -> {
            try(Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(GET_ALL_OFFERS)
            ) {
                while(rs.next()) {
                    Offer offer = new Offer(rs.getInt(2), rs.getString(10), rs.getString(11), rs.getString(12), rs.getInt(4), rs.getInt(6), rs.getString(7), rs.getInt(8), rs.getLong(9));
                    offer.setAmountSold(rs.getInt(5));
                    offerList.add(offer);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void deleteOffer(Offer offer) {
        service.takeRequest((connection) -> {
            try(PreparedStatement stmt = connection.prepareStatement(DELETE_OFFER)
            ) {
                stmt.setString(1, offer.getSeller());
                stmt.setInt(2, offer.getSlot());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void updateOffer(Offer offer) {
        service.takeRequest((connection) -> {
            try(PreparedStatement stmt = connection.prepareStatement(UPDATE_OFFER)
            ) {
                stmt.setInt(1, offer.getAmountSold());
                stmt.setString(2, offer.getSeller());
                stmt.setInt(3, offer.getSlot());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void createHistory(History history) {
        service.takeRequest((connection) -> {
            try(PreparedStatement stmt = connection.prepareStatement(CREATE_HISTORY)
            ) {
                stmt.setInt(1, history.itemId());
                stmt.setString(2, ItemDefinition.forId(history.itemId()).getName());
                stmt.setInt(3, history.amount());
                stmt.setInt(4, history.price());
                stmt.setInt(5, history.price()*history.amount());
                stmt.setString(6, history.seller());
                stmt.setString(7, history.buyer());
                stmt.setLong(8, history.timestamp());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void loadHistory(HashMap<Integer, LinkedList<History>> histories) {
        service.takeRequest((connection) -> {
            try(Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(GET_ALL_HISTORY)
            ) {
                while(rs.next()) {
                    long timestamp = rs.getLong(9);
                    History history = new History(rs.getInt(2), rs.getInt(4), rs.getInt(5), rs.getString(7), rs.getString(8), timestamp, new Date(timestamp));
                    int itemId = history.itemId();
                    LinkedList<History> historyLinkedList = histories.computeIfAbsent(itemId, x -> new LinkedList<>());
                    historyLinkedList.add(history);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void loadCoffers(HashMap<String, Coffer> cofferHashMap) {
        service.takeRequest((connection) -> {
            try(Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(GET_ALL_COFFERS)
            ) {
                while(rs.next()) {
                    String username = rs.getString(2);
                    int amount = rs.getInt(3);
                    Coffer coffer = new Coffer(username, amount);
                    cofferHashMap.put(username, coffer);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
