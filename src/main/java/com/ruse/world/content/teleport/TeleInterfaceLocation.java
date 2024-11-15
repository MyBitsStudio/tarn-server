package com.ruse.world.content.teleport;

import com.ruse.model.Position;
import lombok.Getter;

@Getter
public enum TeleInterfaceLocation {//3487, 3615 , 2711, 4752 , 2527, 2527, 1652, 5600, 2964, 9478, 2565, 2564

    LOCATION1(new Position(3037, 10280, 0), "Tier Ranking: 1 \\n \\n Multi: False \\n \\n STARTER MONSTER"), //GOBLIN
    LOCATION2(new Position(3037, 10280, 4), "Tier Ranking: 1 \\n \\n Multi: False \\n \\n STARTER MONSTER"), //ORC
    LOCATION3(new Position(3037, 10280, 8), "Tier Ranking: 1 \\n \\n Multi: False \\n \\n STARTER MONSTER"), //DEMON
    LOCATION4(new Position(3037, 10280, 12), "Tier Ranking: 1 \\n \\n Multi: False \\n \\n STARTER MONSTER"), //WEREWOLF
    LOCATION5(new Position(3037, 10280, 16), "Tier Ranking: 1 \\n \\n Multi: False \\n \\n STARTER MONSTER"), //CENTAUR
    LOCATION6(new Position(3037, 10280, 20), "Tier Ranking: 1 \\n \\n Multi: False \\n \\n STARTER MONSTER"), //hound
    LOCATION7(new Position(3037, 10280, 24), "Tier Ranking: 1 \\n \\n Multi: False \\n \\n STARTER MONSTER"), //scorpion
    LOCATION8(new Position(3037, 10280, 28), "Tier Ranking: 1 \\n \\n Multi: False \\n \\n STARTER MONSTER"), //ranger
    LOCATION9(new Position(3037, 10280, 32), "Tier Ranking: 1 \\n \\n Multi: False \\n \\n STARTER MONSTER"), //paladin
    LOCATION10(new Position(3037, 10280, 36), "Tier Ranking: 1 \\n \\n Multi: True \\n \\n STARTER MONSTER"), //wyvern
    LOCATION11(new Position(3037, 10280, 40), "Tier Ranking: 1 \\n \\n Multi: True \\n \\n STARTER MONSTER"), //Mystic
    LOCATION12(new Position(3037, 10280, 44), "Tier Ranking: 1 \\n \\n Multi: True \\n \\n STARTER MONSTER"), //Nightmare
    LOCATION13(new Position(3037, 10280, 48), "Tier Ranking: 1 \\n \\n Multi: True \\n \\n STARTER MONSTER"), //Patience
    LOCATION14(new Position(3037, 10280, 52), "Tier Ranking: 2 \\n \\n Multi: True \\n \\n STARTER MONSTER"), //Zinqrux
    LOCATION15(new Position(3037, 10280, 56), "Tier Ranking: 2 \\n \\n Multi: True \\n \\n STARTER MONSTER"), //Aberrant
    LOCATION16(new Position(3037, 10280, 60), "Tier Ranking: 2 \\n \\n Multi: True \\n \\n STARTER MONSTER"), //Inferno
    LOCATION17(new Position(3037, 10280, 64), "Tier Ranking: 2 \\n \\n Multi: True \\n \\n STARTER MONSTER"), //nagi
    LOCATION18(new Position(3037, 10280, 68), "Tier Ranking: 2 \\n \\n Multi: True \\n \\n STARTER MONSTER"), //Kolgal
    LOCATION19(new Position(3037, 10280, 72), "Tier Ranking: 2 \\n \\n Multi: True \\n \\n STARTER MONSTER"), //Yisdar
    LOCATION20(new Position(3037, 10280, 76), "Tier Ranking: 3 \\n \\n Multi: True \\n \\n STARTER MONSTER"), //Igthaur
    LOCATION21(new Position(3037, 10280, 80), "Tier Ranking: 3 \\n \\n Multi: True \\n \\n STARTER MONSTER"), //Zernath
//    LOCATION22(new Position(1652, 5600, 20), "Tier Ranking: 13 \\n \\n Multi: True \\n \\n KC Req: 300 Zernaths \\n \\n Recommended ELO: 95+"), //Avalon
//    LOCATION23(new Position(1652, 5600, 24), "Tier Ranking: 13 \\n \\n Multi: True \\n \\n KC Req: 500 Avalons \\n \\n Recommended ELO: 95+"), //Eragon
//    LOCATION24(new Position(1652, 5600, 28), "Tier Ranking: 14 \\n \\n Multi: True \\n \\n KC Req: 500 Eragons \\n \\n Recommended ELO: 100+"), //Doomwatcher
//    LOCATION25(new Position(1652, 5600, 32), "Tier Ranking: 15 \\n \\n Multi: True \\n \\n KC Req: 500 Doomwatchers \\n \\n Recommended ELO: 120+"), //Maze
//    LOCATION26(new Position(2711, 4752, 44), "Tier Ranking: 16 \\n \\n Multi: True \\n \\n KC Req: 500 Maze Guardians \\n \\n Recommended ELO: 130+"), //Miscreation
//    LOCATION27(new Position(2711, 4752, 48), "Tier Ranking: 17 \\n \\n Multi: True \\n \\n KC Req: 500 Miscreations \\n \\n Recommended ELO: 150+"), //Avatar
//    LOCATION28(new Position(2711, 4752, 52), "Tier Ranking: 18 \\n \\n Multi: True \\n \\n KC Req: 500 Avatar Titans \\n \\n Recommended ELO: 155+"), //Zorbak
//    LOCATION29(new Position(2711, 4752, 56), "Tier Ranking: 19 \\n \\n Multi: True \\n \\n KC Req: 500 Zorbaks \\n \\n Recommended ELO: 164+"), //Death God
//    LOCATION30(new Position(2527, 2527, 56), "Tier Ranking: 20 \\n \\n Multi: True \\n \\n KC Req: 500 Death Gods \\n \\n Recommended ELO: 180+"), //Emerald
//    LOCATION31(new Position(2527, 2527, 60), "Tier Ranking: 21 \\n \\n Multi: True \\n \\n KC Req: 500 Emerald Slayers \\n \\n Recommended ELO: 184+"), //Golden
//    LOCATION32(new Position(2527, 2527, 64), "Tier Ranking: 22 \\n \\n Multi: True \\n \\n KC Req: 750 Golden Golems \\n \\n Recommended ELO: 185+"), //Luffy
//    LOCATION33(new Position(2964, 9478, 64), "Tier Ranking: 23 \\n \\n Multi: True \\n \\n KC Req: 750 Luffys \\n \\n Recommended ELO: 210+"), //Broly
//    LOCATION34(new Position(2964, 9478, 68), "Tier Ranking: 24 \\n \\n Multi: True \\n \\n KC Req: 750 Brolys \\n \\n Recommended ELO: 220+"), //Bowser
//    LOCATION35(new Position(2964, 9478, 72), "Tier Ranking: 25 \\n \\n Multi: True \\n \\n KC Req: 1K Bowsers \\n \\n Recommended ELO: 280+"), //Sasuke
    //BOSSES
    LOCATION36(new Position(2962, 9477, 0), "Tier Ranking: 4 \\n \\n Multi: True \\n \\n STARTER BOSS"), //Sanctum
    LOCATION37(new Position(2962, 9477, 4), "Tier Ranking: 4 \\n \\n Multi: True \\n \\n STARTER BOSS"), //Hydra
    LOCATION38(new Position(2962, 9477, 8), "Tier Ranking: 5 \\n \\n Multi: True \\n \\n STARTER BOSS"), //Gorvek
    LOCATION39(new Position(2962, 9477, 12), "Tier Ranking: 5 \\n \\n Multi: True \\n \\n STARTER BOSS"), //Dragonite
    LOCATION40(new Position(2962, 9477, 16), "Tier Ranking: 6 \\n \\n Multi: True \\n \\n STARTER BOSS"), //Asmodeus
    LOCATION41(new Position(2962, 9477, 20), "Tier Ranking: 6 \\n \\n Multi: True \\n \\n STARTER BOSS"), //Malvek
    LOCATION42(new Position(2962, 9477, 24), "Tier Ranking: 7 \\n \\n Multi: True \\n \\n STARTER BOSS"), //Griffin
    LOCATION43(new Position(2962, 9477, 28), "Tier Ranking: 7 \\n \\n Multi: True \\n \\n STARTER BOSS"), //Zeidan
    LOCATION44(new Position(2962, 9477, 32), "Tier Ranking: 8 \\n \\n Multi: True \\n \\n STARTER BOSS"), //Agthomoth
    LOCATION45(new Position(2962, 9477, 36), "Tier Ranking: 8 \\n \\n Multi: True \\n \\n STARTER BOSS"), //Lili
    LOCATION46(new Position(2962, 9477, 40), "Tier Ranking: 9 \\n \\n Multi: True \\n \\n STARTER BOSS"), //Groudon


