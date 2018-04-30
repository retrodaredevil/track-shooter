package me.retrodaredevil.game.trackshooter.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.Updateable;
import me.retrodaredevil.game.trackshooter.world.World;

public interface Entity extends Renderable, Updateable {

	/**
	 *
	 * @return The EntityController that is currently controlling the entity
	 */
	EntityController getEntityController();

	/**
	 *
	 * @param controller The EntityController to set to control the entity
	 */
	void setEntityController(EntityController controller);

	/**
	 * @return The location of the entity. Feel free to alter it as you please
	 */
	Vector2 getLocation();
	void setLocation(Vector2 location);

	/**
	 * NOTE: 90 is straight up, 0 is right
	 *
	 * @return The rotation in degrees
	 */
	float getRotation();

	/**
	 * @param rotation The rotation in degrees to set
	 */
	void setRotation(float rotation);

	Rectangle getHitbox();

	/**
	 * Note when implementing: there shouldn't be any places in the code where getMoveComponent().update() is called so
	 * you must call MoveComponent#update() in your implementation of #update()
	 * @return The move component currently being used by this entity
	 */
	MoveComponent getMoveComponent();

	boolean shouldRemove(World world);

	/**
	 * Called when the Entity is going to be removed
	 *
	 * Should only be called by the World instance. This should be called after the entity has been removed
	 * @param world
	 */
	void afterRemove(World world);

	boolean isRemoved();

}
