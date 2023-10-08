package com.ruse.world.packages.skills.mining;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Animation;
import com.ruse.model.GameObject;
import com.ruse.model.Item;
import com.ruse.model.Skill;
import com.ruse.util.Misc;
import com.ruse.world.content.CustomObjects;
import com.ruse.world.content.Sounds;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class Miner {

    public static void startMining(final @NotNull Player player, final GameObject oreObject){
        if(player.getSkillManager().getIsSkilling()){
            return;
        }

        player.getSkillManager().stopSkilling();
        player.getPacketSender().sendInterfaceRemoval();

        if (player.busy() || player.getCombatBuilder().isBeingAttacked() || player.getCombatBuilder().isAttacking()) {
            player.getPacketSender().sendMessage("You cannot do that right now.");
            return;
        }
        if (player.getInventory().getFreeSlots() == 0) {
            player.getPacketSender().sendMessage("You do not have any free inventory space left.");
            return;
        }

        player.setInteractingObject(oreObject);
        player.setPositionToFace(oreObject.getPosition());

        final MiningProps.Rocks rock = MiningProps.Rocks.forId(oreObject.getId());
        if(rock != null){
            final int pickaxe = MiningProps.getPickaxe(player);
            final int miningLevel = player.getSkillManager().getCurrentLevel(Skill.MINING);

            if (pickaxe > 0) {
                if (miningLevel >= rock.getReq()) {

                    final MiningProps.PickAxe axe = MiningProps.PickAxe.forId(pickaxe);
                    if (axe != null) {
                        if(miningLevel >= axe.getReq()){
                            player.performAnimation(new Animation(12003));
                            player.getSkillManager().setIsSkilling(true);

                            player.setCurrentTask(new Task(Math.max((int) (rock.getTicks() / axe.getSpeed()), 1), player, false) {

                                int cycle = 0;
                                int amount = 0;
                                final int reqCycle = Misc.getRandom(rock.getTicks() - 1);
                                @Override
                                protected void execute() {
                                    if (player.getInteractingObject() == null
                                            || player.getInteractingObject().getId() != oreObject.getId()) {
                                        player.getSkillManager().stopSkilling();
                                        player.performAnimation(new Animation(65535));
                                        stop();
                                        return;
                                    }
                                    if (player.getInventory().getFreeSlots() == 0) {
                                        player.performAnimation(new Animation(65535));
                                        stop();
                                        player.getPacketSender()
                                                .sendMessage("You do not have any free inventory space left.");
                                        return;
                                    }

                                    if (cycle != reqCycle) {
                                        cycle++;
                                        player.performAnimation(new Animation(12003));
                                    } else {


                                        if(rock == MiningProps.Rocks.AFK || rock == MiningProps.Rocks.AFK1
                                        ||rock == MiningProps.Rocks.AFK2 || rock == MiningProps.Rocks.AFK3){
                                            if(Misc.random(5) == 1) {
                                                amount = (int) (1 * axe.getBonus());
                                                player.getInventory().add(new Item(5020, amount));
                                                player.sendMessage("You manage to mine x" + amount + " AFK tickets.");
                                            }
                                        } else {
                                            final Item reward = rock.getMaterialIds()[Misc.random(rock.getMaterialIds().length - 1)].copy();
                                            amount = (int) (reward.getAmount() * axe.getBonus());
                                            player.getInventory().add(new Item(reward.getId(), amount));
                                            player.sendMessage("You manage to mine x"+amount+" " + reward.getDefinition().getName() + ".");
                                        }

                                        player.getSkillManager().addExperience(Skill.MINING, rock.getXp());
                                        player.getSkillManager().stopSkilling();
                                        stop();

                                        Sounds.sendSound(player, Sounds.Sound.MINE_ITEM);

                                        if (rock.getTimer() > 0) {
                                            player.performAnimation(new Animation(65535));
                                            oreRespawn(player, oreObject, rock);
                                        } else {
                                            player.performAnimation(new Animation(65535));
                                            startMining(player, oreObject);
                                        }

                                    }

                                }

                            });
                            TaskManager.submit(player.getCurrentTask());
                        } else {
                            player.sendMessage("You do not have the required Mining level to use this pickaxe.");
                        }
                    } else {
                        player.sendMessage("You do not have the required pickaxe to mine.");
                    }

                } else {
                    player.sendMessage("You need a Mining level of at least " + rock.getReq() + " to mine this rock.");
                }
            } else {
                player.sendMessage("You do not have a pickaxe that you can use.");
            }
        }
    }

    public static void oreRespawn(final Player player, final GameObject oldOre, MiningProps.Rocks o) {
        if (oldOre == null || oldOre.getPickAmount() >= 1)
            return;
        oldOre.setPickAmount(1);
        for (Player players : player.getLocalPlayers()) {
            if (players == null)
                continue;
            if (players.getInteractingObject() != null && players.getInteractingObject().getPosition()
                    .equals(player.getInteractingObject().getPosition().copy())) {
                players.getPacketSender().sendClientRightClickRemoval();
                players.getSkillManager().stopSkilling();
            }
        }
        player.getPacketSender().sendClientRightClickRemoval();
        player.getSkillManager().stopSkilling();
        CustomObjects.globalObjectRespawnTask(new GameObject(452, oldOre.getPosition().copy(), 10, 0), oldOre,
                o.getTimer());
    }
}
