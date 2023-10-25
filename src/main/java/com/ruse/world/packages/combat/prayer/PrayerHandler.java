package com.ruse.world.packages.combat.prayer;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Prayerbook;
import com.ruse.model.Skill;
import com.ruse.util.NameUtils;
import com.ruse.world.content.BonusManager;
import com.ruse.world.content.Sounds;
import com.ruse.world.content.Sounds.Sound;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.skills.S_Skills;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * All of the prayers that can be activated and deactivated. This currently only
 * has support for prayers present in the <b>317 protocol</b>.
 * 
 * @author Gabriel
 */
public class PrayerHandler {

    /**
	 * Represents a prayer's configurations, such as their level requirement,
	 * buttonId, configId and drain rate.
	 * 
	 * @author relex lawl
	 */
	private enum PrayerData {

		WARLOCK(75, 10, -23033, 690),
		KNIGHT(80, 11, -23031, 691),
		MARKSMAN(85, 12, -23029, 692),
		PROSPEROUS(90, 13, -23027, 693),
		SOVEREIGNTY(94, 14, -23025, 694, 9),
		TRINITY(99, 16, -23023, 695);

		@Contract(pure = true)
		PrayerData(int requirement, double drainRate, int buttonId, int configId, int @NotNull ... hint) {
			this.requirement = requirement;
			this.drainRate = drainRate;
			this.buttonId = buttonId;
			this.configId = configId;
			if (hint.length > 0)
				this.hint = hint[0];
		}

		/**
		 * The prayer's level requirement for player to be able to activate it.
		 */
		private final int requirement, buttonId, configId;

		/**
		 * The prayer's drain rate as which it will drain the associated player's prayer
		 * points.
		 */
		private final double drainRate;

		/**
		 * The prayer's head icon hint index.
		 */
		private int hint = -1 ;

		/**
		 * The prayer's formatted name.
		 */
		private String name;

		/**
		 * Gets the prayer's formatted name.
		 * 
		 * @return The prayer's name
		 */
		private String getPrayerName() {
			if (name == null)
				return NameUtils.capitalizeWords(toString().toLowerCase().replaceAll("_", " "));
			return name;
		}

		/**
		 * Contains the PrayerData with their corresponding prayerId.
		 */
		private static final HashMap<Integer, PrayerData> prayerData = new HashMap<>();

		/**
		 * Contains the PrayerData with their corresponding buttonId.
		 */
		private static final HashMap<Integer, PrayerData> actionButton = new HashMap<>();

		/**
		 * Populates the prayerId and buttonId maps.
		 */
		static {
			for (PrayerData pd : PrayerData.values()) {
				prayerData.put(pd.ordinal(), pd);
				actionButton.put(pd.buttonId, pd);
			}
		}
	}

	/**
	 * Gets the protecting prayer based on the argued combat type.
	 * 
	 * @param type the combat type.
	 * @return the protecting prayer.
	 */
	public static int getProtectingPrayer(CombatType type) {
		return switch (type) {
			case MELEE -> PROTECT_FROM_MELEE;
			case MAGIC, DRAGON_FIRE -> PROTECT_FROM_MAGIC;
			case RANGED -> PROTECT_FROM_MISSILES;
			default -> throw new IllegalArgumentException("Invalid combat type: " + type);
		};
	}

	public static boolean isActivated(Player player, int prayer) {
		//BonusManager.sendCurseBonuses(player);
		return player.getPrayerActive().length > prayer && player.getPrayerActive()[prayer];
	}

	/**
	 * Activates a prayer with specified <code>buttonId</code>.
	 * 
	 * @param player   The player clicking on prayer button.
	 * @param buttonId The button the player is clicking.
	 */
	public static void togglePrayerWithActionButton(Player player, final int buttonId) {
		for (PrayerData pd : PrayerData.values()) {
			if (buttonId == pd.buttonId) {
				if (!player.getPrayerActive()[pd.ordinal()])
					activatePrayer(player, pd.ordinal());
				else
					deactivatePrayer(player, pd.ordinal());
			}
		}
	}

