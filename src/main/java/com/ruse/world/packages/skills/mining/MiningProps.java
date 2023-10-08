package com.ruse.world.packages.skills.mining;

import com.ruse.model.Item;
import com.ruse.model.container.impl.Equipment;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class MiningProps {

    @Getter
    public enum Rocks {

        DONATOR_PERK(10801, new Item[]{new Item(23149), new Item(23148), new Item(23147)},
                75, 20, 120, 95),

        DONATOR_COIN(10799, new Item[]{new Item(995, 500), new Item(995, 1000), new Item(995, 2500)},
                1, 10, 90, 20),

        DONATOR_ENHANCEMENT(10802, new Item[]{
                new Item(13727, 10), new Item(13727, 20), new Item(13727, 30),
                new Item(13727, 40), new Item(13727, 50)},
                45, 20, 90, 62),

        DONATOR_TICKETS(10800, new Item[]{
                new Item(21816, 5), new Item(21815, 5), new Item(21815, 10),
                new Item(21814, 10), new Item(21814, 5),new Item(23003, 2)
        }, 99, 30, 240, 142),

        AFK(21149, new Item[]{new Item(5020, 1)}, 1, 5, 0, 5),
        AFK1(713, new Item[]{new Item(5020, 1)}, 1, 5, 0, 5),
        AFK2(712, new Item[]{new Item(5020, 1)}, 1, 5, 0, 5),
        AFK3(711, new Item[]{new Item(5020, 1)}, 1, 5, 0, 5)
        ;

        private final int objectId, req, ticks, timer, xp;
        private final Item[] materialIds;

        Rocks(int objectId, Item[] materialId, int req, int ticks, int timer, int xp) {
            this.objectId = objectId;
            this.materialIds = materialId;
            this.req = req;
            this.ticks = ticks;
            this.timer = timer;
            this.xp = xp;
        }

        public static @Nullable Rocks forId(int id) {
            for (Rocks r : Rocks.values()) {
                if (r.getObjectId() == id) {
                    return r;
                }
            }
            return null;
        }
    }

    @Getter
    enum PickAxe {

        BASIC(21405, 1, 629, 1.0, 1.0),
        MYSTIC(16142, 19, 624, 1.2, 1.0),
        LAVA(19812, 49, 11019, 2.5, 1.2),
        CLASS_5(14130, 69, 11019, 4.0, 1.5),
        INFERNO(13661, 89, 10226, 6.0, 2.0),
        ICED(15259, 99, 12188, 8.0, 2.5),
        ICED_U(15261, 99, 12188, 12.0, 5.0),


        ;

        private final int id, req, anim;
        private final double speed, bonus;

        PickAxe(int id, int req, int anim, double speed, double bonus) {
            this.id = id;
            this.req = req;
            this.anim = anim;
            this.speed = speed;
            this.bonus = bonus;
        }

        public static @Nullable PickAxe forId(int id) {
            for (PickAxe p : PickAxe.values()) {
                if (p.getId() == id) {
                    return p;
                }
            }
            return null;
        }

    }

    public static int getPickaxe(final Player plr) {
        for (PickAxe p : PickAxe.values()) {
            if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == p.getId())
                return p.getId();
            else if (plr.getInventory().contains(p.getId()))
                return p.getId();
        }
        return -1;
    }

    public static boolean isHoldingPickaxe(final Player player) {
        for (PickAxe p : PickAxe.values()) {
            if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == p.getId()) {
                return true;
            }
        }
        return false;
    }
}
