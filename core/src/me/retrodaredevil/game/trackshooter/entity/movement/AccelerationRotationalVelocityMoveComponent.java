package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.util.RotationalVelocitySetter;

public interface AccelerationRotationalVelocityMoveComponent extends RotationalVelocityMoveComponent, RotationalVelocitySetter {
	/**
	 * @param desiredRotationalVelocity The desired rotational velocity in degrees per second
	 * @param rotationalAccelerationMultiplier The acceleration multiplier. If you want it to instantly accelerate, then it should be Float.POSITIVE_INFINITY.
	 *                                         If you want it to fully accelerate in a number of seconds then is should be (1 / x seconds)
	 * @param maxRotationalVelocity The maximum allows rotational velocity in degrees per second. If larger than desiredRotationalVelocity, has no effect
	 */
	@Override
	void setDesiredRotationalVelocity(float desiredRotationalVelocity, float rotationalAccelerationMultiplier, float maxRotationalVelocity);
}
