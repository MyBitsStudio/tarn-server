package com.ruse.world.content.instanceMananger;

import com.ruse.GameSettings;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.model.RegionInstance;
import com.ruse.model.RegionInstance.RegionInstanceType;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

public class InstanceManager {

	private final Player player;

	boolean InstanceEnded;

	public InstanceManager(Player player) {
		this.player = player;
	}

	public int pos = 4;

	private static final InstanceData[] values = InstanceData.values();

	public void createInstance(int npcId, RegionInstanceType type) {
		if(player.getPosition().getRegionId() == 11082){
			player.sendMessage("<shad=1>@red@You can't start a new instance until this one ends");
			return;
		}
		if (player.getRegionInstance() != null) {
			player.sendMessage("<shad=1>@red@You can't start a new instance here. You must be at home.");
			return;
		}
		if (player.getInventory().contains(ItemDefinition.TOKEN_ID, 10000)) {
			player.getInventory().delete(ItemDefinition.TOKEN_ID, 10000);
		} else {
			player.getPA().sendMessage("You need at least 10,000 Tokens to start an instance.");
			return;
		}
		//if (player.getLocation() != Locations.Location.HOME_BANK) {
			//player.sendMessage("@red@Please teleport to the home area before starting a new instance.");
			//return;
		//}
		if (player.getRegionInstance() == null) {
			for (NPC n : World.getNpcs()) {
				if (n != null) {
					if (n.getPosition().getRegionId() == 11082 && n.getPosition().getZ() == (player.getIndex() * pos)) {
						World.deregister(n);
					}
				}
			}
		} else {
			for (NPC n : player.getRegionInstance().getNpcsList()) {
				if (n != null) {
					World.deregister(n);
				}
			}
			player.getRegionInstance().getNpcsList().clear();
		}
		player.setRegionInstance(new RegionInstance(player, type));
		player.lastInstanceNpc = npcId;
		player.moveTo(new Position(2786, 4775 ,
				player.getIndex() * 4));
		for (int i = 0; i < 4; i++) {
			NPC npc_ = new NPC(npcId, new Position(player.getPosition().getX() - 4 + (i * 2),
					player.getPosition().getY() + 8 , player.getIndex() * pos));
			Player mini = player.getMinimeSystem().getMiniMe();
			if(mini != null) {
				npc_.setSpawnedFor(player, mini);
			} else {
				npc_.setSpawnedFor(player);
			}
			player.getRegionInstance().getNpcsList().add(npc_);
			World.register(npc_);
		}
		for (int i = 0; i < 4; i++) {
			NPC npc_ = new NPC(npcId, new Position(player.getPosition().getX() - 4 + (i * 2),
					player.getPosition().getY() + 6 , player.getIndex() * pos));
			Player mini = player.getMinimeSystem().getMiniMe();
			if(mini != null) {
				npc_.setSpawnedFor(player, mini);
			} else {
				npc_.setSpawnedFor(player);
			}
			player.getRegionInstance().getNpcsList().add(npc_);
			World.register(npc_);
		}
		for (int i = 0; i < 4; i++) {
			NPC npc_ = new NPC(npcId, new Position(player.getPosition().getX() - 4 + (i * 2),
					player.getPosition().getY() + 4 , player.getIndex() * pos));
			Player mini = player.getMinimeSystem().getMiniMe();
			if(mini != null) {
				npc_.setSpawnedFor(player, mini);
			} else {
				npc_.setSpawnedFor(player);
			}
			player.getRegionInstance().getNpcsList().add(npc_);
			World.register(npc_);
		}
		for (int i = 0; i < 4; i++) {
			NPC npc_ = new NPC(npcId, new Position(player.getPosition().getX() - 4 + (i * 2),
					player.getPosition().getY() + 2 , player.getIndex() * pos));
			Player mini = player.getMinimeSystem().getMiniMe();
			if(mini != null) {
				npc_.setSpawnedFor(player, mini);
			} else {
				npc_.setSpawnedFor(player);
			}
			player.getRegionInstance().getNpcsList().add(npc_);
			World.register(npc_);
		}
		for (InstanceData data : values) {
//			if (npcId == data.getNpcid() && player.getRights().equals(PlayerRights.FORSAKEN_DONATOR)
//					|| NpcDefinition.forId(npcId).getName() == data.getName() && player.getRights().equals(PlayerRights.FORSAKEN_DONATOR)) {
//				player.setCurrentInstanceAmount(data.getEndamount() + Misc.getRandom(464, 465));//500
//				player.setCurrentInstanceNpcId(data.getNpcid());
//				player.setCurrentInstanceNpcName(data.getName());
//			}
//			else if (npcId == data.getNpcid() && player.getRights().equals(PlayerRights.OBSIDIAN_DONATOR)
//					|| NpcDefinition.forId(npcId).getName() == data.getName() && player.getRights().equals(PlayerRights.OBSIDIAN_DONATOR)) {
//				player.setCurrentInstanceAmount(data.getEndamount() + Misc.getRandom(314, 315));//350
//				player.setCurrentInstanceNpcId(data.getNpcid());
//				player.setCurrentInstanceNpcName(data.getName());
//			}
//			else if (npcId == data.getNpcid() && player.getRights().equals(PlayerRights.MYSTICAL_DONATOR)
//					|| NpcDefinition.forId(npcId).getName() == data.getName() && player.getRights().equals(PlayerRights.MYSTICAL_DONATOR)) {
//				player.setCurrentInstanceAmount(data.getEndamount() + Misc.getRandom(164, 165));//200
//				player.setCurrentInstanceNpcId(data.getNpcid());
//				player.setCurrentInstanceNpcName(data.getName());
//			}
//			else if (npcId == data.getNpcid() && player.getRights().equals(PlayerRights.TORMENTED_DONATOR)
//					|| NpcDefinition.forId(npcId).getName() == data.getName() && player.getRights().equals(PlayerRights.TORMENTED_DONATOR)) {
//				player.setCurrentInstanceAmount(data.getEndamount() + Misc.getRandom(89, 90));//125
//				player.setCurrentInstanceNpcId(data.getNpcid());
//				player.setCurrentInstanceNpcName(data.getName());
//			}
//			else if (npcId == data.getNpcid() && player.getRights().equals(PlayerRights.CLERIC_DONATOR)
//					|| NpcDefinition.forId(npcId).getName() == data.getName() && player.getRights().equals(PlayerRights.CLERIC_DONATOR)) {
//				player.setCurrentInstanceAmount(data.getEndamount() + Misc.getRandom(39, 40));//75
//				player.setCurrentInstanceNpcId(data.getNpcid());
//				player.setCurrentInstanceNpcName(data.getName());
//			}
//			else if (npcId == data.getNpcid() && player.getRights().equals(PlayerRights.GRACEFUL_DONATOR)
//					|| NpcDefinition.forId(npcId).getName() == data.getName() && player.getRights().equals(PlayerRights.GRACEFUL_DONATOR)) {
//				player.setCurrentInstanceAmount(data.getEndamount() + Misc.getRandom(14, 15));//50
//				player.setCurrentInstanceNpcId(data.getNpcid());
//				player.setCurrentInstanceNpcName(data.getName());
//			}
//			else if (npcId == data.getNpcid() && player.getRights().equals(PlayerRights.DEVELOPER)
//					|| NpcDefinition.forId(npcId).getName() == data.getName() && player.getRights().equals(PlayerRights.DEVELOPER)) {
//				player.setCurrentInstanceAmount(data.getEndamount() + Misc.getRandom(1555, 1556));//50
//				player.setCurrentInstanceNpcId(data.getNpcid());
//				player.setCurrentInstanceNpcName(data.getName());
//			}
//			else if (npcId == data.getNpcid() && player.getRights().equals(PlayerRights.HELPER)
//					|| NpcDefinition.forId(npcId).getName() == data.getName() && player.getRights().equals(PlayerRights.HELPER)) {
//				player.setCurrentInstanceAmount(data.getEndamount() + Misc.getRandom(464, 465));//50
//				player.setCurrentInstanceNpcId(data.getNpcid());
//				player.setCurrentInstanceNpcName(data.getName());
//			}
//			else if (npcId == data.getNpcid() && player.getRights().equals(PlayerRights.MODERATOR)
//					|| NpcDefinition.forId(npcId).getName() == data.getName() && player.getRights().equals(PlayerRights.MODERATOR)) {
//				player.setCurrentInstanceAmount(data.getEndamount() + Misc.getRandom(464, 465));//50
//				player.setCurrentInstanceNpcId(data.getNpcid());
//				player.setCurrentInstanceNpcName(data.getName());
//			}
//			else if (npcId == data.getNpcid() && player.getRights().equals(PlayerRights.ADMINISTRATOR)
//					|| NpcDefinition.forId(npcId).getName() == data.getName() && player.getRights().equals(PlayerRights.ADMINISTRATOR)) {
//				player.setCurrentInstanceAmount(data.getEndamount() + Misc.getRandom(1555, 1556));//50
//				player.setCurrentInstanceNpcId(data.getNpcid());
//				player.setCurrentInstanceNpcName(data.getName());
//			}
			if (npcId == data.getNpcid() || NpcDefinition.forId(npcId).getName() == data.getName()) {
				player.setCurrentInstanceAmount(data.getEndamount());
				player.setCurrentInstanceNpcId(data.getNpcid());
				player.setCurrentInstanceNpcName(data.getName());
			}
		}
		player.getPA().sendMessage("You have instanced yourself " + player.getCurrentInstanceAmount() + " "
				+ player.getCurrentInstanceNpcName());
		player.getPA().sendInterfaceRemoval();
	}

