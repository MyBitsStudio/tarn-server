package com.ruse.world.content.tradingpost.sql;

import java.sql.Connection;

@FunctionalInterface
public interface TradingPostRequest {
    void execute(Connection connection);
}
