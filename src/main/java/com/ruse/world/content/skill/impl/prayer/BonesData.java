package com.ruse.world.content.skill.impl.prayer;

public enum BonesData {
	LAVA(2023, 95),STARTER(2025, 25),MYSTIC(4010, 60),
	CALLOUS(4011, 145), NIGHT(4012, 195), DANGER(4013, 245),
	DONATOR(4014, 495);

	BonesData(int boneId, int buryXP) {
		this.boneId = boneId;
		this.buryXP = buryXP;
	}

	private int boneId;
	private int buryXP;

	public int getBoneID() {
		return this.boneId;
	}

	public int getBuryingXP() {
		return this.buryXP;
	}

	public static BonesData forId(int bone) {
		for (BonesData prayerData : BonesData.values()) {
			if (prayerData.getBoneID() == bone) {
				return prayerData;
			}
		}
		return null;
	}

}
