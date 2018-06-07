package me.retrodaredevil.game.trackshooter.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.effect.Effect;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.Updateable;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.Collection;

public interface Entity extends Renderable, Updateable {

	/**
	 *
	 * @return The EntityController that is currently controlling the entity
	 */
	EntityController getEntityController();

	/** @param controller The EntityController to set to control the entity */
	void setEntityController(EntityController controller);

	/** @return A copy of the Entity's location*/
	Vector2 getLocation();
	/** @return The x value of the location */
	float getX();
	/** @return The y value of the location */
	float getY();
	void setLocation(Vector2 location);
	void setLocation(float x, float y);
	void setLocation(float x, float y, float rotation);
	void setLocation(Vector2 location, float rotation);
	/** NOTE: 90 is straight up, 0 is right, 180 is left, 270 is down. (How it should be)
	 * @return The rotation in degrees */
	float getRotation();
	/** @param rotation The rotation in degrees to set */
	void setRotation(float rotation);

	Rectangle getHitbox();

	/**
	 * Note when implementing: there shouldn't be any places in the code where getMoveComponent().update() is called so
	 * you must call MoveComponent#update() in your implementation of #update() (SimpleEntity handles this so you
	 * probably won't worry about it)
	 * @return The move component currently being used by this entity
	 */
	MoveComponent getMoveComponent();

	/**
	 * If this entity was spawned because of another entity, that entity will be returned. However, it may be null
	 * for most entities
	 * @return The entity that caused the spawning of this entity or null if there is none
	 */
	Entity getShooter();

	/**
	 * Note: This method may be called multiple times before the entity is actually removed.
	 *
	 * @param world The World the entity is being removed from
	 * @return true if the entity should be removed
	 */
	boolean shouldRemove(World world);
	/**
	 * Called when the Entity is going to be removed
	 *
	 * Should only be called by the World instance. This should be called after the entity has been removed
	 * @param world The world this entity will be removed from
	 *
	 */
	void afterRemove(World world);
	/**
	 * Called when the Entity is going to be added
	 * <p>
	 * Should only be called by the World instance. This will be called before the Entity is added to the entities list
	 * <p>
	 * Should be overridden to reset RenderComponent and to possibly reset health/lives or one-way-flags if necessary
	 * @param world The world to be added to
	 */
	void beforeSpawn(World world);
	boolean isRemoved();
	/**
	 *
	 * @return true if you are allowed to call setToRemove()
	 */
	boolean canSetToRemove();
	/**
	 * NOTE: Make sure to check if this is allowed with canSetToRemove() if it is not, this should throw an IllegalStateException
	 * <p>
	 * <p>
	 * Once this is called, it should make the next time (and every time after) shouldRemove() is called return true essentially removing this
	 */
	void setToRemove();

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
	/**
	 *
	 * @return The CollisionIdentity for this instance
	 */
	CollisionIdentity getCollisionIdentity();

	Collection<Effect> getEffects();
	/**
	 * NOTE: Do not call this method in an Effect's update method as it is probably currently iterating over effects,
	 * if this functionality is needed in the future, change this comment.
	 * @param effect The effect to be added
	 */
	void addEffect(Effect effect);
}
