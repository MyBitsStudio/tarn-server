package com.ruse.io.database.models.impl;

import com.ruse.io.ThreadProgressor;
import com.ruse.io.database.models.DatabaseRequestStatement;
import com.ruse.io.database.records.DonateRedeem;
import com.ruse.security.save.impl.server.PlayerDonationSave;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.vip.VIPManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VIPClaim implements DatabaseRequestStatement {

    public static DatabaseRequestStatement finish = new VIPFinish();
    @Override
    public String statement() {
        return "SELECT * FROM payments WHERE player =?";
    }

    @Override
    public Object execute(Player player, String... args) {
        String name = Misc.formatPlayerName(player.getUsername());
        List<DonateRedeem> votes = new ArrayList<>();

        ThreadProgressor.takeRequest((connection) -> {
            try (PreparedStatement stmt = connection.prepareStatement(statement())
            ) {
                stmt.setString(1, name);
                try (ResultSet rs = stmt.executeQuery()) {
                    while ( rs.next() ) {
                        if(rs.getInt("claimed") == 1)
                            continue;
                        votes.add(new DonateRedeem(rs.getInt("id"), rs.getString("payment_id"), rs.getString("player"), rs.getInt(("amount")), rs.getDate("posted").toString()));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if(votes.isEmpty()){
                player.getPacketSender().sendMessage("You have no donations to redeem.");
                return;
            }
            for(DonateRedeem redeem : votes){
                finish.execute(player, redeem.payment_id());
                int amount = VIPManager.modify(player, redeem.amount());
                VIPManager.handleDonation(player, amount);
                new PlayerDonationSave(player, redeem.amount(), redeem.payment_id()).create().save();
                VIPManager.progress(player, redeem.amount());
                VIPManager.handleTickets(player, redeem.amount());
                VIPManager.handleSpecialActive(player, redeem.amount());
                VIPManager.handleDonationBoss(redeem.amount());
            }
        });
        return null;
    }
}
