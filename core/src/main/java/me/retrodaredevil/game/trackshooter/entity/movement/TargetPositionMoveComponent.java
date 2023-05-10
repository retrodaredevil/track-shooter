package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.Vector2;

public interface TargetPositionMoveComponent extends MoveComponent {
	/**
	 * @return A modifiable Vector2 with the coordinates of the target position
	 */
	Vector2 getTargetPosition();
	float getTargetPositionX();
	float getTargetPositionY();

	void setTargetPosition(Vector2 position);
	void setTargetPosition(float x, float y);
}
