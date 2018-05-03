package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.util.VelocitySetter;

public interface RotationalVelocityMoveComponent extends MoveComponent, VelocitySetter {
	/**
	 * @param desiredRotationalVelocity The desired rotational velocity in degrees per second
	 * @param rotationalAccelerationMultiplier The acceleration multiplier
	 * @param maxRotationalVelocity The maximum allows rotational velocity in degrees per second. If larger than desiredRotationalVelocity, has no effect
	 */
	@Override
	void setDesiredRotationalVelocity(float desiredRotationalVelocity, float rotationalAccelerationMultiplier, float maxRotationalVelocity);
}
