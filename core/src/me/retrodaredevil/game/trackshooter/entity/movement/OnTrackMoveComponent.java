package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.effect.EffectUtil;
import me.retrodaredevil.game.trackshooter.effect.SpeedEffect;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.VelocityHandler;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.List;

public class OnTrackMoveComponent extends SimpleMoveComponent implements AccelerationRotationalVelocityMoveComponent, TravelVelocityMoveComponent {
	private Entity entity;

	private float distance = 0; // total distance
	private float velocity = 0; // per second

	private VelocityHandler rotationalVelocityHandler = new VelocityHandler(Constants.ROTATIONAL_VELOCITY_SET_GOTO_DEADBAND);



	public OnTrackMoveComponent(Entity entity){
		super(null, false, true);
		this.entity = entity;
	}

	@Override
	protected void onStart(World world) {
	}

	@Override
	public void onUpdate(float delta, World world) {
		this.distance += delta * getTravelVelocity();

		entity.setLocation(world.getTrack().getDesiredLocation(distance));

		rotationalVelocityHandler.update(delta);
		entity.setRotation(entity.getRotation() + rotationalVelocityHandler.getVelocity() * delta);
	}

	@Override
	protected void onEnd() {
	}

	public void pointToCenter(){
//		float angle = Vector2.Zero.cpy().sub(entity.getLocation()).angle();
		float angle = entity.getLocation().scl(-1).angle();
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
	@Override
	public float getTravelVelocity(){ return velocity * EffectUtil.getSpeedMultiplier(entity); }

	@Override
	public void setDesiredRotationalVelocity(float desiredRotationalVelocity, float rotationalAccelerationMultiplier, float maxRotationalVelocity) {
		rotationalVelocityHandler.setDesiredRotationalVelocity(desiredRotationalVelocity, rotationalAccelerationMultiplier, maxRotationalVelocity);
	}

	@Override
	public float getRotationalVelocity() {
		return rotationalVelocityHandler.getVelocity();
	}

}
