package com.ruse.world.content.forge;

import com.ruse.world.entity.impl.player.Player;

public class ForgeShop {

    private static final int INTERFACE_ID = 49542;

    public static boolean handleButtonClick(Player player, int id) {
        switch (id) {
            case -16019:
                openInterface(player);
                return true;

        }
        return false;
    }

    public static void openInterface(Player player) {
        player.setForgeShopType(ForgeShopType.ARMOURY);
        player.setForgeShopTier(1);
        player.getPacketSender().sendInterface(INTERFACE_ID)
                .sendConfig(1531, 0)
                .sendConfig(1532, 0);
        showShop(player);
    }

    private static void switchShopType(Player player, ForgeShopType forgeShopType) {
        player.setForgeShopType(forgeShopType);
        showShop(player);
    }

    private static void switchTier(Player player, int tier) {
        player.setForgeShopTier(tier);
        showShop(player);
    }

    private static void showShop(Player player) {

    }
}
