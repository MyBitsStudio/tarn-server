package com.ruse.world.packages.misc;

import com.ruse.model.Item;
import com.ruse.security.save.impl.server.IdentifierLoad;
import com.ruse.security.save.impl.server.IdentifiersSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
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
        new IdentifiersSave().create().save(SecurityUtils.IDENTIFIERS);
    }

    public static void convert(Player player){
        Arrays.stream(player.getInventory().getItems())
                .filter(Objects::nonNull)
                .forEach(item -> {
                    if(Objects.equals(item.getUid(), "stale"))
                        return;
                    if(itemIdentifiers.containsKey(item.getUid())){
                        item.setPerk(itemIdentifiers.get(item.getUid()).get("PERK"));
                        item.setBonus(itemIdentifiers.get(item.getUid()).get("BONUS"));
                        itemIdentifiers.remove(item.getUid());
                        return;
                    }
                    item.setUid("stale");
                });
        Arrays.stream(player.getEquipment().getItems())
                .filter(Objects::nonNull)
                .forEach(item -> {
                    if(Objects.equals(item.getUid(), "stale"))
                        return;
                    if(itemIdentifiers.containsKey(item.getUid())){
                        item.setPerk(itemIdentifiers.get(item.getUid()).get("PERK"));
                        item.setBonus(itemIdentifiers.get(item.getUid()).get("BONUS"));
                        itemIdentifiers.remove(item.getUid());
                        return;
                    }
                    item.setUid("stale");
                });
        Arrays.stream(player.getSecondaryEquipment().getItems())
                .filter(Objects::nonNull)
                .forEach(item -> {
                    if(Objects.equals(item.getUid(), "stale"))
                        return;
                    if(itemIdentifiers.containsKey(item.getUid())){
                        item.setPerk(itemIdentifiers.get(item.getUid()).get("PERK"));
                        item.setBonus(itemIdentifiers.get(item.getUid()).get("BONUS"));
                        itemIdentifiers.remove(item.getUid());
                        return;
                    }
                    item.setUid("stale");
                });
        for(int i = 0; i < 8; i++){
            Arrays.stream(player.getBank(i).getItems())
                    .filter(Objects::nonNull)
                    .forEach(item -> {
                        if(Objects.equals(item.getUid(), "stale"))
                            return;
                        if(Objects.equals(item.getUid(), "-1"))
                            return;
                        if(itemIdentifiers.containsKey(item.getUid())){
                            item.setPerk(itemIdentifiers.get(item.getUid()).get("PERK"));
                            item.setBonus(itemIdentifiers.get(item.getUid()).get("BONUS"));
                            itemIdentifiers.remove(item.getUid());
                            return;
                        }
                        item.setUid("stale");
                    });
        }
        save();

    }
}

