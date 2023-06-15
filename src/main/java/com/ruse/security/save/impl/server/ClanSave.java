package com.ruse.security.save.impl.server;

import com.ruse.security.save.SecureSave;
import com.ruse.world.packages.clans.Clan;

import java.io.FileWriter;
import java.io.IOException;

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
