package com.ruse.world.content.tradingpost.sql;

import com.ruse.GameServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class TradingPostService {

    private static TradingPostService INSTANCE;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final LinkedBlockingQueue<TradingPostRequest> boss = new LinkedBlockingQueue<>();

    public TradingPostService() {
        INSTANCE = this;
    }

    public void init() {
        executorService.execute(new TradingPostWorker(this));
    }

    public void takeRequest(TradingPostRequest request) {
        if(!GameServer.isUpdating()) boss.offer(request);
    }

    public LinkedBlockingQueue<TradingPostRequest> getBoss() {
        return boss;
    }

    public static TradingPostService getInstance() {
        return INSTANCE;
    }
}
