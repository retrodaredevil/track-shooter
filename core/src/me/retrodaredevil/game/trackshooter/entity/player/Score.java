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
	 *
	 * @param other the entity causing the death
	 */
	void onDeath(Entity other);
}
