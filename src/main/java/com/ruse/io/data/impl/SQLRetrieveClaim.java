package com.ruse.io.data.impl;

import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.QueryResult;
import com.ruse.io.ThreadProgressor;
import com.ruse.io.data.model.DataHandler;
import com.ruse.io.data.model.DatabasePost;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;

import java.util.concurrent.ExecutionException;

public class SQLRetrieveClaim implements DatabasePost {
    @Override
    public void execute(Connection connection, Player player, String... args) {
        ThreadProgressor.submit(true, () -> {
            try {
                QueryResult result = DataHandler.getInstance().sendStatement(statement().replace("uid", args[0])).get();

            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    @Override
    public String statement() {
        return "UPDATE `retrievals` SET `claimed` = '1' WHERE `id` =uid";
    }
}
