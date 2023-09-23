package com.ruse.world.packages.instances;

import com.ruse.model.Item;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Getter
public enum InstanceInterData {

    /**
     * Multi Instances
     */

    DEATH_GOD("Death God", "T10",
            new Item(995, 25000), 4, 15, 9915,
    InstanceType.MULTI),

    GOLDEN_GOLEM("Golden Golem", "T10",
        new Item(995, 40000), 8, 15, 9024,
    InstanceType.MULTI),
    MALVEK("Malvek", "T11",
            new Item(995, 80000), 12, 15, 8002,
    InstanceType.MULTI),

    GALVEK("Galvek", "T11",
            new Item(995, 90000), 16, 15, 8000,
            InstanceType.MULTI),

    BOWSER("Bowser", "T12",
            new Item(995, 60000), 16, 15, 9919,
            InstanceType.MULTI),

    SLENDER_MAN("Slender Man", "T12",
        new Item(10835, 120), 16, 15, 9913,
    InstanceType.MULTI),

    AGUMON("Agumon", "T13",
            new Item(10835, 100), 16, 15, 3020,
            InstanceType.MULTI),
    QUEEN_FAZULA("Queen Fazula", "T14",
        new Item(10835, 200), 16, 15, 1311,
    InstanceType.MULTI),

    LORD_YASUDA("Lord Yasuda", "T14",
        new Item(10835, 250), 16, 15, 1313,
    InstanceType.MULTI),

    SASUKE("Sasuke", "T15",
            new Item(10835, 300), 16, 15, 9914,
            InstanceType.MULTI),

    JOKER("Joker", "T16",
            new Item(10835, 400), 16, 15, 185,
            InstanceType.MULTI),

    BYAKUYA("Byakuya", "T16",
            new Item(10835, 500), 16, 15, 188,
            InstanceType.MULTI),

    CHARYBDIS("Charybdis", "T16",
            new Item(10835, 600), 16, 15, 3117,
            InstanceType.MULTI),

    AVARYSS("Avaryss", "T17",
            new Item(10835, 750), 8, 15, 9800,
            InstanceType.MULTI),
    NYMORA("Nymora", "T17",
            new Item(10835, 750), 8, 15, 9802,
            InstanceType.MULTI),

    HURRICANE("Hurricane", "T18",
            new Item(10835, 875), 8, 15, 12843,
            InstanceType.MULTI),
    VOLCANO("Volcano", "T18",
            new Item(10835, 875), 8, 15, 667,
            InstanceType.MULTI),
    TORNADO("Tornado", "T18",
            new Item(10835, 875), 8, 15, 5861,
            InstanceType.MULTI),


    AGTHOMOTH("Ag'thomoth", "T10",
         new Item(995, 25000), 1, 20, 3013,
    InstanceType.SINGLE),

    ZERNATH("Zernath", "T11",
        new Item(995, 45000), 1, 20, 3831,
    InstanceType.SINGLE),

    SANCTUM_GOLEM("Sanctum Golem", "T12",
        new Item(995, 70000), 1, 20, 9017,
    InstanceType.SINGLE),

    VARTHRAMOTH("Varthramoth", "T13",
        new Item(10835, 105), 1, 20, 3016,
    InstanceType.SINGLE),

    EXODON("Exodon", "T14",
        new Item(10835, 145), 1, 20, 12239,
    InstanceType.SINGLE),

    DONATOR("Donator Semi-Boss", "VIP 2 / T15",
            new Item(23003, 25), 4, 5, 9818,
            InstanceType.SPECIAL),

    DONATOR_V2("Donator Boss", "VIP 7 / Master",
         new Item(23003, 75), 4, 5, 591,
    InstanceType.SPECIAL),

    VOTE("Vote", "10 Vote Tickets / T15",
         new Item(4000, 10), 4, 5, 593,
    InstanceType.SPECIAL),

    IRONMAN("Ironman", "Ironman / T15",
        new Item(995, 75000), 4, 5, 1880,
    InstanceType.SPECIAL),

    FALL("Fall Boss", "Fall Event/T17",
            new Item(10835, 1000), 1, 15, 6430,
            InstanceType.EVENT),
    EVENT_RANDOM("Event Devils", "Fall Event/T15",
            new Item(10835, 1000), 16, 15, 1736,
            InstanceType.EVENT),

    ZEIDAN("Zeidan Grimm", "T20",
            new Item(10835, 2500), 1, 30, 3010,
            InstanceType.MASTER),

    LILINRYSS("Lilinryss", "Master",
        new Item(10835, 4000), 16, 30, 3014,
    InstanceType.MASTER),

    ASTA("Asta", "Master",
        new Item(10835, 7500), 4, 30, 595,
    InstanceType.MASTER),
//
//    KOLGAL("Kol'gal", "300 Nagendra",
//            new Item(10835, 10000), 16, 100, 9815,
//            InstanceType.MULTI),
//
//    IGTHAUR("Ig'thaur", "300 Yisdar", // single
//            new Item(10835, 10000), 16, 100, 9920,
//            InstanceType.MULTI),
//
//
//
//    BROLY("Broly", "750 Luffy",
//            new Item(10835, 10000), 16, 100, 9918,
//            InstanceType.MULTI),
//
//    SASUKE("Sasuke", "1k Bowser",
//            new Item(10835, 10000), 16, 100, 9914,
//            InstanceType.MULTI),
//
//    DRAGONITE("Dragonite", "1k Gorvek", // tower
//            new Item(10835, 10000), 16, 100, 9816,
//            InstanceType.MULTI),
//
//    GROUNDON("Groundon", "3k Lilinryss",
//            new Item(10835, 10000), 16, 100, 8010,
//            InstanceType.MULTI), //// here
//
//    LUCIFER("Lucifer", "3k Tyrant Lord",
//            new Item(10835, 10000), 16, 100, 9012,
//            InstanceType.MULTI)
//
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
//    BLACK_GOKU("Black Goku", "25k Lord Yasuda",
//            new Item(10835, 10000), 16, 100, 1318,
//            InstanceType.MULTI),
//
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
//
//
//    /**
//     * EVENT
//     */
//    FLASH_EVENTS("Flash Boss", "1 Flash Ticket",
//            new Item(23211, 1), 4, 200, 1120,
//            InstanceType.EVENT),
    ;

    private final String name, req;
    private final Item cost;
    private final int spawns, cap, npcId;
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
