package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.effect.EffectUtil;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.util.VelocityHandler;
import me.retrodaredevil.game.trackshooter.util.VelocitySetter;
import me.retrodaredevil.game.trackshooter.world.World;

public class SmoothTravelMoveComponent extends SimpleMoveComponent implements VelocityTargetPositionMoveComponent, TravelVelocitySetterMoveComponent,
		RotationalVelocityMoveComponent, RotationalVelocityMultiplierSetterMoveComponent {
	private static final Vector2 temp = new Vector2();

	private final Entity entity;
	private final Vector2 target = new Vector2();

	private final VelocityHandler speedHandler = new VelocityHandler(0);
	private float rotationalSpeedMultiplier;

	private float rotationalChange; // just information calculated, not actually used for anything super meaningful

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
		speedHandler.setVelocity(speed);
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

	@Override
	public float getTargetPositionX() {
		return target.x;
	}

	@Override
	public float getTargetPositionY() {
		return target.y;
	}

	@Override
	public Vector2 getTargetPosition(){
		return target.cpy();
	}

	@Override
	public void setTargetPosition(Vector2 target){
		this.target.set(target);
	}

	@Override
	public void setTargetPosition(float x, float y) {
		this.target.set(x, y);
	}

//	/**
//	 * Sets the target position to the track position of the entity plus trackDistanceToAdd. If entity's MoveComponent
//	 * is not an TravelRotateVelocityOnTrackMoveComponent, this returns false and does nothing
//	 *
//	 * @param track The track to use
//	 * @param entity The entity to use their track position
//	 * @param trackDistanceToAdd The distance on the track to add
//	 * @return true if it set the target successfully, false otherwise.
//	 */
//	@Deprecated
//	public boolean setTarget(Track track, Entity entity, float trackDistanceToAdd){
//		MoveComponent entityMoveComponent = entity.getMoveComponent();
//		if(entityMoveComponent instanceof OnTrackMoveComponent){
//			OnTrackMoveComponent trackMove = (OnTrackMoveComponent) entityMoveComponent;
//			this.setTargetPosition(track.getDesiredLocation(trackDistanceToAdd + trackMove.getDistanceOnTrack()));
//			return true;
//		}
//		return false;
//	}

	@Override
	public float getRotationalChange(){
		return rotationalChange;
	}

	@Override
	public float getTravelVelocity() {
		return speedHandler.getVelocity() * EffectUtil.getSpeedMultiplier(entity);
	}

	@Override
	public VelocitySetter getTravelVelocitySetter() {
		return speedHandler;
	}

	@Override
	public float getRotationalVelocity() {
		return rotationalChange * rotationalSpeedMultiplier;
	}
	@Override
	public void setRotationalMultiplier(float multiplier){
		this.rotationalSpeedMultiplier = multiplier;
	}

	@Override
	protected void onStart(World world) {
	}
	@Override
	protected void onEnd(){
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



}
