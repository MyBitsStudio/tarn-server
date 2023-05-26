package com.ruse.world.content.equipmentenhancement;

import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.var;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
@Setter
public class EquipmentEnhancement {

    private static final int INTERFACE_ID = 49400;
    private static final int BASE_CASH_REQUIREMENT = 100000;
    private static final int BASE_OTHER_REQUIREMENT = 100;
    private static final double CASH_REQUIREMENT_MULTI = 300000.0;
    private static final double OTHER_REQUIREMENT_MULTI = 233.0;
    private static final int DROP_RATE_BOOST_ADD = 2;
    private static final int CASH_BOOST_ADD = 5;
    private static final int STATS_BOOST_ADD = 3;
    private static final int CASH_ID = 10835;
    private static final int OTHER_ID = 13727;
    private static final int BASE_CHANCE = 80;

    // don't change
    private static final int MAX_LEVEL = 15;

    private final Player player;
    private long boost;
    private long slotLevel;
    private SlotType selectedSlot;
    private int upgradeChance;
    private int cashRequirement;
    private int otherRequirement;
    private int gemId;

    public int getSlotLevel(int slot) {
        return (int) ((slotLevel >>> (slot << 2)) & 0xF);
    }

    private void setSlotLevel(int slot, int level) {
        var position = slot << 2;
        slotLevel &= ~(0xFL << position);
        slotLevel |= (long) (level & 0xF) << position;
    }

    public int getBoost(@NotNull BoostType boost) {
        var shift = boost.getPosition() * 21;
        var mask = 0x1FFFFFL << shift;
        return (int) ((this.boost & mask) >>> shift);
    }

    @Contract(mutates = "this")
    private void setBoostValue(@NotNull BoostType boost, int value) {
        var shift = boost.getPosition() * 21;
        var mask = ~(0x1FFFFFL << shift);
        this.boost &= mask;
        this.boost |= (long) value << shift;
    }

    private int getBoostBySlot(@NotNull BoostType boost, int slot) {
        var level = getSlotLevel(slot);
        int amount = 0;
        switch(boost) {
            case DR:
                return level * DROP_RATE_BOOST_ADD;
            case CASH:
                return level * CASH_BOOST_ADD;
            case STATS:
                return level * STATS_BOOST_ADD;
        }
        return amount;
    }

    private int calculateCashRequirement(int slot) {
        return (int) (getSlotLevel(slot) * CASH_REQUIREMENT_MULTI) + BASE_CASH_REQUIREMENT;
    }

    private int calculateOtherRequirement(int slot) {
        return (int) (getSlotLevel(slot) * OTHER_REQUIREMENT_MULTI) + BASE_OTHER_REQUIREMENT;
    }

    private int calculateChance(int slot) {
        return BASE_CHANCE - (getSlotLevel(slot) * 5);
    }

    private int getUpgradeGemId(int slot) {
        int level = getSlotLevel(slot);
        if(level >= 10 && slot < 15) {
            return UpgradeGemType.STRONG_GEM.getItemId();
        } else if(level >= 5 && level < 10) {
            return UpgradeGemType.MEDIUM_GEM.getItemId();
        } else {
            return UpgradeGemType.WEAK_GEM.getItemId();
        }
    }

    public boolean handleButtonClick(int id) {
        if(player.getInterfaceId() != INTERFACE_ID) return false;
        if(id == -16094) {
            attemptUpgrade();
            return true;
        }
        var slotTypeOptional = SlotType.VALUES
                .stream()
                .filter(slotType -> slotType.getBtn() == id)
                .findFirst();

        if(slotTypeOptional.isPresent()) {
            selectSlot(slotTypeOptional.get());
            return true;
        }
        return false;
    }

    private void attemptUpgrade() {
        if(selectedSlot == null) return;
        var slotId = selectedSlot.getId();
        var currentLevel = getSlotLevel(slotId);
        if(currentLevel == MAX_LEVEL) {
            player.getPacketSender().sendMessage("You already have the maximum level for this slot.");
            return;
        }
        if(!hasRequiredItems()) {
            player.getPacketSender().sendMessage("@red@You do not have the required items for this upgrade.");
            return;
        }
        var success = isSuccessful();
        player.getInventory().delete(CASH_ID, cashRequirement).delete(OTHER_ID, otherRequirement).delete(gemId, 1);
        if(!success) {
            player.getPacketSender().sendMessage("@red@You have failed this upgrade.");
            return;
        }
        var newLevel = ++currentLevel;
        setSlotLevel(slotId, currentLevel);
        player.getPacketSender().sendMessage("@red@You have successfully upgraded to level " + newLevel + "!");
        player.getSeasonPass().incrementExp(1780 * newLevel, false);
        var currentDr = getBoost(BoostType.DR);
        var currentCash = getBoost(BoostType.CASH);
        var currentStats = getBoost(BoostType.STATS);
        var newDr = currentDr + DROP_RATE_BOOST_ADD;
        var newCash = currentCash + CASH_BOOST_ADD;
        var newStats = currentStats + STATS_BOOST_ADD;
        setBoostValue(BoostType.DR, newDr);
        setBoostValue(BoostType.CASH, newCash);
        setBoostValue(BoostType.STATS, newStats);
        calculateRequirements();
    }

    public void openInterface() {
        selectSlot(SlotType.AMULET);
        player.getPacketSender().sendInterface(INTERFACE_ID);
    }

    public void selectSlot(SlotType slotType) {
        selectedSlot = slotType;
        calculateRequirements();
    }

    public void calculateRequirements() {
        var id = selectedSlot.getId();
        var drBoost = getBoostBySlot(BoostType.DR, id);
        var cashBoost = getBoostBySlot(BoostType.CASH, id);
        var statsBoost = getBoostBySlot(BoostType.STATS, id);
        upgradeChance = calculateChance(id);
        cashRequirement = calculateCashRequirement(id);
        otherRequirement = calculateOtherRequirement(id);
        gemId = getUpgradeGemId(id);
        player.getPacketSender()
                .sendSpriteChange(49440, selectedSlot.getSpriteId())
                .sendString(49409, selectedSlot.getName() + "@cya@ (+"+(getSlotLevel(id))+")")
                .sendString(49427, "Success rate: " + upgradeChance + "%")
                .sendString(49431, drBoost + "%")
                .sendString(49432, cashBoost + "%")
                .sendString(49433, statsBoost + "%")
                .sendString(49435, (drBoost + DROP_RATE_BOOST_ADD) + "%")
                .sendString(49436, (cashBoost + CASH_BOOST_ADD) + "%")
                .sendString(49434, (statsBoost + STATS_BOOST_ADD) + "%")
                .sendItemOnInterface(49441, gemId, 0, 1, 0)
                .sendItemOnInterface(49441, CASH_ID, 1, cashRequirement, 0)
                .sendItemOnInterface(49441, OTHER_ID, 2, otherRequirement, 0);
    }

    private boolean isSuccessful() {
        return upgradeChance > (int) (Math.random() * 100);
    }

    private boolean hasRequiredItems() {
        var inventory = player.getInventory();
        return inventory.getAmount(CASH_ID) >= cashRequirement && inventory.getAmount(OTHER_ID) >= otherRequirement && inventory.contains(gemId);
    }
}

