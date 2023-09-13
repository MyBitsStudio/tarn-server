package com.ruse.security.save.impl.server;

import com.ruse.engine.GameEngine;
import com.ruse.io.ThreadProgressor;
import com.ruse.security.save.SecureSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.packages.misc.ItemIdentifiers;

import java.io.FileWriter;
import java.io.IOException;

public class WellsSave extends SecureSave {
    public WellsSave(){
    }

    @Override
    public WellsSave create() {
        object.add("wells", builder.toJsonTree(GlobalBossManager.getInstance().getWells()));
        return this;
    }

    @Override
    public void save() {
        ThreadProgressor.submit(false, () -> {
            try (FileWriter file = new FileWriter(SecurityUtils.WELLS)) {
                file.write(builder.toJson(object));
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public String key() {
        return SecurityUtils.seeds[1];
    }
}
