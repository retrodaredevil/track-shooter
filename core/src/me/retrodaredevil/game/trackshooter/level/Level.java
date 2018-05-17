package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.Updateable;

public interface Level extends Updateable {

	/**
	 *
	 * @return The level number starting at 1
	 */
	int getNumber();

	/**
	 *
	 * @return true if all the enemies from this level are killed, false otherwise
	 */
	boolean isDone();

	/**
	 * Should continue/begin the process of returning all enemies to their starting positions
	 * (position they should start at after the player dies)
	 *
	 * @return true if all the ships currently on screen have flown in and gone to their starting positions
	 */
	boolean resetAll();
}
