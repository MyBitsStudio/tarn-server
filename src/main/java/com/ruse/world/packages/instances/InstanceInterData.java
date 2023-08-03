package com.ruse.world.packages.instances;

import com.ruse.model.Item;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum InstanceInterData {

    /**
     * Multi Instances
     */

    DEATH_GOD("Death God", "",
            new Item(10835, 1000), 16, 100, 9915,
    InstanceType.MULTI),

    AGTHOMOTH("Ag'thomoth", "",
            new Item(10835, 5000), 1, 100, 3013,
            InstanceType.SINGLE),

    DONATOR("Donator Semi-Boss", "",
            new Item(10835, 5000), 16, 100, 9818,
            InstanceType.SPECIAL),

    SUMMER("Summer Solstice Boss", "",
            new Item(10835, 5000), 16, 100, 3019,
            InstanceType.EVENT),

    ZEIDAN("Zeidan Grimm", "",
            new Item(10835, 250000), 1, 100, 3010,
            InstanceType.MASTER),
//
//    KOLGAL("Kol'gal", "300 Nagendra",
//            new Item(10835, 10000), 16, 100, 9815,
//            InstanceType.MULTI),
//
//    IGTHAUR("Ig'thaur", "300 Yisdar",
//            new Item(10835, 10000), 16, 100, 9920,
//            InstanceType.MULTI),
//
//    ZERNATH("Zernath", "300 Ig'thaur",
//            new Item(10835, 10000), 16, 100, 3831,
//            InstanceType.MULTI),
//
//    DEATH_GOD("Death God", "500 Zorbak",
//            new Item(10835, 10000), 16, 100, 9915,
//            InstanceType.MULTI),
//
//    GOLDEN_GOLEM("Golden Golem", "500 Emerald Slayer",
//            new Item(10835, 10000), 16, 100, 9024,
//            InstanceType.MULTI),
//
//    LUFFY("Luffy", "750 Golden Golem",
//            new Item(10835, 10000), 16, 100, 9916,
//            InstanceType.MULTI),
//
//    BROLY("Broly", "750 Luffy",
//            new Item(10835, 10000), 16, 100, 9918,
//            InstanceType.MULTI),
//
//    BOWSER("Bowser", "750 Broly",
//            new Item(10835, 10000), 16, 100, 9919,
//            InstanceType.MULTI),
//
//    SASUKE("Sasuke", "1k Bowser",
//            new Item(10835, 10000), 16, 100, 9914,
//            InstanceType.MULTI),

//    SANCTUM_GOLEM("Sanctum Golem", "50k NPC Kills",
//            new Item(10835, 10000), 16, 100, 9017,
//            InstanceType.MULTI),
//
//    DRAGONITE("Dragonite", "1k Gorvek",
//            new Item(10835, 10000), 16, 100, 9816,
//            InstanceType.MULTI),
//
//    ASMODEUS("Asmodeus", "2k Dragonite",
//            new Item(10835, 10000), 16, 100, 9903,
//            InstanceType.MULTI),
//
//    MALVEK("Malvek", "2k Asmodeus",
//            new Item(10835, 10000), 16, 100, 8002,
//            InstanceType.MULTI),
//
//
//    ZEIDAN_GRIMM("Zeidan Grimm", "2k Onyx Griffin",
//            new Item(10835, 10000), 16, 100, 3010,
//            InstanceType.MULTI),
//
//    AGTHOMOTH("Ag'thomoth", "2k Zeidan Grimm",
//            new Item(10835, 10000), 16, 100, 3013,
//            InstanceType.MULTI),
//
//    LILINRYSS("Lilinryss", "3k Ag'thomoth",
//            new Item(10835, 10000), 16, 100, 3014,
//            InstanceType.MULTI),
//
//    GROUNDON("Groundon", "3k Lilinryss",
//            new Item(10835, 10000), 16, 100, 8010,
//            InstanceType.MULTI), //// here
//
//    VARTHRAMOTH("Varthramoth", "3k Groundon",
//            new Item(10835, 10000), 16, 100, 3016,
//            InstanceType.MULTI),
//
//    LUCIFER("Lucifer", "3k Tyrant Lord",
//            new Item(10835, 10000), 16, 100, 9012,
//            InstanceType.MULTI),
//
//    VIRTUOSO("Virtuoso", "3k Lucifer",
//            new Item(10835, 10000), 16, 100, 3019,
//            InstanceType.MULTI),
//
//    AGUMON("Agumon", "3k Virtuoso",
//            new Item(10835, 10000), 16, 100, 3020,
//            InstanceType.MULTI),
//
//    SLENDER_MAN("Slender Man", "5k Calamity",
//            new Item(10835, 10000), 16, 100, 9913,
//            InstanceType.MULTI),
//
//    EXODON("Exodon", "5k Scylla",
//            new Item(10835, 10000), 16, 100, 12239,
//            InstanceType.MULTI),
//
//    EZKELNOJAD("Ezkel-Nojad", "5k Exodon",
//            new Item(10835, 10000), 16, 100, 3112,
//            InstanceType.MULTI),
//
//    JANEMBA("Janemba", "5k Ezkel-Nojad",
//            new Item(10835, 10000), 16, 100, 3011,
//            InstanceType.MULTI),
//
//    FRIEZA("Frieza", "5k Janemba",
//            new Item(10835, 10000), 16, 100, 252,
//            InstanceType.MULTI),
//
//    PERFECT_CELL("Perfect Cell", "5k Frieza",
//            new Item(10835, 10000), 16, 100, 449,
//            InstanceType.MULTI),
//
//    SUPER_BUU("Super Buu", "5k Perfect Cell",
//            new Item(10835, 10000), 16, 100, 452,
//            InstanceType.MULTI),
//
//    GOKU("Goku", "5k Super Buu",
//            new Item(10835, 10000), 16, 100, 187,
//            InstanceType.MULTI),
//
//    QUEEN_FAZULA("Queen Fazula", "15k Byakuya",
//            new Item(10835, 10000), 16, 100, 1311,
//            InstanceType.MULTI),
//
//    LORD_YASUDA("Lord Yasuda", "15k Queen Fazula",
//            new Item(10835, 10000), 16, 100, 1313,
//            InstanceType.MULTI),
//
//    BLACK_GOKU("Black Goku", "25k Lord Yasuda",
//            new Item(10835, 10000), 16, 100, 1318,
//            InstanceType.MULTI),
//
//    ENRAGED_SUPREME("Enraged Supreme", "50k Black Goku",
//            new Item(10835, 10000), 16, 100, 440,
//            InstanceType.MULTI),
//
//    //NEXT LIST
//    DARK_SUPREME("Dark Supreme", "50k Enraged Supreme",
//            new Item(10835, 30000), 16, 100, 438,
//            InstanceType.MULTI_2),
//
//    /**
//     * Single Instances
//     */
//
//
//    /**
//     * Special Instances
//     */
//
//    ASTA("Asta", "50k Black Goku",
//            new Item(13650, 100), 4, 999999, 595,
//            InstanceType.SPECIAL),
//    DONATOR("Donator", "3 Donator Tickets",
//            new Item(23204, 3), 2, 200, 591,
//            InstanceType.SPECIAL),
//    VOTE("Vote", "3 Vote Tickets",
//            new Item(23205, 3), 2, 200, 593,
//            InstanceType.SPECIAL),
//
//    IRONMAN("Ironman", "Ironman Mode Only",
//            new Item(10835, 30000), 4, 50, 1880,
//            InstanceType.SPECIAL),
//
//    /**
//     * EVENT
//     */
//    FLASH_EVENTS("Flash Boss", "1 Flash Ticket",
//            new Item(23211, 1), 4, 200, 1120,
//            InstanceType.EVENT),
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

    public static InstanceInterData[] getSingleInstances(){
        return Arrays.stream(InstanceInterData.values())
                .filter(i -> i.getType() == InstanceType.SINGLE).toArray(InstanceInterData[]::new);
    }

    public static InstanceInterData[] getSpecialInstances(){
        return Arrays.stream(InstanceInterData.values())
                .filter(i -> i.getType() == InstanceType.SPECIAL).toArray(InstanceInterData[]::new);
    }

    public static InstanceInterData[] getEventInstances(){
        return Arrays.stream(InstanceInterData.values())
                .filter(i -> i.getType() == InstanceType.EVENT).toArray(InstanceInterData[]::new);
    }

    public static InstanceInterData[] getMasterInstances(){
        return Arrays.stream(InstanceInterData.values())
                .filter(i -> i.getType() == InstanceType.MASTER).toArray(InstanceInterData[]::new);
    }
}
