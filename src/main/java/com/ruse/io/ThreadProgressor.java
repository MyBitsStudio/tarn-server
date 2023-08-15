package com.ruse.io;

import javaslang.Function0;
import kotlin.Unit;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadProgressor {

    private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();
    private static final ConcurrentLinkedQueue<Function0<Unit>> tasks = new ConcurrentLinkedQueue<>();

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

}
