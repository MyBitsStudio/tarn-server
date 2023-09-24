package com.ruse.world.packages.tradingpost.concurrency;

import java.sql.Connection;

@FunctionalInterface
public interface TradingPostRequest {
    void execute(Connection connection);
}
