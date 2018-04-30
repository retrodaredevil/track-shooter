package me.retrodaredevil.game.trackshooter.entity.movement;

public interface RotationalVelocityMoveComponent extends MoveComponent {
	/**
	 *  @param desiredRotationalVelocity The desired rotational velocity in degrees per second
	 *
	 */
	void setDesiredRotationalVelocity(float desiredRotationalVelocity);
}
