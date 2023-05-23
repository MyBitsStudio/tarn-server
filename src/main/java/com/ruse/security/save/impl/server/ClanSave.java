package com.ruse.security.save.impl.server;

import com.ruse.security.save.SecureSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.content.clans.Clan;
import com.ruse.world.content.clans.ClanManager;
import com.ruse.world.entity.impl.player.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClanSave extends SecureSave {

    private final Clan clan;
    public ClanSave(Clan clan) {
        this.clan = clan;
    }
    @Override
    public SecureSave create() {
        object.addProperty("name", clan.getName());
        object.add("banned", builder.toJsonTree(clan.getBanned()));
        return this;
    }

    @Override
    public void save() {
        try (FileWriter file = new FileWriter("./.core/server/clans/"+clan.getName()+".json")) {
                file.write(builder.toJson(object));
                file.flush();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    @Override
    public String key() {
        return null;
    }
}