	/**
	 * Activates said prayer with specified <code>prayerId</code> and de-activates
	 * all non-stackable prayers.
	 * 
	 * @param player   The player activating prayer.
	 * @param prayerId The id of the prayer being turned on, also known as the
	 *                 ordinal in the respective enum.
	 */
	public static void activatePrayer(Player player, final int prayerId) {
		if (player.getPrayerbook() == Prayerbook.CURSES)
			return;
		if (player.getPrayerActive()[prayerId])
			return;
		PrayerData pd = PrayerData.prayerData.get(prayerId);
		if (player.getNewSkills().getCurrentLevel(S_Skills.PRAYER) <= 0) {
			player.getPacketSender().sendConfig(pd.configId, 0);
			player.getPacketSender().sendMessage("You do not have enough Prayer points. You can recharge your points at an altar.");
			return;
		}
		if (!player.isDrainingPrayer()) {
			startDrain(player);
		}
		if (player.getPrayerbook() == Prayerbook.CURSES && !player.isDrainingPrayer()) {
			startDrain(player);
		}
		if (player.getNewSkills().getMaxLevel(S_Skills.PRAYER) < (pd.requirement * 10)) {
			player.getPacketSender().sendConfig(pd.configId, 0);
			player.getPacketSender().sendMessage(
					"You need a Prayer level of at least " + pd.requirement + " to use " + pd.getPrayerName() + ".");
			return;
		}

//
		switch (prayerId) {
			case FURY_SWIPE -> resetPrayers(player, DISABLED_WITH_FURY_SWIPE, prayerId);
			case HUNTERS_EYE -> resetPrayers(player, DISABLED_WITH_HUNTERS_EYE, prayerId);
			case DESTRUCTION -> resetPrayers(player, DISABLED_WITH_DESTRUCTION, prayerId);
			case FORTITUDE -> resetPrayers(player, DISABLED_WITH_FORTITUDE, prayerId);
			case THICK_SKIN, ROCK_SKIN, STEEL_SKIN -> resetPrayers(player, DEFENCE_PRAYERS, prayerId);
			case BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH -> {
				resetPrayers(player, STRENGTH_PRAYERS, prayerId);
				resetPrayers(player, RANGED_PRAYERS, prayerId);
				resetPrayers(player, MAGIC_PRAYERS, prayerId);
			}
			case CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES -> {
				resetPrayers(player, ATTACK_PRAYERS, prayerId);
				resetPrayers(player, RANGED_PRAYERS, prayerId);
				resetPrayers(player, MAGIC_PRAYERS, prayerId);
			}
			case SHARP_EYE, HAWK_EYE, EAGLE_EYE, MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT -> {
				resetPrayers(player, STRENGTH_PRAYERS, prayerId);
				resetPrayers(player, ATTACK_PRAYERS, prayerId);
				resetPrayers(player, RANGED_PRAYERS, prayerId);
				resetPrayers(player, MAGIC_PRAYERS, prayerId);
			}
			case CHIVALRY, PIETY, RIGOUR, AUGURY -> {
				resetPrayers(player, DEFENCE_PRAYERS, prayerId);
				resetPrayers(player, STRENGTH_PRAYERS, prayerId);
				resetPrayers(player, ATTACK_PRAYERS, prayerId);
				resetPrayers(player, RANGED_PRAYERS, prayerId);
				resetPrayers(player, MAGIC_PRAYERS, prayerId);
			}
			case PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE ->
					resetPrayers(player, OVERHEAD_PRAYERS, prayerId);
			case RETRIBUTION, REDEMPTION, SMITE -> resetPrayers(player, OVERHEAD_PRAYERS, prayerId);
		}

		player.setPrayerActive(prayerId, true);
		player.getPacketSender().sendConfig(pd.configId, 1);
		if (hasNoPrayerOn(player, prayerId) && !player.isDrainingPrayer())
			startDrain(player);
		BonusManager.sendCurseBonuses(player);
		if (pd.hint != -1) {
			int hintId = getHeadHint(player);
			player.getAppearance().setHeadHint(hintId);
		}
		Sounds.sendSound(player, Sound.ACTIVATE_PRAYER_OR_CURSE);
	}

	/**
	 * Deactivates said prayer with specified <code>prayerId</code>.
	 * 
	 * @param player   The player deactivating prayer.
	 * @param prayerId The id of the prayer being deactivated.
	 */
	public static void deactivatePrayer(Player player, int prayerId) {
		if (!player.getPrayerActive()[prayerId])
			return;
		PrayerData pd = PrayerData.prayerData.get(prayerId);
		player.getPrayerActive()[prayerId] = false;
		player.getPacketSender().sendConfig(pd.configId, 0);
		BonusManager.sendCurseBonuses(player);
		if (pd.hint != -1) {
			int hintId = getHeadHint(player);
			player.getAppearance().setHeadHint(hintId);
		}
		Sounds.sendSound(player, Sound.DEACTIVATE_PRAYER_OR_CURSE);
	}

