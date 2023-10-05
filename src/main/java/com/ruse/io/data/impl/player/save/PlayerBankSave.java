package com.ruse.io.data.impl.player.save;

import com.ruse.io.data.model.DatabasePost;
import com.ruse.world.entity.impl.player.Player;

public class PlayerBankSave implements DatabasePost {
    @Override
    public void execute(Player player, String... args) {

    }

    @Override
    public String statement() {
        return null;
    }
}
