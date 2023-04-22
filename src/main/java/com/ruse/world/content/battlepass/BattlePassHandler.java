package com.ruse.world.content.battlepass;

import com.ruse.world.content.combat.CombatBuilder;
import com.ruse.world.content.combat.CombatFactory;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BattlePassHandler {

    public static int getDaysRemaining() {
        int days = 0;
        return days;
    }

    public static void GiveExperience(NPC npc) {

        if (npc.getCombatBuilder().getDamageMap().size() == 0)
            return;

        List<Player> killers = new ArrayList<Player>();

        for (Map.Entry<Player, CombatBuilder.CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {
            if (entry == null)
                continue;

            long timeout = entry.getValue().getStopwatch().elapsed();

            if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT)
                continue;

            Player player = entry.getKey();

            if (player.getConstitution() <= 0 || !player.isRegistered())
                continue;

            killers.add(player);
        }

        npc.getCombatBuilder().getDamageMap().clear();

        for(Player killer : killers) {
            if(killer == null)
                continue;
            killer.getBattlePass().incrementXP(1);
        }
    }
}
