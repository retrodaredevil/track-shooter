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
}
