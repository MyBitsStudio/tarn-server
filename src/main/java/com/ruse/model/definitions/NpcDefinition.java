package com.ruse.model.definitions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ruse.util.JsonLoader;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A better npc def file
 * 
 * @author Corrupt
 */
@Getter
@Setter
public class NpcDefinition {

	public static int MAX_NPCs = 6325; // Only used by dropsinterface.java

	public static NpcDefinition[] definitions = new NpcDefinition[14500];

	@Contract(pure = true)
	public static @Nullable NpcDefinition forId(int id) {
		return id > definitions.length || id < 0 ? null : definitions[id];
	}

	private String name, examine;

	private int combat, size, id;

	@Getter
	private boolean attackable, aggressive, retreats, poisonous, multi = false, boss, pet;

	private int respawn, maxHit, attackSpeed, attackAnim, defenceAnim, deathAnim
			, attackBonus, defenceMelee, defenceRange, defenceMage, slayerLevel;

	private long hitpoints;

	public static @NotNull NpcDefinition createDummy() {
		NpcDefinition definition = new NpcDefinition();
		definition.setCombat(1);
		definition.setName("");
		definition.setAttackable(false);
		definition.setSize(1);
		return definition;
	}

	public void setMulti(Boolean multi) {
		this.multi = multi;
	}

}
