package com.ruse.model;

import com.ruse.model.container.impl.Equipment;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.projectile.ItemEffect;
import com.ruse.util.Misc;
import com.ruse.world.content.casketopening.Box;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ItemRarity {

	COMMON(39),
	UNCOMMON(61),
	RARE(73),
	LEGENDARY(82),
	MYTHIC(88);

	private final double percentage;

	ItemRarity(double percentage) {
		this.percentage = percentage;
	}

	public double getPercentage() {
		return this.percentage;
	}

	public static int[] restrictLegendaryAndMythic = {
			6692, 9028, 9029, 9030, 8014, 8003, 202, 811, 9815, 9817, 9920, 3831, 9025, 9026, 9836, 92, 3313, 8008, 1906, 11, 9836, 92, 3313, 8008, 1906, 9915, 2342, 9024, 9916, 9918,
				9919, 9914
	};

	public static void main(String[] args) {
		ItemDefinition.init();
		Item item = new Item(4151);
		for(int i = 0; i < 100; i++) {
			double ran = Misc.getRandom(0, 100);
			ItemRarity rarity;
			int random = new SecureRandom().nextInt(100);
			if(random > ItemRarity.RARE.getPercentage()) {
				rarity = ItemRarity.RARE;
			} else if(random >  ItemRarity.UNCOMMON.getPercentage()) {
				rarity = ItemRarity.UNCOMMON;
			} else {
				rarity = ItemRarity.COMMON;
			}

			List<ItemEffect> list = new ArrayList<>();
			for(ItemEffect effect : ItemEffect.values()) {
				if(effect == null)
					continue;
				if(effect.getRarity() != rarity)
					continue;
				if(effect == ItemEffect.AOE_EFFECT && item.getDefinition().getEquipmentSlot() != Equipment.WEAPON_SLOT)
					continue;
				list.add(effect);
			}
			ItemEffect effect = Misc.randomElement(list);
		}
	}

	public static ItemRarity getRarityForPercentage(double p) {
		return p >= ItemRarity.MYTHIC.getPercentage() ? ItemRarity.MYTHIC : p >= ItemRarity.LEGENDARY.getPercentage() ? ItemRarity.LEGENDARY :
				p >= ItemRarity.RARE.getPercentage() ? ItemRarity.RARE : p >= ItemRarity.UNCOMMON.getPercentage()  ? ItemRarity.UNCOMMON : ItemRarity.COMMON;
	}

	public static ItemEffect getRandomEffectForRarity(Item item, ItemRarity rarity, int npcId) {
		if(ItemEffect.hasNoEffect(item.getId()) || item.getDefinition().isNoted())
			return ItemEffect.NOTHING;
		if(Arrays.stream(restrictLegendaryAndMythic).anyMatch(it -> it==npcId)) {
			int random = new SecureRandom().nextInt(100);
			if(random > ItemRarity.RARE.getPercentage()) {
				rarity = ItemRarity.RARE;
			} else if(random >  ItemRarity.UNCOMMON.getPercentage()) {
				rarity = ItemRarity.UNCOMMON;
			} else {
				rarity = ItemRarity.COMMON;
			}
		}
		List<ItemEffect> list = new ArrayList<>();
		for(ItemEffect effect : ItemEffect.values()) {
			if(effect == null)
				continue;
			if(effect.getRarity() != rarity)
				continue;
			if(effect == ItemEffect.AOE_EFFECT && item.getDefinition().getEquipmentSlot() != Equipment.WEAPON_SLOT)
				continue;
			if(effect == ItemEffect.AOE_EFFECT_2x2 && item.getDefinition().getId() != 20504)
				continue;
			list.add(effect);
		}
		return list.get(Misc.random(list.size() - 1));
	}

	public static ItemRarity getItemRarityForName(String name) {
		return Arrays.stream(values())
				.filter(rarity1 -> rarity1.toString().equalsIgnoreCase(name))
				.findFirst().orElse(ItemRarity.COMMON);
	}
}