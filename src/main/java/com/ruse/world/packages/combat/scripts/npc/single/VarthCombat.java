package com.ruse.world.packages.combat.scripts.npc.single;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Animation;
import com.ruse.model.Graphic;
import com.ruse.model.Locations;
import com.ruse.util.Misc;
import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.AnimGFX;
import org.jetbrains.annotations.NotNull;

public class VarthCombat implements CombatStrategy {
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

        int chance = Misc.random(10);

        if (chance <= 3 && Locations.goodDistance(npc.getPosition().copy(), player.getPosition().copy(), 1)){
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
        } else if(chance == 5){
            wrap(npc, player);
        } else if(chance == 7 ||chance == 9){
            execute(npc, player);
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

    private void wrap(@NotNull NPC npc, Player player) {
        npc.performAnimation(new Animation(AnimGFX.STAB_DOWN_MELEE));
        TaskManager.submit(new Task(1, npc, true) {
            int tick = 0;

            @Override
            public void execute() {
                switch (tick++) {
                    case 1 -> player.performGraphic(new Graphic(AnimGFX.BLACK_WRAP_UP));
                    case 2 -> {
                        npc.getCombatBuilder().setContainer(new CombatContainer(npc, player, 2, 1, CombatType.MAGIC, false));
                        stop();
                    }
                }
            }
        });
    }

    private void execute(@NotNull NPC npc, Player player){
        npc.performAnimation(new Animation(AnimGFX.CHARGE_SPELL_MAGIC));
        TaskManager.submit(new Task(1, npc, true) {
            int tick = 0;

            @Override
            public void execute() {
                switch (tick++) {
                    case 1 -> player.performGraphic(new Graphic(AnimGFX.RED_WAVE_OUT));
                    case 3, 5 -> {
                        npc.performGraphic(new Graphic(AnimGFX.WATER_TUNNEL));
                        npc.getCombatBuilder().setContainer(new CombatContainer(npc, player, 2, 1, CombatType.MELEE, true));
                    }
                    case 7 -> stop();
                }
            }
        });
    }
}
