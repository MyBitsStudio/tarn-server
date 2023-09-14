package com.ruse.world.content.teleport;

import com.ruse.model.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum TeleInterfaceData {

    /* Monsters */
    MONSTER0(TeleInterfaceCategory.MONSTERS, "Blurite Goblin", 9837, 500, TeleInterfaceLocation.LOCATION1, null),
    MONSTER1(TeleInterfaceCategory.MONSTERS, "Blurite Orc", 9027, 1250, TeleInterfaceLocation.LOCATION2, null),
    MONSTER2(TeleInterfaceCategory.MONSTERS, "Blurite Demon", 9835, 1250, TeleInterfaceLocation.LOCATION3, null),
    MONSTER3(TeleInterfaceCategory.MONSTERS, "Blurite Werewolf", 9911, 1150, TeleInterfaceLocation.LOCATION4, null),
    MONSTER4(TeleInterfaceCategory.MONSTERS, "Blurite Centaur", 9922, 1150, TeleInterfaceLocation.LOCATION5, null),
    MONSTER5(TeleInterfaceCategory.MONSTERS, "Zinqrux", 8014, 850, TeleInterfaceLocation.LOCATION6, null),
    MONSTER6(TeleInterfaceCategory.MONSTERS, "Dr. Aberrant", 8003, 650, TeleInterfaceLocation.LOCATION7, null),
    MONSTER7(TeleInterfaceCategory.MONSTERS, "Nagendra", 811, 850, TeleInterfaceLocation.LOCATION8, null),
    MONSTER8(TeleInterfaceCategory.MONSTERS, "Yisdar", 9817, 1500, TeleInterfaceLocation.LOCATION9, null),
    MONSTER9(TeleInterfaceCategory.MONSTERS, "Doomwatcher", 9836, 1500, TeleInterfaceLocation.LOCATION10, null),
    MONSTER10(TeleInterfaceCategory.MONSTERS, "Maze Guardian", 92, 1500, TeleInterfaceLocation.LOCATION11, null),
    MONSTER11(TeleInterfaceCategory.MONSTERS, "Miscreation", 3313, 1500, TeleInterfaceLocation.LOCATION12, null),
    MONSTER12(TeleInterfaceCategory.MONSTERS, "Zorbak", 1906, 350, TeleInterfaceLocation.LOCATION13, null),
    MONSTER13(TeleInterfaceCategory.MONSTERS, "Nature Unicorn", 1742, 750, TeleInterfaceLocation.LOCATION14, null),
    MONSTER14(TeleInterfaceCategory.MONSTERS, "Hyndra", 1743, 750, TeleInterfaceLocation.LOCATION15, null),
    MONSTER15(TeleInterfaceCategory.MONSTERS, "Grooter", 1744, 850, TeleInterfaceLocation.LOCATION16, null),
    MONSTER16(TeleInterfaceCategory.MONSTERS, "Runite Turtle", 1745, 850, TeleInterfaceLocation.LOCATION17, null),
    MONSTER17(TeleInterfaceCategory.MONSTERS, "Elite Toad", 1738, 850, TeleInterfaceLocation.LOCATION18, null),
    MONSTER18(TeleInterfaceCategory.MONSTERS, "Blast Cloud", 1739, 850, TeleInterfaceLocation.LOCATION19, null),
    MONSTER19(TeleInterfaceCategory.MONSTERS, "Elemental Moss", 1740, 850, TeleInterfaceLocation.LOCATION20, null),
    MONSTER20(TeleInterfaceCategory.MONSTERS, "Elemental Fire", 1741, 850, TeleInterfaceLocation.LOCATION21, null),

//    MONSTER10(TeleInterfaceCategory.MONSTERS, "Mystic", 9028, 600, TeleInterfaceLocation.LOCATION11, null),
//    MONSTER11(TeleInterfaceCategory.MONSTERS, "Nightmare", 9029, 600, TeleInterfaceLocation.LOCATION12, null),
//    MONSTER12(TeleInterfaceCategory.MONSTERS, "Patience", 9030, 600, TeleInterfaceLocation.LOCATION13, null),

    //MONSTER16(TeleInterfaceCategory.MONSTERS, "Inferno", 202, 850, TeleInterfaceLocation.LOCATION16, null),

    //MONSTER18(TeleInterfaceCategory.MONSTERS, "Kol'gal", 9815, 1500, TeleInterfaceLocation.LOCATION18, null),

   // MONSTER21(TeleInterfaceCategory.MONSTERS, "Zernath", 3831, 1200, TeleInterfaceLocation.LOCATION21, null),
   // MONSTER25(TeleInterfaceCategory.MONSTERS, "Death God", 9915, 750, TeleInterfaceLocation.LOCATION29, null),
    //MONSTER27(TeleInterfaceCategory.MONSTERS, "Golden Golem", 9024, 1250, TeleInterfaceLocation.LOCATION31, null),
    //MONSTER28(TeleInterfaceCategory.MONSTERS, "Luffy", 9916, 750, TeleInterfaceLocation.LOCATION32, null),
    //MONSTER29(TeleInterfaceCategory.MONSTERS, "Broly", 9918, 500, TeleInterfaceLocation.LOCATION33, null),
    //MONSTER30(TeleInterfaceCategory.MONSTERS, "Bowser", 9919, 1000, TeleInterfaceLocation.LOCATION34, null),
   // MONSTER24(TeleInterfaceCategory.MONSTERS, "Sasuke", 9914, 750, TeleInterfaceLocation.LOCATION35, null),

    //MONSTER15(TeleInterfaceCategory.MONSTERS, "Vol'goth", 9917, 1500, TeleInterfaceLocation.LOCATION1, new Item[] {new Item(4151,1)}),

    /* Bosses */
    BOSS1(TeleInterfaceCategory.BOSSES, "Avalon", 9025, 1050, TeleInterfaceLocation.LOCATION36, null),
    BOSS2(TeleInterfaceCategory.BOSSES, "Eragon", 9026, 1050, TeleInterfaceLocation.LOCATION37, null),
    BOSS3(TeleInterfaceCategory.BOSSES, "Avatar Titan", 8008, 1250, TeleInterfaceLocation.LOCATION38, null),
    BOSS4(TeleInterfaceCategory.BOSSES, "Emerald Slayer", 2342, 1000, TeleInterfaceLocation.LOCATION39, null),
    BOSS5(TeleInterfaceCategory.BOSSES, "Mutant Hydra", 9839, 1650, TeleInterfaceLocation.LOCATION40, null),
    BOSS6(TeleInterfaceCategory.BOSSES, "Gorvek", 9806, 1950, TeleInterfaceLocation.LOCATION41, null),
    BOSS7(TeleInterfaceCategory.BOSSES, "Onyx Griffin", 1746, 1500, TeleInterfaceLocation.LOCATION42, null),
    BOSS8(TeleInterfaceCategory.BOSSES, "Tyrant Lord", 4972, 1500, TeleInterfaceLocation.LOCATION43, null),
    BOSS9(TeleInterfaceCategory.BOSSES, "White Beard", 3021, 1500, TeleInterfaceLocation.LOCATION44, null),
    BOSS10(TeleInterfaceCategory.BOSSES, "Panther", 3305, 1500, TeleInterfaceLocation.LOCATION45, null),
    BOSS11(TeleInterfaceCategory.BOSSES, "The Warrior", 125, 1500, TeleInterfaceLocation.LOCATION46, null),

    //BOSS1(TeleInterfaceCategory.BOSSES, "Sanctum Golem", 9017, 1950, TeleInterfaceLocation.LOCATION36, null),

    //BOSS7(TeleInterfaceCategory.BOSSES, "Dragonite", 9816, 2050, TeleInterfaceLocation.LOCATION39, null),
    //BOSS9(TeleInterfaceCategory.BOSSES, "Asmodeus", 9903, 1450, TeleInterfaceLocation.LOCATION40, null),
   // BOSS10(TeleInterfaceCategory.BOSSES, "Malvek", 8002, 860, TeleInterfaceLocation.LOCATION41, null),

    //BOSS12(TeleInterfaceCategory.BOSSES, "Zeidan Grimm", 3010, 1250, TeleInterfaceLocation.LOCATION43, null),
   // BOSS15(TeleInterfaceCategory.BOSSES, "Ag'thomoth", 3013, 1750, TeleInterfaceLocation.LOCATION44, null),
    //BOSS16(TeleInterfaceCategory.BOSSES, "Lilinryss", 3014, 950, TeleInterfaceLocation.LOCATION45, null),
    //BOSS17(TeleInterfaceCategory.BOSSES, "Groudon", 8010, 1000, TeleInterfaceLocation.LOCATION46, null),
    //BOSS18(TeleInterfaceCategory.BOSSES, "Varthramoth", 3016, 2300, TeleInterfaceLocation.LOCATION47, null),

    //BOSS20(TeleInterfaceCategory.BOSSES, "Lucifer", 9012, 1000, TeleInterfaceLocation.LOCATION49, null),
    //BOSS21(TeleInterfaceCategory.BOSSES, "Virtuoso", 3019, 1000, TeleInterfaceLocation.LOCATION50, null),
    //BOSS22(TeleInterfaceCategory.BOSSES, "Agumon", 3020, 650, TeleInterfaceLocation.LOCATION51, null),
//    BOSS23(TeleInterfaceCategory.BOSSES, "White Beard", 3021, 650, TeleInterfaceLocation.LOCATION52, null),
//    BOSS24(TeleInterfaceCategory.BOSSES, "Panther", 3305, 1100, TeleInterfaceLocation.LOCATION53, null),
//    MONSTER15(TeleInterfaceCategory.BOSSES, "Leviathan", 9818, 1250, TeleInterfaceLocation.LOCATION54, null),
//    MONSTER31(TeleInterfaceCategory.BOSSES, "Calamity", 9912, 1250, TeleInterfaceLocation.LOCATION55, null),
//    //MONSTER32(TeleInterfaceCategory.BOSSES, "Slender Man", 9913, 1250, TeleInterfaceLocation.LOCATION56, null),
//    MONSTER34(TeleInterfaceCategory.BOSSES, "Charybdis", 3117, 850, TeleInterfaceLocation.LOCATION57, null),
//    MONSTER35(TeleInterfaceCategory.BOSSES, "Scylla", 3115, 1250, TeleInterfaceLocation.LOCATION58, null),
//   // MONSTER36(TeleInterfaceCategory.BOSSES, "Exoden", 12239, 1400, TeleInterfaceLocation.LOCATION59, null),
//    //BOSS31(TeleInterfaceCategory.BOSSES, "Ezkel-Nojad", 3112, 1900, TeleInterfaceLocation.LOCATION60, null),
//   // BOSS13(TeleInterfaceCategory.BOSSES, "Janemba", 3011, 750, TeleInterfaceLocation.LOCATION61, null),
//   // BOSS14(TeleInterfaceCategory.BOSSES, "Frieza", 252, 850, TeleInterfaceLocation.LOCATION62, null),
//   // BOSS4(TeleInterfaceCategory.BOSSES, "Perfect Cell", 449, 750, TeleInterfaceLocation.LOCATION63, null),
//  //  BOSS5(TeleInterfaceCategory.BOSSES, "Super Buu", 452, 850, TeleInterfaceLocation.LOCATION64, null),
//   // BOSS2(TeleInterfaceCategory.BOSSES, "Goku", 187, 750, TeleInterfaceLocation.LOCATION65, null),
//    BOSS35(TeleInterfaceCategory.BOSSES, "Byakuya", 188, 650, TeleInterfaceLocation.LOCATION66, null),
    //BOSS366(TeleInterfaceCategory.BOSSES, "Queen Fazula", 1311, 750, TeleInterfaceLocation.LOCATION71, null),
    //BOSS367(TeleInterfaceCategory.BOSSES, "Lord Yasuda", 1313, 750, TeleInterfaceLocation.LOCATION72, null),
    //BOSS368(TeleInterfaceCategory.BOSSES, "Black Goku", 1318, 750, TeleInterfaceLocation.LOCATION73, null),
   // BOSS_FINAL(TeleInterfaceCategory.BOSSES, "Final Boss", 595, 750, TeleInterfaceLocation.LOCATION80, null),
    //BOSS33(TeleInterfaceCategory.BOSSES, "Boss", 5511, 1500, TeleInterfaceLocation.LOCATION1, new Item[] {new Item(4151, 10), new Item(995, 2000000)}),
    //BOSS25(TeleInterfaceCategory.BOSSES, "Boss", 8005, 1500, TeleInterfaceLocation.LOCATION1, new Item[] {new Item(4151, 10), new Item(995, 2000000)}),
    //BOSS26(TeleInterfaceCategory.BOSSES, "Boss", 8006, 1500, TeleInterfaceLocation.LOCATION1, new Item[] {new Item(4151, 10), new Item(995, 2000000)}),
    //BOSS29(TeleInterfaceCategory.BOSSES, "Boss", 1898, 1500, TeleInterfaceLocation.LOCATION1, new Item[] {new Item(4151, 10), new Item(995, 2000000)}),

    /* Minigames */
    TARNTOWER(TeleInterfaceCategory.MINIGAMES, "Tarn Tower", 9910, 1500, TeleInterfaceLocation.TARN_TOWER, new Item(995,1)),

    /* Misc */
   //VEIGAR(TeleInterfaceCategory.MISC, "DISABLED", 9906, 2000, TeleInterfaceLocation.LOCATION67, null),
    //NINETAILS(TeleInterfaceCategory.MISC, "Nine Tails Jinchuriki", 9904, 1500, TeleInterfaceLocation.LOCATION68, null),
   // MERUEM(TeleInterfaceCategory.MISC, "Meruem The King", 9907, 1500, TeleInterfaceLocation.LOCATION69, null),
   // GOLDEN(TeleInterfaceCategory.MISC, "Golden Great Ape", 9908, 1500, TeleInterfaceLocation.LOCATION70, null)

    RAIDS_1(TeleInterfaceCategory.MISC, "DISABLED", 9910, 1500, TeleInterfaceLocation.TARN_TOWER, new Item(995,1)),
    ;
    TeleInterfaceData(TeleInterfaceCategory category, String name, int npcId, int zoom, TeleInterfaceLocation location, Item... drops) {
        this.category = category;
        this.name = name;
        this.npcId = npcId;
        this.zoom = zoom;
        this.location = location;
        this.drops = drops;
    }
    private TeleInterfaceCategory category;
    private TeleInterfaceLocation location;
    private String name;
    private int npcId, zoom;
    private Item[] drops;
    public TeleInterfaceCategory getCategory() {
        return this.category;
    }
    public TeleInterfaceLocation getLocation() {
        return this.location;
    }
    public String getName() {
        return this.name;
    }
    public int getNpcId() {
        return this.npcId;
    }
    public int getZoom() {
        return this.zoom;
    }
    public Item[] getDrops() {
        return this.drops;
    }

    public void setDrops(Item[] drops) {
        this.drops = drops;
    }
    public static TeleInterfaceData @NotNull [] forCategory(TeleInterfaceCategory category) {
        List<TeleInterfaceData> tele = new ArrayList<TeleInterfaceData>();
        for(TeleInterfaceData data : TeleInterfaceData.values()) {
            if(data == null)
                continue;
            if(data.getCategory() != category)
                continue;
            tele.add(data);
        }
        return tele.toArray(new TeleInterfaceData[tele.size()]);
    }
}