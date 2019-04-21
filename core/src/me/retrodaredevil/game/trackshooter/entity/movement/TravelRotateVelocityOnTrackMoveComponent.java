package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.VelocityHandler;
import me.retrodaredevil.game.trackshooter.util.VelocitySetter;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * This class is the basic implementation of OnTrackMoveComponent along with 4 other interfaces. (hell yeah abstraction)
 * This can also get and set both travel and rotational velocities.
 * <p>
 * You should not use instanceof on this class. It is preferred to use instanceof on the interfaces
 * that this class implements
 */
public class TravelRotateVelocityOnTrackMoveComponent extends TravelVelocityOnTrackMoveComponent
		implements RotationalVelocitySetterMoveComponent {

	private final VelocityHandler rotationalVelocityHandler = new VelocityHandler(Constants.ROTATIONAL_VELOCITY_SET_GOTO_DEADBAND);

	public TravelRotateVelocityOnTrackMoveComponent(World world, Entity entity) {
		super(world, entity);
	}

	@Override
	public void onUpdate(float delta) {
		super.onUpdate(delta);

		rotationalVelocityHandler.update(delta);
		entity.setRotation(entity.getRotation() + rotationalVelocityHandler.getVelocity() * delta);
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
