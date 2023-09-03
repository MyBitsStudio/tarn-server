package com.ruse.world.packages.combat.scripts.npc.single;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Animation;
import com.ruse.model.Graphic;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.util.Misc;
import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.single.zernath.ZernathInstance;
import com.ruse.world.packages.combat.AnimGFX;

public class ZernathCombat implements CombatStrategy {
    @Override
    public boolean canAttack(Character entity, Character victim) {
        return victim.isPlayer();
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(Character entity, Character victim) {

        NPC npc = entity.toNpc();
        Player player = victim.asPlayer();
        ZernathInstance zernath = (ZernathInstance) player.getInstance();


        int chance = Misc.random(10);

        if (chance <= 2 && Locations.goodDistance(npc.getPosition().copy(), player.getPosition().copy(), 1)){
            TaskManager.submit(new Task(2, npc, true) {
				int tick = 0;

				@Override
				public void execute() {
					switch (tick++) {
					case 1:
						npc.performGraphic(new Graphic(AnimGFX.EARTHQUAKE_FRONT));
						break;

                    case 2:
                        npc.getCombatBuilder().setContainer(new CombatContainer(npc, player, 2, 1, CombatType.MELEE, false));
                        stop();
                        break;
					}
				}
			});
        } else if(chance == 3) {
            zernath.spawn();
        } else if(chance == 5){
            slay(npc, player);
        } else if( chance == 7){
            erupt(npc, player);
        } else {
            victim.performGraphic(new Graphic(2146));
            npc.getCombatBuilder().setContainer(new CombatContainer(npc, player, 1, 1, CombatType.MAGIC, true));
        }

        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return 5;
    }

    @Override
    public int attackDistance(Character entity) {
        return 14;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }

    private void slay(NPC npc, Player player){
        npc.performAnimation(new Animation(AnimGFX.CAST_SPELL_UP_MAGIC));
        npc.performGraphic(new Graphic(AnimGFX.WHITE_STAR));
        Position position = player.getPosition().copy().add(1, 0);

        TaskManager.submit(new Task(2, npc, true) {
            int tick = 0;

            @Override
            public void execute() {
                switch (tick++) {
                    case 2 -> {
                        npc.setVisible(false);
                        npc.moveTo(position);
                    }
                    case 5 -> npc.getCombatBuilder().setContainer(new CombatContainer(npc, player, 2, 1, CombatType.MELEE, true));
                    case 6 -> {
                        npc.setVisible(true);
                        npc.performGraphic(new Graphic(AnimGFX.BLACK_STAR_DOWN));
                    }
                    case 8 -> stop();
                }
            }
        });
    }

    private void erupt(NPC npc, Player player){
        npc.performAnimation(new Animation(AnimGFX.OVERLOAD_ANIMATION));
        Position position = player.getPosition().copy();

        TaskManager.submit(new Task(2, npc, true) {
            int tick = 0;

            @Override
            public void execute() {
                switch (tick++) {
                    case 2 -> {
                        for(int i = 0; i < 3; i++){
                             npc.moveTo(position.add(i, 0));
                             npc.performGraphic(new Graphic(AnimGFX.SKELETON_FREAKOUT));
                        }
                    }
                    case 5 ->
                    {
                        for(int i = 0; i < 3; i++){
                            for(int z = 0; z < 3; z++){
                                if(player.getPosition().equals(position.add(i, z))){
                                    npc.getCombatBuilder().setContainer(new CombatContainer(npc, player, 1, 1, CombatType.MAGIC, false));
                                }
                            }
                        }
                    }
                    case 7 -> npc.performGraphic(new Graphic(AnimGFX.BLACK_STAR_DOWN));
                    case 8 -> stop();
                }
            }
        });
    }
}
