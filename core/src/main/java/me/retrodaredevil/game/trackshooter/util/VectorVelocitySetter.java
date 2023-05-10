package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.math.Vector2;

public interface VectorVelocitySetter {
	void setDesiredVelocity(Vector2 desiredVelocity, float magnitudeAccelerationMultiplier, float maximumVelocityMagnitude);
	void setDesiredVelocity(float x, float y, float magnitudeAccelerationMultiplier, float maximumVelocityMagnitude);
	void setDesiredVelocityAngleMagnitude(float angleDegrees, float magnitude, float magnitudeAccelerationMultiplier, float maximumVelocityMagnitude);

	void setVelocity(Vector2 velocity, boolean resetOtherFields);
	void setVelocity(float x, float y, boolean resetOtherFields);
	void setVelocityAngleMagnitude(float angleDegrees, float magnitude, boolean resetOtherFields);

	void setVelocity(Vector2 velocity);
	void setVelocity(float x, float y);
	void setVelocityAngleMagnitude(float angleDegrees, float magnitude);
}
