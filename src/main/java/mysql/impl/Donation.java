package mysql.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ruse.model.PlayerRights;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;

/**
 * Using this class:
 * To call this class, it's best to make a new thread. You can do it below like so:
 * new Thread(new Donation(player)).start();
 */
public class Donation implements Runnable {

    public static final String HOST = "106.0.62.69"; // website ip address
    public static final String USER = "tarnrsps_test";
    public static final String PASS = "papyrock2";
    public static final String DATABASE = "tarnrsps_test";

    private Player player;
    private Connection conn;
    private Statement stmt;

    /**
     * The constructor
     * @param player
     */
    public Donation(Player player) {
        this.player = player;
    }

    public static final int SAPPHIRE_DONATION_AMOUNT = 10;
    public static final int EMERALD_DONATION_AMOUNT = 50;
    public static final int RUBY_DONATION_AMOUNT = 250;
    public static final int DIAMOND_DONATION_AMOUNT = 500;
    public static final int ONYX_DONATION_AMOUNT = 2500;
    public static final int ZENYTE_DONATION_AMOUNT = 5000;

    public static void checkForRankUpdate(Player player) {
        if (player.getRights().isStaff()) {
            return;
        }
        PlayerRights rights = null;
        if (player.getAmountDonated() >= SAPPHIRE_DONATION_AMOUNT)
            rights = PlayerRights.GRACEFUL_DONATOR;
        if (player.getAmountDonated() >= EMERALD_DONATION_AMOUNT)
            rights = PlayerRights.CLERIC_DONATOR;
        if (player.getAmountDonated() >= RUBY_DONATION_AMOUNT)
            rights = PlayerRights.TORMENTED_DONATOR;
        if (player.getAmountDonated() >= DIAMOND_DONATION_AMOUNT)
            rights = PlayerRights.MYSTICAL_DONATOR;
        if (player.getAmountDonated() >= ONYX_DONATION_AMOUNT)
            rights = PlayerRights.OBSIDIAN_DONATOR;
        if (player.getAmountDonated() >= ZENYTE_DONATION_AMOUNT)
            rights = PlayerRights.FORSAKEN_DONATOR;
        if (rights != null && rights != player.getRights()) {
            player.getPacketSender().sendMessage(
                    "You've become a " + Misc.formatText(rights.toString().toLowerCase()) + "! Congratulations!");
            player.setRights(rights);
            player.getPacketSender().sendRights();
        }
    }

    @Override
    public void run() {
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            String name = player.getUsername().replace("_", " ");
            ResultSet rs = executeQuery("SELECT * FROM payments WHERE player_name='"+name+"' AND status='Completed' AND claimed=0");

            while (rs.next()) {
                int item_number = rs.getInt("item_number");
                double paid = rs.getDouble("amount");
                int quantity = rs.getInt("quantity");

                switch (item_number) {// add products according to their ID in the ACP

                    case 1: // example
                        player.giveItem(item_number, quantity);
                        break;

                }

                rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!
                rs.updateRow();
            }

            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param host the host ip address or url
     * @param database the name of the database
     * @param user the user attached to the database
     * @param pass the users password
     * @return true if connected
     */
    public boolean connect(String host, String database, String user, String pass) {
        try {
           // DriverManager.getConnection("jdbc:mysql://68.65.122.147:3306/oraciuwl_store" +
                    //"user=oraciuwl_alex&password=9O7Kj4}sL4e)");
            this.conn = DriverManager.getConnection("jdbc:mysql://106.0.62.69:3306/tarnrsps_test", "tarnrsps_test", "papyrock2");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
          //  System.out.println("Failing connecting to database!");
            return false;
        }
    }

    /**
     * Disconnects from the MySQL server and destroy the connection
     * and statement instances
     */
    public void destroy() {
        try {
            conn.close();
            conn = null;
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes an update query on the database
     * @param query
     * @see {@link Statement#executeUpdate}
     */
    public int executeUpdate(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            int results = stmt.executeUpdate(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * Executres a query on the database
     * @param query
     * @see {@link Statement#executeQuery(String)}
     * @return the results, never null
     */
    public ResultSet executeQuery(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            ResultSet results = stmt.executeQuery(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
