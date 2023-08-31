package com.ruse.world.packages.forge.shop;

import java.util.HashMap;

public class ForgeShopData {

    public static HashMap<Integer, Integer> PRICE_MAP = new HashMap<>();

    public static ForgeShopItem[][][] FORGE_SHOP_DATA = new ForgeShopItem[][][] {
            {
                    {       // tier 1 armoury

                            new ForgeShopItem(23226,25),
                            new ForgeShopItem(23227,35),
                            new ForgeShopItem(23228,40),
                            new ForgeShopItem(23229,50),
                    },
                    {       // tier 2 armoury
                            new ForgeShopItem(8100,100),
                            new ForgeShopItem(8101,100),
                            new ForgeShopItem(8102,100),
                            new ForgeShopItem(8103,100),
                            new ForgeShopItem(8104,100),
                            new ForgeShopItem(8105,125),
                            new ForgeShopItem(8106,125),
                            new ForgeShopItem(8107,125),
                            new ForgeShopItem(8108,125),
                            new ForgeShopItem(8109,125),
                            new ForgeShopItem(8110,125),
                    },
                    {       // tier 3 armoury
                            new ForgeShopItem(19641,5000),
                            new ForgeShopItem(9076,500),

                    },
            },
            {
                    {       // tier 1 jewelry
                            new ForgeShopItem(4446,20),
                            new ForgeShopItem(19886,25)
                    },
                    {       // tier 2 jewelry
                            new ForgeShopItem(18888,80),
                            new ForgeShopItem(18823,60)
                    },
                    {       // tier 3 jewelry
                            new ForgeShopItem(17391,40),
                            new ForgeShopItem(1857,30)
                    },
            },
            {
                    {       // tier 1 other

                            new ForgeShopItem(21815,1)
                    },
                    {       // tier 2 other
                            new ForgeShopItem(21814,3)
                    },
                    {       // tier 3 other
                            new ForgeShopItem(4442,100)
                    },
            }
    };

    static {
        for (ForgeShopItem[][] forgeShopDatum : FORGE_SHOP_DATA) {
            for (ForgeShopItem[] forgeShopItems : forgeShopDatum) {
                for (ForgeShopItem forgeShopItem : forgeShopItems) {
                    PRICE_MAP.put(forgeShopItem.getItemId(), forgeShopItem.getPrice());
                }
            }
        }
    }
}
