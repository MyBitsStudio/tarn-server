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

	private boolean attackable, aggressive, retreats, poisonous, multi = false, boss, pet;

	private int respawn, maxHit, attackSpeed, attackAnim, defenceAnim, deathAnim
			, attackBonus, defenceMelee, defenceRange, defenceMage, slayerLevel;

	private long hitpoints;

	public static @NotNull NpcDefinition createDummy() {
		NpcDefinition definition = new NpcDefinition();
		definition.setCombat(1);
		definition.setName("");
		definition.setAttackable(true);
		definition.setSize(1);
		return definition;
	}

	public void setMulti(Boolean multi) {
		this.multi = multi;
	}

	public boolean isMulti() {
		return this.multi;
	}

	/**
	 * Prepares the dynamic json loader for loading npc definitions.
	 * 
	 * @return the dynamic json loader.
	 * @throws Exception if any errors occur while preparing for load.
	 */
	public static JsonLoader parseNpcs() {
		return new JsonLoader() {
			@Override
			public void load(JsonObject reader, Gson builder) {
				int index = reader.get("id").getAsInt();
				definitions[index] = new NpcDefinition();
				definitions[index].setId(index);
				definitions[index].setName(reader.get("name").getAsString());
				definitions[index].setExamine(reader.get("examine").getAsString());
				definitions[index].setCombat(reader.get("combat").getAsInt());
				definitions[index].setSize(reader.get("size").getAsInt());
				definitions[index].setAttackable(reader.get("attackable").getAsBoolean());
				definitions[index].setAggressive(reader.get("aggressive").getAsBoolean());
				definitions[index].setRetreats(reader.get("retreats").getAsBoolean());
				definitions[index].setPoisonous(reader.get("poisonous").getAsBoolean());
				definitions[index].setRespawn(reader.get("respawn").getAsInt());
				definitions[index].setMaxHit(reader.get("maxHit").getAsInt());
				definitions[index].setHitpoints(reader.get("hitpoints").getAsLong());
				definitions[index].setAttackSpeed(reader.get("attackSpeed").getAsInt());
				definitions[index].setAttackAnim(reader.get("attackAnim").getAsInt());
				definitions[index].setDefenceAnim(reader.get("defenceAnim").getAsInt());
				definitions[index].setDeathAnim(reader.get("deathAnim").getAsInt());
				definitions[index].setAttackBonus(reader.get("attackBonus").getAsInt());
				definitions[index].setDefenceMelee(reader.get("defenceMelee").getAsInt());
				definitions[index].setDefenceRange(reader.get("defenceRange").getAsInt());
				definitions[index].setDefenceMage(reader.get("defenceMage").getAsInt());
				if (reader.has("slayerLevel")) {
					definitions[index].setSlayerLevel(reader.get("slayerLevel").getAsInt());
				}

				if (reader.has("isBoss")) {
					definitions[index].boss = reader.get("isBoss").getAsBoolean();
				} else {
					definitions[index].boss = false;
				}

				if (reader.has("isPet")) {
					definitions[index].pet = reader.get("isPet").getAsBoolean();
				} else {
					definitions[index].pet = false;
				}
			}

			@Override
			public String filePath() {
				return "./.core/server/defs/npc/npc_data.json";
			}
		};
	}
}
