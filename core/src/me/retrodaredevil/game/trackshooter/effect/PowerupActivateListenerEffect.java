package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.world.World;

/**
 * By implementing this class, if this effect is in the player's getEffects(), the activatePowerup method will be
 * called when the player wishes to activate their powerup
 */
public interface PowerupActivateListenerEffect extends Effect {
	/**
	 * @param world The World instance
	 * @return true if this did something such as activate a powerup, false otherwise
	 */
	boolean activatePowerup(World world);
}
