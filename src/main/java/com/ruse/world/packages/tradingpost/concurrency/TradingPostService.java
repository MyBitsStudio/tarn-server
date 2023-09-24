package com.ruse.world.packages.tradingpost.concurrency;

import com.ruse.GameServer;
import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class TradingPostService {

    private static TradingPostService INSTANCE;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Getter
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

    public static TradingPostService getInstance() {
        if(INSTANCE == null)
            new TradingPostService();
        return INSTANCE;
    }
}
