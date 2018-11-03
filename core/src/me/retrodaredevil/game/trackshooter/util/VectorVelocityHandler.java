package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class VectorVelocityHandler implements VectorVelocitySetter {

	private final float velocityMagnitudeSetGoToDeadband2;

	private final Vector2 velocity = new Vector2();
	private final Vector2 desiredVelocity = new Vector2();
	private float accelerationMultiplier = 1;
	/** The maximum velocity magnitude, squared */
	private float maxVelocity2 = 0;

	public VectorVelocityHandler(float velocityMagnitudeSetGoToDeadband){
		this.velocityMagnitudeSetGoToDeadband2 = velocityMagnitudeSetGoToDeadband * velocityMagnitudeSetGoToDeadband;
	}

	@Override
	public void setVelocity(float x, float y, boolean resetOtherFields) {
		velocity.set(x, y);
		if(resetOtherFields){
			maxVelocity2 = velocity.len2();
		}
	}
	@Override
	public void setDesiredVelocity(float x, float y, float magnitudeAccelerationMultiplier, float maximumVelocityMagnitude) {
		this.desiredVelocity.set(x, y);
		this.accelerationMultiplier = magnitudeAccelerationMultiplier;
		this.maxVelocity2 = maximumVelocityMagnitude * maximumVelocityMagnitude;
	}
	public void update(float delta){
		Vector2 change = desiredVelocity.cpy().sub(velocity);

		if(change.len2() <= velocityMagnitudeSetGoToDeadband2){
			velocity.set(desiredVelocity);
		} else {
			change.scl(accelerationMultiplier * delta); // delta and acceleration is now applied to change
			velocity.add(change);
			if(desiredVelocity.cpy().sub(velocity).hasOppositeDirection(change)){ // if we added past the desired velocity, set velocity to desired
				velocity.set(desiredVelocity);
			}
		}

		if(velocity.len2() > maxVelocity2){
			velocity.setLength2(maxVelocity2);
		}
	}
	public Vector2 getVelocity(){
		return velocity.cpy();
	}

	// region Method Overloads
	@Override
	public void setDesiredVelocity(Vector2 desiredVelocity, float magnitudeAccelerationMultiplier, float maximumVelocityMagnitude) {
		setDesiredVelocity(desiredVelocity.x, desiredVelocity.y, magnitudeAccelerationMultiplier, maximumVelocityMagnitude);
	}
	@Override
	public void setDesiredVelocityAngleMagnitude(float angleDegrees, float magnitude, float magnitudeAccelerationMultiplier, float maximumVelocityMagnitude) {
		setDesiredVelocity(MathUtils.cosDeg(angleDegrees) * magnitude, MathUtils.sinDeg(angleDegrees) * magnitude,
				magnitudeAccelerationMultiplier, maximumVelocityMagnitude);
	}

	@Override
	public void setVelocity(Vector2 velocity, boolean resetOtherFields) {
		setVelocity(velocity.x, velocity.y, resetOtherFields);
	}
	@Override
	public void setVelocityAngleMagnitude(float angleDegrees, float magnitude, boolean resetOtherFields) {
		setVelocity(MathUtils.cosDeg(angleDegrees) * magnitude, MathUtils.sinDeg(angleDegrees) * magnitude, resetOtherFields);
	}
	@Override
	public void setVelocity(Vector2 velocity) { setVelocity(velocity, true); }
	@Override
	public void setVelocity(float x, float y) { setVelocity(x, y, true); }

	@Override
	public void setVelocityAngleMagnitude(float angleDegrees, float magnitude) { setVelocityAngleMagnitude(angleDegrees, magnitude, true); }
	// endregion


}
