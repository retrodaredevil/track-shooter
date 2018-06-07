package me.retrodaredevil.game.trackshooter;

import java.util.Arrays;
import java.util.List;

/**
 * The idea of this class is to make dealing with collisions easier and less prone to errors. With this alone, you
 * shouldn't have to use instanceof for most things (although you should use that in some cases.) However, just because
 * you can use someIdentity == CollisionIdentity.X doesn't mean you should. If needed create getter methods to access
 * instance variables if you need them.
 * <p>
 * <p>
 * Also note that adding values to this enum may have side effects unless other parts of the code are updated. Yes,
 * it isn't the greatest design but be aware - similar problems like using instanceof, except improved.
 */
public enum CollisionIdentity {

	// Special identities (referenced in the code more, harder to change)
	UNKNOWN(),
	FRIENDLY(), // friendlies don't trigger collisions
	ENEMY(FRIENDLY), // enemy triggers a enemy friendly collision

	// Regular identities (referenced in the code less, easier to change and add to)
	FRIENDLY_PROJECTILE(ENEMY),
	ENEMY_PROJECTILE(FRIENDLY),
	POWERUP(FRIENDLY)
	;

	private final List<CollisionIdentity> triggersWith;


	CollisionIdentity(CollisionIdentity... triggersWith){
		this.triggersWith = Arrays.asList(triggersWith);
	}

	/**
	 *
	 * @return true if this identity can collide with anything. If it is not possible for this to collide, returns false
	 */
	boolean canCollide(){
		return this != UNKNOWN;
	}

	/**
	 * NOTE: If a call with a given parameters returns true, then calling it the other way around returns false:<p>
	 * if a.triggersCollision(b) then b.triggersCollision(a) == false
	 *
	 * @param collisionIdentity The CollisionIdentity of the entity to test to see if this colliding with it triggers a collision
	 * @return true if this colliding with collisionIdentity triggers a collision
	 */
	boolean triggersCollision(CollisionIdentity collisionIdentity){
		return triggersWith.contains(collisionIdentity);
	}
	/**
	 *
	 * @param collisionIdentity The CollisionIdentity of the entity to test if it can collide with
	 * @return true if it can collide with the passed CollisionIdentity
	 */
	boolean collidesWith(CollisionIdentity collisionIdentity){
		return this.triggersCollision(collisionIdentity) || collisionIdentity.triggersCollision(this);
	}
}
