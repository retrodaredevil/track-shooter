package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.world.World;

public class TimedSpeedEffect extends TimedEffect implements SpeedEffect{
	private final float multiplier;

	public TimedSpeedEffect(World world, long last, float multiplier){
		super(world, last);
		this.multiplier = multiplier;
	}

	@Override
	protected void onStart() {
	}

	@Override
	protected void onUpdate(float delta) {
	}

	@Override
	protected void onEnd() {
	}

	@Override
	public float getSpeedMultiplier() {
		return multiplier;
	}
}
