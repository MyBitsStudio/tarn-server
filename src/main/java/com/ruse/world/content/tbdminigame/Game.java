package com.ruse.world.content.tbdminigame;

import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.entity.impl.player.Player;

import java.util.Set;

public class Game {

    private static final Position START_POSITION = new Position(3,3,3);

    private final Set<Player> playerSet;

    public Game(Set<Player> playerSet) {
        this.playerSet = playerSet;
    }

    public void start() {
        playerSet.forEach(player -> {
            player.getPacketSender().sendWalkableInterface(151100, false);
            TeleportHandler.teleportPlayer(player, START_POSITION, TeleportType.NORMAL);
        });
    }

    public void end(boolean hasWon) {
        World.sendMessage("@red@TBD minigame has been " + (hasWon? "won" : "defeated") + " by " + playerSet.size() + " players!");
        Lobby.getInstance().allowEntrance();
    }
}
