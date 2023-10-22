package com.ruse;

import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.net.security.ConnectionHandler;

import java.math.BigInteger;

public class GameSettings {

    public static final int GAME_VERSION = 316;
    public static final int GAME_PORT = 42166;
    public static boolean LOCALHOST = true;
    public static boolean BOGO = false;
    public static boolean B2GO = false;
    public static boolean DOUBLE_VOTE = false;
    public static boolean DOUBLE_DROP = false;
    public static boolean DOUBLE_SKILL_EXP = false;
    public static boolean DOUBLE_KOL = false;
    public static boolean DOUBLE_SLAYER = false;

    public static String broadcastMessage = null;
    public static int broadcastTime = 0;

    public static final String[] CLIENT_HASH = { "d41d8cd98f00b204e9800998ecf8427e" };

    public static final String RSPS_NAME = "Tarn";
    public static final String BCRYPT_EXAMPLE_SALT = "$2a$09$kCPIaYJ6vJmzJM/zq8vuSO";
    /**
     * WEB URLS
     */
    public static final String DomainUrl = "https://tarnserver.com/";
    public static final String StoreUrl = "https://tarnserver.com/donate";

    public static final String VoteUrl = "https://tarnserver.com/voting";
    public static final String DiscordUrl = "https://discord.gg/c5KaC3pBsn";
    public static final int BaseImplingExpMultiplier = 2;
    /**
     * Shop Buy Limit (at one time)
     */
    public static final int Shop_Buy_Limit = 25000;
    public static final int Spec_Restore_Cooldown = 180000; // in milliseconds (180000 = 3 seconds * 60 * 1000)
    public static final int Vote_Announcer = 9; // 1,2,3,4,5,6,7,8,9 = 9 total.
    public static final int massMessageInterval = 180000; // in milliseconds (180000 = 3 seconds * 60 * 1000)
    public static final int charcterBackupInterval = 3600000;
    public static final int charcterSavingInterval = 15 * 60000;

    public static boolean SAVE_BACKUP = false;
    public static final int playerCharacterListCapacity = 5000; // was 1000
    public static final int npcCharacterListCapacity = 50000; // was 2027
    /**
     * The game port
     */
    /**
     * The game version
     */
    public static final int GAME_UID = 23; // don't change
    public static final boolean BCRYPT_HASH_PASSWORDS = false;
    public static final int BCRYPT_ROUNDS = Integer.parseInt("04"); // add a 0 for numbers less than 10. IE: 04, 05, 06,
    /**
     * The maximum amount of players that can be logged in on a single game
     * sequence.
     */
    public static final int LOGIN_THRESHOLD = 100;
    /**
     * The maximum amount of players that can be logged in on a single game
     * sequence.
     */
    public static final int LOGOUT_THRESHOLD = 100;

    public static final int MAX_PLAYERS = 1024;
    /**
     * The maximum amount of players who can receive rewards on a single game
     * sequence.
     */
    public static final int VOTE_REWARDING_THRESHOLD = 15;
    // 07, 08, 09, 10, 11, etc.
    /**
     * The maximum amount of connections that can be active at a time, or in other
     * words how many clients can be logged in at once per connection. (0 is counted
     * too)
     */
    public static final int CONNECTION_AMOUNT = 3;
    /**
     * The throttle interval for incoming connections accepted by the
     * {@link ConnectionHandler}.
     */
    public static final long CONNECTION_INTERVAL = 1000;
    /**
     * The number of seconds before a connection becomes idle.
     */
    public static final int IDLE_TIME = 15;
    /**
     * The keys used for encryption on login
     */
    // REGENERATE RSA
    /*
     * This is the server, so in your PrivateKeys.txt Copy the ONDEMAND_MODULUS's
     * value to RSA_MODULUS below. Copy the ONDEMAND_EXPONENT's value to
     * RSA_EXPONENT below.
     */
    public static final BigInteger RSA_MODULUS = new BigInteger(
            "96194624385427946013325647944654257605872529630710948700405785661900897013831925044455154327577673396546454845008566365279459218895926920900465797949129028296450751054265941727527953060792972393944366210261778350949306621157386141134363849408962437807899195137501781977001604779512001906684176249221065475087");
    public static final BigInteger RSA_EXPONENT = new BigInteger(
            "10115526899149705681907460779181870678656914161033456237854899362496508566463600969905602004580966464102201575296153875429924161672913972715754105175184716220130541156461379598675913906378014007871639616450925000110336698787563369246777487335990014301550477624847390325722978604516325306982857993040042444473");
    /**
     * The maximum amount of messages that can be decoded in one sequence.
     */
    public static final int DECODE_LIMIT = 50;
    /**
     * GAME
     **/

