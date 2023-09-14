package com.ruse.model.definitions;

import com.ruse.GameServer;
import com.ruse.engine.GameEngine;
import com.ruse.model.container.impl.Equipment;
import com.ruse.security.save.impl.server.defs.ItemDataLoad;
import com.ruse.security.save.impl.server.defs.NPCDataLoad;
import lombok.Getter;
import lombok.Setter;

import java.io.*;

/**
 * This file manages every item definition, which includes their name,
 * description, value, skill requirements, etc.
 *
 * @author relex lawl
 */
@Getter
@Setter
public class ItemDefinition {


    public static final int COIN_ID = 995;
    public static final int TOKEN_ID = 10835;
    /**
     * The max amount of items that will be loaded.-+
     */
    private static final int  MAX_AMOUNT_OF_ITEMS = 26000;

    /**
     * ItemDefinition array containing all items' definition values.
     */
    public static final ItemDefinition[] definitions = new ItemDefinition[MAX_AMOUNT_OF_ITEMS];
    /**
     * The id of the item.
     */
    private int id = 0;
    /**
     * The name of the item.
     */
    private String name = "None", description = "Null";
    /**
     * Flag to check if item is stackable.
     */
    private boolean stackable;
    /**
     * The item's shop value.
     */
    private int value;
    /**
     * Flag that checks if item is noted.
     */
    private boolean noted, isTwoHanded, weapon, secondTab;
    private EquipmentType equipmentType = EquipmentType.WEAPON;
    private double[] bonus = new double[18];
    private int[] requirement = new int[25];

    /**
     * Loading all item definitions
     */
    public static void init() {
        new ItemDataLoad().loadArray("./.core/server/defs/items/itemData.json").run();
        dumpItems();
    }
    
    public boolean isEquitable;
    
    public static void dumpItems() {

        File file = new File("./data/def/itemstats.dat");
        if (file.exists())
           file.delete();

        GameEngine.submit(() -> {
            try {
                FileWriter fw = new FileWriter("./data/def/itemstats.dat", true);
                if (fw != null) {
                    fw.write("[STATS]");
                    fw.write(System.lineSeparator());
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        for (ItemDefinition def : definitions) {
            if (def == null || def.getBonus() == null)
                continue;

            boolean dump = false;
            for (double b : def.getBonus()) {
                if (b > 0) {
                    dump = true;
                    break;
                }
            }

            if (dump) {
                GameEngine.submit(() -> {
                    try {
                        FileWriter fw = new FileWriter("./data/def/itemstats.dat", true);
                        String write = def.id + " ";
                        for (int i = 0; i < 10; i++) {
                            write += (int) def.getBonus()[i] + " ";
                        }
                        write += (int) def.getBonus()[14] + " ";
                        write += (int) def.getBonus()[15] + " ";
                        write += (int) def.getBonus()[16] + " ";
                        write += (int) def.getBonus()[17];

                        if (fw != null) {
                            fw.write(write);
                            fw.write(System.lineSeparator());
                            fw.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }


    public static ItemDefinition[] getDefinitions() {
        return definitions;
    }

    /**
     * Gets the item definition correspondent to the id.
     *
     * @param id The id of the item to fetch definition for.
     * @return definitions[id].
     */
    public static ItemDefinition forId(int id) {
        return (id < 0 || id > definitions.length || definitions[id] == null) ? new ItemDefinition() : definitions[id];
    }

    /**
     * Gets the max amount of items that will be loaded in Niobe.
     *
     * @return The maximum amount of item definitions loaded.
     */
    public static int getMaxAmountOfItems() {
        return MAX_AMOUNT_OF_ITEMS;
    }

    public static int getItemId(String itemName) {
        for (int i = 0; i < MAX_AMOUNT_OF_ITEMS; i++) {
            if (definitions[i] != null) {
                if (definitions[i].getName().equalsIgnoreCase(itemName)) {
                    return definitions[i].getId();
                }
            }
        }
        return -1;
    }

    /**
     * Checks if the item is stackable.
     *
     * @return stackable.
     */
    public boolean isStackable() {
        if (noted)
            return true;
        return stackable;
    }

    /**
     * Gets the item's shop value.
     *
     * @return value.
     */
    public int getValue() {
        return isNoted() ? ItemDefinition.forId(getId() - 1).value : value;
    }

    /**
     * Gets the item's equipment slot index.
     *
     * @return equipmentSlot.
     */
    public int getEquipmentSlot() {
        return equipmentType.slot;
    }

    /**
     * Checks if item is noted.
     *
     * @return noted.
     */
    public boolean isNoted() {
        return noted;
    }

    public boolean isSecondTab() { return secondTab; };

    /**
     * Checks if item is two-handed
     */
    public boolean isTwoHanded() {
        return isTwoHanded;
    }

    public boolean isWeapon() {
        return weapon;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    /**
     * Checks if item is full body.
     */
    public boolean isFullBody() {
        return equipmentType.equals(EquipmentType.PLATEBODY);
    }

    /**
     * Checks if item is full helm.
     */
    public boolean isFullHelm() {
        return equipmentType.equals(EquipmentType.FULL_HELMET);
    }
    public boolean isFullHelm1() {
        return equipmentType.equals(EquipmentType.FULL_HELMET1);
    }

    public double[] getBonus() {
        return bonus;
    }

    public int[] getRequirement() {
        return requirement;
    }


    @Override
    public String toString() {
        return "[ItemDefinition(" + id + ")] - Name: " + name + "; equipment slot: " + getEquipmentSlot() + "; value: "
                + value + "; stackable ? " + stackable + "; noted ? " + noted
                + "; 2h ? " + isTwoHanded;
    }

    @Getter
    public enum EquipmentType {
        HAT(Equipment.HEAD_SLOT), CAPE(Equipment.CAPE_SLOT), SHIELD(Equipment.SHIELD_SLOT),
        GLOVES(Equipment.HANDS_SLOT), BOOTS(Equipment.FEET_SLOT), AMULET(Equipment.AMULET_SLOT),
        RING(Equipment.RING_SLOT), ARROWS(Equipment.AMMUNITION_SLOT), FULL_MASK(Equipment.HEAD_SLOT),
        FULL_HELMET(Equipment.HEAD_SLOT), BODY(Equipment.BODY_SLOT), PLATEBODY(Equipment.BODY_SLOT)
        , FULL_HELMET1(Equipment.HEAD_SLOT),
        LEGS(Equipment.LEG_SLOT), WEAPON(Equipment.WEAPON_SLOT), AURA(Equipment.AURA_SLOT),
        ENCHANTMENT(Equipment.ENCHANTMENT_SLOT), HALO(Equipment.HALO_SLOT), GEMSTONE(Equipment.GEMSTONE_SLOT);

        private final int slot;

        EquipmentType(int slot) {
            this.slot = slot;
        }

    }
}
