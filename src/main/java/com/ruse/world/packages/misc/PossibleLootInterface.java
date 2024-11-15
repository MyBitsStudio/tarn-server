package com.ruse.world.packages.misc;

import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.packages.packs.casket.Box;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.casket.packs.*;
import com.ruse.world.packages.packs.casket.packs.limited.HalloweenCracker;
import com.ruse.world.packages.packs.casket.packs.limited.LimitedMaster1;

import java.util.ArrayList;

public class PossibleLootInterface {

    public static void openInterface(Player player, LootData data) {
        player.getPacketSender().sendInterface(101000);

        int stringStart = 101261;
        for (LootData loot : LootData.values()) {
            player.getPacketSender().sendItemOnInterface(stringStart++, loot.getItemId(), 1);
            String name = loot.name != null ? loot.name : "          " + ItemDefinition.forId(loot.getItemId()).getName();
            player.getPacketSender().sendString(stringStart++, (loot == data ? "@whi@" : "") + name);
        }

        player.getPacketSender().setScrollBar(101250, LootData.values().length * 40);

        int index = 101101;
        int i = 0;
        for (Item item : data.getLoot()) {
            player.getPacketSender().sendItemOnInterface(index++, item.getId(), item.getAmount());
            i++;
        }
        int rows = (i / 7) + 1;
        if (rows <= 6)
            rows = 6;
        player.getPacketSender().setScrollBar(101100, 2 + (rows * 35));

        for (int z = i; z < (Math.max(rows * 7, 42)); z++) {
            player.getPacketSender().sendItemOnInterface(index++, -1, 1);
        }
    }

    public static boolean handleButton(Player player, int buttonID) {
        if (buttonID >= 101262 && buttonID <= 101312) {
            int index = (buttonID - 101262) / 2;

            if (index <= LootData.values().length)
                openInterface(player, LootData.values()[index]);

            return true;
        }
        return false;
    }

    public enum LootData {

        STARTER_I(15004, StarterIBox.loot),
        STARTER_II(23300, StarterIIBox.loot),
        STARTER_III(23301, StarterIIIBox.loot),

        ENHANCE_1(20500, new Item[]{new Item(13727, 125), new Item(2380, 3), new Item(2381, 3)}),
        ENHANCE_2(20501, new Item[]{new Item(13727, 460), new Item(2380, 6), new Item(2381, 6), new Item(2382, 6)}),
        ENHANCE_3(20502, new Item[]{new Item(13727, 825), new Item(2380, 9), new Item(2381, 9), new Item(2382, 9)}),

        CERT_PACK_I(23250, new Item[]{new Item(22214), new Item(22215), new Item(22216), new Item(22217)}),
        CERT_PACK_II(23251, new Item[]{new Item(22218), new Item(22219), new Item(22220), new Item(22221), new Item(22222)}),
        CERT_PACK_III(23252, new Item[]{new Item(22223), new Item(22224), new Item(22225), new Item(22226), new Item(22227), new Item(22228)}),

        TICKET_PACK_I(23253, new Item[]{ new Item(21814, 5), new Item(21815, 5)}),
        TICKET_PACK_II(23254, new Item[]{ new Item(21814, 25), new Item(21815, 25)}),
        TICKET_PACK_III(23255, new Item[]{ new Item(21814, 25), new Item(21815, 25), new Item(21816, 10)}),

        DONATOR_BOX_I(23256, DonatorBoxI.loot),
        DONATOR_BOX_II(23257, DonatorBoxII.loot),
        DONATOR_BOX_III(23258, DonatorBoxIII.loot),
        ULTIMATE_DONATOR(23259, UltimateDonatorBox.loot),

        LIMITED_MASTERS(23260, LimitedMaster1.loot),
        VOTE_BOX(18768, VoteBox.loot),
        SLAYER_CASKET(2734, SlayerCasket.loot),

        PvM_CASKET(2736, PvMCasket.loot),

        H_CRACKER(20083, HalloweenCracker.loot),


