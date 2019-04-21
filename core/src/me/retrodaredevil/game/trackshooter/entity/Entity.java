package me.retrodaredevil.game.trackshooter.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.effect.Effect;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.Updateable;
import me.retrodaredevil.game.trackshooter.item.Item;
import me.retrodaredevil.game.trackshooter.level.CanLevelEnd;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.Collection;

public interface Entity extends Renderable, Updateable, CanLevelEnd {

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

	/**
	 * Note, you should NEVER edit the values of this hitbox unless you use HitboxUtil and you know what you are doing
	 * Also, the x and y values are probably not the actual location values
	 *
	 * @return The hitbox of the entity correctly positioned in the world coordinate space
	 */
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
	 * <p>
	 * Depending on the implementation, if this is removed this may return either true or false.
	 *
	 * @return true if the entity should be removed
	 */
	boolean shouldRemove();
	/**
	 * Called when the Entity is going to be removed
	 * <p>
	 * Should only be called by the World instance. This should be called after the entity has been removed
	 * <p>
	 * This should also call disposeRenderComponent()
	 */
	void afterRemove();
	/**
	 * Called when the Entity is going to be added
	 * <p>
	 * Should only be called by the World instance. This will be called before the Entity is added to the entities list
	 * <p>
	 * Should be overridden to reset RenderComponent and to possibly reset health/lives or one-way-flags if necessary
	 */
	void beforeSpawn();
	boolean isRemoved();
	/**
	 * Throughout this Entity's life, this should only return one value (this shouldn't change). Even
	 * though this is not enforced, everything else that calls this method will assume that is the case
	 * so by returning something different after the Entity is constructed, that may cause bugs.
	 *
	 * @return true if you are allowed to call setToRemove()
	 */
	boolean canSetToRemove();
	/**
	 * NOTE: Make sure to check if this is allowed with canSetToRemove() if it is not, this should throw an IllegalStateException
	 * <p>
	 * <p>
	 * Once this is called, it should make the next time (and every time after) shouldRemove() is called return true essentially removing this
	 * @throws IllegalStateException If canSetToRemove() == false or isRemoved() == true
	 */
	void setToRemove();

	/**
	 * This method should be used to react to being hit, it should not try to send messages to other. other should be
	 * used to determine what should happen to this instance.
	 * <p>
	 * For instance, bob collides with pete. bob.onHit(pete) and pete.onHit(bob) are called.
	 * pete should not try to kill bob, bob will determine if pete should kill bob.
	 * <p>
	 * Calls to this method are handled by some sort of collision handler which will use the CollisionIdentity
	 * to determine if two things should collide. see {@link CollisionIdentity} docs for more info
	 *
	 * @param other The other entity this has collided with
	 * @throws CannotHitException This is thrown when the caller has passed an entity that this cannot handle or
	 *                            doesn't know how to handle.
	 */
	void onHit(Entity other);
	/**
	 *
	 * @return The CollisionIdentity for this instance
	 */
	CollisionIdentity getCollisionIdentity();

	/** @return A Collection of effects that are active on the Entity. */
	Collection<Effect> getEffects();
	/** @return A Collection of effects of the given type that are active on the Entity OR null if there are none active */
	<T extends Effect> Collection<T> getEffects(Class<T> clazz);
	/**
	 * NOTE: Do not call this method in an Effect's update method as it is probably currently iterating over effects,
	 * if this functionality is needed in the future, change this comment.
	 * @param effect The effect to be added
	 */
	void addEffect(Effect effect);

	/** @return A Collection of items that this Entity has */
	Collection<Item> getItems();
	/** @return A Collection of items of the given type that this Entity has or null if there are none of that type. */
	<T extends Item> Collection<T> getItems(Class<T> clazz);
	/** @param item The item to be added */
	void addItem(Item item);

}
