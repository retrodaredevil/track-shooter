package me.retrodaredevil.game.trackshooter.entity;

public interface Enemy extends Entity {
	/**
	 * Should start the process of going to the starting position
	 */
	void goToStart();

	/**
	 * NOTE: This is only called when the LevelMode is in RESET
	 * <p>
	 * If this returns false, it means that the Enemy is either not going to go to its starting position,
	 * or that it has already returned to its starting position. Either way, when this returns false
	 * it means that the level can start
	 *
	 * @return true if the Enemy is trying to go to its starting position, false otherwise
	 */
	boolean isGoingToStart();


	/**
	 * Called when going into normal mode
	 */
	void goNormalMode();

}
