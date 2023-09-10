package com.ruse.engine.task.impl;

import com.ruse.engine.task.Task;
import com.ruse.io.ThreadProgressor;
import com.ruse.model.Backup;
import com.ruse.world.World;

/**
 * @author Gabriel Hannason
 */
public class ServerTimeUpdateTask extends Task {

	public ServerTimeUpdateTask() {
		super(40);
	}

	private int tick = 0;

	@Override
	protected void execute() {
		World.updateServerTime();

		if(tick % 150 == 0){
			System.gc();
		}

		if(tick % 600 == 0){
			ThreadProgressor.submit(false, () -> {
				Backup.backup();
				return null;
			});
		}

		tick++;
	}
}