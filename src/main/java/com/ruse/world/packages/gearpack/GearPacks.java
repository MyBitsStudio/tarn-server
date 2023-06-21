package com.ruse.world.packages.gearpack;

import com.ruse.model.Item;
import com.ruse.util.Misc;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GearPacks {

    /**Normal Sets**/
    ANTIQUE(11930, 0,
            new Item(8816, 1), new Item(8817, 1), new Item(8818, 1),
            new Item(8819, 1), new Item(8820, 1), new Item(Misc.random(23144, 23146), 1)
    ),

    EXECUTIVE(11946, 0,
            new Item(4684, 1), new Item(4685, 1), new Item(4686, 1),
            new Item(8273, 1), new Item(8274, 1), new Item(9939, 1),
            new Item(5012, 1)
    ),

    NITE(11942, 0,
            new Item(17698, 1), new Item(17770, 1), new Item(17614, 1),
            new Item(17616, 1), new Item(17618, 1), new Item(17606, 1),
            new Item(17622, 1), new Item(11195, 1)
            ),

    /****/
    OBSIDIAN(11938, 0,
            new Item(23066, 1), new Item(23067, 1), new Item(23061, 1),
            new Item(23062, 1), new Item(23063, 1), new Item(23068, 1),
            new Item(12612, 1)
            ),
    /****/
    MALVEK(19588, 0,
            new Item(14018, 1), new Item(19160, 1), new Item(19519, 1),
            new Item(19158, 1)
            ),
    /****/
    ONYX(11960, 0,
            new Item(20427, 1), new Item(20260, 1), new Item(20095, 1)
            ),
    /****/
    BLOOD(11874, 0,
            new Item(8136, 1), new Item(8813, 1), new Item(8815, 1),
            new Item(17283, 1), new Item(8814, 1), new Item(16194, 1)
            ),
    /****/
    DEMONLORD(11916, 0,
            new Item(14188, 1), new Item(14184, 1), new Item(14178, 1),
            new Item(14186, 1), new Item(14180, 1), new Item(14182, 1)
            ),

    LILI(11944, 0,
            new Item(22143, 1), new Item(22136, 1), new Item(22137, 1),
            new Item(22138, 1), new Item(22141, 1), new Item(22139, 1),
            new Item(22142, 1)
            ),
    /****/
    GROUNDON(11920, 0,
            new Item(13640, 1), new Item(13964, 1), new Item(21934, 1),
            new Item(19918, 1), new Item(19913, 1), new Item(3107, 1)
            ),

    VARTHUR(19592, 0,
            new Item(13640, 1), new Item(13964, 1), new Item(21934, 1),
            new Item(19918, 1), new Item(19913, 1), new Item(3107, 1)
            ),

    TYRANT(11926, 0,
            new Item(17694, 1), new Item(17696, 1), new Item(14190, 1),
            new Item(14192, 1), new Item(14194, 1), new Item(14200, 1),
            new Item(14198, 1), new Item(14196, 1), new Item(12608, 1)
            ),

    LUCIFER(14529, 0,
            new Item(17644, 1), new Item(22100, 1), new Item(22101, 1),
            new Item(22102, 1), new Item(22105, 1), new Item(22103, 1),
            new Item(22104, 1)
    ),

    VIRTUOS(19582, 0,
            new Item(14305, 1), new Item(14307, 1), new Item(14202, 1),
            new Item(14204, 1), new Item(14206, 1), new Item(14303, 1),
            new Item(14301, 1)
            ),

    AUGMON(9666, 0,
            new Item(22155, 1), new Item(22152, 1), new Item(22153, 1),
            new Item(22154, 1), new Item(22158, 1), new Item(22159, 1),
            new Item(22160, 1)
    ),
    /****/
    WHITE(23226, 0,
            new Item(22167, 1), new Item(22163, 1), new Item(22165, 1),
            new Item(22166, 1), new Item(22161, 1), new Item(22162, 1)
    ),

    SOLDIER(23227, 0,
            new Item(23079, 1), new Item(23080, 1), new Item(23075, 1),
            new Item(23076, 1), new Item(23077, 1)
    ),

    EVIL(23228, 0,
            new Item(22133, 1), new Item(14325, 1), new Item(14327, 1),
            new Item(14329, 1), new Item(14323, 1)
    ),

    DEATH(23229, 0,
            new Item(14361, 1), new Item(14359, 1), new Item(14363, 1),
            new Item(14339, 1), new Item(14337, 1), new Item(14347, 1),
            new Item(14345, 1), new Item(14341, 1), new Item(14353, 1)
            ),

    CHARYBDE(23230, 0,
            new Item(8828, 1), new Item(8829, 1), new Item(8833, 1),
            new Item(8830, 1), new Item(8831, 1)
            ),

    SCYLLA(23231, 0,
            new Item(14369, 1), new Item(14373, 1), new Item(14371, 1),
            new Item(14375, 1), new Item(14365, 1), new Item(14367, 1)
            ),

    EXODE(23232, 0,
            new Item(22036, 1), new Item(22037, 1), new Item(22038, 1),
            new Item(5594, 1), new Item(6937, 1), new Item(3905, 1)
            ),

    GLADIATOR(23233, 0,
            new Item(20522, 1), new Item(15008, 1), new Item(15005, 1),
            new Item(15006, 1), new Item(15007, 1), new Item(15100, 1),
            new Item(15201, 1), new Item(15200, 1)
    ),

    AFREET(23234, 0,
            new Item(14379, 1), new Item(14383, 1), new Item(14385, 1),
            new Item(14381, 1)
            ),

    FRIEZA(23235, 0,
            new Item(17702, 1), new Item(11763, 1), new Item(11764, 1),
            new Item(11765, 1), new Item(11767, 1), new Item(11766, 1)

    ),

    CELL(23236, 0,
            new Item(7543, 1), new Item(9481, 1), new Item(9482, 1),
            new Item(9483, 1), new Item(7545, 1)
    ),

    BUU(23237, 0,
            new Item(16249, 1), new Item(15832, 1), new Item(9478, 1),
            new Item(9479, 1), new Item(9480, 1), new Item(16265, 1)
            ),

    GOKU(23238, 0,
            new Item(Misc.random(8410, 8412), 1), new Item(13323, 1), new Item(13324, 1),
            new Item(13325, 1), new Item(1486, 1), new Item(13327, 1),
            new Item(13326, 1)
    ),

    SHINIGAMI(23239, 0,
            new Item(13333, 1), new Item(13328, 1), new Item(13329, 1),
            new Item(13330, 1), new Item(4369, 1), new Item(13332, 1),
            new Item(3318, 1)
            ),

    FAZULA(23240, 0,
            new Item(4071, 1), new Item(4072, 1), new Item(4066, 1),
            new Item(4067, 1), new Item(4068, 1), new Item(4069, 1),
            new Item(4070, 1)
    ),

    YASUDA(23241, 0,
            new Item(3472, 1), new Item(4075, 1), new Item(4077, 1),
            new Item(3473, 1), new Item(3470, 1), new Item(4085, 1),
            new Item(4083, 1)
            ),

    BLACK_GOKU(23242, 0,
            new Item(12843, 1), new Item(14440, 1), new Item(14438, 1),
            new Item(14444, 1), new Item(14446, 1)
    ),

    SOLAR(14525, 0,
            new Item(22135, 1), new Item(15645, 1), new Item(15646, 1),
            new Item(15647, 1)
            ),


    /**Tier 3**/
    SOLAR_3(23243, 3,
            new Item(22135, 1), new Item(15645, 1), new Item(15646, 1),
            new Item(15647, 1)
            ),
    /**Tier 2**/
    SHINIGAMI_2(23244, 2,
            new Item(13333, 1), new Item(13328, 1), new Item(13329, 1),
            new Item(13330, 1), new Item(4369, 1), new Item(13332, 1),
            new Item(3318, 1)
    ),

    FAZULA_2(23245, 2,
            new Item(4071, 1), new Item(4072, 1), new Item(4066, 1),
            new Item(4067, 1), new Item(4068, 1), new Item(4069, 1),
            new Item(4070, 1)
            ),

    YASUDA_2(23246, 2,
            new Item(3472, 1), new Item(4075, 1), new Item(4077, 1),
            new Item(3473, 1), new Item(3470, 1), new Item(4085, 1),
            new Item(4083, 1)
            ),
    /**Tier 1**/
    BLACK_GOKU_1(23247, 1,
            new Item(12843, 1), new Item(14440, 1), new Item(14438, 1),
            new Item(14444, 1), new Item(14446, 1)
            ),
    LIGHT_GUILD_1(23248, 1,
            new Item(8100, 1), new Item(8101, 1), new Item(8102, 1),
            new Item(8103, 1), new Item(8104, 1)
            ),

    DARK_GUILD_1(23249, 1,
            new Item(8105, 1), new Item(8106, 1), new Item(8107, 1),
            new Item(8108, 1), new Item(8109, 1), new Item(8110, 1)
    ),

    WARDEN_1(23250, 1,
            new Item(24003, 1), new Item(24004, 1), new Item(24005, 1),
            new Item(24006, 1), new Item(24007, 1), new Item(24008, 1),
            new Item(24009, 1), new Item(24010, 1)
    ),

    ASTA_1(23251, 1,
            new Item(22254, 1), new Item(22242, 1), new Item(22244, 1),
            new Item(22246, 1), new Item(22248, 1), new Item(22250, 1),
            new Item(22252, 1)
    ),

    ;
    private final int itemId, boostTier;
    private final Item[] items;
    GearPacks(int itemId, int boost, Item... items){
        this.itemId = itemId;
        this.boostTier = boost;
        this.items = items;
    }

    public boolean isBoosted(){
        return boostTier > 0;
    }

    public static GearPacks getPack(int id){
        return Arrays.stream(GearPacks.values()).filter(p -> p.getItemId() == id).findFirst().orElse(null);
    }

    public static boolean isPack(int id){
        return Arrays.stream(GearPacks.values()).anyMatch(p -> p.getItemId() == id);
    }
}
