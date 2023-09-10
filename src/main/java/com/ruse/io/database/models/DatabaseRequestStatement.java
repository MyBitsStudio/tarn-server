package com.ruse.io.database.models;

import com.ruse.world.entity.impl.player.Player;

public interface DatabaseRequestStatement {

    String statement();
    Object execute(Player player, String... args);
}
