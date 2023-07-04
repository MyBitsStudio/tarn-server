package com.ruse.model.container.impl;

import com.ruse.model.Item;
import com.ruse.model.container.ItemContainer;
import com.ruse.model.container.StackType;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.slot.SlotBonus;
import com.ruse.world.packages.slot.SlotEffect;
import lombok.Getter;

/**
 * Represents a player's equipment item container.
 * 
 * @author relex lawl
 */

public class Equipment extends ItemContainer {

	//packet 159

	@Getter
	private final SlotBonus[] slotBonuses = new SlotBonus[15];

	/**
	 * The Equipment constructor.
	 * 
	 * @param player The player who's equipment is being represented.
	 */
	public Equipment(Player player) {
		super(player);
		for(int i = 0; i < slotBonuses.length; i++)
			slotBonuses[i] = new SlotBonus();
	}

	@Override
	public int capacity() {
		return 15;
	}

	@Override
	public StackType stackType() {
		return StackType.DEFAULT;
	}

	@Override
	public ItemContainer refreshItems() {
		getPlayer().getPacketSender().sendItemContainer(this, INVENTORY_INTERFACE_ID);
		return this;
	}
	//alright, so that sword isnt supposed to tbe that rarity? yeah no, if it has a glow = rarity, no glow = no rarity, basically player gets a random chance to receive rarity either from boxes or drops,

	@Override
	public Equipment full() {
		return this;
	}

	/**
	 * The equipment inventory interface id.
	 */
	public static final int INVENTORY_INTERFACE_ID = 1688;

	/**
	 * The helmet slot.
	 */
	public static final int HEAD_SLOT = 0;

	/**
	 * The cape slot.
	 */
	public static final int CAPE_SLOT = 1;

	/**
	 * The amulet slot.
	 */
	public static final int AMULET_SLOT = 2;

	/**
	 * The weapon slot.
	 */
	public static final int WEAPON_SLOT = 3;

	/**
	 * The chest slot.
	 */
	public static final int BODY_SLOT = 4;

	/**
	 * The shield slot.
	 */
	public static final int SHIELD_SLOT = 5;

	public static final int AURA_SLOT = 6;

	/**
	 * The bottoms slot.
	 */
	public static final int LEG_SLOT = 7;

	public static final int ENCHANTMENT_SLOT = 8;

	/**
	 * The gloves slot.
	 */
	public static final int HANDS_SLOT = 9;

	/**
	 * The boots slot.
	 */
	public static final int FEET_SLOT = 10;

	public static final int HALO_SLOT = 11;

	/**
	 * The rings slot.
	 */
	public static final int RING_SLOT = 12;

	/**
	 * The arrows slot.
	 */
	public static final int AMMUNITION_SLOT = 13;

	public static final int GEMSTONE_SLOT = 14;

	public boolean wearingNexAmours() {
		int head = getPlayer().getEquipment().getItems()[HEAD_SLOT].getId();
		int body = getPlayer().getEquipment().getItems()[BODY_SLOT].getId();
		int legs = getPlayer().getEquipment().getItems()[LEG_SLOT].getId();
		boolean torva = head == 14008 && body == 14009 && legs == 14010;
		boolean pernix = head == 14011 && body == 14012 && legs == 14013;
		boolean virtus = head == 14014 && body == 14015 && legs == 14016;
		return torva || pernix || virtus;
	}

	/**
	 * Gets the amount of item of a type a player has, for example, gets how many
	 * Zamorak items a player is wearing for GWD
	 * 
	 * @param p The player
	 * @param s The item type to search for
	 * @return The amount of item with the type that was found
	 */
	public static int getItemCount(Player p, String s, boolean inventory) {
		int count = 0;
		for (Item t : p.getEquipment().getItems()) {
			if (t == null || t.getId() < 1 || t.getAmount() < 1)
				continue;
			if (t.getDefinition().getName().toLowerCase().contains(s.toLowerCase()))
				count++;
		}
		if (inventory)
			for (Item t : p.getInventory().getItems()) {
				if (t == null || t.getId() < 1 || t.getAmount() < 1)
					continue;
				if (t.getDefinition().getName().toLowerCase().contains(s.toLowerCase()))
					count++;
			}
		return count;
	}

	/**
	 * Booleans for effects
	 */

	public boolean hasDoubleKills(){
		for (SlotBonus slotBonus : slotBonuses) {
			if (slotBonus.getEffect() == SlotEffect.MULTI_KILLS) {
				return true;
			}
		}
		return false;
	}

	public boolean hasTripleKills(){
		for (SlotBonus slotBonus : slotBonuses) {
			if (slotBonus.getEffect() == SlotEffect.MULTI_KILLS) {
				if(slotBonus.getBonus() == 2)
					return true;
			}
		}
		return false;
	}

	public boolean hasQuadKills(){
		for (SlotBonus slotBonus : slotBonuses) {
			if (slotBonus.getEffect() == SlotEffect.MULTI_KILLS) {
				if(slotBonus.getBonus() == 3)
					return true;
			}
		}
		return false;
	}

	public boolean hasAoE(){
		return slotBonuses[WEAPON_SLOT].getEffect() == SlotEffect.AOE_EFFECT;
	}

	public boolean hasDoubleCash(){
		for (SlotBonus slotBonus : slotBonuses) {
			if (slotBonus.getEffect() == SlotEffect.DOUBLE_CASH) {
				return true;
			}
		}
		return false;
	}

	public boolean hasDoubleXP(){
		for (SlotBonus slotBonus : slotBonuses) {
			if (slotBonus.getEffect() == SlotEffect.DOUBLE_XP) {
				return true;
			}
		}
		return false;
	}

	public int getDamageBonus() {
		for (SlotBonus slotBonus : slotBonuses) {
			if (slotBonus.getEffect() == SlotEffect.ALL_DAMAGE) {
				return slotBonus.getBonus();
			}
		}
		return 0;
	}

	public int getDoubleDrop(){
		int bonus = 0;
		for (SlotBonus slotBonus : slotBonuses) {
			if (slotBonus.getEffect() == SlotEffect.DOUBLE_DROP) {
				bonus += slotBonus.getBonus();
			}
		}
		return bonus;
	}

	public int getDropRateBonus() {
		int bonus = 0;
		for (SlotBonus slotBonus : slotBonuses) {
			if (slotBonus.getEffect() == SlotEffect.DROP_RATE_LOW || slotBonus.getEffect() == SlotEffect.DROP_RATE_MED || slotBonus.getEffect() == SlotEffect.DROP_RATE_HIGH) {
				bonus += slotBonus.getBonus();
			}
		}
		return bonus;
	}
}
