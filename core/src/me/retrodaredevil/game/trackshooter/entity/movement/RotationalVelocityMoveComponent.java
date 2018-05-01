package me.retrodaredevil.game.trackshooter.entity.movement;

public interface RotationalVelocityMoveComponent extends MoveComponent {
	/**
	 * @param desiredRotationalVelocity The desired rotational velocity in degrees per second
	 * @param rotationalAccelerationMultiplier The acceleration multiplier
	 * @param maxRotationalVelocity The maximum allows rotational velocity in degrees per second. If larger than desiredRotationalVelocity, has no effect
	 */
	void setDesiredRotationalVelocity(float desiredRotationalVelocity, float rotationalAccelerationMultiplier, float maxRotationalVelocity);
}
