package com.ruse.world.packages.combat;

import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.ruse.model.container.impl.Equipment.*;

public class CombatConstants {

    public static boolean wearingEventArmor(@NotNull Player player){
        return player.getEquipment().containsAll(23127, 23128, 23129, 23130, 23131, 23132, 23133);
    }

    public static boolean wearingSlayerArmor(@NotNull Player player){
        return player.getEquipment().containsAll(24000, 24001, 24002);
    }

    public static boolean wearingUltimateBankItem(@NotNull Player player){
        return player.getEquipment().containsAny(4446, 19886, 15450, 23087, 19888, 18888, 13555, 15834, 11195, 4489, 18818, 18823);
    }

    /**
     * Animation Constants
     * @param player
     * @return
     */

    public static int BACK_HAND_MELEE = 2876;
    public static int FORWARD_CHOP_MELEE = 402;
    public static int FAST_CHOP_MELEE = 1658;

    public static int STAFF_CHARGE_MAGIC = 791;
    public static int CAST_SPELL_MAGIC = 729;
    public static int CAST_SPELL_UP_MAGIC = 725;
    public static int SWING_SPELL_MAGIC = 1060;
    public static int CHARGE_SPELL_MAGIC = 1914;


    public static int OVERLOAD_ANIMATION = 3170;
    public static int SHAKE_PUT_DOWN = 3007;
    public static int SIT_AND_BLOW = 1877;

    public static int CHEER = 2400;
    public static int FALLING = 744;
    public static int STEP_BACK = 1125;

    /**
     * GFX Constants
     * @param player
     * @return
     */

    public static int WHITE_CRYSTAL_MAGIC = 2264;
    public static int BLUE_ORB_IN_FRONT = 499;
    public static int SKELETON_FREAKOUT = 720;
    public static int PURPLE_PORTAL_UP = 332;

    /**
     * Projectile Constants
     * @param player
     * @return
     */

    public static int WHITE_CRYSTAL_MAGIC_PROJECTILE = 360;

    public static boolean waeringRuthlessGladiator(@NotNull Player player){
        return player.getEquipment().containsAll(15005, 15006, 15007, 15008,
                15200, 15201, 15100, 14915);
    }
}
