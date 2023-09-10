package com.ruse.world.packages.slot.effects;

import com.ruse.engine.task.Task;
import com.ruse.model.Graphic;
import com.ruse.model.Hit;
import com.ruse.model.Locations;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.world.clip.region.RegionClipping;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.max.MagicMax;

public class FireWall extends Task {

    public FireWall(Player player, int startX, int startY, int stages){
        super(stages);
        this.player = player;
        this.startX = startX;
        this.startY = startY;
        this.stages = stages;
    }
    int stage = 0, stages, startX, startY, width = 6;
    Player player;

    @Override
    protected void execute() {
        if(stage == stages-1) stop();
        if(player.getLocalNpcs().isEmpty()) stop();

        if(player.getPosition().getX() >= startX + 11) stop();
        if(player.getPosition().getY() >= startY + 11) stop();

        if(!Locations.Location.inMulti(player)) stop();

        for (NPC next : player.getLocalNpcs()) {
            if (next == null || next.getConstitution() <= 0 || next.isDying()
                    || !next.getDefinition().isAttackable() || next.isSummoningNpc()){
                continue;
            }

            if (!RegionClipping.canProjectileAttack(player, next)) {
                continue;
            }

            if (next.getPosition().isWithinDistance(player.getPosition(), width)) {
                next.performGraphic(new Graphic(453));
                long maxHit = MagicMax.newMagic(player, next) / 10;
                next.dealDamage(new Hit(maxHit));
                next.setAggressive(true);
                next.getCombatBuilder().setLastAttacker(player);
                next.getCombatBuilder().addDamage(player, maxHit);
                next.getCombatBuilder().attack(player);
            }

        }
        stage++;
    }
}
