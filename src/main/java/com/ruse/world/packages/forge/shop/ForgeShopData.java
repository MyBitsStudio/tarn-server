package com.ruse.world.packages.forge.shop;

import java.util.HashMap;

public class ForgeShopData {

    public static HashMap<Integer, Integer> PRICE_MAP = new HashMap<>();

    public static ForgeShopItem[][][] FORGE_SHOP_DATA = new ForgeShopItem[][][] {
            {
                    {       // tier 1 armoury
                            new ForgeShopItem(4151,1),
                            new ForgeShopItem(4151,1),
                            new ForgeShopItem(4151,1),
                            new ForgeShopItem(4151,1),
                    },
                    {       // tier 2 armoury
                            new ForgeShopItem(6585,1),
                            new ForgeShopItem(6585,1),
                            new ForgeShopItem(4151,1),
                            new ForgeShopItem(4151,1),
                    },
                    {       // tier 3 armoury
                            new ForgeShopItem(11732,1),
                            new ForgeShopItem(4151,1),
                            new ForgeShopItem(6585,1),
                            new ForgeShopItem(4151,1),
                    },
            },
            {
                    {       // tier 1 weapon
                            new ForgeShopItem(4712,1),
                            new ForgeShopItem(4712,1),
                            new ForgeShopItem(4712,1),
                            new ForgeShopItem(4712,1),
                    },
                    {       // tier 2 weapon
                            new ForgeShopItem(4714,1),
                            new ForgeShopItem(4714,1),
                            new ForgeShopItem(4714,1),
                            new ForgeShopItem(4714,1),
                    },
                    {       // tier 3 weapon
                            new ForgeShopItem(4716,1),
                            new ForgeShopItem(4716,1),
                            new ForgeShopItem(4716,1),
                            new ForgeShopItem(4716,1),
                    },
            },
            {
                    {       // tier 1 jewelry
                            new ForgeShopItem(4718,1),
                            new ForgeShopItem(4718,1),
                            new ForgeShopItem(4718,1),
                            new ForgeShopItem(4718,1),
                    },
                    {       // tier 2 jewelry
                            new ForgeShopItem(4720,1),
                            new ForgeShopItem(4720,1),
                            new ForgeShopItem(4720,1),
                            new ForgeShopItem(4720,1),
                    },
                    {       // tier 3 jewelry
                            new ForgeShopItem(4722,1),
                            new ForgeShopItem(4722,1),
                            new ForgeShopItem(4722,1),
                            new ForgeShopItem(4722,1),
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
