//package com.ruse.world.content;
//
//import com.ruse.engine.task.Task;
//import com.ruse.engine.task.TaskManager;
//import com.ruse.model.*;
//import com.ruse.model.container.impl.Equipment;
//import com.ruse.model.definitions.WeaponAnimations;
//import com.ruse.model.definitions.WeaponInterfaces;
//import com.ruse.net.PlayerSession;
//import com.ruse.util.NameUtils;
//import com.ruse.world.World;
//import com.ruse.world.entity.impl.player.Player;
//
//import java.util.Arrays;
//import java.util.Objects;
//
//public class MiniMeSystem {
//
//    private final Player player;
//
//    private Player miniMe;
//
//    public MiniMeSystem(Player player) {
//        this.player = player;
//    }
//
//    public void spawn() {
//        if (miniMe == null) {
//            createMinime();
//        } else {
//            player.sendMessage("Your Mini-me is already alive.");
//        }
//        World.getLoginQueue().add(miniMe);
//    }
//
//    public Player getMiniMe() {
//        return miniMe;
//    }
//
//    public void despawn() {
//        if (miniMe == null) return;
//        unequipAll();
//        TaskManager.cancelTasks(miniMe.getCombatBuilder());
//        TaskManager.cancelTasks(this);
//        World.getLogoutQueue().add(miniMe);
//        miniMe = null;
//    }
//
//
//    private void createMinime() {
//        if (miniMe != null) {
//            return;
//        }
//        PlayerSession minimeSession = new PlayerSession(null);
//        miniMe = new Player(minimeSession);
//        miniMe.setMiniPlayer(true);
//        miniMe.setUsername("mini " + player.getUsername());
//        miniMe.setLongUsername(NameUtils.stringToLong("mini " + player.getUsername()));
//        miniMe.setHostAddress(player.getHostAddress());
//        miniMe.setSerialNumber(player.getSerialNumber());
//        miniMe.setPosition(new Position(player.getPosition().getX() - 1, player.getPosition()
//                .getY()));
//        miniMe.setAnimation(player.getAnimation());
//        miniMe.setCharacterAnimations(player.getCharacterAnimations().clone());
//        miniMe.getMovementQueue().setFollowCharacter(player);
//        miniMe.setOwner(player);
//        miniMe.getUpdateFlag().flag(Flag.APPEARANCE);
//        player.getUpdateFlag().flag(Flag.APPEARANCE);
//        miniMe.setMini(true);
//        equipAll();
//        TaskManager.submit(new Task(1, this, true) {
//            @Override
//            protected void execute() {
//            	if(miniMe == null || player == null) {
//            		stop();
//            		return;
//            	}
//                    miniMe.getMovementQueue().setFollowCharacter(player);
//                }
//            }
//        );
//    }
//
//
//    public void unequipAll() {
//        if (miniMe == null) {
//            return;
//        }
//        if (player.getInventory().getFreeSlots() < 15) {
//            player.sendMessage("@red@You should have at least 15 inventory spaces free to do this.");
//            return;
//        }
//        for (int i = 0; i < player.getMinimeEquipment().length; i++) {
//            if (player.getMinimeEquipment()[i] != null && player.getMinimeEquipment()[i].getId() >= 0) {
//                player.getInventory().add(player.getMinimeEquipment()[i].copy());
//            }
//            player.setMinimeEquipment(new Item(-1), i);
//        }
//        miniMe.getEquipment().resetItems();
//        miniMe.getUpdateFlag().flag(Flag.APPEARANCE);
//    }
//
//    private void equipAll() {
//        for (Item item : player.getMinimeEquipment()) {
//            if (item != null && item.getId() != -1) {
//                equip(item, false);
//            }
//        }
//    }
//
//    public void equip(Item item, boolean check) {
//        if (miniMe == null) {
//            return;
//        }
//        Item copy = item.copy();
//        player.getInventory().delete(item);
//        int slot = copy.getDefinition().getEquipmentType().getSlot();
//        if (check && player.getMinimeEquipment()[slot] != null && player.getMinimeEquipment()[slot].getId() >= 0) {
//            player.getInventory().add(player.getMinimeEquipment()[slot]);
//        }
//        miniMe.getEquipment().setItem(slot, copy);
//        player.getMinimeEquipment()[slot] = copy;
//        miniMe.getUpdateFlag().flag(Flag.APPEARANCE);
//
//        Item weapon = miniMe.getEquipment().get(Equipment.WEAPON_SLOT);
//       // System.out.println(weapon);
//        if (weapon != null && weapon.getId() >= 0) {
//            WeaponInterfaces.assign(miniMe, weapon);
//            WeaponAnimations.assign(miniMe, weapon);
//        }
//
//        miniMe.getUpdateFlag().flag(Flag.APPEARANCE);
//    }
//
//    public void targetPlayer() {
//        if (miniMe == null) {
//            return;
//        }
//        resetAttack();
//        Position newPos = new Position(player.getPosition().getX() - 1, player.getPosition()
//                .getY(), player.getPosition().getZ());
//        miniMe.moveTo(newPos);
//        miniMe.getMovementQueue().setFollowCharacter(player);
//    }
//
//
//    public void onLogin() {
//        boolean hasItems = Arrays.stream(player.getMinimeEquipment())
//                .filter(Objects::nonNull)
//                .anyMatch(item -> item.getId() >= 0);
//       // System.out.println("has items: " + hasItems);
//        if (hasItems) {
//            spawn();
//        }
//    }
//
//    private void resetAttack() {
//        if (miniMe == null) {
//            return;
//        }
//        miniMe.getCombatBuilder().reset(true);
//    }
//}