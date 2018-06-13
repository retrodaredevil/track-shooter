package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.entity.Entity;

import java.util.Collection;

public final class EffectUtil {
	private EffectUtil(){}


	public static float getSpeedMultiplier(Entity entity){
		Collection<SpeedEffect> effects = entity.getEffects(SpeedEffect.class);
		float r = 1;
		if(effects == null){
			return r;
		}
		for(SpeedEffect speedEffect : effects){
			r *= speedEffect.getSpeedMultiplier();
		}
		return r;
	}
}
