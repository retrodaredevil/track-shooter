package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.VelocityHandler;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.World;

public class PointTargetMoveComponent implements MoveComponent {
	private static final Vector2 temp = new Vector2();

	private VelocityHandler rotationalVelocityHandler = new VelocityHandler(Constants.ROTATIONAL_VELOCITY_SET_GOTO_DEADBAND);
	private Entity entity;
	private final Vector2 target;

	private float speed;
	private float rotationalSpeed;

	private final float rotationalAccelerationMultiplier;
	private final float maxRotationalVelocity;

	private float rotationalChange;

	public PointTargetMoveComponent(Entity entity, Vector2 target, float speed, float rotationalSpeed, float rotationalAccelerationMultiplier, float maxRotationalVelocity){
		this.entity = entity;
		this.target = target.cpy();
		this.speed = speed;
		this.rotationalSpeed = rotationalSpeed;
		this.rotationalAccelerationMultiplier = rotationalAccelerationMultiplier;
		this.maxRotationalVelocity = maxRotationalVelocity;
	}

	public void setTarget(Vector2 target){
		this.target.set(target);
	}

	/**
	 * Sets the target position to the track position of the entity plus trackDistanceToAdd. If entity's MoveComponent
	 * is not an OnTrackMoveComponent, this returns false and does nothing
	 *
	 * @param track The track to use
	 * @param entity The entity to use their track position
	 * @param trackDistanceToAdd The distance on the track to add
	 * @return true if it set the target successfully, false otherwise.
	 */
	public boolean setTarget(Track track, Entity entity, float trackDistanceToAdd){
		MoveComponent entityMoveComponent = entity.getMoveComponent();
		if(entityMoveComponent instanceof OnTrackMoveComponent){
			OnTrackMoveComponent trackMove = (OnTrackMoveComponent) entityMoveComponent;
			this.setTarget(track.getDesiredLocation(trackDistanceToAdd + trackMove.getDistance()));
			return true;
		}
		return false;
	}

	/**
	 *
	 * @return The amount this needs to change its angle by to get to its desired angle (in degrees)
	 */
	public float getRotationalChange(){
		return rotationalChange;
	}

	@Override
	public MoveComponent getNextComponent() {
		return this;
	}

	@Override
	public void update(float delta, World world) {
		float desiredAngle = temp.set(target).sub(entity.getLocation()).angle();
		float currentAngle = entity.getRotation();

		float change = desiredAngle - currentAngle;
		change %= 360;
		if(Math.abs(change) > 180){
			// 270 -> -90
			// -181 -> 179
			if(change < 0){
				change += 360;
			} else {
				change -= 360;
			}
		}
		this.rotationalChange = change;
		rotationalVelocityHandler.setDesiredRotationalVelocity(change, rotationalAccelerationMultiplier, maxRotationalVelocity);
		rotationalVelocityHandler.update(delta);
		entity.setRotation(entity.getRotation() + rotationalVelocityHandler.getVelocity() * delta * rotationalSpeed);

		float rotation = entity.getRotation();
		Vector2 velocity = new Vector2(MathUtils.cosDeg(rotation), MathUtils.sinDeg(rotation));
		velocity.scl(delta * speed);
		entity.setLocation(entity.getLocation().add(velocity));
	}

}
