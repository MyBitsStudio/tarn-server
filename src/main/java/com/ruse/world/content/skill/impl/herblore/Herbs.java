package com.ruse.world.content.skill.impl.herblore;

public enum Herbs {

	GUAM(199, 249, 1, 3), MARRENTILL(201, 251, 5, 4), TARROMIN(203, 253, 11, 5), HARRALANDER(205, 255, 20, 7),
	RANARR(207, 257, 25, 8), TOADFLAX(3049, 2998, 30, 9), SPIRITWEED(12174, 12172, 35, 9), IRIT(209, 259, 40, 9),
	WERGALI(14836, 14854, 30, 9), AVANTOE(211, 261, 48, 10), KWUARM(213, 263, 54, 11), SNAPDRAGON(3051, 3000, 59, 12),
	CADANTINE(215, 265, 65, 13), LANTADYME(2485, 2481, 67, 14), DWARFWEED(217, 267, 70, 15),

	LARITH(775, 777, 1, 3), TARRAGON(781, 783, 5, 8), CARDAMON(787, 789, 11, 10),
	ROSOCELY(793, 795, 20, 15), VERINISE(813, 815, 25, 18), SEPHIALLA(912, 913, 30, 22),
	FENNEL(915, 916, 35, 27), RALORAGE(918, 919, 40, 31), TURMERIC(921, 922, 46, 41),
	MARJORAM(924, 925, 52, 82), KUNNERREL(927, 928, 60, 99), CARAWAY(930, 931, 72, 140),
	USTROVE(933, 934, 86, 340), REXETH(936, 937, 97, 170), CREEPER(939, 940, 114, 500),

	TORSTOL(219, 269, 75, 16);

	private int grimyHerb, cleanHerb, levelReq, cleaningExp;

	private Herbs(int grimyHerb, int cleanHerb, int levelReq, int cleaningExp) {
		this.grimyHerb = grimyHerb;
		this.cleanHerb = cleanHerb;
		this.levelReq = levelReq;
		this.cleaningExp = cleaningExp;
	}

	public int getGrimyHerb() {
		return grimyHerb;
	}

	public int getCleanHerb() {
		return cleanHerb;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public int getExp() {
		return cleaningExp;
	}

	public static Herbs forId(int herbId) {
		for (Herbs herb : Herbs.values()) {
			if (herb.getGrimyHerb() == herbId) {
				return herb;
			}
		}
		return null;
	}

}