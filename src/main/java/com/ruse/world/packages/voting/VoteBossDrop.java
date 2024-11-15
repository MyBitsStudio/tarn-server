//package com.ruse.world.packages.voting;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.ruse.engine.GameEngine;
//import com.ruse.model.Position;
//import com.ruse.motivote3.doMotivote;
//import com.ruse.world.World;
//import com.ruse.world.content.combat.CombatBuilder.CombatDamageCache;
//import com.ruse.world.content.combat.CombatFactory;
//import com.ruse.world.packages.discordbot.JavaCord;
//import com.ruse.world.entity.impl.npc.NPC;
//import com.ruse.world.entity.impl.player.Player;
//
//import java.io.*;
//import java.util.*;
//import java.util.Map.Entry;
//
//public class VoteBossDrop {
//
//	public static NPC currentSpawn;
//
//
//	public static void handleSpawn() {
//		if (currentSpawn != null){
//			//System.out.println("Already spawned.");
//			return;
//		}
//
//		doMotivote.setVoteCount(doMotivote.getVoteCount() - 50);
//		save();
//		currentSpawn = new NPC(8013, new Position(2980, 2778, 0));
//
//		World.register(currentSpawn);
//		World.sendMessage(
//				"<img=28><shad=f9f6f6>Vote boss has spawned at ::voteboss kill it now for amazing rewards!<shad=-1>");
//		JavaCord.sendMessage(1117224370587304057L, "**Vote boss has spawned at ::voteboss kill it now for amazing rewards!**");
//	}
//
//	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//	private static final File SAVE_FILE = new File("./data/saves/votes.json");
//
//	public static void save() {
//		GameEngine.submit(() -> {
//			JsonObject jsonObject = new JsonObject();
//			jsonObject.addProperty("amount", doMotivote.getVoteCount());
//			jsonObject.addProperty("lastSaved", System.currentTimeMillis());
//			try (FileWriter writer = new FileWriter(SAVE_FILE)) {
//				gson.toJson(jsonObject, writer);
//				writer.flush();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});
//	}
//
//	public static void load() {
//		try (Reader reader = new FileReader("./data/saves/votes.json")) {
//			JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
//			doMotivote.setVoteCount(jsonObject.get("amount").getAsInt());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void handleForcedSpawn() {
//		if (currentSpawn != null){
//		//	System.out.println("Already spawned.");
//			return;
//		}
//
//		currentSpawn = new NPC(8013, new Position(2980, 2778, 0));
//
//		World.register(currentSpawn);
//		World.sendMessage(
//				"<img=28><shad=f9f6f6>Vote boss has spawned at ::voteboss kill it now for amazing rewards!<shad=-1>");
//		JavaCord.sendMessage(1117224370587304057L, "**Vote boss has spawned at ::voteboss kill it now for amazing rewards!**");
//	}
//
//	public static void handleDrop(NPC npc) {
//		if (npc.getCombatBuilder().getDamageMap().size() == 0) {
//			return;
//		}
//		Map<Player, Long> killers = new HashMap<>();
//
//		for (Entry<Player, CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {
//			if (entry == null) {
//				continue;
//			}
//
//			long timeout = entry.getValue().getStopwatch().elapsed();
//			if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
//				continue;
//			}
//
//			Player player = entry.getKey();
//			if (player.getConstitution() <= 0 || !player.isRegistered()) {
//				continue;
//			}
//
//			killers.put(player, entry.getValue().getDamage());
//		}
//
//		npc.getCombatBuilder().getDamageMap().clear();
//
//		List<Entry<Player, Long>> result = sortEntries(killers);
//		for (Iterator<Entry<Player, Long>> iterator = result.iterator(); iterator.hasNext(); ) {
//			Entry<Player, Long> entry = iterator.next();
//			Player killer = entry.getKey();
//			//NPCDrops.handleDrops(killer, npc);
//			iterator.remove();
//		}
//		currentSpawn = null;
//	}
//
//
//	/**
//	 *
//	 * @param map
//	 * @return
//	 */
//	static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortEntries(Map<K, V> map) {
//
//		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());
//
//		Collections.sort(sortedEntries, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));
//
//		return sortedEntries;
//
//	}
//}