    public static final int[] MASSACRE_ITEMS = {4587, 20051, 13634, 13632, 11842, 11868, 14525, 13734, 10828, 1704,
            15365, 15363, 7462, 3842};
    /**
     * Processing the engine
     */
    public static final int ENGINE_PROCESSING_CYCLE_RATE = 600;// 200;
    public static final int GAME_PROCESSING_CYCLE_RATE = 600;
    /**
     * The default position
     */
    public static final Position DEFAULT_POSITION = new Position(2222, 3747, 0);//3093, 3506
    public static final Position STARTER = new Position(3037, 10280, 0);//3093, 3506
    public static final Position CORP_CORDS = new Position(2900, 4384);
    public static final Position HOME_CORDS = new Position(2207, 3746, 0);//3093, 3506
    public static final Position OLDHOME_CORDS = new Position(2654, 3998, 0);
    public static final Position EDGE_CORDS = new Position(3086, 3488, 0);
    public static final Position TRIO_CORDS = new Position(3025, 5231, 0); //globals ?
    public static final Position KFC_CORDS = new Position(2606, 4774, 4);
    public static final Position TRADE_CORDS = new Position(3164, 3485, 0);
    public static final Position CHILL_CORDS = new Position(3816, 2829, 0);
    public static final Position MEMBER_ZONE = new Position(2851, 3348); // dead
    public static final Position SUPER_ZONE = new Position(1664, 5695);// dead
    public static final Position EXTREME_ZONE = new Position(2791, 3096);
    public static final Position SUPER_ZONE_NPC = new Position(2827, 2866, 8);
    public static final Position EXTREME_ZONE_NPC = new Position(2827, 2866, 12);
    public static final Position DEVILSPAWN = new Position(2958, 9487, 0); //good for big spawns
    public static final Position LORDS = new Position(2989, 9483, 0);
    public static final Position DEMON = new Position(2330, 3888, 0);
    public static final Position DRAGON = new Position(2377, 3886, 0);
    public static final Position BEAST = new Position(1698, 5600, 0);
    public static final Position HULK = new Position(3486, 3623, 0);
    public static final Position LEO = new Position(2708, 4752, 0); //smal spawns
    public static final Position KING = new Position(1625, 5601, 0); // dzone?
    public static final Position TRAIN = new Position(2517, 2521, 0); //spawns w.e

    public static final Position PENG = new Position(3041, 9543, 0);
    public static final Position CYAN = new Position(2399, 3488, 0);
    public static final Position REVS = new Position(3650, 3485, 0);
    public static final Position CMINI = new Position(2582, 2565, 0);
    public static final Position GWD = new Position(2527, 2652, 0);
    public static final Position CORP = new Position(2900, 4384, 0);
    public static final int[] hweenIds2016 = {9922, 9921, 22036, 22037, 22038, 22039, 22040};
    public static final Position PORTALS = new Position(3192, 9828, 0);
    public static final int MAX_STARTERS_PER_IP = 3;
    public static final Item nullItem = new Item(-1, 0);

    public static final Position EVENT = new Position(2731, 3463, 0); // Staff drop event
    /**
     * Untradeable items Items which cannot be traded or staked
     */

    public static final int[] UNTRADEABLE_ITEMS = {
            5020, 5022, 9719, 14505, 19640, 23020, 4000, 4001, 15682, 882, 23335,
            12852, 23003, 21819, 18653, 19641,

            //Starter items
            21403, 15004, 23300, 23301, 703, 704, 705, 23089, 23091, 22083, 22092,
            22084, 19945, 19944, 19946, 19914,

            //ironman
            16691, 17239, 16669, 22120,

            //Event Items
            14819, 23165, 20083, 17831, 20413, 20419, 20425, 20407, 17985, 17986, 17987,

            //packs
            19001, 18768, 23276, 2734, 2736, 15004, 23300, 23301, 20500, 20501, 20502,
            23253, 23254, 23255, 23256, 23257, 23258, 23259, 29, 25101, 25102, 25103,
            23330, 12630, 15230, 15231, 15232, 15234, 12533, 13263, 21075, 21076, 21077,
            21078, 21079, 21051, 21052, 18819, 4373
    };
    /**
     * Unsellable items Items which cannot be sold to shops
     */
    public static final int[] UNSELLABLE_ITEMS = new int[]{
            995, 10835
    };
    public static final int ATTACK_TAB = 0, SKILLS_TAB = 1, QUESTS_TAB = 2, ACHIEVEMENT_TAB = 15, INVENTORY_TAB = 3,
            EQUIPMENT_TAB = 4, PRAYER_TAB = 5, MAGIC_TAB = 6,

    SUMMONING_TAB = 13, FRIEND_TAB = 8, IGNORE_TAB = 9, CLAN_CHAT_TAB = 10, LOGOUT = 14, OPTIONS_TAB = 11,
            EMOTES_TAB = 12, STAFF_TAB = 7;
    public static int players = 12;
    public static boolean DOUBLEDR = false;
    public static boolean Halloween = false;
    public static boolean Christmas2016 = false;

    public static boolean LOG_CHAT = true;
    public static boolean LOG_NPCDROPS = true;
    public static boolean LOG_TRADE = true;
    public static boolean LOG_DUEL = true;
    public static boolean LOG_COMMANDS = true;
    public static boolean LOG_KILLS = true;
    public static boolean LOG_LOGINS = true;
    public static boolean LOG_LOGINSWIP = true;
    public static boolean LOG_BONDS = true;
    public static boolean LOG_SPINSWIP = true;
    public static boolean LOG_POS = true;
    public static boolean LOG_PICKUPS = true;
    public static boolean LOG_DROPPED = true;
    public static boolean LOG_DONATIONS = true;
    public static boolean LOG_CLAN = true;

    public static String DATABASE_URL = "jdbc:mysql://198.12.12.226:3306/tarnser1_website_core_en_91827";

    public static String DATABASE_USER = "tarnser1_website_core_en_user_019278";

    public static String DATABASE_PASS = "Z7W88DW4Q0lAWZ2v1oA1hbhd";

}
