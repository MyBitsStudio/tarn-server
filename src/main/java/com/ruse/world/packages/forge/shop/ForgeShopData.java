package com.ruse.world.packages.forge.shop;

import java.util.HashMap;

public class ForgeShopData {

    public static HashMap<Integer, Integer> PRICE_MAP = new HashMap<>();

    public static ForgeShopItem[][][] FORGE_SHOP_DATA = new ForgeShopItem[][][] {
            {
                    {       // tier 1 armoury
                            //conqueuer
                            new ForgeShopItem(17614,75),
                            new ForgeShopItem(17616,75),
                            new ForgeShopItem(17618,75),
                            new ForgeShopItem(17606,75),
                            new ForgeShopItem(8411,100),
                            new ForgeShopItem(8410,100),
                            new ForgeShopItem(8412,100),
                            //joker
                            new ForgeShopItem(5068,100),
                            new ForgeShopItem(5069,100),
                            new ForgeShopItem(5070,100),
                            new ForgeShopItem(5071,100),
                            new ForgeShopItem(5072,100),
                            new ForgeShopItem(17718,200),
                            //shin
                            new ForgeShopItem(11328,100),
                            new ForgeShopItem(11329,100),
                            new ForgeShopItem(13330,100),
                            new ForgeShopItem(13332,100),
                            new ForgeShopItem(13333,200),
                            //char
                            new ForgeShopItem(8828,100),
                            new ForgeShopItem(8829,100),
                            new ForgeShopItem(8833,100),
                            new ForgeShopItem(8830,100),
                            new ForgeShopItem(8831,100),
                            new ForgeShopItem(22173,200)
                    },
                    {       // tier 2 armoury
                            //obisidian
                            new ForgeShopItem(22177,350),
                            new ForgeShopItem(23066,350),
                            new ForgeShopItem(23067,350),
                            new ForgeShopItem(23064,350),
                            new ForgeShopItem(23065,350),
                            new ForgeShopItem(22202,200),
                            new ForgeShopItem(22203,200),
                            new ForgeShopItem(23061,200),
                            new ForgeShopItem(23062,200),
                            new ForgeShopItem(23063,200),
                            new ForgeShopItem(23068,200),

                            //ocean
                            new ForgeShopItem(22179,400),
                            new ForgeShopItem(22180,400),
                            new ForgeShopItem(22181,400),
                            new ForgeShopItem(22182,400),
                            new ForgeShopItem(22183,400),
                            new ForgeShopItem(22184,600),

                            //white
                            new ForgeShopItem(22161,400),
                            new ForgeShopItem(22162,400),
                            new ForgeShopItem(22163,400),
                            new ForgeShopItem(22164,400),
                            new ForgeShopItem(22165,400),
                            new ForgeShopItem(22166,400),
                            new ForgeShopItem(22167,600),

                            //wind
                            new ForgeShopItem(21607,400),
                            new ForgeShopItem(21608,400),
                            new ForgeShopItem(21609,400),
                            new ForgeShopItem(21610,500),
                            new ForgeShopItem(21611,400),
                            new ForgeShopItem(21612,400),
                            new ForgeShopItem(22176,600)
                    },
                    {       // tier 3 armoury
                            new ForgeShopItem(22134,1250),
                            new ForgeShopItem(8109,1000),
                            new ForgeShopItem(8108,1000),
                            new ForgeShopItem(8107,1000),
                            new ForgeShopItem(8106,1000),
                            new ForgeShopItem(8105,1000),
                            new ForgeShopItem(8110,1250),
                            new ForgeShopItem(8104,1000),
                            new ForgeShopItem(8103,1000),
                            new ForgeShopItem(8102,1000),
                            new ForgeShopItem(8101,1000),
                            new ForgeShopItem(8100,1000),
                            new ForgeShopItem(14202,1000),
                            new ForgeShopItem(14204,1000),
                            new ForgeShopItem(14206,1000),
                            new ForgeShopItem(14301,1000),
                            new ForgeShopItem(14303,1000),
                            new ForgeShopItem(14305,1250),
                            new ForgeShopItem(14307,1250),

                    },
            },
            {
                    {       // tier 1 jewelry
                            new ForgeShopItem(14880,400),
                            new ForgeShopItem(15585,1500),
                            new ForgeShopItem(15586,1500),
                            new ForgeShopItem(15584,1500),
                    },
                    {       // tier 2 jewelry
                            new ForgeShopItem(15449,1500),
                            new ForgeShopItem(15448,1500),
                            new ForgeShopItem(23087,1500),
                    },
                    {       // tier 3 jewelry
                            new ForgeShopItem(18823,2100),
                            new ForgeShopItem(19888,2100),
                            new ForgeShopItem(15589,2100),
                            new ForgeShopItem(15588,2100),
                            new ForgeShopItem(15587,2100),
                            new ForgeShopItem(23088,4200)
                    },
            },
            {
                    {       // tier 1 other

                            new ForgeShopItem(21815,5),
                            new ForgeShopItem(21814,5),
                            new ForgeShopItem(23149,25)
                    },
                    {       // tier 2 other
                            new ForgeShopItem(21816,25),
                            new ForgeShopItem(23148,50)
                    },
                    {       // tier 3 other
                            new ForgeShopItem(19641,15000),
                            new ForgeShopItem(9076,8000),
                            new ForgeShopItem(9040,28000),
                            new ForgeShopItem(13748,28000),
                            new ForgeShopItem(13746,28000),
                            new ForgeShopItem(13750,28000),
                            new ForgeShopItem(13752,28000),
                            new ForgeShopItem(23147,100)
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
