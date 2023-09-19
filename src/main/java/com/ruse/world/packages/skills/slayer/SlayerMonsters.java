package com.ruse.world.packages.skills.slayer;

import com.ruse.util.Misc;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum SlayerMonsters {

    BLURITE_GOBLIN(9837, 0, 10, 32,
            false, false,new int[]{1, 25}),
    BLURITE_ORC(9027, 0, 10, 32,
            false, false,new int[]{1, 25}),
    BLURITE_DEMON(9835, 0, 10, 32,
            false,false, new int[]{1, 25}),
    BLURITE_WEREWOLF(9911, 0, 10, 32,
            false, false,new int[]{1, 25}),
    BLURITE_CENTUAR(9922, 0, 10, 32,
            false,false, new int[]{1, 25}),

    ZINQRUX(8014, 0, 15, 46,
            false,false, new int[]{1, 50}),
    ABBARANT(8003, 0, 15, 46,
            false,false, new int[]{1, 50}),
    NAGENDRA(811, 0, 15, 46,
            false,false, new int[]{1, 50}),
    YISDAR(9817, 0, 15, 46,
            false,false, new int[]{1, 50}),
    DOOMWATCHER(9836, 0, 15, 46,
            false,false, new int[]{1, 50}),

    MAZE(92, 0, 22, 58,
            false,false, new int[]{1, 65}),
    MISCREATION(3313, 0, 22, 58,
            false,false, new int[]{1, 65}),
    ZORBAK(1906, 0, 22, 58,
            false, false,new int[]{1, 65}),

    UNICORN(1742, 0, 29, 64,
            false,false, new int[]{5, 75}),
    HYNDRA(1743, 0, 29, 64,
            false, false,new int[]{5, 75}),
    GROOTER(1744, 0, 29, 64,
            false,false, new int[]{5, 75}),
    RUNITE(1745, 0, 29, 64,
            false, false,new int[]{5, 75}),

    TOAD(1738, 0, 38, 78,
            false, false,new int[]{10, 95}),
    CLOUD(1739, 0, 38, 78,
            false, false,new int[]{10, 95}),
    MOSS(1740, 0, 38, 78,
            false, false,new int[]{10, 95}),
    FIRE(1741, 0, 38, 78,
            false, false,new int[]{10, 95}),

    AVALON(9025, 1, 52, 96,
            false, false,new int[]{15, 65}),
    ERAGON(9026, 1, 52, 96,
            false, false,new int[]{15, 65}),
    TITAN(8008, 1, 52, 96,
            false, false,new int[]{15, 65}),
    EMERALD(9839, 1, 52, 96,
            false, false,new int[]{15, 65}),

    HYDRA(9839, 1, 68, 102,
            false, false,new int[]{19, 78}),
    GORVEK(9806, 1, 68, 102,
            false, false,new int[]{19, 78}),
    GRIFFIN(9806, 1, 68, 102,
            false, false,new int[]{19, 78}),
    TYRANT(4972, 1, 68, 102,
            false, false,new int[]{19, 78}),

    BEARD(3021, 1, 74, 116,
            false, false,new int[]{24, 89}),
    PANTHER(3305, 1, 74, 116,
            false,false, new int[]{24, 89}),
    WARRIOR(125, 1, 74, 116,
            false, false,new int[]{24, 89}),

    DEATH(9915, 2, 92, 132,
            true, false, new int[]{45, 95}),
    GOLEM(9024, 2, 92, 132,
            true, false, new int[]{55, 105}),
    MALVEK(8002, 2, 92, 132,
            true, false, new int[]{65, 115}),
    GALVEK(8000, 2, 92, 132,
            true, false, new int[]{75, 125}),

    AGUMON(3020, 2, 112, 146,
            true, false, new int[]{72, 245}),
    BOWSER(9919, 2, 112, 146,
            true, false, new int[]{72, 245}),
    SLENDER(9913, 2, 112, 146,
            true, false, new int[]{72, 245}),
    LUFFY(9916, 2, 112, 146,
            true, false, new int[]{72, 25}),

    FAZULA(1311, 2, 128, 168,
            true, false, new int[]{89, 265}),
    YASUDA(1313, 2, 128, 168,
            true, false, new int[]{89, 265}),

    SASUKE(9914, 2, 144, 189,
            true, false, new int[]{94, 295}),
    JOKER(185, 2, 144, 189,
            true, false, new int[]{94, 295}),
    BYAKUYA(188, 2, 144, 189,
            true, false, new int[]{94, 295}),
    CHARYBDIS(93117, 2, 144, 189,
            true, false, new int[]{94, 295}),


    AGTHO(3013, 3, 92, 198,
            true, false, new int[]{25, 112}),
    ZERNATH(3831, 3, 92, 198,
            true, false, new int[]{25, 112}),

    SANCTUM(9017, 3, 112, 212,
            true, false, new int[]{29, 142}),
    ASMO(9903, 3, 112, 212,
            true, false, new int[]{29, 142}),

    VARTH(3016, 3, 128, 234,
            true, false, new int[]{32, 178}),
    EXODON(12239, 3, 128, 234,
            true, false, new int[]{32, 178}),
    ;

    private final int id, category, tickets, xp;
    private final int[] ranges;
    private final boolean needInstance, needsRaid;

    SlayerMonsters(int id, int category, int tickets, int xp, boolean needInstance,
                   boolean needsRaid, int[] ranges) {
        this.id = id;
        this.category = category;
        this.tickets = tickets;
        this.xp = xp;
        this.needInstance = needInstance;
        this.needsRaid = needsRaid;
        this.ranges = ranges;
    }

    public static SlayerMonsters randomForCategory(int category){
        List<SlayerMonsters> monsters = new ArrayList<>();
        for(SlayerMonsters monster : values()){
            if(monster.category == category){
                monsters.add(monster);
            }
        }
        return monsters.get(Misc.random(monsters.size() - 1));
    }

    public static @NotNull List<SlayerMonsters> forCategory(int category){
        List<SlayerMonsters> monsters = new ArrayList<>();
        for(SlayerMonsters monster : values()){
            if(monster.category == category){
                monsters.add(monster);
            }
        }
        return monsters;
    }

    public static @NotNull SlayerMonsters forId(int id){
        for(SlayerMonsters monster : values()){
            if(monster.id == id){
                return monster;
            }
        }
        return null;
    }
}
