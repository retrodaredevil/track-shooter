package me.retrodaredevil.game.trackshooter.util;

public interface VelocitySetter {

	/**
	 * @param desiredVelocity Desired velocity in degrees/second
	 * @param accelerationMultiplier The higher this value, the faster it will go to get to desiredVelcity
	 * @param maxVelocity Caps final velocity at this amount. If maxVelocity > abs(desiredVelocity) then this has no effect.
	 */
	void setDesiredVelocity(float desiredVelocity, float accelerationMultiplier, float maxVelocity);

	/**
	 * @param velocity Desired velocity to set immediately
	 * @param resetOtherFields true to reset other possible present fields such as maxVelocity,
	 *                         desiredVelocity, velocity, accelerationMultiplier or other fields that could
	 *                         conflict with this.
	 */
	void setVelocity(float velocity, boolean resetOtherFields);

	/**
	 * NOTE: This implementation should always call {@link #setVelocity(float, boolean)} with resetOtherFields = true
	 * @param velocity Desired rotational velocity to set immediately
	 */
	void setVelocity(float velocity);

}
