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

	private final World world;
	protected final Entity entity;

	private float distance = 0; // total distance

	private final VelocityHandler travelVelocityHandler = new VelocityHandler(Constants.TRAVEL_VELOCITY_SET_GOTO_DEADBAND);

	public TravelVelocityOnTrackMoveComponent(World world, Entity entity){
		super(null, false, true);
		this.world = world;
		this.entity = entity;
	}

	@Override
	protected void onStart() {
	}

	@Override
	public void onUpdate(float delta) {
		travelVelocityHandler.update(delta);
		this.distance += delta * getTravelVelocity();

		updateLocation();
	}
	private void updateLocation(){
		entity.setLocation(world.getTrack().getDesiredLocation(distance));
	}

	@Override
	public Vector2 getCorrectLocation() {
		return world.getTrack().getDesiredLocation(distance);
	}

	@Override
	protected void onEnd() {
	}

	@Override
	public void setDistanceOnTrack(float distance){
		this.distance = distance;
		updateLocation(); // TODO This wasn't designed to mutate the entity's position. Maybe there's a way around this?
		// I believe the reason for this was because by doing this, it fixes a rare bug. But I haven't tested what happens if this is removed yet.
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
