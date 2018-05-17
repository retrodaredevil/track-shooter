package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.world.World;

public class DirectTravelMoveComponent extends SimpleMoveComponent {
	private static final Vector2 temp = new Vector2();

	private Entity entity;
	private Vector2 target;
	private float speed;
	private Float desiredRotation;
	private float turnVelocity;

	/**
	 *
	 * @param entity The entity this MoveComponent controls
	 * @param target The target location to go to. Feel free to alter after constructor is called. (calls .cpy())
	 * @param desiredRotation The desired rotation to rotate to
	 * @param turnVelocity The turn velocity in degrees per second. Must be positive
	 */
	protected DirectTravelMoveComponent(Entity entity, Vector2 target, float speed, Float desiredRotation, float turnVelocity,
	                                    MoveComponent moveComponent, boolean canHaveNext, boolean canRecycle) {
		super(moveComponent, canHaveNext, canRecycle);
		this.entity = entity;
		this.target = target.cpy();
		this.speed = speed;
		this.desiredRotation = desiredRotation;
		this.turnVelocity = turnVelocity;
	}
	public DirectTravelMoveComponent(Entity entity, Vector2 target, float speed, Float desiredRotation, float turnVelocity){
		this(entity, target, speed, desiredRotation, turnVelocity, null, false, true);
	}


	@Override
	protected void onStart(World world) {
	}

	@Override
	protected void onEnd() {
	}

	@Override
	protected void onUpdate(float delta, World world) {
		if(desiredRotation != null){
			float change = MathUtil.minChange(entity.getRotation(), desiredRotation, 360);
			if(turnVelocity == 0 || Math.abs(change) <= turnVelocity){
				entity.setRotation(desiredRotation);
			} else {
				entity.setRotation(entity.getRotation() + (delta * Math.signum(change) * turnVelocity));
			}
		}
		Vector2 location = entity.getLocation();
		Vector2 away = temp.set(target).sub(location); // target - current
		away.nor();
		away.scl(speed * delta);
		entity.setLocation(away.add(location));

	}
}
