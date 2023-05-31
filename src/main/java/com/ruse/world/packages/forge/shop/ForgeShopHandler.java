package com.ruse.world.packages.forge.shop;

import com.google.common.collect.ImmutableList;
import com.ruse.model.container.impl.Inventory;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.input.EnterAmount;
import com.ruse.world.packages.forge.Forge;
import com.ruse.world.entity.impl.player.Player;

import java.util.Optional;

public class ForgeShopHandler {

    private static final int INTERFACE_ID = 49542;

    public static boolean handleButtonClick(Player player, int id) {
        Optional<ForgeButtonType> buttonTypeOptional = ForgeButtonType
                .VALUES
                .stream()
                .filter(forgeButtonType -> forgeButtonType.buttonId == id)
                .findFirst();
        if(buttonTypeOptional.isPresent()) {
            ForgeButtonType buttonType = buttonTypeOptional.get();
            buttonType.forgeButton.execute(player);
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
        if(tier > player.getForge().getTier()) {
            player.getPacketSender().sendMessage(":forge:0");
        } else {
            player.getPacketSender().sendMessage(":forge:1");
        }
        ForgeShopItem[] items = ForgeShopData.FORGE_SHOP_DATA[type.getKey()][tier-1];
        if(items != null) {
            player.getPacketSender().sendItemContainer(items, 49565);
        }
    }

    public static void purchaseX(Player player, int itemId) {
        player.getPacketSender().sendEnterAmountPrompt("Enter amount of " + ItemDefinition.forId(itemId).getName() + " to buy:");
        player.setInputHandling(new EnterAmount() {
            @Override
            public void handleAmount(Player player, int amount) {
                purchase(player, itemId, amount);
            }
        });
    }

    public static void purchase(Player player, int itemId, int amount) {
        int viewingTier = player.getForgeShopTier();
        int currentTier = player.getForge().getTier();
        if(currentTier < viewingTier) {
            player.getPacketSender().sendMessage("@red@You need tier " + viewingTier + " to buy items from this shop.");
            return;
        }
        Integer price;
        if((price = ForgeShopData.PRICE_MAP.get(itemId)) != null) {
            long total = (long) price * amount;
            if(total > Integer.MAX_VALUE) {
                player.getPacketSender().sendMessage("@red@Unable to buy this quantity at this price.");
                return;
            }
            ItemDefinition itemDefinition = ItemDefinition.getDefinitions()[Forge.FRAGMENT_ITEM_ID];
            Inventory inventory = player.getInventory();
            int playerAmount = inventory.getAmount(itemDefinition.getId());
            if(total > playerAmount) {
                player.getPacketSender().sendMessage("@red@Insufficient " + itemDefinition.getName() + " to complete this transaction.");
                return;
            }
            if(inventory.isFull()) {
                player.getPacketSender().sendMessage("@red@You need some inventory slots to buy this item.");
                return;
            }
            if(inventory.getFreeSlots() < amount) {
                player.getPacketSender().sendMessage("@red@You need at least " + amount + " inventory slots to buy this item.");
                return;
            }
            inventory.delete(itemDefinition.getId(), (int) total).add(itemId, amount).refreshItems();
            player.getPacketSender().sendItemContainer(inventory, 49567);
            return;
        }
        player.getPacketSender().sendMessage("@red@Error buying item. please report");
    }

    @FunctionalInterface
    interface ForgeButton {
        void execute(Player player);
    }

    enum ForgeButtonType {
        OPEN_SHOP(-16019, ForgeShopHandler::openInterface),
        TIER_ONE(-15984, (Player player) -> switchTier(player, 1)),
        TIER_TWO(-15983, (Player player) -> switchTier(player, 2)),
        TIER_THREE(-15982, (Player player) -> switchTier(player, 3)),
        ARMOURY(-15981, (Player player) -> switchShopType(player, ForgeShopType.ARMOURY)),
        WEAPON(-15980, (Player player) -> switchShopType(player, ForgeShopType.WEAPONS)),
        JEWELRY(-15979, (Player player) -> switchShopType(player, ForgeShopType.JEWELRY));

        private final int buttonId;
        private final ForgeButton forgeButton;

        ForgeButtonType(int buttonId, ForgeButton forgeButton) {
            this.buttonId = buttonId;
            this.forgeButton = forgeButton;
        }

        public static final ImmutableList<ForgeButtonType> VALUES = ImmutableList.copyOf(values());
    }
}
