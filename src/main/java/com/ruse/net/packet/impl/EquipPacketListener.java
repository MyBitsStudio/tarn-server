package com.ruse.net.packet.impl;

import com.ruse.GameSettings;
import com.ruse.model.*;
import com.ruse.model.Locations.Location;
import com.ruse.model.container.impl.Equipment;
import com.ruse.model.container.impl.Inventory;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.definitions.WeaponAnimations;
import com.ruse.model.definitions.WeaponInterfaces;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.AutoCastSpell;
import com.ruse.world.content.BonusManager;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.content.PlayerPunishment.Jail;
import com.ruse.world.content.Sounds;
import com.ruse.world.content.Sounds.Sound;
import com.ruse.world.content.combat.magic.Autocasting;
import com.ruse.world.content.combat.weapon.CombatSpecial;
import com.ruse.world.content.minigames.impl.Dueling.DuelRule;
import com.ruse.world.content.skill.impl.old_dungeoneering.Dungeoneering;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.skills.S_Skills;

import java.time.LocalDateTime;

/**
 * This packet listener manages the equip action a player executes when wielding
 * or equipping an item.
 *
 * @author relex lawl
 */

public class EquipPacketListener implements PacketListener {


    public static final int OPCODE = 41;

    public static boolean equipItem(Player player, int id, int slot, int interfaceId) {
        if (!player.getControllerManager().canEquip(slot, id)) {
            return false;
        }

        ItemDefinition def = ItemDefinition.forId(id);
        if(def != null) {
            if(def.isNoted()) {
                String offence = "Attempted to equip notable item!";
                PlayerLogs.log(player.getUsername(), offence);
                return false;
            }
        }
        Equipment equipment;
        if(player.isSecondaryEquipment()) {
            if(!player.getSecondaryEquipmentUnlocks()[def.getEquipmentSlot()]) {
                player.sendMessage("@red@You must first claim a " + def.getEquipmentType() + " Certificate to unlock this Secondary Equipment Slot.");
                return false;
            }
            equipment = player.getSecondaryEquipment();
        } else {
            if(def.isSecondTab()) {
                player.sendMessage("This item can only be equipped into the Secondary Equipment Slot.");
                return false;
            }
            equipment = player.getEquipment();
        }
        if(!player.getMode().canWear(id)){
            player.sendMessage("You cannot wear this item in this game mode.");
            return false;
        }

        switch (id) {
            case 14880:
                DialogueManager.sendStatement(player, "<img=14>This item collects all drops automatically!");
                player.getPacketSender().sendMessage("<shad=1>@red@This item collects all drops automatically!");
                break;
            case 773:
                if (player.getRank().isDeveloper()) {
                    player.getPacketSender().sendMessage("Precious, precious, precious! My Precious! O my Precious!");
                    World.sendStaffMessage("@red@[BUG] " + player.getUsername() + " just tried to equip a Perfect Ring!");
                } else {
                    player.getPacketSender()
                            .sendMessage("We wants it, we needs it. Must have the precious. They stole it from us.")
                            .sendMessage("Sneaky little hobbitses. Wicked, tricksy, false! The ring has vanished again..");
                    player.getInventory().delete(id, 1);
                    break;
                }
                break;
        }
        if (interfaceId == Inventory.INTERFACE_ID) {/*
         * Making sure slot is valid.
         */
            if (slot >= 0 && slot <= 28) {
                Item item = player.getInventory().getItems()[slot].copy();
                if (!player.getInventory().contains(item.getId()))
                    return false;
                /*
                 * Making sure item exists and that id is consistent.
                 */
                if (id == item.getId()) {
                    for (S_Skills skill : S_Skills.values()) {

                        if (item.getDefinition().getRequirement().length != 0 && item.getDefinition().getRequirement()[skill.ordinal()] > player.getNewSkills()
                                .getMaxLevel(skill)) {
                            StringBuilder vowel = new StringBuilder();
                            if (skill.getName().startsWith("a") || skill.getName().startsWith("e")
                                    || skill.getName().startsWith("i") || skill.getName().startsWith("o")
                                    || skill.getName().startsWith("u")) {
                                vowel.append("an ");
                            } else {
                                vowel.append("a ");
                            }
                            player.getPacketSender().sendMessage("You need " + vowel
                                    + Misc.formatText(skill.getName()) + " level of at least "
                                    + item.getDefinition().getRequirement()[skill.ordinal()] + " to wear this.");
                            return false;
                        }
                    }

                    int equipmentSlot = item.getDefinition().getEquipmentSlot();
                    Item equipItem = equipment.forSlot(equipmentSlot).copy();

                    if (equipItem.getDefinition().isStackable() && equipItem.getId() == item.getId()) {
                        int amount = Math.min(equipItem.getAmount() + item.getAmount(), Integer.MAX_VALUE);
                        player.getInventory().delete(item);
                        equipment.getItems()[equipmentSlot].setAmount(amount);
                        equipItem.setAmount(amount);
                        equipment.refreshItems();
                    } else if (item.getDefinition().isTwoHanded()
                            && item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
                        int slotsRequired = equipment.isSlotOccupied(Equipment.SHIELD_SLOT)
                                && equipment.isSlotOccupied(Equipment.WEAPON_SLOT) ? 1 : 0;
                        if (player.getInventory().getFreeSlots() < slotsRequired) {
                            player.getInventory().full();
                            return false;
                        }

                        Item shield = equipment.getItems()[Equipment.SHIELD_SLOT];
                        Item weapon = equipment.getItems()[Equipment.WEAPON_SLOT];

                        equipment.set(Equipment.SHIELD_SLOT, new Item(-1, 0));
                        player.getInventory().delete(item);
                        equipment.set(equipmentSlot, item);

                        if (shield.getId() != -1) {
                            player.getInventory().add(shield);
                        }

                        if (weapon.getId() != -1) {
                            player.getInventory().add(weapon);
                        }
                    } else if (equipmentSlot == Equipment.SHIELD_SLOT
                            && equipment.getItems()[Equipment.WEAPON_SLOT].getDefinition()
                            .isTwoHanded()) { //
                        player.getInventory().setItem(slot,
                                equipment.getItems()[Equipment.WEAPON_SLOT]);
                        equipment.setItem(Equipment.WEAPON_SLOT, new Item(-1));
                        equipment.setItem(Equipment.SHIELD_SLOT, item);
                        resetWeapon(player);
                    } else if (item.getDefinition().getEquipmentSlot() == equipItem.getDefinition().getEquipmentSlot()
                            && equipItem.getId() != -1) {
                        if (player.getInventory().contains(equipItem.getId())) {
                            player.getInventory().delete(item);
                            player.getInventory().add(equipItem);
                        } else
                            player.getInventory().setItem(slot, equipItem);
                        equipment.setItem(equipmentSlot, item);
                    } else {
                        player.getInventory().setItem(slot, new Item(-1, 0));
                        equipment.setItem(item.getDefinition().getEquipmentSlot(), item);
                    }
                    if (equipmentSlot == Equipment.WEAPON_SLOT) {
                        resetWeapon(player);
                    }

                    if (equipment.get(Equipment.WEAPON_SLOT).getId() != 4153) {
                        player.getCombatBuilder().cooldown(false);
                    }

                    player.setCastSpell(null);
                    BonusManager.update(player);
                    equipment.refreshItems();
                    player.getInventory().refreshItems();
                    player.getUpdateFlag().flag(Flag.APPEARANCE);
                    Sounds.sendSound(player, Sound.EQUIP_ITEM);
                }
            }
        }
        return true;
    }

