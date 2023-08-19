package com.ruse.world.content;

import lombok.Getter;

@Getter
public enum EffectTimer {
	
	X2_DR_1HR(15355, 3600),
	X2_DDR_1HR(15356, 3600),
	X2_DMG_1HR(15357, 3600),
	X2_DR_30MIN(15358, 1800),
	X2_DMG_30MIN(15359, 1800),
	T1_INF_OVERLOAD(23124, 500),
	T2_INF_OVERLOAD(23125, 500),
	T3_INF_OVERLOAD(23126, 500),
	;
	
	EffectTimer(int clientSprite, int time) {
		this.clientSprite = clientSprite;
		this.time = time;
	}
	
	private final int clientSprite, time;
}
