package com.ruse.model;

import com.ruse.GameSettings;
import com.ruse.ReducedSellPrice;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import lombok.Getter;

/**
 * Represents an item which is owned by a player.
 *
 * @author relex lawl
 */

@Getter
public class Item {

	private int id, amount, slot;
	private String uid;

	/**
	 * An Item object constructor.
	 *
	 * @param id     Item id.
	 * @param amount Item amount.
	 */
	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
		this.uid = "stale";
	}

	public Item(int id, int amount, String uid) {
		this.id = id;
		this.amount = amount;
		this.uid = uid;
	}

	/**
	 * An Item object constructor.
	 *
	 * @param id Item id.
	 */
	public Item(int id) {
		this(id, 1);
	}


	public void setSlot(int slot) {
		this.slot = slot;
	}

	public void setUid(String uid){
		this.uid = uid;
	}

	public Item setId(int id) {
		this.id = id;
		return this;
	}

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
		for (int i : GameSettings.UNTRADEABLE_ITEMS) {
			if (id == i)
				return false;
		}
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
		return new Item(id, amount, uid);
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
	 * Decrement the amount by the specified amount.
	 */
	public void decrementAmountBy(int amount) {
		if ((this.amount - amount) < 1) {
			this.amount = 0;
		} else {
			this.amount -= amount;
		}
	}

	@Override
	public String toString() {
		return "Item{" +
				"id=" + id +
				", amount=" + amount +
				'}';
	}
}