package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.enemies.SnakePart;
import me.retrodaredevil.game.trackshooter.world.World;

public class SnakeFollowMoveComponent extends SimpleMoveComponent{

	private SnakePart entity;
	private SnakePart inFront;
	private final float correctDistanceMultiplier;

	/**
	 *
	 * @param entity The entity that this MoveComponent is attached to
	 * @param inFront The entity that entity will "follow." It helps if this entity's MoveComponent implements RotationalVelocityMoveComponent
	 *                so this class is able to tell the rate at which is it turning and also helps if it implements TravelVelocityMoveComponent
	 * @param correctDistanceMultiplier The multiplier for the distance that is used to make following look more circle/arc like. If you raise this too high,
	 *                        the tail will go crazy, if this is too small, the tail will close in on it's target.<br/><br/>
	 *                        It's recommended that you make this half of followDistance.
	 */
	public SnakeFollowMoveComponent(SnakePart entity, SnakePart inFront, float correctDistanceMultiplier) {
		super(null, false, false);
		this.entity = entity;
		this.inFront = inFront;
		this.correctDistanceMultiplier = correctDistanceMultiplier;
	}

	@Override
	protected void onStart(World world) {
	}

	@Override
	protected void onEnd() {
	}

	@Override
	protected void onUpdate(float delta, World world) {
		final float currentRotation = entity.getRotation();

//		float velocity = 0; // units per second
//		MoveComponent frontMove = inFront.getHead().getMoveComponent();
//		if(frontMove instanceof TravelVelocityMoveComponent){
//			velocity = ((TravelVelocityMoveComponent) frontMove).getTravelVelocity();
//		}
		float followDistance = entity.getFollowDistance();
		final float moveAmount = followDistance * correctDistanceMultiplier;

		Vector2 distanceAway = inFront.getLocation().sub(entity.getX(), entity.getY());
		float angle = distanceAway.angle(); // the angle to go towards
		float backDistance = followDistance + moveAmount;

		Vector2 location = inFront.getLocation().sub(MathUtils.cosDeg(angle) * backDistance, MathUtils.sinDeg(angle) * backDistance);
		location.add(
				MathUtils.cosDeg(currentRotation) * moveAmount,
				MathUtils.sinDeg(currentRotation) * moveAmount
		);
		entity.setLocation(location, angle);
	}

}
