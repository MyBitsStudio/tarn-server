package com.ruse.world.content.tbdminigame;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.world.World;
import com.ruse.world.content.dialogue.DialogueManager;
import com.ruse.world.entity.impl.player.Player;

import java.util.HashSet;
import java.util.Set;

public class Lobby {

    private static Lobby INSTANCE;

    /**
     * The amount of time in minutes that the players in {@link #playerSet}
     * must wait until a new game begins.
     */
    private static final int LOBBY_TIMER = 2;

    private final Set<Player> playerSet = new HashSet<>();

    /**
     * The current ongoing {@link Game}
     */
    private Game game;

    public Lobby() {
        INSTANCE = this;
    }

    public void barrierClick(Player player) {
        if(playerSet.contains(player))
            remove(player);
        else
            enter(player);
    }

    private void enter(Player player) {
        if(game != null) {
            DialogueManager.sendStatement(player, "Cannot join lobby with an ongoing game in progress.");
            return;
        }
        playerSet.add(player);
        if(playerSet.size() == 1) startTimer();
        player.getPacketSender().sendWalkableInterface(1, true);
        updatePlayerCountInterface();
    }

    private void remove(Player player) {
        playerSet.remove(player);
        player.getPacketSender().sendWalkableInterface(1, false);
        if(playerSet.isEmpty())
            cancelTimer();
    }

    private void startGame() {
        game = new Game(playerSet);
        game.start();
    }

    private void updatePlayerCountInterface() {
        for(Player player : playerSet)
            player.getPacketSender().sendString(1, String.valueOf(playerSet.size()));
    }

    private void updateTimerInterface(int minutesLeft) {
        for(Player player : playerSet)
            player.getPacketSender().sendString(1, minutesLeft + "min left");
    }

    private void cancelTimer() {
        TaskManager.cancelTasks(this);
    }

    private void startTimer() {
        TaskManager.submit(new Task(100, this, false) {
            int ticks = 0;
            @Override
            protected void execute() {
                if(ticks == LOBBY_TIMER) {
                    startGame();
                    stop();
                } else {
                   ticks++;
                   updateTimerInterface(LOBBY_TIMER - ticks);
                }
            }
        });
    }

    public static Lobby getInstance() {
        return INSTANCE;
    }

    public void allowEntrance() {
        game = null;
        World.sendMessage("@yel@type ::tbd to player the tbd minigame!");
    }
}
