package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Entity extends Renderable, Updateable {

	/**
	 * @return The location of the entity. This should not be altered at all
	 */
	Vector2 getLocation();
	void setLocation(Vector2 location);

	Rectangle getHitbox();

	MoveComponent getMoveComponent();

}
