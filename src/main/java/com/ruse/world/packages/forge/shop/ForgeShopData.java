package com.ruse.world.packages.forge.shop;

import java.util.HashMap;

public class ForgeShopData {

    public static HashMap<Integer, Integer> PRICE_MAP = new HashMap<>();

    public static ForgeShopItem[][][] FORGE_SHOP_DATA = new ForgeShopItem[][][] {
            {
                    {       // tier 1 armoury

                            new ForgeShopItem(23226,10),
                            new ForgeShopItem(23227,15),
                            new ForgeShopItem(23228,20),
                            new ForgeShopItem(23229,25),
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
                            new ForgeShopItem(24003,200),//warden
                            new ForgeShopItem(24004,200),
                            new ForgeShopItem(24005,200),
                            new ForgeShopItem(24006,200),
                            new ForgeShopItem(24007,200),
                            new ForgeShopItem(24008,200),
                            new ForgeShopItem(24009,200),
                            new ForgeShopItem(23127,350),//mystical
                            new ForgeShopItem(23128,350),
                            new ForgeShopItem(23129,350),
                            new ForgeShopItem(23130,350),
                            new ForgeShopItem(23131,350),
                            new ForgeShopItem(22242,400),//asta
                            new ForgeShopItem(22244,400),
                            new ForgeShopItem(22246,400),
                            new ForgeShopItem(22250,400),
                            new ForgeShopItem(22248,400),
                            new ForgeShopItem(22252,400),
                            new ForgeShopItem(24010,200),//warden
                            new ForgeShopItem(22254,400),//asta

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
                            new ForgeShopItem(4442,50),
                            new ForgeShopItem(4440,125),
                            new ForgeShopItem(743,30),
                            new ForgeShopItem(13019,120),
                            new ForgeShopItem(3686,2000),
                            new ForgeShopItem(15003,30),
                            new ForgeShopItem(15002,100),
                            new ForgeShopItem(15004,250),
                            new ForgeShopItem(20491,600),
                            new ForgeShopItem(20490,900),
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
