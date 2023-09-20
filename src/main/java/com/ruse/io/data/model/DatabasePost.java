package com.ruse.io.data.model;


import com.github.jasync.sql.db.Connection;
import com.ruse.world.entity.impl.player.Player;

public interface DatabasePost {

    void execute(Connection connection, Player player, String... args);
    String statement();
}
