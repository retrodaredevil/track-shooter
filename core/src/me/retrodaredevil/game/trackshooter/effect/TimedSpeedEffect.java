package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class TimedSpeedEffect extends TimedEffect implements SpeedEffect{
	private final float multiplier;

	public TimedSpeedEffect(long last, float multiplier){
		super(last);
		this.multiplier = multiplier;
	}

	@Override
	protected void onStart(World world) {
	}

	@Override
	protected void onUpdate(float delta, World world) {
	}

	@Override
	protected void onEnd(World world) {
	}

	@Override
	public float getSpeedMultiplier() {
		return multiplier;
	}
}