	/**
	 * Deactivates every prayer in the player's prayer book.
	 * 
	 * @param player The player to deactivate prayers for.
	 */
	public static void deactivatePrayers(Player player) {
		for (int i = 0; i < player.getPrayerActive().length; i++) {
			if (player.getPrayerActive()[i]) {
				deactivatePrayer(player, i);
			}
		}
	}

	public static void deactivateAll(Player player) {
		for (int i = 0; i < player.getPrayerActive().length; i++) {
			PrayerData pd = PrayerData.prayerData.get(i);
			if (pd == null)
				continue;
			player.getPrayerActive()[i] = false;
			player.getPacketSender().sendConfig(pd.configId, 0);
			if (pd.hint != -1) {
				int hintId = getHeadHint(player);
				player.getAppearance().setHeadHint(hintId);
				BonusManager.sendCurseBonuses(player);
			}
		}
	}

	/**
	 * Gets the player's current head hint if they activate or deactivate a head
	 * prayer.
	 * 
	 * @param player The player to fetch head hint index for.
	 * @return The player's current head hint index.
	 */
	private static int getHeadHint(Player player) {
		boolean[] prayers = player.getPrayerActive();
		if (prayers[PROTECT_FROM_MELEE])
			return 0;
		if (prayers[PROTECT_FROM_MISSILES])
			return 1;
		if (prayers[PROTECT_FROM_MAGIC])
			return 2;
		if (prayers[RETRIBUTION])
			return 3;
		if (prayers[SMITE])
			return 4;
		if (prayers[REDEMPTION])
			return 5;
		if (prayers[SOUL_LEECH])
			return 18;
		return -1;
	}

	/**
	 * Initializes the player's prayer drain once a first prayer has been selected.
	 * 
	 * @param player The player to start prayer drain for.
	 */
	public static void startDrain(final Player player) {
		if (getDrain(player) <= 0 && !player.isDrainingPrayer())
			return;
		player.setDrainingPrayer(true);
		TaskManager.submit(new Task(1, player, true) {
			@Override
			public void execute() {
				if (player.getNewSkills().getCurrentLevel(S_Skills.PRAYER) <= 0) {
					for (int i = 0; i < player.getPrayerActive().length; i++) {
						if (player.getPrayerActive()[i])
							deactivatePrayer(player, i);
					}
					Sounds.sendSound(player, Sound.RUN_OUT_OF_PRAYER_POINTS);
					player.getPacketSender().sendMessage("You have run out of Prayer points!");
					this.stop();
					return;
				}
				double drainAmount = getDrain(player);
				boolean drain = player.getVariables().getBooleanValue("monic-prayer");

				if(!drain){
					int total = (int) (player.getNewSkills().getCurrentLevel(S_Skills.PRAYER) - drainAmount);
					player.getNewSkills().setCurrentLevel(S_Skills.PRAYER, total, true);
				}

			}

			@Override
			public void stop() {
				setEventRunning(false);
				player.setDrainingPrayer(false);
			}
		});
	}

	/**
	 * Gets the amount of prayer to drain for <code>player</code>.
	 * 
	 * @param player The player to get drain amount for.
	 * @return The amount of prayer that will be drained from the player.
	 */
	private static double getDrain(Player player) {
		double toRemove = 0.0;

		for (int i = 0; i < player.getPrayerActive().length; i++) {

			if (player.getPrayerActive()[i]) {
				PrayerData prayerData = PrayerData.prayerData.get(i);
				toRemove += prayerData.drainRate / 10;
			}
		}

		if(player.getVip().getRank() == 10){
			toRemove *= 0.0;
		} else if(player.getVip().getRank() >= 8){
			toRemove *= 0.25;
		} else if(player.getVip().getRank() >= 6){
			toRemove *= 0.45;
		} else if(player.getVip().getRank() >= 4){
			toRemove *= 0.60;
		} else if(player.getVip().getRank() >= 2){
			toRemove *= 0.85;
		}

		if (toRemove > 0) {
			toRemove /= (1 + (0.05 * player.getBonusManager().getOtherBonus()[2]));
		}
		return toRemove;
	}

