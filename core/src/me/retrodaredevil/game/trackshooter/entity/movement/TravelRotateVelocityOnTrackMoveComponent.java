package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.effect.EffectUtil;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.VelocityHandler;
import me.retrodaredevil.game.trackshooter.util.VelocitySetter;
import me.retrodaredevil.game.trackshooter.world.World;

public class TravelRotateVelocityOnTrackMoveComponent extends SimpleMoveComponent
		implements OnTrackMoveComponent,
		RotationalVelocityMoveComponent, RotationalVelocitySetter,
		TravelVelocityMoveComponent, TravelVelocitySetter {
	private final Entity entity;

	private float distance = 0; // total distance

	private final VelocityHandler travelVelocityHandler = new VelocityHandler(Constants.TRAVEL_VELOCITY_SET_GOTO_DEADBAND);
	private final VelocityHandler rotationalVelocityHandler = new VelocityHandler(Constants.ROTATIONAL_VELOCITY_SET_GOTO_DEADBAND);



	public TravelRotateVelocityOnTrackMoveComponent(Entity entity){
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

	@Override
	public void setDistanceOnTrack(float distance){ this.distance = distance;}
	@Override
	public float getDistanceOnTrack(){ return distance; }

	@Override
	public float getTravelVelocity(){ return travelVelocityHandler.getVelocity() * EffectUtil.getSpeedMultiplier(entity); }
	@Override
	public VelocitySetter getTravelVelocitySetter() {
		return travelVelocityHandler;
	}

	@Override
	public float getRotationalVelocity(){
		return rotationalVelocityHandler.getVelocity();
	}
	@Override
	public VelocitySetter getRotationalVelocitySetter() {
		return rotationalVelocityHandler;
	}
}
