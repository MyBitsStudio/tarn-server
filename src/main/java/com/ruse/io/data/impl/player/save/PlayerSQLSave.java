package com.ruse.io.data.impl.player.save;

import com.ruse.io.ThreadProgressor;
import com.ruse.io.data.model.DatabasePost;
import com.ruse.world.entity.impl.player.Player;

public class PlayerSQLSave implements DatabasePost {
    @Override
    public void execute(Player player, String... args) {
        ThreadProgressor.submit(true, () -> {
            new PlayerMainSave().execute(player);
            //new PlayerBankSave().execute(player);
            return null;
        });
    }

    @Override
    public String statement() {
        return "UPDATE players WHERE `username` = 'proxy1'";
    }
}
