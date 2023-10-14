package com.ruse.world.packages.combat.scripts.npc.master;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.*;
import com.ruse.util.Misc;
import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.packages.combat.prayer.CurseHandler;
import com.ruse.world.packages.combat.prayer.PrayerHandler;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.AnimGFX;
import org.jetbrains.annotations.NotNull;

public class ZeidanCombat implements CombatStrategy {
    @Override
    public boolean canAttack(Character entity, @NotNull Character victim) {
        return victim.isPlayer();
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(@NotNull Character entity, @NotNull Character victim) {

        NPC npc = entity.toNpc();
        Player player = victim.asPlayer();

        if (npc.isChargingAttack() || victim.getConstitution() <= 0) {
            return true;
        }

        int chance = Misc.getRandom(0, 5);
        if(chance == 4){
            disable(npc, player);
        } else if(chance == 3){
            sendLightning(npc, player);
        } else if(chance == 1){
            cosmicBurst(npc, player);
        } else if(chance == 0){
            destructiveBlow(npc, player);
        } else {
            npc.performAnimation(new Animation(AnimGFX.BACK_HAND_MELEE));
            npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 2, 1, CombatType.MELEE, true));
        }

        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return 7;
    }

    @Override
    public int attackDistance(Character entity) {
        return 14;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }

    private void disable(@NotNull NPC npc, Player player){
        npc.performGraphic(new Graphic(AnimGFX.SKELETON_FREAKOUT));

        TaskManager.submit(new Task(true){
            @Override
            protected void execute() {
                player.performGraphic(new Graphic(AnimGFX.SKELETON_FREAKOUT));
                CurseHandler.deactivateCurses(player);
                PrayerHandler.deactivateAll(player);
                int total = player.getSkillManager().getCurrentLevel(Skill.PRAYER) - 75;
                player.getSkillManager().setCurrentLevel(Skill.PRAYER, total, true);
                player.performGraphic(new Graphic(AnimGFX.WHITE_CRYSTAL_MAGIC));
                player.dealDamage(new Hit(player.getConstitution() / 3, Hitmask.LIGHT_YELLOW, CombatIcon.MAGIC));
                this.stop();
            }
        });
    }

    private void sendLightning(@NotNull NPC npc, Player player){
        npc.performAnimation(new Animation(AnimGFX.CAST_SPELL_UP_MAGIC));
        npc.performGraphic(new Graphic(AnimGFX.WHITE_CRYSTAL_MAGIC));

        npc.setChargingAttack(true);

        TaskManager.submit(new Task(2, false){

            @Override
            protected void execute() {
                Position[] positions = new Position[16];
                for(int i = 0; i < 4; i++){
                    for(int j = 0; j < 4; j++){
                        positions[i * 4 + j] = new Position(npc.getPosition().getX() + i, npc.getPosition().getY() + j, player.getPosition().getZ());
                    }
                }
                for(Position position : positions){
                    if(position.equals(npc.getPosition()))
                        continue;

                    new Projectile(npc.getPosition(), position, 0, 665, 44, 1, 43, 31, 0).sendProjectile();
                    if(position.equals(player.getPosition())){
                        player.performGraphic(new Graphic(AnimGFX.WHITE_CRYSTAL_MAGIC));
                        player.dealDamage(new Hit(Misc.getRandom(20, 300), Hitmask.LIGHT_YELLOW, CombatIcon.MAGIC));
                    }
                }
                positions = new Position[16];
                for(int i = 0; i < 4; i++){
                    for(int j = 0; j < 4; j++){
                        positions[i * 4 + j] = new Position(npc.getPosition().getX() - i, npc.getPosition().getY() - j, player.getPosition().getZ());
                    }
                }
                for(Position position : positions){
                    if(position.equals(npc.getPosition()))
                        continue;

                    new Projectile(npc.getPosition(), position, 0, 665, 44, 1, 43, 31, 0).sendProjectile();
                    if(position.equals(player.getPosition())){
                        player.performGraphic(new Graphic(AnimGFX.WHITE_CRYSTAL_MAGIC));
                        player.dealDamage(new Hit(Misc.getRandom(20, 300), Hitmask.LIGHT_YELLOW, CombatIcon.MAGIC));
                    }
                }
                positions = new Position[16];
                for(int i = 0; i < 4; i++){
                    for(int j = 0; j < 4; j++){
                        positions[i * 4 + j] = new Position(npc.getPosition().getX() + i, npc.getPosition().getY() - j, player.getPosition().getZ());
                    }
                }
                for(Position position : positions){
                    if(position.equals(npc.getPosition()))
                        continue;

                    new Projectile(npc.getPosition(), position, 0, 665, 44, 1, 43, 31, 0).sendProjectile();
                    if(position.equals(player.getPosition())){
                        player.performGraphic(new Graphic(AnimGFX.WHITE_CRYSTAL_MAGIC));
                        player.dealDamage(new Hit(Misc.getRandom(20, 300), Hitmask.LIGHT_YELLOW, CombatIcon.MAGIC));
                    }
                }
                positions = new Position[16];
                for(int i = 0; i < 4; i++){
                    for(int j = 0; j < 4; j++){
                        positions[i * 4 + j] = new Position(npc.getPosition().getX() - i, npc.getPosition().getY() + j, player.getPosition().getZ());
                    }
                }
                for(Position position : positions){
                    if(position.equals(npc.getPosition()))
                        continue;

                    new Projectile(npc.getPosition(), position, 0, 665, 44, 1, 43, 31, 0).sendProjectile();
                    if(position.equals(player.getPosition())){
                        player.performGraphic(new Graphic(AnimGFX.WHITE_CRYSTAL_MAGIC));
                        player.dealDamage(new Hit(Misc.getRandom(20, 300), Hitmask.LIGHT_YELLOW, CombatIcon.MAGIC));
                    }
                }
                npc.setChargingAttack(false);
                this.stop();
            }
        });
    }

    private void cosmicBurst(@NotNull NPC npc, Player player){
        npc.performAnimation(new Animation(AnimGFX.CHARGE_SPELL_MAGIC));
        npc.performGraphic(new Graphic(AnimGFX.PURPLE_PORTAL_UP));

        npc.setChargingAttack(true);

        TaskManager.submit(new Task(2, false){

            @Override
            protected void execute() {
                if(player.getPosition().getDistance(npc.getPosition()) <= 1){
                    npc.performAnimation(new Animation(AnimGFX.SLASH_DOWN_MELEE));
                    player.performGraphic(new Graphic(AnimGFX.PURPLE_PORTAL_UP));
                    npc.getCombatBuilder().setContainer(new CombatContainer(npc, player, 2, 1, CombatType.MELEE, true));
                } else {
                    npc.performAnimation(new Animation(AnimGFX.CAST_SPELL_MAGIC));
                    new Projectile(npc.getPosition(), player.getPosition(), 0, 1901, 44, 1, 43, 31, 0).sendProjectile();
                    player.performGraphic(new Graphic(AnimGFX.PURPLE_PORTAL_UP));
                    player.dealDamage(new Hit(player.getConstitution() / 3, Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
                }
                npc.setChargingAttack(false);
                this.stop();
            }
        });
    }

    private void destructiveBlow(@NotNull NPC npc, @NotNull Player player){
        npc.performAnimation(new Animation(AnimGFX.OVERLOAD_ANIMATION));
        npc.performGraphic(new Graphic(AnimGFX.BLACK_ARMOR));

        if(player.getPosition().isWithinDistance(npc.getPosition(), 2)){
            npc.performAnimation(new Animation(AnimGFX.FAST_CHOP_MELEE));
            npc.getCombatBuilder().setContainer(new CombatContainer(npc, player, 2, 1, CombatType.MELEE, true));
        } else {
            npc.setChargingAttack(true);
            npc.setVisible(false);

            TaskManager.submit(new Task(2, false){
                @Override
                protected void execute() {
                    npc.moveTo(player.getPosition().copy().add(1, 1)).setPosition(player.getPosition().copy().add(1, 1));
                    npc.setVisible(true);
                    npc.setChargingAttack(false);
                    npc.getCombatBuilder().setContainer(new CombatContainer(npc, player, 3, 1, CombatType.MELEE, true));
                    this.stop();
                }
            });

        }
    }

}
