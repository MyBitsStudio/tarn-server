package com.ruse.io.database.models.impl;

import com.ruse.io.ThreadProgressor;
import com.ruse.io.database.models.DatabaseRequestStatement;
import com.ruse.world.entity.impl.player.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RetrieveFinish implements DatabaseRequestStatement {
    @Override
    public String statement() {
        return "UPDATE `retrievals` SET `claimed` = '1' WHERE `id` =?";
    }

    @Override
    public Object execute(Player player, String... args) {
        ThreadProgressor.takeRequest((connection) -> {
            try(PreparedStatement stmt = connection.prepareStatement(statement())
            ) {
                stmt.setString(1, args[0]);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return null;
    }
}
