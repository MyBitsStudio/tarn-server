package com.ruse.io;

import com.ruse.GameServer;
import com.ruse.io.database.modal.DatabaseRequest;
import com.ruse.io.database.modal.DatabaseWorker;
import javaslang.Function0;
import kotlin.Unit;
import lombok.Getter;

import java.util.concurrent.*;

public class ThreadProgressor {

    private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();
    private static final LinkedBlockingQueue<Function0<Unit>> tasks = new LinkedBlockingQueue<>();
    private static final Executor execute = Executors.newSingleThreadExecutor();
    @Getter
    private static final LinkedBlockingQueue<DatabaseRequest> boss = new LinkedBlockingQueue<>();


    public static void submit(boolean immediate, Function0<Unit> task) {
        if(immediate)
            executor.execute(task::apply);
        else
            tasks.offer(task);
    }

    public static void execute(){
        Function0<Unit> task;
        while ((task = tasks.poll()) != null) {
            forkJoinPool.execute(task::get);
        }
    }

    public static void shutdown(){
        executor.shutdown();
        forkJoinPool.shutdown();
    }

    public static void takeRequest(DatabaseRequest request) {
        if(!GameServer.isUpdating()) boss.offer(request);
    }

    public static void initDatabase(){
        execute.execute(new DatabaseWorker());
    }

}
