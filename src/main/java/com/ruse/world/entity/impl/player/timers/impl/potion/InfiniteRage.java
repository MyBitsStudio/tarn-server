package com.ruse.world.entity.impl.player.timers.impl.potion;

import com.ruse.model.*;
import com.ruse.world.World;
import com.ruse.world.content.Consumables;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class InfiniteRage extends PotionTimer {

    private int tick = -1;
    public InfiniteRage(){
        super(null, 0);
    }
    public InfiniteRage(Player player) {
        super(player, MINUTES * 30);
    }

    @Override
    public void applyEffect() {
        if(++tick == 0){
            getPlayer().getPacketSender().sendMessage("@red@Your Rage's effect has started.");
        }
        ObjectArrayList<NPC> npcs = World.getNearbyNPCs(getPlayer().getPosition(), 6);
        for(NPC npc : npcs){
            if(npc != null){
                if(!npc.isAggressive() && npc.getDefinition().isAttackable()){
                    npc.setAggressive(true);
                    npc.setAggressiveDistance(10);
                    npc.setForceAggressive(true);
                }
            }
        }
    }

    @Override
    public EffectTimer getEffectTimer() {
        return null;
    }

    @Override
    public Animation getAnimation() {
        return new Animation(829);
    }


    @Override
    public void finish() {
        getPlayer().getPacketSender().sendMessage("@red@Your Rage's effect has run out.");
    }

    @Override
    public String getName() {
        return "inf-rage-1";
    }
}
