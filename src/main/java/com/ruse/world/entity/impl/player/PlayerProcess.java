package com.ruse.world.entity.impl.player;

import com.ruse.GameSettings;
import com.ruse.model.Locations;
import com.ruse.model.Locations.Location;
import com.ruse.model.Position;
import com.ruse.model.Prayerbook;
import com.ruse.model.RegionInstance.RegionInstanceType;
import com.ruse.model.Skill;
import com.ruse.world.content.PlayerPanel;
import com.ruse.world.content.PlayerPunishment;
import com.ruse.world.content.combat.pvp.BountyHunter;
import com.ruse.world.content.skill.impl.construction.House;
import com.ruse.world.entity.impl.GroundItemManager;

public class PlayerProcess {

	/*
	 * The player (owner) of this instance
	 */
	private Player player;

	/*
	 * The loyalty tick, once this reaches 6, the player will be given loyalty
	 * points. 6 equals 3.6 seconds.
	 */
	private int loyaltyTick;

	/*
	 * The timer tick, once this reaches 2, the player's total play time will be
	 * updated. 2 equals 1.2 seconds.
	 */
	private int timerTick;

	/*
	 * Makes sure ground items are spawned on height change
	 */
	private int previousHeight;

	public PlayerProcess(Player player) {
		this.player = player;
		this.previousHeight = player.getPosition().getZ();
	}

	public void sequence() {


		/** COMBAT **/
		player.getCombatBuilder().process();

		player.getControllerManager().process();

		/** SKILLS **/
		if (player.shouldProcessFarming()) {
			player.getFarming().sequence();
		}

		/** MISC **/

		if (previousHeight != player.getPosition().getZ()) {
			GroundItemManager.handleRegionChange(player);
			previousHeight = player.getPosition().getZ();
		}

		player.getAfk().process();
		/*
		 * if(timerTick >= 1) { HANDLED BY PlayerPanel
		 * player.getPacketSender().sendString(39166,
		 * "@or2@Time played:  @yel@"+Misc.getTimePlayed((player.getTotalPlayTime() +
		 * player.getRecordedLogin().elapsed()))); timerTick = 0; }
		 */
		timerTick++;


		/*if (player.afkTicks >= 500 && !player.afk) {
			player.moveTo(new Position(2658, 3987, 0));
			player.afk = true;
		}*/
	}
}
