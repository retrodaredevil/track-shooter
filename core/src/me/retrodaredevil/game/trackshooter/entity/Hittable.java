package me.retrodaredevil.game.trackshooter.entity;

import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.world.World;

public interface Hittable extends Entity {
	/**
	 * This method should be used to react to being hit, it should not try to send messages to other. other should be
	 * used to determine what should happen to this instance.
	 * <p>
	 * For instance, bob collides with pete. bob.onHit(pete) and pete.onHit(bob) are called.
	 * pete should not try to kill bob, bob will determine if pete should kill bob.
	 * <p>
	 * This should also not be called for every Entity that collides with this. Implementations of this should be kept
	 * simple and this method will be called intelligently by a CollisionHandler. ex: two bullets cannot collide so
	 * CollisionHandler should not call Bullet#onHit(otherBullet)
	 *
	 * @param world The World object
	 * @param other The other entity this has collided with
	 * @throws CannotHitException This is thrown when the caller has passed an entity that this cannot handle or
	 *                            doesn't know how to handle.
	 */
	void onHit(World world, Entity other) throws CannotHitException;
}
