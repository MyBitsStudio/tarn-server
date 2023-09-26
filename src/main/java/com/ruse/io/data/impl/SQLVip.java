package com.ruse.io.data.impl;

import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.ResultSet;
import com.github.jasync.sql.db.RowData;
import com.ruse.io.ThreadProgressor;
import com.ruse.io.data.model.DataHandler;
import com.ruse.io.data.model.DatabasePost;
import com.ruse.io.data.records.DonateRedeem;
import com.ruse.io.data.records.Retrievals;
import com.ruse.security.save.impl.server.PlayerDonationSave;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.vip.VIPManager;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class SQLVip implements DatabasePost {

    @Override
    public void execute(Player player, String... args) {
        String name = Misc.formatPlayerName(player.getUsername());
        List<DonateRedeem> donate = new ArrayList<>();

        ThreadProgressor.submit(true, () -> {
            try {
                QueryResult result = DataHandler.getInstance().result(statement()).get();

                final Optional<ResultSet> rowsOption = Optional.of(result.getRows());
                if(rowsOption.get().isEmpty()){
                    throw new RuntimeException("Empty Result Set");
                } else {
                    final ResultSet set = rowsOption.get();
                    for (RowData row : set) {
                        if(row.isEmpty()){
                            continue;
                        }
                        if (!Objects.equals(Objects.requireNonNull(row.getString("player")).toLowerCase(), name.toLowerCase())) {
                            continue;
                        }

                        if (row.getByte("claimed") == 1)
                            continue;

                        donate.add(new DonateRedeem(row.getInt("id"), row.getString("payment_id"), row.getString("player"), row.getInt("amount"), new Date().toString()));
                    }
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(donate.isEmpty()){
                player.getPacketSender().sendMessage("You have no donations to redeem.");
                return null;
            }
            for(DonateRedeem redeem : donate){
                new SQLVipClaim().execute(player, redeem.payment_id());
                int amount = VIPManager.modify(player, redeem.amount());
                VIPManager.handleDonation(player, amount);
                new PlayerDonationSave(player, redeem.amount(), redeem.payment_id()).create().save();
                VIPManager.progress(player, redeem.amount());
                VIPManager.handleTickets(player, redeem.amount());
                VIPManager.handleSpecialActive(player, redeem.amount());
                VIPManager.handleDonationBoss(redeem.amount());
            }
            player.save();
            return null;
        });
    }

    @Override
    public String statement() {
        return "SELECT * FROM payments";
    }
}
