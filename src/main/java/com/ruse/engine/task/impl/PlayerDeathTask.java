package com.ruse.engine.task.impl;

import com.ruse.GameSettings;
import com.ruse.engine.task.Task;
import com.ruse.model.*;
import com.ruse.model.Locations.Location;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.WorldIPChecker;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.impl.Ironman;
import com.ruse.world.packages.mode.impl.UltimateIronman;

/**
 * Represents a player's death task, through which the process of dying is
 * handled, the animation, dropping items, etc.
 *
 * @author relex lawl, redone by Gabbe.
 */

public class PlayerDeathTask extends Task {

    /**
     * The PlayerDeathTask constructor.
     *
     * @param player The player setting off the task.
     */
    public PlayerDeathTask(Player player) {
        super(1, player, false);
        this.player = player;
    }

    private Player player;
    private int ticks = 5;
    Position oldPosition;
    Location loc;
    NPC death;

    @Override
    public void execute() {
        if (player == null) {
            stop();
            return;
        }
        try {
            switch (ticks--) {
                case 5 -> {
                    if(handleUrns(player)){
                        this.stop();
                        return;
                    }
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getMovementQueue().setLockMovement(true).reset();
                }
                case 4 -> {
                    if (player.getInstance() != null) {
                        player.getInstance().remove(player);
                    }
                }
                case 3 -> {
                    this.oldPosition = player.getPosition().copy();
                    this.loc = player.getLocation();
                    player.getPacketSender().sendInterfaceRemoval();
                    player.setEntityInteraction(null);
                    player.getMovementQueue().setFollowCharacter(null);
                    player.getCombatBuilder().cooldown(false);
                    player.setTeleporting(false);
                    player.setWalkToTask(null);
                    player.getSkillManager().stopSkilling();
                }
                case 2 -> death = getDeathNpc(player);
                case 0 -> {

                    if (player.getMode() instanceof UltimateIronman) {
                        player.getMode().changeMode(new Ironman());
                        player.sendMessage("Your account has been converted to an Ironman account.");
                    }

                    WorldIPChecker.getInstance().leaveContent(player);
                    player.moveTo(GameSettings.DEFAULT_POSITION.copy());
                    player.restart();
                    player.getUpdateFlag().flag(Flag.APPEARANCE);
                    loc.onDeath(player);
                    player = null;
                    oldPosition = null;
                    if(death != null)
                        World.deregister(death);

                    stop();
                }
            }
        } catch (Exception e) {
            setEventRunning(false);
            e.printStackTrace();
            if (player != null) {
                player.moveTo(GameSettings.DEFAULT_POSITION.copy());
                if (player.isGodMode()) {
                    return;
                }
                player.setConstitution(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
            }
        }
    }

    public static NPC getDeathNpc(Player player) {
        NPC death = new NPC(2862, new Position(player.getPosition().getX() + 1, player.getPosition().getY() + 1));
        World.register(death);
        death.setEntityInteraction(player);
        death.performAnimation(new Animation(401));
        death.forceChat(randomDeath(player.getUsername()));
        return death;
    }

    public static String randomDeath(String name) {
        return switch (Misc.getRandom(8)) {
            case 0 -> "There is no escape, " + Misc.formatText(name) + "...";
            case 1 -> "Muahahahaha!";
            case 2 -> "You belong to me!";
            case 3 -> "Beware mortals, " + Misc.formatText(name) + " travels with me!";
            case 4 -> "Your time here is over, " + Misc.formatText(name) + "!";
            case 5 -> "Now is the time you die, " + Misc.formatText(name) + "!";
            case 6 -> "I claim " + Misc.formatText(name) + " as my own!";
            case 7 -> Misc.formatText(name) + " is mine!";
            case 8 -> "Say goodbye, " + Misc.formatText(name) + "!";
            case 9 -> "I have come for you, " + Misc.formatText(name) + "!";
            default -> "";
        };
    }

    private static boolean handleUrns(Player player){
        if(player.getInventory().contains(20425, 1)){
            player.getInventory().delete(20425, 1);
            player.restart();
            player.sendMessage("Your Infinity Urn has revived you and has striked all NPC's around you!");
            for(NPC npc : player.getLocalNpcs()){
                if(npc != null && npc.getConstitution() > 0){
                    npc.performGraphic(new Graphic(665));
                    npc.dealDamage(new Hit(Misc.random(1000000, 50000000)));
                }
            }
            return true;
        }
        if(player.getInventory().contains(20419, 1)){
            player.getInventory().delete(20419, 1);
            player.restart();
            player.sendMessage("Your Accursed Urn has revived you!");
            player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                    player.getSkillManager().getCurrentLevel(Skill.PRAYER)
                            / 2);
            return true;
        }
        if(player.getInventory().contains(20413, 1)){
            player.getInventory().delete(20413, 1);
            player.restart();
            player.sendMessage("Your Ceremonial Urn has revived you!");
            player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                    player.getSkillManager().getCurrentLevel(Skill.PRAYER)
                            / 2);
            player.setConstitution(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) / 2);
            return true;
        }
        return false;
    }

}
