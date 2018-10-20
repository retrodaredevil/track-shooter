package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.Vector2;

import me.retrodaredevil.game.trackshooter.effect.EffectUtil;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.VelocityHandler;
import me.retrodaredevil.game.trackshooter.util.VelocitySetter;
import me.retrodaredevil.game.trackshooter.world.World;

public class TravelVelocityOnTrackMoveComponent extends SimpleMoveComponent
		implements OnTrackMoveComponent, TravelVelocitySetterMoveComponent {

	protected final Entity entity;

	private World lastWorld;

	private float distance = 0; // total distance

	private final VelocityHandler travelVelocityHandler = new VelocityHandler(Constants.TRAVEL_VELOCITY_SET_GOTO_DEADBAND);

	public TravelVelocityOnTrackMoveComponent(Entity entity){
		super(null, false, true);
		this.entity = entity;
	}

	@Override
	protected void onStart(World world) {
	}

	@Override
	public void onUpdate(float delta, World world) {
		lastWorld = world;
		travelVelocityHandler.update(delta);
		this.distance += delta * getTravelVelocity();

		updateLocation(world);
	}
	private void updateLocation(World world){
		entity.setLocation(world.getTrack().getDesiredLocation(distance));
	}

	@Override
	public Vector2 getCorrectLocation(World world) {
		return world.getTrack().getDesiredLocation(distance);
	}

	@Override
	protected void onEnd() {
	}

	@Override
	public void setDistanceOnTrack(float distance){
		this.distance = distance;
		if(lastWorld != null){
			updateLocation(lastWorld);
		}
	}
	@Override
	public float getDistanceOnTrack(){ return distance; }

	@Override
	public float getTravelVelocity(){ return travelVelocityHandler.getVelocity() * EffectUtil.getSpeedMultiplier(entity); }
	@Override
	public VelocitySetter getTravelVelocitySetter() {
		return travelVelocityHandler;
	}

}
