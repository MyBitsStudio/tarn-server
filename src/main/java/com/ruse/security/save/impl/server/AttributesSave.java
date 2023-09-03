package com.ruse.security.save.impl.server;

import com.ruse.engine.GameEngine;
import com.ruse.security.save.SecureSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.World;
import com.ruse.world.packages.globals.GlobalBossManager;

import java.io.FileWriter;
import java.io.IOException;

public class AttributesSave extends SecureSave {
    public AttributesSave(){
        ;
    }

    @Override
    public AttributesSave create() {
        object.add("attributes", builder.toJsonTree(World.attributes.getSettings()));
        return this;
    }

    @Override
    public void save() {
        GameEngine.submit(() -> {
            try (FileWriter file = new FileWriter(SecurityUtils.ATTRIBUTES)) {
                file.write(builder.toJson(object));
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String key() {
        return SecurityUtils.seeds[1];
    }
}