	public void death(Player player, NPC npc, String NpcName) {
		if (npc.getId() != player.getCurrentInstanceNpcId()) {
			return;
		}
		if (player.currentInstanceNpcId == -1 || player.currentInstanceNpcName == "") {
			return;
		}
		player.setCurrentInstanceAmount(player.getCurrentInstanceAmount() - 1);
		if (player.getPSettings().getBooleanValue("drop-message-personal")) {
			player.getPA().sendMessage("You currently need to kill " + player.getCurrentInstanceAmount() + " " + NpcName);
		}
		if (player.getCurrentInstanceAmount() <= 0) {
			player.getPA().sendMessage("You have used up the total instance count!");
			finish();
			return;
		}
	}

	public void finish() {
		player.getPA().sendMessage("You have used up all your kills inside the instance.");
		player.moveTo(GameSettings.HOME_CORDS);
		new InstanceManager(player).onLogout();
		if (player != null) {
			onLogout();
		}
	}

	public void onLogout() {
		if (player.getRegionInstance() != null)
			player.getRegionInstance().destruct();
		player.setData(null);
		player.setCurrentInstanceAmount(-1);
		player.setCurrentInstanceNpcId(-1);
		player.setCurrentInstanceNpcName("");
		InstanceEnded = true;
	}
}
