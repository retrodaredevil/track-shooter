package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.VelocityHandler;
import me.retrodaredevil.game.trackshooter.world.World;

public class OnTrackMoveComponent implements MoveComponent, RotationalVelocityMoveComponent {
	private Entity entity;

	private float distance = 0; // total distance
	private float velocity = 0; // per second

	private VelocityHandler rotationalVelocityHandler = new VelocityHandler(Constants.ROTATIONAL_VELOCITY_SET_GOTO_DEADBAND);


	public OnTrackMoveComponent(Entity entity){
		this.entity = entity;
	}

	@Override
	public void update(float delta, World world) {
		this.distance += delta * this.velocity;

		entity.setLocation(world.getTrack().getDesiredLocation(distance));

		rotationalVelocityHandler.update(delta);
		entity.setRotation(entity.getRotation() + rotationalVelocityHandler.getVelocity() * delta);
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
		rotationalVelocityHandler.setDesiredRotationalVelocity(desiredRotationalVelocity, rotationalAccelerationMultiplier, maxRotationalVelocity);
	}
}
