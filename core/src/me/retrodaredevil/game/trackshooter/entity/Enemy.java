package me.retrodaredevil.game.trackshooter.entity;

public interface Enemy extends Entity {
	/**
	 * Should start the process of going to the starting position
	 */
	void goToStart();

	/**
	 *
	 * @return true if the Enemy is trying to go to its starting position, false otherwise
	 */
	boolean isGoingToStart();


	void goNormalMode();

}
