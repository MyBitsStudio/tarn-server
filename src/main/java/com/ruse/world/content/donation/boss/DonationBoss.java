package com.ruse.world.content.donation.boss;

import com.ruse.model.Position;
import com.ruse.model.definitions.NPCDrops;
import com.ruse.world.content.combat.CombatBuilder;
import com.ruse.world.content.combat.CombatFactory;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.donation.DonationManager;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import java.util.*;

public class DonationBoss extends NPC {

    public static Position base = new Position(2529, 2633, 4);
    public DonationBoss() {
        super(587, base, false);
        this.getDefinition().setAggressive(true);
        this.getDefinition().setMulti(true);
        this.getDefinition().setRespawnTime(-1);
    }

    public void handleDrops(){
        if (getCombatBuilder().getDamageMap().size() == 0) {
            return;
        }
        Map<Player, Long> killers = new HashMap<>();

        for (Map.Entry<Player, CombatBuilder.CombatDamageCache> entry : getCombatBuilder().getDamageMap().entrySet()) {
            if (entry == null) {
                continue;
            }

            long timeout = entry.getValue().getStopwatch().elapsed();
            if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
                continue;
            }

            Player player = entry.getKey();
            if (player.getConstitution() <= 0 || !player.isRegistered()) {
                continue;
            }

            killers.put(player, entry.getValue().getDamage());
        }

        getCombatBuilder().getDamageMap().clear();

        List<Map.Entry<Player, Long>> result = sortEntries(killers);
        for (Iterator<Map.Entry<Player, Long>> iterator = result.iterator(); iterator.hasNext(); ) {
            Map.Entry<Player, Long> entry = iterator.next();
            Player killer = entry.getKey();
            NPCDrops.handleDrops(killer, this);
            iterator.remove();
        }
        DonationManager.getInstance().nullBoss();
    }

    static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sortEntries(Map<K, V> map) {

        List<Map.Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());

        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        return sortedEntries;

    }

    @Override
    public CombatStrategy determineStrategy() {
        return new BossCombat();
    }
}
