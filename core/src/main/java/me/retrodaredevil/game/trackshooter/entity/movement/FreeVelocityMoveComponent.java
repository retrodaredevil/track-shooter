package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.Vector2;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.VectorVelocityHandler;
import me.retrodaredevil.game.trackshooter.util.VectorVelocitySetter;
import me.retrodaredevil.game.trackshooter.util.VelocityHandler;
import me.retrodaredevil.game.trackshooter.util.VelocitySetter;
import me.retrodaredevil.game.trackshooter.world.World;

public class FreeVelocityMoveComponent extends SimpleMoveComponent implements VectorVelocitySetterMoveComponent, RotationalVelocitySetterMoveComponent{

	private final VectorVelocityHandler vectorVelocityHandler = new VectorVelocityHandler(Constants.VECTOR_VELOCITY_SET_GOTO_DEADBAND);
	private final VelocityHandler rotationalVelocityHandler = new VelocityHandler(Constants.ROTATIONAL_VELOCITY_SET_GOTO_DEADBAND);
	private final Entity entity;

	public FreeVelocityMoveComponent(Entity entity){
		super(null, true, true);
		this.entity = entity;
	}

	@Override
	protected void onStart() {
	}

	@Override
	protected void onEnd() {
	}

	@Override
	protected void onUpdate(float delta) {
		vectorVelocityHandler.update(delta);
		rotationalVelocityHandler.update(delta);

		entity.setLocation(entity.getLocation().add(getVectorVelocity().scl(delta)));
	}

	@Override
	public VectorVelocitySetter getVectorVelocitySetter() {
        return vectorVelocityHandler;
	}

	@Override
	public Vector2 getVectorVelocity() {
        return vectorVelocityHandler.getVelocity();
	}

	@Override
	public VelocitySetter getRotationalVelocitySetter() {
        return rotationalVelocityHandler;
	}

	@Override
	public float getRotationalVelocity() {
        return rotationalVelocityHandler.getVelocity();
	}
}
