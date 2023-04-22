package com.ruse.world.content;

import com.ruse.world.entity.impl.player.Player;

public enum NpcRequirements {
    //todo
    ORC(9027, 9837, 10),
    DEMON(9835, 9027, 20),
    WEREWOLF(9911, 9835, 30),
    CENTAUR(9922, 9911, 40),
    HOUND(9838, 9922, 50),
    SCORPION(9845, 9838, 60),
    RANGER(9910, 9845, 70),
    PALADIN(9807, 9910, 80),
    WYVERN(6692, 9807, 90),
    MYSTIC(9028, 500),
    NIGHTMARE(9029, 500),
    PATIENCE(9030, 500),
    ZINQRUX(8014, 1000),

    ABERRANT(8003, 8014, 200),
    INFERNO(202, 8003, 200),
    NAGENDRA(811, 202, 200),

    KOLGAL(9815, 811, 300), //300 nagendra
    YISDAR(9817, 9815, 300),
    IGTHAUR(9920, 9817, 300),
    ZERNATH(3831, 9920, 300),
    AVALON(9025, 3831, 300),

    ERAGON(9026, 9025, 500), //500 avalon
    DOOMWATCH(9836, 9026, 500),
    GUARDIAN(92, 9836, 500),
    MISCREATION(3313, 92, 500),
    AVATAR(8008, 3313, 500),
    ZORBAK(1906, 8008, 500),
    DEATHGOD(9915, 1906, 500),
    SLAYER(2342, 9915, 500),
    GOLEM(9024, 2342, 500),

    LUFFY(9916, 9024, 750), //750 golems
    BROLY(9918, 9916, 750),
    BOWSER(9919, 9918, 750),

    SASUKE(9914, 9919, 1000), //1k bowsers
    //bosses
    SANCTUM(9017, 9914, 1000),
    HYDRA(9839, 9017, 1000),
    GORVEK(9806, 9839, 1000),
    DRAGONITE(9816, 9806, 1000),

    ASMODEUS(9903, 9816, 2000), //2k dragonites
    MALVEK(8002, 9903, 2000),
    ONYX(1746, 8002, 2000),
    ZEIDAN(3010, 1746, 2000),
    AGTHOMOTH(3013, 3010, 2000),

    LILI(3014, 3013, 3000), //3k agthomoth
    GROUDON(8010, 3014, 3000),
    VARTH(3016, 8010, 3000),
    TYRANT(4972, 3016, 3000),
    LUCIFER(9012, 4972, 3000),
    VIRTUOS(3019, 9012, 3000),
    AGUMON(3020, 3019, 3000),

    WHITE(3021, 3020, 5000), //5k agumons
    PANTHER(3305, 3021, 5000),
    LEVI(9818, 3305, 5000),
    CALA(9912, 9818, 5000),
    SLENDER(9913, 9912, 5000),
    CHARYB(3117, 9913, 5000),
    SCYLLA(3115, 3117, 5000),
    EXODEN(12239, 3115, 5000),
    EZKEL(3112, 12239, 5000),
    JANEMBA(3011, 3112, 5000),
    FRIEZA(252, 3011, 5000),
    PERFECT(449, 252, 5000),
    SUPERBUU(452, 449, 5000),
    GOKU(187, 452, 5000),

    BYAKUYA(188, 187, 10000), //10k gokus
    FAZULA(1311, 188, 15000), //10k gokus
    YASUDA(1313, 1311, 15000), //10k gokus

    ;


    private int npcId;
    private int requireNpcId;
    private int amountRequired;
    private int killCount;

    NpcRequirements(int npcId, int requireNpcId, int amountRequired) {
        this.npcId = npcId;
        this.requireNpcId = requireNpcId;
        this.amountRequired = amountRequired;
        this.killCount = 0;
    }

    NpcRequirements(int npcId, int killCount) {
        this.npcId = npcId;
        this.killCount = killCount;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getKillCount() {
        return killCount;
    }

    public int getRequireNpcId() {
        return requireNpcId;
    }

    public int getAmountRequired() {
        return amountRequired;
    }

}