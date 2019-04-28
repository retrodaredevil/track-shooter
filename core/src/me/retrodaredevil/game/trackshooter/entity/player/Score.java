package me.retrodaredevil.game.trackshooter.entity.player;

import me.retrodaredevil.game.trackshooter.entity.Entity;

/**
 * Represents the score of an Entity (usually a Player) and also keeps track of their lives.
 */
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
	 * Called when onKill() cannot be called because we are getting points for a reason like a cargo
	 * ship of something.
	 * @param points The number of points to add to the score
	 */
	void onScore(int points);

	/**
	 * NOTE: Unlike onKill(), this is going to be called from projectiles. HOWEVER, this is not enforced but is recommended. <br/>
	 * Should be called whenever a player's bullet hits an entity. Not necessarily if it kills it.
	 * @param hitEntity The entity (usually an enemy) that the player hit
	 * @param playerProjectile The projectile where getShooter() is the player. (This is normally a Bullet object)
	 */
	void onBulletHit(Entity hitEntity, Entity playerProjectile);

	/**
	 * Should be called whenever the player
	 * @param numberOfShots The number of shots shot out, usually 1
	 */
	void onShot(int numberOfShots);

	/** @return The number of shots where a triple shot or double shot only counts as 1 */
	int getNumberShots();
	/** @return The total number of shots where a triple shot counts as 3, double counts as 2 etc.*/
	int getTotalNumberShots();

	int getNumberShotsHit();

	/**
	 * Called when the player dies to an enemy or an enemy bullet
	 * @param other the entity causing the death
	 */
	void onDeath(Entity other);

	/**
	 * Should be called when the player loses all their lives or if the game is ended early
	 */
	void onGameEnd();
	void printOut();
}
