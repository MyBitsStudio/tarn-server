package com.ruse.world.content;

import com.ruse.model.container.impl.Shop;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

public class DonatorShop {

    public static final int ITEM_CHILD_ID = 33900;
    public static final int ITEM_CHILD_ID_CLICK = -31636;
    public static final int INTERFACE_ID = 118000;

    private Player player;

    public DonatorShop(Player player) {
        this.player = player;
    }

    public boolean handleButton(int buttonId) {

        switch (buttonId) {
            case 118005:
                openInterface(DonatorShopType.WEAPON);
                return true;
            case 118006:
                openInterface(DonatorShopType.ARMOUR);
                return true;
            case 118007:
                openInterface(DonatorShopType.ACCESSORY);
                return true;
            case 118008:
                openInterface(DonatorShopType.MISC);
                return true;
        }

        return false;
    }

    public void openInterface(DonatorShopType type) {
        player.getPacketSender().sendConfig(5333, type.ordinal());
        Shop.ShopManager.getShops().get(type.getShopId()).open(player);
    }
    
    public static Object[] getPrice(int item){
        switch (item) {
            case 23146://dung weps
            case 23145:
            case 23144:
                return new Object[]{20, "Donator points"};
            case 8809://vod weps
                return new Object[]{40, "Donator points"};
            case 21018://vod weps
                return new Object[]{50, "Donator points"};
            case 7014://vod weps
                return new Object[]{60, "Donator points"};
            case 22135://vod weps
                return new Object[]{100, "Donator points"};
            case 5012://vod weps
                return new Object[]{115, "Donator points"}; //6
            case 17698://vod weps
            case 17700://vod weps
                return new Object[]{150, "Donator points"}; //7
            case 22143://vod weps
                return new Object[]{200, "Donator points"}; //8
            case 22148://vod weps
                return new Object[]{235, "Donator points"}; //9
            case 17694://vod weps
            case 17696://vod weps
                return new Object[]{275, "Donator points"}; //10
            case 14305://vod weps
                return new Object[]{350, "Donator points"};
            case 22155://vod weps
                return new Object[]{500, "Donator points"};
                //armours
            case 11930://vod weps
                return new Object[]{30, "Donator points"};
            case 11960://vod weps
                return new Object[]{50, "Donator points"};
            case 19588://vod weps
                return new Object[]{80, "Donator points"};
            case 11938://vod weps
                return new Object[]{100, "Donator points"};
            case 14525://vod weps
                return new Object[]{150, "Donator points"};
            case 11946://vod weps
                return new Object[]{200, "Donator points"};
            case 11942://vod weps
                return new Object[]{300, "Donator points"};
            case 11944://vod weps
                return new Object[]{400, "Donator points"};
            case 19592://vod weps
                return new Object[]{500, "Donator points"};
            case 11926://vod weps
                return new Object[]{650, "Donator points"};
            case 14529://vod weps
                return new Object[]{775, "Donator points"};
            case 19582://vod weps
                return new Object[]{850, "Donator points"};
            case 9666://vod weps
                return new Object[]{1000, "Donator points"};
                //acces
            case 12614://vod weps
                return new Object[]{30, "Donator points"};
            case 10887://vod weps
                return new Object[]{35, "Donator points"};
            case 23092://vod weps
            case 23093://vod weps
            case 23094://vod weps
                return new Object[]{40, "Donator points"};
            case 17391://vod weps
                return new Object[]{50, "Donator points"};
            case 12610://vod weps
                return new Object[]{75, "Donator points"};
            case 12612://vod weps
                return new Object[]{125, "Donator points"};
            case 1857://vod weps
                return new Object[]{200, "Donator points"};
            case 15448://vod weps
                return new Object[]{250, "Donator points"};
            case 5730://vod weps
                return new Object[]{275, "Donator points"};
            case 19886://vod weps
            case 4446://vod weps
                return new Object[]{35, "Donator points"};
            case 15450://vod weps
                return new Object[]{500, "Donator points"};
            case 2380://vod weps
                return new Object[]{7, "Donator points"};
            case 2381://vod weps
                return new Object[]{15, "Donator points"};
            case 2382://vod weps
                return new Object[]{30, "Donator points"};
            case 2383://vod weps
                return new Object[]{50, "Donator points"};
            case 8788://vod weps
                return new Object[]{10, "Donator points"};
            case 15250://vod weps
            case 15251://vod weps
                return new Object[]{1, "Donator points"};
            case 3253://vod weps
                return new Object[]{1500, "Donator points"};
            case 15355://vod weps
            case 15357://vod weps
                return new Object[]{5, "Donator points"};
            case 21816://vod weps
                return new Object[]{7, "Donator points"};
            case 23178://vod weps
                return new Object[]{200, "Donator points"};
            case 9084://vod weps
                return new Object[]{50, "Donator points"};
            case 22110://vod weps
                return new Object[]{250, "Donator points"};
            case 9083://vod weps
                return new Object[]{500, "Donator points"};
            case 23167://vod weps
            case 23165://vod weps
            case 23168://vod weps
            case 23170://vod weps
            case 23169://vod weps
            case 23166://vod weps
                return new Object[]{150, "Donator points"};
            case 23123://vod weps
            case 23120://vod weps
            case 23126://vod weps
                return new Object[]{75, "Donator points"};

            default:
                return new Object[]{1000, "Donator points"};
        }
    }

    @Getter
    public enum DonatorShopType {
        WEAPON(80),
        ARMOUR(200),
        ACCESSORY(201),
        MISC(202);
        private int shopId;

        DonatorShopType(int shopId) {
            this.shopId = shopId;
        }
        
        public static boolean isDonatorStore(int id){
            for (DonatorShopType donatorShopType : values()){
                if ( id == donatorShopType.getShopId()){
                    return true;
                }
            }
            return false;
        }
    }

}
