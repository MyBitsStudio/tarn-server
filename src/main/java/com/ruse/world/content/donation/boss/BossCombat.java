package com.ruse.world.content.donation.boss;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.*;
import com.ruse.util.Misc;
import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.donation.DonationManager;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

public class BossCombat implements CombatStrategy {

    public static Position[] minionPos = {
            new Position(2537, 2639, 4), new Position(2539, 2628, 4),
            new Position(2520, 2628, 4), new Position(2518, 2636, 4),
    };

    @Override
    public boolean canAttack(Character entity, @NotNull Character victim) {
        return victim.isPlayer();
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(Character entity, @NotNull Character victim) {
        DonationBoss boss = (DonationBoss) entity;
        if (victim.getConstitution() <= 0) {
            return true;
        }
        if (boss.isChargingAttack()) {
            return true;
        }
        if (!victim.isPlayer()) {
            return true;
        }
        Player player = (Player) victim;

        int x = Misc.random(4);

        if(x == 0){

            if(Misc.random(1) == 0) {
                Position pos = minionPos[Misc.random(minionPos.length - 1)];
                boss.performAnimation(new Animation(boss.getDefinition().getAttackAnimation()));
                DonationManager.getInstance().spawnMinion(pos);
                boss.forceChat("Minion, rise and attack!");
            } else {
                enhancedHits(boss);
            }

        }  else if (x == 1) {
            if (Locations.goodDistance(boss.copy(), victim.copy(), 1)) {
                int b = Misc.random(2);
                if(b == 0){
                    earthquake(boss);
                } else if(b == 2){
                    enhancedHits(boss);
                } else{
                    meleeAttack(boss, player);
                }
            }
        } else if (x == 3) {
            smackThem(boss);
        } else if (x == 4) {
            int y = Misc.random(2);
            if (y == 0) {
                runForYourLife(boss);
            } else {
                enhancedHits(boss);
            }
        } else {
            mageThemAll(boss);
        }
        return true;
    }

    private void mageThemAll(@NotNull DonationBoss boss){
        boss.setChargingAttack(true);
        boss.performAnimation(new Animation(boss.getDefinition().getAttackAnimation()));
        boss.forceChat("Check out this spell");
        TaskManager.submit(new Task(1, boss, false) {
            int tick = 0;

            @Override
            public void execute() {
                if(tick == 1){
                    for(Player player : boss.getClosePlayers(10)){
                        boss.getCombatBuilder().setContainer(new CombatContainer(boss, player, 1, 0, CombatType.MAGIC, false));
                        player.getPacketSender().sendGlobalGraphic(new Graphic(988), player.getPosition());
                    }
                }
                if (tick == 4) {
                    boss.setChargingAttack(false);
                    stop();
                }
                tick++;
            }
        });
    }

    private void runForYourLife(@NotNull DonationBoss boss){
        boss.setChargingAttack(true);
        boss.performAnimation(new Animation(boss.getDefinition().getAttackAnimation()));
        boss.forceChat("You think you can run?");
        TaskManager.submit(new Task(1, boss, false) {
            int tick = 0;

            @Override
            public void execute() {
                if(tick == 1 || tick == 4 || tick == 7){
                    Player player = boss.getClosePlayers(10).get(Misc.random(boss.getClosePlayers(10).size() - 1));
                    boss.getCombatBuilder().setContainer(new CombatContainer(boss, player, 1, 0, CombatType.MAGIC, false));
                    player.getPacketSender().sendGlobalGraphic(new Graphic(76), player.getPosition());
                }
                if (tick == 9) {
                    boss.setChargingAttack(false);
                    stop();
                }
                tick++;
            }
        });
    }

    private void smackThem(@NotNull DonationBoss boss){
        boss.setChargingAttack(true);
        boss.forceChat("You won't see this one coming");
        ObjectArrayList<Player> players = boss.getClosePlayers(10);
        ObjectArrayList<Player> players2 = new ObjectArrayList<>();
        TaskManager.submit(new Task(1, boss, false) {
            int tick = 0;

            @Override
            public void execute() {
                if(tick == 1 || tick == 3 ||  tick == 5 ){
                    if(players.size() == 1){
                        boss.setChargingAttack(false);
                        stop();
                    } else {
                        Player player = players.get(Misc.random(players.size() - 1));
                        if (players2.contains(player)) {
                            player = players.get(Misc.random(players.size() - 1));
                        }
                        players.remove(player);
                        players2.add(player);
                        player.getPacketSender().sendGlobalGraphic(new Graphic(912), player.getPosition());
                    }
                }
                if(tick == 8 || tick == 6){
                    for(Player player : players2){
                        if(!player.getPosition().equals(boss.getPosition())) {
                            boss.getCombatBuilder().setContainer(new CombatContainer(boss, player, 1, 0, CombatType.MAGIC, false));
                            player.getPacketSender().sendGlobalGraphic(new Graphic(912), player.getPosition());
                        }
                    }
                }
                if (tick == 10) {
                    boss.setChargingAttack(false);
                    stop();
                }
                tick++;
            }
        });

    }

    private void enhancedHits(@NotNull DonationBoss boss){
        ObjectArrayList<Player> players = boss.getClosePlayers(10);
        if(players.size() >= 2) {
            boss.getCombatBuilder().setContainer(new CombatContainer(boss, players.get(0), 2, 0, CombatType.MELEE, false));
            boss.getCombatBuilder().setContainer(new CombatContainer(boss, players.get(1), 2, 0, CombatType.MELEE, true));
            boss.setChargingAttack(true);
            boss.performAnimation(new Animation(boss.getDefinition().getAttackAnimation()));

        } else {
            boss.getCombatBuilder().setContainer(new CombatContainer(boss, players.get(0), 2, 0, CombatType.MELEE, false));
            boss.getCombatBuilder().setContainer(new CombatContainer(boss, players.get(0), 2, 0, CombatType.MELEE, true));
            boss.setChargingAttack(true);
            boss.performAnimation(new Animation(boss.getDefinition().getAttackAnimation()));

        }

        TaskManager.submit(new Task(1, boss, true) {
            int tick = 0;

            @Override
            public void execute() {
                if (tick == 2) {
                    boss.setChargingAttack(false);
                    stop();
                }
                tick++;
            }
        });
    }

    private void earthquake(@NotNull DonationBoss boss){
        boss.setChargingAttack(true);
        boss.performAnimation(new Animation(boss.getDefinition().getAttackAnimation())); // new anim

        TaskManager.submit(new Task(1, boss, false) {
            int tick = 0;

            @Override
            public void execute() {
                if(tick == 1){
                    boss.forceChat("You think I can't make the very earth quake?");
                }
                if(tick == 3){
                    boss.forceChat("You're wrong!");
                }
                if(tick == 4 || tick == 6){
                    for(Player player : boss.getClosePlayers(10)){
                        boss.getCombatBuilder().setContainer(new CombatContainer(boss, player, 1, 0, CombatType.MAGIC, false));
                    }
                }

                if(tick == 8){
                    for(Player player : boss.getClosePlayers(10)){
                        player.getPacketSender().sendInterfaceRemoval();
                    }
                    boss.setChargingAttack(false);
                    stop();
                }
                tick++;
            }
        });



    }

    private void meleeAttack(@NotNull DonationBoss boss, Player player) {
        boss.getCombatBuilder().setContainer(new CombatContainer(boss, player, 1, 0, CombatType.MELEE, false));
        boss.setChargingAttack(true);
        boss.performAnimation(new Animation(boss.getDefinition().getAttackAnimation()));


        TaskManager.submit(new Task(1, boss, false) {
            int tick = 0;

            @Override
            public void execute() {
                if (tick == 2) {
                    boss.setChargingAttack(false);
                    stop();
                }
                tick++;
            }
        });
    }

    @Override
    public int attackDelay(Character entity) {
        return 4;
    }

    @Override
    public int attackDistance(Character entity) {
        return 8;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}
