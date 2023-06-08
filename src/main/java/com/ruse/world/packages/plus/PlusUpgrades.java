package com.ruse.world.packages.plus;

import com.ruse.model.Item;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PlusUpgrades {

    OWNER_CHEST(22233, new Item[]{
            new Item(23217, 20)
    }, new Item(23300, 1)),
    OWNER_CHEST_1(23300, new Item[]{
            new Item(23217, 30)
    }, new Item(23301, 1)),
    OWNER_CHEST_2(23301, new Item[]{
            new Item(23217, 40)
    }, new Item(23302, 1)),
    OWNER_CHEST_3(23302, new Item[]{
            new Item(23218, 15)
    }, new Item(23303, 1)),
    OWNER_CHEST_4(23303, new Item[]{
            new Item(23218, 25)
    }, new Item(23304, 1)),

    OWNER_AURA(22111, new Item[]{
        new Item(23217, 20)
    }, new Item(23305, 1)),
    OWNER_AURA_1(23305, new Item[]{
            new Item(23217, 30)
    }, new Item(23306, 1)),
    OWNER_AURA_2(23306, new Item[]{
            new Item(23217, 40)
    }, new Item(23307, 1)),
    OWNER_AURA_3(23307, new Item[]{
            new Item(23218, 15)
    }, new Item(23308, 1)),
    OWNER_AURA_4(23308, new Item[]{
            new Item(23218, 25)
    }, new Item(23309, 1)),

    OWNER_SWORD(22229, new Item[]{
            new Item(23217, 20)
    }, new Item(23310, 1)),
    OWNER_SWORD_1(23310, new Item[]{
            new Item(23217, 30)
    }, new Item(23311, 1)),
    OWNER_SWORD_2(23311, new Item[]{
            new Item(23217, 40)
    }, new Item(23312, 1)),
    OWNER_SWORD_3(23312, new Item[]{
            new Item(23218, 15)
    }, new Item(23313, 1)),
    OWNER_SWORD_4(23313, new Item[]{
            new Item(23218, 25)
    }, new Item(23314, 1)),

    OWNER_STAFF(22230, new Item[]{
            new Item(23217, 20)
    }, new Item(23315, 1)),
    OWNER_STAFF_1(23315, new Item[]{
            new Item(23217, 30)
    }, new Item(23316, 1)),
    OWNER_STAFF_2(23316, new Item[]{
            new Item(23217, 40)
    }, new Item(23317, 1)),
    OWNER_STAFF_3(23317, new Item[]{
            new Item(23218, 15)
    }, new Item(23318, 1)),
    OWNER_STAFF_4(23318, new Item[]{
            new Item(23218, 25)
    }, new Item(23319, 1)),

    OWNER_BOW(22231, new Item[]{
            new Item(23217, 20)
    }, new Item(23320, 1)),
    OWNER_BOW_1(23320, new Item[]{
            new Item(23217, 30)
    }, new Item(23321, 1)),
    OWNER_BOW_2(23321, new Item[]{
            new Item(23217, 40)
    }, new Item(23322, 1)),
    OWNER_BOW_3(23322, new Item[]{
            new Item(23218, 15)
    }, new Item(23323, 1)),
    OWNER_BOW_4(23323, new Item[]{
            new Item(23218, 25)
    }, new Item(23324, 1)),

    OWNER_HELM(22232, new Item[]{
            new Item(23217, 20)
    }, new Item(23325, 1)),
    OWNER_HELM_1(23325, new Item[]{
            new Item(23217, 30)
    }, new Item(23326, 1)),
    OWNER_HELM_2(23326, new Item[]{
            new Item(23217, 40)
    }, new Item(23327, 1)),
    OWNER_HELM_3(23327, new Item[]{
            new Item(23218, 15)
    }, new Item(23328, 1)),
    OWNER_HELM_4(23328, new Item[]{
            new Item(23218, 25)
    }, new Item(23329, 1)),

    OWNER_LEGS(22234, new Item[]{
            new Item(23217, 20)
    }, new Item(23330, 1)),
    OWNER_LEGS_1(23330, new Item[]{
            new Item(23217, 30)
    }, new Item(23331, 1)),
    OWNER_LEGS_2(23331, new Item[]{
            new Item(23217, 40)
    }, new Item(23332, 1)),
    OWNER_LEGS_3(23332, new Item[]{
            new Item(23218, 15)
    }, new Item(23333, 1)),
    OWNER_LEGS_4(23333, new Item[]{
            new Item(23218, 25)
    }, new Item(23334, 1)),

    OWNER_BOOTS(22235, new Item[]{
            new Item(23217, 20)
    }, new Item(23335, 1)),
    OWNER_BOOTS_1(23335, new Item[]{
            new Item(23217, 30)
    }, new Item(23336, 1)),
    OWNER_BOOTS_2(23336, new Item[]{
            new Item(23217, 40)
    }, new Item(23337, 1)),
    OWNER_BOOTS_3(23337, new Item[]{
            new Item(23218, 15)
    }, new Item(23338, 1)),
    OWNER_BOOTS_4(23338, new Item[]{
            new Item(23218, 25)
    }, new Item(23339, 1)),


    OWNER_GLOVES(22236, new Item[]{
            new Item(23217, 20)
    }, new Item(23340, 1)),
    OWNER_GLOVES_1(23340, new Item[]{
            new Item(23217, 30)
    }, new Item(23341, 1)),
    OWNER_GLOVES_2(23341, new Item[]{
            new Item(23217, 40)
    }, new Item(23342, 1)),
    OWNER_GLOVES_3(23342, new Item[]{
            new Item(23218, 15)
    }, new Item(23343, 1)),
    OWNER_GLOVES_4(23343, new Item[]{
            new Item(23218, 25)
    }, new Item(23344, 1)),

    OWNER_WINGS(22237, new Item[]{
            new Item(23217, 20)
    }, new Item(23345, 1)),
    OWNER_WINGS_1(23345, new Item[]{
            new Item(23217, 30)
    }, new Item(23346, 1)),
    OWNER_WINGS_2(23346, new Item[]{
            new Item(23217, 40)
    }, new Item(23347, 1)),
    OWNER_WINGS_3(23347, new Item[]{
            new Item(23218, 15)
    }, new Item(23348, 1)),
    OWNER_WINGS_4(23348, new Item[]{
            new Item(23218, 25)
    }, new Item(23349, 1)),

    

    ;

    private final int itemId;
    private final Item[] cost;
    private final Item result;
    PlusUpgrades(int itemId, Item[] cost, Item result){
        this.itemId = itemId;
        this.cost = cost;
        this.result = result;
    }

    public static PlusUpgrades getPlus(int id){
        return Arrays.stream(PlusUpgrades.values()).filter(p -> p.getItemId() == id).findFirst().orElse(null);
    }

    public static boolean isMaterial(int id){
        return Arrays.stream(PlusUpgrades.values()).anyMatch(p -> Arrays.stream(p.getCost()).anyMatch(i -> i.getId() == id));
    }

}
