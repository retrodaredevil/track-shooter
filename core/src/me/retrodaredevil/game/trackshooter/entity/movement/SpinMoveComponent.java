package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.World;

public class SpinMoveComponent extends TimedMoveComponent implements RotationalVelocityMoveComponent{


	private final Entity entity;
	private final float spinPerSecond;

	/**
	 *
	 * @param time The amount of time to spin before returning the next component
	 * @param spinPerSecond The amount to rotate in one second in degrees/second
	 */
	public SpinMoveComponent(Entity entity, long time, float spinPerSecond) {
		super(time);
		this.entity = entity;
		this.spinPerSecond = spinPerSecond;
	}

	@Override
	public float getRotationalVelocity() {
		return spinPerSecond;
	}


	@Override
	protected void onUpdate(float delta, World world) {
		super.onUpdate(delta, world);
		entity.setRotation(entity.getRotation() + delta * spinPerSecond);
	}
}
