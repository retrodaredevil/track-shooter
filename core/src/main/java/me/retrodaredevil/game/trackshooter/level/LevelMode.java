package me.retrodaredevil.game.trackshooter.level;

public enum LevelMode {
	/**
	 * Is the mode usually after the player dies so the enemy ships can reset
	 */
	RESET,
	/**
	 * Is used after the level has exited RESET and can be used to indicate that the level should do nothing
	 */
	STANDBY,
	/**
	 * The main mode of the level.
	 */
	NORMAL
}