    public static void resetWeapon(Player player) {
        Equipment equipment;
        if(player.isSecondaryEquipment()) {
            equipment = player.getSecondaryEquipment();
        } else {
            equipment = player.getEquipment();
        }
        Item weapon = equipment.get(Equipment.WEAPON_SLOT);

        WeaponInterfaces.assign(player, weapon);
        WeaponAnimations.update(player);

        if (AutoCastSpell.getAutoCastSpell(player) == null) {
            if (player.getAutocastSpell() != null || player.isAutocast()) {
                Autocasting.resetAutocast(player, true);
            }
        } else {
            player.setAutocastSpell(AutoCastSpell.getAutoCastSpell(player).getSpell());
        }

        if (weapon.getId() == 4024) {
            player.setNpcTransformationId(3000);
            player.getStrategy(3000);
            player.getUpdateFlag().flag(Flag.APPEARANCE);
        }

        player.setSpecialActivated(false);
        CombatSpecial.updateBar(player);
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getConstitution() <= 0)
            return;
        int id = packet.readShort();
        int slot = packet.readShortA();
        int interfaceId = packet.readShortA();
        if (player.getInterfaceId() > 0 && player.getInterfaceId() != 21172 /* EQUIP SCREEN */) {
            player.getPacketSender().sendInterfaceRemoval();
            // return;
        }
        if (player.aonBoxItem > 0) {
            player.sendMessage("Please choose to keep or gamble your item before doing this!");
            return;
        }

        equipItem(player, id, slot, interfaceId);

    }
}