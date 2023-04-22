package com.ruse.model.input;

import com.ruse.model.projectile.ItemEffect;

/**
 * Handles entering amounts
 * 
 * @author Gabriel Hannason
 */
public abstract class EnterAmount extends Input {

	private int item, slot;
	private ItemEffect effect;

	public int getItem() {
		return item;
	}

	public int getSlot() {
		return slot;
	}

	public ItemEffect getEffect() {
		return effect;
	}

	public EnterAmount() {
	}

	public EnterAmount(int item) {
		this.item = item;
	}

	public EnterAmount(int item, int slot) {
		this.item = item;
		this.slot = slot;
	}

	public EnterAmount(int item, ItemEffect effect) {
		this.item = item;
		this.effect = effect;
	}
	public EnterAmount(int item, int slot, ItemEffect effect) {
		this.item = item;
		this.slot = slot;
	}

}
