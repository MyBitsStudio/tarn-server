package com.ruse.world.content;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BestItemsInterface {
    private static String[] text = new String[]{"Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush",
            "Magic", "Range", "Strength", "Ranged Str", "Magic Damage"};

    public static boolean buttonClicked(Player player, int buttonId) {
        if (buttonId >= 100010 && buttonId <= 100014) {
            int index = (buttonId - 100010);
            openInterface(player, index);
            return true;
        } else if (buttonId >= 100016 && buttonId <= 100020) {
            int index = (buttonId - 100016) + 5;
            openInterface(player, index);
            return true;
        } else if (buttonId == 100022) {
            openInterface(player, 14);
            return true;
        } else if (buttonId == 100023) {
            openInterface(player, 15);
            return true;
        } else if (buttonId == 100024) {
            openInterface(player, 17);
            return true;
        }
        return false;
    }

    public static void openInterface(Player player, int bonus) {

        for (int i = 0; i < 5; i++) {
            player.getPacketSender().sendString(100010 + i, (bonus == i ? "@whi@Check " : "Check ") + text[i]);
        }
        for (int i = 5; i < 10; i++) {
            player.getPacketSender().sendString(100011 + i, (bonus == i ? "@whi@Check " : "Check ") + text[i]);
        }
        player.getPacketSender().sendString(100022, (bonus == 14 ? "@whi@Check " : "Check ") + text[10]);
        player.getPacketSender().sendString(100023, (bonus == 15 ? "@whi@Check " : "Check ") + text[11]);
        player.getPacketSender().sendString(100024, (bonus == 17 ? "@whi@Check " : "Check ") + text[12]);

        ArrayList<ItemDefinition> objects = new ArrayList<>();

        for (ItemDefinition i : ItemDefinition.getDefinitions()) {
            if (i != null) {
                if (!i.isNoted() && i.getBonus()[bonus] > 0)
                    objects.add(i);
            }
        }

        objects.sort((p, p1) -> Double.compare(p1.getBonus()[bonus], p.getBonus()[bonus]));

        int interId = 100102;
        int size = (Math.min(objects.size(), 100));
        for (int i = 0; i < size; i++) {
            player.getPacketSender().sendString(interId++,
                    "" + objects.get(i).getBonus()[bonus]);
            player.getPacketSender().sendString(interId++, "" + objects.get(i).getName());
            player.getPacketSender().sendItemOnInterface(interId++, objects.get(i).getId(), 1);
            interId++;
        }
        player.getPacketSender().sendInterface(100000);

        player.getPacketSender().setScrollBar(100050, size * 40);

    }

}