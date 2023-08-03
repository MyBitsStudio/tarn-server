package com.ruse.world.packages.misc;

import com.ruse.security.save.impl.server.IdentifierLoad;
import com.ruse.security.save.impl.server.IdentifiersSave;
import com.ruse.security.tools.SecurityUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ItemIdentifiers {

    public static Map<String, Map<String, String>> itemIdentifiers = new ConcurrentHashMap<>();

    public static void addItemIdentifier(String item, String identifier, String value) {
        if (!itemIdentifiers.containsKey(item)) {
            itemIdentifiers.put(item, new ConcurrentHashMap<>());
        }
        itemIdentifiers.get(item).put(identifier, value);
        save();
    }

    public static void removeItemIdentifier(String item) {
        itemIdentifiers.remove(item);
        save();
    }

    public static String getItemIdentifier(String item, String identifier) {
        if (itemIdentifiers.containsKey(item)) {
            return itemIdentifiers.get(item).get(identifier);
        }
        return "0";
    }

    public static void load(){
        new IdentifierLoad().loadJSON(SecurityUtils.IDENTIFIERS).run();
    }

    public static void save(){
        new IdentifiersSave().create().save();
    }
}
