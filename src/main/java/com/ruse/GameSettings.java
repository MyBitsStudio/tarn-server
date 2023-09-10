package com.ruse;

import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.net.security.ConnectionHandler;

import java.math.BigInteger;

public class GameSettings {

    public static final int GAME_VERSION = 101;
    public static final int GAME_PORT = 43519;
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
    public static final Position MEMBER_ZONE = new Position(2851, 3348);
    public static final Position SUPER_ZONE = new Position(1664, 5695);
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
    public static final Position KING = new Position(1625, 5601, 0); //big spawns
    public static final Position PREDATOR = new Position(2901, 3617, 0);
    public static final Position BULWARK = new Position(2413, 3523, 0);
    public static final Position TRAIN = new Position(2517, 2521, 0); //spawns w.e
    public static final Position EVENT = new Position(2731, 3463, 0);
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
    /**
     * Untradeable items Items which cannot be traded or staked
     */

    public static final int[] UNTRADEABLE_ITEMS = {
            5020, 5022, 9719, 14505, 19640, 23020, 4000, 4001, 15682, 882,
            12852, 2734, 2736,

            //Starter items
            21403, 15004, 23300, 23301, 703, 704, 705, 23089, 23091, 22083, 22092,
            22084, 19945, 19944, 19946, 19914,

            //Event Items
            14819
    };
    /**
     * Unsellable items Items which cannot be sold to shops
     */
    public static final int UNSELLABLE_ITEMS[] = new int[]{14067, 15328, 15330, 4438, 9054, 9055, 9056, 9057, 9058, 11319, 11310, 11318, 2724, // clue casket
            15492, 13263, 13281, 14019, 14022, 19785, 19786, 1419, 16127, 4084, 5497, 15403, 10887, 13727, 20079, 20081,
            1959, 1960, 6199, 15501, 11848, 11850, 11856, 11854, 11852, 11846, 14000, 14001,
            //15018, 15019, 15020, 15220,
            2947, 2948, 2949, 6198, 19994,
            6545, 15279, 15278,
            14002, 14003, 2577, 19335, 15332, 19336, 19337, 19338, 19339, 19340, 9813, 20084, 8851, 6529, 14641, 14642,
            14017, 2996, 10941, 10939, 14938, 10933, 14936, 10940, 18782, 14021, 14020, 13653, 10935,
            10943, 10944, 7774, 7775, 7776, 10936, 10946,
            //1038, 1040, 1042, 1044, 1046, 1048, 6769, 10942, 10934,  // Phats
            //1053, 1055, 1057, // Hween
            5154, 5155, 5156, 8830, 8831, // multipliers
            //1050, // Santa
            5074,
            21814, 21815, 21816,
            5504, 5506, 5507, 5508, 5560,
            3904, 18365, 16879, 13641, 20054, // Avernic gear
            8830, 8831, 757, 33231, 19131, 16043, 20118, 19135, 19164, 19163, 19161, 4401, 12657, // upgzone
            19780, // Korasi's
            20671, // Brackish
            11316,
            20135, 20139, 20143, 20147, 20151, 20155, 20159, 20163, 20167, // Nex armors
            6570, 15103, 15104, 15106, 15105, // Fire cape
            19143, 19146, 19149, // God bows
            //13744,13742,13740,13747
            8844, 8845, 8846, 8847, 8848, 8849, 8850, 13262, // Defenders
            20135, 20139, 20143, 20147, 20151, 20155, 20159, 20163, 20167, // Torva, pernix, virtus
            13746, 13748, 13749, 13750, 13751, 13752, 13739, 13741, 13743,  // Spirit
            5563,                                                                                // shields
            11315, 11314,                                                            // &
            11317,

            22000,22001,22002,22003,22004,22005,22006,// slayer helms, infernal ring, deathtouch darts

            // Sigil
            /*
             * 11694, 11696, 11698, 11700, 11702, 11704, 11706, 11708, 11686, 11688, 11690,
             * 11692, 11710, 11712, 11714, //Godswords, hilts, pieces 15486, //sol 11730,
             * //ss
             *
             * 11718, 11720, 11722, //armadyl 11724, 11726, 11728, //bandos
             */
            22055, // wildywyrm pet
            11286, 11283, // dfs & visage
            14472, 14474, 14476, 14479, // dragon pieces and plate
            18685, // dragon claws
            13887, 13888, 13893, 13895, 13899, 13901, 13905, 13907, 13911, 13913, 13917, 13919, 13923, 13925, 13929,
            13931, // Vesta's
            13884, 13886, 13890, 13892, 13896, 13898, 13902, 13904, 13908, 13910, 13914, 13916, 13920, 13922, 13926,
            13928, // Statius's
            13870, 13872, 13873, 13875, 13876, 13878, 13879, 13880, 13881, 13882, 13883, 13944, 13946, 13947, 13949,
            13950, 13952, 13953, 13954, 13955, 13956, 13957, // Morrigan's
            13858, 13860, 13861, 13863, 13864, 13866, 13867, 13869, 13932, 13934, 13935, 13937, 13938, 13940, 13941,
            13943, // Zuriel's
            20147, 20149, 20151, 20153, 20155, 20157, // Pernix
            20159, 20161, 20163, 20165, 20167, 20169, // Virtus
            20135, 20137, 20139, 20141, 20143, 20145, // Torva
            11335, // D full helm
            6731, 6733, 6735, 19111, // warrior ring, seers ring, archer ring
            962, // Christmas Cracker
            //20000, 20001, 20002,
            21787, 21790, 21793, // Steadfast, glaiven, ragefire
            20674, // Something something..... pvp armor, statuettes
            13958, 13961, 13964, 13967, 13970, 13973, 13976, 13979, 13982, 13985, 13988, 13908, 13914, 13926, 13911,
            13917, 13923, 13929, 13932, 13935, 13938, 13941, 13944, 13947, 13950, 13953, 13957, 13845, 13846, 13847,
            13848, 13849, 13850, 13851, 13852, 13853, 13854, 13855, 13856, 13857, // Le corrupted items
            19712, 8830, 8831,  // peng.

            12001, 12002, 12003, 12005, 12006, 11990, 11991, 11992, 11993, 11994, 11989, 11988, 11987, 11986, 11985,
            11984, 11983, 11982, 11981, 11979,621, 19119

    };
    public static final int ATTACK_TAB = 0, SKILLS_TAB = 1, QUESTS_TAB = 2, ACHIEVEMENT_TAB = 15, INVENTORY_TAB = 3,
            EQUIPMENT_TAB = 4, PRAYER_TAB = 5, MAGIC_TAB = 6,

    SUMMONING_TAB = 13, FRIEND_TAB = 8, IGNORE_TAB = 9, CLAN_CHAT_TAB = 10, LOGOUT = 14, OPTIONS_TAB = 11,
            EMOTES_TAB = 12, STAFF_TAB = 7;
    public static int players = 0;
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
