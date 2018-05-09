package me.retrodaredevil.game.trackshooter.entity;

public interface Enemy extends Entity {
	/**
	 * This should continue/begin the process of going back to this Enemy's starting position
	 * @return
	 */
	boolean goToStart();
}