        //SLAYER_CASKET(2734, SlayerCasket.loot),
        //ELITE_SLAYER_CHEST(18647, EliteChest.loot22),
        //HALLS_OF_VALOR("          Halls of Valor", 23094, HallsOfValor.loot),
        //TREASURE_HUNTER("          Treasure Hunter", 23049, TreasureHunter.loot),
        //VOID_OF_DARKNESS("          Void of Darkness", 23033, VoidOfDarkness.loot),
        //KEEPERS_OF_LIGHT("          Keepers of Light", 23135, KeepersOfLight.loot),
        //VAULT_OF_WAR("          Vault of War", 23102, VaultOfWar.loot),
        //ELITE_RAIDS("          Elite Raids", 7014, ZombieRaids.loot),
        //AURA_RAIDS("          Aura Raids", 23049, AuraRaids.loot),


//        SILVER_BOX(15003, SilverBox.loot),
//        RUBY_BOX(15002, RubyBox.loot),
//        DIAMOND_BOX(15004, DiamondBox.loot),
//        PREMIUM_BOX(20489, PremiumBox.loot),
//        ONYX_BOX(20491, OnyxBox.loot),
//        ZENYTE_BOX(20490, ZenyteBox.loot),
        //SUPREME_BOX(20488, SupremeBox.loot),
        //RAIDS_BOX(18404, RaidsBox.loot),
        //ELITE_BOX(19117, EliteBox.loot),
        //RARE_BOX(23171, RareBox.loot),
        //OFF_BOX(23173, OffBox.loot),
        //DEF_BOX(23172, DefBox.loot),
  /* SUPER(19116, Super.common,Super.uncommon,Super.rare),
        EXTREME(19115, Extreme.common,Extreme.uncommon,Extreme.rare),
        GRAND(19114, Grand.common,Grand.uncommon,Grand.rare),
        OPchest(20488, OP.common,OP.uncommon,OP.rare),
        LAUNCH(20489, Launch.common,Launch.uncommon,Launch.rare),
        SILVER(15003, Ruby.common,Ruby.uncommon,Ruby.rare),
        RUBY(15002, Silver.common,Silver.uncommon,Silver.rare),
        DIAMOND(15004, Diamond.common,Diamond.uncommon,Diamond.rare),

        OCAPE(3578, Ocape.rare),
        RAIDS_ONE(13591,Raids1.common,Raids1.uncommon,Raids1.rare1),
        RAIDS_TWO_BOX(18404,Raids2.common,Raids2.uncommon,Raids2.rare),
        DRAGONBALLBOX(18768,  DragonballBox.common1,  DragonballBox.uncommon1,  DragonballBox.rare1),
        SLAYERBOX( 7120, SlayerBox.commonpro2,  SlayerBox.uncommonpro2,  SlayerBox.rarepro2),
        PROGRESSIVEBOX(10025,  ProgressiveBox.commonpro,  ProgressiveBox.uncommonpro,  ProgressiveBox.rarepro),
        PVMMBOX(PVMBox.ITEM_ID, PVMBox.commonpvm, PVMBox.uncommonpvm, PVMBox.rarepvm),
        MBOX(6199,MBox.common,MBox.uncommon,MBox.rare),
        FPK_SOLDIER(-1, FPK.LOOT, "Youtube Soldier"),
        RAIDS_TWO(-1, ZombieRaidLoot.LOOT, "Raids [2]"),
        AFK1(-1, Stalls.loot1, "AFK Stall (1)"),
        AFK2(-1, Stalls.loot2, "AFK Stall (2)"),
        AFK3(-1, Stalls.loot3, "AFK Stall (3)"),
        AFK4(-1, Stalls.loot4, "Zenyte AFK Stall"),*/
        ;

        private int itemId;
        private String name;
        private Item[] loot;


        LootData(int itemId, Item[] C, String name) {
            this.itemId = itemId;
            this.loot = new Item[C.length];
            this.name = name;
            int i = 0;
            for (Item d : C) {
                this.loot[i++] = new Item(d.getId(), d.getAmount());
            }
        }

        LootData(String name, int itemId, Box[] loot) {
            this.itemId = itemId;
            this.loot = new Item[loot.length];
            this.name = name;
            int i = 0;
            for (Box d : loot) {
                this.loot[i++] = new Item(d.getId(), d.getMax());
            }
        }


        LootData(int itemId, Box[] loot) {
            this.itemId = itemId;
            this.loot = new Item[loot.length];
            int i = 0;
            for (Box d : loot) {
                this.loot[i++] = new Item(d.getId(), d.getMax());
            }
        }


        LootData(int itemId, ArrayList<Item> loot) {
            this.itemId = itemId;
            this.loot = Misc.convertItems(loot);
        }

        LootData(int itemId, int[]... items) {
            this.itemId = itemId;

            ArrayList<Item> loot = new ArrayList<>();
            for (int i = 0; i < items.length; i++) {
                for (int z = 0; z < items[i].length; z++) {
                    loot.add(new Item(items[i][z]));
                }
            }
            this.loot = Misc.convertItems(loot);
        }
        /*LootData(int itemId, int[]... items) {
            this.itemId = itemId;

            ArrayList<Item> loot = new ArrayList<>();
                for (int z = 0; z < items[0].length; z++) {
                    loot.add(new Item(items[0][z]));
            }

            this.loot = Misc.convertItems(loot);
        }*/

        LootData(int itemId, Item[]... items) {
            this.itemId = itemId;

            ArrayList<Item> loot = new ArrayList<>();
            for (int i = 0; i < items.length; i++) {
                for (int z = 0; z < items[i].length; z++) {
                    loot.add(items[i][z]);
                }
            }
            this.loot = Misc.convertItems(loot);
        }

        public int getItemId() {
            return itemId;
        }

        public Item[] getLoot() {
            return loot;
        }

    }

}