package com.ruse.io.data.impl.player.save;

import com.ruse.io.ThreadProgressor;
import com.ruse.io.data.model.DataHandler;
import com.ruse.io.data.model.DatabasePost;
import com.ruse.world.entity.impl.player.Player;

import java.util.concurrent.ExecutionException;

public class PlayerMainSave implements DatabasePost {
    @Override
    public void execute(Player player, String... args) {
        ThreadProgressor.submit(true, () -> {
            try {
                DataHandler.getInstance().sendStatement(statement()
                        .replace("proxy1", player.getUsername())
                        .replace("proxy2", String.valueOf(player.getTotalPlayTime()))
                        .replace("proxy3", player.getRank().toString())
                        .replace("proxy4", String.valueOf(player.getPlayerVIP()))
                        .replace("proxy5", player.getMode().toString())
                ).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    @Override
    public String statement() {
        return "UPDATE players WHERE `username` = 'proxy1' SET 'total-play-time' = 'proxy2' AND 'staff' = 'proxy3' AND 'vip' = 'proxy4' AND 'game-mode' = 'proxy5";
    }
}
