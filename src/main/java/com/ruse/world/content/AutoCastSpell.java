package com.ruse.world.content;

import com.ruse.model.container.impl.Equipment;
import com.ruse.world.content.combat.magic.CombatSpells;
import com.ruse.world.entity.impl.player.Player;

public enum AutoCastSpell {

    STARTER(22092, CombatSpells.BEGINNER),
    STARTER2(22174, CombatSpells.BEGINNER),
    FESTIVE(14924, CombatSpells.BEGINNER),
    AZURE(3739, CombatSpells.AZURE),
    FLAMETHROWER(17712, CombatSpells.FLAMETHROWER),
    SORCERY(14377, CombatSpells.SORCERY),
    GANOPURP(8809, CombatSpells.PURPSTAFF),
    GANOPURP2(2198, CombatSpells.PURPSTAFF),
    CHAOS(22143, CombatSpells.CHAOS),
    CHAOS2(2378, CombatSpells.CHAOS),
    PATRONUM(17664, CombatSpells.PATRONUM),
    PATRONUM2(2108, CombatSpells.PATRONUM),
    EXECUTION(9942, CombatSpells.EXECUTION),
    LIGHT(17013, CombatSpells.LIGHTSANG),
    DEMON(22135, CombatSpells.DEMON),
    DEMON2(2278, CombatSpells.DEMON),
    SUPERBUU(16249, CombatSpells.SUPERBUU),
    ELITE(8412, CombatSpells.ELITE),
    GROUDON(13640, CombatSpells.GROUDON),
    GROUDON2(2396, CombatSpells.GROUDON),
    RUTHLESS(23145, CombatSpells.RUTHLESS),
    RUTHLESS2(2156, CombatSpells.RUTHLESS),
    MAGICSTAFF(23026, CombatSpells.MAGICSTAFF),
    GLAIVE(20558, CombatSpells.GLAIVE),
    ELITEGLAIVE(20551, CombatSpells.ELITEGLAIVE),
    DARKSANG(22114, CombatSpells.DARKSANG),
    TOXICSANG(20495, CombatSpells.TOXICSANG),
    VIRTUOSO(14305, CombatSpells.VIRTO),
    WHITESTAFF(22167, CombatSpells.WHITE),
    DEATH(14355, CombatSpells.WHITE),
    FAZULA(4071, CombatSpells.WHITE),
    OWNERS(22230, CombatSpells.WHITE),
    ;

    private int itemId;
    private CombatSpells spell;

    AutoCastSpell(int itemId, CombatSpells spell) {
        this.itemId = itemId;
        this.spell = spell;
    }

    public static CombatSpells getAutoCastSpell(Player player) {
            if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 18629) {
                return CombatSpells.MEDIUM;
            }
            for (AutoCastSpell d : AutoCastSpell.values()) {
                if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == d.getItemId()) {
                    return d.getSpell();
                }
            }
        return null;
    }


    public static AutoCastSpell getAutoCast(Player player) {
            for (AutoCastSpell d : AutoCastSpell.values()) {
                if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == d.getItemId()) {
                    return d;
                }
            }
        return null;
    }

    public int getItemId() {
        return itemId;
    }

    public CombatSpells getSpell() {
        return spell;
    }

}
