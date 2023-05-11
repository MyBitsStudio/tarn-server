package com.ruse.world.content.forge.shop;

import com.ruse.world.entity.impl.player.Player;

public class ForgeShopHandler {

    private static final int INTERFACE_ID = 49542;

    public static boolean handleButtonClick(Player player, int id) {
        switch (id) {
            case -16019:
                openInterface(player);
                return true;
            case -15984:
                switchTier(player, 1);
                return true;
            case -15983:
                switchTier(player, 2);
                return true;
            case -15982:
                switchTier(player, 3);
                return true;
            case -15981:
                switchShopType(player, ForgeShopType.ARMOURY);
                return true;
            case -15980:
                switchShopType(player, ForgeShopType.WEAPONS);
                return true;
            case -15979:
                switchShopType(player, ForgeShopType.JEWELRY);
                return true;

        }
        return false;
    }

    public static void openInterface(Player player) {
        player.setForgeShopType(ForgeShopType.ARMOURY);
        player.setForgeShopTier(1);
        player.getPacketSender().sendItemContainer(player.getInventory(), 49567)
                .sendInterfaceSet(INTERFACE_ID, 49566)
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
        ForgeShopType type = player.getForgeShopType();
        int tier = player.getForgeShopTier();
        ForgeShopItem[] items = ForgeShopData.FORGE_SHOP_DATA[type.getKey()][tier-1];
        if(items != null) {
            player.getPacketSender().sendItemContainer(items, 49565);
        }
    }
}
