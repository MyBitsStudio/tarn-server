package com.ruse.world.packages.instances;

import com.ruse.model.Item;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum InstanceInterData {

    /**
     * Multi Instances
     */
    ZINGRUX("Zingrux", "1k NPC",
            new Item(10835, 10000), 16, 100, 8014,
            InstanceType.MULTI),

    DR_ABERRANT("Dr. Aberrant", "200 Zingrux",
            new Item(10835, 10000), 16, 100, 8003,
            InstanceType.MULTI),

    INFERNO("Inferno", "200 Dr.Aberrant",
                        new Item(10835, 10000), 16, 100, 202,
    InstanceType.MULTI),

    NAGENDRA("Nagendra", "200 Inferno",
            new Item(10835, 10000), 16, 100, 811,
            InstanceType.MULTI),

    KOLGAL("Kol'gal", "300 Nagendra",
            new Item(10835, 10000), 16, 100, 9815,
            InstanceType.MULTI),

    YISDAR("Yisdar", "300 Kol'gal",
            new Item(10835, 10000), 16, 100, 9817,
            InstanceType.MULTI),

    IGTHAUR("Ig'thaur", "300 Yisdar",
            new Item(10835, 10000), 16, 100, 9920,
            InstanceType.MULTI),

    ZERNATH("Zernath", "300 Ig'thaur",
            new Item(10835, 10000), 16, 100, 3831,
            InstanceType.MULTI),

    AVALON("Avalon", "300 Zernath",
            new Item(10835, 10000), 16, 100, 9025,
            InstanceType.MULTI),

    ERAGON("Nagendra", "500 Avalon",
            new Item(10835, 10000), 16, 100, 9026,
            InstanceType.MULTI),

    DOOMWATCHER("Doomwatcher", "500 Eragon",
            new Item(10835, 10000), 16, 100, 9836,
            InstanceType.MULTI),

    MAZE_GUARDIAN("Maze Guardian", "500 Doomwatcher",
            new Item(10835, 10000), 16, 100, 92,
            InstanceType.MULTI),

    MISCREATION("Miscreation", "500 Maze Guardian",
            new Item(10835, 10000), 16, 100, 3313,
            InstanceType.MULTI),

    AVATAR_TITAN("Avatar Titan", "500 Miscreation",
            new Item(10835, 10000), 16, 100, 8008,
            InstanceType.MULTI),

    ZORBAK("Zorbak", "500 Avatar Titan",
            new Item(10835, 10000), 16, 100, 1906,
            InstanceType.MULTI),

    DEATH_GOD("Death God", "500 Zorbak",
            new Item(10835, 10000), 16, 100, 9915,
            InstanceType.MULTI),

    EMERALD_SLAYER("Emerald Slayer", "500 Death God",
            new Item(10835, 10000), 16, 100, 2342,
            InstanceType.MULTI),

    GOLDEN_GOLEM("Golden Golem", "500 Emerald Slayer",
            new Item(10835, 10000), 16, 100, 9024,
            InstanceType.MULTI),

    LUFFY("Luffy", "750 Golden Golem",
            new Item(10835, 10000), 16, 100, 9916,
            InstanceType.MULTI),

    BROLY("Broly", "750 Luffy",
            new Item(10835, 10000), 16, 100, 9918,
            InstanceType.MULTI),

    BOWSER("Bowser", "750 Broly",
            new Item(10835, 10000), 16, 100, 9919,
            InstanceType.MULTI),

    SASUKE("Sasuke", "1k Bowser",
            new Item(10835, 10000), 16, 100, 9914,
            InstanceType.MULTI),

    SANCTUM_GOLEM("Sanctum Golem", "50k NPC Kills",
            new Item(10835, 10000), 16, 100, 9017,
            InstanceType.MULTI),

    MUTANT_HYDRA("Mutant Hydra", "1k Sanctum Golem",
            new Item(10835, 10000), 16, 100, 9839,
            InstanceType.MULTI),

    GORVEK("Gorvek", "1k Mutant Hydra",
            new Item(10835, 10000), 16, 100, 9806,
            InstanceType.MULTI),

    DRAGONITE("Dragonite", "1k Gorvek",
            new Item(10835, 10000), 16, 100, 9816,
            InstanceType.MULTI),

    ASMODEUS("Asmodeus", "2k Dragonite",
            new Item(10835, 10000), 16, 100, 9903,
            InstanceType.MULTI),

    MALVEK("Malvek", "2k Asmodeus",
            new Item(10835, 10000), 16, 100, 8002,
            InstanceType.MULTI),

    ONYX_GRIFFIN("Onyx Griffin", "2k Malvek",
            new Item(10835, 10000), 16, 100, 1746,
            InstanceType.MULTI),

    ZEIDAN_GRIMM("Zeidan Grimm", "2k Onyx Griffin",
            new Item(10835, 10000), 16, 100, 3010,
            InstanceType.MULTI),

    AGTHOMOTH("Ag'thomoth", "2k Zeidan Grimm",
            new Item(10835, 10000), 16, 100, 3013,
            InstanceType.MULTI),

    LILINRYSS("Lilinryss", "3k Ag'thomoth",
            new Item(10835, 10000), 16, 100, 3014,
            InstanceType.MULTI),

    GROUNDON("Groundon", "3k Lilinryss",
            new Item(10835, 10000), 16, 100, 8010,
            InstanceType.MULTI), //// here

    VARTHRAMOTH("Varthramoth", "3k Groundon",
            new Item(10835, 10000), 16, 100, 3016,
            InstanceType.MULTI),

    TYRANT_LORD("Tyrant Lord", "3k Varthramoth",
            new Item(10835, 10000), 16, 100, 4972,
            InstanceType.MULTI),

    LUCIFER("Lucifer", "3k Tyrant Lord",
            new Item(10835, 10000), 16, 100, 9012,
            InstanceType.MULTI),

    VIRTUOSO("Virtuoso", "3k Lucifer",
            new Item(10835, 10000), 16, 100, 3019,
            InstanceType.MULTI),

    AGUMON("Agumon", "3k Virtuoso",
            new Item(10835, 10000), 16, 100, 3020,
            InstanceType.MULTI),

    WHITE_BEARD("White Beard", "5k Agumon",
            new Item(10835, 10000), 16, 100, 3021,
            InstanceType.MULTI),

    PANTHER("Panther", "5k White Beard",
            new Item(10835, 10000), 16, 100, 3305,
            InstanceType.MULTI),

    LEVIATHAN("Leviathan", "5k Panther",
            new Item(10835, 10000), 16, 100, 9818,
            InstanceType.MULTI),

    CALAMITY("Calamity", "5k Leviathan",
            new Item(10835, 10000), 16, 100, 9912,
            InstanceType.MULTI),

    SLENDER_MAN("Slender Man", "5k Calamity",
            new Item(10835, 10000), 16, 100, 9913,
            InstanceType.MULTI),

    CHARYBDIS("Charybdis", "5k Slender Man",
            new Item(10835, 10000), 16, 100, 3117,
            InstanceType.MULTI),

    SCYLLA("Scylla", "5k Charybdis",
            new Item(10835, 10000), 16, 100, 3115,
            InstanceType.MULTI),

    EXODON("Exodon", "5k Scylla",
            new Item(10835, 10000), 16, 100, 12239,
            InstanceType.MULTI),

    EZKELNOJAD("Ezkel-Nojad", "5k Exodon",
            new Item(10835, 10000), 16, 100, 3112,
            InstanceType.MULTI),

    JANEMBA("Janemba", "5k Ezkel-Nojad",
            new Item(10835, 10000), 16, 100, 3011,
            InstanceType.MULTI),

    FRIEZA("Frieza", "5k Janemba",
            new Item(10835, 10000), 16, 100, 252,
            InstanceType.MULTI),

    PERFECT_CELL("Perfect Cell", "5k Frieza",
            new Item(10835, 10000), 16, 100, 449,
            InstanceType.MULTI),

    SUPER_BUU("Super Buu", "5k Perfect Cell",
            new Item(10835, 10000), 16, 100, 452,
            InstanceType.MULTI),

    GOKU("Goku", "5k Super Buu",
            new Item(10835, 10000), 16, 100, 187,
            InstanceType.MULTI),

    BYAKUYA("Byakuya", "10k Goku",
            new Item(10835, 10000), 16, 100, 188,
            InstanceType.MULTI),

    QUEEN_FAZULA("Queen Fazula", "15k Byakuya",
            new Item(10835, 10000), 16, 100, 1311,
            InstanceType.MULTI),

    LORD_YASUDA("Lord Yasuda", "15k Queen Fazula",
            new Item(10835, 10000), 16, 100, 1313,
            InstanceType.MULTI),

    BLACK_GOKU("Black Goku", "25k Lord Yasuda",
            new Item(10835, 10000), 16, 100, 1318,
            InstanceType.MULTI),

    ENRAGED_SUPREME("Enraged Supreme", "50k Black Goku",
            new Item(10835, 10000), 16, 100, 440,
            InstanceType.MULTI),

    //NEXT LIST
    DARK_SUPREME("Dark Supreme", "50k Enraged Supreme",
            new Item(10835, 30000), 16, 100, 438,
            InstanceType.MULTI_2),

    /**
     * Single Instances
     */


    /**
     * Special Instances
     */

    ASTA("Asta", "50k Black Goku",
            new Item(13650, 100), 4, 999999, 595,
            InstanceType.SPECIAL),
    DONATOR("Donator", "3 Donator Tickets",
            new Item(23204, 3), 2, 200, 591,
            InstanceType.SPECIAL),
    VOTE("Vote", "3 Vote Tickets",
            new Item(23205, 3), 2, 200, 593,
            InstanceType.SPECIAL),

    IRONMAN("Ironman", "Ironman Mode Only",
            new Item(10835, 30000), 4, 50, 1880,
            InstanceType.SPECIAL),

    /**
     * EVENT
     */
    FLASH_EVENTS("Flash Boss", "1 Flash Ticket",
            new Item(23211, 1), 4, 200, 1120,
            InstanceType.EVENT),
    /**
     * GROUP
     */

    ;

    @Getter
    private final String name, req;
    @Getter
    private final Item cost;
    @Getter
    private final int spawns, cap, npcId;
    @Getter
    private final InstanceType type;

    InstanceInterData(String name, String req,  Item cost,
                      int spawns, int cap, int npcId,
                      InstanceType type){
        this.name = name;
        this.req = req;
        this.cost = cost;
        this.spawns = spawns;
        this.cap = cap;
        this.npcId = npcId;
        this.type = type;
    }

    public static @Nullable InstanceInterData get(String name){
        for(InstanceInterData data : values()){
            if(data.getName().equalsIgnoreCase(name))
                return data;
        }
        return null;
    }

    public static InstanceInterData[] getMultiInstances(){
        return Arrays.stream(InstanceInterData.values())
                .filter(i -> i.getType() == InstanceType.MULTI).toArray(InstanceInterData[]::new);
    }

    public static InstanceInterData[] getMulti2Instances(){
        return Arrays.stream(InstanceInterData.values())
                .filter(i -> i.getType() == InstanceType.MULTI_2).toArray(InstanceInterData[]::new);
    }

    public static InstanceInterData[] getSpecialInstances(){
        return Arrays.stream(InstanceInterData.values())
                .filter(i -> i.getType() == InstanceType.SPECIAL).toArray(InstanceInterData[]::new);
    }

    public static InstanceInterData[] getEventInstances(){
        return Arrays.stream(InstanceInterData.values())
                .filter(i -> i.getType() == InstanceType.EVENT).toArray(InstanceInterData[]::new);
    }

    public static InstanceInterData[] getGroupInstances(){
        return Arrays.stream(InstanceInterData.values())
                .filter(i -> i.getType() == InstanceType.GROUP).toArray(InstanceInterData[]::new);
    }
}
