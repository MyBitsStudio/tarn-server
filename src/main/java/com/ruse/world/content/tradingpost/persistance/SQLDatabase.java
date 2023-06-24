package com.ruse.world.content.tradingpost.persistance;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.content.tradingpost.models.Coffer;
import com.ruse.world.content.tradingpost.models.History;
import com.ruse.world.content.tradingpost.models.Offer;
import com.ruse.world.content.tradingpost.concurrency.TradingPostService;
import org.checkerframework.checker.units.qual.C;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class SQLDatabase implements Database {

    private static final TradingPostService service = TradingPostService.getInstance();

    private static final String CREATE_OFFER = "INSERT INTO live_offers(item_id, item_name, item_bonus, item_effect, item_rarity, item_initial_amount, item_amount_sold, price, seller, slot) VALUES(?,?,?,?,?,?,?,?,?, ?)";
    private static final String CREATE_COFFER = "INSERT INTO coffers(username, amount) VALUES(?,?)";
    private static final String GET_ALL_OFFERS = "SELECT * FROM live_offers";
    private static final String DELETE_OFFER = "DELETE FROM live_offers WHERE seller = ? AND slot = ? LIMIT 1";
    private static final String UPDATE_OFFER = "UPDATE live_offers SET item_amount_sold = ? WHERE seller = ? AND slot = ? LIMIT 1";
    private static final String CREATE_HISTORY = "INSERT INTO offer_history(item_id, item_name, item_bonus, item_effect, item_rarity, item_amount, price_each, total_price, seller, buyer) VALUES(?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_COFFER = "UPDATE coffers SET amount = ? WHERE username = ? LIMIT 1";
    private static final String GET_ALL_COFFERS = "SELECT * FROM coffers";

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
                stmt.setInt(3, history.bonus());
                stmt.setString(4, history.itemEffect());
                stmt.setString(5, history.itemRarity());
                stmt.setInt(6, history.amount());
                stmt.setInt(7, history.price());
                stmt.setInt(8, history.price()*history.amount());
                stmt.setString(9, history.seller());
                stmt.setString(10, history.buyer());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void loadHistory(List<History> historyList) {

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
