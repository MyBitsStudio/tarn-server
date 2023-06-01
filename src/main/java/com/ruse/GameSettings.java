package com.ruse;

import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.net.security.ConnectionHandler;

import java.math.BigInteger;

public class GameSettings {

    public static final int GAME_VERSION = 37;
    public static final int GAME_PORT = 43216;
    public static boolean LOCALHOST = false;
    public static boolean BOGO = false;
    public static boolean B2GO = false;

    // Beta variable, allows certain functionality.. DISABLE WHEN LIVE
    public static boolean BETA_ENABLED = false;

    /*
     * Bunch of variables for the Global Events
     */
    public static boolean DOUBLE_BOXES_DONO = false;
    public static boolean DOUBLE_BONDS_DONO = false;
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
    public static final String DomainUrl = "https://discord.gg/BeENR8amj2";
    public static final String ForumUrl = "https://discord.gg/BeENR8amj2";
    public static final String StoreUrl = "https://tarnserver.everythingrs.com/services/store";
    public static final String effigy = "https://www.youtube.com/channel/UCR-GGPuNM7V51JYWVbcDURQ";
    public static final String wr3cked = "https://www.youtube.com/channel/UCNm7R0y8KN8kSn3yVn04pkg";

    public static final String VoteUrl = "https://tarn-rsps.com/vote.html";
    public static final String IronManModesUrl = "https://discord.gg/BeENR8amj2";
    public static final String DiscordUrl = "https://discord.gg/BeENR8amj2";
    public static final long tempMuteInterval = 86400000;
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
            "136372797250248515494725007031352695820661976100577123621922165213252164408563141917662973539688438987443289479949817056524738855568986013386951311178779180713642386389389086415102617381399925779316303122074928205813424959088157928614453815921447102963102742650355875523107329940229485850340433449026881293039");
    public static final BigInteger RSA_EXPONENT = new BigInteger(
            "117709635699712498657332563250523510340467318675823530662753758057025015430422506553524865147826351780546744576373670617414459428747522785682485645830007161940728810258070404056765149227916441471366929551534937681047780882276893610487874658939499765133177928826522297994196224888480621752407730939226260117633");
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
            9719, 23069, 23044, 23041, 23042, 23043, 23070, 23045, 23122, 23119, 23125, 11137, 23074, 23046, 23110, 23108, 23109,
            2028,2030,2032,2034,2036,2038,2040,2042,2044,2046,2048,2050,2052,2054,2056,2058,2060,2062,2064,2066,2068,2070,2072,2074,2076,2078,2080,2082,2084,2086,2088,2090,2092,2094,2096,2098,2100,2102,2104,2106,2108,2110,2112,2114,2116,2118,2120,2122,2124,2126,2128,2130,2132,2134,2136,2138,2140,2142,2144,2146,2148,2150,2152,2154,2156,2158,2160,2162,2164,2166,2168,2170,2172,2174,2176,2178,2180,2182,2184,2186,2188,2190,2192,2194,2196,2198,2200,2202,2204,2206,2208,2210,2212,2214,2216,2218,2220,2222,2224,2226,2228,2230,2232,2234,2236,2238,2240,2242,2244,2246,2248,2250,2252,2254,2256,2258,2260,2262,2264,2266,2268,2270,2272,2274,2276,2278,2280,2282,2284,2286,2288,2290,2292,2294,2296,2298,2300,2302,2304,2306,2308,2310,2312,2314,2316,2318,2320,2322,2324,2326,2328,2330,2332,2334,2336,2338,2340,2342,2344,2346,2348,2350,2352,2354,2356,2358,2360,2362,2364,2366,2368,2370,2372,2374,2376,2378,2384,2386,2388,2390,2392,2394,2396,2398,2400,2402,2404,2406,2408,2410,2412,2414,2416,2418,2420,2422,2424,2426,2428,2430,2432,2434,2436,2438,2440,2442,2444,2446,2448,2450,2452,2454,2456,2458,2460,2462,2464,2466,2468,2470,2472,2474,2476,2478,2480,2482,2484,2486,2488,2490,2492,2494,2496,2498,2500,2502,2504,2506,2508,2510,2512,2514,2516,2518,2520,2522,2524,2526,2528,2530,2688,2532,2534,2536,2538,2540,2542,2544,2546,2548,2550,2552,2554,2556,2558,2560,2562,2564,2566,2568,2570,2572,2574,2576,2578,2580,2582,2584,2586,2588,2590,2592,2594,2596,2598,2600,2602,2604,2606,2608,2610,2612,2614,2616,2618,2620,2622,2624,2626,2628,2630,2632,2634,2636,2638,2640,2642,2644,2646,2648,2650,2652,2654,2656,2658,2660,2662,2664,2666,2668,2670,2672,2674,2676,2678,2680,2682,2684,2686,
            23003, 23004, 23005, 23006, 23007, 23007,23018,

            11319, 11310, 15328, 15330, 4438, 14067, 11318, 9054, 9055, 9056, 9057, 9058, 13661, 13262, 13727, 20079, 6500, 20692, 10138, 17634, 7509, 7510,
            1580, 19994, // ROCK CAKES, Ice gloves
            22053, // ecumenical keys
            19748, // ardy cape 4
            21814, 21815, 21816, 6198,
            11317, 2947, 2948, 2949,
            11315, 11314, 11316,
            5154, 5155, 5156, 8830, 8831, // multipliers
            3904, 18365, 16879, 13641, 20054, // Avernic gear
            1561, // pet return
            1563, 1564, 1562, 12514, 12512, 12522, 12518, 456, 457, 458, 459, 460, 461, 462,
            /* EVENT ITEMS */
            7329, 7330, 7331, 10326, 10327, 7404, 10329, 7406, 7405, 10328, 2946, // Firelighters, colored logs, gold
            5504, 5506, 5507, 5508, 5560,                                                                    // tinderbox
            5563,
            6545, 15279, 15278,
            // easter 2017
            22051, 4565, 1037, 12845, 757,
            // dice bag - seeds
            15084,299,

            /* xmas event 2016 */
            15420, 13101, 14595, 14603, 14602, 14605,
            /* end xmas event 2016 */

            9013, 13150, // friday the 13th items (may 2016)
            9922, 9921, 22039, 22040, // hween 2k16
            /* DONE EVENT ITEMS */

            2724, // clue casket
            15707, // ring of kinship
            22014, 22015, 22016, 22017, 22018, 22019, 22020, 22021, 22022, 22023, 22024, 22025, 22026, 22027, 22028,
            22029, 22030, 22031, 22032, // skilling pets
            14130, 14131, 14140, 14141, // sacred clay tools
            6797, // unlimited watering can
            16691, 9704, 17239, 16669, 6068, 9703, // Iron Man Items
            773, // perfect ring
            5074,
            6529, 16127, 2996, 2677, 2678, 2679, 2680, 2682, 20081, 2683, 2684, 2685, 2686, 2687, 2688, 2689, 2690,
            11180, 6570, 12158, 12159, 12160, 12163, 12161, 12162, 19143, 19149, 19146, 4155, 8850, 10551, 8839, 8840,
            8842, 11663, 11664, 19712, 3321, 3322, 3319, 3320, 3318, 3323, 18350, 18366, 18354, 18358, 18356, 18352, // new
            // pvm
            // untradable
            // items
            11665, 8844, 8845, 8846, 8847, 8848, 8849, 8850, 7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, 7453,
            11665, 10499, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799, 9805, 9781, 9796, 9793,
            9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809, 9785, 9800, 9806,
            20072, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801,
            9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 9948, 9949, 9950, 12169,
            12170, 12171, 14641, 14642, 6188, 10954, 10956, 10958, 3057, 3904, 18365, 16879, 13641, 20054,621, // Avernic
            // gear
             33231, 19131, 16043, 20118, 19135, 4401, 12657, // upgzone
            5512, 5509, 5514, 5510, // rc pouches
            14076, 14077, 14081, 9925, 9924, 9923, 9922, 9921, 4565, 14595, 14603, 14602, 14605, 11789, 19708, 19706,
            19707, 4860, 4866, 4872, 4878, 4884, 4896, 4890, 4896, 4902, 4932, 4938, 4944, 4950, 4908, 4914, 4920, 4926,
            4956, 4926, 4968, 4994, 4980, 4986, 4992, 4998, 18778, 18779, 18780, 18781, 13450, 13444, 13405, 15502,
            10548, 10549, 10550, 10555, 10552, 10553, 20747, 18365, 18373, 18371, 15246, 12964, 12971, 12978, 14017,
            8851, 13855, 13848, 13849, 13857, 13856, 13854, 13853, 13852, 13851, 13850, 5509, 13653, 14021, 14020,
            19111, 14019, 14022, 19785, 19786, 18782, 18351, 18349, 18353, 18357, 18355, 18359, 18335, 11977, 11978,
            11979, 11980, 11981, 11982, 11983, 11984, 11985, 11986, 11987, 11988, 11989, 11990, 11991, 11992, 11993,
            11994, 11995, 11996, 11997, 11999, 12001, 12002, 12003, 12004, 12005, 15103, 15104, 15106, 15105, 13613,
            13619, 13622, 13623, 13616, 13614, 13617, 13618, 13626, 13624, 13627, 13628, // runecrafting shit
            22052, // member cape
              // peng
            22033, 22049, 22050, // zulrah pets
            // begin (deg) ancient armour
            13898, 13886, 13892, 13904, 13889, 13895, 13901, 13907, 13866, 13860, 13863, 13869, 13878, 13872, 13875, 19119

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
    public static boolean newYear2017 = false;
    public static boolean FridayThe13th = false;



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

}
