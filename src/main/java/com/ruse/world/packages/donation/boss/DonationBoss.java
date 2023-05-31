package com.ruse.world.packages.donation.boss;

import com.ruse.model.Position;
import com.ruse.model.definitions.NPCDrops;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.packages.donation.DonationManager;
import com.ruse.world.entity.impl.npc.NPC;

public class DonationBoss extends NPC {

    public static Position base = new Position(2529, 2633, 4);
    public DonationBoss() {
        super(587, base, false);
        this.getDefinition().setAggressive(true);
        this.getDefinition().setMulti(true);
        this.getDefinition().setRespawnTime(-1);
    }

    @Override
    public boolean isAttackable() {
        for(DonationMinion minion : DonationManager.getInstance().getMinions()){
            if(minion != null && minion.getConstitution() > 0){
                return false;
            }
        }
        return true;
    }

    public void handleDrops(){
        if (getCombatBuilder().getDamageMap().size() == 0) {
            return;
        }

        getClosePlayers(20).forEach(player -> {
            if (player.getConstitution() <= 0 || !player.isRegistered()) {
                return;
            }
            NPCDrops.handleDrops(player, this);
        });

        getCombatBuilder().getDamageMap().clear();

        DonationManager.getInstance().nullBoss();
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new BossCombat();
    }
}
