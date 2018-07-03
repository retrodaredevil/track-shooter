package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.effect.EffectUtil;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.World;

public class SmoothTravelMoveComponent extends SimpleMoveComponent implements TravelVelocityMoveComponent, RotationalVelocityMoveComponent{
	private static final Vector2 temp = new Vector2();

	private Entity entity;
	private final Vector2 target = new Vector2();

	private float speed;
	private float rotationalSpeedMultiplier;

	private float rotationalChange;

	/**
	 *
	 * @param entity The entity that this move component will control
	 * @param initialTarget The point that entity will target until setTarget() is called. Feel free to mutate it after passing it.
	 * @param speed The speed in units/second
	 * @param rotationalSpeedMultiplier The rotational speed multiplier in rotations/second
	 */
	public SmoothTravelMoveComponent(Entity entity, Vector2 initialTarget, float speed, float rotationalSpeedMultiplier,
	                                 MoveComponent nextComponent, boolean canHaveNext, boolean canRecycle){
		super(nextComponent, canHaveNext, canRecycle);
		this.entity = entity;
		this.target.set(initialTarget);
		this.speed = speed;
		this.rotationalSpeedMultiplier = rotationalSpeedMultiplier;
	}

	/**
	 * Creates a SmoothTravelMoveComponent that is not able to have a next component and is able to be recycled
	 * @param entity The entity that this move component will control
	 * @param initialTarget The point that entity will target until setTarget() is called
	 * @param speed The speed in units/second
	 * @param rotationalSpeedMultiplier The rotational speed multiplier in rotations/second
	 */
	public SmoothTravelMoveComponent(Entity entity, Vector2 initialTarget, float speed, float rotationalSpeedMultiplier){
		this(entity, initialTarget, speed, rotationalSpeedMultiplier,
				null, false, true);
	}

	/**
	 * NOTE: The returned value is able to be mutated but should NEVER be mutated (clone it with .cpy() if you want to alter it)
	 * @return The target Vector2 that this instance uses.
	 */
	public Vector2 getTarget(){
		return target;
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
	protected void onStart(World world) {
	}


	@Override
	protected void onUpdate(float delta, World world) {
		float desiredAngle = temp.set(target).sub(entity.getLocation()).angle();
		float currentAngle = entity.getRotation();

		rotationalChange = MathUtil.minChange(desiredAngle, currentAngle, 360);
		entity.setRotation(entity.getRotation() + rotationalChange * delta * rotationalSpeedMultiplier);

		float rotation = entity.getRotation();
		Vector2 velocity = new Vector2(MathUtils.cosDeg(rotation), MathUtils.sinDeg(rotation));
		velocity.scl(delta * getTravelVelocity());
		entity.setLocation(entity.getLocation().add(velocity));
	}
	@Override
	protected void onEnd(){

	}

	@Override
	public float getTravelVelocity() {
		return speed * EffectUtil.getSpeedMultiplier(entity);
	}
	public void setVelocity(float speed){
		this.speed = speed;
	}

	@Override
	public float getRotationalVelocity() {
		return rotationalChange * rotationalSpeedMultiplier;
	}
	public void setRotationalSpeedMultiplier(float multiplier){
		this.rotationalSpeedMultiplier = multiplier;
	}


}
