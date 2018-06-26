package me.retrodaredevil.game.trackshooter.effect;

/**
 * An Effect that if active, will affect the speed of the entity.
 * <p><p>
 * By implementing this class you can alter the speed of the Entity
 */
public interface SpeedEffect extends Effect{
	/**
	 *
	 * @return The number to multiply the speed by. (1 means no change)
	 */
	float getSpeedMultiplier();
}
