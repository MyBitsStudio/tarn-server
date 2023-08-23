package com.ruse.world.entity.impl.player;

import com.ruse.security.save.impl.player.PlayerSecureSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.util.Misc;
import com.ruse.world.content.dailyTask.DailyTaskData;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;

public class PlayerSaving {

	private static final DailyTaskData[] daily = DailyTaskData.values();

	public static void save(Player player) {
		new PlayerSecureSave(player).create().save(SecurityUtils.PLAYER_FILE+player.getUsername()+".json");
	}
}
