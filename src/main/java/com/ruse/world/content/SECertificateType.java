package com.ruse.world.content;

import com.google.common.collect.ImmutableList;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.entity.impl.player.Player;

import java.util.Optional;

public enum SECertificateType {
    HELMET(22214, 0),
    NECKLACE(22215, 2),
    BODY(22216, 4),
    LEGS(22217, 7),
    BOOTS(22218, 10),
    HALO(22219, 11),
    CAPE(22220, 1),
    WEAPON(22221, 3),
    AURA(22222, 6),
    GLOVE(22223, 9),
    GEMSTONE(22224, 14),
    ARROW(22225, 13),
    SHIELD(22226, 5),
    ENCHANTMENT(22227, 8),
    RING(22228, 12);

    private final int itemId;
    private final int slot;

    SECertificateType(int itemId, int slot) {
        this.itemId = itemId;
        this.slot = slot;
    }

    private static final ImmutableList<SECertificateType> SECertificates = ImmutableList.copyOf(SECertificateType.values());

    public static boolean playerConsume(Player player, int itemId) {
        Optional<SECertificateType> possibleCert = SECertificates.stream().filter(seCertificateType -> seCertificateType.itemId == itemId).findAny();

        if(possibleCert.isPresent()) {
            SECertificateType cert = possibleCert.get();

            if(!player.getSecondaryEquipmentUnlocks()[cert.slot] && player.getInventory().contains(cert.itemId)) {
                player.getInventory().delete(cert.itemId, 1);
                player.getSecondaryEquipmentUnlocks()[cert.slot] = true;
                player.sendMessage("@red@You have consumed a " + ItemDefinition.forId(cert.itemId).getName());
            } else {
                player.sendMessage("@red@Unable to consume this certificate");
            }

            return true;
        }

        return false;
    }
}