    SLAYER0(new Position(2637, 3797, 0), "Tier Ranking: 1 \\n \\n Multi: True \\n \\n Slayer Monsters"), //First Zone
    SLAYER1(new Position(2637, 3797, 4), "Tier Ranking: 1 \\n \\n Multi: True \\n \\n Slayer Monsters"), //First Zone
    SLAYER2(new Position(2637, 3797, 8), "Tier Ranking: 1 \\n \\n Multi: True \\n \\n Slayer Monsters"), //First Zone
    SLAYER3(new Position(2637, 3797, 12), "Tier Ranking: 1 \\n \\n Multi: True \\n \\n Slayer Monsters"), //First Zone
    SLAYER4(new Position(2637, 3797, 16), "Tier Ranking: 2 \\n \\n Multi: True \\n \\n Slayer Monsters"), //First Zone
    SLAYER5(new Position(2637, 3797, 20), "Tier Ranking: 2 \\n \\n Multi: True \\n \\n Slayer Monsters"), //First Zone
    SLAYER6(new Position(2637, 3797, 24), "Tier Ranking: 3 \\n \\n Multi: True \\n \\n Slayer Monsters"), //First Zone
    SLAYER7(new Position(2637, 3797, 28), "Tier Ranking: 4 \\n \\n Multi: True \\n \\n Slayer Monsters"), //First Zone
    SLAYER8(new Position(2637, 3797, 32), "Tier Ranking: 5-6 \\n \\n Multi: True \\n \\n Slayer Monsters"), //First Zone
//    LOCATION47(new Position(2962, 9477, 44), "Tier Ranking: 37 \\n \\n Multi: True \\n \\n KC Req: 3K Groudons \\n \\n Recommended ELO: 900+"), //Varth
//    LOCATION48(new Position(2962, 9477, 48), "Tier Ranking: 38 \\n \\n Multi: True \\n \\n KC Req: 3K Varthramoths \\n \\n Recommended ELO: 918+"), //Tyrant
//    LOCATION49(new Position(2962, 9477, 52), "Tier Ranking: 39 \\n \\n Multi: True \\n \\n KC Req: 3K Tyrant Lords \\n \\n Recommended ELO: 980+"), //lucifer
//    LOCATION50(new Position(2962, 9477, 56), "Tier Ranking: 40 \\n \\n Multi: True \\n \\n KC Req: 3K Lucifers \\n \\n Recommended ELO: 1010+"), //virtuoso
//    LOCATION51(new Position(2964, 9478, 104), "Tier Ranking: 41 \\n \\n Multi: True \\n \\n KC Req: 3K Virtuosos \\n \\n Recommended ELO: 1058+"), //Agumon
//    LOCATION52(new Position(2565, 2564, 104), "Tier Ranking: 42 \\n \\n Multi: True \\n \\n KC Req: 5K Agumons \\n \\n Recommended ELO: 1170+"), //White beard
//    LOCATION53(new Position(2964, 9478, 108), "Tier Ranking: 43 \\n \\n Multi: True \\n \\n KC Req: 5K White Beards \\n \\n Recommended ELO: 1228+"), //Panther
//    LOCATION54(new Position(2565, 2564, 112), "Tier Ranking: 44 \\n \\n Multi: True \\n \\n KC Req: 5K Panthers \\n \\n Recommended ELO: 1273+"), //levi
//    LOCATION55(new Position(2565, 2564, 116), "Tier Ranking: 45 \\n \\n Multi: True \\n \\n KC Req: 5K Leviathans \\n \\n Recommended ELO: 1390+"), //cala
//    LOCATION56(new Position(1615, 5588, 116), "Tier Ranking: 46 \\n \\n Multi: True \\n \\n KC Req: 5K Calamitys \\n \\n Recommended ELO: 1540+"), //slender
//    LOCATION57(new Position(2565, 2564, 120), "Tier Ranking: 47 \\n \\n Multi: True \\n \\n KC Req: 5K Slender Mans \\n \\n Recommended ELO: 2400+"), //charyb
//    LOCATION58(new Position(2565, 2564, 124), "Tier Ranking: 47 \\n \\n Multi: True \\n \\n KC Req: 5K Charybdis's \\n \\n Recommended ELO: 2424+"), //scylla
//    LOCATION59(new Position(2565, 2564, 128), "Tier Ranking: 48 \\n \\n Multi: True \\n \\n KC Req: 5K Scyllas \\n \\n Recommended ELO: 2464+"), //exod
//    LOCATION60(new Position(2565, 2564, 132), "Tier Ranking: 49 \\n \\n Multi: True \\n \\n KC Req: 5K Exodens \\n \\n Recommended ELO: 2578+"), //ezkel
//    LOCATION61(new Position(2964, 9478, 132), "Tier Ranking: 50 \\n \\n Multi: True \\n \\n KC Req: 5K Ezkel-Nojads \\n \\n Recommended ELO: 2804+"), //janemba
//    LOCATION62(new Position(2964, 9478, 136), "Tier Ranking: 51 \\n \\n Multi: True \\n \\n KC Req: 5K Janembas \\n \\n Recommended ELO: 2884+"), //frieza
//    LOCATION63(new Position(2964, 9478, 140), "Tier Ranking: 52 \\n \\n Multi: True \\n \\n KC Req: 5K Friezas \\n \\n Recommended ELO: 3104+"), //perfect
//    LOCATION64(new Position(2964, 9478, 144), "Tier Ranking: 53 \\n \\n Multi: True \\n \\n KC Req: 5K Perfect Cells \\n \\n Recommended ELO: 3504+"), //superbuu
//    LOCATION65(new Position(2565, 2564, 144), "Tier Ranking: 54 \\n \\n Multi: True \\n \\n KC Req: 5K Super Buus \\n \\n Recommended ELO: 3854+"), //goku
//    LOCATION66(new Position(2964, 9478, 148), "Tier Ranking: 55 \\n \\n Multi: True \\n \\n KC Req: 10K Gokus \\n \\n Recommended ELO: 4705+"), //byakuya
//    LOCATION71(new Position(2472, 10163, 0), "Tier Ranking: 56 \\n \\n Multi: True \\n \\n KC Req: 15K Byakuya \\n \\n Recommended ELO: 4705+"), //fazula
//    LOCATION72(new Position(2472, 10163, 4), "Tier Ranking: 57 \\n \\n Multi: True \\n \\n KC Req: 15K Fazula \\n \\n Recommended ELO: 4705+"), //yasuda
//    LOCATION73(new Position(2472, 10163, 8), "Tier Ranking: 58 \\n \\n Multi: True \\n \\n KC Req: 25K Yasuda \\n \\n Recommended ELO: 4705+"), //yasuda
//
//
//    LOCATION67(new Position(2137, 5016, 8), "Tier Ranking: ? \\n \\n Multi: True \\n \\n KC Req: None\\n \\n Recommended ELO: 10+"), //veigar
//    LOCATION68(new Position(2137, 5016, 12), "Tier Ranking: ? \\n \\n Multi: True \\n \\n KC Req: None\\n \\n Recommended ELO: 10+"), //nine tails
//    LOCATION69(new Position(2137, 5016, 4), "Tier Ranking: ? \\n \\n Multi: True \\n \\n KC Req: None\\n \\n Recommended ELO: 10+"), //meruem
//    LOCATION70(new Position(2137, 5016, 0), "Tier Ranking: ? \\n \\n Multi: True \\n \\n KC Req: None\\n \\n Recommended ELO: 10+"), //golden
//
//    LOCATION80(new Position(3065, 2758, 8), "Tier Ranking: ? \\n \\n Multi: True \\n \\n KC Req: None \\n \\n Recommended ELO: Counter Token"), //yasuda
//    LOCATION100(new Position(2661, 3044, 0), "Tier Ranking: 25.5 \\n \\n Multi: True \\n \\n KC Req: None\\n \\n Recommended ELO: 1150+"),

    /* New */

    TARN_TOWER(new Position(2859, 2744, 0), "Tarn Tower \\n \\n Multi: True \\n \\n KC Req: None\\n \\n Climb The Tower"),
    DAILY_DONATOR(new Position(2859, 2744, 0), "Daily Donator Instance \\n \\n Tons of rewards \\n \\n VIP 5 +\\n \\n Mine Away!!"),
    TREASURE_HUNTER(new Position(2859, 2744, 0), "Daily Treasure Hunter \\n \\n Fight The Bosses \\n \\n Raid The Chest \\n \\n Loyalty 3 +"),
    DAILY_CASKET(new Position(2859, 2744, 0), "Daily Casket Raid \\n \\n Fight The Bosses \\n \\n Raid The Chest \\n \\n Halloween Event"),
    CREATION(new Position(2399, 3753, 0), "Creation Minigame \\n \\n Multi: false \\n \\n KC Req: None\\n \\n Defend the Tree")


    ; //raids


    TeleInterfaceLocation(Position position, String description) {
        this.position = position;
        this.description = description;
    }

    private Position position;
    private String description;

}
