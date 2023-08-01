package com.ruse.world.content.tbdminigame;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.DialogueManager;

import java.util.HashSet;
import java.util.Set;

public class Lobby {

    private static final Position LOBBY_ENTER_POSITION = new Position(2399, 3747, 0);
    private static final Position LOBBY_EXIT_POSITION = new Position(2399, 3748, 0);

    private static Lobby INSTANCE;

    /**
     * The amount of time in minutes that the players in {@link #playerSet}
     * must wait until a new game begins.
     */
    private static final int LOBBY_TIMER = 2;

    private final Set<Player> playerSet = new HashSet<>();

    private int taskTicks;

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
        player.moveTo(LOBBY_ENTER_POSITION);
        if(playerSet.size() == 1) startTimer();
        player.getPacketSender().sendWalkableInterface(151100, true);
        updatePlayerCountInterface();
        updateTimerInterface(LOBBY_TIMER - taskTicks);
    }

    private void remove(Player player) {
        playerSet.remove(player);
        player.moveTo(LOBBY_EXIT_POSITION);
        player.getPacketSender().sendWalkableInterface(151100, false);
        if(playerSet.isEmpty())
            cancelTimer();
    }

    private void startGame() {
        game = new Game(playerSet);
        game.start();
    }

    private void updatePlayerCountInterface() {
        for(Player player : playerSet)
            player.getPacketSender().sendString(151104, "Players: " + playerSet.size());
    }

    private void updateTimerInterface(int minutesLeft) {
        for(Player player : playerSet)
            player.getPacketSender().sendString(151105, "Time left: " + minutesLeft + " min");
    }

    private void cancelTimer() {
        TaskManager.cancelTasks(this);
    }

    private void startTimer() {
        taskTicks = 0;
        TaskManager.submit(new Task(100, this, false) {
            @Override
            protected void execute() {
                taskTicks++;
                if(taskTicks == LOBBY_TIMER) {
                    startGame();
                    stop();
                } else {
                   updateTimerInterface(LOBBY_TIMER - taskTicks);
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
