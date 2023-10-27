package com.ruse.world.packages.pets;

import com.ruse.model.*;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.AnimGFX;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompanionHandler {

    @Getter
    private int nextSummonId = -1;
    private final Player player;
    private final List<Companion> companions = new ArrayList<>();
    @Getter
    private Companion companion;

    public CompanionHandler(Player player) {
        this.player = player;
    }

    public boolean setCompanion(CompanionData companion){
        if(companion == null){
            return false;
        }
        if(companion.getNpcId() == -1){
            return false;
        }
        if(companions.stream().noneMatch(c -> c.getData().getNpcId() == companion.getNpcId())){
            return false;
        }
        nextSummonId = companion.getNpcId();
        System.out.println("Set companion to " + companion.getNpcId());
        return true;
    }

    public void summon(){
        if(nextSummonId == -1){
            System.out.println("No companion set");
            return;
        }
        if (!player.getLocation().isSummoningAllowed()) {
            player.sendMessage("You cannot summon a companion here.");
            return;
        }
        desummon();
        companion = companions.stream().filter(c -> c.getData().getNpcId() == nextSummonId).findFirst().orElse(null);
        if(companion == null){
            System.out.println("Companions is null");
            return;
        }

        companion.setPosition(player.getPosition().copy().add(0, 1).copy());
        companion.performGraphic(new Graphic(1315));
        companion.setPositionToFace(player.getPosition());
        companion.setSummoningNpc(true);
        companion.setSpawnedFor(player);
        companion.setVisible(true);

        World.register(companion);

        companion.setEntityInteraction(player);
        companion.getMovementQueue().setFollowCharacter(player);
        companion.getActive().set(true);

        process();

        player.getPacketSender().sendString(0, "[SUMMOtrue");
        player.getPacketSender().sendString(54028, companion.getDefinition().getName().replaceAll("_", " "));
        player.getPacketSender().sendNpcOnInterface(54021, companion.getData().getNpcId(), companion.getData().getZoom() );

        player.getLastSummon().reset();
    }

    public void unlockCompanion(CompanionData companion){
        if(companion == null){
            return;
        }
        if(companions.stream().anyMatch(c -> c.getData().getNpcId() == companion.getNpcId())){
            return;
        }
        companions.add(new Companion(companion));
        player.sendMessage("You have unlocked a new companion!");
    }

    public void desummon(){
        if(companion == null){
            System.out.println("Companion is null");
            return;
        }
        World.deregister(companion);
        companion.getActive().set(false);
        player.getPacketSender().sendString(0, "[SUMMOfalse");
    }

    public void onLogin(){

    }


    public void updateTab(){

    }

    public void process(){
        if(companion == null)
            return;


    }

    public void handleDefence(Character attacker){
        if(companion == null)
            return;

        CompanionSkills skill = companion.getSkill();

        switch(skill){
            case HEAL_LOW -> {
                if(Misc.random(5) == 3) {
                    companion.performGraphic(new Graphic(AnimGFX.WHITE_STAR));
                    player.heal(Misc.random(5, 20));
                }
                companion.addXP(2);
            }
            case HEAL_MED -> {
                if(Misc.random(5) == 3) {
                    companion.performGraphic(new Graphic(AnimGFX.WHITE_STAR));
                    player.heal(Misc.random(20, 40));
                }
                companion.addXP(4);
            }
            case HEAL_HIGH -> {
                if(Misc.random(5) == 3) {
                    companion.performGraphic(new Graphic(AnimGFX.WHITE_STAR));
                    player.heal(Misc.random(40, 80));
                }
                companion.addXP(10);
            }
        }
    }

    public void handleAttack(Character victim) {
        if(companion == null)
            return;

        if(!companion.getSkill().isAttackSkill())
            return;

        CompanionSkills skill = companion.getSkill();

        switch(skill){
            case ATTACK_LOW_MELEE -> {
                companion.performGraphic(new Graphic(AnimGFX.DRAGON_CLOUD));
                victim.dealDamage(new Hit(Misc.getRandom(20, 500), Hitmask.DARK_PURPLE, CombatIcon.MELEE));
                companion.addXP(2);
            }
            case ATTACK_MED_MELEE -> {
                companion.performGraphic(new Graphic(AnimGFX.DRAGON_CLOUD));
                victim.dealDamage(new Hit(Misc.getRandom(500, 5000), Hitmask.DARK_PURPLE, CombatIcon.MELEE));
                companion.addXP(4);
            }
            case ATTACK_HIGH_MELEE -> {
                companion.performGraphic(new Graphic(AnimGFX.DRAGON_CLOUD));
                victim.dealDamage(new Hit(Misc.getRandom(5000, 500000), Hitmask.DARK_PURPLE, CombatIcon.MELEE));
                companion.addXP(10);
            }
            case ATTACK_LOW_RANGED -> {
                companion.performGraphic(new Graphic(AnimGFX.DRAGON_CLOUD));
                new Projectile(companion.getPosition(), victim.getPosition(), 0, 1120, 44, 0, 43, 31, 0).sendProjectile();
                victim.dealDamage(new Hit(Misc.getRandom(20, 500), Hitmask.DARK_PURPLE, CombatIcon.RANGED));
                companion.addXP(2);
            }
            case ATTACK_MED_RANGED -> {
                companion.performGraphic(new Graphic(AnimGFX.DRAGON_CLOUD));
                new Projectile(companion.getPosition(), victim.getPosition(), 0, 1120, 44, 0, 43, 31, 0).sendProjectile();
                victim.dealDamage(new Hit(Misc.getRandom(500, 5000), Hitmask.DARK_PURPLE, CombatIcon.RANGED));
                companion.addXP(4);
            }
            case ATTACK_HIGH_RANGED -> {
                companion.performGraphic(new Graphic(AnimGFX.DRAGON_CLOUD));
                new Projectile(companion.getPosition(), victim.getPosition(), 0, 1120, 44, 0, 43, 31, 0).sendProjectile();
                victim.dealDamage(new Hit(Misc.getRandom(5000, 500000), Hitmask.DARK_PURPLE, CombatIcon.RANGED));
                companion.addXP(10);
            }
            case ATTACK_LOW_MAGIC -> {
                companion.performGraphic(new Graphic(AnimGFX.DRAGON_CLOUD));
                new Projectile(companion.getPosition(), victim.getPosition(), 0, 360, 44, 0, 43, 31, 0).sendProjectile();
                victim.dealDamage(new Hit(Misc.getRandom(20, 500), Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
                companion.addXP(2);
            }
            case ATTACK_MED_MAGIC -> {
                companion.performGraphic(new Graphic(AnimGFX.DRAGON_CLOUD));
                new Projectile(companion.getPosition(), victim.getPosition(), 0, 360, 44, 0, 43, 31, 0).sendProjectile();
                victim.dealDamage(new Hit(Misc.getRandom(500, 5000), Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
                companion.addXP(4);
            }
            case ATTACK_HIGH_MAGIC -> {
                companion.performGraphic(new Graphic(AnimGFX.DRAGON_CLOUD));
                new Projectile(companion.getPosition(), victim.getPosition(), 0, 360, 44, 0, 43, 31, 0).sendProjectile();
                victim.dealDamage(new Hit(Misc.getRandom(5000, 500000), Hitmask.DARK_PURPLE, CombatIcon.MAGIC));
                companion.addXP(10);
            }
        }
    }
}
