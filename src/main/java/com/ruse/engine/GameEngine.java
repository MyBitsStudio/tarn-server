package com.ruse.engine;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ruse.GameSettings;
import com.ruse.engine.task.TaskManager;
import com.ruse.io.ThreadProgressor;
import com.ruse.util.playerSavingTimer;
import com.ruse.world.World;
import com.ruse.world.packages.clans.ClanManager;
import com.ruse.world.content.grandexchange.GrandExchangeOffers;
import com.ruse.world.content.groupironman.GroupManager;
import com.ruse.world.entity.impl.GlobalItemSpawner;

import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

/**
 * @author lare96
 * @author Gabriel Hannason
 */
public final class GameEngine implements Runnable {
    private static final ThreadFactory THREAD_FACTORY_BUILDER = new ThreadFactoryBuilder().setNameFormat("GameThread").build();

    /**
     * STATIC
     **/

    public static ScheduledExecutorService createLogicService() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.setRejectedExecutionHandler(new CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("LogicServiceThread").build());
        executor.setKeepAliveTime(45, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return Executors.unconfigurableScheduledExecutorService(executor);
    }

    /*
     * private EngineState next() { if (engineTick == PROCESS_GAME_TICK) {
     * engineTick = 0; return EngineState.GAME_PROCESSING; } engineTick++; return
     * EngineState.PACKET_PROCESSING; }
     *
     * private enum EngineState { PACKET_PROCESSING, GAME_PROCESSING; }
     */

    @Override
    public void run() {
        try {
            TaskManager.sequence();
            ThreadProgressor.execute();
            World.sequence();
            playerSavingTimer.massSaving();
            GlobalItemSpawner.startup();
        } catch (Throwable e) {
            e.printStackTrace();
            GroupManager.saveGroups();
            World.savePlayers();
            playerSavingTimer.massSaving();
            GrandExchangeOffers.save();
            ClanManager.getManager().init();
        }
    }

    public void init(){

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY_BUILDER);
        final ScheduledFuture<?> handle = executorService.scheduleAtFixedRate(this, 0,
                GameSettings.ENGINE_PROCESSING_CYCLE_RATE,
                TimeUnit.MILLISECONDS);
        final Thread exceptionHandlerThread = new Thread(() -> {
            try {
                handle.get();
            } catch (ExecutionException | InterruptedException e){
                GroupManager.saveGroups();
                World.savePlayers();
                playerSavingTimer.massSaving();
                GrandExchangeOffers.save();
                ClanManager.getManager().save();
            }
        }, "GameEngine Exception Handler");
        exceptionHandlerThread.start();
    }

    /**
     * Submit runnable to game thread.
     *
     * @param runnable the {@link Runnable} to be submitted.
     */
    public static void submit(Runnable runnable) {
        World.submitGameThreadJob(() -> {
            runnable.run();
            return null;
        });
    }

}