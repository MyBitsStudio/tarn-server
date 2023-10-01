package com.ruse.world.packages.referral;

import com.ruse.model.Item;
import lombok.Getter;

@Getter
public enum Refer {

    DRAGONIC(new String[]{"dragonic", "dragonicnl", "dragonicyt"},
            new Item(995, 1_500_000), new Item(23257, 2), new Item(23258, 1),
            new Item(20502, 3), new Item(23254, 2), new Item(23147, 3)),

    JIPY(new String[]{"jipy", "jipyrs", "jipyyt"},
            new Item(995, 500_000), new Item(23257, 2), new Item(20501, 3),
            new Item(23253, 2), new Item(23148, 3)),

    NOOBS(new String[]{"noobsown", "noobs", "noobsyt", "noobsownyt"},
            new Item(995, 1_000_000), new Item(23257, 3), new Item(23256, 2),
            new Item(23251, 1), new Item(23250, 1)),

    SAND(new String[]{"andesus", "sandyt", "nsandesusyt"},
            new Item(995, 500_000), new Item(23257, 2), new Item(23256, 1),
            new Item(23253, 1), new Item(23148, 2)),

    BASIC(new String[]{"TarnBASIC"},
            new Item(995, 500_000), new Item(23149, 3), new Item(23148, 2),
            new Item(20500, 2), new Item(23253, 2), new Item(23057, 1)),

    ;

    private final String[] synthax;
    private final Item[] pack;
    Refer(String[] synthax, Item... pack){
        this.synthax = synthax;
        this.pack = pack;
    }

    public static boolean isRefer(String synthax){
        for (Refer refer : Refer.values()){
            for (String s : refer.getSynthax()){
                if (s.equalsIgnoreCase(synthax)){
                    return true;
                }
            }
        }
        return false;
    }
}
