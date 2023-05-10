package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.world.World;

public class DirectTravelMoveComponent extends SimpleMoveComponent {
	private static final Vector2 temp = new Vector2();

	private final Entity entity;
	private final Vector2 target = new Vector2();
	private final float speed;
	private final Float desiredRotation;
	private final float turnVelocity;

	/**
	 * @param entity The entity this MoveComponent controls
	 * @param target The target location to go to. Feel free to alter after constructor is called. (calls .cpy())
	 * @param desiredRotation The desired rotation to rotate to
	 * @param turnVelocity The turn velocity in degrees per second. Must be positive or 0. If 0, then will instantly turn to desiredRotation
	 */
	protected DirectTravelMoveComponent(Entity entity, Vector2 target, float speed, Float desiredRotation, float turnVelocity,
	                                    MoveComponent moveComponent, boolean canHaveNext, boolean canRecycle) {
		super(moveComponent, canHaveNext, canRecycle);
		this.entity = entity;
		this.target.set(target);
		this.speed = speed;
		this.desiredRotation = desiredRotation;
		this.turnVelocity = turnVelocity;

	}
	public DirectTravelMoveComponent(Entity entity, Vector2 target, float speed, Float desiredRotation, float turnVelocity){
		this(entity, target, speed, desiredRotation, turnVelocity, null, false, true);
	}


	@Override
	protected void onStart() {
	}

	@Override
	protected void onEnd() {
	}

	@Override
	protected void onUpdate(float delta) {
		if(desiredRotation != null){
			float change = MathUtil.minChange(desiredRotation, entity.getRotation(), 360);
			if(turnVelocity == 0 || Math.abs(change) <= turnVelocity * delta){
				entity.setRotation(desiredRotation);
			} else {
				entity.setRotation(entity.getRotation() + (delta * Math.signum(change) * turnVelocity));
			}
		}
		final float moveAmount = speed * delta;

		float x = entity.getX(), y = entity.getY();
		Vector2 away = temp.set(target).sub(x, y); // target - current
		if(away.len2() < moveAmount * moveAmount){ // we don't want to move past the target
			entity.setLocation(target);
			return;
		}
		away.nor();
		away.scl(moveAmount);
		entity.setLocation(away.add(x, y));

	}
}
