package me.retrodaredevil.game.trackshooter.util;

public class VelocityHandler implements VelocitySetter {

	private final float velocitySetZeroDeadband;

	private float velocity = 0;
	private float desiredVelocity = 0;
	private float accelerationMultiplier = 1;
	private float maxVelocity = 0;

	/**
	 *
	 * @param velocitySetZeroDeadband When |velocity - desiredVelocity| is less than this,
	 *                                it will set velocity to the desired velocity
	 */
	public VelocityHandler(float velocitySetZeroDeadband){
		this.velocitySetZeroDeadband = velocitySetZeroDeadband;
	}


	public void update(float delta){
		float change = desiredVelocity - velocity;

		if(Math.abs(change) < velocitySetZeroDeadband){
			velocity = desiredVelocity;
		} else {
			change *= accelerationMultiplier;
			velocity += change * delta;
		}

		if(velocity > maxVelocity){
			velocity = maxVelocity;
		} else if(velocity < -maxVelocity){
			velocity = -maxVelocity;
		}
	}

	/**
	 *
	 * @return The current velocity in units per second. (You likely have to multiply by delta)
	 */
	public float getVelocity(){
		return velocity;
	}

	/**
	 *
	 * @param velocity The velocity to set in units per second
	 */
	public void setVelocity(float velocity){
		this.velocity = velocity;
	}

	@Override
	public void setDesiredRotationalVelocity(float desiredVelocity, float accelerationMultiplier, float maxVelocity) {
		this.desiredVelocity = desiredVelocity;
		this.accelerationMultiplier = accelerationMultiplier;
		this.maxVelocity = maxVelocity;
	}
}
