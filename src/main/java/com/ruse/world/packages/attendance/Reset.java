package com.ruse.world.packages.attendance;

import com.ruse.security.save.impl.player.PlayerSecureLoad;
import com.ruse.security.save.impl.player.PlayerSecureSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.seasonpass.SeasonPassLoader;

import java.io.File;

public class Reset {

    public static void main(String[] args) {
        SeasonPassLoader.load();
        File[] files = new File("./data/security/saves").listFiles();
        assert files != null;
        for(File file : files) {
            Player player = new Player(null);
            new PlayerSecureLoad(player).loadJSON(file).run();
            player.getAttendenceManager().getPlayerAttendanceProgress().clear();
            player.getPSettings().setSetting("donator", false);
            player.getPSettings().setSetting("summer-unlock", false);
            System.out.println("Resetting attendance for player: " + player.getUsername());
            new PlayerSecureSave(player).create().save(SecurityUtils.PLAYER_FILE+player.getUsername()+".json");
        }
    }
}
