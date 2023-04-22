package com.ruse.world.content.item_upgrader;

import com.ruse.model.Item;

public enum UpgradeData {
    
    /*
     * Weapons
     */
    EXP_POTION_MORTAL(1, -18255, UpgradeType.WEAPON, 1027, 100, 15, 15, -1, new Item(732, 1), new Item(779, 1), new Item(942, 1)),
    DDR_POTION_MORTAL(1, -18254, UpgradeType.WEAPON, 1035, 100, 19, 28, -1, new Item(732, 1), new Item(785, 1), new Item(943, 1)),
    DR_POTION_MORTAL(1, -18253, UpgradeType.WEAPON, 3084, 100, 27, 33, -1, new Item(732, 1), new Item(791, 1), new Item(945, 1)),
    DAMAGE_POTION_MORTAL(1, -18252, UpgradeType.WEAPON, 3090, 100, 33, 40, -1, new Item(732, 1), new Item(797, 1), new Item(1005, 1)),
    EXP_POTION_GODLY(1, -18251, UpgradeType.WEAPON, 1031, 100, 48, 57, -1, new Item(732, 1), new Item(817, 1), new Item(1007, 1)),
    DDR_POTION_GODLY(1, -18250, UpgradeType.WEAPON, 3080, 100, 54, 69, -1, new Item(732, 1), new Item(914, 1), new Item(1009, 1)),
    DR_POTION_GODLY(1, -18249, UpgradeType.WEAPON, 3086, 100, 65, 72, -1, new Item(732, 1), new Item(917, 1), new Item(1011, 1)),
    EXP_POTION_DIVINE(1, -18248, UpgradeType.WEAPON, 1033, 100, 88, 74, -1, new Item(732, 1), new Item(920, 1), new Item(1013, 1), new Item(588, 20)),
    AGGRO_POTION_MORTAL(1, -18247, UpgradeType.WEAPON, 17546, 100, 112, 76, -1, new Item(732, 1), new Item(923, 1), new Item(1019, 1)),
    DDR_POTION_DIVINE(1, -18246, UpgradeType.WEAPON, 3082, 100, 142, 84, -1, new Item(732, 1), new Item(926, 1), new Item(1017, 1), new Item(588, 40)),
    DAMAGE_POTION_GODLY(1, -18245, UpgradeType.WEAPON, 3092, 100, 178, 86, -1, new Item(732, 1), new Item(929, 1), new Item(1015, 1)),
    DR_POTION_DIVINE(1, -18244, UpgradeType.WEAPON, 3088, 100, 314, 91, -1, new Item(732, 1), new Item(932, 1), new Item(1025, 1), new Item(588, 60)),
    AGGRO_POTION_GODLY(1, -18243, UpgradeType.WEAPON, 17544, 100, 560, 102, -1, new Item(732, 1), new Item(935, 1), new Item(1021, 1)),
    DAMAGE_POTION_DIVINE(1, -18242, UpgradeType.WEAPON, 3094, 100, 780, 105, -1, new Item(732, 1), new Item(938, 1), new Item(1023, 1), new Item(588, 80)),
    AGGRO_POTION_DIVINE(1, -18241, UpgradeType.WEAPON, 17542, 100, 1000, 118, -1, new Item(732, 1), new Item(941, 1), new Item(1029, 1), new Item(588, 100)),
    /*
     * Armor
     */
    LARITH_POTION(1, -18255, UpgradeType.ARMOR, 779, 100, 8, 1, -1, new Item(777, 1), new Item(6953, 1)),
    TARRAGON_POTION(1, -18254, UpgradeType.ARMOR, 785, 100, 12, 5, -1, new Item(783, 1), new Item(6953, 1)),
    CARDAMON_POTION(1, -18253, UpgradeType.ARMOR, 791, 100, 23, 11, -1, new Item(789, 1), new Item(6953, 1)),
    ROSOCELY_POTION(1, -18252, UpgradeType.ARMOR, 797, 100, 30, 20, -1, new Item(795, 1), new Item(6953, 1)),
    VERINISE_POTION(1, -18251, UpgradeType.ARMOR, 817, 100, 42, 25, -1, new Item(815, 1), new Item(6953, 1)),
    SEPHIALLA_POTION(1, -18250, UpgradeType.ARMOR, 914, 100, 51, 30, -1, new Item(913, 1), new Item(6953, 1)),
    FENNEL_POTION(1, -18249, UpgradeType.ARMOR, 917, 100, 61, 35, -1, new Item(916, 1), new Item(6953, 1)),
    RALORAGE_POTION(1, -18248, UpgradeType.ARMOR, 920, 100, 81, 40, -1, new Item(919, 1), new Item(6953, 1)),
    TURMERIC_POTION(1, -18247, UpgradeType.ARMOR, 923, 100, 100, 46, -1, new Item(922, 1), new Item(6953, 1)),
    MARJORAM_POTION(1, -18246, UpgradeType.ARMOR, 926, 100, 132, 52, -1, new Item(925, 1), new Item(6953, 1)),
    KUNNERREL_POTION(1, -18245, UpgradeType.ARMOR, 929, 100, 161, 60, -1, new Item(928, 1), new Item(6953, 1)),
    CARAWAY_POTION(1, -18244, UpgradeType.ARMOR, 932, 100, 218, 72, -1, new Item(931, 1), new Item(6953, 1)),
    USTROVE_POTION(1, -18243, UpgradeType.ARMOR, 935, 100, 412, 86, -1, new Item(934, 1), new Item(6953, 1)),
    REXETH_POTION(1, -18242, UpgradeType.ARMOR, 938, 100, 604, 97, -1, new Item(937, 1), new Item(6953, 1)),
    CREEPER_POTION(1, -18241, UpgradeType.ARMOR, 941, 100, 832, 114, -1, new Item(940, 1), new Item(6953, 1)),
    /*
     * Tools
     */
    HOLY_WATER(1, -18255, UpgradeType.TOOL, 732, 100, 1, 1, -1, new Item(731, 1), new Item(13754, 1), new Item(8212, 100)),

   // DARK_ATTACHMENT(1, -18254, UpgradeType.TOOL, 22112, 100, false, 0, -1,
    //        new Item(10949, 1), new Item(10835, 300000)),

    ;

    private final UpgradeType type;

    public final int buttonId;
    
    public final int clickId;
    
    private int resultItem;
    
    private float successRate;
    
    private int otherCurrency;
    
    private int currencyAmount; //LEVEL REQUIRED
    
    private Item[] ingredients;
    
    private int safeItem;
    
    
    
    UpgradeData(int buttonId, int clickId, UpgradeType type, int resultItem, float successRate, int otherCurrency, int currencyAmount, int safeItem, Item... ingredients) {
        this.buttonId = buttonId;
        this.clickId = clickId;
        this.type = type;
        this.resultItem = resultItem;
        this.successRate = successRate;
        this.otherCurrency = otherCurrency;
        this.currencyAmount = currencyAmount; //LEVEL REQUIRED
        this.ingredients = ingredients;
        this.safeItem = safeItem;
    }

    
    public int getButtonId() {
        return buttonId;
    }
    
    public int getClickId() {
        return clickId;
    }
    
    public UpgradeType getType() {
        return type;
    }
    
    public int getResultItem() {
        return resultItem;
    }
    
    public float getSuccessRate() {
        return successRate;
    }
    
    public int getOtherCurrency() {
        return otherCurrency;
    }
    
    public int getCurrencyAmount() {
        return currencyAmount;
    }
    
    public Item[] getIngredients() {
        return ingredients;
    }
    
    public int getSafeItem() {
        return safeItem;
    }
}