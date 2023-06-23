package com.ruse.world.content.tradingpost.concurrency;

import java.sql.Connection;

@FunctionalInterface
public interface TradingPostRequest {
    void execute(Connection connection);
}
