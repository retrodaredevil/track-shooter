package me.retrodaredevil.game.trackshooter;

/**
 * The idea of this class is to make dealing with collisions easier and less prone to errors. With this alone, you
 * shouldn't have to use instanceof for most things (although you should use that in some cases.) However, just because
 * you can use someIdentity == CollisionIdentity.X doesn't mean you should. If needed create getter methods to access
 * instance variables if you need them.
 */
public enum CollisionIdentity {
	UNKNOWN(false, false, false),
	FRIENDLY(true, true, false, true, false),
	ENEMY(true, false, true),
	FRIENDLY_PROJECTILE(false, true, false),
	ENEMY_PROJECTILE(false, false, true),
	POWERUP(false, false, true)
	;

	private final boolean collidesWithEnemyBullets;
	private final boolean collidesWithPowerup;
	private final boolean collidesWithFriendlyBullets;
	private final boolean collidesWithEnemy;
	private final boolean collidesWithFriendly;

	private final boolean canCollide;

	CollisionIdentity(boolean collidesWithEnemyBullets, boolean collidesWithPowerup,
	                  boolean collidesWithFriendlyBullets, boolean collidesWithEnemy, boolean collidesWithFriendly){
		this.collidesWithEnemyBullets = collidesWithEnemyBullets;
		this.collidesWithPowerup = collidesWithPowerup;
		this.collidesWithFriendlyBullets = collidesWithFriendlyBullets;
		this.collidesWithEnemy = collidesWithEnemy;
		this.collidesWithFriendly = collidesWithFriendly;

		this.canCollide = collidesWithEnemyBullets || collidesWithPowerup || collidesWithFriendlyBullets || collidesWithEnemy || collidesWithFriendly;
	}
	CollisionIdentity(boolean collidesWithFriendlyBullets, boolean collidesWithEnemy, boolean collidesWithFriendly){
		this(false, false, collidesWithFriendlyBullets, collidesWithEnemy, collidesWithFriendly);
	}

//	boolean collidesWithEnemyBullets(){
//		return collidesWithEnemyBullets;
//	}
//	boolean collidesWithFriendlyBullets(){
//		return collidesWithFriendlyBullets;
//	}
//
//	boolean collidesWithEnemy(){
//		return collidesWithEnemy;
//	}
//	boolean collidesWithFriendly(){
//		return collidesWithFriendly;
//	}

	/**
	 *
	 * @return true if this identity can collide with anything. If it is not possible for this to collide, returns false
	 */
	boolean canCollide(){
		return canCollide;
	}

	/**
	 *
	 * @param collisionIdentity The CollisionIdentity of the entity to test if it can collide with
	 * @return true if it can collide with the passed CollisionIdentity
	 */
	boolean collidesWith(CollisionIdentity collisionIdentity){
		return (collidesWithFriendlyBullets && collisionIdentity == FRIENDLY_PROJECTILE) ||
				(collidesWithEnemyBullets && collisionIdentity == ENEMY_PROJECTILE) ||
				(collidesWithPowerup && collisionIdentity == POWERUP) ||
				(collidesWithEnemy && collisionIdentity == ENEMY) ||
				(collidesWithFriendly && collisionIdentity == FRIENDLY);
	}
}
