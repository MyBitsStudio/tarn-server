package com.ruse.model.container.impl;

import com.google.common.base.Objects;
import com.ruse.model.Flag;
import com.ruse.model.Item;
import com.ruse.model.container.ItemContainer;
import com.ruse.model.container.StackType;
import com.ruse.model.definitions.WeaponAnimations;
import com.ruse.model.definitions.WeaponInterfaces;
import com.ruse.world.content.BonusManager;
import com.ruse.world.content.combat.magic.Autocasting;
import com.ruse.world.content.combat.weapon.CombatSpecial;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.drops.DropCalculator;
import com.ruse.world.packages.combat.max.MagicMax;
import com.ruse.world.packages.combat.max.MeleeMax;
import com.ruse.world.packages.combat.max.RangeMax;
import com.ruse.world.packages.combat.sets.SetBonus;
import com.ruse.world.packages.combat.sets.SetBonuses;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.dialogue.impl.slot.UnequipSlotPerk;
import com.ruse.world.packages.slot.PerkEquip;
import com.ruse.world.packages.slot.SlotBonus;
import com.ruse.world.packages.slot.SlotEffect;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a player's equipment item container.
 * 
 * @author relex lawl
 */

public class Equipment extends ItemContainer {

	//packet 159

	@Getter
	private final SlotBonus[] slotBonuses = new SlotBonus[15];

