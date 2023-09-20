package com.ruse.io.data.impl;

import com.ruse.io.ThreadProgressor;
import com.ruse.io.data.model.DataHandler;
import com.ruse.io.data.model.DatabasePost;
import com.ruse.world.entity.impl.player.Player;

import java.util.concurrent.ExecutionException;

public class SQLVipClaim implements DatabasePost {

    @Override
    public void execute(Player player, String... args) {
        ThreadProgressor.submit(true, () -> {
            try {
                DataHandler.getInstance().sendStatement(statement().replace("uid", args[0])).get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    @Override
    public String statement() {
        return "UPDATE payments SET `claimed` = '1' WHERE `payment_id` = 'uid'";
    }
}
