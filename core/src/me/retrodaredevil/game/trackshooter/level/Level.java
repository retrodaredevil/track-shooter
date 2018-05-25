package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.Updateable;
import me.retrodaredevil.game.trackshooter.world.Track;

public interface Level extends Updateable {

	/**
	 *
	 * @return The level number starting at 1
	 */
	int getNumber();

	Track getTrack();

	/**
	 *
	 * @return true if all the enemies from this level are killed, false otherwise
	 */
	boolean isDone();

	/**
	 * Sets the mode of the level
	 * @param mode The mode to set
	 */
	void setMode(LevelMode mode);

	/**
	 *
	 * @return The current LevelMode. Will never be null
	 */
	LevelMode getMode();

	/**
	 *
	 * @return The time in milliseconds that represents how long the current mode has been going on. If getMode() == null,
	 *         this should throw an IllegalStateException
	 */
	long getModeTime();

	/**
	 * Note: If this level hasn't started yet, this should return -1
	 *
	 * @return The total amount of time in milliseconds since this level began
	 */
	long getLevelTime();
}
