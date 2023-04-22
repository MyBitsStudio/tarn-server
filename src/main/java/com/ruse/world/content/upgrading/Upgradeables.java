package com.ruse.world.content.upgrading;

import com.ruse.model.Item;
import lombok.Getter;

import java.util.ArrayList;

import static com.ruse.world.content.upgrading.Upgradeables.UpgradeType.*;

@Getter
public enum Upgradeables {

    STARTER1(WEAPON, new Item(20098, 1), new Item(21048, 1), 15000, 100),
    STARTER2(WEAPON, new Item(17654, 1), new Item(21055, 1), 15000, 100),
    STARTER3(WEAPON, new Item(17672, 1), new Item(14919, 1), 35000, 100),
    STARTER4(WEAPON, new Item(14915, 1), new Item(23079, 1), 35000, 100),
    STARTER5(WEAPON, new Item(14377, 1), new Item(8809, 1), 35000, 100),
    STARTER6(WEAPON, new Item(17708, 1), new Item(17710, 1), 100000, 100),

    EXE_VITUR(WEAPON, new Item(12535, 1), new Item(8136, 1), 500000, 30),
    EXE_TBOW(WEAPON, new Item(5012, 1), new Item(5011, 1), 500000, 30),
    EXE_STAFF(WEAPON, new Item(9942, 1), new Item(17013, 1), 500000, 30),
    HAMMER1(WEAPON, new Item(17698, 1), new Item(17694, 1), 1000000, 50),
    HAMMER2(WEAPON, new Item(17700, 1), new Item(17696, 1), 1000000, 50),
    SUP_BOW(WEAPON, new Item(21018, 1), new Item(20497, 1), 350000, 75),

    STARTERARM1(ARMOUR, new Item(21036, 1), new Item(21042, 1), 50000, 100),
    STARTERARM2(ARMOUR, new Item(21037, 1), new Item(21043, 1), 50000, 100),
    STARTERARM3(ARMOUR, new Item(21038, 1), new Item(21044, 1), 50000, 100),
    STARTERARM4(ARMOUR, new Item(21041, 1), new Item(21047, 1), 50000, 100),
    STARTERARM5(ARMOUR, new Item(21040, 1), new Item(21046, 1), 50000, 100),
    STARTERARM6(ARMOUR, new Item(21039, 1), new Item(21045, 1), 50000, 100),

    SATANIC(ARMOUR, new Item(13300, 1), new Item(21028, 1), 1000000, 100),
    SATANIC2(ARMOUR, new Item(13301, 1), new Item(21029, 1), 1000000, 100),
    SATANIC3(ARMOUR, new Item(13304, 1), new Item(21030, 1), 1000000, 100),
    LUCI(ARMOUR, new Item(22100, 1), new Item(20427, 1), 1000000, 50),
    LUCI2(ARMOUR, new Item(22101, 1), new Item(20260, 1), 1000000, 50),
    LUCI3(ARMOUR, new Item(22102, 1), new Item(20095, 1), 1000000, 50),

    STARTERACC1(ACCESSORY, new Item(20092, 1), new Item(21068, 1), 10000, 100),
    STARTERACC2(ACCESSORY, new Item(20093, 1), new Item(21069, 1), 10000, 100),
    STARTERACC3(ACCESSORY, new Item(21068, 1), new Item(19991, 1), 75000, 100),
    STARTERACC4(ACCESSORY, new Item(21069, 1), new Item(19992, 1), 75000, 100),

    ROW1(ACCESSORY, new Item(3324, 1), new Item(20492, 1), 10000, 75),
    ROW2(ACCESSORY, new Item(20492, 1), new Item(4001, 1), 35000, 50),
    RING(ACCESSORY, new Item(23087, 1), new Item(23090, 1), 100000, 50),
    RING2(ACCESSORY, new Item(8335, 1), new Item(18817, 1), 1500000, 30),
    RING3(ACCESSORY, new Item(19892, 1), new Item(11195, 1), 1500000, 30),
    RING5(ACCESSORY, new Item(23046, 1), new Item(23047, 1), 500000, 50),

    IDD5(MISC, new Item(23057, 1), new Item(23058, 1), 100000, 35),
    IDD(MISC, new Item(23058, 1), new Item(3578, 1), 100000, 25),
    IDD2(MISC, new Item(15003, 1), new Item(15002, 1), 125000, 50),
    IDD3(MISC, new Item(15002, 1), new Item(15004, 1), 1000000, 50),
    IDD4(MISC, new Item(15004, 1), new Item(20489, 1), 2500000, 50),
    IDD6(MISC, new Item(604, 1), new Item(10947, 1), 100000, 75),

    ;

    private UpgradeType type;
    private Item required, reward;
    private int cost, successRate;
    private boolean rare;

    Upgradeables(UpgradeType type, Item required, Item reward, int cost, int successRate, boolean rare) {
        this.type = type;
        this.required = required;
        this.reward = reward;
        this.cost = cost;
        this.successRate = successRate;
        this.rare = rare;
    }

    Upgradeables(UpgradeType type, Item required, Item reward, int cost, int successRate) {
        this.type = type;
        this.required = required;
        this.reward = reward;
        this.cost = cost;
        this.successRate = successRate;
        this.rare = false;
    }
    
    public static ArrayList<Upgradeables> getForType(UpgradeType type){
        ArrayList<Upgradeables> upgradeablesArrayList = new ArrayList<>();
        for (Upgradeables upgradeables : values()){
            if (upgradeables.getType() == type){
                upgradeablesArrayList.add(upgradeables);
            }
        }
        return upgradeablesArrayList;
    }
    

    public enum UpgradeType{

        WEAPON, ARMOUR, ACCESSORY, MISC;

    }


}