	/**
	 * Checks if a player has no prayer on.
	 * 
	 * @param player      The player to check prayer status for.
	 * @param exceptionId The prayer id currently being turned on/activated.
	 * @return if <code>true</code>, it means player has no prayer on besides
	 *         <code>exceptionId</code>.
	 */
	private static boolean hasNoPrayerOn(Player player, int exceptionId) {
		int prayersOn = 0;
		for (int i = 0; i < player.getPrayerActive().length; i++) {
			if (player.getPrayerActive()[i] && i != exceptionId)
				prayersOn++;
		}
		return prayersOn == 0;
	}

	/**
	 * Resets <code> prayers </code> with an exception for <code> prayerID </code>
	 * 
	 * @param prayers  The array of prayers to reset
	 * @param prayerID The prayer ID to not turn off (exception)
	 */
	public static void resetPrayers(Player player, int[] prayers, int prayerID) {
		for (int i = 0; i < prayers.length; i++) {
			if (prayers[i] != prayerID)
				deactivatePrayer(player, prayers[i]);
		}
	}

	/**
	 * Checks if action button ID is a prayer button.
	 *
	 */
	public static final boolean isButton(final int actionButtonID) {
		return PrayerData.actionButton.containsKey(actionButtonID);
	}

	public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1, CLARITY_OF_THOUGHT = 2, SHARP_EYE = 3,
			MYSTIC_WILL = 4, ROCK_SKIN = 5, SUPERHUMAN_STRENGTH = 6, IMPROVED_REFLEXES = 7, RAPID_RESTORE = 8,
			RAPID_HEAL = 9, PROTECT_ITEM = 10, HAWK_EYE = 11, MYSTIC_LORE = 12, STEEL_SKIN = 13, ULTIMATE_STRENGTH = 14,
			INCREDIBLE_REFLEXES = 15, PROTECT_FROM_MAGIC = 16, PROTECT_FROM_MISSILES = 17, PROTECT_FROM_MELEE = 18,
			EAGLE_EYE = 19, MYSTIC_MIGHT = 20, RETRIBUTION = 21, REDEMPTION = 22, SMITE = 23, CHIVALRY = 24, PIETY = 25,
			RIGOUR = 26, AUGURY = 27, DESTRUCTION = 28, HUNTERS_EYE = 29, FORTITUDE = 30, GNOMES_GREED = 31,
			SOUL_LEECH = 32, FURY_SWIPE = 33;

	private static final int[] DISABLED_WITH_FURY_SWIPE = {HUNTERS_EYE, DESTRUCTION, FORTITUDE};
	private static final int[] DISABLED_WITH_HUNTERS_EYE = {FURY_SWIPE, DESTRUCTION, FORTITUDE};
	private static final int[] DISABLED_WITH_DESTRUCTION = {HUNTERS_EYE, FURY_SWIPE, FORTITUDE};
	private static final int[] DISABLED_WITH_FORTITUDE = {HUNTERS_EYE, FURY_SWIPE, DESTRUCTION};


	/**
	 * Contains every prayer that counts as a defense prayer.
	 */
	private static final int[] DEFENCE_PRAYERS = { THICK_SKIN, ROCK_SKIN, STEEL_SKIN, CHIVALRY, PIETY, RIGOUR, AUGURY };

	/**
	 * Contains every prayer that counts as a strength prayer.
	 */
	private static final int[] STRENGTH_PRAYERS = { BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CHIVALRY,
			PIETY };

	/**
	 * Contains every prayer that counts as an attack prayer.
	 */
	private static final int[] ATTACK_PRAYERS = { CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY,
			PIETY };

	/**
	 * Contains every prayer that counts as a ranged prayer.
	 */
	private static final int[] RANGED_PRAYERS = { SHARP_EYE, HAWK_EYE, EAGLE_EYE, RIGOUR };

	/**
	 * Contains every prayer that counts as a magic prayer.
	 */
	private static final int[] MAGIC_PRAYERS = { MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, AUGURY };

	/**
	 * Contains every prayer that counts as an overhead prayer, excluding protect
	 * from summoning.
	 */
	public static final int[] OVERHEAD_PRAYERS = { PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE,
			RETRIBUTION, REDEMPTION, SMITE, SOUL_LEECH };

}
