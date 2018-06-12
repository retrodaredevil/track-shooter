package me.retrodaredevil.game.trackshooter.item;

import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * By implementing this class, if this item is in the player's getItems(), the activatePowerup method will be
 * called when the player wishes to activate their powerup normally adding an effect
 */
public interface PowerupActivateListenerItem extends Item {
	/**
	 * @param world The World instance
	 * @param player The Player that has this item and is trying to activate it
	 * @return true if this did something such as activate a powerup, false otherwise
	 */
	boolean activatePowerup(World world, Player player);
}
