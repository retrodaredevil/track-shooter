package me.retrodaredevil.game.trackshooter.entity.movement;

/**
 * By implementing this interface, you allow other places in the code to check to see if a MoveComponent is an instanceof
 * this class so that place in the code is able to see the rate of rotation. If your MoveComponent can, it is recommended to
 * implement this class if possible (and if it's reasonable)
 */
public interface RotationalVelocityMoveComponent extends MoveComponent {
	/**
	 * @return The amount of degrees this will rotate per second
	 */
	float getRotationalVelocity();
}
