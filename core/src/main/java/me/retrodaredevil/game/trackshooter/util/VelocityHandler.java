package me.retrodaredevil.game.trackshooter.util;

/**
 * A class that can be used for keeping track of a variable that accelerates. (Get a velocity and that velocity increases
 * an amount each second)
 */
public class VelocityHandler implements VelocitySetter {

	private final float velocitySetGotoDeadband;

	private float velocity = 0;
	private float desiredVelocity = 0;
	private float accelerationMultiplier = 1;
	private float maxVelocity = 0;

	/**
	 *
	 * @param velocitySetGotoDeadband When |velocity - desiredVelocity| is less than this,
	 *                                it will set velocity to the desired velocity
	 */
	public VelocityHandler(float velocitySetGotoDeadband){
		this.velocitySetGotoDeadband = velocitySetGotoDeadband;
	}


	public void update(float delta){
		float change = desiredVelocity - velocity;

		if(Math.abs(change) < velocitySetGotoDeadband){
			velocity = desiredVelocity;
		} else {
			change *= accelerationMultiplier;
			velocity += change * delta;
			if(Math.signum(desiredVelocity - velocity) != Math.signum(change)){
				velocity = desiredVelocity;
			}
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

	@Override
	public void setDesiredVelocity(float desiredVelocity, float accelerationMultiplier, float maxVelocity) {
		this.desiredVelocity = desiredVelocity;
		this.accelerationMultiplier = accelerationMultiplier;
		this.maxVelocity = maxVelocity;
	}

    @Override
    public void setVelocity(float velocity, boolean resetOtherFields) {
        if(resetOtherFields){
            accelerationMultiplier = 1;
            maxVelocity = Math.abs(velocity);
        }
        this.desiredVelocity = velocity;
        this.velocity = velocity;
    }

    @Override
    public void setVelocity(float velocity) {
        setVelocity(velocity, true);
    }
}
