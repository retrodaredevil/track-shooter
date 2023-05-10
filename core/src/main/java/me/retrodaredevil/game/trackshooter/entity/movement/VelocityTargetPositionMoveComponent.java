package me.retrodaredevil.game.trackshooter.entity.movement;

/**
 * Represents a MoveComponent that has a target and is trying to get there using it's velocity and
 * the direction it's traveling to is changing to be able to  get closer to the target. The method
 * getRotationalChange() returns the amount in degrees it needs to correct itself in order to move
 * toward the target.
 * <p>
 * You should only use instanceof on this interface if you need the method(s) is contains
 */
public interface VelocityTargetPositionMoveComponent extends TargetPositionMoveComponent, TravelVelocityMoveComponent {

	/**
	 * As of right now, this method is only used on the rendering side of things.
	 * @return The amount this needs to change its angle by to get to its desired angle (in degrees)
	 */
	float getRotationalChange();
}
