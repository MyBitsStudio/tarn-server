package com.ruse.engine.task.impl;

import com.ruse.engine.task.Task;
import com.ruse.model.Locations.Location;
import com.ruse.model.Position;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.skill.impl.hunter.Hunter;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.packages.instances.InstanceManager;

public class NPCRespawnTask extends Task {

	public NPCRespawnTask(NPC npc, int respawn, Player killer) {
		super(respawn);
		this.npc = npc;
		this.killer = killer;
	}

	final Player killer;
	final NPC npc;

	@Override
	public void execute() {
		if(killer != null && killer.getInstance() != null && npc.getLocation() == killer.getLocation() && npc.getPosition().getZ() == (killer.getIndex() * 4)){
//			if(npc.getInstanceId().equals(killer.getInstance().getInstanceId()))
//				killer.getInstance().signalSpawn(npc);
//			else
//				World.getNpcs().remove(npc);
			Instance instance = InstanceManager.getManager().byId(killer.getInstance().getInstanceId());

			if(instance != null)
				instance.signalSpawn(npc);
			else
				World.getNpcs().remove(npc);

			super.stop();
			return;
		}

		NPC npc_ = new NPC(npc.getId(), npc.getDefaultPosition());

		npc_.getMovementCoordinator().setCoordinator(npc.getMovementCoordinator().getCoordinator());

		World.register(npc_);
		stop();
	}

}
