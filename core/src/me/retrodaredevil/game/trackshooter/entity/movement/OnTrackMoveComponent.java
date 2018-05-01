package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.World;

public class OnTrackMoveComponent implements MoveComponent, RotationalVelocityMoveComponent {
	private static final float ROTATIONAL_VELOCITY_SET_ZERO_DEADBAND = 10;

	private Entity entity;

	private float distance = 0; // total distance
	private float velocity = 0; // per second

	private float rotationalVelocity = 0; // degrees per second
	private float desiredRotationalVelocity = 0;
	private float rotationalAccelerationMultiplier = 1;
	private float maxRotationalVelocity = 0;

	public OnTrackMoveComponent(Entity entity){
		this.entity = entity;
	}

	@Override
	public void update(float delta, World world) {
		this.distance += delta * this.velocity;

		entity.setLocation(world.getTrack().getDesiredLocation(distance));

		float change = desiredRotationalVelocity - rotationalVelocity;
//		Gdx.app.debug("change", "" + change);
		if(Math.abs(change) < ROTATIONAL_VELOCITY_SET_ZERO_DEADBAND){
			rotationalVelocity = desiredRotationalVelocity;
		} else {
			change *= rotationalAccelerationMultiplier;
			rotationalVelocity += change * delta;
		}

		if(rotationalVelocity > maxRotationalVelocity){
			rotationalVelocity = maxRotationalVelocity;
		} else if(rotationalVelocity < -maxRotationalVelocity){
			rotationalVelocity = -maxRotationalVelocity;
		}

		entity.setRotation(entity.getRotation() + rotationalVelocity * delta);
	}

	@Override
	public MoveComponent getNextComponent() {
		return this;
	}

	public void pointToCenter(){
		float angle = Vector2.Zero.cpy().sub(entity.getLocation()).angle();
		entity.setRotation(angle);
	}
	public void setDistance(float distance, boolean zeroVelocity){
		if(zeroVelocity){
			velocity = 0;
		}
		this.distance = distance;
	}

	/**
	 * calls setDistance(distance, true) (with zeroVelocity = true)
	 * @param distance
	 */
	public void setDistance(float distance){ this.setDistance(distance, true);}
	public float getDistance(){ return distance; }
	public void setVelocity(float velocity){ this.velocity = velocity; }
	public float getVelocity(){ return velocity; }

	@Override
	public void setDesiredRotationalVelocity(float desiredRotationalVelocity, float rotationalAccelerationMultiplier, float maxRotationalVelocity) {
		this.desiredRotationalVelocity = desiredRotationalVelocity;
		this.rotationalAccelerationMultiplier = rotationalAccelerationMultiplier;
		this.maxRotationalVelocity = maxRotationalVelocity;
	}
}
