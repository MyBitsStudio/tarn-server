package com.ruse.world.packages.raids.firefight.npc.one.strategy;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.*;
import com.ruse.util.Misc;
import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.packages.raids.firefight.FireFightRaid;
import com.ruse.world.packages.raids.firefight.npc.one.FireFightRoomOneBoss;
import com.ruse.world.packages.raids.firefight.npc.one.FireFightRoomOneMinion;
import com.ruse.world.packages.raids.firefight.rooms.FireFightRoomOne;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.ruse.world.packages.raids.firefight.FireFightConstants.*;

public class FFOneBossStrategy implements CombatStrategy {

    @Override
    public boolean canAttack(Character entity, @NotNull Character victim) {
        return victim.isPlayer() && victim.asPlayer().getRaid() != null;
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(Character entity, @NotNull Character victim) {
        NPC npc = (NPC) entity;
        if (victim.getConstitution() <= 0) {
            return true;
        }
        if (npc.isChargingAttack()) {
            return true;
        }
        if (!victim.isPlayer()) {
            return true;
        }
        Player player = (Player) victim;

        if(player.getRaid() != null){
            FireFightRaid raid = (FireFightRaid) player.getRaid();
            FireFightRoomOneBoss boss = (FireFightRoomOneBoss) entity;
            FireFightRoomOne room = (FireFightRoomOne) boss.getRoom();

            if(raid.getCurrentRoom() != boss.getRoom()){
                return true;
            }

            int count = room.getAliveMinions();
            FireFightRoomOneMinion minion = room.getMinions()[Misc.random(count - 1)];
            int random = Misc.random(count + 1);
            int physical = Misc.random(2);

            if(random > count){
                if(minion != null){
                    minion.setGraphic(MINION_ONE_GRAPHIC);
                    minion.getCombatBuilder().attack(player);
                }
                finalBurst(boss, player);

            } else {
                switch(count){
                    case 0:
                        switch(physical){
                            case 0:
                            case 2:
                                finalBurst(boss, player);
                                break;
                            case 1:
                                flurry(boss);
                                break;
                        }
                        break;
                    case 1:
                        switch(physical){
                            case 0:
                                if(minion != null)
                                    minionHeal(boss, minion, player);
                                else
                                    finalBurst(boss, player);
                                break;
                            case 1:
                                if(minion != null)
                                    minionAssist(boss, minion);
                                else
                                    flurry(boss);
                                break;
                            case 2:
                                finalBurst(boss, player);
                                break;
                        }
                        break;
                    case 2:
                        switch(physical){
                            case 0:
                                if(minion != null)
                                    minionHeal(boss, minion, player);
                                else
                                    flurry(boss);
                                break;
                            case 1:
                                if(minion != null)
                                    minionAssist(boss, minion);
                                else
                                    finalBurst(boss, player);
                                break;
                            case 2:
                                if(minion != null)
                                    minionAssist(boss, minion);
                                else
                                    flurry(boss);
                                break;
                        }
                        break;
                    case 3:
                        switch(physical){
                            case 0:
                                if(minion != null)
                                    minionHeal(boss, minion, player);
                                else
                                    flurry(boss);
                                break;
                            case 1:
                            case 2:
                                if(minion != null)
                                    minionAssist(boss, minion);
                                else
                                    flurry(boss);
                                break;
                        }
                        break;
                }
            }

        }

        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return 9;
    }

    @Override
    public int attackDistance(Character entity) {
        return 12;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }

    private void finalBurst(@NotNull FireFightRoomOneBoss entity, Player victim){
        entity.forceChat("Final Burst!");
        entity.performAnimation(BOSS_ONE_FINAL_ANIMATION);
        entity.performGraphic(BOSS_ONE_FINAL_GRAPHIC);
        entity.getCombatBuilder()
                .setContainer(new CombatContainer(entity, victim, 1, 3, CombatType.MAGIC, true));
        TaskManager.submit(new Task(1, entity, false) {

            int tick = 0;

            @Override
            protected void execute() {
                if(tick == 0){
                    new Projectile(entity, victim, 2706, 44, 3, 43, 43, 0).sendProjectile();
                }

                if(tick == 2){
                    entity.performAnimation(BOSS_ONE_FINAL_ANIMATION);
                    for(Player player : entity.getRoom().getPlayers()){
                        if(player != null && player.getPosition().isWithinDistance(entity.getPosition(), 5)){
                            player.dealDamage(new Hit(100, Hitmask.RED, CombatIcon.NONE));
                        }
                    }
                }

                if(tick == 4){
                    entity.performAnimation(BOSS_ONE_FINAL_ANIMATION);
                    for(Player player : entity.getRoom().getPlayers()){
                        if(player != null && player.getPosition().isWithinDistance(entity.getPosition(), 3)){
                            player.dealDamage(new Hit(100, Hitmask.RED, CombatIcon.NONE));
                        }
                    }
                }

                if(tick == 6){
                    entity.performAnimation(BOSS_ONE_FINAL_ANIMATION);
                    for(Player player : entity.getRoom().getPlayers()){
                        if(player != null && player.getPosition().isWithinDistance(entity.getPosition(), 1)){
                            player.dealDamage(new Hit(100, Hitmask.RED, CombatIcon.NONE));
                        }
                    }
                }

                if(tick == 8){
                    entity.setChargingAttack(false).getCombatBuilder()
                            .setAttackTimer(0);
                    stop();
                }


                tick++;
            }
        });
    }

    private void minionHeal(@NotNull FireFightRoomOneBoss entity, FireFightRoomOneMinion minion, Player player){
        entity.forceChat("Minion Heal!");
        entity.performAnimation(BOSS_ONE_HEAL_ANIMATION);
        entity.performGraphic(BOSS_ONE_MINION_HEAL);
        entity.getCombatBuilder()
                .setContainer(new CombatContainer(entity, player, 1, 3, CombatType.MAGIC, true));
        TaskManager.submit(new Task(1, entity, false) {

            int tick = 0;

            @Override
            protected void execute() {
                if(tick == 0){
                    new Projectile(entity, player, 2706, 44, 3, 43, 43, 0).sendProjectile();
                }

                if(tick == 3){
                    player.dealDamage(new Hit(100, Hitmask.DARK_RED, CombatIcon.NONE));
                    new Projectile(player, minion, 2706, 44, 3, 43, 43, 0).sendProjectile();
                }

                if(tick == 6){
                    minion.setGraphic(BOSS_ONE_MINION_HEAL);
                    minion.heal(100000L * entity.getRoom().getPlayers().size());
                }

                if(tick == 8){
                    entity.setChargingAttack(false).getCombatBuilder()
                            .setAttackTimer(0);
                    stop();
                }

                tick++;
            }
        });
    }

    private void flurry(@NotNull FireFightRoomOneBoss entity){
        entity.performAnimation(BOSS_ONE_FLURRY_ANIMATION);
        entity.performGraphic(BOSS_ONE_FLURRY_GRAPHIC);

    }

    private void minionAssist(@NotNull FireFightRoomOneBoss entity, FireFightRoomOneMinion minion){
        entity.performAnimation(BOSS_ONE_ASSIST_ANIMATION);
        entity.performGraphic(BOSS_ONE_ASSIST_MINION);
    }

}
