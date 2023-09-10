package com.ruse.security.save.impl.server;

import com.ruse.engine.GameEngine;
import com.ruse.io.ThreadProgressor;
import com.ruse.security.ServerSecurity;
import com.ruse.security.save.SecureSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.packages.misc.ItemIdentifiers;

import java.io.FileWriter;
import java.io.IOException;

public class IdentifiersSave extends SecureSave {
    public IdentifiersSave(){
        ;
    }

    @Override
    public IdentifiersSave create() {
        object.add("identifier", builder.toJsonTree(ItemIdentifiers.itemIdentifiers));
        return this;
    }

    @Override
    public void save() {
        ThreadProgressor.submit(false, () -> {
            try (FileWriter file = new FileWriter(SecurityUtils.IDENTIFIERS)) {
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
