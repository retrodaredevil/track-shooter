package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Entity extends Renderable, Updateable, Controlable {

	/**
	 * @return The location of the entity. This should not be altered at all
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

}