	@Getter
	@Setter
	private SetBonus bonus;

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
		getPlayer().getPacketSender().sendEquipment();
		checkBonus();
		equipStats();
		return this;
	}

	private void equipStats(){
		getPlayer().getPacketSender().sendTooltip(162602, "Melee Max : "+ BonusManager.formatNumber((MeleeMax.newMelee(getPlayer(), getPlayer()))));
		getPlayer().getPacketSender().sendTooltip(162603, "Magic Max : "+ BonusManager.formatNumber((MagicMax.newMagic(getPlayer(), getPlayer()))));
		getPlayer().getPacketSender().sendTooltip(162604, "Ranged Max : "+ BonusManager.formatNumber((RangeMax.newRange(getPlayer(), getPlayer()))));
		getPlayer().getPacketSender().sendTooltip(162605, "Drop Rate : "+ DropCalculator.getDropChance(getPlayer(), 9837));
		getPlayer().getPacketSender().sendTooltip(162606, "Double Drop : "+ DropCalculator.getDoubleDropChance(getPlayer(), 9837));
		getPlayer().getPacketSender().sendTooltip(162607, "Set Bonus : "+ (getBonus() != null ? getBonus().name() : "None"));
	}

	@Override
	public Equipment full() {
		return this;
	}

	public void setSlots(SlotBonus[] slots){
		System.arraycopy(slots, 0, slotBonuses, 0, slotBonuses.length);
	}

	/**
	 * The equipment inventory interface id.
	 */
	public static final int INVENTORY_INTERFACE_ID = 31444;

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

	public String slotToName(int slot){
		return switch(slot){
			case HEAD_SLOT -> "Head";
			case CAPE_SLOT -> "Cape";
			case AMULET_SLOT -> "Amulet";
			case WEAPON_SLOT -> "Weapon";
			case BODY_SLOT -> "Body";
			case SHIELD_SLOT -> "Shield";
			case ENCHANTMENT_SLOT -> "Aura";
			case LEG_SLOT -> "Legs";
			case AURA_SLOT -> "Enchantment";
			case HANDS_SLOT -> "Hands";
			case FEET_SLOT -> "Feet";
			case HALO_SLOT -> "Halo";
			case RING_SLOT -> "Ring";
			case AMMUNITION_SLOT -> "Ammunition";
			case GEMSTONE_SLOT -> "Gemstone";
			default -> throw new IllegalStateException("Unexpected value: " + slot);
		};
	}

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

	public boolean hasTripleShot(){
		return slotBonuses[WEAPON_SLOT].getEffect() == SlotEffect.MULTI_SHOT
				&& slotBonuses[WEAPON_SLOT].getBonus() == 3;
	}

	public boolean hasDoubleShot(){
		return slotBonuses[WEAPON_SLOT].getEffect() == SlotEffect.MULTI_SHOT
				&& slotBonuses[WEAPON_SLOT].getBonus() == 2;
	}

	public boolean hasDoubleCash(){
		for (SlotBonus slotBonus : slotBonuses) {
			if (slotBonus.getEffect() == SlotEffect.DOUBLE_CASH) {
				return true;
			}
		}
		return false;
	}

	public boolean hasTripleCash(){
		for (SlotBonus slotBonus : slotBonuses) {
			if (slotBonus.getEffect() == SlotEffect.TRIPLE_CASH) {
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

	public boolean hasDoubleSlayer(){
		for (SlotBonus slotBonus : slotBonuses) {
			if (slotBonus.getEffect() == SlotEffect.DOUBLE_SLAYER_TICK) {
				return true;
			}
		}
		return false;
	}

	public boolean hasFirewall(){
		for (SlotBonus slotBonus : slotBonuses) {
			if (slotBonus.getEffect() == SlotEffect.FIREWALL) {
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

	public boolean hasCollector(){
		return containsAny(14880);
	}

	private void handleUnequip(int slot, int id){
		slot = convertSlotFromClient(slot);
		Item item = slot < 0 ? null : getItems()[slot];
		assert item != null;
		item = new Item(item.getId(), item.getAmount());
		if (item.getId() != id)
			return;
		boolean stackItem = item.getDefinition().isStackable() && getPlayer().getInventory().getAmount(item.getId()) > 0;
		int inventorySlot = getPlayer().getInventory().getEmptySlot();
		if (inventorySlot == -1) {
			getPlayer().getInventory().full();
		} else {
			Item itemReplacement = new Item(-1, 0);
			setItem(slot, itemReplacement);
			if (!stackItem)
				getPlayer().getInventory().setItem(inventorySlot, item);
			else
				getPlayer().getInventory().add(item.getId(), item.getAmount());
			if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
				WeaponInterfaces.assign(getPlayer(), getPlayer().getEquipment().get(Equipment.WEAPON_SLOT));
				WeaponAnimations.update(getPlayer());
				if (getPlayer().getAutocastSpell() != null || getPlayer().isAutocast()) {
					Autocasting.resetAutocast(getPlayer(), true);
					getPlayer().getPacketSender().sendMessage("Autocast spell cleared.");
				}
				getPlayer().setSpecialActivated(false);
				CombatSpecial.updateBar(getPlayer());
			}
			BonusManager.update(getPlayer());
			refreshItems();
			getPlayer().getInventory().refreshItems();
			getPlayer().getUpdateFlag().flag(Flag.APPEARANCE);
		}
	}

	private void checkBonus(){
		bonus = SetBonuses.checkBonus(getPlayer());
	}

	private void sendSlotPerk(int slot){
		getPlayer().getPacketSender().sendInterface(162650);
		getPlayer().getPacketSender().sendString(162652, getSlotName(slot));

		slot = convertSlotFromClient(slot);
		getPlayer().getVariables().setSetting("slot-chosen", slot);

		if(slotBonuses[slot] == null) {
			getPlayer().sendMessage("Something went wrong here.");
		} else {
			SlotBonus bonus = slotBonuses[slot];
			if(Objects.equal(bonus.getEffect(), SlotEffect.NOTHING)) {
				getPlayer().getPacketSender().sendString(162656, "NONE");
				getPlayer().getPacketSender().sendString(162658, "BONUS : 0%");
				getPlayer().getPacketSender().sendString(162659, "RARITY : ");
			} else {
				getPlayer().getPacketSender().sendString(162656, bonus.getEffect().name().replace("_", " "));
				getPlayer().getPacketSender().sendString(162658, "BONUS : "+bonus.getBonus() + "%");
				getPlayer().getPacketSender().sendString(162659, "RARITY : "+bonus.getEffect().getRarity().name());
			}
		}
	}

	private void sendAllPerks(){
		getPlayer().getPacketSender().sendInterface(162700);
	}

	public boolean handleContainer(int slot, int option, int id){
		if(slot >= 0 && slot <= 14){
			if(option == 1){
				handleUnequip(slot, id);
				return true;
			}
			if(option == 3){
				sendSlotPerk(slot);
				return true;
			}
		}
		return false;
	}

	public boolean handleClicks(int button){
		if(button >= 162501 && button <= 162515){
			sendSlotPerk(button - 162501);
			return true;
		}
		if(button >= 162551 && button <= 162565){
			if(getPlayer().getInterfaceId() == 162750){
				PerkEquip.handleButton(getPlayer(), button - 162551);
				return true;
			}
		}
		switch(button){
			case 162612 -> {
				if(getPlayer().isSecondaryEquipment()) {
					getPlayer().sendMessage("Switched back to original equip");
					getPlayer().setIsSecondaryEquipment(false);
				} else {
					getPlayer().sendMessage("Switched to second equip");
					getPlayer().setIsSecondaryEquipment(true);
				}
				getPlayer().getEquipment().refreshItems();
				getPlayer().getUpdateFlag().flag(Flag.APPEARANCE);
				return true;
			}
			case 162610 -> {
				if (getPlayer().getInterfaceId() == -1) {
					getPlayer().getSkillManager().stopSkilling();
					BonusManager.update(getPlayer());
					getPlayer().getPacketSender().sendInterface(21172);
				} else
					getPlayer().getPacketSender().sendMessage("Please close the interface you have open before doing this.");

				return true;
			}
			case 162611 ->{
				getPlayer().getEquipmentEnhancement().openInterface();
				return true;
			}
			case 162613 -> {
				sendAllPerks();
				return true;
			}
			case 162660 -> {
				int slot = getPlayer().getVariables().getIntValue("slot-chosen");
				if(slot == -1){
					getPlayer().sendMessage("Something went wrong here.");
				} else {
					SlotBonus bonus = slotBonuses[slot];
					if(bonus == null){
						getPlayer().sendMessage("Something went wrong here..");
					} else if (bonus.getEffect() == SlotEffect.NOTHING) {
						getPlayer().sendMessage("There isn't a perk to remove.");
					} else {
						DialogueManager.sendDialogue(getPlayer(), new UnequipSlotPerk(getPlayer()), -1);
					}
				}
				return true;
			}
		}
		return false;
	}

	public int convertSlotFromClient(int slot){
		return switch(slot){
			case 0 -> HALO_SLOT;
			case 1 -> HEAD_SLOT;
			case 2 -> GEMSTONE_SLOT;
			case 3 -> CAPE_SLOT;
			case 4 -> AMULET_SLOT;
			case 5 -> AMMUNITION_SLOT;
			case 6 -> WEAPON_SLOT;
			case 7 -> BODY_SLOT;
			case 8 -> SHIELD_SLOT;
			case 11 -> ENCHANTMENT_SLOT;
			case 10 -> LEG_SLOT;
			case 9 -> AURA_SLOT;
			case 12 -> HANDS_SLOT;
			case 13 -> FEET_SLOT;
			case 14 -> RING_SLOT;
			default -> throw new IllegalStateException("Unexpected value: " + slot);
		};
	}

	private String getSlotName(int slot){
		return switch(slot){
			case 0 -> "Halo";
			case 1 -> "Head";
			case 2 -> "Gems";
			case 3 -> "Cape";
			case 4 -> "Amulet";
			case 5 -> "Ammo";
			case 6 -> "Weapon";
			case 7 -> "Body";
			case 8 -> "Shield";
			case 9 -> "Enchant";
			case 10 -> "Legs";
			case 11 -> "Aura";
			case 12 -> "Hands";
			case 13 -> "Feet";
			case 14 -> "Ring";
			default -> throw new IllegalStateException("Unexpected value: " + slot);
		};
	}
}
