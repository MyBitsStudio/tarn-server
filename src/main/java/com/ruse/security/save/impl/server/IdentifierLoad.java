package com.ruse.security.save.impl.server;

import com.ruse.security.save.SecureLoad;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.packages.misc.ItemIdentifiers;

import java.util.Map;

public class IdentifierLoad extends SecureLoad {
    public IdentifierLoad(){
        ;
    }

    @Override
    public String key() {
        return SecurityUtils.seeds[1];
    }

    @Override
    public IdentifierLoad run() {
        ItemIdentifiers.itemIdentifiers = builder.fromJson(object.get("identifier"),
                new com.google.gson.reflect.TypeToken<Map<String, Map<String, String>>>() {
                }.getType());
        System.out.println("Loaded " + ItemIdentifiers.itemIdentifiers.size() + " item identifiers.");
        return this;
    }
}
