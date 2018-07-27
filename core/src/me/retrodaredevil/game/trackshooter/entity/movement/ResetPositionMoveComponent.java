package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.world.World;

public abstract class ResetPositionMoveComponent extends NestedComponentMoveComponent {

	protected final Entity entity;
	protected final Vector2 startingPosition = new Vector2();
	protected final float startingRotation;

	/**
	 *
	 * @param entity The entity that this is handling
	 * @param startingPosition The position to return to
	 * @param startingRotation The rotation to return to
	 */
	protected ResetPositionMoveComponent(Entity entity, Vector2 startingPosition, float startingRotation) {
		super(null, true, true); // this is designed to be recycled
		this.entity = entity;
		this.startingPosition.set(startingPosition);
		this.startingRotation = startingRotation;
	}

	@Override
	protected void onStart(World world) {
		super.onStart(world);

	}

	@Override
	protected void onEnd() {
		super.onEnd();
		if(endedPeacefully()) {
			entity.setLocation(startingPosition);
			entity.setRotation(startingRotation);
		}
	}

	@Override
	protected void onUpdate(float delta, World world) {
		super.onUpdate(delta, world);
	}

	@Override
	public boolean isDone() {
		Vector2 location = entity.getLocation();
		done = MathUtil.minDistance(startingRotation, entity.getRotation(), 360) < 1 && location.epsilonEquals(startingPosition);
		return super.isDone();
	}
}
