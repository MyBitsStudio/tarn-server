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
                            new ForgeShopItem(24003,400),//warden
                            new ForgeShopItem(24004,400),
                            new ForgeShopItem(24005,400),
                            new ForgeShopItem(24006,400),
                            new ForgeShopItem(24007,400),
                            new ForgeShopItem(24008,400),
                            new ForgeShopItem(24009,400),
                            new ForgeShopItem(23127,600),//mystical
                            new ForgeShopItem(23128,600),
                            new ForgeShopItem(23129,600),
                            new ForgeShopItem(23130,600),
                            new ForgeShopItem(23131,600),
                            new ForgeShopItem(22242,800),//asta
                            new ForgeShopItem(22244,800),
                            new ForgeShopItem(22246,800),
                            new ForgeShopItem(22250,800),
                            new ForgeShopItem(22248,800),
                            new ForgeShopItem(22252,800),
                            new ForgeShopItem(24010,400),//warden
                            new ForgeShopItem(22254,600),//asta

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

                            new ForgeShopItem(21815,1),
                            new ForgeShopItem(9084,15),
                            new ForgeShopItem(2380,5),
                            new ForgeShopItem(741,10),
                            new ForgeShopItem(23219,30),
                            new ForgeShopItem(8788,3),
                    },
                    {       // tier 2 other
                            new ForgeShopItem(21814,3),
                            new ForgeShopItem(21816,10),
                            new ForgeShopItem(23178,40),
                            new ForgeShopItem(22110,60),
                            new ForgeShopItem(13650,2),
                            new ForgeShopItem(742,15),
                    },
                    {       // tier 3 other
                            new ForgeShopItem(4442,100),
                            new ForgeShopItem(4440,250),
                            new ForgeShopItem(743,120),
                            new ForgeShopItem(13019,500),
                            new ForgeShopItem(3686,5000),
                            new ForgeShopItem(15003,50),
                            new ForgeShopItem(15002,250),
                            new ForgeShopItem(15004,600),
                            new ForgeShopItem(20491,1100),
                            new ForgeShopItem(20490,1900),
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
