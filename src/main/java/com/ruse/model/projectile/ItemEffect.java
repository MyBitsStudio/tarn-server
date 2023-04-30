package com.ruse.model.projectile;

import com.ruse.model.Item;
import com.ruse.model.ItemRarity;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum ItemEffect {

    /* Common Effect */
    NOTHING(ItemRarity.COMMON),

    /* Uncommon Rarity Bonuses */
    //STRENGTH_DAMAGE(ItemRarity.UNCOMMON, 1000, 1000),
    RANGE_DAMAGE(ItemRarity.UNCOMMON, 1, 1), //slayer ticks
    MAGIC_DAMAGE(ItemRarity.UNCOMMON, 1, 1), //double xp

    /* Rare Rarity Bonuses */
    DROP_RATE_LOW(ItemRarity.RARE, 5, 20),
    DOUBLE_DROP(ItemRarity.RARE, 5, 20),
    ALL_DAMAGE_LOW(ItemRarity.RARE, 1, 1),

    /* Legendary Rarity Bonuses */
    DOUBLE_KILLS(ItemRarity.LEGENDARY, 1, 2),
    DOUBLE_CASH(ItemRarity.LEGENDARY, 1, 2),
    ALL_DAMAGE_MEDIUM(ItemRarity.LEGENDARY, 2, 2),
    AOE_EFFECT_2x2(ItemRarity.LEGENDARY, 1, 2),

    /* Mythic Rarity Bonuses */
    TRIPLE_KILLS(ItemRarity.MYTHIC, 1, 2),
    AOE_EFFECT(ItemRarity.MYTHIC, 1, 2),
    DROP_RATE_HIGH(ItemRarity.MYTHIC, 25, 99),
    ALL_DAMAGE_HIGH(ItemRarity.MYTHIC, 3, 3);

    ItemEffect(ItemRarity rarity) {
        this.rarity = rarity;
    }

    ItemEffect(ItemRarity rarity, int lowBonus, int highBonus) {
        this.rarity = rarity;
        this.lowBonus = lowBonus;
        this.highBonus = highBonus;
    }

    private ItemRarity rarity;
    private int lowBonus, highBonus;

    public int getLowBonus() {
        return this.lowBonus;
    }

    public int getHighBonus() {
        return this.highBonus;
    }

    public void setRarity(ItemRarity rarity) {
        this.rarity = rarity;
    }

    public ItemRarity getRarity() {
        return this.rarity;
    }

    public static boolean hasDoubleKills(Player player) {
        for (Item item : player.getEquipment().getItems()) {
            if(item == null)
                continue;
            ItemEffect effect = item.getEffect();
            if(effect == ItemEffect.DOUBLE_KILLS)
                return true;
        }
        return false;
    }

    public static boolean hasTripleKills(Player player) {
        if(player.getEquipment() == null){
            System.out.println("Player "+player.getUsername()+" equipment is null");
        } else if( player.getEquipment().getItems() == null){
            System.out.println("Player "+player.getUsername()+" equipment items is null");
        }
        for (Item item : player.getEquipment().getItems()) {
            if(item == null)
                continue;
            ItemEffect effect = item.getEffect();
            if(effect == ItemEffect.TRIPLE_KILLS)
                return true;
        }
        return false;
    }

    public static boolean hasAllPerks(Player player) {
        for (Item item : player.getEquipment().getItems()) {
            if(item == null)
                continue;
            ItemEffect effect = item.getEffect();
            if(effect == ItemEffect.TRIPLE_KILLS && effect == ItemEffect.DOUBLE_KILLS)
                return true;
        }
        return false;
    }

    public static boolean hasDoubleSlayer(Player player) {
        for (Item item : player.getEquipment().getItems()) {
            if(item == null)
                continue;
            ItemEffect effect = item.getEffect();
            if(effect == ItemEffect.RANGE_DAMAGE)
                return true;
        }
        return false;
    }
    public static boolean hasDoubleXp(Player player) {
        for (Item item : player.getEquipment().getItems()) {
            if(item == null)
                continue;
            ItemEffect effect = item.getEffect();
            if(effect == ItemEffect.MAGIC_DAMAGE)
                return true;
        }
        return false;
    }

    public static boolean hasDoubleCash(Player player) {
        for (Item item : player.getEquipment().getItems()) {
            if(item == null)
                continue;
            ItemEffect effect = item.getEffect();
            if(effect == ItemEffect.DOUBLE_CASH)
                return true;
        }
        return false;
    }

    private static List<Integer> noEffects;

    static {
        noEffects = Arrays.asList(995, 10835, 6, 8, 10, 2023, 14488, 19984, 19985, 19986, 20400, 19989, 19988, 19992, 19991, 14487, 20086, 20087, 20088, 20089, 20091, 20093, 20092, 18011,
                17999, 18001, 18003, 18005, 18009, 14490, 14492, 23126, 23123, 23120, 23165, 23166, 23167, 23168, 23169, 23170, 23057, 15003, 15002, 3578, 23058, 15004, 20489, 10946, 4278, 10947,
               13019, 4442, 20491, 23059, 22110, 19481, 19482, 23081, 19483, 19484, 19485, 19486, 19487, 19488, 19489, 19490, 19491, 19492, 19493, 19494, 19495, 20582, 20583, 20584,
                20585, 20586, 20587, 13650, 20588, 20589, 20590, 9084, 20602, 20603, 20604, 20605, 13774, 4073, 13775, 15357, 15355, 21816, 21815, 21814, 2736, 23020, 23060, 8788, 2734, 11137, 23174, 19624, 23002
        );
    }

    public static boolean hasNoEffect(int id) {
        return noEffects.stream().anyMatch(item -> item == id);
    }

    public static ItemEffect getEffectForName(String name) {
        return Arrays.stream(values())
                .filter(rarity1 -> rarity1.toString().equalsIgnoreCase(name))
                .findFirst().orElse(ItemEffect.NOTHING);
    }
}
