package com.ruse.model;

import com.ruse.GameSettings;
import com.ruse.ReducedSellPrice;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.projectile.ItemEffect;
import com.ruse.util.Misc;
import com.ruse.util.StringUtils;
import com.ruse.world.content.Effigies;

/**
 * Represents an item which is owned by a player.
 *
 * @author relex lawl
 */

public class Item {

	private int id, amount, slot;

	private ItemBonus bonus;

	/**
	 * An Item object constructor.
	 *
	 * @param id     Item id.
	 * @param amount Item amount.
	 */
	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	public Item(int id, int amount, ItemEffect effect, int bonus, ItemRarity rarity){
		this(id, amount, effect, bonus);
		if(this.bonus == null)
			this.bonus = new ItemBonus(rarity, effect, bonus);
		else{
			this.bonus.setEffect(effect, bonus);
		}
	}

	public Item(int id, int amount, ItemEffect effect, int bonus) {
		this(id, amount);
		if(this.bonus == null)
			this.bonus = new ItemBonus(effect, bonus);
		else
			this.bonus.setEffect(effect, bonus);
	}

	public Item(int id, int amount, ItemBonus bonus){
		this(id, amount);
		this.bonus = bonus;
	}

	/**
	 * An Item object constructor.
	 *
	 * @param id Item id.
	 */
	public Item(int id) {
		this(id, 1);
	}

	public int getId() {
		return id;
	}


	public int getAmount() {
		return amount;
	}

	public int getSlot() {
		return this.slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public Item setId(int id) {
		this.id = id;
		return this;
	}

	public ItemBonus getItemBonus(){ return this.bonus;}

	/**
	 * Sets the amount of the item.
	 */
	public Item setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	/**
	 * Gets item's definition.
	 */
	public ItemDefinition getDefinition() {
		return ItemDefinition.forId(id);
	}

	public boolean tradeable() {
		String name = getDefinition().getName().toLowerCase();
		/*if (name.contains("clue scroll"))
			return false;
		if (name.contains("overload") && !name.contains("infinite"))
			return false;
		if (name.toLowerCase().contains("(deg)") || name.toLowerCase().contains("brawling"))
			return false;
		if (name.toLowerCase().contains("pet"))
			return false;*/

		for (int i : GameSettings.UNTRADEABLE_ITEMS) {
			if (id == i)
				return false;
		}
		if (Effigies.isEffigy(id))
			return false;
		return true;
	}

	public boolean reducedPrice() {
		for (ReducedSellPrice r : ReducedSellPrice.values()) {
			if (r.getUnNotedId() == id || r.getNotedId() == id) {
				return true;
			}
		}
		return false;
	}

	public boolean sellable() {
		String name = getDefinition().getName().toLowerCase();
		if (name.contains("clue scroll"))
			return false;
		if (name.contains("overload") || name.contains("extreme"))
			return false;
		if (name.toLowerCase().contains("(deg)") || name.toLowerCase().contains("brawling"))
			return false;
		for (int i : GameSettings.UNTRADEABLE_ITEMS) {
			if (id == i)
				return false;
		}
		for (int i : GameSettings.UNSELLABLE_ITEMS) {
			if (id == i)
				return false;
		}
		if (Effigies.isEffigy(id))
			return false;
		return true;
	}

	public static Item getNoted(int id, int amount) {
		int notedItem = id + 1;
		if (ItemDefinition.forId(notedItem).getName().equals(ItemDefinition.forId(id).getName())) {
			return new Item(notedItem, amount);
		}
		return new Item(id, amount);
	}

	public static int getNoted(int id) {
		int noted = id + 1;
		if (id == 11283 || id == 11284) {
			noted = 11285;
		}
		if (ItemDefinition.forId(noted).getName().equals(ItemDefinition.forId(id).getName())) {
			// // System.out.println("getNoted has returned "+noted);
			return noted;
		}
		// // System.out.println("getNoted has not returned anything good.");
		return id;
	}

	public static int getUnNoted(int id) {
		int unNoted = id - 1;
		if (id == 11284 || id == 11285) {
			unNoted = 11283;
		}
		if (ItemDefinition.forId(unNoted).getName().equals(ItemDefinition.forId(id).getName())) {
			return unNoted;
		}
		return id;
	}

	public static boolean tradeable(int item) {
		return new Item(item).tradeable();
	}

	public static boolean sellable(int item) {
		return new Item(item).sellable();
	}

	/**
	 * Copying the item by making a new item with same values.
	 */
	public Item copy() {
		return new Item(id, amount, bonus);
	}

	/**
	 * Increment the amount by 1.
	 */
	public void incrementAmount() {
		if ((amount + 1) > Integer.MAX_VALUE) {
			return;
		}
		amount++;
	}

	/**
	 * Decrement the amount by 1.
	 */
	public void decrementAmount() {
		if ((amount - 1) < 0) {
			return;
		}
		amount--;
	}

	/**
	 * Increment the amount by the specified amount.
	 */
	public void incrementAmountBy(int amount) {
		if ((this.amount + amount) > Integer.MAX_VALUE) {
			this.amount = Integer.MAX_VALUE;
		} else {
			this.amount += amount;
		}
	}

	/**
	 * Decrement the amount by the specified amount.
	 */
	public void decrementAmountBy(int amount) {
		if ((this.amount - amount) < 1) {
			this.amount = 0;
		} else {
			this.amount -= amount;
		}
	}

	public void setEffect(ItemEffect effect){
		this.bonus = new ItemBonus(effect);
	}

	public void setDefaultEffect(ItemEffect effect){
		this.bonus = new ItemBonus(effect);
	}

	public ItemEffect getEffect(){
		return this.bonus == null ? ItemEffect.NOTHING : this.bonus.getEffect();
	}

	public void setBonus(int bonus){
		this.bonus.setBonus(bonus);
	}

	public Item setRarity(ItemRarity rarity){
		if(this.bonus == null){
			this.bonus = new ItemBonus(ItemEffect.NOTHING);
		}
		this.bonus.setRarity(rarity);
		return this;
	}

	public ItemRarity getRarity() {
		return this.bonus == null ? ItemRarity.COMMON : this.bonus.getRarity();
	}

	public int getBonus(){ return this.bonus == null ? 0 : this.bonus.getBonus();}

	@Override
	public String toString() {
		return "Item{" +
				"id=" + id +
				", amount=" + amount +
				'}';
	}
}