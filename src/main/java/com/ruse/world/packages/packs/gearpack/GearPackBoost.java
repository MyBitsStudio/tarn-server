package com.ruse.world.packages.packs.gearpack;

import com.ruse.model.container.impl.Equipment;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GearPackBoost {

    T1_WEAPON(1, Equipment.WEAPON_SLOT, 2, "AOE_EFFECT"),
    T1_HEAD(1, Equipment.HEAD_SLOT, 95, "DROP_RATE_HIGH"),
    T1_BODY(1, Equipment.BODY_SLOT, 95, "DROP_RATE_HIGH"),
    T1_LEGS(1, Equipment.LEG_SLOT, 95, "DROP_RATE_HIGH"),
    T1_HANDS(1, Equipment.HANDS_SLOT, 95, "DROP_RATE_HIGH"),
    T1_FEET(1, Equipment.FEET_SLOT, 95, "DROP_RATE_HIGH"),
    T1_CAPE(1, Equipment.CAPE_SLOT, 95, "DROP_RATE_HIGH"),
    T1_AMULET(1, Equipment.AMULET_SLOT, 95, "DROP_RATE_HIGH"),
    T1_RING(1, Equipment.RING_SLOT, 95, "DROP_RATE_HIGH"),
    T1_ARROWS(1, Equipment.AMMUNITION_SLOT, 95, "DROP_RATE_HIGH"),
    T1_AURA(1, Equipment.AURA_SLOT, 95, "DROP_RATE_HIGH"),
    T1_HALO(1, Equipment.HALO_SLOT, 95, "DROP_RATE_HIGH"),
    T1_GEMSTONE(1, Equipment.GEMSTONE_SLOT, 95, "DROP_RATE_HIGH"),
    T1_SHIELD(1, Equipment.SHIELD_SLOT, 95, "DROP_RATE_HIGH"),

    T2_WEAPON(2, Equipment.WEAPON_SLOT, 1, "AOE_EFFECT"),
    T2_HEAD(2, Equipment.HEAD_SLOT, 50, "DROP_RATE_HIGH"),
    T2_BODY(2, Equipment.BODY_SLOT, 50, "DROP_RATE_HIGH"),
    T2_LEGS(2, Equipment.LEG_SLOT, 50, "DROP_RATE_HIGH"),
    T2_HANDS(2, Equipment.HANDS_SLOT, 50, "DROP_RATE_HIGH"),
    T2_FEET(2, Equipment.FEET_SLOT, 50, "DROP_RATE_HIGH"),
    T2_CAPE(2, Equipment.CAPE_SLOT, 50, "DROP_RATE_HIGH"),
    T2_AMULET(2, Equipment.AMULET_SLOT, 50, "DROP_RATE_HIGH"),
    T2_RING(2, Equipment.RING_SLOT, 50, "DROP_RATE_HIGH"),
    T2_ARROWS(2, Equipment.AMMUNITION_SLOT, 50, "DROP_RATE_HIGH"),
    T2_AURA(2, Equipment.AURA_SLOT, 50, "DROP_RATE_HIGH"),
    T2_HALO(2, Equipment.HALO_SLOT, 50, "DROP_RATE_HIGH"),
    T2_GEMSTONE(2, Equipment.GEMSTONE_SLOT, 50, "DROP_RATE_HIGH"),
    T2_SHIELD(2, Equipment.SHIELD_SLOT, 50, "DROP_RATE_HIGH"),

    T3_WEAPON(3, Equipment.WEAPON_SLOT, 2, "TRIPLE_KILLS"),
    T3_HEAD(3, Equipment.HEAD_SLOT, 25, "DROP_RATE_HIGH"),
    T3_BODY(3, Equipment.BODY_SLOT, 25, "DROP_RATE_HIGH"),
    T3_LEGS(3, Equipment.LEG_SLOT, 25, "DROP_RATE_HIGH"),
    T3_HANDS(3, Equipment.HANDS_SLOT, 25, "DROP_RATE_HIGH"),
    T3_FEET(3, Equipment.FEET_SLOT, 25, "DROP_RATE_HIGH"),
    T3_CAPE(3, Equipment.CAPE_SLOT, 25, "DROP_RATE_HIGH"),
    T3_AMULET(3, Equipment.AMULET_SLOT, 25, "DROP_RATE_HIGH"),
    T3_RING(3, Equipment.RING_SLOT, 25, "DROP_RATE_HIGH"),
    T3_ARROWS(3, Equipment.AMMUNITION_SLOT, 25, "DROP_RATE_HIGH"),
    T3_AURA(3, Equipment.AURA_SLOT, 25, "DROP_RATE_HIGH"),
    T3_HALO(3, Equipment.HALO_SLOT, 25, "DROP_RATE_HIGH"),
    T3_GEMSTONE(3, Equipment.GEMSTONE_SLOT, 25, "DROP_RATE_HIGH"),
    T3_SHIELD(3, Equipment.SHIELD_SLOT, 25, "DROP_RATE_HIGH"),

    ;

    private final int tier, itemSlot, bonus;
    private final String perk;

    GearPackBoost(int tier, int itemSlot, int bonus, String perk){
        this.tier = tier;
        this.itemSlot = itemSlot;
        this.bonus = bonus;
        this.perk = perk;
    }

    public static GearPackBoost getBoost(int tier, int slot){
        return Arrays.stream(GearPackBoost.values()).filter(p -> p.getTier() == tier && p.getItemSlot() == slot).findFirst().orElse(null);
    }
}
