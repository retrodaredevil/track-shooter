package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.World;

public class SpinMoveComponent extends TimedMoveComponent {


	private Entity entity;
	private float spinPerSecond;

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
	public float getSpinPerSecond(){
		return spinPerSecond;
	}

	@Override
	public void update(float delta, World world) {
		super.update(delta, world);
		entity.setRotation(entity.getRotation() + delta * spinPerSecond);
	}
}
