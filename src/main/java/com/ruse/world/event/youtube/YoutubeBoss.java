package com.ruse.world.event.youtube;

import com.ruse.model.Position;
import com.ruse.model.definitions.NPCDrops;
import com.ruse.world.World;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.event.Event;

import javax.xml.stream.Location;

public class YoutubeBoss extends Event {

    private NPC boss;

    public YoutubeBoss(Player player, Location loc) {
        super(player, loc);
    }

    @Override
    public void start() {
        boss = new NPC(4540, new Position(2856, 2708, 4));
        World.register(boss);
        World.sendNewsMessage("@yel@ [EVENT] Staff has spawned a Youtube Boss event! Get them now!");
    }

    @Override
    public void stop() {
        if(World.npcIsRegistered(4540)) {
            World.deregister(boss);
        }
        World.sendNewsMessage("@yel@ [EVENT] Youtube Boss event has ended!");
    }

    @Override
    public String key() {
        return "youtube";
    }

    @Override
    public void moveTo(Player player) {

    }

    public static void drop(NPC bosses){
        System.out.println("dropping youtube");
        bosses.getClosePlayers(20).forEach(player -> {
            if (player.getConstitution() <= 0 || !player.isRegistered()) {
                return;
            }
            NPCDrops.handleDrops(player, bosses);
        });

        bosses.getCombatBuilder().getDamageMap().clear();
    }
}
