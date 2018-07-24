package me.retrodaredevil.game.trackshooter.entity.player;

import me.retrodaredevil.game.trackshooter.entity.Entity;

public interface Score {

	int getScore();
	int getLives();

	/**
	 * This should not be called in the player class and is used when an entity realizes the player has caused their
	 * death. ex: Bullet or crashing.
	 *  @param killed
	 * @param killerSource Usually the bullet that killed "killed" or the player if the player crashed
	 * @param points
	 */
	void onKill(Entity killed, Entity killerSource, int points);

	/**
	 * Should be called whenever the player
	 * @param numberOfShots The number of shots shot out, usually 1
	 */
	void onShot(int numberOfShots);

	/** @return The number of shots where a triple shot or double shot only counts as 1 */
	int getNumberShots();
	/** @return The total number of shots where a triple shot counts as 3, double counts as 2 etc.*/
	int getTotalNumberShots();

	/**
	 * Called when the player dies to an enemy or an enemy bullet
	 * @param other the entity causing the death
	 */
	void onDeath(Entity other);
}
