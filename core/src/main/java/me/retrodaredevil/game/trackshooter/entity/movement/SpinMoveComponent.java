package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.World;

public class SpinMoveComponent extends TimedMoveComponent implements RotationalVelocityMoveComponent{


	private final Entity entity;
	private final float spinPerSecond;

	/**
	 *
	 * @param world
	 * @param time The amount of time to spin before returning the next component
	 * @param spinPerSecond The amount to rotate in one second in degrees/second
	 */
	public SpinMoveComponent(World world, float time, Entity entity, float spinPerSecond) {
		super(world, time);
		this.entity = entity;
		this.spinPerSecond = spinPerSecond;
	}

	@Override
	public float getRotationalVelocity() {
		return spinPerSecond;
	}


	@Override
	protected void onUpdate(float delta) {
		super.onUpdate(delta);
		entity.setRotation(entity.getRotation() + delta * spinPerSecond);
	}
}
